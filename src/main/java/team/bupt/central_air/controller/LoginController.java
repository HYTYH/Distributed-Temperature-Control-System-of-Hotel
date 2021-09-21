package team.bupt.central_air.controller;

import com.google.gson.Gson;

import java.util.HashMap;

public class LoginController {

    public String login(HashMap<String, Object> inputMap) {
        String errorMsg = null;
        String username = null;
        if (inputMap.containsKey("username")) {
            Object o = inputMap.get("username");
            username = String.valueOf(o);
        } else {
            errorMsg = "客户名输入错误";
        }

        HashMap<String, Object> returnValue = new HashMap<>();
        if (errorMsg==null) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("token","root-token");
            data.put("type", username);
            returnValue.put("code", 200);
            returnValue.put("message", "OK");
            returnValue.put("data", data);

        } else {
            returnValue.put("code", 300);
            returnValue.put("message", errorMsg);
        }
        Gson gson = new Gson();
        return gson.toJson(returnValue);
    }
}
