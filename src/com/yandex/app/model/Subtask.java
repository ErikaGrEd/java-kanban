package com.yandex.app.model;

public class Subtask extends Task {

    private final int epicId;

    public Subtask(String title, String description, int id, int epicId) {
        super(title, description, id);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,SUBTASK,%s,%s,%s,%d",
                getId(),
                getTitle(),
                getStatus(),
                getDescription(),
                getEpicId()
        );
    }
}

