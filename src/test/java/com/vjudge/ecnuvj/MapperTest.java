package com.vjudge.ecnuvj;

import com.vjudge.ecnuvj.entity.RawProblem;
import com.vjudge.ecnuvj.mapper.RawProblemMapper;
import com.vjudge.ecnuvj.service.SubmissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author tcg
 * @date 2021/3/27
 */
@SpringBootTest
public class MapperTest {
    @Autowired
    RawProblemMapper rawProblemMapper;

    @Autowired
    SubmissionService submissionService;

    @Test
    void testMapper() {
        RawProblem rawProblem = rawProblemMapper.findRawProblemByRemoteOjAndRemoteId(2, "2000");
        System.out.println(rawProblem.getUpdatedAt());
    }

    @Test
    void testInsertMapper() {

        //rawProblemMapper.addOrModifyRawProblem(new RawProblem());
    }
}
