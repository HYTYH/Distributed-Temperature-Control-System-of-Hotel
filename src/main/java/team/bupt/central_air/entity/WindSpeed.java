package team.bupt.central_air.entity;

public enum WindSpeed {
    HALT(0, 0.5, 0, "停风"), LOW(1, 0.4, 1 / 3, "低速"), MID(2, 0.5, 0.5, "中速"), HIGH(3, 0.6, 1, "高速");

    private int id;
    private double ratePerMinute;
    private double costPerMinute;
    private String description;
    private double scale = 1;

    private WindSpeed(int id, double ratePerMinute, double costPerMinute, String description) {
        this.id = id;
        this.ratePerMinute = ratePerMinute;
        this.costPerMinute = costPerMinute;
        this.description = description;
    }

    public int toInt() {
        return id;
    }

    public String toString() {
        return description;
    }

    public double getRate() {
        return ratePerMinute / 60 * scale;
    }

    public double calculateCost(long milliseconds) {
        return costPerMinute * milliseconds / 1000 / 60 * scale;
    }

    public double getCost() {
        return costPerMinute;
    }

    public void setCost(double costPerMinute) {
        this.costPerMinute = costPerMinute;
    }

    public static WindSpeed fromInt(int speed) {
        if (speed == 0) {
            return WindSpeed.HALT;
        } else if (speed == 1) {
            return WindSpeed.LOW;
        } else if (speed == 2) {
            return WindSpeed.MID;
        } else if (speed == 3) {
            return WindSpeed.HIGH;
        }
        return WindSpeed.HALT;
    }
}
