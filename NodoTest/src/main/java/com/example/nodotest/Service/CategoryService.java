package com.example.nodotest.Service;

import com.example.nodotest.Dto.Pagination.PagedResponse;
import com.example.nodotest.Dto.Pagination.PaginationInfo;
import com.example.nodotest.Dto.Request.CategoryImageRequest;
import com.example.nodotest.Dto.Request.CategoryRequest;
import com.example.nodotest.Dto.Response.ApiResponse;
import com.example.nodotest.Dto.Response.CategoryResponse;
import com.example.nodotest.Entity.Category;
import com.example.nodotest.Entity.CategoryImage;
import com.example.nodotest.Exception.CategoryCodeExistsException;
import com.example.nodotest.Exception.CategoryNotFoundException;
import com.example.nodotest.Exception.ErrorExcel.ExcelExportException;
import com.example.nodotest.Exception.ErrorExcel.ExcelRowLimitExceededException;
import com.example.nodotest.Exception.ErrorExcel.InvalidDateRangeException;
import com.example.nodotest.Exception.ErrorExcel.NoDataToExportException;
import com.example.nodotest.Exception.FileSizeExceededException;
import com.example.nodotest.Exception.InvalidFileException;
import com.example.nodotest.Mapper.CategoryImageMapper;
import com.example.nodotest.Mapper.CategoryMapper;
import com.example.nodotest.Repository.CategoryImageRepository;
import com.example.nodotest.Repository.CategoryRepository;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryImageRepository categoryImageRepository;
    private final CategoryMapper categoryMapper;
    private final MessageSource messageSource;
    private final CategoryImageMapper categoryImageMapper;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryMapper categoryMapper,
                           MessageSource messageSource,
                           CategoryImageMapper categoryImageMapper,
                           CategoryImageRepository categoryImageRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.messageSource = messageSource;
        this.categoryImageMapper = categoryImageMapper;
        this.categoryImageRepository = categoryImageRepository;
    }

    // Bai 1

    @Value("${image.upload.dir}")
    private String imageFolderPath;

    // Giới hạn kích thước ảnh (10MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    // Các định dạng ảnh được phép
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    // Các extension được phép
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".webp"
    );

    @Transactional
    public ApiResponse<CategoryResponse> createCategory(CategoryRequest requestDTO, Locale locale) {

        // Check category code exists
        checkCategoryCodeExists(requestDTO.getCategoryCode(), locale);

        // Map DTO to Entity
        Category category = categoryMapper.toCategory(requestDTO);

        // Save category first để có ID
        Category savedCategory = categoryRepository.save(category);

        List<String> savedFilePaths = new ArrayList<>();

        // Save images if provided
        if (requestDTO.getImages() != null && requestDTO.getImages().toArray().length > 0) {
            try {
                List<CategoryImage> categoryImages = saveImages(requestDTO.getImages(), savedCategory, locale, savedFilePaths);
                // Add images to category
                categoryImages.forEach(savedCategory::addImage);
                categoryImageRepository.saveAll(categoryImages);
            } catch (IOException e) {
                // Cleanup all saved files before transaction rollback
                cleanupFiles(savedFilePaths);

                String errorMessage = messageSource.getMessage("error.category.image.save", null, locale);
                log.error("Failed to save images for category: {}", requestDTO.getCategoryCode(), e);
                throw new RuntimeException(errorMessage, e);

            } catch (FileSizeExceededException | InvalidFileException e) {
                // Cleanup all saved files before transaction rollback
                cleanupFiles(savedFilePaths);

                log.error("Validation failed for category images: {}", requestDTO.getCategoryCode(), e);
                throw e;

            } catch (Exception e) {
                // Cleanup all saved files for any unexpected errors
                cleanupFiles(savedFilePaths);

                String errorMessage = messageSource.getMessage("error.category.image.save", null, locale);
                log.error("Unexpected error saving images for category: {}", requestDTO.getCategoryCode(), e);
                throw new RuntimeException(errorMessage, e);
            }
        }

        CategoryResponse response = categoryMapper.toCategoryResponse(savedCategory);

        return new ApiResponse<>(
                200,
                messageSource.getMessage("category.create.success", null, locale),
                response
        );
    }

    private void checkCategoryCodeExists(String categoryCode, Locale locale) {
        if (categoryRepository.existsByCategoryCode(categoryCode)) {
            String errorMessage = messageSource.getMessage("error.category.exists", null, locale);
            throw new CategoryCodeExistsException(errorMessage);
        }
    }

    private List<CategoryImage> saveImages(List<MultipartFile> images,
                                             Category category,
                                             Locale locale,
                                             List<String> savedFilePaths
    ) throws IOException {
        List<CategoryImage> categoryImages = new ArrayList<>();

        // Create directory if not exists
        File directory = new File(imageFolderPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new IOException("Cannot create upload directory");
            }
        }

        for (MultipartFile image : images) {
            // Skip empty files
            if (image.isEmpty()) {
                log.warn("Skipping empty image file");
                continue;
            }

            // Validate file size
            if (image.getSize() > MAX_FILE_SIZE) {
                String errorMessage = messageSource.getMessage("error.category.image.size", null, locale);
                throw new FileSizeExceededException(errorMessage);
            }

            // Validate file type
            String contentType = image.getContentType();
            if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
                String errorMessage = messageSource.getMessage("error.category.image.type", null, locale);
                throw new InvalidFileException(errorMessage);
            }

            // Validate file extension
            String originalFilename = image.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                log.warn("Image has no filename, skipping");
                continue;
            }

            String extension = getFileExtension(originalFilename);
            if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                String errorMessage = messageSource.getMessage("error.category.image.extension", null, locale);
                throw new InvalidFileException(errorMessage);
            }

            // Generate unique filename
            String uuid = UUID.randomUUID().toString();
            String newFileName = uuid + extension;
            String imagePath = imageFolderPath + newFileName;

            // Save file to disk
            Path destinationPath = Paths.get(imagePath);
            try {
                Files.copy(image.getInputStream(), destinationPath);
                savedFilePaths.add(imagePath);
                log.debug("Image saved: {}", newFileName);
            } catch (IOException e) {
                log.error("Failed to save image: {}", newFileName, e);
                throw new IOException("Failed to save image: " + originalFilename);
            }

            // Create CategoryImage entity
            CategoryImageRequest imageRequestDTO = new CategoryImageRequest(originalFilename, uuid, imagePath, "1");

            CategoryImage categoryImage = categoryImageMapper.toCategoryImage(imageRequestDTO);
            categoryImage.setCategory(category);

            // Save to database
