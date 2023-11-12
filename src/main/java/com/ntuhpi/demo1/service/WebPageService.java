package com.ntuhpi.demo1.service;

import com.ntuhpi.demo1.model.WebPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WebPageService {
    Page<WebPage> searchWebPages(String keyword, Pageable pageable);
    Page<WebPage> searchWebPagesSimple(String keyword, Pageable pageable);

    WebPage getWebPageById(String id);

    void deleteAll();

    WebPage processUrl(String url);
}

