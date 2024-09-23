package com.joshbarros.taskmanager;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TaskManager {

    private static final TaskService taskService = new TaskService();

    public static void main(String[] args) {
        // Start the scheduler for the cron job in a separate thread
        new Thread(SchedulerSetup::startScheduler).start();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            clearConsole();

            System.out.println("\nTask Manager Menu:");
            System.out.println("1. Add Task");
            System.out.println("2. List Tasks");
            System.out.println("3. Delete Task");
            System.out.println("4. Mark Task as Completed");
            System.out.println("5. Exit");

            System.out.print("Select an option: ");

            // Handle invalid input and prevent crashes
            try {
                int option = scanner.nextInt();
                scanner.nextLine(); // Clear the newline

                switch (option) {
                    case 1:
                        System.out.print("Enter task description: ");
                        String description = scanner.nextLine();
                        Task newTask = taskService.addTask(description);
                        System.out.println("Added: " + newTask);
                        break;
                    case 2:
                        System.out.println("Listing all tasks:");
                        taskService.listTasks().forEach(System.out::println);
                        break;
                    case 3:
                        System.out.print("Enter task ID to delete: ");
                        int deleteId = scanner.nextInt();
                        scanner.nextLine(); // Clear the newline
                        if (taskService.deleteTask(deleteId)) {
                            System.out.println("Task deleted.");
                        } else {
                            System.out.println("Task not found.");
                        }
                        break;
                    case 4:
                        System.out.print("Enter task ID to mark as completed: ");
                        int completeId = scanner.nextInt();
                        scanner.nextLine(); // Clear the newline
                        if (taskService.markTaskAsCompleted(completeId)) {
                            System.out.println("Task marked as completed.");
                        } else {
                            System.out.println("Task not found.");
                        }
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid option. Please choose a number between 1 and 5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Consume the invalid input to avoid an infinite loop
            }

            System.out.println("\nReturning to the main menu...");
            pause();
        }
    }

    // Clear the console screen
    private static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error clearing the console: " + e.getMessage());
        }
    }

    // Pause to allow the user to read before clearing the console
    private static void pause() {
        try {
            // Pause for 5 second before showing the menu again
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
