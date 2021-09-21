package team.bupt.central_air.service;

import team.bupt.central_air.dao.DBConnectionUtil;
import team.bupt.central_air.dao.RoomId;
import team.bupt.central_air.entity.DailyReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportService {
    private static final ReportService instance = new ReportService();
    private List<String> roomList = new ArrayList<>();

    private ReportService() {
        try {
            List<Object> roomIdList = DBConnectionUtil.query("select * from RoomId", RoomId.class);
            for (Object roomId : roomIdList) {
                roomList.add(String.valueOf(((RoomId) roomId).getRoomId()));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static ReportService getService() {
        return instance;
    }

    public HashMap<String, Object> createDailyReportMap(long sTime, long eTime) {
        return DailyReport.createDailyReport(roomList, sTime, eTime).toDailyReportHashMap();
    }
}
