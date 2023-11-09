package com.ntuhpi.demo1.controller;

import com.ntuhpi.demo1.model.WebPage;
import com.ntuhpi.demo1.model.WebPageDTO;
import com.ntuhpi.demo1.service.WebPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class WebPageController {
    private final WebPageService webPageService;

    @Autowired
    public WebPageController(WebPageService webPageService) {
        this.webPageService = webPageService;
    }

    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/cache/{id}")
    public String viewCachePage(@PathVariable String id, Model model) {
        Optional<WebPage> webPage = webPageService.getWebPageById(id);
        if (webPage != null) {
            model.addAttribute("content", webPage.get().getFullPageDump());

            return "cache";
        }
        return "404";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword,
                         @RequestParam(name = "page", defaultValue = "1") int page,
                         @RequestParam(name = "size", defaultValue = "5") int size,
                         Model model) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<WebPage> searchResult = webPageService.searchWebPages(keyword, pageable);

        List<WebPageDTO> webPageDTOList = searchResult.getContent().stream()
                .map(webPage -> {
                    WebPageDTO dto = new WebPageDTO();
                    dto.setId(webPage.getId());
                    dto.setUrl(webPage.getUrl());
                    dto.setFullPageDump(webPage.getFullPageDump());

                    String pageDump = webPage.getPageDump();
                    int keywordIndex = pageDump.indexOf(keyword);
                    int startIndex = Math.max(0, keywordIndex - 200);
                    int endIndex = Math.min(pageDump.length(), keywordIndex + 200 + keyword.length());
                    pageDump = pageDump.substring(startIndex, endIndex);
                    String pageDumpWithHighlight = pageDump.replace(keyword, "<span style='background-color: yellow;'>" + keyword + "</span>");
                    dto.setPage(pageDumpWithHighlight);


                    return dto;
                })
                .collect(Collectors.toList());

        stopWatch.stop();
        long executionTime = stopWatch.getTotalTimeMillis();

        model.addAttribute("searchResult", webPageDTOList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageSize", size);
        model.addAttribute("executionTime", executionTime);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("totalSearchResults", searchResult.getTotalElements());
        model.addAttribute("currentPage", searchResult.getNumber() + 1);

        return "search-results";
    }

}


