package com.vjudge.ecnuvj.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author tcg
 * @date 2021/5/4
 */
@Data
public class ProblemGroup {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Long rawProblemId;
    private Long groupId;
    private boolean mainProblem;
    private Integer remoteOj;
    private String remoteProblemId;
}
