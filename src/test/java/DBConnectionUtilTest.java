import team.bupt.central_air.dao.DBConnectionUtil;
import team.bupt.central_air.dao.GuestInfo;
import team.bupt.central_air.dao.RoomId;
import team.bupt.central_air.entity.PanelUsageItem;

import java.util.List;

class DBConnectionUtilTest {

    public static void roomIdTest() throws Exception {
        List<Object> query = DBConnectionUtil.query("select * from RoomId", RoomId.class);
        for (Object o : query) {
            System.out.println(o);
        }
    }

    public static void guestTest() throws Exception {
        GuestInfo guestInfo = new GuestInfo();
        guestInfo.setGuestName("wanz");
        guestInfo.setCheckinTime(System.currentTimeMillis());
        guestInfo.setCheckoutTime(System.currentTimeMillis());
        guestInfo.setRoomId(1);
        int i = DBConnectionUtil.insertToTable(guestInfo);
        System.out.println(i);

        List<Object> query = DBConnectionUtil.query("select * from GuestInfo", GuestInfo.class);
        for (Object o : query) {
            System.out.println(o);
            int delete = DBConnectionUtil.delete(o);
            System.out.println(delete);
        }
    }

    public static void panelTest() throws Exception {
        long timestamp = System.currentTimeMillis();
        PanelUsageItem panelUsageItem = new PanelUsageItem();
        panelUsageItem.setRoomId(1);
        panelUsageItem.setEndTime(timestamp);
        panelUsageItem.setStartTime(timestamp);
        panelUsageItem.setTargetTemp(26);
        panelUsageItem.setWindSpeed(0);
        int i = DBConnectionUtil.insertToTable(panelUsageItem);
        System.out.println(i);

        List<Object> query = DBConnectionUtil.query("select * from PanelUsageItem", PanelUsageItem.class);
        for (Object o : query) {
            System.out.println(o);
            int delete = DBConnectionUtil.delete(o);
            System.out.println(delete);
        }
    }

    public static void main(String[] args) throws Exception {
//        guestTest();
//        roomIdTest();
        panelTest();
    }
}