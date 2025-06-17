package com.businessArea.businessArea.controller;

import com.businessArea.businessArea.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/addresses") // 관리자용 주소 API
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/update") // POST /api/admin/addresses/update
    public ResponseEntity<String> updateAllAddresses() {
        try {
            addressService.fetchAndSaveAllAddressData();
            return ResponseEntity.ok("주소 데이터베이스 업데이트를 성공적으로 완료했습니다.");
        } catch (Exception e) {
            // 구체적인 예외 처리를 하는 것이 좋습니다.
            return ResponseEntity.internalServerError().body("주소 데이터 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 각 행정동의 경계(좌표) 데이터를 DB에 저장/업데이트하는 API
     */
    @PostMapping("/update-boundaries")
    public ResponseEntity<String> updateAllBoundaries() {
        try {
            addressService.fetchAndSaveBoundaries();
            return ResponseEntity.ok("행정동 경계 데이터 업데이트를 성공적으로 완료했습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("경계 데이터 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}