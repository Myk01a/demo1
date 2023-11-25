package com.ntuhpi.demo1.repository;

import com.ntuhpi.demo1.model.WebPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface WebPageRepository extends MongoRepository<WebPage, String> {
    @Query("{'$text': {'$search': ?0}}")
    Page<WebPage> findByTextSearch(String keyword, Pageable pageable);

    Page<WebPage> findByPageDumpContainingIgnoreCase(String keyword, Pageable pageable);

    WebPage getById(String id);

    WebPage save(WebPage webPage);

    void deleteAll();

    @Override
    long count();
}
