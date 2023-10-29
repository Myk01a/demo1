package com.ntuhpi.demo1.repository;

import com.ntuhpi.demo1.model.WebPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WebPageRepository extends MongoRepository<WebPage, String> {
    Page<WebPage> findByPageDumpContaining(String keyword, Pageable pageable);
}