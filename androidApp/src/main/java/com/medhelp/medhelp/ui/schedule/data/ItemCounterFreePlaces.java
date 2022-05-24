package com.medhelp.medhelp.ui.schedule.data;

public class ItemCounterFreePlaces {
    private String date;
    private int counterPlaces;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCounterPlaces() {
        return counterPlaces;
    }

    public void setCounterPlaces(int counterPlaces) {
        this.counterPlaces = counterPlaces;
    }

    public ItemCounterFreePlaces(String date, int counterPlaces)
    {
        this.date=date;
        this.counterPlaces=counterPlaces;
    }

    public void addPlaces(int number)
    {
        counterPlaces+=number;
    }
}

