package com.vjudge.ecnuvj.remote.provider.mxt;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.crawler.AuthenticatedCrawler;
import com.vjudge.ecnuvj.remote.crawler.RawProblemInfo;
import com.vjudge.ecnuvj.tool.GsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * User: zzzz76
 * Date: 2020-11-28
 */
@Component
public class MXTCrawler extends AuthenticatedCrawler {

    @Override
    public RemoteOjInfo getOjInfo() {
        return MXTInfo.INFO;
    }

    @Override
    protected void preValidate(String problemId) {
        Validate.isTrue(problemId.matches("[1-9]\\d*"));
    }

    @Override
    protected RawProblemInfo crawl(String problemId, RemoteAccount remoteAccount, DedicatedHttpClient client) throws Exception {
        HttpPost post = new HttpPost("/problem/7/" + problemId + "/one");
        String post_json = client.execute(post, HttpStatusValidator.SC_OK).getBody();
        GsonUtil gsonUtil = new GsonUtil(post_json);

        RawProblemInfo info = new RawProblemInfo();
        info.title = gsonUtil.getStrMem("title");
        info.timeLimit = Integer.parseInt(gsonUtil.getStrMem("time_limit")) * 1000;
        info.memoryLimit = Integer.parseInt(gsonUtil.getStrMem("memory_limit")) * 1024;
        info.description = "<link rel=\"stylesheet\" href=\"https://www.maxuetang.cn/lxojres/mxt-editor/css/mxt-editor.min.css?v=20190117\"/>";
        info.description += gsonUtil.getStrMem("description");
        info.input = gsonUtil.getStrMem("input");
        info.output = gsonUtil.getStrMem("output");
        // 原生页面在此处进行了‘<>’的符号替换，此处尚未加以处理
        info.sampleInput = "<pre>" + gsonUtil.getStrMem("sample_input") + "</pre>";
        info.sampleOutput = "<pre>" + gsonUtil.getStrMem("sample_output") + "</pre>";
        info.hint = gsonUtil.getStrMem("hint");
        info.source = gsonUtil.getStrMem("source");
        info.url = getHost().toURI() + "/course/" + problemId + ".html";

        Validate.isTrue(!StringUtils.isBlank(info.title));
        Validate.isTrue(info.timeLimit > 2);
        return info;
    }
}
