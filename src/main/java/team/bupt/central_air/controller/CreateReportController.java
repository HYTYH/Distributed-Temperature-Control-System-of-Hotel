package team.bupt.central_air.controller;

import com.google.gson.Gson;
import team.bupt.central_air.service.ReportService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

public class CreateReportController {
    public String createDailyReport(HashMap<String, Object> inputMap) {
        String date = null;
        String errorMsg = null;
        if (inputMap.containsKey("date")) {
            Object guest = inputMap.get("date");
            date = String.valueOf(guest);
        } else {
            errorMsg = "日期输入错误";
        }
        HashMap<String, Object> returnMap = new HashMap<>();
        if (date!=null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            long startTime = getMilliSeconds(LocalDateTime.parse(date + " 00:00:00", formatter));
            long endTime = getMilliSeconds(LocalDateTime.parse(date + " 23:59:59", formatter));
            returnMap = ReportService.getService().createDailyReportMap(startTime, endTime);
            returnMap.put("code", 200);
            returnMap.put("message", "OK");
            returnMap.put("success", true);
        } else {
            returnMap.put("code", 300);
            returnMap.put("message", "无日期");
            returnMap.put("success", false);
        }
        Gson gson = new Gson();
        return gson.toJson(returnMap);
    }

    private long getMilliSeconds(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
