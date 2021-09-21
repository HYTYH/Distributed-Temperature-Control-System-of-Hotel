package team.bupt.central_air.entity;

public class AirPanelState {
    String state;
    int target_temp;
    int init_temp;
    double current_temp;
    String fan_speed;
    double fee;
    long room_id;

    public AirPanelState(String state, double target_temp, double init_temp, double current_temp, String fan_speed,
            double fee, long room_id) {
        this.state = state;
        this.target_temp = (int) target_temp;
        this.init_temp = (int) init_temp;
        this.current_temp = current_temp;
        this.fan_speed = fan_speed;
        this.fee = fee;
        this.room_id = room_id;
    }

}
