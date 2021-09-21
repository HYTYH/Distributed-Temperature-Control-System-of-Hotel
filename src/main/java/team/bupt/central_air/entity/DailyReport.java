package team.bupt.central_air.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DailyReport {
    List<RoomReport> reports;

    private DailyReport() {
        reports = new ArrayList<>();
    }
    
    public static DailyReport createDailyReport(List<String> roomList, long sTime, long eTime) {
        DailyReport dailyReport = new DailyReport();
        for (String id : roomList) {
            RoomReport report = RoomReport.getRoomReport(id, sTime, eTime);
            dailyReport.reports.add(report);
        }
        return dailyReport;
    }

    public HashMap<String, Object> toDailyReportHashMap() {
        List<HashMap<String, Object>> list = new ArrayList<>();
        for (RoomReport roomReport: reports) {
            list.add(roomReport.toRoomReportHashMap());
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("roomReports", list);
        return hashMap;
    }
}
