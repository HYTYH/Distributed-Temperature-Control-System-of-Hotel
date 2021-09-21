package team.bupt.central_air.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PanelUsageItem {

    private long roomId;
    private String state;
    private double targetTemp;
    private int windSpeed;
    private long startTime;
    private long endTime;
    private double cost;

    public PanelUsageItem() {}

    public PanelUsageItem(long roomId, String state, double targetTemp, int windSpeed, long startTime, long endTime, double cost) {
        this.roomId = roomId;
        this.state = state;
        this.targetTemp = targetTemp;
        this.windSpeed = windSpeed;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = cost;
    }

    public HashMap<String, Object> toPanelUsageItemHashMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("targetTemp", (int)targetTemp);
        hashMap.put("windSpeed", WindSpeed.fromInt(windSpeed).toString());
        hashMap.put("startTime", convertToDateTime(startTime));
        hashMap.put("endTime", convertToDateTime(endTime));
        hashMap.put("cost", cost);
        return hashMap;
    }

    private String convertToDateTime(long time) {
        LocalDateTime localDateTime = Instant.ofEpochMilli(time).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(localDateTime);
    }

    public boolean isPoweredOff() {
        return windSpeed == 0;
    }

    public String getState() {
        return state;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public double getTargetTemp() {
        return targetTemp;
    }

    public void setTargetTemp(double targetTemp) {
        this.targetTemp = targetTemp;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "PanelUsage{" + "roomId=" + roomId + ", targetTemp=" + targetTemp + ", windSpeed=" + windSpeed
                + ", startTime=" + startTime + ", endTime=" + endTime + ", cost=" + cost + '}';
    }
}
