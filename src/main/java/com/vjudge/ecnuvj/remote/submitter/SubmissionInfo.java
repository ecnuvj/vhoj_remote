package com.vjudge.ecnuvj.remote.submitter;

import com.vjudge.ecnuvj.remote.common.RemoteOj;

import java.util.Date;

public class SubmissionInfo {

    public RemoteOj remoteOj;

    public String remoteProblemId;

    /**
     * the language symbol of the remote OJ
     */
    public String remotelanguage;

    public String sourceCode;

    /**
     * leave null if any public remote account is eligible.
     * After submitting, it will set to the defacto account id.
     */
    public String remoteAccountId;

    public String remoteRunId;

    public Date remoteSubmitTime;

}
