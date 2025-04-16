package com.yandex.app.service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.util.List;


class TaskManagerTest {

    @Test
    void testAddTaskToAnd() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1", "Описание 1", 1);
        Task task2 = new Task("Задача 2", "Описание 2", 2);
        Task task3 = new Task("Задача 3", "Описание 3", 3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size(), "Должно быть 3 задачи");
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));

    }

    @Test
    void testRemoveTask() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1", "Описание 1", 1);
        Task task2 = new Task("Задача 2", "Описание 2", 2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "После удаления должна остаться одна задача");
        assertFalse(history.contains(task1), "Задача 1 должна быть удалена");
        assertEquals(task2, history.getFirst());
    }

    @Test
    void testWithoutDuplicateTask() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1", "Описание 1", 1);
        Task task2 = new Task("Задача 2", "Описание 2", 2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1));

    }


    @Test
    void TasksAreEqualById() {
        Task task1 = new Task("Задача 1", "Описание 1", 1);
        Task task2 = new Task("Задача 2", "Описание 2", 1);
        Assertions.assertEquals(task1, task2);
    }

    @Test
    void EpicsAreEqualById() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", 1);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", 1);
        Assertions.assertEquals(epic1, epic2);
    }

    @Test
    void SubtasksAreEqualById() {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, 3);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 1, 3);
        Assertions.assertEquals(subtask1, subtask2);
    }

    TaskManager taskManager = Managers.getDefault();

    @Test
    void noConflictIds() {
        Task task1 = taskManager.addTask("Задача 1", "Описание 1");
        Task task2 = taskManager.addTask("Задача 2", "Описание 2");
        assertNotEquals(task1, task2);
    }

    @Test
    void addDifferentTypesOfTasks() {
        Task task = taskManager.addTask("Название задачи", "Описание задачи");
        Epic epic = taskManager.addEpic("Название эпика", "Описание эпика");
        Subtask subtask = taskManager.addSubtask("Название подзадачи", "Описание подзадачи", epic.getId());

        List<Task> allTasks = taskManager.getAllTasks();
        assertEquals(1, allTasks.size(), "Одна задача в списке.");
        assertEquals(task, allTasks.getFirst(), "Задачи совпадают.");

        List<Epic> allEpics = taskManager.getAllEpics();
        assertEquals(1, allEpics.size(), "Один эпик в списке.");
        assertEquals(epic, allEpics.getFirst(), "Эпики совпадают");

        List<Subtask> allSubtasks = taskManager.getAllSubtasks();
        assertEquals(1, allSubtasks.size(), "одна подзадача в списке.");
        assertEquals(subtask, allSubtasks.getFirst(), "Подзадача должна совпадают.");

        List<Integer> epicSubtasks = epic.getSubtaskIds();
        assertEquals(1, epicSubtasks.size(), "У эпика одна подзадача.");
        assertTrue(epicSubtasks.contains(subtask.getId()));
    }

    @Test
    void testNoConflictWithGeneratedIds() {
        Task task1 = taskManager.addTask("Навзвание задачи 1", "Описание задачи 1");
        int generatedId = task1.getId(); // Сохраняем сгенерированный ID

        Task task2 = taskManager.addTask("Название задачи 2", "Описание задачи 2");

        assertNotEquals(generatedId, task2.getId(), "Разные id");

        List<Task> allTasks = taskManager.getAllTasks();
        assertEquals(2, allTasks.size(), "В списке 2 задачи");
        assertEquals(task1, allTasks.get(0), "Должно совпадать");
        assertEquals(task2, allTasks.get(1), "Должно совпадать");
    }

    @Test
    void shouldReturnUnchangedTaskWhenAddedInManager() {

        Task firstTask = new Task("Название", "Описание", 6);

        taskManager.addTask(firstTask.getTitle(), firstTask.getDescription());
        Task returnTask = taskManager.getAllTasks().getFirst();

        assertEquals(firstTask.getTitle(), returnTask.getTitle(), "Заголовок задачи не изменился.");
        assertEquals(firstTask.getDescription(), returnTask.getDescription(), "Описание задачи не изменилось.");
        assertEquals(firstTask.getStatus(), returnTask.getStatus(), "Статус задачи не изменился.");
    }

    @Test
    void shouldReturnUnchangedTaskWhenAddedInHistoryManager() {
        Task task = new Task("уборка", "помыть посуду", 7);
        taskManager.addTask(task.getTitle(), task.getDescription());
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);

        List<Task> history = taskManager.getHistory();

        assertNotNull(history, "История должна содержать элементы.");
        assertEquals(1, history.size(), "История должна содержать одну задачу.");

        Task returnTask = history.getFirst();
        assertEquals("уборка", returnTask.getTitle(), "Одинаковые названия");
    }

    @Test
    void addNSubtaskToNonExistentEpic() {
        Subtask subtask = new Subtask("Название", "Описание", 1, 1000);
        Subtask result = taskManager.addSubtask(subtask.getTitle(), subtask.getDescription(), subtask.getEpicId());
        assertNull(result, "Подзадача не должна добавиться");
    }

    @Test
    void deleteNonExistentTask() {
        int countTaskBefore = taskManager.getAllTasks().size();
        taskManager.deleteTaskById(2000);
        int countTaskAfter = taskManager.getAllTasks().size();
        assertEquals(countTaskBefore, countTaskAfter, "Должны быть одинаковые");
    }

    @Test
    void updateStatusTaskWithIncorrectId() {
        Task task = new Task("Название", "Описание", 1);
        taskManager.addTask(task.getTitle(), task.getDescription());

        String titleBefore = task.getTitle();
        String descriptionBefore = task.getDescription();
        task.setId(1000);

        taskManager.updateTask(task);

        assertEquals(titleBefore, task.getTitle(), "Должны быть одинаковые");
        assertEquals(descriptionBefore, task.getDescription(), "Должны быть одинаковые");

    }
}

