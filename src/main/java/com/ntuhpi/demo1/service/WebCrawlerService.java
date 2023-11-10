package com.ntuhpi.demo1.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.ntuhpi.demo1.model.WebPage;
import com.ntuhpi.demo1.repository.WebPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebCrawlerService {

    private final WebPageRepository webPageRepository;

    @Autowired
    public WebCrawlerService(WebPageRepository webPageRepository) {
        this.webPageRepository = webPageRepository;
    }

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

    public void deleteAll() {
        webPageRepository.deleteAll();
    }
}

