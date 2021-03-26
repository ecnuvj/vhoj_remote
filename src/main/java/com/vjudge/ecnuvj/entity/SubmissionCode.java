package com.vjudge.ecnuvj.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author tcg
 * @date 2021/3/27
 */
@Data
public class SubmissionCode {
    private long id;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private long submissionId;
    private String sourceCode;
    private long codeLength;
}
