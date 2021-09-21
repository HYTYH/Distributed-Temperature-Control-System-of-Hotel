package team.bupt.central_air.entity;

public enum OperationMode {
    COOL(18, 25, "制冷"), HEAT(25, 30, "制热");

    private double lowerBound;
    private double upperBound;
    private String description;

    private OperationMode(double lowerBound, double upperBound, String description) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.description = description;
    }

    public boolean isInBound(double targetTemp) {
        return lowerBound <= targetTemp && targetTemp <= upperBound;
    }

    public void setBound(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public static OperationMode fromInt(int mode) {
        if (mode == 0) {
            return OperationMode.COOL;
        } else if (mode == 1) {
            return OperationMode.HEAT;
        }
        return null;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public String toString() {
        return description;
    }
}
