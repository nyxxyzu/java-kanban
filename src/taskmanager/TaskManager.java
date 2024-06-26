package taskmanager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TaskManager {
	Task createTask(Task task);

	Epic createEpic(Epic epic);

	Subtask createSubtask(Subtask subtask);

	Task updateTask(Task task);

	Epic updateEpic(Epic epic);

	Subtask updateSubtask(Subtask subtask);

	Task getTask(int taskId);

	Epic getEpic(int epicId);

	Subtask getSubtask(int subtaskId);

	void clearTasks();

	void clearEpics();

	void clearSubtasks();

	Collection<Task> getAllTasks();

	Collection<Epic> getAllEpics();

	Collection<Subtask> getAllSubtasks();

	void removeTaskById(int taskId);

	void removeEpicById(int epicId);

	void removeSubtaskById(int subtaskId);

	List<Subtask> getAllSubtasksByEpic(int epicId);

	List<Task> getHistory();

	Set<Task> getPrioritizedTasks();

	void setTaskId(int taskId);
}
