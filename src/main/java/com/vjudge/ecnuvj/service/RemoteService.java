package com.vjudge.ecnuvj.service;

import com.vjudge.ecnuvj.bean.Submission;
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

    @Override
    public void submitCode(SubmitCodeRequest request, StreamObserver<SubmitCodeResponse> responseObserver) {
        SubmitCodeResponse res = SubmitCodeResponse.newBuilder().build();
        try {
            Map<String, String> languageMap = languageManager.getLanguages(request.getRemoteOj(), request.getRemoteProblemId());
            Map<Integer, String> languageConverter = languageManager.getLanguagesConverter(request.getRemoteOj());
            //String source = new String(Base64.decodeBase64(request.getSourceCode()), "utf-8");
            String language = languageConverter.get(request.getLanguage());
            if (!languageMap.containsKey(language)) {
                throw new Exception("no such language");
            }
            Submission submission = new Submission();
            submission.setSubTime(new Date());
            submission.setStatus("Pending");
            submission.setStatusCanonical(RemoteStatusType.PENDING.name());
            submission.setLanguage(language);
            submission.setSource(request.getSourceCode());
            submission.setDispLanguage(languageMap.get(language));
            submission.setUsername(request.getUsername());
            submission.setOriginOJ(request.getRemoteOj());
            submission.setUserId(request.getUserId());
            submission.setOriginProb(request.getRemoteProblemId());
            submission.setLanguageCanonical(Tools.getCanonicalLanguage(submission.getDispLanguage()).toString());
            submission.setId(1);
            System.out.println(submission.getStatus());
            submitCodeManager.submitCode(submission);
            res = SubmitCodeResponse.newBuilder().setBaseResponse(
                    Base.BaseResponse.newBuilder().setStatus(Base.REPLY_STATUS.SUCCESS).setMessage("submit success").build()
            ).build();
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
            crawlProblemManager.crawlProblem(request.getRemoteOj(), request.getRemoteProblemId(), request.getEnforce());
            res = CrawlProblemResponse.newBuilder().setBaseResponse(
                    Base.BaseResponse.newBuilder().setStatus(Base.REPLY_STATUS.SUCCESS).build()
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
