package com.vjudge.ecnuvj.mapper;

import com.vjudge.ecnuvj.entity.RawProblem;
import org.apache.ibatis.annotations.Mapper;
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
     * add or modify raw problem
     *
     * @param rawProblem
     * @return
     */
    int addOrModifyRawProblem(RawProblem rawProblem);

}
