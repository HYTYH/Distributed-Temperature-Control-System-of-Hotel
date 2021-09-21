package team.bupt.central_air.entity;

import java.util.*;

public class WindRequestQueue {
    private List<WindRequest> queue = new ArrayList<>();

    public WindRequestQueue() {
    }

    public int size() {
        return queue.size();
    }

    public void append(WindRequest windRequest) {
        queue.add(windRequest);
        Collections.sort(queue);
    }

    public boolean remove(String roomID) {
        return queue.removeIf(windRequest -> windRequest.checkRoomID(roomID));
    }

    public WindRequest getLowest() {
        return queue.get(0);
    }

    public WindRequest getHighest() {
        return queue.get(queue.size() - 1);
    }

    public void shift() {
        queue.remove(0);
    }

    public void pop() {
        queue.remove(queue.size() - 1);
    }
}
