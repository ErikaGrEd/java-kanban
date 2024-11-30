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

        if (subtasks.isEmpty()) {
            setStatus(Status.NEW);
            return;
        }

        boolean subtaskNew = false;
        boolean subtaskInProgress = false;

        for (Subtask subtask : subtasks.values()) {
            switch (subtask.getStatus()) {
                case NEW:
                    subtaskNew = true;
                    break;
                case IN_PROGRESS:
                    subtaskInProgress = true;
                    break;
                case DONE:
                    break;
            }
        }
        if (subtaskNew) {
            setStatus(Status.NEW);
        } else if (subtaskInProgress) {
            setStatus(Status.IN_PROGRESS);
        } else {
            setStatus(Status.DONE);
        }
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }
}
