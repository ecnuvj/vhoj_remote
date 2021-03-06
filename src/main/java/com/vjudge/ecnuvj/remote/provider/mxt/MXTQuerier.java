package com.vjudge.ecnuvj.remote.provider.mxt;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.querier.AuthenticatedQuerier;
import com.vjudge.ecnuvj.remote.status.RemoteStatusType;
import com.vjudge.ecnuvj.remote.status.SubmissionRemoteStatus;
import com.vjudge.ecnuvj.remote.status.SubstringNormalizer;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import com.vjudge.ecnuvj.tool.GsonUtil;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * User: zzzz76
 * Date: 2020-11-22
 */
@Component
public class MXTQuerier extends AuthenticatedQuerier {

    @Override
    public RemoteOjInfo getOjInfo() {
        return MXTInfo.INFO;
    }

    private static final String[] statusArray = new String[]{
            "Pending", "Pending Rejudging", "Compiling", "Running & Judging", "Accepted", "Presentation Error", "Wrong Answer", "Time Limit Exceed", "Memory Limit Exceed", "Output Limit Exceed", "Runtime Error", "Compile Error", "Compile OK", "Test Running Done", "Read"
    };

    @Override
    protected SubmissionRemoteStatus query(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) throws Exception {
        HttpPost post = new HttpPost("/submit/solution/" + info.remoteRunId + "/");
        String post_json = client.execute(post, HttpStatusValidator.SC_OK).getBody();
        GsonUtil gsonUtil = new GsonUtil(post_json);

        SubmissionRemoteStatus status = new SubmissionRemoteStatus();
        status.rawStatus = statusArray[Integer.parseInt(gsonUtil.getStrMem("result"))];
        status.executionTime = Integer.parseInt(gsonUtil.getStrMem("time"));
        status.executionMemory = Integer.parseInt(gsonUtil.getStrMem("memory"));
        // 从原生状态映射到统一状态
        status.statusType = SubstringNormalizer.DEFAULT.getStatusType(status.rawStatus);
        status.failCase = (int) (Double.parseDouble(gsonUtil.getStrMem("pass_rate")) * 100);
        if (status.statusType.finalized && status.statusType != RemoteStatusType.AC
                && status.statusType != RemoteStatusType.CE) {
            status.rawStatus += " on pass rate " + status.failCase + "%";
        }
        return status;
    }

}
