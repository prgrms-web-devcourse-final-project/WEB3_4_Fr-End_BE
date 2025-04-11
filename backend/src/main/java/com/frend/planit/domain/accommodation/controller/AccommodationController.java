package com.frend.planit.domain.accommodation.controller;

import com.frend.planit.domain.accommodation.dto.request.AccommodationRequestDto;
import com.frend.planit.domain.accommodation.dto.response.AccommodationResponseDto;
import com.frend.planit.domain.accommodation.service.AccommodationService;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.Role;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import com.frend.planit.global.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    // 숙소 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<AccommodationResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(accommodationService.findById(id));
    }

    // 숙소 전체 조회 + 정렬 + 페이징 (PageResponse 적용)
    @GetMapping
    public ResponseEntity<PageResponse<AccommodationResponseDto>> findAllPaged(
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<AccommodationResponseDto> response =
                new PageResponse<>(accommodationService.findAllPaged(sortBy, direction, page, size));
        return ResponseEntity.ok(response);
    }

    // 숙소 생성
    @PostMapping
    public ResponseEntity<AccommodationResponseDto> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AccommodationRequestDto dto
    ) {
        validateAdmin(user);
        AccommodationResponseDto created = accommodationService.create(dto);
        URI location = URI.create("/api/v1/accommodations/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    // 숙소 수정
    @PutMapping("/{id}")
    public ResponseEntity<AccommodationResponseDto> update(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AccommodationRequestDto dto
    ) {
        validateAdmin(user);
        return ResponseEntity.ok(accommodationService.update(id, dto));
    }

    // 숙소 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        validateAdmin(user);
        accommodationService.delete(id, true);
        return ResponseEntity.noContent().build();
    }

    // 필터링 조회 (지역, 이름, 유형 기준)
    @GetMapping("/search")
    public ResponseEntity<PageResponse<AccommodationResponseDto>> search(
            @RequestParam(required = false) String areaCode,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String cat3,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<AccommodationResponseDto> response =
                new PageResponse<>(accommodationService.searchWithFilters(areaCode, title, cat3, sortBy, direction, page, size));
        return ResponseEntity.ok(response);
    }

    private void validateAdmin(User user) {
        if (user == null || user.getRole() != Role.ADMIN) {
            throw new ServiceException(ErrorType.ACCOMMODATION_DELETE_UNAUTHORIZED);
        }
    }
}