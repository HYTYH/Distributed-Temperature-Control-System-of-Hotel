package team.bupt.central_air.controller;
import com.google.gson.Gson;
import team.bupt.central_air.entity.AirPanelState;
import team.bupt.central_air.service.AirService;

import java.util.HashMap;

public class ModifyAirController {

    public String getBasicInfo() {
        return AirService.getService().getBasicInfo();
    }

    public String power(HashMap<String, Object> inputMap) {
        String roomID = getRoomID(inputMap);
        if (roomID!=null) {
            AirService.getService().power(roomID);
        }
        // 获取当前状态
        return roomID == null ? "fatal" : getCurPanelState(roomID);
    }

    public String highWind(HashMap<String, Object> inputMap) {
        String roomID = getRoomID(inputMap);
        if (roomID!=null) {
            AirService.getService().modifyAirPanelWind(roomID, 3);
        }
        // 获取当前状态
        return roomID == null ? "fatal" : getCurPanelState(roomID);
    }

    public String midWind(HashMap<String, Object> inputMap) {
        String roomID = getRoomID(inputMap);
        if (roomID!=null) {
            AirService.getService().modifyAirPanelWind(roomID, 2);
        }
        // 获取当前状态
        return roomID == null ? "fatal" : getCurPanelState(roomID);
    }

    public String lowWind(HashMap<String, Object> inputMap) {
        String roomID = getRoomID(inputMap);
        if (roomID!=null) {
            AirService.getService().modifyAirPanelWind(roomID, 1);
        }
        // 获取当前状态
        return roomID == null ? "fatal" : getCurPanelState(roomID);
    }

    public String upTemp(HashMap<String, Object> inputMap) {
        String roomID = getRoomID(inputMap);
        if (roomID!=null) {
            AirService.getService().modifyAirPanelTemp(roomID, 1);
        }
        // 获取当前状态
        return roomID == null ? "fatal" : getCurPanelState(roomID);
    }

    public String downTemp(HashMap<String, Object> inputMap) {
        String roomID = getRoomID(inputMap);
        if (roomID!=null) {
            AirService.getService().modifyAirPanelTemp(roomID, -1);
        }
        // 获取当前状态
        return roomID == null ? "fatal" : getCurPanelState(roomID);
    }

    public String getCurInfo(HashMap<String, Object> inputMap) {
        String roomID = getRoomID(inputMap);
        return roomID == null ? "fatal" : getCurPanelState(roomID);
    }

    private String getCurPanelState(String roomID){
        Gson gson = new Gson();
        AirPanelState airPanelState = AirService.getService().monitorAirState(roomID);
        return gson.toJson(airPanelState);
    }

    private String getRoomID(HashMap<String, Object> inputMap) {
        long roomID = 0;
        if (inputMap.containsKey("room_id")) {
            Object id = inputMap.get("room_id");
            roomID = Double.valueOf(String.valueOf(id)).longValue();
        }
        if (roomID == 0) {
            return null;
        }
        return String.valueOf(roomID);
    }
}
