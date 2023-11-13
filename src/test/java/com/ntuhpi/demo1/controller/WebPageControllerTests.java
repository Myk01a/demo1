package com.ntuhpi.demo1.controller;

import com.ntuhpi.demo1.service.WebPageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WebPageControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebPageService webPageService;

//    @BeforeEach
//    public void setUp() throws Exception {
//        // Отримайте дані для підключення до віддаленої бази даних з вашого середовища або конфігурації
//        String remoteHost = System.getenv("REMOTE_HOST"); // IP-адреса або хост вашої віддаленої БД
//        String username = System.getenv("DB_USERNAME"); // Логін
//        String password = System.getenv("DB_PASSWORD"); // Пароль
//        int port = 27017; // Порт для підключення (якщо відомий)
//
//        MongodExecutable mongodExecutable = MongodStarter.getDefaultInstance().prepare(new MongodConfigBuilder()
//                .version(Version.Main.PRODUCTION)
//                .net(new Net(remoteHost, port, Network.localhostIsIPv6()))
//                .build());
//        mongodExecutable.start();

        // Використання з'єднання з віддаленою БД для ваших тестів
        // Ініціалізуйте та налаштуйте ваше з'єднання з віддаленою БД
        // Використовуйте змінні `remoteHost`, `username` та `password` для з'єднання з віддаленою базою даних
//    }

    @Test
    public void testGetHomePage() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    public void testGetCachePage() throws Exception {
        String id = "your_id"; // Replace with an actual ID
        String keyword = "your_keyword"; // Replace with an actual keyword
        mockMvc.perform(get("/cache/" + id + "/" + keyword)).andExpect(status().isOk());
    }

    @Test
    public void testGetSearchPage() throws Exception {
        mockMvc.perform(get("/search")).andExpect(status().isOk());
    }

    @Test
    public void testGetSitePage() throws Exception {
        mockMvc.perform(get("/site")).andExpect(status().isOk());
    }

    @Test
    public void testSearchPageWithRelevantContent() throws Exception {
        String keyword = "your_keyword"; // Replace with the keyword you're testing

        // Assuming /search endpoint returns pages with 'span' tags around the matched keyword
        mockMvc.perform(get("/search").param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(content().string("span style='background-color: yellow;'>" + keyword + "</span>"));
    }
}

