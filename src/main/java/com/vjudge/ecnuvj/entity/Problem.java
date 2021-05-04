package com.vjudge.ecnuvj.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author tcg
 * @date 2021/5/4
 */
@Data
public class Problem {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Long groupId;
    private Long rawProblemId;
    private Integer status;
    private Long submitted;
    private Long accepted;
}
