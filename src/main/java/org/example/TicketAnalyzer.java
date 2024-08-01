package org.example;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TicketAnalyzer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TicketAnalyzer <path-to-json-file>");
            return;
        }

        String filePath = args[0];
        List<Ticket> tickets = readJsonFile(filePath);

        if (tickets == null || tickets.isEmpty()) {
            System.out.println("No tickets found.");
            return;
        }

        analyzeTickets(tickets, "VVO", "TLV");
    }

    private static List<Ticket> readJsonFile(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            TicketList ticketList = gson.fromJson(reader, TicketList.class);
            return ticketList.getTickets();
        } catch (IOException e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private static void analyzeTickets(List<Ticket> tickets, String origin, String destination) {
        // Фильтрация билетов
        List<Ticket> filteredTickets = tickets.stream()
                .filter(ticket -> origin.equals(ticket.getOrigin()) && destination.equals(ticket.getDestination()))
                .collect(Collectors.toList());

        if (filteredTickets.isEmpty()) {
            System.out.println("No flights found from " + origin + " to " + destination + ".");
            return;
        }

        // Расчет времени полета
        Map<String, List<Duration>> carrierFlightTimes = new HashMap<>();
        List<Integer> prices = new ArrayList<>();
        for (Ticket ticket : filteredTickets) {
            try {
                // Проверка на null
                if (ticket.getDepartureDate() == null || ticket.getDepartureTime() == null ||
                        ticket.getArrivalDate() == null || ticket.getArrivalTime() == null) {
                    System.err.println("Skipping ticket due to missing date or time fields: " + ticket);
                    continue;
                }

                LocalDateTime departure = parseDateTime(ticket.getDepartureDate(), ticket.getDepartureTime());
                LocalDateTime arrival = parseDateTime(ticket.getArrivalDate(), ticket.getArrivalTime());
                Duration flightDuration = Duration.between(departure, arrival);

               // System.out.println("Flight from " + ticket.getOrigin() + " to " + ticket.getDestination() +
                //        ": Departure - " + departure.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                //        ", Arrival - " + arrival.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                //        ", Duration - " + formatDuration(flightDuration));

                carrierFlightTimes
                        .computeIfAbsent(ticket.getCarrier(), k -> new ArrayList<>())
                        .add(flightDuration);

                prices.add(ticket.getPrice());
            } catch (Exception e) {
                System.err.println("Error parsing date/time string for ticket: " + ticket + " - " + e.getMessage());
            }
        }

        // Вывод минимального времени полета для каждого перевозчика
        System.out.println("\nMinimum flight times by carrier:");
        carrierFlightTimes.forEach((carrier, durations) -> {
            Duration minDuration = durations.stream().min(Duration::compareTo).orElse(Duration.ZERO);
            System.out.println("Carrier " + carrier + ": Minimum flight time - " + formatDuration(minDuration));
        });

        // Вывод средней цены, медианы и разницы
        if (!prices.isEmpty()) {
            double averagePrice = prices.stream().mapToInt(Integer::intValue).average().orElse(0);
            double medianPrice = calculateMedian(prices);
            double difference = averagePrice - medianPrice;

            System.out.println("\nAverage price: " + averagePrice);
            System.out.println("Median price: " + medianPrice);
            System.out.println("Difference between average price and median price: " + difference);
        }
    }

    private static LocalDateTime parseDateTime(String date, String time) {
        String normalizedTime = normalizeTime(time);
        String dateTimeStr = date + " " + normalizedTime;
        return LocalDateTime.parse(dateTimeStr, FORMATTER);
    }

    private static String normalizeTime(String time) {
        String[] parts = time.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid time format.");
        }

        String hour = parts[0];
        String minute = parts[1];

        if (hour.length() == 1) {
            hour = "0" + hour;
        }

        if (minute.length() == 1) {
            minute = "0" + minute;
        }

        return hour + ":" + minute;
    }

    private static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return hours + " hours " + minutes + " minutes";
    }

    private static double calculateMedian(List<Integer> prices) {
        Collections.sort(prices);
        int size = prices.size();
        if (size % 2 == 0) {
            return (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
            return prices.get(size / 2);
        }
    }
}