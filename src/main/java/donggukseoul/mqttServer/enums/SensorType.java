package donggukseoul.mqttServer.enums;

public enum SensorType {
    ALL("all"),
    TEMPERATURE("temperature"),
    TVOC("tvoc"),
    AMBIENTNOISE("ambientNoise"),
    IAQINDEX("iaqIndex"),
    AQMSCORES("aqmScores"),
    HUMIDITY("humidity"),
    USBPOWERED("usbPowered"),
    BUTTONPRESSED("buttonPressed"),
    WATERDETECTION("waterDetection"),
    PM2_5MASSCONCENTRATION("PM2_5MassConcentration");

    private final String typeName;

    SensorType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public static SensorType fromTypeName(String typeName) {
        for (SensorType type : values()) {
            if (type.getTypeName().equalsIgnoreCase(typeName)) {
                return type;
            }
        }
        return null;
    }
}
