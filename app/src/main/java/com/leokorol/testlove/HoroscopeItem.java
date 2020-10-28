package com.leokorol.testlove;

public class HoroscopeItem {
    private String _zodiacName;
    private String _prediction;

    public HoroscopeItem(String zodiacName, String prediction) {
        _zodiacName = zodiacName;
        _prediction = prediction;
    }

    public String getZodiacName() {
        return _zodiacName;
    }

    public String getPrediction() {
        return _prediction;
    }
}
