package com.yandex.app.service;

import com.yandex.app.model.*;
import exception.ManagerSaveException;


import java.io.*;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    protected void save() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : getAllTasks()) {
                writer.write(task.toString() + "\n");
            }
            for (Epic epic : getAllEpics()) {
                writer.write(epic.toString() + "\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(subtask.toString() + "\n");
            }

            writer.write("\n");

            List<Task> history = historyManager.getHistory();
            for (int i = 0; i < history.size(); i++) {
                writer.write(String.valueOf(history.get(i).getId()));
                if (i < history.size() - 1) {
                    writer.write(",");
                }
            }
            writer.write("\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла", e);
        }

    }

    public static Task fromString(String value) {
        String[] parts = value.split(",", 6);
        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String title = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];

        switch (type) {
            case TASK:
                Task task = new Task(title, description, id);
                task.setStatus(status);
                return task;
            case EPIC:
                Epic epic = new Epic(title, description, id);
                epic.setStatus(status);
                return epic;
            case SUBTASK:
                int epicID = Integer.parseInt(parts[5]);
                Subtask subtask = new Subtask(title, description, id, epicID);
                subtask.setStatus(status);
                return subtask;
            default:
                throw new IllegalArgumentException("Неверный типа задачи");
        }

    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null && !line.isBlank()) {
                Task task = fromString(line);
                if (task instanceof Epic) {
                    manager.epics.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    Subtask subtask = (Subtask) task;
                    manager.subtasks.put(subtask.getId(), subtask);
                    Epic epic = manager.epics.get(subtask.getEpicId());
                    if (epic != null) {
                        epic.addSubtask(subtask);
                        manager.updateEpicStatus(epic);
                    }
                } else {
                    manager.tasks.put(task.getId(), task);
                }
            }

            line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                String[] historyIds = line.split(",");
                for (String str : historyIds) {
                    int id = Integer.parseInt(str.trim());
                    Task task = manager.getTaskById(id);
                    if (task == null) {
                        task = manager.getEpicById(id);
                    }
                    if (task == null) {
                        task = manager.getSubtaskById(id);
                    }
                    if (task != null) {
                        manager.historyManager.add(task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке из файла" + file.getName(), e);
        }
        int maxId = 0;
        for (Task task : manager.getAllTasks()) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        for (Epic epic : manager.getAllEpics()) {
            if (epic.getId() > maxId) {
                maxId = epic.getId();
            }
        }
        for (Subtask subtask : manager.getAllSubtasks()) {
            if (subtask.getId() > maxId) {
                maxId = subtask.getId();
            }
        }
        InMemoryTaskManager.setCountId(maxId);
        return manager;
    }

    @Override
    public Task addTask(String title, String description) {
        Task task = super.addTask(title, description);
        save();
        return task;
    }

    @Override
    public Epic addEpic(String title, String description) {
        Epic epic = super.addEpic(title, description);
        save();
        return epic;
    }

    @Override
    public Subtask addSubtask(String title, String description, int epicId) {
        Subtask subtask = super.addSubtask(title, description, epicId);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
        save();
    }

    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
            save();
        }
        return task;

    }

    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
            save();
        }
        return epic;
    }

    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
            save();
        }
        return subtask;
    }
}
