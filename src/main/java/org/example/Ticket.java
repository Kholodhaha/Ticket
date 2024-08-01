package org.example;

public class Ticket {
    private String origin;
    private String origin_name;
    private String destination;
    private String destination_name;
    private String departure_date;
    private String departure_time;
    private String arrival_date;
    private String arrival_time;
    private String carrier;
    private int stops;
    private int price;

    // Геттеры и сеттеры
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOriginName() {
        return origin_name;
    }

    public void setOriginName(String origin_name) {
        this.origin_name = origin_name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationName() {
        return destination_name;
    }

    public void setDestinationName(String destination_name) {
        this.destination_name = destination_name;
    }

    public String getDepartureDate() {
        return departure_date;
    }

    public void setDepartureDate(String departure_date) {
        this.departure_date = departure_date;
    }

    public String getDepartureTime() {
        return departure_time;
    }

    public void setDepartureTime(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getArrivalDate() {
        return arrival_date;
    }

    public void setArrivalDate(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public String getArrivalTime() {
        return arrival_time;
    }

    public void setArrivalTime(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public int getStops() {
        return stops;
    }

    public void setStops(int stops) {
        this.stops = stops;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}