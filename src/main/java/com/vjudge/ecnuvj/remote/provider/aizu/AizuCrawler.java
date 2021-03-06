package com.vjudge.ecnuvj.remote.provider.aizu;

import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.crawler.RawProblemInfo;
import com.vjudge.ecnuvj.remote.crawler.SimpleCrawler;
import com.vjudge.ecnuvj.tool.Tools;
import org.springframework.stereotype.Component;

@Component
public class AizuCrawler extends SimpleCrawler {

    @Override
    public RemoteOjInfo getOjInfo() {
        return AizuInfo.INFO;
    }

    @Override
    protected String getProblemUrl(String problemId) {
        return getHost().toURI() + "/onlinejudge/description.jsp?id=" + problemId;
    }

    @Override
    protected void populateProblemInfo(RawProblemInfo info, String problemId, String html) {
        info.title = Tools.regFind(html, "<h1 class=\"title\">(?:.+</a> -)?([\\s\\S]*?)</h1>").trim();
        info.timeLimit = 1000 * Integer.parseInt(Tools.regFind(html, "Time Limit : (\\d+) sec"));
        info.memoryLimit = Integer.parseInt(Tools.regFind(html, "Memory Limit : (\\d+) KB"));
        info.description = Tools.regFind(html, "<div class=\"description\">([\\s\\S]*?)<hr />").replaceAll("^[\\s\\S]*</h1>", "");
        info.source = Tools.regFind(html, "style=\"font-size:10pt\">\\s*Source:([\\s\\S]*?)</div>");
    }

}
