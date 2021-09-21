package team.bupt.central_air.entity;

import java.time.LocalDateTime;
import java.time.Duration;

public class WindRequest implements Comparable<WindRequest> {
    private String roomID;
    private int windSpeed;
    private LocalDateTime startTime;
    private boolean served = false;

    public WindRequest(String roomID, int windSpeed) {
        this.roomID = roomID;
        this.windSpeed = windSpeed;
        this.startTime = LocalDateTime.now();
    }

    public String getRoomID() {
        return roomID;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public int getDuration(LocalDateTime endTime) {
        return (int) Duration.between(startTime, endTime).toSeconds();
    }

    public boolean checkRoomID(String roomID) {
        return this.roomID.equals(roomID);
    }

    public void setServed(Boolean served) {
        this.served = served;
        this.startTime = LocalDateTime.now();
    }

    @Override
    public int compareTo(WindRequest other) {
        if (windSpeed != other.windSpeed) {
            return windSpeed - other.windSpeed;
        } else {
            LocalDateTime endTime = LocalDateTime.now();
            return getDuration(endTime) - other.getDuration(endTime);
        }
    }
}
