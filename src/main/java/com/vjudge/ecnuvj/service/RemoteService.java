package com.vjudge.ecnuvj.service;

import com.vjudge.ecnuvj.bean.Submission;
import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.manager.CrawlProblemManager;
import com.vjudge.ecnuvj.remote.manager.LanguageManager;
import com.vjudge.ecnuvj.remote.manager.SubmitCodeManager;
import com.vjudge.ecnuvj.remote.status.RemoteStatusType;
import com.vjudge.ecnuvj.rpc.base.Base;
import com.vjudge.ecnuvj.rpc.service.*;
import com.vjudge.ecnuvj.tool.Tools;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * @author tcg
 * @date 2021/3/24
 */
@GRpcService
public class RemoteService extends RemoteServiceGrpc.RemoteServiceImplBase {

    @Autowired
    SubmitCodeManager submitCodeManager;

    @Autowired
    CrawlProblemManager crawlProblemManager;

    @Autowired
    LanguageManager languageManager;

    @Autowired
    SubmissionService submissionService;

    @Override
    public void submitCode(SubmitCodeRequest request, StreamObserver<SubmitCodeResponse> responseObserver) {
        SubmitCodeResponse res = SubmitCodeResponse.newBuilder().build();
        try {
            RemoteOj remoteOj = RemoteOj.codeValueOf(request.getRemoteOj());
            //remote oj lang id (str) -> lang name ("1" -> "C++")
            Map<String, String> languageMap = languageManager.getLanguages(remoteOj.name(), request.getRemoteProblemId());
            //统一语言编码 -> remote oj lang id (str)
            Map<Integer, String> languageConverter = languageManager.getLanguagesConverter(remoteOj.name());
            String language = languageConverter.get(request.getLanguage());
            if (!languageMap.containsKey(language)) {
                throw new Exception("no such language");
            }
            Submission submission = new Submission();
            submission.setSubTime(new Date());
            submission.setStatus("Pending");
            submission.setStatusCanonical(RemoteStatusType.PENDING.name());
            submission.setLanguage(language);
            submission.setDispLanguage(languageMap.get(language));
            submission.setSource(request.getSourceCode());
            submission.setLangCode(request.getLanguage());
            submission.setUsername(request.getUsername());
            submission.setOriginOJ(remoteOj.name());
            submission.setUserId(request.getUserId());
            submission.setProblemId(request.getProblemId());
            submission.setOriginProb(request.getRemoteProblemId());
            submission.setLanguageCanonical(Tools.getCanonicalLanguage(submission.getDispLanguage()).toString());
            submission.setContestId(request.getContestId());
            Long id = submissionService.addOrModifySubmission(submission);
            submission.setId(id.intValue());
            //System.out.println(submission.getStatus());
            submitCodeManager.submitCode(submission);
            res = SubmitCodeResponse.newBuilder().setBaseResponse(
                    Base.BaseResponse.newBuilder().setStatus(Base.REPLY_STATUS.SUCCESS).setMessage("submit success").build()
            ).setSubmissionId(id).build();
        } catch (Exception e) {
            res = SubmitCodeResponse.newBuilder().setBaseResponse(
                    Base.BaseResponse.newBuilder().setStatus(Base.REPLY_STATUS.FAILURE).setMessage(e.toString()).build()
            ).build();
        } finally {
            responseObserver.onNext(res);
            responseObserver.onCompleted();
        }

    }

    @Override
    public void crawlProblem(CrawlProblemRequest request, StreamObserver<CrawlProblemResponse> responseObserver) {
        CrawlProblemResponse res = CrawlProblemResponse.newBuilder().build();
        try {
            RemoteOj remoteOj = RemoteOj.codeValueOf(request.getRemoteOj());
            crawlProblemManager.crawlProblem(remoteOj, request.getRemoteProblemId(), request.getEnforce());
            res = CrawlProblemResponse.newBuilder().setBaseResponse(
                    Base.BaseResponse.newBuilder().setStatus(Base.REPLY_STATUS.SUCCESS).setMessage("crawl success").build()
            ).build();
        } catch (Exception e) {
            res = CrawlProblemResponse.newBuilder().setBaseResponse(
                    Base.BaseResponse.newBuilder().setStatus(Base.REPLY_STATUS.FAILURE).setMessage(e.getMessage()).build()
            ).build();
        } finally {
            responseObserver.onNext(res);
            responseObserver.onCompleted();
        }
    }
}
