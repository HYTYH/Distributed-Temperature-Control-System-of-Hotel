package team.bupt.central_air.entity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CentralAirSystem {
    private List<RoomAirPanel> roomAirPanelList = new ArrayList<>();
    private int initTargetTemp = 0;
    private OperationMode operationMode = null;

    public CentralAirSystem(List<String> roomList, double[] initTemp, OperationMode operationMode) {
        this.operationMode = operationMode;
        for (int i = 0; i < roomList.size(); i++) {
            roomAirPanelList.add(new RoomAirPanel(roomList.get(i), initTemp[i], 1, operationMode));
        }
    }

    public List<MonitorPanelState> monitorAirState() {
        List<MonitorPanelState> monitorPanelStates = new ArrayList<>();
        for (RoomAirPanel roomAirPanel : roomAirPanelList) {
            monitorPanelStates.add(roomAirPanel.monitorPanelState());
        }
        return monitorPanelStates;
    }

    public AirPanelState monitorAirState(String roomID) {
        RoomAirPanel roomAirPanel = getPanelByRoomID(roomID);
        return roomAirPanel.monitorState();
    }

    public RoomAirPanel getPanelByRoomID(String roomID) {
        for (RoomAirPanel roomAirPanel : roomAirPanelList) {
            if (roomAirPanel.getRoomID().equals(roomID)) {
                return roomAirPanel;
            }
        }
        return null;
    }

    public void power(String roomID) {
        RoomAirPanel roomAirPanel = getPanelByRoomID(roomID);
        roomAirPanel.power();
    }

    public void clear(String roomID) {
        RoomAirPanel roomAirPanel = getPanelByRoomID(roomID);
        roomAirPanel.clear();
    }

    public boolean modifyTargetTemp(String roomID, double targetTemp) {
        RoomAirPanel roomAirPanel = getPanelByRoomID(roomID);
        return roomAirPanel.modifyTargetTemp(targetTemp);
    }

    public void initTargetTemp(double targetTemp) {
        this.initTargetTemp = (int) targetTemp;
        for (RoomAirPanel roomAirPanel : roomAirPanelList) {
            roomAirPanel.initTargetTemp(targetTemp);
        }
    }

    public void setWindSpeed(String roomID, WindSpeed windSpeed) {
        RoomAirPanel roomAirPanel = getPanelByRoomID(roomID);
        roomAirPanel.setWindSpeed(windSpeed);
    }

    public void setWindSpeed(WindSpeed windSpeed) {
        for (RoomAirPanel roomAirPanel : roomAirPanelList) {
            roomAirPanel.setWindSpeed(windSpeed);
        }
    }

    public void setOperationMode(OperationMode operationMode) {
        for (RoomAirPanel roomPanel : roomAirPanelList) {
            roomPanel.setOperationMode(operationMode);
        }
    }

    public void setBound(double[] tempBound) {
        for (RoomAirPanel roomPanel : roomAirPanelList) {
            roomPanel.setBound(tempBound);
        }
    }

    public List<RoomAirPanel> updateAchieved() {
        for (RoomAirPanel roomAirPanel : roomAirPanelList) {
            roomAirPanel.updateAchieved();
        }
        return roomAirPanelList;
    }

    public void updateCurrentTemp() {
        for (RoomAirPanel roomAirPanel : roomAirPanelList) {
            roomAirPanel.updateCurrentTemp();
        }
    }

    public String getBasicInfo() {
        HashMap<String, Object> returnValue = new HashMap<>();
        returnValue.put("init_temp", initTargetTemp);
        returnValue.put("mode", operationMode.toString());
        returnValue.put("low_bound", (int) operationMode.getLowerBound());
        returnValue.put("high_bound", (int) operationMode.getUpperBound());
        Gson gson = new Gson();
        return gson.toJson(returnValue);
    }
}
