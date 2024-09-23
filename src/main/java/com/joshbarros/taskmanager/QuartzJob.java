package com.joshbarros.taskmanager;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QuartzJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        GraphQLClient client = new GraphQLClient();
        String jsonResponse = client.fetchEvents();

        // Save JSON response to a file
        try (FileWriter file = new FileWriter("json-output/events.json")) {
            file.write(jsonResponse);
            // Commented out because its annoying
            // System.out.println("Prettified JSON data saved to json-output/events.json");

            // Update logs
            appendLog("Cron job successfully fetched and saved events.json at " + getCurrentTime());
        } catch (IOException e) {
            throw new JobExecutionException("Error saving JSON file", e);
        }
    }

    // Method to append logs to logs.txt
    private void appendLog(String message) {
        try (FileWriter logFile = new FileWriter("json-output/logs.txt", true)) {
            logFile.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Utility to get current time in readable format
    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
