package com.vjudge.ecnuvj.mapper;

import com.vjudge.ecnuvj.entity.Submission;
import com.vjudge.ecnuvj.entity.SubmissionCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author tcg
 * @date 2021/3/27
 */
@Mapper
@Component
public interface SubmissionMapper {
    Integer addOrModifySubmission(Submission submission);

    Integer addOrModifySubmissionCode(SubmissionCode code);

    @Select("select * from submission_codes where submission_id = #{submissionId}")
    SubmissionCode findSubmissionCodeBySubmissionId(Long submissionId);
}
