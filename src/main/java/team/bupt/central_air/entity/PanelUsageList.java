package team.bupt.central_air.entity;

import team.bupt.central_air.dao.DBConnectionUtil;

import java.util.*;

public class PanelUsageList {
    private List<PanelUsageItem> panelUsageList = new ArrayList<>();

    private PanelUsageList() {}

    public static PanelUsageList loadUsageList(String roomID, long startTime, long endTime) {
        PanelUsageList panelUsageList = new PanelUsageList();
        try {
            String sql = "select * from PanelUsageItem where roomId = " + roomID + " and " + "startTime > " + startTime + " and " + " endTime" + " < " + endTime;
            List<Object> query = DBConnectionUtil.query(sql, PanelUsageItem.class);
            for (Object o : query) {
                PanelUsageItem panelUsageItem = (PanelUsageItem) o;
                panelUsageList.append(panelUsageItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return panelUsageList;
    }

    public double calculateTotalCost() {
        double totalCost = 0;
        for (PanelUsageItem panelUsageItem: panelUsageList) {
            totalCost += panelUsageItem.getCost();
        }
        return totalCost;
    }

    public int calculateUseCnt() {
        int useCnt = 0;
        for (PanelUsageItem panelUsageItem: panelUsageList) {
            useCnt += panelUsageItem.isPoweredOff() ? 1:0;
        }
        return useCnt;
    }

    public int calculateMostUsedTemp() {
        Map<Double, Integer> map = new HashMap<>();
        for (PanelUsageItem panelUsageItem: panelUsageList) {
            Double targetTemp = panelUsageItem.getTargetTemp();
            if(map.containsKey(targetTemp)) {
                Integer oldValue = map.get(targetTemp);
                map.put(targetTemp, oldValue + 1);
            } else {
                map.put(targetTemp, 1);
            }
        }
        List<Map.Entry<Double,Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> (o2.getValue() - o1.getValue()));
        return list.size() == 0 ? 0:list.get(0).getKey().intValue();
    }

    public String calculateMostUsedWind() {
        Map<Integer, Integer> map = new HashMap<>();
        for (PanelUsageItem panelUsageItem: panelUsageList) {
            Integer targetTemp = panelUsageItem.getWindSpeed();
            if(map.containsKey(targetTemp)) {
                Integer oldValue = map.get(targetTemp);
                map.put(targetTemp, oldValue + 1);
            } else {
                map.put(targetTemp, 1);
            }
        }
        List<Map.Entry<Integer,Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> (o2.getValue() - o1.getValue()));
        int windSpeed = list.size() == 0 ? 0: list.get(0).getKey();
        return WindSpeed.fromInt(windSpeed).toString();
    }

    public int getReachedTempCount() {
        int count = 0;
        for (PanelUsageItem panelUsageItem: panelUsageList) {
            count += panelUsageItem.getState().equals("休眠") ? 1:0;
        }
        return count;
    }

    public int getScheduleTimes() {
        int count = 0;
        for (PanelUsageItem panelUsageItem: panelUsageList) {
            count += panelUsageItem.getState().equals("服务中") ? 1:0;
        }
        return count;
    }

    public int getRecordsCount() {
        return panelUsageList.size();
    }

    public HashMap<String, Object> toPanelUsageListHashMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap<String, Object>> usageList = new ArrayList<>();
        for (PanelUsageItem panelUsageItem:panelUsageList) {
            usageList.add(panelUsageItem.toPanelUsageItemHashMap());
        }
        hashMap.put("panelUsageList", usageList);
        return hashMap;
    }

    private void append(PanelUsageItem panelUsageItem) {
        panelUsageList.add(panelUsageItem);
    }
}
