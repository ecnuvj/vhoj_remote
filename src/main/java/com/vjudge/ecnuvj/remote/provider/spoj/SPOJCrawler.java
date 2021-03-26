package com.vjudge.ecnuvj.remote.provider.spoj;

import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.crawler.RawProblemInfo;
import com.vjudge.ecnuvj.remote.crawler.SimpleCrawler;
import com.vjudge.ecnuvj.tool.Tools;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

@Component
public class SPOJCrawler extends SimpleCrawler {

    @Override
    public RemoteOjInfo getOjInfo() {
        return SPOJInfo.INFO;
    }

    @Override
    protected String getProblemUrl(String problemId) {
        return getHost().toURI() + "/problems/" + problemId + "/";
    }

    @Override
    protected void preValidate(String problemId) {
        Validate.isTrue(problemId.matches("\\S+"));
    }

    @Override
    protected void populateProblemInfo(RawProblemInfo info, String problemId, String html) {
        Validate.isTrue(!html.contains("In a few seconds you will be redirected to"));

        info.title = Tools.regFind(html, "<h2 id=\"problem-name\" class=\"text-center\">[\\s\\S]*? - ([\\s\\S]*?)</h2>").trim();
        info.timeLimit = (int) (1000 * Double.parseDouble(Tools.regFind(html, "Time limit:</td><td>([\\s\\S]*?)s")));
        info.memoryLimit = (int) (1024 * Double.parseDouble(Tools.regFind(html, ">Memory limit:</td><td>([\\s\\S]*?)MB")));
        info.description = (Tools.regFind(html, "<div id=\"problem-body\">([\\s\\S]*?)</div><div class=\"text-center\"><a href=\"http://www.spoj.com/submit/"))
                .replace("<b>Input:</b> ", "<b>Input:</b>\n").replace("<b>Output:</b>", "\n\n<b>Output:</b>").replace("<b>Output:</b> ", "<b>Output:</b>\n");
        info.source = (Tools.regFind(html, "<tr><td>Resource:</td><td>([\\s\\S]*?)</td></tr>"));
    }

}
