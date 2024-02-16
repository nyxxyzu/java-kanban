import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class TaskManager {

	private static final HashMap<Integer, Task> tasks = new HashMap<>();
	private static final HashMap<Integer, Epic> epics = new HashMap<>();
	private static final HashMap<Integer, Subtask> subtasks = new HashMap<>();


	private int taskId = 1;
	private int epicId = 1;
	private int subtaskId = 1;


	public Task createTask(Task task) {
		task.setId(taskId++);
		tasks.put(task.getId(), task);
		return task;


	}

	public Epic createEpic(Epic epic) {
		epic.setId(epicId++);
		epics.put(epic.getId(), epic);
		return epic;

	}

	public Subtask createSubtask(int epicId, Subtask subtask) {
		if (epics.containsKey(epicId)) {
			Epic epic = epics.get(epicId);
			subtask.setId(subtaskId++);
			subtasks.put(subtask.getId(), subtask);
			epic.getSubtasks().add(subtask);


		}

		return subtask;
	}

	public void updateTask(Task task) {
		tasks.put(task.getId(), task);

	}

	public void updateEpic(Epic epic) {
		List <Subtask> subs = epics.get(epic.getId()).getSubtasks();
		epic.setSubtasks(subs);
		epics.put(epic.getId(), epic);

	}

	public void updateSubtask(Subtask subtask) {

		for (Epic epic : epics.values()) {
			for (Subtask sub : epic.getSubtasks()) {

				if (sub.getId() == subtask.getId()) {
					epic.getSubtasks().remove(sub);
					epic.getSubtasks().add(subtask);
					subtasks.put(subtask.getId(), subtask);

				}

			}
			updateStatus(epic);
		}


	}
	public Task getTask(int taskId) {
		return tasks.get(taskId);

	}

	public Epic getEpic(int epicId) {
		return epics.get(epicId);

	}

	public Subtask getSubtask(int subtaskId) {
		return subtasks.get(subtaskId);
	}

	public void clearTasks() {
		tasks.clear();
		taskId = 1;
	}
	public void clearEpics() {
		epics.clear();
		epicId = 1;
	}
	public void clearSubtasks() {
		subtasks.clear();
		subtaskId = 1;
		for (Epic epic : epics.values()) {
			epic.getSubtasks().clear();
		}
	}

	public Collection<Task> getAllTasks() {
		return tasks.values();

	}
	public Collection<Epic> getAllEpics() {
		return epics.values();

	}
	public Collection<Subtask> getAllSubtasks() {
		return subtasks.values();

	}
	public void removeTaskById(int taskId) {
		tasks.remove(taskId);

	}
	public void removeEpicById(int epicId) {
		List <Subtask> subs = epics.get(epicId).getSubtasks();
		for (Subtask sub : subs) {
			subtasks.remove(sub.getId());
		}
		epics.remove(epicId);


	}
	public void removeSubtaskById(int subtaskId) {

		for (Epic epic : epics.values()) {
			epic.getSubtasks().removeIf(sub -> sub.getId() == subtaskId);
			updateStatus(epic);
		}
		subtasks.remove(subtaskId);

	}

	private static void updateStatus(Epic epic) {
		int doneCount = 0;
		int progressCount = 0;
		for (Subtask subs : epic.getSubtasks()) {

			int taskCount = epic.getSubtasks().size();

			if (subs.getStatus() == Status.DONE) {
				doneCount++;
			}
			if (subs.getStatus() == Status.IN_PROGRESS) {
				progressCount++;
			}
			if (doneCount == taskCount) {
				epic.setStatus(Status.DONE);

			}
			if (progressCount > 0 || doneCount > 0 & doneCount != taskCount) {
				epic.setStatus(Status.IN_PROGRESS);
			} else {
				epic.setStatus(Status.NEW);
			}
		}
	}

	public List<Subtask> getAllSubtasksByEpic(int epicId) {
		Epic epic = epics.get(epicId);
		return epic.getSubtasks();

	}


}



