package taskmanager;

import exceptions.NotFoundException;
import tasks.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {

	protected Map<Integer, Task> tasks = new HashMap<>();
	protected Map<Integer, Epic> epics = new HashMap<>();
	protected Map<Integer, Subtask> subtasks = new HashMap<>();
	protected HistoryManager historyManager = Managers.getDefaultHistory();
	private int taskId = 1;
	private Set<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

	@Override
	public Task createTask(Task task) {
		if (task.getStartTime() != null && isOverlapping(task)) {
			return null;
		} else {
			task.setId(incrementId());
			tasks.put(task.getId(), task);
			addTaskToSortedList(task);
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
			updateEpicStatusTime(epic);
			addTaskToSortedList(subtask);
			return subtask;
		}
	}

	@Override
	public Task updateTask(Task task) {
		if (task.getStartTime() != null && isOverlapping(task)) {
			return null;
		} else {
			if (tasks.containsKey(task.getId())) {
				tasks.put(task.getId(), task);
				addTaskToSortedList(task);
				return task;
			} else {
				throw new NotFoundException("Задача не найдена");
			}
		}
	}

	@Override
	public Epic updateEpic(Epic epic) {
		if (epics.containsKey(epic.getId())) {
			List<Integer> subtaskIds = epics.get(epic.getId()).getSubtaskIds();
			epic.setSubtaskIds(subtaskIds);
			epics.put(epic.getId(), epic);
			updateEpicStatusTime(epic);
			return epic;
		} else {
			throw new NotFoundException("Эпик не найден");
		}
	}

	@Override
	public Subtask updateSubtask(Subtask subtask) {
		if (subtask.getStartTime() != null && isOverlapping(subtask)) {
			return null;
		} else {
			if (epics.containsKey(subtask.getEpicId())) {
				if (subtasks.containsKey(subtask.getId())) {
					Epic epic = epics.get(subtask.getEpicId());
					subtasks.put(subtask.getId(), subtask);
					updateEpicStatusTime(epic);
					addTaskToSortedList(subtask);
					return subtask;
				} else {
					throw new NotFoundException("Сабтаск не найден");
				}
			} else {
				throw new NotFoundException("Эпик не найден");
			}
		}
	}

	@Override
	public Task getTask(int taskId) {
		if (tasks.containsKey(taskId)) {
			historyManager.add(tasks.get(taskId));
			return tasks.get(taskId);
		} else {
			throw new NotFoundException("Задача не найдена");
		}
	}

	@Override
	public Epic getEpic(int epicId) {
		if (epics.containsKey(epicId)) {
			historyManager.add(epics.get(epicId));
			return epics.get(epicId);
		} else {
			throw new NotFoundException("Эпик не найден");
		}
	}

	@Override
	public Subtask getSubtask(int subtaskId) {
		if (subtasks.containsKey(subtaskId)) {
			historyManager.add(subtasks.get(subtaskId));
			return subtasks.get(subtaskId);
		} else {
			throw new NotFoundException("Сабтаск не найден");
		}
	}

	@Override
	public void clearTasks() {
		for (Integer key : tasks.keySet()) {
			historyManager.remove(key);
			sortedTasks.removeIf(sortedTask -> sortedTask.getId() == key);
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
			sortedTasks.removeIf(sortedTask -> sortedTask.getId() == key);
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
		if (tasks.containsKey(taskId)) {
			historyManager.remove(taskId);
			tasks.remove(taskId);
			sortedTasks.removeIf(sortedTask -> sortedTask.getId() == taskId);
		} else {
			throw new NotFoundException("Задача не найдена");
		}
	}

	@Override
	public void removeEpicById(int epicId) {
		if (epics.containsKey(epicId)) {
			historyManager.remove(epicId);
			List<Integer> subs = epics.get(epicId).getSubtaskIds();
			for (Integer sub : subs) {
				historyManager.remove(sub);
				subtasks.remove(sub);
			}
			epics.remove(epicId);
		} else {
			throw new NotFoundException("Эпик не найден");
		}
	}

	@Override
	public void removeSubtaskById(int subtaskId) {
		if (subtasks.containsKey(subtaskId)) {
			historyManager.remove(subtaskId);
			Epic epic = epics.get(subtasks.get(subtaskId).getEpicId());
			epic.getSubtaskIds().removeIf(sub -> sub.equals(subtaskId));
			updateEpicStatusTime(epic);
			subtasks.remove(subtaskId);
			sortedTasks.removeIf(sortedTask -> sortedTask.getId() == subtaskId);
		} else {
			throw new NotFoundException("Сабтаск не найден");
		}
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
		if (epics.containsKey(epicId)) {
			return epics.get(epicId).getSubtaskIds()
					.stream()
					.map(subtaskId -> subtasks.get(subtaskId))
					.toList();
		} else {
			throw new NotFoundException("Эпик не найден");
		}
	}

	@Override
	public List<Task> getHistory() {
		return historyManager.getHistory();

	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	private void updateEpicTimes(Epic epic) {
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
		return sortedTasks;
	}

	private boolean isOverlapping(Task task) {
		return getPrioritizedTasks()
				.stream()
				.anyMatch(sortedTask -> !sortedTask.equals(task) && (sortedTask.getEndTime().isAfter(task.getStartTime())
						&& sortedTask.getStartTime().isBefore(task.getStartTime())
						|| sortedTask.getStartTime().isBefore(task.getEndTime())
						&& sortedTask.getStartTime().isAfter(task.getStartTime())
						|| sortedTask.getStartTime().equals(task.getStartTime())));

	}

	private void updateEpicStatusTime(Epic epic) {
		updateStatus(epic);
		updateEpicTimes(epic);
	}

	private void addTaskToSortedList(Task task) {
		if (task.getStartTime() != null) {
			sortedTasks.removeIf(sortedTask -> sortedTask.getId() == task.getId());
			sortedTasks.add(task);
		}
	}


}



