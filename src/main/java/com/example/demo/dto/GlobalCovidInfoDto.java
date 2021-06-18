package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GlobalCovidInfoDto {

    private String country;
    private String region;
    private Integer cases;
    private Integer deaths;


    @Getter
    @Setter
    public static class GlobalCovidInfoByRegion implements Serializable {

        private String country;
        private Integer cases;
        private Integer deaths;
    }

}


