package team.bupt.central_air.entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import team.bupt.central_air.dao.DBConnectionUtil;

public class RoomAirPanel {
    private String roomID;
    private double initialTemp;
    private double currentTemp;
    private double restartThreshold;
    private OperationMode operationMode;
    private double targetTemp;

    private WindSpeed windSpeed = WindSpeed.MID;
    private WindSpeed recentWindSpeed = WindSpeed.MID;
    private LocalDateTime startTime = LocalDateTime.now(ZoneOffset.UTC);
    private boolean powered = false;
    private boolean achieved = false;
    private double cost = 0;

    public RoomAirPanel(String roomID, double initialTemp, double restartThreshold, OperationMode operationMode) {
        this.roomID = roomID;
        this.initialTemp = initialTemp;
        this.currentTemp = initialTemp;
        this.restartThreshold = restartThreshold;
        this.operationMode = operationMode;
    }

    public AirPanelState monitorState() {
        double tempCost = calculateCost(getMilliSeconds(startTime), getMilliSeconds(LocalDateTime.now(ZoneOffset.UTC)));
        return new AirPanelState(getState(), targetTemp, initialTemp, currentTemp, recentWindSpeed.toString(),
                cost + tempCost, Long.parseLong(roomID));
    }

    public MonitorPanelState monitorPanelState() {
        return new MonitorPanelState(getState(), targetTemp, currentTemp, recentWindSpeed.toString(),
                Long.parseLong(roomID));
    }

    public String getRoomID() {
        return roomID;
    }

    public void setOperationMode(OperationMode operationMode) {
        this.operationMode = operationMode;
    }

    public void setBound(double[] tempBound) {
        operationMode.setBound(tempBound[0], tempBound[1]);
    }

    public boolean initTargetTemp(double targetTemp) {
        if (operationMode.isInBound(targetTemp)) {
            this.targetTemp = targetTemp;
            return true;
        }
        return false;
    }

    public boolean modifyTargetTemp(double bias) {
        double temp = targetTemp + bias;
        if (operationMode.isInBound(temp)) {
            createPanelUsageItem();
            targetTemp = temp;
            return true;
        }
        return false;
    }

    public WindSpeed getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(WindSpeed windSpeed) {
        createPanelUsageItem();
        this.windSpeed = windSpeed;
        if (windSpeed != WindSpeed.HALT) {
            recentWindSpeed = windSpeed;
        }
    }

    public void createPanelUsageItem() {
        int speed = powered ? windSpeed.toInt() : 0;
        long sTime = getMilliSeconds(startTime);
        startTime = LocalDateTime.now(ZoneOffset.UTC);
        long eTime = getMilliSeconds(startTime);
        double itemCost = calculateCost(sTime, eTime);
        cost += itemCost;
        PanelUsageItem panelUsageItem = new PanelUsageItem(Long.parseLong(roomID), getState(), targetTemp, speed, sTime, eTime, itemCost);
        try {
            DBConnectionUtil.insertToTable(panelUsageItem);
        } catch (IllegalAccessException e) {
            // TODO: handle exception
        }
    }

    public WindSpeed getRecentWindSpeed() {
        return recentWindSpeed;
    }

    public boolean isPowered() {
        return powered;
    }

    public void power() {
        createPanelUsageItem();
        powered = !powered;
        if (!powered) {
            windSpeed = WindSpeed.MID;
            recentWindSpeed = WindSpeed.MID;
        }
    }

    public void clear() {
        if (powered && !achieved) {
            createPanelUsageItem();
        }
        powered = false;
        cost = 0;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public void updateAchieved() {
        switch (operationMode) {
            case COOL:
                if (!achieved && currentTemp <= targetTemp) {
                    achieved = true;
                } else if (achieved && currentTemp >= targetTemp + restartThreshold) {
                    achieved = false;
                }
                break;
            case HEAT:
                if (!achieved && currentTemp >= targetTemp) {
                    achieved = true;
                } else if (achieved && currentTemp <= targetTemp - restartThreshold) {
                    achieved = false;
                }
                break;
        }
    }

    public void updateCurrentTemp() {
        double temp = targetTemp;
        double rate = windSpeed.getRate();
        if (!powered || windSpeed == WindSpeed.HALT) {
            temp = initialTemp;
            rate = WindSpeed.HALT.getRate();
        }
        if (currentTemp > temp) {
            currentTemp = Math.max(temp, currentTemp - rate);
        } else if (currentTemp < temp) {
            currentTemp = Math.min(temp, currentTemp + rate);
        }
    }

    private String getState() {
        if (!powered) {
            return "关机";
        } else if (achieved) {
            return "休眠";
        } else if (windSpeed == WindSpeed.HALT) {
            return "等待";
        } else {
            return "服务中";
        }
    }

    private long getMilliSeconds(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

    private double calculateCost(long sTime, long eTime) {
        WindSpeed speed = powered ? windSpeed : WindSpeed.HALT;
        return speed.calculateCost(eTime - sTime);
    }
}
