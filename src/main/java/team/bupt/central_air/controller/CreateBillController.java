package team.bupt.central_air.controller;
import com.google.gson.Gson;
import team.bupt.central_air.service.AirService;
import team.bupt.central_air.service.BillService;

import java.util.HashMap;

public class CreateBillController {
    public String createDetailedBill(HashMap<String, Object> inputMap) {
        String roomID = getRoomID(inputMap);
        HashMap<String, Object> returnMap = new HashMap<>();
        if (roomID!=null) {
            // 关闭房间空调
             AirService.getService().clear(roomID);
            // 退房并获取详单
            returnMap = BillService.getService().createDetailedBillMap(roomID);
            // 添加其他信息
            returnMap.put("code", 200);
            returnMap.put("message", "OK");
            returnMap.put("success", true);
        } else {
            returnMap.put("code", 300);
            returnMap.put("message", "无房间号码");
            returnMap.put("success", false);
        }
        Gson gson = new Gson();
        return gson.toJson(returnMap);
    }

    public String createSimpleBill(HashMap<String, Object> inputMap) {
        String roomID = getRoomID(inputMap);
        HashMap<String, Object> returnMap = new HashMap<>();
        if (roomID!=null) {
            // 关闭房间空调
            AirService.getService().clear(roomID);
            // 退房并获取账单
            returnMap = BillService.getService().createSimpleBillMap(roomID);
            // 添加其他信息
            returnMap.put("code", 200);
            returnMap.put("message", "OK");
            returnMap.put("success", true);
        } else {
            returnMap.put("code", 300);
            returnMap.put("message", "无房间号码");
            returnMap.put("success", false);
        }
        Gson gson = new Gson();
        return gson.toJson(returnMap);
    }

    private String getRoomID(HashMap<String, Object> inputMap) {
        long roomID = 0;
        if (inputMap.containsKey("roomID")) {
            Object id = inputMap.get("roomID");
            roomID = Double.valueOf(String.valueOf(id)).longValue();
        }
        if (roomID == 0) {
            return null;
        }
        return String.valueOf(roomID);
    }
}
