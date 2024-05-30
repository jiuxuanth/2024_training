package com.jiuth.sysmonitorserver.controller;

import com.jiuth.sysmonitorserver.enity.SysInfoCapture;
import com.jiuth.sysmonitorserver.dto.SysInfoCaptureDTO;
import com.jiuth.sysmonitorserver.service.SysInfoCaptureService;
import com.jiuth.sysmonitorserver.util.ApiResponse;
import com.jiuth.sysmonitorserver.util.converter.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metric")
public class SysInfoCaptureController {

    @Autowired
    private SysInfoCaptureService sysInfoCaptureService;

    @GetMapping
    public ApiResponse<List<SysInfoCapture>> getAllSysInfo() {
        try {
            List<SysInfoCapture> sysInfoCaptures = sysInfoCaptureService.findAll();
            return ApiResponse.success(sysInfoCaptures);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ApiResponse<Void> createSysInfo(@RequestBody List<SysInfoCaptureDTO> sysInfoCaptureDTOs) {
        try {
//            List<SysInfoCapture> sysInfoCaptures = sysInfoCaptureDTOs.stream().map(dto -> {
//                SysInfoCapture entity = new SysInfoCapture();
//                entity.setMetric(dto.getMetric());
//                entity.setEndpoint(dto.getEndpoint());
//                entity.setTimestamp(dto.getTimestamp());
//                entity.setStep(dto.getStep());
//                entity.setValue(dto.getValue());
//                return entity;
//            }).collect(Collectors.toList());

            List<SysInfoCapture> sysInfoCaptures = Convert.toEntityList(sysInfoCaptureDTOs);
//            sysInfoCaptureService.saveAll(sysInfoCaptures);
//            return ResponseEntity.ok(ApiResponse.success(null));

            sysInfoCaptureService.saveAll(sysInfoCaptures);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<SysInfoCapture> getSysInfoById(@PathVariable Long id) {
        try {
            SysInfoCapture sysInfoCapture = sysInfoCaptureService.findById(id);
            if (sysInfoCapture != null) {
                return ApiResponse.success(sysInfoCapture);
            } else {
                return ApiResponse.error("Data not found");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSysInfo(@PathVariable Long id) {
        try {
            sysInfoCaptureService.deleteById(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}

//@RestController
//@RequestMapping("/api/sys-info")
//public class SysInfoCaptureController {
//
//    @Autowired
//    private SysInfoCaptureService sysInfoCaptureService;
//
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<SysInfoCapture>>> getAllSysInfo() {
//        try {
//            List<SysInfoCapture> sysInfoCaptures = sysInfoCaptureService.findAll();
//            return ResponseEntity.ok(ApiResponse.success(sysInfoCaptures));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(e.getMessage()));
//        }
//    }
//
//    @PostMapping
//    public ResponseEntity<ApiResponse<Void>> createSysInfo(@RequestBody List<SysInfoCaptureDTO> sysInfoCaptureDTOs) {
//        try {
//            List<SysInfoCapture> sysInfoCaptures = Convert.toEntityList(sysInfoCaptureDTOs);
//            sysInfoCaptureService.saveAll(sysInfoCaptures);
//            return ResponseEntity.ok(ApiResponse.success(null));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(e.getMessage()));
//        }
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<SysInfoCapture>> getSysInfoById(@PathVariable Long id) {
//        try {
//            SysInfoCapture sysInfoCapture = sysInfoCaptureService.findById(id);
//            if (sysInfoCapture != null) {
//                return ResponseEntity.ok(ApiResponse.success(sysInfoCapture));
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Data not found"));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(e.getMessage()));
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse<Void>> deleteSysInfo(@PathVariable Long id) {
//        try {
//            sysInfoCaptureService.deleteById(id);
//            return ResponseEntity.ok(ApiResponse.success(null));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(e.getMessage()));
//        }
//    }
//}