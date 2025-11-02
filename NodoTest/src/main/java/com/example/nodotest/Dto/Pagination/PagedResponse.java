package com.example.nodotest.Dto.Pagination;

import java.util.ArrayList;
import java.util.List;

public class PagedResponse<T> {
    private List<T> content;
    private PaginationInfo pagination;

    public PagedResponse(List<T> content, PaginationInfo pagination) {
        this.content = content;
        this.pagination = pagination;
    }

    public PagedResponse() {
    }

    public <E> PagedResponse(ArrayList<E> es, Object o, String s) {
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public PaginationInfo getPagination() {
        return pagination;
    }

    public void setPagination(PaginationInfo pagination) {
        this.pagination = pagination;
    }
}
