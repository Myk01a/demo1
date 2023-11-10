package com.ntuhpi.demo1.controller;

import com.ntuhpi.demo1.model.WebCrawlRequest;
import com.ntuhpi.demo1.service.WebCrawlerService;
import com.ntuhpi.demo1.model.WebPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
public class WebCrawlerController {
    @GetMapping("/site")
    public String webCrawlerForm(Model model) {
        model.addAttribute("webCrawlRequest", new WebCrawlRequest());
        return "crawler";
    }
    private final WebCrawlerService webPageService;

    @Autowired
    public WebCrawlerController(WebCrawlerService webPageService) {
        this.webPageService = webPageService;
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
        System.out.println("executionTime: "+ executionTime);
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

}

