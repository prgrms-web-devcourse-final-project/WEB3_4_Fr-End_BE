-- 유저
INSERT INTO users (id, email, nickname, profile_image, gender, social_id, login_type, social_type,
                   role, status, created_at, updated_at, mailing_type, bio)
VALUES (1, 'user1@example.com', '유저1', 'https://example.com/img1.png', 'MALE', 'social-id-1',
        'SOCIAL', 'GOOGLE', 'USER', 'REGISTERED', NOW(), NOW(), true, '안녕하세요 유저1입니다.'),
       (2, 'user2@example.com', '유저2', 'https://example.com/img2.png', 'FEMALE', 'social-id-2',
        'SOCIAL', 'GOOGLE', 'USER', 'REGISTERED', NOW(), NOW(), true, '안녕하세요 유저2입니다.'),
       (3, 'user3@example.com', '유저3', 'https://example.com/img3.png', 'MALE', 'social-id-3',
        'SOCIAL', 'GOOGLE', 'USER', 'REGISTERED', NOW(), NOW(), true, '안녕하세요 유저3입니다.'),
       (4, 'user4@example.com', '유저4', 'https://example.com/img4.png', 'FEMALE', 'social-id-4',
        'SOCIAL', 'GOOGLE', 'USER', 'REGISTERED', NOW(), NOW(), true, '안녕하세요 유저4입니다.'),
       (6, 'user6@example.com', '유저6', 'https://example.com/img6.png', 'FEMALE', 'social-id-6',
        'SOCIAL', 'GOOGLE', 'USER', 'REGISTERED', NOW(), NOW(), true, '안녕하세요 유저6입니다.');
-- 모집 인원 3명
INSERT INTO mate (id, writer_id, title, content, recruit_count, recruitment_status,
                  travel_start_date, travel_end_date, travel_region,
                  mate_gender, created_at, updated_at)
VALUES (5, 1, '여행 갑시다', '정말 재밌을 거예요', 3, 'CLOSED',
        '2025-06-01', '2025-06-10', 'SEOUL',
        'MALE', NOW(), NOW());

-- 수락된 신청자 3명 (user_id 2, 3, 4)
INSERT INTO mate_application (id, user_id, mate_id, status, created_at, updated_at)
VALUES (1, 2, 5, 'PENDING', NOW(), NOW()),
       (2, 3, 5, 'PENDING', NOW(), NOW()),
       (3, 4, 5, 'PENDING', NOW(), NOW());

-- 테스트에서 신청 시도할 user_id = 5 (아직 신청 안 함)
INSERT INTO users (id, email, nickname, profile_image, gender, social_id, login_type, social_type,
                   role, status, created_at, updated_at, mailing_type, bio)
VALUES (5, 'user5@example.com', '유저5', 'https://example.com/img5.png', 'MALE', 'social-id-5',
        'SOCIAL', 'GOOGLE', 'USER', 'REGISTERED', NOW(), NOW(), true, '테스트 유저입니다.');
