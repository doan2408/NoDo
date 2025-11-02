package com.example.nodotest.Service;

import com.example.nodotest.Dto.Pagination.PagedResponse;
import com.example.nodotest.Dto.Pagination.PaginationInfo;
import com.example.nodotest.Dto.Request.ProductImageRequest;
import com.example.nodotest.Dto.Request.ProductRequest;
import com.example.nodotest.Dto.Response.ApiResponse;
import com.example.nodotest.Dto.Response.ProductImageResponse;
import com.example.nodotest.Dto.Response.ProductResponse;
import com.example.nodotest.Entity.*;
import com.example.nodotest.Exception.ErrorProduct.ProductCategoryNotFoundException;
import com.example.nodotest.Exception.ErrorProduct.ProductCodeExistsException;
import com.example.nodotest.Exception.ErrorProduct.ProductNotFoundException;
import com.example.nodotest.Exception.FileSizeExceededException;
import com.example.nodotest.Exception.InvalidFileException;
import com.example.nodotest.Mapper.ProductImageMapper;
import com.example.nodotest.Mapper.ProductMapper;
import com.example.nodotest.Repository.CategoryRepository;
import com.example.nodotest.Repository.ProductImageRepository;
import com.example.nodotest.Repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final MessageSource messageSource;
    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          MessageSource messageSource, ProductMapper productMapper, ProductImageMapper productImageMapper,
                          ProductImageRepository productImageRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.messageSource = messageSource;
        this.productMapper = productMapper;
        this.productImageMapper = productImageMapper;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
    }

    @Value("${image.upload.product}")
    private String imageFolderPath;

    // Giới hạn kích thước ảnh (10MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    // Loại ảnh được phép
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    // Extension hợp lệ
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".webp"
    );

    // Bai 6

    @Transactional
    public ApiResponse<ProductResponse> createProduct(ProductRequest req, Locale locale) {
        // trim spaces
        sanitizeRequest(req);

        // check product code exists
        checkProductCodeExists(req.getProductCode(), locale);

        // get category list from id with status 1
        List<Category> categories = categoryRepository.getListIdWithStatus(req.getCategoryIds(), "1");

        if (categories.isEmpty()) {
            String msg = messageSource.getMessage("product.category.not.found", null, locale);
            throw new ProductCategoryNotFoundException(msg);
        }

        // map productReq to entity
        Product product = productMapper.toEntity(req);

        // assign category to product (cascade save)
        for (Category category : categories) {
            product.addCategory(category);
        }

        // save product first to get ID
        Product savedProduct = productRepository.save(product);

        // Track saved file paths for cleanup on error
        List<String> savedFilePaths = new ArrayList<>();

        // Save images if provided
        if (req.getImages() != null && !req.getImages().isEmpty()) {
            try {
                List<ProductImage> images = saveImages(req.getImages(), savedProduct, locale, savedFilePaths);
                images.forEach(savedProduct::addImage);
                log.debug("Added {} images to product: {}", images.size(), req.getProductCode());
//                productImageRepository.saveAll(images);
            } catch (FileSizeExceededException | InvalidFileException e) {
                // Cleanup newly saved files before rollback
                cleanupFiles(savedFilePaths);

                log.error("Validation failed for product images: {}", req.getProductCode(), e);
                throw e;

            } catch (IOException e) {
                // Cleanup newly saved files before rollback
                cleanupFiles(savedFilePaths);

                String error = messageSource.getMessage("error.product.image.save", null, locale);
                log.error("Failed to save images for product: {}", req.getProductCode(), e);
                throw new RuntimeException(error, e);

            } catch (Exception e) {
                // Cleanup newly saved files for any unexpected errors
                cleanupFiles(savedFilePaths);

                String error = messageSource.getMessage("error.product.image.save", null, locale);
                log.error("Unexpected error saving images for product: {}", req.getProductCode(), e);
                throw new RuntimeException(error, e);
            }
        }

        ProductResponse productResponse = productMapper.toResponse(savedProduct);

        return new ApiResponse<>(
                200,
                messageSource.getMessage("product.create.success", null, locale),
                productResponse
        );
    }

    private void sanitizeRequest(ProductRequest req) {
        // Trim name
        if (req.getName() != null) {
            req.setName(req.getName().trim());
        }

        // Trim và uppercase product code
        if (req.getProductCode() != null) {
            req.setProductCode(req.getProductCode().trim().toUpperCase());
        }

        // Round price to 2 decimal places
        if (req.getPrice() != null) {
            req.setPrice(Math.round(req.getPrice() * 100.0) / 100.0);
        }

        // Clean up categoryIds: remove null values
        if (req.getCategoryIds() != null) {
            req.getCategoryIds().removeIf(Objects::isNull);
        }

        // Clean up images: remove null and empty files
        if (req.getImages() != null) {
            req.getImages().removeIf(file -> file == null || file.isEmpty());
        }
    }

    private void checkProductCodeExists(String productCode, Locale locale) {
        if (productRepository.existsByProductCode(productCode)) {
            String msg = messageSource.getMessage("product.code.exists", null, locale);
            throw new ProductCodeExistsException(msg);
        }
    }

    private List<ProductImage> saveImages(List<MultipartFile> images, Product product, Locale locale, List<String> savedFilePaths) throws IOException {
        List<ProductImage> productImages = new ArrayList<>();

        // Create folder if not exists
        File folder = new File(imageFolderPath);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Cannot create image folder");
        }

        for (MultipartFile image : images) {
            if (image.isEmpty()) continue;
            // Validate size
            if (image.getSize() > MAX_FILE_SIZE) {
                String msg = messageSource.getMessage("error.product.image.size", null, locale);
                throw new FileSizeExceededException(msg);
            }

            // Validate content type
            String contentType = image.getContentType();
            if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
                String msg = messageSource.getMessage("error.product.image.type", null, locale);
                throw new InvalidFileException(msg);
            }

            // Validate extension
            String originalName = image.getOriginalFilename();
            if (originalName == null || originalName.isEmpty()) continue;

            String extension = getFileExtension(originalName);
            if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                String msg = messageSource.getMessage("error.product.image.extension", null, locale);
                throw new InvalidFileException(msg);
            }

            // Save file
            String uuid = UUID.randomUUID().toString();
            String newFileName = uuid + extension;
            Path destination = Paths.get(imageFolderPath, newFileName);
            try {
                Files.copy(image.getInputStream(), destination);

                // Track saved file path for potential cleanup
                savedFilePaths.add(destination.toString());

                log.debug("Image saved: {}", newFileName);
            } catch (IOException e) {
                log.error("Failed to save image: {}", newFileName, e);
                throw new IOException("Failed to save image: " + originalName);
            }

            // Map to ProductImage entity
            ProductImageRequest imageRequest = new ProductImageRequest(
                    originalName, uuid, destination.toString(), "1", null
            );
            ProductImage productImage = productImageMapper.toEntity(imageRequest);
            productImage.setProduct(product);

            productImages.add(productImage);
        }

        return productImages;
    }

    private void cleanupFiles(List<String> filePaths) {
        if (filePaths == null || filePaths.isEmpty()) {
            return;
        }

        log.info("Starting cleanup of {} files", filePaths.size());

        for (String filePath : filePaths) {
            try {
                Path path = Paths.get(filePath);
                boolean deleted = Files.deleteIfExists(path);

                if (deleted) {
                    log.info("Successfully cleaned up file: {}", filePath);
                } else {
                    log.warn("File not found during cleanup: {}", filePath);
                }

            } catch (IOException e) {
                log.error("Failed to cleanup file: {}", filePath, e);
                // Continue with other files even if one fails
            }
        }

        log.info("Cleanup completed");
    }

    private String getFileExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        return idx == -1 ? "" : filename.substring(idx);
    }

    // bai 7

    // Batch lấy Category mapping
    private List<ProductCategory> findProductCategoryByBatches(List<Long> productIds, int batchSize) {
        List<ProductCategory> result = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i += batchSize) {
            int end = Math.min(i + batchSize, productIds.size());
            List<Long> batch = new ArrayList<>(productIds.subList(i, end));
            if (!batch.isEmpty()) {
                List<ProductCategory> productCategories =
                        productRepository.findByProductIdsAndStatus(batch, "1", "1");
                result.addAll(productCategories);
            }
        }
        return result;
    }

    // Batch lấy ảnh sản phẩm
    private List<ProductImage> findProductImagesByBatches(List<Long> productIds, int batchSize) {
        List<ProductImage> allImages = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i += batchSize) {
            int end = Math.min(i + batchSize, productIds.size());
            List<Long> batch = new ArrayList<>(productIds.subList(i, end));
            if (!batch.isEmpty()) {
                List<ProductImage> images =
                        productImageRepository.findByProductIdsAndStatus(batch, "1");
                allImages.addAll(images);
            }
        }
        return allImages;
    }

    private ProductResponse buildProductResponse(
            Product product,
            Map<Long, List<Category>> categoryMap,
            Map<Long, List<ProductImage>> imageMap
    ) {

        ProductResponse response = productMapper.toResponse(product);

        // Map categories to comma-separated string
        List<Category> categories = categoryMap.getOrDefault(product.getId(), Collections.emptyList());
        String categoryNames = categories.stream()
                .map(Category::getName)
                .collect(Collectors.joining(", "));
        response.setCategories(categoryNames);

        // Map images using ProductImageMapper
        List<ProductImage> images = imageMap.getOrDefault(product.getId(), Collections.emptyList());
        List<ProductImageResponse> imageResponses = productImageMapper.toResponses(images);
        response.setImages(imageResponses);

        return response;
    }


    public PagedResponse<ProductResponse> getProducts(
            String name,
            String code,
            Date startDate,
            Date endDate,
            Long categoryId,
            Pageable pageable
    ) {
        name = sanitizeSearchParam(name);
        code = sanitizeSearchParam(code);

        // Query phân trang
        Page<Product> productsPage = productRepository.getAllProductsByStatus(
                "1", name, code, startDate, endDate, categoryId, pageable
        );

        List<Product> products = productsPage.getContent();

        // Check empty
        if (products.isEmpty()) {
            return new PagedResponse<>(
                    Collections.emptyList(),
                    new PaginationInfo(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            0, 0, false, false
                    )
            );
        }

        // Lấy danh sách product IDs
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .toList();

        // Batch query categories
        List<ProductCategory> productCategories =
                findProductCategoryByBatches(productIds, 1000);

        // Group categories by product ID
        Map<Long, List<Category>> categoryMap = productCategories.stream()
                .collect(Collectors.groupingBy(
                        pc -> pc.getProduct().getId(),
                        Collectors.mapping(ProductCategory::getCategory, Collectors.toList())
                ));

        // Batch query images
        List<ProductImage> productImages =
                findProductImagesByBatches(productIds, 1000);

        // Group images by product ID
        Map<Long, List<ProductImage>> imageMap = productImages.stream()
                .collect(Collectors.groupingBy(pi -> pi.getProduct().getId()));

        // Map to response với custom logic
        List<ProductResponse> responseList = products.stream()
                .map(product -> buildProductResponse(product, categoryMap, imageMap))
                .toList();

        // Build pagination info
        PaginationInfo paginationInfo = new PaginationInfo(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                productsPage.getTotalElements(),
                productsPage.getTotalPages(),
                productsPage.hasNext(),
                productsPage.hasPrevious()
        );

        return new PagedResponse<>(responseList, paginationInfo);
    }

    private String sanitizeSearchParam(String param) {
        if (param == null) {
            return null;
        }

        param = param.trim();
        if (param.isEmpty()) {
            return null;
        }

        // Escape SQL LIKE wildcards
        param = param.replace("\\", "\\\\");  // \ → \\
        param = param.replace("%", "\\%");    // % → \%
        param = param.replace("_", "\\_");    // _ → \_

        // Giới hạn độ dài
        if (param.length() > 200) {
            param = param.substring(0, 200);
        }

        return param;
    }

    // cau 8
    public ByteArrayInputStream exportProductsToExcel(
            String name,
            String code,
            Date startDate,
            Date endDate,
            Long categoryId
    ) throws IOException {
        // 1. Query ALL products matching filters (không phân trang)
        List<Product> products = productRepository.findAllProductsForExport(
                "1", name, code, startDate, endDate, categoryId
        );

        if (products.isEmpty()) {
            return createEmptyExcel();
        }

        // 2. Lấy categories cho products
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .toList();

        List<ProductCategory> productCategories =
                findProductCategoryByBatches(productIds, 1000);

        Map<Long, String> categoryMap = productCategories.stream()
                .collect(Collectors.groupingBy(
                        pc -> pc.getProduct().getId(),
                        Collectors.mapping(
                                pc -> pc.getCategory().getName(),
                                Collectors.joining(", ")
                        )
                ));

        // 3. Generate Excel
        return generateExcel(products, categoryMap);
    }


    // Generate Excel workbook

    private ByteArrayInputStream generateExcel(
            List<Product> products,
            Map<Long, String> categoryMap
    ) throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Products");

            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Create date style
            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy hh:mm"));

            // Create currency style
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Tên sản phẩm", "Mã sản phẩm", "Giá",
                    "Số lượng", "Ngày tạo", "Ngày sửa", "Danh mục"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Fill data rows
            int rowIdx = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowIdx++);

                // ID
                row.createCell(0).setCellValue(product.getId());

                // Tên sản phẩm
                row.createCell(1).setCellValue(product.getName());

                // Mã sản phẩm
                row.createCell(2).setCellValue(product.getProductCode());

                // Giá
                Cell priceCell = row.createCell(3);
                priceCell.setCellValue(product.getPrice());
                priceCell.setCellStyle(currencyStyle);

                // Số lượng
                row.createCell(4).setCellValue(product.getQuantity());

                // Ngày tạo
                Cell createdDateCell = row.createCell(5);
                if (product.getCreatedDate() != null) {
                    createdDateCell.setCellValue(product.getCreatedDate());
                    createdDateCell.setCellStyle(dateStyle);
                }

                // Ngày sửa
                Cell modifiedDateCell = row.createCell(6);
                if (product.getModifiedDate() != null) {
                    modifiedDateCell.setCellValue(product.getModifiedDate());
                    modifiedDateCell.setCellStyle(dateStyle);
                }

                // Danh mục
                String categories = categoryMap.getOrDefault(product.getId(), "");
                row.createCell(7).setCellValue(categories);
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    /**
     * Create header cell style
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(font);

        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        return headerStyle;
    }

    /**
     * Create empty Excel when no data
     */
    private ByteArrayInputStream createEmptyExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Products");
            Row headerRow = sheet.createRow(0);

            String[] columns = {"ID", "Tên sản phẩm", "Mã sản phẩm", "Giá",
                    "Số lượng", "Ngày tạo", "Ngày sửa", "Danh mục"};

            for (int i = 0; i < columns.length; i++) {
                headerRow.createCell(i).setCellValue(columns[i]);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }


    // bai 9

    @Transactional
    public ApiResponse<ProductResponse> updateProduct(
            Long id,
            ProductRequest req,
            Locale locale
    ) {
        // 1. Kiểm tra product tồn tại với status = 1
        Product product = productRepository.findByIdAndStatus(id, "1")
                .orElseThrow(() -> {
                    String msg = messageSource.getMessage("product.not.found", null, locale);
                    return new ProductNotFoundException(msg);
                });

        // Check productCode if changed
        if (!Objects.equals(product.getProductCode(), req.getProductCode())) {
            checkProductCodeExists(req.getProductCode(), locale);
        }

        // Validate categories tồn tại với status = 1
        List<Category> newCategories = categoryRepository.getListIdWithStatus(req.getCategoryIds(), "1");
        if (newCategories.isEmpty()) {
            String msg = messageSource.getMessage("product.category.not.found", null, locale);
            throw new ProductCategoryNotFoundException(msg);
        }

        // Update basic fields
        productMapper.updateProductFromRequest(req, product);

        // Tracking for rollback
        Map<Long, String> modifiedCategories = new HashMap<>();
        List<String> savedFilePaths = new ArrayList<>();
        List<ProductImage> softDeletedImages = new ArrayList<>();

        try {
            // Update categories with soft delete logic
            updateProductCategories(product, req.getCategoryIds(), modifiedCategories);

            // Update images if provided (soft delete old, insert new)
            if (req.getImages() != null && !req.getImages().isEmpty()) {
                updateProductImages(product, req.getImages(), locale, savedFilePaths, softDeletedImages);
            }

        } catch (FileSizeExceededException | InvalidFileException e) {
            // Rollback all changes
            cleanupFiles(savedFilePaths);
            restoreSoftDeletedProductImages(softDeletedImages);
            rollbackCategoryChanges(product, modifiedCategories);

            log.error("Validation failed for product update: {}", id, e);
            throw e;

        } catch (IOException e) {
            // Rollback all changes
            cleanupFiles(savedFilePaths);
            restoreSoftDeletedProductImages(softDeletedImages);
            rollbackCategoryChanges(product, modifiedCategories);

            String error = messageSource.getMessage("error.product.image.save", null, locale);
            log.error("Failed to save images for product: {}", id, e);
            throw new RuntimeException(error, e);

        } catch (Exception e) {
            // Rollback all changes
            cleanupFiles(savedFilePaths);
            restoreSoftDeletedProductImages(softDeletedImages);
            rollbackCategoryChanges(product, modifiedCategories);

            String error = messageSource.getMessage("general.error", null, locale);
            log.error("Unexpected error updating product: {}", id, e);
            throw new RuntimeException(error, e);
        }

        // Save final changes
        Product savedProduct = productRepository.save(product);

        ProductResponse productResponse = productMapper.toResponse(savedProduct);

        return new ApiResponse<>(
                200,
                messageSource.getMessage("product.update.success", null, locale),
                productResponse
        );
    }

    /**
     * Update product categories with rollback support
     * - Soft deletes removed categories
     * - Restores previously deleted categories
     * - Adds new categories
     */
    private void updateProductCategories(
            Product product,
            List<Long> newCategoryIds,
            Map<Long, String> modifiedCategories
    ) {
        // Lấy tất cả ProductCategory hiện tại (kể cả status = 0)
        List<ProductCategory> existingPCs = productRepository
                .findAllByProductId(product.getId());

        // Map categoryId -> ProductCategory
        Map<Long, ProductCategory> existingMap = existingPCs.stream()
                .collect(Collectors.toMap(
                        pc -> pc.getCategory().getId(),
                        pc -> pc
                ));

        Date now = new Date(System.currentTimeMillis());
        Set<Long> newCategoryIdSet = new HashSet<>(newCategoryIds);

        // Xử lý các liên kết CŨ
        for (ProductCategory existingPC : existingPCs) {
            Long categoryId = existingPC.getCategory().getId();
            String currentStatus = existingPC.getCategory().getStatus();

            if (newCategoryIdSet.contains(categoryId)) {
                // Category vẫn được giữ lại
                if ("0".equals(currentStatus)) {
                    // Track original status before change
                    modifiedCategories.put(categoryId, "0");

                    // Trước bị xóa mềm, đổi lại thành 1
                    existingPC.getCategory().setStatus("1");
                    existingPC.setModifiedDate(now);

                    log.debug("Restored category {} for product {}", categoryId, product.getId());
                }
            } else {
                // Category không còn trong danh sách mới
                if ("1".equals(currentStatus)) {
                    // Track original status before change
                    modifiedCategories.put(categoryId, "1");

                    // Soft delete
                    existingPC.getCategory().setStatus("0");
                    existingPC.setModifiedDate(now);

                    log.debug("Soft deleted category {} for product {}", categoryId, product.getId());
                }
            }
        }

        // Xử lý các new liên kết
        for (Long newCategoryId : newCategoryIds) {
            if (!existingMap.containsKey(newCategoryId)) {
                // Track as new category (use null to indicate it's new)
                modifiedCategories.put(newCategoryId, null);

                // Tạo liên kết mới
                Category category = categoryRepository.findById(newCategoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found: " + newCategoryId));

                ProductCategory newPC = new ProductCategory();
                newPC.setProduct(product);
                newPC.setCategory(category);
                newPC.setCreatedDate(now);
                newPC.setModifiedDate(now);

                product.getProductCategories().add(newPC);

                log.debug("Added new category {} for product {}", newCategoryId, product.getId());
            }
        }
    }

    // Update product images with rollback support
    private void updateProductImages(
            Product product,
            List<MultipartFile> newImages,
            Locale locale,
            List<String> savedFilePaths,
            List<ProductImage> softDeletedImages
    ) throws IOException {

        Date now = new Date(System.currentTimeMillis());

        // Lấy tất cả ảnh active hiện tại (status = 1) TRƯỚC KHI soft delete
        // Dùng filter thay vì query mới
        List<ProductImage> activeImages = product.getProductImages().stream()
                .filter(img -> "1".equals(img.getStatus()))
                .collect(Collectors.toList());

        // Track để restore nếu cần
        softDeletedImages.addAll(activeImages);

        // Soft delete all old images
        productImageRepository.updateStatusByProductId(product.getId(), "0", now);
        log.debug("Soft deleted {} images for product: {}", activeImages.size(), product.getId());

        // Save new images
        List<ProductImage> images = saveImages(newImages, product, locale, savedFilePaths);

        images.forEach(img -> {
            img.setStatus("1");
            img.setCreatedDate(now);
            img.setModifiedDate(now);
            product.getProductImages().add(img);
        });

        log.debug("Added {} new images for product: {}", images.size(), product.getId());
    }

    // Rollback category changes
    private void rollbackCategoryChanges(Product product, Map<Long, String> modifiedCategories) {
        if (modifiedCategories == null || modifiedCategories.isEmpty()) {
            return;
        }

        log.info("Rolling back {} category changes for product: {}",
                modifiedCategories.size(), product.getId());

        Date now = new Date(System.currentTimeMillis());

        // Dùng lại data có sẵn từ product.getProductCategories()
        List<ProductCategory> existingPCs = productRepository
                .findAllByProductId(product.getId());

        Map<Long, ProductCategory> pcMap = existingPCs.stream()
                .collect(Collectors.toMap(
                        pc -> pc.getCategory().getId(),
                        pc -> pc
                ));

        for (Map.Entry<Long, String> entry : modifiedCategories.entrySet()) {
            Long categoryId = entry.getKey();
            String originalStatus = entry.getValue();

            try {
                if (originalStatus == null) {
                    // This was a NEW category, remove it
                    ProductCategory pc = pcMap.get(categoryId);
                    if (pc != null) {
                        product.getProductCategories().remove(pc);
                        log.debug("Removed new category {} from product", categoryId);
                    }
                } else {
                    // This was a MODIFIED category, restore original status
                    ProductCategory pc = pcMap.get(categoryId);
                    if (pc != null) {
                        pc.getCategory().setStatus(originalStatus);
                        pc.setModifiedDate(now);
                        log.debug("Restored category {} to status: {}", categoryId, originalStatus);
                    }
                }
            } catch (Exception e) {
                log.error("Failed to rollback category {}: {}", categoryId, e.getMessage());
            }
        }

        log.info("Category rollback completed");
    }

    // Restore soft deleted product images
    private void restoreSoftDeletedProductImages(List<ProductImage> softDeletedImages) {
        if (softDeletedImages == null || softDeletedImages.isEmpty()) {
            return;
        }

        log.info("Restoring {} soft deleted product images", softDeletedImages.size());

        Date now = new Date(System.currentTimeMillis());

        for (ProductImage image : softDeletedImages) {
            try {
                image.setStatus("1");
                image.setModifiedDate(now);
                productImageRepository.save(image);

                log.debug("Restored product image: {}", image.getUuid());
            } catch (Exception e) {
                log.error("Failed to restore product image {}: {}", image.getUuid(), e.getMessage());
            }
        }

        log.info("Product image restoration completed");
    }

    // cau 10
    public String softDeleteProduct(Long id, Locale locale) {
        Optional<Product> productOptional = productRepository.findByIdAndStatus(id, "1");
        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException("product.notfound");
        }
        Product productExists = productOptional.get();
        productExists.setStatus("0");
        productExists.setModifiedDate(new java.sql.Date(System.currentTimeMillis()));
        productRepository.save(productExists);
        return messageSource.getMessage("delete.success", null, locale);
    }


}
