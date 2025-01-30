package com.meac.springsecurity.controller.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record FeedDTO(List<FeedItemDTO> feedItems, int page, int pageSize, int totalPages, long totalRecords) {
    public static FeedDTO fromPage(Page<FeedItemDTO> page) {
        return new FeedDTO(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
