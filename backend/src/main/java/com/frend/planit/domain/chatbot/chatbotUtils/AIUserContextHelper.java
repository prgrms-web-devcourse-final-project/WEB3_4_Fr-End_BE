package com.frend.planit.domain.chatbot.chatbotUtils;

import com.frend.planit.domain.calendar.schedule.day.entity.ScheduleDayEntity;
import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import java.time.LocalDateTime;
import java.util.List;

public class AIUserContextHelper {

    public static String buildUserTravelContext(List<ScheduleEntity> schedules) {
        StringBuilder sb = new StringBuilder();

        if (schedules == null || schedules.isEmpty()) {
            sb.append("""
                    당신은 한국인입니다. 당신은 한국어로 대답해야 합니다.
                    이 채팅의 목적인 plan-it 이라는 서비스를 이용하는 고객에게 해당 서비스의 정보를 제공하는데 있습니다.
                    plan-it 은 고객이 여행을 계획하는데 도움을 주는 서비스입니다.
                    """.stripIndent().trim()
            );

            sb.append("""
                    사용자가 여행 일정을 등록하지 않았습니다.
                    """.stripIndent().trim()
            );

            return sb.toString();
        }

        sb.append("""
                당신은 한국인입니다. 당신은 한국어로 대답해야 합니다.
                이 채팅의 목적인 plan-it 이라는 서비스를 이용하는 고객에게 해당 서비스의 정보를 제공하는데 있습니다.
                plan-it 은 고객이 여행을 계획하는데 도움을 주는 서비스입니다.
                """.stripIndent().trim()
        ).append("\n");

        sb.append("현재 날짜 : ")
                .append(LocalDateTime.now())
                .append("\n");

        sb.append("다음은 사용자의 여행 일정입니다:\n\n");

        for (ScheduleEntity schedule : schedules) {
            sb.append("📅 여행 제목: ").append(schedule.getScheduleTitle()).append("\n");
            sb.append("   기간: ").append(schedule.getStartDate())
                    .append(" ~ ").append(schedule.getEndDate()).append("\n");

            for (ScheduleDayEntity day : schedule.getScheduleDayList()) {
                if (day.getTravelList().isEmpty()) {
                    continue;
                }

                sb.append("   ▸ 날짜: ").append(day.getDate()).append("\n");

                for (TravelEntity travel : day.getTravelList()) {
                    sb.append("     - 장소: ").append(travel.getLocation()).append("\n");
                    sb.append("       카테고리: ").append(travel.getCategory()).append("\n");
                    sb.append("       방문 시간: ")
                            .append(String.format("%02d시 %02d분", travel.getVisitHour(),
                                    travel.getVisitMinute()))
                            .append("\n");
                }

                sb.append("\n");
            }

            sb.append("\n");
        }

        sb.append("위 여행 일정을 참고하여 사용자의 질문에 응답해주세요.");

        return sb.toString();

    }
}
