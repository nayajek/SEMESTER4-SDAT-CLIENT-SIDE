package com.keyin;


import java.io.*;
import java.net.*;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;

public class FoodOrderingClient {

    private static final String BASE_URL = "http://localhost:8081/api/orders"; // Assuming this is the base API URL for orders
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.println("\nFood Order Client Menu:");
            System.out.println("1. Place a new food order");
            System.out.println("2. View all orders");
            System.out.println("3. Check order status");
            System.out.println("4. Exit");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Clear the newline character

            switch (option) {
                case 1:
                    placeNewOrder();
                    break;
                case 2:
                    getAllOrders();
                    break;
                case 3:
                    checkOrderStatus();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option! Try again.");
            }
        }
    }

    // Method to place a new food order
    private static void placeNewOrder() throws Exception {
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter food item (e.g., pizza, burger): ");
        String foodItem = scanner.nextLine();

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Clear the newline character

        JSONObject orderData = new JSONObject();
        orderData.put("customerName", customerName);
        orderData.put("foodItem", foodItem);
        orderData.put("quantity", quantity);

        String url = BASE_URL + "/place";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        // Send the request body
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = orderData.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                JSONObject jsonResponse = new JSONObject(response.toString());
                System.out.println("Order placed successfully!");
                System.out.println("Order ID: " + jsonResponse.getInt("orderId"));
                System.out.println("Customer Name: " + jsonResponse.getString("customerName"));
                System.out.println("Food Item: " + jsonResponse.getString("foodItem"));
                System.out.println("Quantity: " + jsonResponse.getInt("quantity"));
                System.out.println("Order Status: " + jsonResponse.getString("status"));
            }
        } else {
            System.out.println("Error: " + status);
        }
    }

    // Method to get all orders
    private static void getAllOrders() throws Exception {
        String url = BASE_URL + "/all";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        int status = connection.getResponseCode();
        if (status == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            JSONArray jsonResponseArray = new JSONArray(response.toString());
            System.out.println("List of Orders:");
            for (int i = 0; i < jsonResponseArray.length(); i++) {
                JSONObject jsonResponse = jsonResponseArray.getJSONObject(i);
                System.out.println("Order " + (i + 1) + ":");
                System.out.println("  Order ID: " + jsonResponse.getInt("orderId"));
                System.out.println("  Customer Name: " + jsonResponse.getString("customerName"));
                System.out.println("  Food Item: " + jsonResponse.getString("foodItem"));
                System.out.println("  Quantity: " + jsonResponse.getInt("quantity"));
                System.out.println("  Status: " + jsonResponse.getString("status"));
                System.out.println();
            }
        } else {
            System.out.println("Error: " + status);
        }
    }

    // Method to check the status of an order
    private static void checkOrderStatus() throws Exception {
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine(); // Clear the newline character

        String url = BASE_URL + "/status/" + orderId;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        int status = connection.getResponseCode();
        if (status == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            System.out.println("Order ID: " + jsonResponse.getInt("orderId"));
            System.out.println("Status: " + jsonResponse.getString("status"));
        } else {
            System.out.println("Error: " + status);
        }
    }
}

