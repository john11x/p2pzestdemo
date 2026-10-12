package zest.springbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import zest.springbackend.model.enums.BookingStatus;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String startTime;

    @Column(nullable = false)
    private String endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(nullable = false)
    private String memberSchoolId;

    @Column(nullable = false)
    private String leadTeacherId;

    @Column(nullable = false)
    private String trainerId;

    @Column(nullable = false)
    private String partnerSchoolId;

    private String cancelledAt;

    private String cancellationReason;

    public Booking() {
    }

    public Booking(String date, String startTime, String endTime, BookingStatus status,
                   String memberSchoolId, String leadTeacherId, String trainerId, String partnerSchoolId) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.memberSchoolId = memberSchoolId;
        this.leadTeacherId = leadTeacherId;
        this.trainerId = trainerId;
        this.partnerSchoolId = partnerSchoolId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getMemberSchoolId() {
        return memberSchoolId;
    }

    public void setMemberSchoolId(String memberSchoolId) {
        this.memberSchoolId = memberSchoolId;
    }

    public String getLeadTeacherId() {
        return leadTeacherId;
    }

    public void setLeadTeacherId(String leadTeacherId) {
        this.leadTeacherId = leadTeacherId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getPartnerSchoolId() {
        return partnerSchoolId;
    }

    public void setPartnerSchoolId(String partnerSchoolId) {
        this.partnerSchoolId = partnerSchoolId;
    }

    public String getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(String cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
}
