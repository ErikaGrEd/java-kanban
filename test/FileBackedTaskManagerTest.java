import com.yandex.app.model.Epic;
import com.yandex.app.model.Task;
import com.yandex.app.model.Subtask;
import com.yandex.app.service.FileBackedTaskManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class FileBackedTaskManagerTest {
    @Test
    void testSaveAndLoadEmptyFile() throws IOException {
        File tempFile = File.createTempFile("Tasks", ".csv");
        try {
            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
            assertTrue(loadedManager.getAllEpics().isEmpty(), "Эпики должны быть пусты");
            assertTrue(loadedManager.getAllTasks().isEmpty(), "Задачи должны быть пусты");
            assertTrue(loadedManager.getAllSubtasks().isEmpty(), "Подзадачи должны быть пусты");
        } finally {
            Files.deleteIfExists(tempFile.toPath());

        }
    }


    @Test
    void testSaveMultipleTasks() throws IOException {

        File tempFile = File.createTempFile("Tasks", ".csv");

        try {
            FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

            manager.addTask("Задача 1", "Описание задачи 1");
            manager.addTask("Задача 2", "Описание задачи 2");
            manager.addEpic("Эпик 1", "Описание эпика 1");
            manager.addEpic("Эпик 2", "Описание эпика 2");

            assertTrue(tempFile.length() > 0, "В файле должны быть задачи");

        } finally {
            Files.deleteIfExists(tempFile.toPath());

        }
    }


    @Test
    void testLoadMultipleTasks() throws IOException {
        File tempFile = File.createTempFile("Tasks", ".csv");

        try {
            FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

            var task1 = manager.addTask("Задача 1", "Описание задачи 1");
            var task2 = manager.addTask("Задача 2", "Описание задачи 2");
            Epic epic1 = manager.addEpic("Эпик 1", "Описание эпика 1");
            Epic epic2 = manager.addEpic("Эпик 2", "Описание эпика 2");
            var subtask1 = manager.addSubtask("Подзадача 1", "Описание подзадачи 1", epic1.getId());
            var subtask2 = manager.addSubtask("Подзадача 2", "Описание подзадачи 2", epic2.getId());

            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

            assertEquals(2, loadedManager.getAllTasks().size(), "Должно быть 2 задачи");
            assertEquals(2, loadedManager.getAllEpics().size(), "Должно быть 2 эпика");
            assertEquals(2, loadedManager.getAllSubtasks().size(), "Должно быть 2 подзадачи");


        } finally {
            Files.deleteIfExists(tempFile.toPath());

        }
    }

    @Test
    void testSaveAndLoadHistory() throws Exception {

        File tempFile = File.createTempFile("tasks", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        Task task1 = manager.addTask("Задача 1", "Описание 1");
        Epic epic1 = manager.addEpic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = manager.addSubtask("подзадача 1", "Описание подзадачи 1", epic1.getId());

        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getSubtaskById(subtask1.getId());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        List<Task> history = loadedManager.getHistory();

        assertEquals(3, history.size(), "История должна содержать 3 задачи");
        assertEquals(task1.getId(), history.get(0).getId());
        assertEquals(epic1.getId(), history.get(1).getId());
        assertEquals(subtask1.getId(), history.get(2).getId());
    }
}