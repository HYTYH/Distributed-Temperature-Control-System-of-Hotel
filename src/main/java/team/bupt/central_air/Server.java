package team.bupt.central_air;

import com.google.gson.Gson;
import team.bupt.central_air.controller.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9543);
            while (true) {
                Socket socket;
                if((socket = serverSocket.accept()) != null) {
                    socket.setSoTimeout(5000);
                    Thread handle = new Handle(socket);
                    handle.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Handle extends Thread {
    Socket socket;

    public Handle(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream input = this.socket.getInputStream();
            OutputStream output = this.socket.getOutputStream();
            this.handle(input, output);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.socket.close();
            } catch (IOException socket_e) {
                e.printStackTrace();
            }
            System.out.println("client disconnected.");
        }
    }

    public void handle(InputStream input, OutputStream output) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        char[] buffer = new char[8192];
        int len = 0;
        try {
            len = reader.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String returnString = "";
        String inputString = String.copyValueOf(buffer, 0, len);
        System.out.println(inputString);
        String[] split = inputString.split("\r\n");
        boolean post = split[0].contains("POST");
        String path = split[0].split(" ")[1].split("\\?")[0];
        HashMap<String, Object> keyValueMap = new HashMap<>();
        if (post) {
            inputString = split[split.length - 1];
            try {
                keyValueMap = new Gson().fromJson(inputString, HashMap.class);
            } catch (Exception e) {
                // e.printStackTrace();
            }
        } else {
            if (!path.equals("/basic_info")) {
                String[] split1 = split[0].split("room_id=");
                String s = split1[split1.length - 1].split(" ")[0];
                keyValueMap.put("room_id", Long.parseLong(s));
            }
        }

        if (path.equals("/monitor")) {
            // 监控空调状态
            MonitorAirController monitorAirController = new MonitorAirController();
            returnString = monitorAirController.monitorRoomsAirState();
        } else if (path.equals("/setupSystem")) {
            // 启动空调系统
            SetupSysController setupSysController = new SetupSysController();
            returnString = setupSysController.setupSystem(keyValueMap);
        } else if (path.equals("/checkin")) {
            // 入住
            CheckInController checkInController = new CheckInController();
            returnString = checkInController.checkin(keyValueMap);
        } else if (path.equals("/basic_info")) {
            // 基础配置
            ModifyAirController modifyAirController = new ModifyAirController();
            returnString = modifyAirController.getBasicInfo();
        } else if (path.equals("/power")) {
            // 开关
            ModifyAirController modifyAirController = new ModifyAirController();
            returnString = modifyAirController.power(keyValueMap);
        } else if (path.equals("/high")) {
            // 高风
            ModifyAirController modifyAirController = new ModifyAirController();
            returnString = modifyAirController.highWind(keyValueMap);
        } else if (path.equals("/mid")) {
            // 中风
            ModifyAirController modifyAirController = new ModifyAirController();
            returnString = modifyAirController.midWind(keyValueMap);
        } else if (path.equals("/low")) {
            // 低风
            ModifyAirController modifyAirController = new ModifyAirController();
            returnString = modifyAirController.lowWind(keyValueMap);
        } else if (path.equals("/up")) {
            // 调高温度
            ModifyAirController modifyAirController = new ModifyAirController();
            returnString = modifyAirController.upTemp(keyValueMap);
        } else if (path.equals("/down")) {
            // 调低温度
            ModifyAirController modifyAirController = new ModifyAirController();
            returnString = modifyAirController.downTemp(keyValueMap);
        } else if (path.equals("/info")) {
            // 获取状态
            ModifyAirController modifyAirController = new ModifyAirController();
            returnString = modifyAirController.getCurInfo(keyValueMap);
        } else if (path.equals("/checkoutBill")) {
            // 退房并获取账单
            CreateBillController createBillController = new CreateBillController();
            returnString = createBillController.createSimpleBill(keyValueMap);
        } else if (path.equals("/checkoutDetailedBill")) {
            // 退房并获取详单
            CreateBillController createBillController = new CreateBillController();
            returnString = createBillController.createDetailedBill(keyValueMap);
        } else if (path.equals("/dailyReport")) {
            // 获取日报表
            CreateReportController createReportController = new CreateReportController();
            returnString = createReportController.createDailyReport(keyValueMap);
        } else if (path.equals("/login")) {
            LoginController loginController = new LoginController();
            returnString = loginController.login(keyValueMap);
        } else {
            System.out.println("404");
        }

        int length = returnString.getBytes(StandardCharsets.UTF_8).length;
        try {
            writer.write("HTTP/1.1 200 OK\r\n");
            writer.write("Connection: close\r\n");
            writer.write("Access-Control-Allow-Origin: *\r\n");
            writer.write("Content-Type: application/json;charset=UTF-8\r\n");
            writer.write("Content-Length: " + length + "\r\n");
            if (!returnString.equals("")) {
                writer.write("\r\n");
                writer.write(returnString);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
