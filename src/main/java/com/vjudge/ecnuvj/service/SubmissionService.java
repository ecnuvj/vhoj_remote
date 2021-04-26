package com.vjudge.ecnuvj.service;

import com.vjudge.ecnuvj.bean.Submission;
import com.vjudge.ecnuvj.entity.SubmissionCode;
import com.vjudge.ecnuvj.mapper.SubmissionMapper;
import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.status.RemoteStatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author tcg
 * @date 2021/4/25
 * 原submission耦合太重不好修改，将原submission转化数据库submission
 */
@Service
public class SubmissionService {
    @Autowired
    SubmissionMapper submissionMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long addOrModifySubmission(Submission info) {
        com.vjudge.ecnuvj.entity.Submission submission = new com.vjudge.ecnuvj.entity.Submission();
        submission.setContestId(info.getContestId());
        submission.setUserId(info.getUserId());
        submission.setUsername(info.getUsername());
        submission.setProblemId(info.getProblemId());
        submission.setTimeCost(info.getTime());
        submission.setMemoryCost(info.getMemory());
        submission.setLanguage(info.getLangCode());
        if (info.getSubTime() == null) {
            submission.setCreatedAt(new Date());
        } else {
            submission.setCreatedAt(info.getSubTime());
        }
        submission.setUpdatedAt(info.getStatusUpdateTime());
        submission.setRemoteOj(RemoteOj.valueOf(info.getOriginOJ()).getCode());
        submission.setRealRunId(info.getRealRunId());
        submission.setResult(RemoteStatusType.valueOf(info.getStatusCanonical()).code);
        submissionMapper.addOrModifySubmission(submission);

        SubmissionCode code = submissionMapper.findSubmissionCodeBySubmissionId(submission.getId());
        if (code == null) {
            code = new SubmissionCode();
            code.setCreatedAt(new Date());
        }
        code.setUpdatedAt(new Date());
        code.setSourceCode(info.getSource());
        code.setSubmissionId(submission.getId());
        submissionMapper.addOrModifySubmissionCode(code);

        return submission.getId();
    }
}
