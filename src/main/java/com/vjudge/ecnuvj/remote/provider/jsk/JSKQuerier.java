package com.vjudge.ecnuvj.remote.provider.jsk;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.querier.AuthenticatedQuerier;
import com.vjudge.ecnuvj.remote.status.RemoteStatusType;
import com.vjudge.ecnuvj.remote.status.SubmissionRemoteStatus;
import com.vjudge.ecnuvj.remote.status.SubstringNormalizer;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import com.vjudge.ecnuvj.tool.GsonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * User: zzzz76
 * Date: 2020-10-13
 */
@Component
public class JSKQuerier extends AuthenticatedQuerier {

    @Override
    public RemoteOjInfo getOjInfo() {
        return JSKInfo.INFO;
    }

    // 统计计蒜客的所有静态原生状态 --> unkown error 暂时未统计
    private static final Map<String, String> statusMap = new HashMap<String, String>() {{
        put("AC", "Accepted");
        put("PE", "Presentation Error");
        put("WA", "Wrong Answer");
        put("TL", "Time Limit Exceeded");
        put("ML", "Memory Limit Exceeded");
        put("OL", "Output Limit Exceeded");
        put("RE", "Runtime Error");
        put("SF", "Segmentation Fault");
        put("AE", "Arithmetical Error");
        put("CE", "Compilation Error");
        put("pending", "Queuing");
        put("fail", "Submit Failed");
    }};

    @Override
    protected SubmissionRemoteStatus query(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) throws Exception {

        String token = JSKTokenUtil.getToken(client);
        HttpEntity entity = SimpleNameValueEntityFactory.create(
                "id", info.remoteProblemId
        );
        HttpPost post = new HttpPost("/solve/check/" + info.remoteRunId);
        post.setEntity(entity);
        post.setHeader("X-Requested-With", "XMLHttpRequest");
        post.setHeader("X-XSRF-TOKEN", token);
        String post_json = client.execute(post, HttpStatusValidator.SC_OK).getBody();
        GsonUtil gsonUtil = new GsonUtil(post_json);
        String status_code = gsonUtil.getStrMem("status");
        if ("finished".equals(status_code)) {
            status_code = gsonUtil.getStrMem("reason", "data");
        }

        SubmissionRemoteStatus status = new SubmissionRemoteStatus();
        status.rawStatus = statusMap.getOrDefault(status_code, status_code);
        status.statusType = SubstringNormalizer.DEFAULT.getStatusType(status.rawStatus);

        if (status.statusType == RemoteStatusType.AC) {
            // 补充执行时间和内存
            HttpGet get = new HttpGet("/t/" + info.remoteProblemId + "/submissions");
            get.setHeader("X-Requested-With", "XMLHttpRequest");
            get.setHeader("X-XSRF-TOKEN", token);
            String get_json = client.execute(get, HttpStatusValidator.SC_OK).getBody();
            List<Map<String, Object>> list = GsonUtil.jsonToListMaps(new GsonUtil(get_json).getStrMem("data"));
            status.executionTime = ((Double) list.get(0).get("used_time")).intValue();
            status.executionMemory = ((Double) list.get(0).get("used_mem")).intValue();

        } else if (status.statusType == RemoteStatusType.CE) {
            // 补充编译错误信息
            status.compilationErrorInfo = gsonUtil.getStrMem("message", "data");
        }
        return status;
    }

}
