package com.vjudge.ecnuvj.remote.provider.lightoj;

import com.vjudge.ecnuvj.executor.ExecutorTaskType;
import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.DedicatedHttpClientFactory;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.account.RemoteAccountTask;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.crawler.Crawler;
import com.vjudge.ecnuvj.remote.crawler.RawProblemInfo;
import com.vjudge.ecnuvj.remote.loginer.LoginersHolder;
import com.vjudge.ecnuvj.tool.Handler;
import com.vjudge.ecnuvj.tool.HtmlHandleUtil;
import com.vjudge.ecnuvj.tool.Tools;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LightOJCrawler implements Crawler {

    @Autowired
    private DedicatedHttpClientFactory dedicatedHttpClientFactory;

    @Override
    public RemoteOjInfo getOjInfo() {
        return LightOJInfo.INFO;
    }

    @Override
    public void crawl(String problemId, Handler<RawProblemInfo> handler) throws Exception {
        try {
            Validate.isTrue(problemId.matches("[1-9]\\d*"));
        } catch (Throwable t) {
            handler.onError(t);
            return;
        }

        new CrawlTask(problemId, handler).submit();
    }

    class CrawlTask extends RemoteAccountTask<RawProblemInfo> {
        private String problemId;

        public CrawlTask(String problemId, Handler<RawProblemInfo> handler) {
            super(ExecutorTaskType.UPDATE_PROBLEM_INFO, getOjInfo().remoteOj, null, null, handler);
            this.problemId = problemId;
        }

        @Override
        protected RawProblemInfo call(RemoteAccount remoteAccount) throws Exception {
            LoginersHolder.getLoginer(getOjInfo().remoteOj).login(remoteAccount);

            HttpHost host = getOjInfo().mainHost;
            DedicatedHttpClient client = dedicatedHttpClientFactory.build(host, remoteAccount.getContext(), getOjInfo().defaultChaset);

            String problemUrl = host.toURI() + "/volume_showproblem.php?problem=" + problemId;
            String pageContent = client.get(problemUrl, HttpStatusValidator.SC_OK).getBody();
            pageContent = HtmlHandleUtil.transformUrlToAbs(pageContent, problemUrl);

            RawProblemInfo info = new RawProblemInfo();
            info.url = problemUrl;
            populateProblemInfo(info, problemId, pageContent);

            return info;
        }
    }

    protected void populateProblemInfo(RawProblemInfo info, String problemId, String html) {
        info.title = Tools.regFind(html, "Problem \\d+ - ([\\s\\S]*?)</title>").trim();
        info.timeLimit = ((int) (1000 * Double.parseDouble(Tools.regFind(html, "([\\d\\.]*?) second\\(s\\)</span>"))));
        info.memoryLimit = (1024 * Integer.parseInt(Tools.regFind(html, "([\\d\\.]*?) MB</span>")));

        List<String> styleSheets = HtmlHandleUtil.getStyleSheet(html);
        String targetStyleSheet = null;
        for (String styleSheet : styleSheets) {
            if (styleSheet.contains("Font Definitions")) {
                targetStyleSheet = styleSheet;
            }
        }

        info.description = (targetStyleSheet + Tools.regFind(html, "<div class=\"Section1\">([\\s\\S]*?)<h1>Input</h1>"));
        info.input = (Tools.regFind(html, "<h1>Input</h1>([\\s\\S]*?)<h1>Output</h1>"));
        info.output = (Tools.regFind(html, "<h1>Output</h1>([\\s\\S]*?)<table class=\"Mso\\w+"));
        info.sampleInput = (Tools.regFind(html, "<h1>Output</h1>[\\s\\S]*<table class=\"Mso\\w+[\\s\\S]*?<td[\\s\\S]*?<td[\\s\\S]*?<td[^>]*?>([\\s\\S]*?)</td>"));
        info.sampleOutput = (Tools.regFind(html, "<h1>Output</h1>[\\s\\S]*<table class=\"Mso\\w+[\\s\\S]*?<td[\\s\\S]*?<td[\\s\\S]*?<td[\\s\\S]*?<td[^>]*?>([\\s\\S]*?)</td>"));
        info.hint = (Tools.regFind(html, "Note</h1>([\\s\\S]*?)</div>\\s+</body>"));

        info.source = (Tools.regFind(html, "(<div id=\"problem_setter\">[\\s\\S]*?)</div>\\s*</div>\\s*<span id=\"showNavigation\""));

        Validate.isTrue(!StringUtils.isBlank(info.title));
    }

}
