package team.bupt.central_air.dao;

public class GuestInfo {

    private String guestName;
    private long roomId;
    private long checkinTime;
    private long checkoutTime;

    public GuestInfo() {
    }

    public GuestInfo(String guestName, long roomId, long checkinTime, long checkoutTime) {
        this.guestName = guestName;
        this.roomId = roomId;
        this.checkinTime = checkinTime;
        this.checkoutTime = checkoutTime;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }


    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }


    public long getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(long checkinTime) {
        this.checkinTime = checkinTime;
    }


    public long getCheckoutTime() {
        return checkoutTime;
    }

    @Override
    public String toString() {
        return "GuestInfo{" +
                "guestName='" + guestName + '\'' +
                ", roomId=" + roomId +
                ", checkinTime=" + checkinTime +
                ", checkoutTime=" + checkoutTime +
                '}';
    }

    public void setCheckoutTime(long checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

}
