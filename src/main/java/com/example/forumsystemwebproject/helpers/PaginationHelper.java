package com.example.forumsystemwebproject.helpers;

import com.example.forumsystemwebproject.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

public class PaginationHelper {

    public static <T> Page<T> findPaginated(Pageable pageable, List<T> items) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        currentPage = Math.max(currentPage, 0);
        int startItem = currentPage * pageSize;
        List<T> list;

        if (items.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, items.size());
            list = items.subList(startItem, toIndex);
        }

        Page<T> page = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), items.size());

        return page;
    }
}
