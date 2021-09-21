package team.bupt.central_air.controller;

import com.google.gson.Gson;
import team.bupt.central_air.service.AirService;

import java.util.HashMap;

public class MonitorAirController {
    public String monitorRoomsAirState() {
        HashMap<String, Object> returnValue = new HashMap<>();
        if (AirService.getService().running()) {
            String states = AirService.getService().monitorAirState();
            returnValue.put("code",200);
            returnValue.put("message","OK");
            returnValue.put("success", true);
            returnValue.put("states", states);
        } else {
            returnValue.put("code",300);
            returnValue.put("message","中央空调未启动");
            returnValue.put("success", false);
        }
        Gson gson = new Gson();
        return gson.toJson(returnValue);
    }
}
