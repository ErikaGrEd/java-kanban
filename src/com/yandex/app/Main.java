package com.yandex.app;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Task;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Status;
import com.yandex.app.service.InMemoryHistoryManager;
import com.yandex.app.service.TaskManager;
import com.yandex.app.service.Managers;

public class Main {

    public static void main(String[] args) {

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1", "Описание 1", 1);
        Task task2 = new Task("Задача 2", "Описание 2", 2);
        Task task3 = new Task("Задача 3", "Описание 3", 3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task1);

        System.out.println("История просмотров: ");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task.getTitle());
        }

        historyManager.remove(task3.getId());

        System.out.println("Новая история просмотров: ");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task.getTitle());
        }

        historyManager.add(task3);

        System.out.println("Новая история просмотров 2: ");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task.getTitle());
        }



        /* Создание эпиков и подзадач
        //Epic epic1 = taskManager.addEpic("Эпик 1", "Описание эпика 1");
        int epic1Id = epic1.getId();

        Subtask subtask1Epic1 = taskManager.addSubtask("Подзадача 1.1", "Описание подзадачи 1.1", epic1Id);
        Subtask subtask2Epic1 = taskManager.addSubtask("Подзадача 1.2", "Описание подзадачи 1.2", epic1Id);
        Subtask subtask3Epic1 = taskManager.addSubtask("Подзадача 1.3", "Описание подзадачи 1.3", epic1Id);
        Epic epic2 = taskManager.addEpic("Эпик 2", "Описание эпика 2");
        int epic2Id = epic2.getId();

        Subtask subtask1Epic2 = taskManager.addSubtask("Подзадача 2.1", "Описание подзадачи 2.1", epic2Id);

        // Вывод всех задач
        System.out.println("Все задачи:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task.getId() + ". " + task.getTitle() +
                    ": " + task.getDescription() + ", Статус: " + task.getStatus());
        }

        System.out.println("\nВсе эпики:");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic.getId() + ". " + epic.getTitle() +
                    ": " + epic.getDescription() + ", Статус: " +
                    epic.getStatus());
        }
        System.out.println("\nВсе подзадачи:");
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(+subtask.getId() + ". Эпик " + subtask.getEpicId() + ". " + subtask.getTitle() +
                    ": " + subtask.getDescription() +
                    ", Статус: " + subtask.getStatus());
        }

        task1.setStatus(Status.IN_PROGRESS);
        subtask1Epic1.setStatus(Status.NEW);
        subtask2Epic1.setStatus(Status.DONE);
        subtask3Epic1.setStatus(Status.IN_PROGRESS);

        taskManager.updateSubtask(subtask1Epic1);
        taskManager.updateSubtask(subtask2Epic1);


        System.out.println("\nСтатусы:");
        System.out.println("Статус задачи 1: " + task1.getStatus());
        System.out.println("Статус подзадачи 1.1: " + subtask1Epic1.getStatus());
        System.out.println("Статус подзадачи 1.2: " + subtask2Epic1.getStatus());
        System.out.println("Статус подзадачи 1.3: " + subtask3Epic1.getStatus());
        System.out.println("Статус эпика 1: " + epic1.getStatus());


        taskManager.deleteTaskById(task2.getId());

        System.out.println("\nОстались задачи:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task.getId() + ". " + task.getTitle());
        }

        taskManager.deleteEpicById(epic1Id);

        System.out.println("\nОстались эпики:");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic.getId() + ". " + epic.getTitle());
        }
        System.out.println("\nОстались подзадачи:");
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask.getId() + ". " + subtask.getTitle());
            */
        }
    }
