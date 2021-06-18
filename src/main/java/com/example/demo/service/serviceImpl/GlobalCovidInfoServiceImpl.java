package com.example.demo.service.serviceImpl;

import com.example.demo.dto.GlobalCovidInfoDto;
import com.example.demo.entity.GlobalCovidInfo;
import com.example.demo.repository.GlobalCovidInfoRepository;
import com.example.demo.service.GlobalCovidInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GlobalCovidInfoServiceImpl implements GlobalCovidInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalCovidInfoServiceImpl.class);

    @Autowired
    GlobalCovidInfoRepository globalCovidInfoRepository;

    @Override
    public ResponseEntity<List<GlobalCovidInfoDto>> getAll() {
        LOGGER.info("getAll");
        List<GlobalCovidInfoDto> dtos = new ArrayList<>();

        try {
            List<GlobalCovidInfo> globalCovidInfos = globalCovidInfoRepository.findAll();
            for (GlobalCovidInfo globalCovidInfo : globalCovidInfos) {
                GlobalCovidInfoDto dto = setToDto(globalCovidInfo);
                dtos.add(dto);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }


    @Override
    public ResponseEntity<GlobalCovidInfoDto> getByCountry(String country) {
        LOGGER.info("getByCountry " + country);

        GlobalCovidInfoDto dto = new GlobalCovidInfoDto();
        GlobalCovidInfo globalCovidInfo = null;
        try {

            globalCovidInfo = globalCovidInfoRepository.findByCountry(country);
            if (globalCovidInfo != null) {
                dto = setToDto(globalCovidInfo);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return globalCovidInfo != null ? ResponseEntity.status(HttpStatus.OK).body(dto) : ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * cache get clear when new record entered
     **/
    @CacheEvict(value = "getMapByRegion", allEntries = true)
    public ResponseEntity<String> save(GlobalCovidInfoDto dto) {
        LOGGER.info("save");

        String message = "";

        try {
            if ((dto.getCountry() == null || Objects.equals(dto.getCountry(), ""))) {
                message = "Country cannot be empty!";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            } else {
                GlobalCovidInfo globalCovidInfo = globalCovidInfoRepository.findByCountry(dto.getCountry());
                if (globalCovidInfo == null) {
                    GlobalCovidInfo newGlobalCovidInfo = new GlobalCovidInfo();
                    message = setToEntity(dto, newGlobalCovidInfo, "save");
                    if (message.equals("")) {
                        globalCovidInfoRepository.save(newGlobalCovidInfo);
                        message = "Saved Successfully!";
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
                    }
                } else {
                    message = "Record already exist for country!";
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    public GlobalCovidInfoDto setToDto(GlobalCovidInfo globalCovidInfo) throws Exception {
        LOGGER.info("setToDto " + globalCovidInfo.getCountry());

        GlobalCovidInfoDto dto = new GlobalCovidInfoDto();

        try {
            dto.setCountry(globalCovidInfo.getCountry());
            dto.setRegion(globalCovidInfo.getRegion());
            dto.setCases(globalCovidInfo.getCases());
            dto.setDeaths(globalCovidInfo.getDeaths());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

        return dto;
    }

    public String setToEntity(GlobalCovidInfoDto dto, GlobalCovidInfo globalCovidInfo, String event) throws Exception {
        LOGGER.info("setToEntity");

        String message = "";

        try {

            if (event.equals("save")) {
                if (dto.getCountry() == null || Objects.equals(dto.getCountry(), "")) {
                    return "Country cannot be empty!";
                } else {
                    globalCovidInfo.setCountry(dto.getCountry());
                }
                if (dto.getRegion() == null || Objects.equals(dto.getRegion(), "")) {
                    return "Region cannot be empty!";
                } else {
                    globalCovidInfo.setRegion(dto.getRegion());
                }
            }
            if (dto.getCases() == null || Objects.equals(dto.getCases(), "")) {
                globalCovidInfo.setCases(0);
            } else {
                globalCovidInfo.setCases(dto.getCases());
            }
            if (dto.getDeaths() == null || Objects.equals(dto.getDeaths(), "")) {
                globalCovidInfo.setDeaths(0);
            } else {
                globalCovidInfo.setDeaths(dto.getDeaths());
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        return message;
    }

    /**
     * cache get clear when record get updated
     **/
    @CacheEvict(value = "getMapByRegion", allEntries = true)
    public ResponseEntity<String> update(GlobalCovidInfoDto dto) {
        LOGGER.info("update ");

        String message = "";

        try {
            if ((dto.getCountry() == null || Objects.equals(dto.getCountry(), ""))) {
                message = "Country cannot be empty!";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            } else {
                GlobalCovidInfo globalCovidInfo = globalCovidInfoRepository.findByCountry(dto.getCountry());
                if (globalCovidInfo == null) {
                    message = "Record not found!";
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
                }
                message = setToEntity(dto, globalCovidInfo, "update");
                if (message.equals("")) {
                    globalCovidInfoRepository.save(globalCovidInfo);
                    message = "Successfully Updated!";
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    /**
     * HashMap being used to hold and structure the data (data structuring)
     * cache the method to access it faster (performance)
     **/
    @Cacheable(cacheNames = "getMapByRegion")
    public Map<String, List<GlobalCovidInfoDto.GlobalCovidInfoByRegion>> getMapByRegion() {
        LOGGER.info("getMapByRegion");

        Map<String, List<GlobalCovidInfoDto.GlobalCovidInfoByRegion>> mapByRegion = new HashMap<>();

        try {

            List<GlobalCovidInfo> globalCovidInfos = globalCovidInfoRepository.findAll();
            for (GlobalCovidInfo globalCovidInfo : globalCovidInfos) {

                if (!mapByRegion.containsKey(globalCovidInfo.getRegion())) {
                    List<GlobalCovidInfoDto.GlobalCovidInfoByRegion> globalCovidInfoList = new ArrayList<>();
                    GlobalCovidInfoDto.GlobalCovidInfoByRegion dto = setDtoByRegion(globalCovidInfo);
                    globalCovidInfoList.add(dto);
                    mapByRegion.put(globalCovidInfo.getRegion(), globalCovidInfoList);
                } else {
                    List<GlobalCovidInfoDto.GlobalCovidInfoByRegion> covidInfoByRegionList = mapByRegion.get(globalCovidInfo.getRegion());
                    GlobalCovidInfoDto.GlobalCovidInfoByRegion dto = setDtoByRegion(globalCovidInfo);
                    covidInfoByRegionList.add(dto);
                    mapByRegion.remove(globalCovidInfo.getRegion());
                    mapByRegion.put(globalCovidInfo.getRegion(), covidInfoByRegionList);
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return mapByRegion;
    }

    public GlobalCovidInfoDto.GlobalCovidInfoByRegion setDtoByRegion(GlobalCovidInfo globalCovidInfo) {
        LOGGER.info("setToDto");

        GlobalCovidInfoDto.GlobalCovidInfoByRegion dto = new GlobalCovidInfoDto.GlobalCovidInfoByRegion();

        try {
            dto.setCountry(globalCovidInfo.getCountry());
            dto.setCases(globalCovidInfo.getCases());
            dto.setDeaths(globalCovidInfo.getDeaths());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return dto;
    }


    @Override
    public ResponseEntity<GlobalCovidInfoDto> findMaximumCases() {
        LOGGER.info("findMaximumCases");
        GlobalCovidInfoDto dto = new GlobalCovidInfoDto();

        try {
            GlobalCovidInfo globalCovidInfo = globalCovidInfoRepository.findTopByOrderByCasesDesc();
            dto = setToDto(globalCovidInfo);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

}
