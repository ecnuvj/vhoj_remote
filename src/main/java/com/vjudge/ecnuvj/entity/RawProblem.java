package com.vjudge.ecnuvj.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author tcg
 * @date 2021/3/27
 */
@Data
public class RawProblem {
    private long id;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private String title;
    private String description;
    private String sampleInput;
    private String sampleOutput;
    private String input;
    private String output;
    private String hint;
    private int remoteOj;
    private String remoteProblemId;
    private String timeLimit;
    private String memoryLimit;
    private String spj;
    private String std;
    private String source;
}
