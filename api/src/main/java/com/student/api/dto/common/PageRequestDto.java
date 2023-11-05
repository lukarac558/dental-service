package com.student.api.dto.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = false)
public class PageRequestDto extends AbstractPageRequest {
    private final Map<String, Sort.Direction> sortMap;
    @Accessors(fluent = true)
    private final Boolean isEnabled;

    public PageRequestDto(Long page, Long size, Map<String, Sort.Direction> sort, Boolean isEnabled) {
        super(Math.toIntExact(page), Math.toIntExact(size));
        this.sortMap = sort;
        this.isEnabled = isEnabled;
    }

    public PageRequestDto(int page, PageRequestDto pageRequest) {
        super(page, pageRequest.getPageNumber());
        this.sortMap = pageRequest.getSortMap();
        this.isEnabled = pageRequest.isEnabled();
    }

    @Override
    public boolean isPaged() {
        return isEnabled;
    }

    @Override
    public boolean isUnpaged() {
        return !isEnabled;
    }

    @Override
    public Sort getSort() {
        return Sort.by(sortMap.entrySet().stream()
                .map(e -> new Sort.Order(e.getValue(), e.getKey())).toList()
        );
    }

    @Override
    public Pageable next() {
        return new PageRequestDto(
                getPageNumber()+1,
                this
        );
    }

    @Override
    public Pageable previous() {
        if(getPageNumber() == 0) {
            return this;
        } else {
            return new PageRequestDto(
                    getPageNumber()-1,
                    this
            );
        }
    }

    @Override
    public Pageable first() {
        return new PageRequestDto(
                0,
                this
        );
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new PageRequestDto(
                pageNumber,
                this
        );
    }
}
