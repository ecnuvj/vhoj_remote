package com.vjudge.ecnuvj.remote.provider.hysbz;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.crawler.RawProblemInfo;
import com.vjudge.ecnuvj.remote.crawler.SyncCrawler;
import com.vjudge.ecnuvj.tool.HtmlHandleUtil;
import com.vjudge.ecnuvj.tool.Tools;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpHost;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HYSBZCrawler extends SyncCrawler {

    @Override
    public RemoteOjInfo getOjInfo() {
        return HYSBZInfo.INFO;
    }

    private static final HttpHost HOSTS[] = new HttpHost[]{
            new HttpHost("www.lydsy.com"),
            new HttpHost("www.lydsy.com", 808, "http"),
    };

    @Override
    public RawProblemInfo crawl(String problemId) throws Exception {
        preValidate(problemId);

        String problemUrl = null;
        String html = null;
        for (HttpHost host : HOSTS) {
            try {
                DedicatedHttpClient client = dedicatedHttpClientFactory.build(host);
                problemUrl = getProblemUrl(host, problemId);
                html = client.get(problemUrl, HttpStatusValidator.SC_OK).getBody();
                html = HtmlHandleUtil.transformUrlToAbs(html, problemUrl);
                break;
            } catch (Throwable t) {
            }
        }
        Validate.notBlank(html);

        RawProblemInfo info = new RawProblemInfo();
        info.title = Tools.regFind(html, "<center><h2>([\\s\\S]*?)</h2>").replaceAll(problemId + ": ", "").trim();
        info.source = (Tools.regFind(html, "<h2>Source</h2>[\\s\\S]*?<div class=content><p>([\\s\\S]*?)</p></div><center>"));
        Matcher matcher = Pattern.compile("\\[(.*)\\](.*)").matcher(info.title);
        if (matcher.find()) {
            info.title = matcher.group(2);
            info.source = matcher.group(1);
        }
        info.timeLimit = (1000 * Integer.parseInt(Tools.regFind(html, "Time Limit: </span>(\\d+) Sec")));
        info.memoryLimit = (1024 * Integer.parseInt(Tools.regFind(html, "Memory Limit: </span>(\\d+) MB")));
        info.description = (Tools.regFind(html, "<h2>Description</h2>([\\s\\S]*?)<h2>Input</h2>"));
        info.input = (Tools.regFind(html, "<h2>Input</h2>([\\s\\S]*?)<h2>Output</h2>"));
        info.output = (Tools.regFind(html, "<h2>Output</h2>([\\s\\S]*?)<h2>Sample Input</h2>"));
        info.sampleInput = (Tools.regFind(html, "<h2>Sample Input</h2>([\\s\\S]*?)<h2>Sample Output</h2>").replaceAll("<span", "<pre").replaceAll("</span>", "</pre>").replace("<br /> ", "<br />"));
        info.sampleOutput = (Tools.regFind(html, "<h2>Sample Output</h2>([\\s\\S]*?)<h2>HINT</h2>").replaceAll("<span", "<pre").replaceAll("</span>", "</pre>").replace("<br /> ", "<br />"));
        info.hint = (Tools.regFind(html, "<h2>HINT</h2>([\\s\\S]*?)<h2>Source</h2>"));
        info.url = problemUrl;
        Validate.isTrue(!StringUtils.isBlank(info.title));

        return info;
    }

    protected String getProblemUrl(HttpHost host, String problemId) {
        return host.toURI() + "/JudgeOnline/problem.php?id=" + problemId;
    }

    protected void preValidate(String problemId) {
        Validate.isTrue(problemId.matches("[1-9]\\d*"));
    }

}
