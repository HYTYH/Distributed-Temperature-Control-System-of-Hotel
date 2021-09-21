package team.bupt.central_air.entity;

import team.bupt.central_air.dao.DBConnectionUtil;
import team.bupt.central_air.dao.GuestInfo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailedBill {
    private String roomID;
    private String guestName;
    private double totalCost;
    private long checkInTime;
    private long checkOutTime;
    private PanelUsageList panelUsageList;

    private DetailedBill(String roomID, String guestName, long checkInTime, long checkOutTime, PanelUsageList panelUsageList) {
        this.roomID = roomID;
        this.guestName = guestName;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.panelUsageList = panelUsageList;
    }

    public static DetailedBill createDetailedBill(String roomID) {
        // 查询GuestInfo获取cheInTime和guestName
        String guestName = null;
        long checkInTime = 0;
        try {
            List<Object> query = DBConnectionUtil.query("select * from GuestInfo where roomId = " + roomID, GuestInfo.class);
            for (Object o : query) {
                GuestInfo guestInfo = (GuestInfo)o;
                guestName = guestInfo.getGuestName();
                checkInTime = guestInfo.getCheckinTime();
                DBConnectionUtil.delete(o);
                System.out.println(roomID + ": checkout");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long checkOutTime = System.currentTimeMillis();
        PanelUsageList panelUsageList = PanelUsageList.loadUsageList(roomID, checkInTime, checkOutTime);
        DetailedBill detailedBill = new DetailedBill(roomID, guestName, checkInTime, checkOutTime, panelUsageList);
        detailedBill.calculateTotalCost();
        return detailedBill;
    }

    private void calculateTotalCost() {
        totalCost = panelUsageList.calculateTotalCost();
    }

    public HashMap<String, Object> toDetailedBillHashMap() {
        HashMap<String, Object> map = toSimpleBillHashMap();
        map.putAll(panelUsageList.toPanelUsageListHashMap());
        return map;
    }

    public HashMap<String, Object> toSimpleBillHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("guestName", guestName);
        map.put("roomID", Integer.parseInt(roomID));
        map.put("checkinTime", convertToDateTime(checkInTime));
        map.put("checkoutTime", convertToDateTime(checkOutTime));
        map.put("totalCost", totalCost);
        return map;
    }

    private String convertToDateTime(long time) {
        LocalDateTime localDateTime = Instant.ofEpochMilli(time).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(localDateTime);
    }
}