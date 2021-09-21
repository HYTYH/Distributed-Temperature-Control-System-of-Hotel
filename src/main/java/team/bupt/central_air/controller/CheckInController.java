package team.bupt.central_air.controller;

import com.google.gson.Gson;
import team.bupt.central_air.service.CheckInService;

import java.util.HashMap;

public class CheckInController {
    public String checkin(HashMap<String, Object> inputMap) {
        String errorMsg = null;
        long roomID = 0;
        String guestName = null;
        if (inputMap.containsKey("guestName")) {
            Object guest = inputMap.get("guestName");
            guestName = String.valueOf(guest);
        } else {
            errorMsg = "客户名输入错误";
        }
        if (inputMap.containsKey("roomID")) {
            Object id = inputMap.get("roomID");
            roomID = Double.valueOf(String.valueOf(id)).longValue();
        } else {
            errorMsg = "房间号输入错误";
        }

        HashMap<String, Object> returnValue = new HashMap<>();
        if (errorMsg != null) {
            returnValue.put("code", 300);
            returnValue.put("message", errorMsg);
            returnValue.put("success", false);
        } else {
            boolean success = CheckInService.getService().checkin(guestName, roomID);
            returnValue.put("code", success ? 200:500);
            returnValue.put("message",success ? "OK":"error");
            returnValue.put("success", success);
        }
        Gson gson = new Gson();
        return gson.toJson(returnValue);
    }
}
