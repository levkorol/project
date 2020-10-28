package com.leokorol.testlove;

public enum Zodiacs {
    Aries(0), // овен
    Taurus(1), // телец
    Gemini(2), // близнецы
    Cancer(3), // рак
    Leo(4), // лев
    Virgo(5), // дева
    Libra(6), // весы
    Scorpio(7), // скорпион
    Sagittarius(8), // стрелец
    Capricorn(9), // козерог
    Aquarius(10), // водолей
    Pisces(11); // рыбы

    private final int index;

    Zodiacs(int index) {
        this.index = index;
    }

    public int index() {
        return index;
    }
}
