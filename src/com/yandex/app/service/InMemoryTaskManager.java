package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.model.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    private static int countId = 0;

    public static int generateId() {
        return ++countId;
    }

    public static void setCountId(int id) {
        if (id > countId) {
            countId = id;
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return List.copyOf(tasks.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return List.copyOf(subtasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return List.copyOf(epics.values());
    }

    @Override
    public void deleteAll() {
        tasks.clear();
        subtasks.clear();
        epics.clear();
    }

    @Override
    public Task addTask(String title, String description) {
        int id = generateId();
        Task task = new Task(title, description, id);
        tasks.put(id, task);
        return task;
    }

    @Override
    public Subtask addSubtask(String title, String description, int epicId) {
        if (!epics.containsKey(epicId)) {
            return null;
        }

        int id = generateId();
        Subtask subtask = new Subtask(title, description, id, epicId);
        subtasks.put(id, subtask);
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.addSubtask(subtask);
            updateEpicStatus(epic);
        }
        return subtask;
    }

    @Override
    public Epic addEpic(String title, String description) {
        int id = generateId();
        Epic epic = new Epic(title, description, id);
        epics.put(id, epic);
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        historyManager.add(task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic);
        }
        historyManager.add(subtask);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtaskToDelete = subtasks.get(id);
            subtasks.remove(id);

            Epic epic = epics.get(subtaskToDelete.getEpicId());
            if (epic != null) {
                epic.getSubtaskIds().remove(id);
                updateEpicStatus(epic);
            }
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epicToDelete = epics.get(id);
            for (int subtaskId : epicToDelete.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


    public void updateEpicStatus(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();
        boolean subtasksNew = false;
        boolean subtasksDone = false;
        boolean subtasksInProgress = false;

        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getStatus() == Status.NEW) {
                subtasksNew = true;
            } else if (subtask.getStatus() == Status.DONE) {
                subtasksDone = true;
            } else if (subtask.getStatus() == Status.IN_PROGRESS) {
                subtasksInProgress = true;
            }
        }

        if (subtaskIds.isEmpty() || subtasksNew && !subtasksDone && !subtasksInProgress) {
            epic.setStatus(Status.NEW);

        } else if (subtasksDone && !subtasksInProgress && !subtasksNew) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }
}

