package com.vjudge.ecnuvj.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author tcg
 * @date 2021/3/27
 */
@Data
public class Submission {
    private long id;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private long problemId;
    private long userId;
    private String username;
    private int result;
    private long timeCost;
    private long memoryCost;
    private int language;
    private long contestId;
    private int remoteOj;
    private String realRunId;
}
