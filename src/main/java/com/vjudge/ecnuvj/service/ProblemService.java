package com.vjudge.ecnuvj.service;

import com.vjudge.ecnuvj.entity.Problem;
import com.vjudge.ecnuvj.entity.ProblemGroup;
import com.vjudge.ecnuvj.entity.RawProblem;
import com.vjudge.ecnuvj.mapper.RawProblemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author tcg
 * @date 2021/5/4
 */
@Service
public class ProblemService {

    @Autowired
    RawProblemMapper rawProblemMapper;

    @Transactional(rollbackFor = Exception.class)
    public void addOrModifyProblem(RawProblem rawProblem) {
        RawProblem tmpProblem = rawProblemMapper.findRawProblemByRemoteOjAndRemoteId(rawProblem.getRemoteOj(), rawProblem.getRemoteProblemId());
        if (tmpProblem == null) {
            rawProblem.setCreatedAt(new Date());
            rawProblem.setUpdatedAt(new Date());
            rawProblem.setTitle("");
            rawProblemMapper.addRawProblem(rawProblem);
        } else {
            rawProblem.setId(tmpProblem.getId());
            rawProblemMapper.updateRawProblem(rawProblem);
            if (rawProblem.getDeletedAt() != null) {
                rawProblemMapper.deleteProblemGroupByRawId(rawProblem.getId());
                rawProblemMapper.deleteProblemByRawId(rawProblem.getId());
            } else if (rawProblem.getTitle() != null && !"".equals(rawProblem.getTitle())) {
                ProblemGroup problemGroup = rawProblemMapper.findProblemGroupByRawId(rawProblem.getId());
                if (problemGroup == null) {
                    problemGroup = new ProblemGroup();
                    problemGroup.setCreatedAt(new Date());
                    problemGroup.setUpdatedAt(new Date());
                    problemGroup.setGroupId(rawProblem.getId());
                    problemGroup.setMainProblem(true);
                    problemGroup.setRawProblemId(rawProblem.getId());
                    problemGroup.setRemoteOj(rawProblem.getRemoteOj());
                    problemGroup.setRemoteProblemId(rawProblem.getRemoteSubmitId());
                    rawProblemMapper.addProblemGroup(problemGroup);
                }
                Problem problem = rawProblemMapper.findProblemByGroupId(problemGroup.getGroupId());
                if (problem == null) {
                    problem = new Problem();
                    problem.setCreatedAt(new Date());
                    problem.setUpdatedAt(new Date());
                    problem.setRawProblemId(rawProblem.getId());
                    problem.setGroupId(rawProblem.getId());
                    problem.setAccepted(0L);
                    problem.setSubmitted(0L);
                    problem.setStatus(0);
                    rawProblemMapper.addProblem(problem);
                }
            }
        }
    }
}
