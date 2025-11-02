package com.example.nodotest.Controller;

import com.example.nodotest.Dto.Pagination.PagedResponse;
import com.example.nodotest.Dto.Pagination.PaginationInfo;
import com.example.nodotest.Dto.Request.CategoryRequest;
import com.example.nodotest.Dto.Response.ApiResponse;
import com.example.nodotest.Dto.Response.CategoryResponse;
import com.example.nodotest.Dto.Response.ErrorResponse;
import com.example.nodotest.Exception.CategoryCodeExistsException;
import com.example.nodotest.Exception.CategoryNotFoundException;
import com.example.nodotest.Exception.InvalidPageableParameterException;
import com.example.nodotest.Service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final MessageSource messageSource;

    public CategoryController(CategoryService categoryService, MessageSource messageSource) {
        this.categoryService = categoryService;
        this.messageSource = messageSource;
    }

    // bai 1
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @ModelAttribute CategoryRequest categoryRequest
    ) {
        Locale locale = LocaleContextHolder.getLocale();

        ApiResponse<CategoryResponse> response = categoryService.createCategory(categoryRequest, locale);
        return ResponseEntity.ok(response);
    }


    // bai 2
    @GetMapping
    public ResponseEntity<PagedResponse<CategoryResponse>> getCategories(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "categoryCode", required = false) String categoryCode,
            @RequestParam(value = "startDate", required = false) Date startDate,
            @RequestParam(value = "endDate", required = false) Date endDate,
            @PageableDefault Pageable pageable) {

        // Kiểm tra page và size là số hợp lệ hay không
        if (!isValidPageAndSize(pageable)) {
            // Nếu không hợp lệ, trả về lỗi
            throw new InvalidPageableParameterException("Invalid page or size"); // message sẽ lấy từ i18n
        }

//        categoryService.getCategories(name, categoryCode, startDate, endDate, pageable);

        return ResponseEntity.ok(categoryService.getCategories(name, categoryCode, startDate, endDate, pageable));
    }

    private boolean isValidPageAndSize(Pageable pageable) {
        try {
            int page = pageable.getPageNumber();
            int size = pageable.getPageSize();

            // Kiểm tra page >= 0 và size > 0
            return page >= 0 && size > 0;
        } catch (Exception e) {
            return false;  // Nếu có exception, tức là giá trị không hợp lệ
        }
    }

    // bai 3
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCategory(
            @PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("categoryCode") String categoryCode,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "replaceUuids", required = false) List<String> replaceUuids,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            System.out.println("locale: " + locale);

            CategoryRequest req = new CategoryRequest();
            req.setName(name);
            req.setCategoryCode(categoryCode);
            req.setDescription(description);
            req.setReplaceUuids(replaceUuids);
            req.setImages(images);

            CategoryResponse categoryResponse = categoryService.updateCategory(id, req, locale);

            return ResponseEntity.ok(categoryResponse);
        } catch (CategoryNotFoundException e) {
            String errorMessage = messageSource.getMessage("category.notfound", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        } catch (CategoryCodeExistsException e) {
            String errorMessage = messageSource.getMessage("category.code.exists", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage));
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("general.error", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    // bai 4
    @GetMapping("/export")
    public ResponseEntity<?> exportCategoriesToExcel(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryCode,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        try {

            byte[] excelData = categoryService.exportCategoriesToExcel(name, categoryCode, startDate, endDate);

            String filename = String.format("Categories_Export_%s.xlsx",
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis())));

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(excelData);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "EXPORT_FAILED",
                            "message", e.getMessage(),
                            "timestamp", new Date(System.currentTimeMillis())
                    ));
        }
    }

    // bai 5
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            String message = categoryService.softDeleteCategory(id, locale);
            return ResponseEntity.ok(message);
        } catch (CategoryNotFoundException e) {
            String errorMessage = messageSource.getMessage("category.notfound", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

}