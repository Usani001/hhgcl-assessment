package com.hhgcl.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Pagination {
    private int page;
    private int limit;
    private long total;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrev;
    private Integer nextPage;
    private Integer prevPage;
    private String sortBy;
    private String sortOrder;

}
