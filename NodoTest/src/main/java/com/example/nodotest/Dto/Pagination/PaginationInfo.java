package com.example.nodotest.Dto.Pagination;

public record PaginationInfo(
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {

}