package TaskManager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.Collection;
import java.util.List;

public interface TaskManager {
	Task createTask(Task task);

	Epic createEpic(Epic epic);

	Subtask createSubtask(Subtask subtask);

	boolean updateTask(Task task);

	boolean updateEpic(Epic epic);

	boolean updateSubtask(Subtask subtask);

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

}
