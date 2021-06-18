package com.example.demo.controller;

import com.example.demo.dto.GlobalCovidInfoDto;
import com.example.demo.service.GlobalCovidInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/covid19")
public class GlobalCovidInfoController {


    @Autowired
    GlobalCovidInfoService globalCovidInfoService;

    @GetMapping("/getAll")
    ResponseEntity<List<GlobalCovidInfoDto>> getAll() {
        return globalCovidInfoService.getAll();
    }

    @GetMapping("/getByCountry/{country}")
    ResponseEntity<GlobalCovidInfoDto> getByCountry(@PathVariable("country") String country) {
        return globalCovidInfoService.getByCountry(country);
    }

    @PostMapping("/save")
    ResponseEntity<String> save(@RequestBody GlobalCovidInfoDto dto) {
        return globalCovidInfoService.save(dto);
    }

    @PatchMapping("/update")
    ResponseEntity<String> update(@RequestBody GlobalCovidInfoDto dto) {
        return globalCovidInfoService.update(dto);
    }

    @GetMapping("/getMapByRegion")
    Map<String, List<GlobalCovidInfoDto.GlobalCovidInfoByRegion>> getMapByRegion() {
        return globalCovidInfoService.getMapByRegion();
    }

    @GetMapping("/findMaximumCases")
    ResponseEntity<GlobalCovidInfoDto> findMaximumCases() {
        return globalCovidInfoService.findMaximumCases();
    }

}
