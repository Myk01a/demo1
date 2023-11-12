package com.ntuhpi.demo1.service;

import com.ntuhpi.demo1.model.WebPage;
import com.ntuhpi.demo1.repository.WebPageRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebPageServiceImpl implements WebPageService {
    private final WebPageRepository webPageRepository;

    @Autowired
    public WebPageServiceImpl(WebPageRepository webPageRepository) {
        this.webPageRepository = webPageRepository;
    }


    @Override
    public Page<WebPage> searchWebPagesSimple(String keyword, Pageable pageable) {
        return webPageRepository.findByPageDumpContaining(keyword, pageable);
    }

    @Override
    public Page<WebPage> searchWebPages(String keyword, Pageable pageable) {
        return webPageRepository.findByTextSearch(keyword, pageable);
    }


    @Override
    public WebPage getWebPageById(String id) {
        return webPageRepository.getById(id);
    }

    @Override
    public WebPage processUrl(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            if (!isHtmlPage(document)) {
                return null;
            }

            String pageDump = document.text();
            String fullPageDump = document.html();
            WebPage webPage = new WebPage(url, pageDump, fullPageDump);
            saveWebPage(webPage);
            return webPage;
        } catch (IOException e) {
            return null;
        }
    }

    private boolean isHtmlPage(Document document) {
        String type = String.valueOf(document.documentType());
        return document != null && "<!doctype html>".equalsIgnoreCase(type);
    }

    private void saveWebPage(WebPage webPage) {
        if (webPage != null) {
            webPageRepository.save(webPage);
        }
    }

    @Override
    public void deleteAll() {
        webPageRepository.deleteAll();
    }


}
