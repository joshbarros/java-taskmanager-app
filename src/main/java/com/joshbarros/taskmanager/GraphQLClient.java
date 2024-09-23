package com.joshbarros.taskmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GraphQLClient {

    public String fetchEvents() {
        String apiUrl = "http://localhost:8080/graphql";
        String query = "{\"query\":\"{ onlineEvents { nodes { id title onlineEventDate onlineEventDescription } } }\"}";

        try {
            // Establish connection
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Write the GraphQL query to the output stream
            try (OutputStream os = conn.getOutputStream()) {
                os.write(query.getBytes());
                os.flush();
            }

            // Read the response
            StringBuilder response = new StringBuilder();
            try (Scanner scanner = new Scanner(conn.getInputStream())) {
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
            }

            // Convert the response to a pretty JSON format using Gson
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(gson.fromJson(response.toString(), Object.class));

            // Return the prettified JSON
            return prettyJson;

        } catch (IOException e) {
            e.printStackTrace();
            return "{}";  // Return an empty JSON object if there's an error
        }
    }
}
