package team.bupt.central_air.entity;

import java.util.HashMap;

public class RoomReport {
    private String roomID;
    private int useCnt;
    private int mostUsedTargetTemp;
    private String mostUsedWindSpeed;
    private int reachTempCnt;
    private int schedulingTimes;
    private int detailedRecordsCnt;
    private double totalCost;

    public RoomReport(String roomID, PanelUsageList panelUsageList) {
        this.roomID = roomID;
        this.useCnt = panelUsageList.calculateUseCnt();
        this.mostUsedTargetTemp = panelUsageList.calculateMostUsedTemp();
        this.mostUsedWindSpeed = panelUsageList.calculateMostUsedWind();
        this.reachTempCnt = panelUsageList.getReachedTempCount();
        this.schedulingTimes = panelUsageList.getScheduleTimes();
        this.detailedRecordsCnt = panelUsageList.getRecordsCount();
        this.totalCost = panelUsageList.calculateTotalCost();
    }

    public static RoomReport getRoomReport(String roomID, long sTime, long eTime) {
        PanelUsageList panelUsageList = PanelUsageList.loadUsageList(roomID, sTime, eTime);
        return new RoomReport(roomID, panelUsageList);
    }

    public HashMap<String, Object> toRoomReportHashMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("roomID", Integer.parseInt(roomID));
        hashMap.put("useCnt", useCnt);
        hashMap.put("mostUsedTargetTemp", mostUsedTargetTemp);
        hashMap.put("mostUsedWindSpeed", mostUsedWindSpeed);
        hashMap.put("reachTargetTempCnt", reachTempCnt);
        hashMap.put("scheduleTimes", schedulingTimes);
        hashMap.put("detailedRecordsCnt", detailedRecordsCnt);
        hashMap.put("totalCost", totalCost);
        return hashMap;
    }
}
