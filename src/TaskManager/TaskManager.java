package TaskManager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class TaskManager {

	private HashMap<Integer, Task> tasks = new HashMap<>();
	private HashMap<Integer, Epic> epics = new HashMap<>();
	private HashMap<Integer, Subtask> subtasks = new HashMap<>();


	private int taskId = 1;



	public Task createTask(Task task) {
		task.setId(incrementId());
		tasks.put(task.getId(), task);
		return task;


	}

	public Epic createEpic(Epic epic) {
		epic.setId(incrementId());
		epics.put(epic.getId(), epic);
		return epic;

	}

	public Subtask createSubtask(Subtask subtask) {
		subtask.setId(incrementId());
		subtasks.put(subtask.getId(), subtask);
		Epic epic = epics.get(subtask.getEpicId());
		epic.getSubtaskIds().add(subtask.getId());
		updateStatus(epic);
		return subtask;

	}

	public void updateTask(Task task) {
		if (tasks.containsKey(task.getId())) {
			tasks.put(task.getId(), task);

		} else {
			System.out.println("Такой задачи нет.");
		}
	}

	public void updateEpic(Epic epic) {
		if (epics.containsKey(epic.getId())){
			List<Integer> subtaskIds = epics.get(epic.getId()).getSubtaskIds();
			epic.setSubtaskIds(subtaskIds);
			epics.put(epic.getId(), epic);
			updateStatus(epic);
		} else {
			System.out.println("Такого эпика не существует");
		}
	}

	public void updateSubtask(Subtask subtask) {
		if (subtasks.containsKey(subtask.getId())) {
			subtasks.put(subtask.getId(), subtask);
			Epic epic = epics.get(subtask.getEpicId());
			updateStatus(epic);

		} else {
			System.out.println("Такой подзадачи нет");
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
	}
	public void clearEpics() {
		epics.clear();
		clearSubtasks();
	}
	public void clearSubtasks() {
		subtasks.clear();
		for (Epic epic : epics.values()) {
			epic.getSubtaskIds().clear();
			updateStatus(epic);
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
		List<Integer> subs = epics.get(epicId).getSubtaskIds();
		for (Integer sub : subs) {
			subtasks.remove(sub);
		}
		epics.remove(epicId);


	}
	public void removeSubtaskById(int subtaskId) {
		Epic epic = epics.get(subtasks.get(subtaskId).getEpicId());
		epic.getSubtaskIds().removeIf(sub -> sub.equals(subtaskId));
		updateStatus(epic);
		subtasks.remove(subtaskId);


	}

	private  void updateStatus(Epic epic) {
		int doneCount = 0;
		for (Integer subs : epic.getSubtaskIds()) {
			int taskCount = epic.getSubtaskIds().size();

			if (subtasks.get(subs).getStatus() == Status.DONE) {
				doneCount++;
			}
			if (subtasks.get(subs).getStatus() == Status.IN_PROGRESS) {
				epic.setStatus(Status.IN_PROGRESS);
				break;
			}
			if (doneCount == taskCount) {
				epic.setStatus(Status.DONE);
			} else if (doneCount > 0) {
				epic.setStatus(Status.IN_PROGRESS);
			} else {
				epic.setStatus(Status.NEW);
			}
		}
		if (epic.getSubtaskIds().isEmpty()) {
			epic.setStatus(Status.NEW);
		}
	}

	private int incrementId() {
		return taskId++;

	}

	public List<Subtask> getAllSubtasksByEpic(int epicId) {
		Epic epic = epics.get(epicId);
		List<Subtask> subtaskList = new ArrayList<>();
		for (Integer subs : epic.getSubtaskIds()) {
			subtaskList.add(subtasks.get(subs));
		}
		return subtaskList;
	}
}



