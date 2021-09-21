package team.bupt.central_air.service;

import team.bupt.central_air.dao.DBConnectionUtil;
import team.bupt.central_air.dao.GuestInfo;

public class CheckInService {
    private static final CheckInService instance = new CheckInService();

    private CheckInService() {}

    public static CheckInService getService() {
        return instance;
    }

    public boolean checkin(String guestName, long roomID) {
        GuestInfo guestInfo = new GuestInfo();
        guestInfo.setGuestName(guestName);
        guestInfo.setCheckinTime(System.currentTimeMillis());
        guestInfo.setCheckoutTime(System.currentTimeMillis());
        guestInfo.setRoomId(roomID);
        int i = 0;
        try {
            i = DBConnectionUtil.insertToTable(guestInfo);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (i == 0) {
            return false;
        }
        return true;
    }
}
