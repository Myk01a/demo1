package com.ntuhpi.demo1.service;

import com.ntuhpi.demo1.model.WebPage;
import com.ntuhpi.demo1.repository.WebPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WebPageServiceImpl implements WebPageService {
    private final WebPageRepository webPageRepository;

    @Autowired
    public WebPageServiceImpl(WebPageRepository webPageRepository) {
        this.webPageRepository = webPageRepository;
    }

    @Override
    public WebPage saveWebPage(WebPage webPage) {
        return webPageRepository.save(webPage);
    }

    @Override
    public void deleteWebPage(String id) {
        webPageRepository.deleteById(id);
    }

    @Override
    public Page<WebPage> searchWebPages(String keyword, Pageable pageable) {
        return webPageRepository.findByPageDumpContaining(keyword, pageable);
    }
    @Override
    public Page<String> searchWebPageContent(String keyword, Pageable pageable) {
        Page<WebPage> searchResult = webPageRepository.findByPageDumpContaining(keyword, pageable);
        return searchResult.map(webPage -> extractContentWithKeyword(webPage, keyword));
    }

    private String extractContentWithKeyword(WebPage webPage, String keyword) {
        String pageDump = webPage.getPageDump();
        int keywordIndex = pageDump.indexOf(keyword);
        int startIndex = Math.max(0, keywordIndex - 100); // Початок від ключового слова - 100 символів
        int endIndex = Math.min(pageDump.length(), keywordIndex + 100 + keyword.length()); // Кінець від ключового слова + 100 символів
        return pageDump.substring(startIndex, endIndex);
    }


}
