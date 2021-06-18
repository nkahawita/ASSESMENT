package com.example.demo.repository;

import com.example.demo.entity.GlobalCovidInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface GlobalCovidInfoRepository extends JpaRepository<GlobalCovidInfo, String> {

    GlobalCovidInfo findByCountry(String country);

    GlobalCovidInfo findTopByOrderByCasesDesc();


}
