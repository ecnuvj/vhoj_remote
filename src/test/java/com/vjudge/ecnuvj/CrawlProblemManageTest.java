package com.vjudge.ecnuvj;

import com.vjudge.ecnuvj.remote.manager.CrawlProblemManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author tcg
 * @date 2021/3/25
 */
@SpringBootTest
public class CrawlProblemManageTest {
    @Autowired
    CrawlProblemManager crawlProblemManager;

    @Test
    void crawlProblem() throws InterruptedException {
        crawlProblemManager.crawlProblem("HDU", "1000", true);
        Thread.sleep(5000);
    }
}
