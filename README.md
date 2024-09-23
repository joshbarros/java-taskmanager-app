# Task Manager Application - README

## Overview
This project is a Java-based console application called **Task Manager** with integrated scheduling functionality using **Quartz Scheduler**. The application allows users to manage a to-do list (add, list, delete, and mark tasks as completed) while periodically fetching event data from a **WordPress GraphQL API** and saving it as a JSON file. The scheduling is done using the Quartz framework, and the fetched event data is prettified and stored in a JSON format, along with a log of each cron execution.

## Features
### Task Management:
- Add, list, delete, and mark tasks as completed.

### Scheduled Cron Job:
- Every hour, the application queries a WordPress GraphQL API to fetch event data and saves it as a JSON file in `json-output/events.json`.
- Each cron execution is logged in `json-output/logs.txt`.

### Console UI:
- A simple console-based user interface for task management.

### Quartz Scheduler:
- Integrated to run a cron job every hour to fetch event data.

### GraphQL Integration:
- The application fetches data from a WordPress GraphQL endpoint for online events and saves them to a file.

## Prerequisites
### 1. Java 17
Make sure Java 17 is installed. You can use **SDKMAN** to easily manage different versions of Java:

```bash
sdk install java 17.0.8-tem
sdk use java 17.0.8-tem
````

### 2. Docker and Docker Compose

You need Docker and Docker Compose to run the WordPress and MySQL containers for the GraphQL API. Make sure Docker is installed on your machine.

- [Docker Installation Guide](https://docs.docker.com/get-docker/)
- [Docker Compose Installation Guide](https://docs.docker.com/compose/install/)

## Running the Application

### 1. Set Up WordPress with GraphQL

- Clone the WordPress GraphQL repository or set up a WordPress instance with the WPGraphQL plugin installed.
- Use the provided `docker-compose.yml` file to start the WordPress environment.

```bash
docker-compose up -d
````

Access WordPress at [http://localhost:8080](http://localhost:8080).
Access phpMyAdmin at [http://localhost:8081](http://localhost:8081).

### 2. Running the Task Manager Application

After setting up the WordPress GraphQL API:

1. Clone this repository or download the source code.
2. Navigate to the project directory and build the application using Gradle by running:

```bash
docker compose up -d
```

3. Open another terminal and run the application:

```bash
java -jar build/libs/gradle.jar
```

### 3. Application Menu
When running the application, you'll be greeted with a simple menu to manage your tasks:

```bash
Task Manager Menu:
1. Add Task
2. List Tasks
3. Delete Task
4. Mark Task as Completed
5. Exit
Select an option:
```

### Task Management:
- **Add Task**: Enter a task description, and the task will be added to your list.
- **List Tasks**: View all your current tasks, including their status (Pending/Completed).
- **Delete Task**: Remove a task by providing its ID.
- **Mark Task as Completed**: Update the status of a task as "Completed."

### Scheduling:
- Every hour, a cron job will automatically fetch online event data from the WordPress GraphQL API and save the result to `json-output/events.json`.
- A log of each cron job execution will be saved in `json-output/logs.txt`.

### 4. Customizing the Scheduler:
- By default, the cron job is scheduled to run every hour. You can modify the interval in the `SchedulerSetup.java` file.
- If using the `quartz-config.xml` file, you can adjust the cron expression in the configuration file to change the schedule.

## Technical Explanation

### Design and Architecture
The Task Manager application is built using **Java 17**, **Quartz Scheduler**, **Gson** for JSON parsing, and integrates with a **WordPress GraphQL API** to retrieve and store event data. The project follows a clean architecture with clear separation between the task management functionality, scheduling, and API integrations. Below is a breakdown of the core components:

### 1. Task Management
The **TaskManager** class provides a console-based user interface for users to add, list, delete, and mark tasks as completed. The tasks are stored in an in-memory `ArrayList`, and each task is represented by a simple **Task** class with attributes like `id`, `description`, and `isCompleted`.

- The menu allows users to interact with the task list.
- Error handling is implemented using `try-catch` blocks to prevent crashes from invalid inputs.

**References:**
- [Java Collections - ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)
- [Java Scanner Class](https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html)

### 2. Quartz Scheduler Integration
The application uses **Quartz Scheduler** to periodically execute a cron job that queries a WordPress GraphQL API every hour. The fetched data is saved in a formatted JSON file (`json-output/events.json`), and a log of each execution is appended to a log file (`json-output/logs.txt`).

- The **SchedulerSetup** class is responsible for setting up the Quartz job and scheduling it.
- The **QuartzJob** class handles the actual execution, which includes calling the GraphQL API using `HttpURLConnection` and saving the response as a JSON file.

**References:**
- [Quartz Scheduler](http://www.quartz-scheduler.org/)
- [Cron Triggers in Quartz](https://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/crontrigger.html)

### 3. GraphQL Integration
The **GraphQLClient** class makes HTTP requests to the WordPress GraphQL API using `HttpURLConnection` to fetch data about online events. This data is returned as a JSON response, which is then prettified using the **Gson** library and saved.

- The manual handling of HTML entities was removed in favor of directly handling the raw JSON response and ensuring it is stored in a human-readable format.

**References:**
- [WPGraphQL Plugin](https://www.wpgraphql.com/)
- [Gson Library for JSON Processing](https://github.com/google/gson)

### 4. Cron Job and Logging
The cron job fetches the event data every hour and logs the timestamp of each successful fetch. The logs are stored in a file called `json-output/logs.txt`.

- Each job execution logs the current time and status of the execution.
- The log file serves as a history of when the API was queried and whether the data was saved successfully.

### 5. Error Handling and Robustness
The application includes basic error handling to ensure that it doesn't crash on invalid user input or when API requests fail. This is achieved through `try-catch` blocks in both the task management UI and the API fetching logic.

- Input validation ensures that non-numeric values don’t cause crashes in the console UI.
- API errors are caught, and the application returns an empty JSON object as a fallback.

**Key External Documentation:**
- [Error Handling in Java](https://docs.oracle.com/javase/tutorial/essential/exceptions/handling.html)
