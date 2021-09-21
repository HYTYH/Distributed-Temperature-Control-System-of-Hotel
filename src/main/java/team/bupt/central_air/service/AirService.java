package team.bupt.central_air.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import team.bupt.central_air.dao.DBConnectionUtil;
import team.bupt.central_air.dao.RoomId;
import team.bupt.central_air.entity.*;

public class AirService {
    private static final AirService instance = new AirService();
    private WindRequestQueue servedQueue = new WindRequestQueue();
    private WindRequestQueue waitingQueue = new WindRequestQueue();
    private CentralAirSystem centralAirSystem;
    private List<String> roomList = new ArrayList<>();
    private int maxServeNum;
    private int timeSlice = 5;

    private AirService() {
        try {
            List<Object> roomIdList = DBConnectionUtil.query("select * from RoomId", RoomId.class);
            for (Object roomId : roomIdList) {
                roomList.add(String.valueOf(((RoomId) roomId).getRoomId()));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static AirService getService() {
        return instance;
    }

    public boolean setupAirService(double[] initTemp, int mode, double[] tempBound, double targetTemp,
            int maxServeNum) {
        OperationMode operationMode = OperationMode.fromInt(mode);
        centralAirSystem = new CentralAirSystem(roomList, initTemp, operationMode);
        setOperationMode(operationMode, tempBound);
        initTargetTemp(targetTemp);
        setMaxServeNum(maxServeNum);
        return true;
    }

    public boolean running() {
        return centralAirSystem != null;
    }

    public void power(String roomID) {
        centralAirSystem.power(roomID);
    }

    public void clear(String roomID) {
        centralAirSystem.clear(roomID);
    }

    public void modifyAirPanelTemp(String roomID, double targetTemp) {
        centralAirSystem.modifyTargetTemp(roomID, targetTemp);
    }

    public void modifyAirPanelWind(String roomID, int windSpeed) {
        servedQueue.remove(roomID);
        waitingQueue.remove(roomID);
        waitingQueue.append(new WindRequest(roomID, windSpeed));
    }

    public String monitorAirState() {
        List<MonitorPanelState> monitorPanelStates =  centralAirSystem.monitorAirState();
        Gson gson = new Gson();
        return gson.toJson(monitorPanelStates);
    }

    public AirPanelState monitorAirState(String roomID) {
        return centralAirSystem.monitorAirState(roomID);
    }

    public String getBasicInfo() {
        return this.centralAirSystem.getBasicInfo();
    }

    private void setOperationMode(OperationMode operationMode, double[] tempBound) {
        centralAirSystem.setOperationMode(operationMode);
        centralAirSystem.setBound(tempBound);
    }

    private void initTargetTemp(double targetTemp) {
        centralAirSystem.initTargetTemp(targetTemp);
    }

    private void stopAirPanelWind(String roomID) {
        servedQueue.remove(roomID);
        waitingQueue.remove(roomID);
        centralAirSystem.setWindSpeed(roomID, WindSpeed.HALT);
    }

    private void serveWindRequest(WindRequest windRequest) {
        centralAirSystem.setWindSpeed(windRequest.getRoomID(), WindSpeed.fromInt(windRequest.getWindSpeed()));
    }

    public int getMaxServeNum() {
        return maxServeNum;
    }

    private void setMaxServeNum(int maxServeNum) {
        this.maxServeNum = maxServeNum;
    }

    public void startTimerTask() {
        new Timer().scheduleAtFixedRate(new AirServiceTimerTask(), 1000, 1000);
    }

    public class AirServiceTimerTask extends TimerTask {
        @Override
        public void run() {
            for (RoomAirPanel roomAirPanel : centralAirSystem.updateAchieved()) {
                if (roomAirPanel.isPowered()) {
                    if (roomAirPanel.getWindSpeed() != WindSpeed.HALT && roomAirPanel.isAchieved()) {
                        stopAirPanelWind(roomAirPanel.getRoomID());
                    } else if (roomAirPanel.getWindSpeed() == WindSpeed.HALT && !roomAirPanel.isAchieved()) {
                        modifyAirPanelWind(roomAirPanel.getRoomID(),
                        roomAirPanel.getRecentWindSpeed().toInt());
                    }
                }
            }

            while (waitingQueue.size() > 0) {
                WindRequest windRequest = waitingQueue.getHighest();
                if (servedQueue.size() < maxServeNum) {
                    waitingQueue.pop();
                    servedQueue.append(windRequest);
                    serveWindRequest(windRequest);
                } else {
                    WindRequest windRequestLowest = servedQueue.getLowest();
                    if (windRequest.getWindSpeed() > windRequestLowest.getWindSpeed()) {
                        servedQueue.shift();
                        waitingQueue.append(windRequestLowest);
                        stopAirPanelWind(windRequestLowest.getRoomID());
                        servedQueue.append(windRequest);
                        serveWindRequest(windRequest);
                    } else if (windRequest.getWindSpeed() == windRequestLowest.getWindSpeed()
                            && windRequest.getDuration(LocalDateTime.now()) >= timeSlice) {
                        servedQueue.shift();
                        waitingQueue.append(windRequestLowest);
                        stopAirPanelWind(windRequestLowest.getRoomID());
                        servedQueue.append(windRequest);
                        serveWindRequest(windRequest);
                        break;
                    } else {
                        break;
                    }
                }
            }
            centralAirSystem.updateCurrentTemp();
        }
    }
}
