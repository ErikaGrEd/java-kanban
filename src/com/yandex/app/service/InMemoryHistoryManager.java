package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task>history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if(history.size() >= 10) {
            history.removeFirst();
        }
        history.add(task);

    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyCopy = new ArrayList<>();
        for (Task task : history) {
            historyCopy.add(task);
        }
        return historyCopy;
    }
}
