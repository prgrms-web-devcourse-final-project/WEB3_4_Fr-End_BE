package com.frend.planit.domain.chatbot.chatbotUtils;

import com.frend.planit.domain.calendar.schedule.day.entity.ScheduleDayEntity;
import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import java.util.List;

public class AIUserContextHelper {

    public static String buildUserTravelContext(List<ScheduleEntity> schedules) {
        if (schedules == null || schedules.isEmpty()) {
            return "현재 사용자는 등록된 여행 일정이 없습니다.";
        }

        StringBuilder sb = new StringBuilder();
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
