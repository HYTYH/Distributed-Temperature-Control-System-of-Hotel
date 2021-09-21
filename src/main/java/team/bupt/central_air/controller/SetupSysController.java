package team.bupt.central_air.controller;

import com.google.gson.Gson;
import team.bupt.central_air.service.AirService;
import team.bupt.central_air.service.BillService;

import java.util.ArrayList;
import java.util.HashMap;

public class SetupSysController {
    public String setupSystem(HashMap<String, Object> inputMap) {
        String errorMessage = null;
        ArrayList<Double> tempBoundArray = null;
        ArrayList<Double> costWkhMinArray = null;
        ArrayList<Double> initTempArray = null;
        int maxServeNum = 0;
        Double target = null;

        if (inputMap.containsKey("tempBound")) {
            Object tempBound = inputMap.get("tempBound");
            tempBoundArray = (ArrayList<Double>)tempBound;
        } else {
            errorMessage = "no tempBound";
        }

        if (inputMap.containsKey("targetTemp")) {
            Object targetTemp = inputMap.get("targetTemp");
            target = Double.valueOf(String.valueOf(targetTemp));
        } else {
            errorMessage = "no targetTemp";
        }

        if (inputMap.containsKey("maxServeNum")) {
            Object maxServe = inputMap.get("maxServeNum");
            maxServeNum = Double.valueOf(String.valueOf(maxServe)).intValue();
        } else {
            errorMessage = "no maxServeNum";
        }

        if (inputMap.containsKey("costWkhMin")) {
            Object costWkhMin = inputMap.get("costWkhMin");
            costWkhMinArray = (ArrayList<Double>)costWkhMin;
        } else {
            errorMessage = "no costWkhMin";
        }

        if (inputMap.containsKey("initTemp")) {
            Object initTemp = inputMap.get("initTemp");
            initTempArray = (ArrayList<Double>)initTemp;
        } else {
            errorMessage = "no initTemp";
        }

        HashMap<String, Object> returnValue = new HashMap<>();
        if (errorMessage == null) {
            double[] tempBound = {tempBoundArray.get(0) , tempBoundArray.get(1) };
            double targetTemp = target;
            double[] costPerMinute = { 1.0/costWkhMinArray.get(0), 1.0/costWkhMinArray.get(1) , 1.0/costWkhMinArray.get(2) };
            double[] initTemp = {initTempArray.get(0), initTempArray.get(1), initTempArray.get(2), initTempArray.get(3)};
            BillService.getService().setupBillService(costPerMinute);
            AirService.getService().setupAirService(initTemp, 0, tempBound, targetTemp, maxServeNum);
            AirService.getService().startTimerTask();
            returnValue.put("code", 200);
            returnValue.put("message", "OK");
            returnValue.put("success", true);
        } else {
            returnValue.put("code", 300);
            returnValue.put("message", errorMessage);
            returnValue.put("success", false);
        }
        Gson gson = new Gson();
        return gson.toJson(returnValue);
    }
}
