package com.yandex.app.model;

import java.util.HashMap;

public class Epic extends Task {

    private final HashMap<Integer, Subtask> subtasks;

    public Epic(String title, String description, int id) {
        super(title, description, id);
        this.subtasks = new HashMap<>();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus();
    }

    public void updateEpicStatus() {
        boolean subtasksNew = true;
        boolean subtasksDone = true;

        for (Subtask subtask : subtasks.values()) {
            if (subtask.getStatus() != Status.NEW) {
                subtasksNew = false;
            }
            if (subtask.getStatus() != Status.DONE)
                subtasksDone = false;
        }

        if (subtasks.isEmpty() || subtasksNew) {
            setStatus(Status.NEW);
        } else if (subtasksDone) {
            setStatus(Status.DONE);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }
}

