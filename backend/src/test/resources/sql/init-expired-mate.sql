-- 작성자
INSERT INTO users (id, email, nickname, profile_image, gender, social_id, login_type, social_type,
                   role, status, created_at, updated_at, mailing_type, bio)
VALUES (1, 'expired@example.com', '유저10', 'https://example.com/img10.png', 'MALE', 'social-id-10',
        'SOCIAL', 'GOOGLE', 'USER', 'REGISTERED', NOW(), NOW(), true, '만료 테스트용 유저');

-- 모집글: 어제 시작 -> 오늘 테스트 시 모집 마감 자동처리 대상
INSERT INTO mate (id, writer_id, title, content, recruit_count, recruitment_status,
                  travel_start_date, travel_end_date, travel_region,
                  mate_gender, created_at, updated_at)
VALUES (10, 1, '지난 여행', '이미 시작된 여행', 3, 'OPEN',
        DATEADD('DAY', -1, CURRENT_DATE), DATEADD('DAY', 2, CURRENT_DATE), 'BUSAN',
        'FEMALE', NOW(), NOW());
