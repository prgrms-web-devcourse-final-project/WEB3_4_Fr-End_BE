package com.frend.planit.domain.mateboard.post.repository;

import com.frend.planit.domain.mateboard.post.entity.Mate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 메이트 모집 게시글에 대한 데이터베이스 접근을 처리하는 Repository 인터페이스입니다.
 * <p> 기본 CRUD 메서드는 JpaRepository에서 상속받아 사용할 수 있으며,</p>
 * <p> 추후 조건 검색 등 커스텀 쿼리 메서드도 이 인터페이스에 정의합니다.</p>
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-28
 */
public interface MateRepository extends JpaRepository<Mate, Long> {

    // 로그인한 사용자의 게시글을 조회 (활동 내역 조회용)
    List<Mate> findByWriterId(Long userId);
}
