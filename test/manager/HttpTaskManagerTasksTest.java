package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import task.Epic;
import task.Statuses;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {
    TaskManager<Task> manager = new InMemoryTaskManager<>();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Statuses.NEW, LocalDateTime.now(), Duration.ofMinutes(5));

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8050/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldReturnTwoTasksOneEpicAndTwoSubtasks() throws IOException, InterruptedException {
        Task task1 = new Task("Test 1", "Testing task 1",
                Statuses.NEW);
        Task task2 = new Task("Test 2", "Testing task 2",
                Statuses.IN_PROGRESS);
        Epic epic = new Epic("Epic 1", "Epic description 1");
        Subtask subtask1 = new Subtask("Subtask 1", "Testing subtask 1", 3, Statuses.DONE);
        Subtask subtask2 = new Subtask("Subtask 2", "Testing subtask 2", 3, Statuses.DONE);

        String taskJson1 = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);
        String taskJson3 = gson.toJson(epic);
        String taskJson4 = gson.toJson(subtask1);
        String taskJson5 = gson.toJson(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8050/tasks");
        URI url2 = URI.create("http://localhost:8050/subtasks");
        URI url3 = URI.create("http://localhost:8050/epics");

        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(HttpRequest.BodyPublishers.ofString(taskJson1)).build();
        HttpRequest request2 = HttpRequest.newBuilder().uri(url1).POST(HttpRequest.BodyPublishers.ofString(taskJson2)).build();
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).POST(HttpRequest.BodyPublishers.ofString(taskJson3)).build();
        HttpRequest request4 = HttpRequest.newBuilder().uri(url2).POST(HttpRequest.BodyPublishers.ofString(taskJson4)).build();
        HttpRequest request5 = HttpRequest.newBuilder().uri(url2).POST(HttpRequest.BodyPublishers.ofString(taskJson5)).build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertEquals(200, response2.statusCode());
        assertEquals(200, response3.statusCode());
        assertEquals(200, response4.statusCode());
        assertEquals(200, response5.statusCode());

        try {
            manager.getTask(1);
            manager.getSubtask(5);
            manager.getEpic(3);
        } catch (NotFoundException ex) {
            System.out.println(ex);
        }

        URI url4 = URI.create("http://localhost:8050/history");

        HttpRequest request6 = HttpRequest.newBuilder().uri(url4).GET().build();

        HttpResponse<String> response6 = client.send(request6, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response6.statusCode());
        assertEquals("[1,class task.Task,Test 1,NEW,Testing task 1, " +
                "5,class task.Subtask,Subtask 2,DONE,Testing subtask 2,3, " +
                "3,class task.Epic,Epic 1,DONE,Epic description 1]", manager.getHistory().toString().trim());
    }

    @Test
    public void shouldDeleteOneTaskFromTwoTasksAndReturnLastOne() throws IOException, InterruptedException {
        Task task1 = new Task("Test 1", "Testing task 1",
                Statuses.NEW);
        Task task2 = new Task("Test 2", "Testing task 2",
                Statuses.IN_PROGRESS);

        String taskJson1 = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8050/tasks");

        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(HttpRequest.BodyPublishers.ofString(taskJson1)).build();
        HttpRequest request2 = HttpRequest.newBuilder().uri(url1).POST(HttpRequest.BodyPublishers.ofString(taskJson2)).build();

        client.send(request1, HttpResponse.BodyHandlers.ofString());
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        try {
            manager.getTask(1);
            manager.getTask(2);
        } catch (NotFoundException ex) {
            System.out.println(ex);
        }

        URI url2 = URI.create("http://localhost:8050/tasks/2");

        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).DELETE().build();

        client.send(request3, HttpResponse.BodyHandlers.ofString());

        URI url3 = URI.create("http://localhost:8050/history");

        HttpRequest request4 = HttpRequest.newBuilder().uri(url3).GET().build();

        HttpResponse<String> response6 = client.send(request4, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response6.statusCode());
        assertEquals("[1,class task.Task,Test 1,NEW,Testing task 1]", manager.getHistory().toString().trim());
    }

    @Test
    public void shouldReturnPrioritizedListOfTask() throws IOException, InterruptedException {
        Task task1 = new Task("Test 1", "Testing task 1",
                Statuses.NEW, LocalDateTime.of(2004, 12, 5, 5, 5), Duration.ofMinutes(5));
        Task task2 = new Task("Test 2", "Testing task 2",
                Statuses.NEW, LocalDateTime.of(2004, 12, 5, 5, 5).minusDays(5), Duration.ofMinutes(5));
        Task task3 = new Task("Test 3", "Testing task 3",
                Statuses.NEW, LocalDateTime.of(2004, 12, 5, 5, 5).plusDays(21), Duration.ofMinutes(5));

        String taskJson1 = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);
        String taskJson3 = gson.toJson(task3);

        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8050/tasks");

        HttpRequest request1 = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson1)).build();
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson2)).build();
        HttpRequest request3 = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson3)).build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8050/prioritized");

        HttpRequest request4 = HttpRequest.newBuilder().uri(url2).GET().build();

        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());

        System.out.println(manager.getPrioritizedTasks());

        assertEquals(200, response4.statusCode());
        assertEquals("[2,class task.Task,Test 2,NEW,Testing task 2,05:05 30:11:04,5,05:10 30:11:04, " +
                "1,class task.Task,Test 1,NEW,Testing task 1,05:05 05:12:04,5,05:10 05:12:04, " +
                "3,class task.Task,Test 3,NEW,Testing task 3,05:05 26:12:04,5,05:10 26:12:04]", manager.getPrioritizedTasks().toString().trim());
    }
}
