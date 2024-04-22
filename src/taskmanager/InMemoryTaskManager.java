package taskmanager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {

	Map<Integer, Task> tasks = new HashMap<>();
	Map<Integer, Epic> epics = new HashMap<>();
	Map<Integer, Subtask> subtasks = new HashMap<>();
	HistoryManager historyManager = Managers.getDefaultHistory();
	private int taskId = 1;

	@Override
	public Task createTask(Task task) {
		if (task.getStartTime() != null && isOverlapping(task)) {
			return null;
		} else {
			task.setId(incrementId());
			tasks.put(task.getId(), task);
			return task;
		}

	}

	@Override
	public Epic createEpic(Epic epic) {
		epic.setId(incrementId());
		epics.put(epic.getId(), epic);
		return epic;

	}

	@Override
	public Subtask createSubtask(Subtask subtask) {
		if (subtask.getStartTime() != null && isOverlapping(subtask)) {
			return null;
		} else {
			subtask.setId(incrementId());
			subtasks.put(subtask.getId(), subtask);
			Epic epic = epics.get(subtask.getEpicId());
			epic.getSubtaskIds().add(subtask.getId());
			updateStatus(epic);
			updateEpicTimes(epic);
			return subtask;
		}
	}

	@Override
	public boolean updateTask(Task task) {
		if (task.getStartTime() != null && isOverlapping(task)) {
			return false;
		} else {
			if (tasks.containsKey(task.getId())) {
				tasks.put(task.getId(), task);
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean updateEpic(Epic epic) {
		if (epics.containsKey(epic.getId())) {
			List<Integer> subtaskIds = epics.get(epic.getId()).getSubtaskIds();
			epic.setSubtaskIds(subtaskIds);
			epics.put(epic.getId(), epic);
			updateStatus(epic);
			updateEpicTimes(epic);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateSubtask(Subtask subtask) {
		if (subtask.getStartTime() != null && isOverlapping(subtask)) {
			return false;
		} else {
			if (epics.containsKey(subtask.getEpicId())) {
				if (subtasks.containsKey(subtask.getId()) /*&& subtask.getStartTime() != null && !isOverlapping(subtask)*/) {
					Epic epic = epics.get(subtask.getEpicId());
					subtasks.put(subtask.getId(), subtask);
					updateStatus(epic);
					updateEpicTimes(epic);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	@Override
	public Task getTask(int taskId) {
		historyManager.add(tasks.get(taskId));
		return tasks.get(taskId);

	}

	@Override
	public Epic getEpic(int epicId) {
		historyManager.add(epics.get(epicId));
		return epics.get(epicId);

	}

	@Override
	public Subtask getSubtask(int subtaskId) {
		historyManager.add(subtasks.get(subtaskId));
		return subtasks.get(subtaskId);

	}

	@Override
	public void clearTasks() {
		for (Integer key : tasks.keySet()) {
			historyManager.remove(key);
		}
		tasks.clear();
	}

	@Override
	public void clearEpics() {
		for (Integer key : epics.keySet()) {
			historyManager.remove(key);
		}
		for (Integer key : subtasks.keySet()) {
			historyManager.remove(key);
		}
		epics.clear();
		subtasks.clear();
	}

	@Override
	public void clearSubtasks() {
		for (Integer key : subtasks.keySet()) {
			historyManager.remove(key);
		}
		subtasks.clear();
		for (Epic epic : epics.values()) {
			epic.getSubtaskIds().clear();
			epic.setStatus(Status.NEW);
			updateEpicTimes(epic);
		}
	}

	@Override
	public Collection<Task> getAllTasks() {
		return tasks.values();

	}

	@Override
	public Collection<Epic> getAllEpics() {
		return epics.values();

	}

	@Override
	public Collection<Subtask> getAllSubtasks() {
		return subtasks.values();

	}

	@Override
	public void removeTaskById(int taskId) {
		historyManager.remove(taskId);
		tasks.remove(taskId);

	}

	@Override
	public void removeEpicById(int epicId) {
		historyManager.remove(epicId);
		List<Integer> subs = epics.get(epicId).getSubtaskIds();
		for (Integer sub : subs) {
			historyManager.remove(sub);
			subtasks.remove(sub);
		}
		epics.remove(epicId);


	}

	@Override
	public void removeSubtaskById(int subtaskId) {
		historyManager.remove(subtaskId);
		Epic epic = epics.get(subtasks.get(subtaskId).getEpicId());
		epic.getSubtaskIds().removeIf(sub -> sub.equals(subtaskId));
		updateStatus(epic);
		subtasks.remove(subtaskId);
		updateEpicTimes(epic);

	}

	private void updateStatus(Epic epic) {
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

	public int incrementId() {
		return taskId++;

	}

	@Override
	public List<Subtask> getAllSubtasksByEpic(int epicId) {
		return epics.get(epicId).getSubtaskIds()
				.stream()
				.map(subtaskId -> subtasks.get(subtaskId))
				.toList();
	}

	@Override
	public List<Task> getHistory() {
		return historyManager.getHistory();

	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public void updateEpicTimes(Epic epic) {
		Duration epicDuration = Duration.ofMinutes(0);
		epic.setDuration(null);
		epic.setStartTime(null);
		epic.setEndTime(null);
		for (Integer id : epic.getSubtaskIds()) {
			ZonedDateTime subtaskStartTime = subtasks.get(id).getStartTime();
			Duration subtaskDuration = subtasks.get(id).getDuration();
			if (subtaskStartTime != null) {
				ZonedDateTime subtaskEndTime = subtasks.get(id).getEndTime();
				if (epic.getStartTime() == null) {
					epic.setStartTime(subtaskStartTime);
				} else if (epic.getStartTime().isAfter(subtaskStartTime)) {
					epic.setStartTime(subtaskStartTime);
				}
				if (epic.getEndTime() == null) {
					epic.setEndTime(subtaskEndTime);
				} else if (epic.getEndTime().isBefore(subtaskEndTime)) {
					epic.setEndTime(subtaskEndTime);
				}
				epicDuration = epicDuration.plus(subtaskDuration);
			}
		}
		epic.setDuration(epicDuration);
	}

	public Set<Task> getPrioritizedTasks() {
		TreeSet<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
		for (Task task : tasks.values()) {
			if (task.getStartTime() != null) {
				sortedTasks.add(task);
			}
		}
		for (Subtask subtask : subtasks.values()) {
			if (subtask.getStartTime() != null) {
				sortedTasks.add(subtask);
			}
		}
		return sortedTasks;
	}

	public boolean isOverlapping(Task task) {
		return getPrioritizedTasks()
				.stream()
				.anyMatch(sortedTask -> sortedTask.getEndTime().isAfter(task.getStartTime())
						&& sortedTask.getStartTime().isBefore(task.getStartTime())
						|| sortedTask.getStartTime().isBefore(task.getEndTime())
						&& sortedTask.getStartTime().isAfter(task.getStartTime())
						|| sortedTask.getStartTime().equals(task.getStartTime()));

	}
}



