package zest.springbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import zest.springbackend.model.enums.AvailabilityOwnerType;
import zest.springbackend.model.enums.DayOfWeek;

@Entity
@Table(name = "availability")
public class Availability {

    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String ownerID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AvailabilityOwnerType ownerType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private String startTime;

    @Column(nullable = false)
    private String endTime;

    @Column(nullable = false)
    private boolean isRecurring;

    @Column(nullable = false)
    private String timezone;

    private String startDate;

    private String endDate;

    public Availability() {
    }

    public Availability(String ownerID, AvailabilityOwnerType ownerType, DayOfWeek dayOfWeek,
                       String startTime, String endTime, boolean isRecurring, String timezone,
                       String startDate, String endDate) {
        this.ownerID = ownerID;
        this.ownerType = ownerType;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isRecurring = isRecurring;
        this.timezone = timezone;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public AvailabilityOwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(AvailabilityOwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
