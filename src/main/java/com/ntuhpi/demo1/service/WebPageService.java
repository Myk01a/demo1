package com.ntuhpi.demo1.service;

import com.ntuhpi.demo1.model.WebPage;
import com.ntuhpi.demo1.model.WebPageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WebPageService {
    Page<WebPage> searchWebPages(String keyword, Pageable pageable);

    WebPageDTO getWebPageById(String id);
}

