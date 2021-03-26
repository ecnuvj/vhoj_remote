package com.vjudge.ecnuvj;

import com.vjudge.ecnuvj.entity.RawProblem;
import com.vjudge.ecnuvj.mapper.RawProblemMapper;
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

    @Test
    void testMapper() {
        RawProblem rawProblem = rawProblemMapper.findRawProblemByRemoteOjAndRemoteId(2, "1001");
        System.out.println(rawProblem);
    }
}
