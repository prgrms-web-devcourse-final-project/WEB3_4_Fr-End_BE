package com.frend.planit.domain.board.mate.post.entity;

/**
 * 여행 지역을 정의하는 Enum입니다. 18개의 항목이 존재합니다.
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-28
 */
public enum TravelRegion {
    SEOUL("서울"),
    INCHEON("인천"),
    DAEJEON("대전"),
    DAEGU("대구"),
    GWANGJU("광주"),
    BUSAN("부산"),
    ULSAN("울산"),
    SEJONG("세종특별자치시"),
    GYEONGGI("경기도"),
    GANGWON("강원특별자치도"),
    CHUNGBUK("충청북도"),
    CHUNGNAM("충청남도"),
    GYEONGBUK("경상북도"),
    GYEONGNAM("경상남도"),
    JEONBUK("전북특별자치도"),
    JEONNAM("전라남도"),
    JEJU("제주도");

    private final String displayName;

    TravelRegion(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
