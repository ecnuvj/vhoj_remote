package com.vjudge.ecnuvj.mapper;

import com.vjudge.ecnuvj.entity.Submission;
import com.vjudge.ecnuvj.entity.SubmissionCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * @author tcg
 * @date 2021/3/27
 */
@Mapper
@Component
public interface SubmissionMapper {
    Integer addSubmission(Submission submission);

    Integer updateSubmission(Submission submission);

    Integer addSubmissionCode(SubmissionCode code);

    @Select("select * from submission_codes where submission_id = #{submissionId}")
    SubmissionCode findSubmissionCodeBySubmissionId(Long submissionId);

    @Update("update problems set accepted = accepted + 1 where id = #{problemId}")
    Integer addProblemAcceptedCountById(Long problemId);

    @Update("update contest_problems set accepted = accepted + 1 where problem_id = #{problemId} and contest_id = #{contestId}")
    Integer addContestProblemAcceptedCountById(@Param("problemId") Long problemId, @Param("contestId") Long contestId);
}
