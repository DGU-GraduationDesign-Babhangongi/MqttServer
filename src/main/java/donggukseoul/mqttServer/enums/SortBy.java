package donggukseoul.mqttServer.enums;

public enum SortBy {
    TIMESTAMP("timestamp"),
    SENSOR_ID("sensorId"),
    VALUE("value");

    private final String fieldName;

    SortBy(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
