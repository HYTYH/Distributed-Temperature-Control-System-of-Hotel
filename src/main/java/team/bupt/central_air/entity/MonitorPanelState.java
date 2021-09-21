package team.bupt.central_air.entity;

public class MonitorPanelState {
    String state;
    int targetTemp;
    double currentTemp;
    String operationMode;
    String windSpeed;
    long roomId;

    public MonitorPanelState(String state, double targetTemp, double currentTemp, String windSpeed, long roomId) {
        this.state = state;
        this.targetTemp = (int) targetTemp;
        this.currentTemp = currentTemp;
        this.windSpeed = windSpeed;
        this.roomId = roomId;
    }
}
