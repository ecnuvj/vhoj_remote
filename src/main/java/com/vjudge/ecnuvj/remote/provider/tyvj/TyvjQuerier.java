package com.vjudge.ecnuvj.remote.provider.tyvj;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.querier.AuthenticatedQuerier;
import com.vjudge.ecnuvj.remote.status.RemoteStatusType;
import com.vjudge.ecnuvj.remote.status.SubmissionRemoteStatus;
import com.vjudge.ecnuvj.remote.status.SubstringNormalizer;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import org.apache.struts2.json.JSONException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TyvjQuerier extends AuthenticatedQuerier {

    private static final String[] STATUS = {"Accepted", "Presentation Error", "Wrong Answer", "Output Limit Exceeded",
            "Time Limit Exceeded", "Memory Limit Exceeded", "Runtime Error", "Compile Error",
            "System Error", "Hacked", "Running", "Running", "Hidden"};

    @Override
    public RemoteOjInfo getOjInfo() {
        return TyvjInfo.INFO;
    }

    @Override
    protected SubmissionRemoteStatus query(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) throws JSONException {
        Map<String, String>[] json = new Gson().fromJson(client.get("/Status/GetStatusDetails/" + info.remoteRunId).getBody(),
                new TypeToken<HashMap<String, String>[]>() {
                }.getType());

        SubmissionRemoteStatus status = new SubmissionRemoteStatus();

        int resultInt = -1;
        for (Map<String, String> testcase : json) {
            resultInt = Math.max(resultInt, Integer.parseInt(testcase.get("Result")));
        }
        if (resultInt < 0 || resultInt >= STATUS.length) {
            status.rawStatus = "Wait";
        } else {
            status.rawStatus = STATUS[resultInt];
        }
        status.statusType = SubstringNormalizer.DEFAULT.getStatusType(status.rawStatus);

        if (status.statusType == RemoteStatusType.AC) {
            int memoryUsage = 0;
            int timeUsage = 0;
            for (Map<String, String> testcase : json) {
                memoryUsage = Math.max(memoryUsage, Integer.parseInt(testcase.get("MemoryUsage")));
                timeUsage += Integer.parseInt(testcase.get("TimeUsage"));
            }
            status.executionMemory = memoryUsage;
            status.executionTime = timeUsage;
        } else if (status.statusType == RemoteStatusType.CE) {
            status.compilationErrorInfo = "<pre>" + json[0].get("Hint").replace("\n", "<br>") + "</pre>";
        }

        return status;
    }

}
