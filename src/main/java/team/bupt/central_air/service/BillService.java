package team.bupt.central_air.service;

import team.bupt.central_air.entity.DetailedBill;
import team.bupt.central_air.entity.WindSpeed;

import java.util.HashMap;

public class BillService {
    private static final BillService instance = new BillService();

    public static BillService getInstance() {
        return instance;
    }

    private BillService() {}

    public static BillService getService() {
        return instance;
    }

    public HashMap<String, Object> createDetailedBillMap(String roomID) {
        return DetailedBill.createDetailedBill(roomID).toDetailedBillHashMap();
    }

    public HashMap<String, Object> createSimpleBillMap(String roomID) {
        return DetailedBill.createDetailedBill(roomID).toSimpleBillHashMap();
    }

    public boolean setupBillService(double[] costPerMinute)  {
        setCostPerMinute(costPerMinute);
        return true;
    }

    private void setCostPerMinute(double[] costPerMinute) {
        WindSpeed.HIGH.setCost(costPerMinute[0]);
        WindSpeed.MID.setCost(costPerMinute[1]);
        WindSpeed.LOW.setCost(costPerMinute[2]);
    }
}
