package com.example.demo;

import com.example.demo.entity.GlobalCovidInfo;
import com.example.demo.repository.GlobalCovidInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AssessmentApplicationTests {


    @Autowired
    GlobalCovidInfoRepository globalCovidInfoRepository;

    @Test
    void findMaximumCasesTest() {

        try {
            GlobalCovidInfo globalCovidInfo = globalCovidInfoRepository.findTopByOrderByCasesDesc();
            Assertions.assertEquals("United States of America", globalCovidInfo.getCountry());
        } catch (Exception e) {
            Assertions.fail();
        }

    }

}
