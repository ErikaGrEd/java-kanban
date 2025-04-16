package com.yandex.app.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String title, String description, int id) {
        super(title, description, id);

    }

    public void addSubtask(Subtask subtask) {
        subtaskIds.add(subtask.getId());

    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }
}

