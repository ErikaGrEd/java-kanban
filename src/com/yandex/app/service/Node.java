package com.yandex.app.service;

import com.yandex.app.model.Task;

public class Node {
    public Task task;
    public Node next;
    public Node prev;


    public Node(Task task, Node prev, Node next) {
        this.task = task;
        this.prev = prev;
        this.prev = next;
    }
}
