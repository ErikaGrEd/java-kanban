import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private static int countId = 0;

    public static int generateId() {
        return ++countId;
    }

    public ArrayList<Task> getAllTasks() {   //Получение списка задач
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Task task : tasks.values()) {
            tasksList.add(task);
        }
        return tasksList;
    }

    public ArrayList<Subtask> getAllSubtasks() {   //Получение списка подзадач
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtasksList.add(subtask);
        }
        return subtasksList;
    }

    public ArrayList<Epic> getAllEpics() {   //Получение спика эпиков
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicsList.add(epic);
        }
        return epicsList;
    }

    public void deleteAll() { //Удаление всех видов задач
        tasks.clear();
        subtasks.clear();
        epics.clear();
    }


    public Task addTask(String title, String description) { // Добавление задач
        int id = generateId();
        Task task = new Task(title, description, id);
        tasks.put(id, task);
        return task;
    }

    public Subtask addSubtask(String title, String description, int epicId) { // Добавление подзадач
        int id = generateId();
        Subtask subtask = new Subtask(title, description, id, epicId);
        subtasks.put(id, subtask);
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.addSubtask(subtask);
        }
        return subtask;
    }

    public Epic addEpic(String title, String description) { // Добавление эпиков
        int id = generateId();
        Epic epic = new Epic(title, description, id);
        epics.put(id, epic);
        return epic;
    }

    public void updateTask(Task task) { // Обновление задач
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) { // Обновление подзадач
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.updateEpicStatus();
        }
    }

    public void updateEpic(Epic epic) { // Обновление эпиков
        epics.put(epic.getId(), epic);
        epic.updateEpicStatus();
    }

    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            subtasks.remove(id);
        }
    }

    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            epics.remove(id);
        }
    }
}
