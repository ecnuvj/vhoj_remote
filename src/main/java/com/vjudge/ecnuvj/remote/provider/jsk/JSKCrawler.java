package com.vjudge.ecnuvj.remote.provider.jsk;

import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.crawler.RawProblemInfo;
import com.vjudge.ecnuvj.remote.crawler.SimpleCrawler;
import com.vjudge.ecnuvj.tool.GsonUtil;
import com.vjudge.ecnuvj.tool.Tools;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * User: zzzz76
 * Date: 2020-10-03
 */

@Component
public class JSKCrawler extends SimpleCrawler {

    @Override
    public RemoteOjInfo getOjInfo() {
        return JSKInfo.INFO;
    }

    @Override
    protected String getProblemUrl(String problemId) {
        return getHost().toURI() + "/t/" + problemId;
    }

    @Override
    protected void preValidate(String problemId) {
        Validate.isTrue(problemId.matches("[A|T][1-9]\\d*"));
    }

    @Override
    protected void populateProblemInfo(RawProblemInfo info, String problemId, String html) {
        String json = Tools.regFind(html, "var problem=(\\{[\\s\\S]*?\\});var").trim();
        GsonUtil gsonUtil = new GsonUtil(json);

        info.title = gsonUtil.getStrMem("title");
        info.timeLimit = Integer.parseInt(gsonUtil.getStrMem("time_limit"));
        info.memoryLimit = Integer.parseInt(gsonUtil.getStrMem("mem_limit"));
        info.description = gsonUtil.getStrMem("description");
        info.sampleInput = gsonUtil.getStrMem("sample_input");
        info.sampleOutput = gsonUtil.getStrMem("sample_output");
        info.hint = gsonUtil.getStrMem("hint");
        info.source = gsonUtil.getStrMem("source");
        info.remoteSubmitId = Tools.regFind(html, "wenda.jisuanke.com/nanti/(\\d*)");
        System.out.println("jsk=========" + info.remoteSubmitId);
    }
}
