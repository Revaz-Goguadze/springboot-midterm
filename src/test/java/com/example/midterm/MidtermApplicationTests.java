package com.example.midterm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"DELETE FROM tasks", "DELETE FROM users"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MidtermApplicationTests {

    @Autowired
    private TestRestTemplate rest;

    // --- User CRUD ---

    @Test
    void createUser() {
        var user = Map.of("name", "Alice", "email", "alice@example.com");
        ResponseEntity<Map> response = rest.postForEntity("/api/users", user, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).containsKeys("id", "name", "email");
        assertThat(response.getBody().get("name")).isEqualTo("Alice");
    }

    @Test
    void getAllUsers() {
        rest.postForEntity("/api/users", Map.of("name", "Alice", "email", "a@x.com"), Map.class);
        rest.postForEntity("/api/users", Map.of("name", "Bob", "email", "b@x.com"), Map.class);

        ResponseEntity<List> response = rest.getForEntity("/api/users", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void getUserById() {
        var created = rest.postForEntity("/api/users",
                Map.of("name", "Alice", "email", "alice@x.com"), Map.class);
        Long id = ((Number) created.getBody().get("id")).longValue();

        ResponseEntity<Map> response = rest.getForEntity("/api/users/" + id, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("name")).isEqualTo("Alice");
    }

    @Test
    void updateUser() {
        var created = rest.postForEntity("/api/users",
                Map.of("name", "Alice", "email", "alice@x.com"), Map.class);
        Long id = ((Number) created.getBody().get("id")).longValue();

        rest.put("/api/users/" + id, Map.of("name", "Alice Updated", "email", "alice@x.com"));
        ResponseEntity<Map> response = rest.getForEntity("/api/users/" + id, Map.class);
        assertThat(response.getBody().get("name")).isEqualTo("Alice Updated");
    }

    @Test
    void deleteUser() {
        var created = rest.postForEntity("/api/users",
                Map.of("name", "Alice", "email", "alice@x.com"), Map.class);
        Long id = ((Number) created.getBody().get("id")).longValue();

        rest.delete("/api/users/" + id);
        ResponseEntity<Map> response = rest.getForEntity("/api/users/" + id, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // --- Task CRUD ---

    @Test
    void createTask() {
        var user = rest.postForEntity("/api/users",
                Map.of("name", "Alice", "email", "a@x.com"), Map.class);
        Long userId = ((Number) user.getBody().get("id")).longValue();

        var task = Map.of("title", "Homework", "description", "Do it", "completed", false, "userId", userId);
        ResponseEntity<Map> response = rest.postForEntity("/api/tasks", task, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).containsKeys("id", "title", "completed", "userId", "userName");
        assertThat(response.getBody().get("title")).isEqualTo("Homework");
        assertThat(response.getBody().get("userName")).isEqualTo("Alice");
    }

    @Test
    void getAllTasks() {
        var user = rest.postForEntity("/api/users",
                Map.of("name", "Alice", "email", "a@x.com"), Map.class);
        Long userId = ((Number) user.getBody().get("id")).longValue();

        rest.postForEntity("/api/tasks",
                Map.of("title", "Task One", "description", "d1", "completed", false, "userId", userId), Map.class);
        rest.postForEntity("/api/tasks",
                Map.of("title", "Task Two", "description", "d2", "completed", true, "userId", userId), Map.class);

        ResponseEntity<List> response = rest.getForEntity("/api/tasks", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void getTaskById() {
        var user = rest.postForEntity("/api/users",
                Map.of("name", "Alice", "email", "a@x.com"), Map.class);
        Long userId = ((Number) user.getBody().get("id")).longValue();

        var created = rest.postForEntity("/api/tasks",
                Map.of("title", "Homework", "description", "Do it", "completed", false, "userId", userId), Map.class);
        Long taskId = ((Number) created.getBody().get("id")).longValue();

        ResponseEntity<Map> response = rest.getForEntity("/api/tasks/" + taskId, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("title")).isEqualTo("Homework");
    }

    @Test
    void updateTask() {
        var user = rest.postForEntity("/api/users",
                Map.of("name", "Alice", "email", "a@x.com"), Map.class);
        Long userId = ((Number) user.getBody().get("id")).longValue();

        var created = rest.postForEntity("/api/tasks",
                Map.of("title", "Homework", "description", "Do it", "completed", false, "userId", userId), Map.class);
        Long taskId = ((Number) created.getBody().get("id")).longValue();

        rest.put("/api/tasks/" + taskId,
                Map.of("title", "Homework", "description", "Do it", "completed", true, "userId", userId));
        ResponseEntity<Map> response = rest.getForEntity("/api/tasks/" + taskId, Map.class);
        assertThat(response.getBody().get("completed")).isEqualTo(true);
    }

    @Test
    void deleteTask() {
        var user = rest.postForEntity("/api/users",
                Map.of("name", "Alice", "email", "a@x.com"), Map.class);
        Long userId = ((Number) user.getBody().get("id")).longValue();

        var created = rest.postForEntity("/api/tasks",
                Map.of("title", "Homework", "description", "Do it", "completed", false, "userId", userId), Map.class);
        Long taskId = ((Number) created.getBody().get("id")).longValue();

        rest.delete("/api/tasks/" + taskId);
        ResponseEntity<Map> response = rest.getForEntity("/api/tasks/" + taskId, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // --- Validation & Error Handling ---

    @Test
    void invalidEmailReturns400() {
        var badUser = Map.of("name", "Alice", "email", "not-an-email");
        ResponseEntity<Map> response = rest.postForEntity("/api/users", badUser, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("fieldErrors");
    }

    @Test
    void blankNameReturns400() {
        var badUser = Map.of("name", "", "email", "a@x.com");
        ResponseEntity<Map> response = rest.postForEntity("/api/users", badUser, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void missingUserIdReturns404() {
        ResponseEntity<Map> response = rest.getForEntity("/api/users/99999", Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().get("message").toString()).contains("User not found");
    }

    @Test
    void taskWithoutUserReturns404() {
        var task = Map.of("title", "Test", "description", "d", "completed", false, "userId", 99999);
        ResponseEntity<Map> response = rest.postForEntity("/api/tasks", task, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void blankTaskTitleReturns400() {
        var user = rest.postForEntity("/api/users",
                Map.of("name", "Alice", "email", "a@x.com"), Map.class);
        Long userId = ((Number) user.getBody().get("id")).longValue();

        var badTask = Map.of("title", "", "description", "d", "completed", false, "userId", userId);
        ResponseEntity<Map> response = rest.postForEntity("/api/tasks", badTask, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void duplicateEmailAllowed() {
        var u1 = Map.of("name", "User One", "email", "same@x.com");
        var u2 = Map.of("name", "User Two", "email", "same@x.com");

        ResponseEntity<Map> r1 = rest.postForEntity("/api/users", u1, Map.class);
        ResponseEntity<Map> r2 = rest.postForEntity("/api/users", u2, Map.class);

        assertThat(r1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(r2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
