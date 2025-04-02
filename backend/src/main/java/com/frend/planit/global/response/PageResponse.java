package com.frend.planit.global.response;

import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.domain.Page;

/*
 * 페이징을 위한 DTO 클래스입니다.
 */
@Getter
public class PageResponse<T> {

    private final int currentPage;
    private final int pageSize;
    private final long totalPages;
    private final boolean hasNext;
    private final boolean hasPrevious;
    private final long totalData;
    private final List<T> data;

    public PageResponse(@NonNull Page<T> page) {
        this.currentPage = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        this.totalData = page.getTotalElements();
        this.data = page.getContent();
    }
}