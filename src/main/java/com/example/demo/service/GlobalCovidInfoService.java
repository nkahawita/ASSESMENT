package com.example.demo.service;

import com.example.demo.dto.GlobalCovidInfoDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface GlobalCovidInfoService {
    ResponseEntity<List<GlobalCovidInfoDto>> getAll();

    ResponseEntity<GlobalCovidInfoDto> getByCountry(String country);

    ResponseEntity<String> save(GlobalCovidInfoDto dto);

    ResponseEntity<String> update(GlobalCovidInfoDto dto);

    Map<String, List<GlobalCovidInfoDto.GlobalCovidInfoByRegion>> getMapByRegion();

    ResponseEntity<GlobalCovidInfoDto> findMaximumCases();
}
