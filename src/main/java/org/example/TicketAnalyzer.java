package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TicketAnalyzer {

    public static void main(String[] args) throws IOException {
        // Проверка, что передан путь к файлу
        if (args.length != 1) {
            System.err.println("Usage: java Ticket <tickets.json>");
            System.exit(1);
        }

        // Чтение файла
        String filePath = args[0];
        JSONArray ticketsArray = readJsonFile(filePath);

        // Фильтрация билетов по маршруту Владивосток - Тель-Авив
        List<JSONObject> filteredTickets = filterTickets(ticketsArray, "Владивосток", "Тель-Авив");

        // Группировка билетов по авиакомпаниям и вычисление минимального времени полёта
        calculateMinimumFlightTimes(filteredTickets);

        // Вычисление разницы между средней ценой и медианой
        calculatePriceDifference(filteredTickets);
    }

    // Чтение JSON файла и возврат содержимого в виде JSONArray
    private static JSONArray readJsonFile(String filePath) throws IOException {
        try (FileReader reader = new FileReader(Paths.get(filePath).toFile())) {
            return new JSONArray(new JSONTokener(reader));
        }
    }

    // Фильтрация билетов по городам отправления и прибытия
    private static List<JSONObject> filterTickets(JSONArray ticketsArray, String fromCity, String toCity) {
        List<JSONObject> filteredTickets = new ArrayList<>();
        for (int i = 0; i < ticketsArray.length(); i++) {
            JSONObject ticket = ticketsArray.getJSONObject(i);
            if (fromCity.equals(ticket.getString("from")) && toCity.equals(ticket.getString("to"))) {
                filteredTickets.add(ticket);
            }
        }
        return filteredTickets;
    }

    // Группировка билетов по авиакомпаниям и вычисление минимального времени полёта
    private static void calculateMinimumFlightTimes(List<JSONObject> tickets) {
        // Группируем билеты по авиакомпаниям
        tickets.stream()
                .collect(Collectors.groupingBy(ticket -> ticket.getString("carrier")))
                .forEach((carrier, carrierTickets) -> {
                    // Находим минимальное время полёта для каждой авиакомпании
                    int minFlightTime = carrierTickets.stream()
                            .mapToInt(ticket -> ticket.getInt("flightTime"))
                            .min()
                            .orElse(Integer.MAX_VALUE);
                    System.out.printf("Минимальное время полёта для %s: %d минут\n", carrier, minFlightTime);
                });
    }

    // Вычисление разницы между средней ценой и медианой
    private static void calculatePriceDifference(List<JSONObject> tickets) {
        List<Integer> prices = tickets.stream()
                .map(ticket -> ticket.getInt("price"))
                .collect(Collectors.toList());

        double averagePrice = prices.stream().mapToInt(price -> price).average().orElse(0);
        Collections.sort(prices);
        double medianPrice;
        int size = prices.size();
        if (size % 2 == 0) {
            medianPrice = (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
            medianPrice = prices.get(size / 2);
        }

        System.out.printf("Средняя цена: %.2f\n", averagePrice);
        System.out.printf("Медианная цена: %.2f\n", medianPrice);
        System.out.printf("Разница между средней ценой и медианой: %.2f\n", Math.abs(averagePrice - medianPrice));
    }
}