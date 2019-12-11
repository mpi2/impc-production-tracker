package org.gentar.util;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PaginationHelper
{
    public static <T> Page<T> createPage(List<T> content, Pageable pageable)
    {
        PagedListHolder<T> pagedListHolder = new PagedListHolder<>(content);
        pagedListHolder.setPageSize(pageable.getPageSize());
        pagedListHolder.setPage(pageable.getPageNumber());

        return new PageImpl<>(pagedListHolder.getPageList(), pageable, content.size());
    }
}
