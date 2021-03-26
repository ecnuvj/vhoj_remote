package com.vjudge.ecnuvj.remote.provider.local;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpBodyValidator;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.querier.AuthenticatedQuerier;
import com.vjudge.ecnuvj.remote.status.RemoteStatusType;
import com.vjudge.ecnuvj.remote.status.SubmissionRemoteStatus;
import com.vjudge.ecnuvj.remote.status.SubstringNormalizer;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

@Component
public class LOCALQuerier extends AuthenticatedQuerier {

    @Override
    public RemoteOjInfo getOjInfo() {
        return LOCALInfo.INFO;
    }

    String[] result = {"Pending", "Pending Rejudging", "Compiling", "Running Judging", "Accepted", "Presentation Error", "Wrong Answer", "Time Limit Exceed", "Memory Limit Exceed", "Output Limit Exceed", "Runtime Error", "Compile Error", "Compile OK"};

    @Override
    protected SubmissionRemoteStatus query(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        String html = client.get(
                LOCALInfo.getPath() + "/status-ajax.php?solution_id=" + info.remoteRunId,
                new HttpBodyValidator("<title>Error</title>", true)).getBody();
//    	 System.out.println(html);
        String[] s = html.split(",");

        SubmissionRemoteStatus status = new SubmissionRemoteStatus();
        try {
            status.rawStatus = result[Integer.parseInt(s[0])];
            status.statusType = SubstringNormalizer.DEFAULT.getStatusType(status.rawStatus);
            if (status.statusType == RemoteStatusType.AC) {
                status.executionMemory = Integer.parseInt(s[1].split(" ")[0]);
                status.executionTime = Integer.parseInt(s[2].split(" ")[0]);
            } else if (status.statusType == RemoteStatusType.CE) {

                Validate.isTrue(result[Integer.parseInt(s[0])].contains("Compile Error"));
                status.compilationErrorInfo = client.get(
                        LOCALInfo.getPath() + "/ceinfo.php?sid=" + info.remoteRunId,
                        new HttpBodyValidator("<title>Error</title>", true)).getBody().substring(3277);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[" + s[2] + "]");
            System.out.println(html);
        }
        return status;
    }

}
