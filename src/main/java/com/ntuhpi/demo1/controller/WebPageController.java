package com.ntuhpi.demo1.controller;

import com.ntuhpi.demo1.model.WebCrawlRequest;
import com.ntuhpi.demo1.model.WebPage;
import com.ntuhpi.demo1.service.WebPageService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @GetMapping("/cache/{id}/{keyword}")
    public String viewCachePage(@PathVariable String id, Model model, @PathVariable String keyword) {
        WebPage webPage = webPageService.getWebPageById(id);
        if (webPage != null) {
            String fullPageDump = webPage.getFullPageDump();
            String highlighted = highlight(keyword, fullPageDump);
            webPage.setFullPageDump(highlighted);
            model.addAttribute("keyword", keyword);
            model.addAttribute("content", webPage.getFullPageDump());
            model.addAttribute("id", id);
            return "cache";
        }
        return "404";
    }

    private String highlight(String keyword, String fullPageDump) {
        if (!containsWhitespace(keyword)) {
            Pattern pattern = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(fullPageDump);
            return matcher.replaceAll("<span style='background-color: yellow;'>$0</span>");
        } else {
            String[] words = keyword.split("\\s+");
            for (String word : words) {
                Pattern pattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\b", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(fullPageDump);
                fullPageDump = matcher.replaceAll("<span style='background-color: yellow;'>$0</span>");
            }
            return fullPageDump;
        }
    }

    private boolean containsWhitespace(String input) {
        return input != null && input.contains(" ");
    }


    @GetMapping("/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword,
                         @RequestParam(name = "page", defaultValue = "1") int page,
                         @RequestParam(name = "size", defaultValue = "5") int size,
                         Model model) {
        try {

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<WebPage> searchResult = webPageService.searchWebPages(keyword, pageable);

            List<WebPage> webPageList = searchResult.getContent();
            if (webPageList.size() == 0) {
                searchResult = webPageService.searchWebPagesSimple(keyword, pageable);
                webPageList = searchResult.getContent();
            }

            for (WebPage webPage : webPageList) {
                String pageDump = webPage.getPageDump();
                int keywordIndex = pageDump.indexOf(keyword);
                int startIndex = Math.max(0, keywordIndex - 200);
                int endIndex = Math.min(pageDump.length(), keywordIndex + 200 + keyword.length());
                String pageDumpWithHighlight = pageDump.substring(startIndex, endIndex);
                webPage.setPageDump(highlight(keyword, pageDumpWithHighlight));
                String title = webPage.getTitle();
                webPage.setTitle(highlight(keyword, title));
            }

            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeMillis();

            model.addAttribute("searchResult", webPageList);
            model.addAttribute("keyword", keyword);
            model.addAttribute("pageSize", size);
            model.addAttribute("executionTime", executionTime);
            model.addAttribute("totalPages", searchResult.getTotalPages());
            model.addAttribute("totalSearchResults", searchResult.getTotalElements());
            model.addAttribute("currentPage", searchResult.getNumber() + 1);

            return "search-results";
        } catch (InvalidDataAccessApiUsageException e) {
            return "404";
        }
    }

    @GetMapping("/site")
    public String webCrawlerForm(Model model) {
        long pageCount = webPageService.countWebPages();
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("webCrawlRequest", new WebCrawlRequest());
        return "crawler";
    }

    @PostMapping("/delete")
    public String deleteData(Model model) {
        webPageService.deleteAll();
        return "deleted";
    }

    @PostMapping("/site")
    public String webCrawlerSubmit(@ModelAttribute WebCrawlRequest webCrawlRequest,
                                   Model model) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String url = webCrawlRequest.getUrl();
        String domain = getDomain(url);
        HashSet<String> foundUrls = new HashSet<>();
        foundUrls.add(url);
        recursiveCrawl(url, domain, foundUrls);

        List<WebPage> webPages = new ArrayList<>();

        for (String foundUrl : foundUrls) {
            WebPage webPage = webPageService.processUrl(foundUrl);
            if (webPage != null) {
                webPages.add(webPage);
            }
        }
        model.addAttribute("webPages", webPages);
        stopWatch.stop();
        long executionTime = stopWatch.getTotalTimeMillis();
        System.out.println("executionTime: " + executionTime);
        return "result";
    }


    private void recursiveCrawl(String url, String domain, HashSet<String> foundUrls) {
        if (foundUrls.size() >= 100) {
            return;
        }

        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[href]");

            for (Element link : links) {
                String linkedUrl = link.absUrl("href");
                if (isInDomain(linkedUrl, domain) && !foundUrls.contains(linkedUrl)) {
                    foundUrls.add(linkedUrl);
                    recursiveCrawl(linkedUrl, domain, foundUrls);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDomain(String url) {
        try {
            URL parsedUrl = new URL(url);
            String host = parsedUrl.getHost();
            return host;
        } catch (Exception e) {
            return "";
        }
    }

    private boolean isInDomain(String url, String domain) {
        return url.startsWith(domain) || url.matches("https?://([a-zA-Z0-9.-]+\\.)?" + domain + "(/.*)?");
    }

    @RequestMapping("*")
    public String handleNotFound() {
        return "404";
    }


}


