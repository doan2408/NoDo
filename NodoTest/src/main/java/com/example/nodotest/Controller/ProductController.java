package com.example.nodotest.Controller;

import com.example.nodotest.Dto.Pagination.PagedResponse;
import com.example.nodotest.Dto.Request.ProductRequest;
import com.example.nodotest.Dto.Response.ApiResponse;
import com.example.nodotest.Dto.Response.ProductResponse;
import com.example.nodotest.Service.ProductService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    private final MessageSource messageSource;

    public ProductController(ProductService productService, MessageSource messageSource) {
        this.productService = productService;
        this.messageSource = messageSource;
    }

    // bai 6
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @ModelAttribute ProductRequest productRequest
    ) {

            Locale locale = LocaleContextHolder.getLocale();

        return ResponseEntity.ok(productService.createProduct(productRequest, locale));
    }

    // bai 7
    @GetMapping("/search")
    public ResponseEntity<PagedResponse<ProductResponse>> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault Pageable pageable
    ) {
        PagedResponse<ProductResponse> response = productService.getProducts(name, code, startDate, endDate, categoryId, pageable);
        return ResponseEntity.ok(response);
    }

    // bai 8
    @GetMapping("/export")
    public ResponseEntity<?> exportProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) Long categoryId
    ) {
        try {
            ByteArrayInputStream in = productService.exportProductsToExcel(
                    name, code, startDate, endDate, categoryId
            );

            // Generate filename with timestamp
            String filename = "products_" +
                              new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis())) +
                              ".xlsx";

            InputStreamResource resource = new InputStreamResource(in);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // bai 9
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductRequest request,
            Locale locale
    ) {
        ApiResponse<ProductResponse> response = productService.updateProduct(id, request, locale);
        return ResponseEntity.ok(response);
    }

    // bai 10
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable("id") Long id
    ) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            String message = productService.softDeleteProduct(id, locale);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("product.notfound", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

}
