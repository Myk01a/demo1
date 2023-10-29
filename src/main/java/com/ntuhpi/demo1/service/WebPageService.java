package com.ntuhpi.demo1.service;

import com.ntuhpi.demo1.model.WebPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WebPageService {
    WebPage saveWebPage(WebPage webPage);
    void deleteWebPage(String id);
    Page<WebPage> searchWebPages(String keyword, Pageable pageable);
}