//            CategoryImage savedImage = imageRepository.save(categoryImage);

            categoryImages.add(categoryImage);
        }

        return categoryImages;
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

    // check image type
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex);
    }

    // Bai 2: find product type

    private List<CategoryImage> findCategoryImagesByBatches(List<Long> categoryIds, int batchSize) {
        List<CategoryImage> allImages = new ArrayList<>();
        for (int i = 0; i < categoryIds.size(); i += batchSize) {
            int end = Math.min(i + batchSize, categoryIds.size());
            List<Long> batch = new ArrayList<>(categoryIds.subList(i, end));
            if (!batch.isEmpty()) {
                // lấy tất categoryImage theo status = 1 và list id category
                List<CategoryImage> images = categoryImageRepository.findAllByStatus("1", batch);
                allImages.addAll(images);
            }
        }
        return allImages;
    }

    public PagedResponse<CategoryResponse> getCategories(String name, String categoryCode, Date startDate, Date endDate, Pageable pageable) {
        name = sanitizeSearchParam(name);
        categoryCode = sanitizeSearchParam(categoryCode);
        validateDateRange(startDate, endDate);

        Page<Category> categories = categoryRepository.getAllCategoriesByStatus("1", name, categoryCode, startDate, endDate, pageable);
        List<Category> categoryList = new ArrayList<>(categories.getContent());

        // Early return if not exist data
        if (categoryList.isEmpty()) {
            return new PagedResponse<>(
                    Collections.emptyList(),
                    new PaginationInfo(pageable.getPageNumber(), pageable.getPageSize(), 0, 0, false, false)
            );
        }
        // get list id of category
        List<Long> categoryIds = categoryList.stream()
                .map(Category::getId).toList();

//        List<CategoryImage> categoryImages = categoryImageRepository.findAllByStatus("1", categoryIds);

        // gom 1000 -> chia nhỏ query
        List<CategoryImage> categoryImages = findCategoryImagesByBatches(categoryIds, 100);

        // group categoryImage by categoryId
        Map<Long, List<CategoryImage>> categoryImageMap = categoryImages.stream()
                .collect(Collectors.groupingBy(
                        CategoryImage::getCategoryId,
                        Collectors.toList()
                ));

        System.out.println("Category IDs: " + categoryIds);
        System.out.println("Image map keys: " + categoryImageMap.keySet());

        categories.forEach(c ->
                c.setImages(categoryImageMap.getOrDefault(c.getId(), new ArrayList<>())));

        PaginationInfo paginationInfo = new PaginationInfo(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                categories.getTotalElements(),
                categories.getTotalPages(),
                categories.hasNext(),
                categories.hasPrevious()
        );
        return new PagedResponse<>(categoryMapper.toCategoryResponseList(categories.getContent()), paginationInfo);
//        return new PageImpl<>(categoryMapper.toCategoryResponseList(categories.getContent()), pageable, categories.getTotalElements());
    }

    private String sanitizeSearchParam(String param) {

        System.out.println("=== DEBUG PARAM ===");
        System.out.println("Raw param: [" + param + "]");
        System.out.println("Param == null: " + (param == null));
        if (param != null) {
            System.out.println("Param length: " + param.length());
            System.out.println("Param bytes: " + Arrays.toString(param.getBytes(StandardCharsets.UTF_8)));
            System.out.println("Contains replacement char: " + param.contains("\uFFFD"));
            for (int i = 0; i < param.length(); i++) {
                System.out.println("  char[" + i + "]: '" + param.charAt(i) + "' (code: " + (int) param.charAt(i) + ")");
            }
        }
        System.out.println("==================");
        if (param == null) {
            return null;
        }

        // Trim spaces
        param = param.trim();

        // Return null if empty after trim
        if (param.isEmpty()) {
            return null;
        }
// THÊM: Kiểm tra malformed URL encoding
        if (containsMalformedUrlEncoding(param)) {
            System.out.println("WARNING: Malformed URL encoding detected, returning null: " + param);
            return null;
        }

        // ✅ Check malformed UTF-8 characters
        byte[] bytes = param.getBytes(StandardCharsets.UTF_8);
        String reconstructed = new String(bytes, StandardCharsets.UTF_8);

        // Nếu có ký tự � (U+FFFD - replacement character)
        if (reconstructed.contains("\uFFFD")) {
            throw new IllegalArgumentException("Invalid search parameter: malformed encoding");
        }

        // Reject wildcards only
        if (param.matches("^[%_\\s]+$")) {
            throw new IllegalArgumentException(
                    "Search parameter cannot contain only wildcard characters"
            );
        }


        //  Escape SQL LIKE wildcards: % và _
        // User nhập "test_name" → không bị coi là wildcard
        // User nhập "test%name" → không bị coi là wildcard
        param = param.replace("\\", "\\\\"); // Escape backslash first
        param = param.replace("%", "\\%");   // Escape %
        param = param.replace("_", "\\_");   // Escape _

        // Giới hạn độ dài để tránh DoS
        if (param.length() > 200) {
            param = param.substring(0, 200);
        }

        return param;
    }

    private boolean containsMalformedUrlEncoding(String str) {
        if (!str.contains("%")) {
            return false;
        }

        try {
            java.net.URLDecoder.decode(str, java.nio.charset.StandardCharsets.UTF_8);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    /**
     * Validate date range
     */
    private void validateDateRange(Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            if (startDate.after(endDate)) {
                throw new InvalidDateRangeException("Start date must be before or equal to end date");
            }

            // Giới hạn khoảng thời gian tìm kiếm (tránh query quá lớn)
            long diffInMillis = endDate.getTime() - startDate.getTime();
            long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

            if (diffInDays > 3650) { // 10 years
                throw new InvalidDateRangeException("Date range cannot exceed 10 years");
            }
        }

        // Validate date không ở quá xa trong tương lai
        if (startDate != null || endDate != null) {
            Date maxFutureDate = new Date(System.currentTimeMillis() + (365L * 24 * 60 * 60 * 1000)); // 1 year ahead

            if (startDate != null && startDate.after(maxFutureDate)) {
                throw new InvalidDateRangeException("Start date too far in the future");
            }
            if (endDate != null && endDate.after(maxFutureDate)) {
                throw new InvalidDateRangeException("End date too far in the future");
            }
        }
    }

    // Bai 3: update product

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest req, Locale locale) {
        // only allow update category with status = 1
        Category categoryExist = categoryRepository.findByIdAndStatus(id, "1")
                .orElseThrow(() -> new CategoryNotFoundException("category.notfound"));

        if (!Objects.equals(req.getCategoryCode(), categoryExist.getCategoryCode())) {
            if (categoryRepository.existsByCategoryCode(req.getCategoryCode())) {
                throw new CategoryCodeExistsException("category.code.exists");
            }
            categoryExist.setCategoryCode(req.getCategoryCode());
        }

        // transfer from request to category
        categoryMapper.updateCategoryFromRequest(req, categoryExist);
        categoryExist = categoryRepository.save(categoryExist);
        System.out.println("cagory id after save: " + categoryExist.getId());

        List<String> savedFilePaths = new ArrayList<>();

        List<CategoryImage> softDeleteImages = new ArrayList<>();

        try {
            // in case there are a list of old images uuid that needs to be replaced
            if (req.getReplaceUuids() != null && !req.getReplaceUuids().isEmpty()) {
                for (String uuidReplace : req.getReplaceUuids()) {
                    Optional<CategoryImage> imageOptional = categoryImageRepository
                            .findByCategoryIdAndStatus(id, uuidReplace, "1");
                    // mark old images as no longer use
                    if (imageOptional.isPresent()) {
                        CategoryImage imageToUpdate = imageOptional.get();
                        imageToUpdate.setStatus("0"); // soft delete
                        categoryImageRepository.save(imageToUpdate);
                        // track for potential rollback
                        softDeleteImages.add(imageToUpdate);
                        log.debug("Soft deleted image with uuid: {}", uuidReplace);
                    }
                }
            }

            // if there is a new image uploaded, process it
            if (req.getImages() != null && !req.getImages().isEmpty()) {
                List<CategoryImage> newImages = saveImages(req.getImages(), categoryExist, locale, savedFilePaths);
                // Gán category_id cho ảnh mới (set category cho từng ảnh mới)
                for (CategoryImage img : newImages) {
                    img.setStatus("1");
                    img.setCategory(categoryExist);  // Set category_id cho ảnh
                    categoryImageRepository.save(img);
                }
                categoryExist.getImages().addAll(newImages);
//                categoryImageRepository.saveAll(newImages);
//                categoryExist.getImages().addAll(newImages);

            }
        } catch (FileSizeExceededException | InvalidFileException e) {
            cleanupFiles(savedFilePaths);
            restoreSoftDeletedImages(softDeleteImages);
            log.error("Unexpected error updating category: {}", id, e);
        } catch (IOException e) {
            cleanupFiles(savedFilePaths);

            restoreSoftDeletedImages(softDeleteImages);

            String errorMessage = messageSource.getMessage("error.category.image.save", null, locale);
            log.error("Failed to save images for category update: {}", id, e);
            throw new RuntimeException(errorMessage, e);
        } catch (Exception e) {
            // Cleanup newly saved files
            cleanupFiles(savedFilePaths);

            // Restore soft deleted images
            restoreSoftDeletedImages(softDeleteImages);

            String errorMessage = messageSource.getMessage("general.error", null, locale);
            log.error("Unexpected error updating category: {}", id, e);
            throw new RuntimeException(errorMessage, e);
        }
        Category updatedCategory = categoryRepository.save(categoryExist);
        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    private void restoreSoftDeletedImages(List<CategoryImage> softDeletedImages) {
        if (softDeletedImages == null || softDeletedImages.isEmpty()) {
            return;
        }

        log.info("Restoring {} soft deleted images", softDeletedImages.size());

        for (CategoryImage image : softDeletedImages) {
            try {
                image.setStatus("1"); // Restore to active
                categoryImageRepository.save(image);
                log.debug("Restored image with uuid: {}", image.getUuid());
            } catch (Exception e) {
                log.error("Failed to restore image with uuid: {}", image.getUuid(), e);
                // Continue with other images even if one fails
            }
        }

        log.info("Image restoration completed");
    }

    // Bai 4
    // Constants
    private static final int MAX_EXCEL_ROWS = 1_000_000; // Excel limit ~1M rows, để an toàn
    private static final String EXCEL_FILENAME_PREFIX = "Categories_Export_";

    public byte[] exportCategoriesToExcel(String name, String categoryCode, Date startDate, Date endDate) throws IOException {
        // Validation đầu vào
        validateExportParams(startDate, endDate);

        // Lấy TẤT CẢ data để export (không dùng pageable)
        List<Category> categoryList = categoryRepository.findAllForExport("1", name, categoryCode, startDate, endDate);

        // Validate kích thước data
        if (categoryList.isEmpty()) {
            throw new NoDataToExportException("Không có dữ liệu để xuất");
        }

        if (categoryList.size() > MAX_EXCEL_ROWS) {
            throw new ExcelRowLimitExceededException(
                    String.format("Dữ liệu vượt quá giới hạn Excel (%d rows). Vui lòng thu hẹp điều kiện tìm kiếm.", MAX_EXCEL_ROWS)
            );
        }

        // Sử dụng try-with-resources để đảm bảo đóng workbook
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Categories");

            // Tạo styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle textStyle = createTextStyle(workbook);

            // Tạo header
            createHeader(sheet, headerStyle);

            // Điền dữ liệu
            fillData(sheet, categoryList, dateStyle, textStyle);

            // Auto-size columns
            autoSizeColumns(sheet, 8);

            // Write to output stream
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            throw new ExcelExportException("Lỗi khi xuất file Excel: " + e.getMessage(), e);
        }
    }

    // Validation
    private void validateExportParams(Date startDate, Date endDate) {
        if (startDate != null && endDate != null && startDate.after(endDate)) {
            throw new InvalidDateRangeException("Ngày bắt đầu phải trước ngày kết thúc");
        }
    }

    // Tạo header với style
    private void createHeader(Sheet sheet, CellStyle headerStyle) {
        Row header = sheet.createRow(0);
        String[] headers = {"ID", "Tên", "Mã", "Mô tả", "Ngày tạo", "Ngày sửa", "Người tạo", "Người sửa"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    // Điền dữ liệu với null safety
    private void fillData(Sheet sheet, List<Category> categoryList, CellStyle dateStyle, CellStyle textStyle) {
        int rowNum = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (Category category : categoryList) {
            Row row = sheet.createRow(rowNum++);

            // ID
            createCell(row, 0, category.getId(), textStyle);

            // Name
            createCell(row, 1, category.getName(), textStyle);

            // Category Code
            createCell(row, 2, category.getCategoryCode(), textStyle);

            // Description
            createCell(row, 3,
                    category.getDescription() != null ? category.getDescription() : "",
                    textStyle);

            // Created Date
            createDateCell(row, 4, category.getCreatedDate(), dateFormat, dateStyle);

            // Modified Date
            createDateCell(row, 5, category.getModifiedDate(), dateFormat, dateStyle);

            // Created By
            createCell(row, 6,
                    category.getCreatedBy() != null ? category.getCreatedBy() : "N/A",
                    textStyle);

            // Modified By
            createCell(row, 7,
                    category.getModifiedBy() != null ? category.getModifiedBy() : "N/A",
                    textStyle);
        }
    }

    // Helper methods
    private void createCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value != null) {
            if (value instanceof Number) {
                cell.setCellValue(((Number) value).doubleValue());
            } else {
                cell.setCellValue(value.toString());
            }
        }
        cell.setCellStyle(style);
    }

    private void createDateCell(Row row, int column, Date date, SimpleDateFormat dateFormat, CellStyle style) {
        Cell cell = row.createCell(column);
        if (date != null) {
            cell.setCellValue(dateFormat.format(date));
        } else {
            cell.setCellValue("N/A");
        }
        cell.setCellStyle(style);
    }

    // Styles
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTextStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            // Thêm padding
            int currentWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, currentWidth + 1000);
        }
    }

    // Bai 5
    // soft delete productType

    @Transactional
    public String softDeleteCategory(Long id, Locale locale) {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndStatus(id, "1");
        if (categoryOptional.isEmpty()) {
            throw new CategoryNotFoundException("category.notfound");
        }
        Category categoryExist = categoryOptional.get();
        categoryExist.setStatus("0");
        categoryExist.setModifiedDate(new java.sql.Date(System.currentTimeMillis()));
        categoryRepository.save(categoryExist);
        return messageSource.getMessage("delete.success", null, locale);
    }
}
