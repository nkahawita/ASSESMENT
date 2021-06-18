package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "global_covid_info")
public class GlobalCovidInfo {

    @Id
    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "REGION")
    private String region;

    @Column(name = "CASES")
    private Integer cases;

    @Column(name = "DEATHS")
    private Integer deaths;

}
