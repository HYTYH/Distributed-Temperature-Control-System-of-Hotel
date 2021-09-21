package team.bupt.central_air.dao;

public class RoomId {

    private long roomId;


    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public RoomId(long roomId) {
        this.roomId = roomId;
    }

    public RoomId() {
    }

    @Override
    public String toString() {
        return "RoomId{" +
                "roomId=" + roomId +
                '}';
    }
}
