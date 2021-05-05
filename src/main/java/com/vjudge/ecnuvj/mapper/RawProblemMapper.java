package com.vjudge.ecnuvj.mapper;

import com.vjudge.ecnuvj.entity.Problem;
import com.vjudge.ecnuvj.entity.ProblemGroup;
import com.vjudge.ecnuvj.entity.RawProblem;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * @author tcg
 * @date 2021/3/27
 */
@Mapper
@Component
public interface RawProblemMapper {
    /**
     * {remoteOj + remoteProblemId} -> rawProblem
     *
     * @param remoteOj
     * @param remoteProblemId
     * @return
     */
    RawProblem findRawProblemByRemoteOjAndRemoteId(int remoteOj, String remoteProblemId);

    /**
     * add raw problem
     *
     * @param rawProblem
     * @return
     */
    Integer addRawProblem(RawProblem rawProblem);

    /**
     * update raw problem
     *
     * @param rawProblem
     * @return
     */
    Integer updateRawProblem(RawProblem rawProblem);

    /**
     * raw problem失效删除group
     *
     * @param rawId
     * @return
     */
    @Delete("delete from problem_groups where raw_problem_id = #{rawId}")
    Integer deleteProblemGroupByRawId(@Param("rawId") Long rawId);

    @Insert("insert into problem_groups(created_at,updated_at,deleted_at,raw_problem_id,group_id,main_problem,remote_oj,remote_problem_id) " +
            "values (#{createdAt},#{updatedAt},#{deletedAt},#{rawProblemId},#{groupId},#{mainProblem},#{remoteOj},#{remoteProblemId})")
    Integer addProblemGroup(ProblemGroup problemGroup);

    @Select("select * from problem_groups where raw_problem_id = #{rawId}")
    ProblemGroup findProblemGroupByRawId(@Param("rawId") Long rawId);

    @Delete("delete from problems where raw_problem_id = #{rawId}")
    Integer deleteProblemByRawId(@Param("rawId") Long rawId);

    @Insert("insert into problems(created_at,updated_at,deleted_at,group_id,raw_problem_id,status,submitted,accepted) " +
            "values(#{createdAt},#{updatedAt},#{deletedAt},#{groupId},#{rawProblemId},#{status},#{submitted},#{accepted})")
    Integer addProblem(Problem problem);

    @Select("select * from problems where raw_problem_id = #{rawId}")
    Problem findProblemByRawId(@Param("rawId") Long rawId);

    @Select("select * from problems where group_id = #{groupId}")
    Problem findProblemByGroupId(@Param("groupId") Long groupId);

    @Select("select * from raw_problems where id = #{rawId}")
    RawProblem findRawById(@Param("rawId") Long rawId);
}
