public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = taskManager.addTask("Задача 1", "Описание 1");
        Task task2 = taskManager.addTask("Задача 2", "Описание 2");

        // Создание эпиков и подзадач
        Epic epic1 = taskManager.addEpic("Эпик 1", "Описание эпика 1");
        int epic1Id = epic1.getId();

        Subtask subtask1Epic1 = taskManager.addSubtask("Подзадача 1.1", "Описание подзадачи 1.1", epic1Id);
        Subtask subtask2Epic1 = taskManager.addSubtask("Подзадача 1.2", "Описание подзадачи 1.2", epic1Id);

        Epic epic2 = taskManager.addEpic("Эпик 2", "Описание эпика 2");
        int epic2Id = epic2.getId();

        Subtask subtask1Epic2 = taskManager.addSubtask("Подзадача 2.1", "Описание подзадачи 2.1", epic2Id);

        // Вывод всех задач
        System.out.println("Все задачи:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task.getId() + ". " + task.getTitle() +
                    ": " + task.getDescription() + ", Статус: " + task.getStatus());
        }

        System.out.println("\nВсе эпики:");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic.getId() + ". " + epic.getTitle() +
                    ": " + epic.getDescription() + ", Статус: " +
                    epic.getStatus());
        }
        System.out.println("\nВсе подзадачи:");
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(+subtask.getId() + ". Эпик " + subtask.getEpicId() + ". " + subtask.getTitle() +
                    ": " + subtask.getDescription() +
                    ", Статус: " + subtask.getStatus());
        }


        task1.setStatus(Status.IN_PROGRESS);
        subtask1Epic1.setStatus(Status.DONE);


        epic1.updateEpicStatus();


        System.out.println("\nСтатусы:");
        System.out.println("Статус задачи 1: " + task1.getStatus());
        System.out.println("Статус подзадачи 1.1: " + subtask1Epic1.getStatus());
        System.out.println("Статус эпика 1: " + epic1.getStatus());


        taskManager.deleteTaskById(task2.getId());

        System.out.println("\nОстались задачи:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task.getId() + ". " + task.getTitle());
        }

        taskManager.deleteEpicById(epic2Id);

        System.out.println("\nОстались эпики:");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic.getId() + ". " + epic.getTitle());
        }
    }
}