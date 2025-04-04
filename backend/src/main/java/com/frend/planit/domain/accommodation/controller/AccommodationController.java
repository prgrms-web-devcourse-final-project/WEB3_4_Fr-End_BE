package com.frend.planit.domain.accommodation.controller;

import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import com.frend.planit.domain.accommodation.dto.response.AccommodationResponseDto;
import com.frend.planit.domain.accommodation.service.AccommodationService;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

   //숙소 조회 (모두 가능)
    @GetMapping("/{id}")
    public ResponseEntity<AccommodationResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(accommodationService.findById(id));
    }

    //숙소 생성(관리자)
    @PostMapping
    public ResponseEntity<AccommodationResponseDto> create(
            @RequestParam(defaultValue = "false") boolean admin,
            @Valid @RequestBody AccommodationRequestDto dto
    ) {
        validateAdmin(admin);
        AccommodationResponseDto created = accommodationService.create(dto);
        URI location = URI.create("/api/accommodations/" + created.id());
        return ResponseEntity.created(location).body(created); // 201 Created 반환
    }

    // 숙소 수정(관리자)
    @PutMapping("/{id}")
    public ResponseEntity<AccommodationResponseDto> update(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean admin,
            @Valid @RequestBody AccommodationRequestDto dto
    ) {
        validateAdmin(admin);
        return ResponseEntity.ok(accommodationService.update(id, dto));
    }

    //숙소 삭제(관리자)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean admin
    ) {
        validateAdmin(admin);
        accommodationService.delete(id, true);
        return ResponseEntity.noContent().build();
    }

    private void validateAdmin(boolean isAdmin) {
        if (!isAdmin) {
            throw new ServiceException(ErrorType.ACCOMMODATION_DELETE_UNAUTHORIZED);
        }
    }
}