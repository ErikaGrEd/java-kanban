package com.yandex.app.service;

import com.yandex.app.model.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final  Map<Integer, Node> historyMap = new HashMap<>();
    private Node head;
    private Node tail;


     private Node linkLast(Task task) {
         Node newNode = new Node(task, tail, null);
    if (tail == null) {
        head = newNode;
    } else {
        tail.next = newNode;
    }
    tail = newNode;
    return newNode;
    }

    public  List<Task> getTasks() {
    List <Task> tasks = new ArrayList<>();
    Node currentNode = head;
    while (currentNode != null) {
        tasks.add(currentNode.task);
        currentNode = currentNode.next;
    }
    return tasks;
    }

    private void removeNode(Node node) {
      if (node == null) {
          return;
      }

        if (node.prev != null) {
          node.prev.next = node.next ;
        } else {
            head = node.next;
        }
         if (node.next != null) {
    node.next.prev = node.prev;
         } else {
           tail = node.prev;
       }
    }

    @Override
    public void add(Task task) {

         if (task == null) {
             return;
         }

        if (historyMap.containsKey(task.getId())) {
            removeNode(historyMap.get(task.getId()));
        }
        Node newNode = linkLast(task);

        historyMap.put(task.getId(), newNode);
    }

        @Override
        public List<Task> getHistory () {
            return getTasks();
        }
            @Override
            public void remove ( int id){
                if(historyMap.containsKey(id)) {
                    removeNode(historyMap.get(id));
                    historyMap.remove(id);
            }

        }
    }