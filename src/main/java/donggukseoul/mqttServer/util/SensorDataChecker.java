package donggukseoul.mqttServer.util;

import org.springframework.stereotype.Component;

@Component
public class SensorDataChecker {

    public static String checkTemperature(double value) {
        if (value < 16.5 || value > 27.5) {
            return "RED";
        } else if ((value >= 16.5 && value < 17.6) || (value > 26.4 && value <= 27.5)) {
            return "ORANGE";
        } else if ((value >= 17.6 && value < 18.7) || (value > 25.3 && value <= 26.4)) {
            return "YELLOW";
        } else if ((value >= 18.7 && value < 19.8) || (value > 24.2 && value <= 25.3)) {
            return "GREEN";
        } else {
            return "BLUE";
        }
    }

    public static String checkHumidity(double value) {
        if (value < 10 || value > 90) {
            return "RED";
        } else if ((value >= 10 && value < 20) || (value > 80 && value <= 90)) {
            return "ORANGE";
        } else if ((value >= 20 && value < 30) || (value > 70 && value <= 80)) {
            return "YELLOW";
        } else if ((value >= 30 && value < 40) || (value > 60 && value <= 70)) {
            return "GREEN";
        } else {
            return "BLUE";
        }
    }

    public static String checkTvoc(double value) {
        if (value > 10000) {
            return "RED";
        } else if (value > 3000 && value <= 10000) {
            return "ORANGE";
        } else if (value > 1000 && value <= 3000) {
            return "YELLOW";
        } else if (value > 300 && value <= 1000) {
            return "GREEN";
        } else {
            return "BLUE";
        }
    }

    public static String checkPm25(double value) {
        if (value > 64) {
            return "RED";
        } else if (value > 53 && value <= 64) {
            return "ORANGE";
        } else if (value > 41 && value <= 53) {
            return "YELLOW";
        } else if (value > 23 && value <= 41) {
            return "GREEN";
        } else {
            return "BLUE";
        }
    }

    public static String checkNoise(double value) {
        if (value > 80) {
            return "RED";
        } else if (value > 70 && value <= 80) {
            return "ORANGE";
        } else if (value > 60 && value <= 70) {
            return "YELLOW";
        } else if (value > 50 && value <= 60) {
            return "GREEN";
        } else {
            return "BLUE";
        }
    }
}