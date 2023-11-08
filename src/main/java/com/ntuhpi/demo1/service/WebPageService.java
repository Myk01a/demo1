package com.ntuhpi.demo1.service;

import com.ntuhpi.demo1.model.WebPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WebPageService {
    void saveWebPage(WebPage webPage);

    void deleteWebPage(String id);
    Page<WebPage> searchWebPages(String keyword, Pageable pageable);
    Page<String> searchWebPageContent(String keyword, Pageable pageable);

    Optional<WebPage> getWebPageById(String id);
}

