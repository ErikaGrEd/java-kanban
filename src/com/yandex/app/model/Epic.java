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

    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }
}

