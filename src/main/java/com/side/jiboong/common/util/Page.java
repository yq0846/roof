package com.side.jiboong.common.util;

import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
public class Page<T> {

    private final Integer page;
    private final Integer size;
    private final Integer total;
    private final Integer totalPage;
    private final Boolean isFirst;
    private final Boolean isLast;
    private final Integer firstPageNum;
    private final Integer lastPageNum;
    private final List<T> list;

    private Page(Pageable condition, Integer total, List<T> list) {
        this.page = condition.getPageNumber() + 1;
        this.size = condition.getPageSize();
        this.total = total;
        this.totalPage = total % condition.getPageSize() == 0 ? (total / condition.getPageSize()) : (total / condition.getPageSize() + 1);
        this.list = list;
        this.isFirst = page == 1;
        this.isLast = totalPage.equals(page);
        this.firstPageNum = page % 10 == 0 ? page - 9 : page / 10 * 10 + 1;
        int lastPage = page % 10 == 0 ? page : (page / 10 + 1) * 10;
        if (lastPage > totalPage) lastPage = totalPage;
        this.lastPageNum = lastPage;
    }
    
    public static <T> Page<T> from(Pageable condition, Integer total, List<T> list) {
        return new Page<>(condition, total, list);
    }

}
