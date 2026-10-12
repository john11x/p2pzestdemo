package zest.springbackend.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public String sendSessionNotification(String action,
                                         String sessionDate,
                                         String startTime,
                                         String endTime,
                                         String trainerName,
                                         String trainerEmail,
                                         String trainerPhone,
                                         String trainerTimezone,
                                         String leadTeacherName,
                                         String leadTeacherEmail,
                                         String leadTeacherTimezone,
                                         String memberSchoolName,
                                         String partnerSchoolName) {

        String status = "SCHEDULED".equalsIgnoreCase(action) ? "scheduled" : "cancelled";

        String summary = "Session " + status + " for " + memberSchoolName + " / " + partnerSchoolName
            + " on " + sessionDate + " " + startTime + "-" + endTime;

        System.out.println("[NotificationService] " + summary);
        System.out.println("[NotificationService] Trainer email: " + trainerEmail);
        System.out.println("[NotificationService] Lead teacher email: " + leadTeacherEmail);

        return summary;
    }
}
