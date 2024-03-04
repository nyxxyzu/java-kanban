package TaskManager;

import Tasks.Task;

import java.util.LinkedList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
	private List<Task> history = new LinkedList<>();
	private static final int HISTORY_MAX_SIZE = 10;

	@Override
	public void add(Task task) {
		if (history.size() < HISTORY_MAX_SIZE) {
			history.add(task);

		} else {
			history.remove(0);
			history.add(task);

		}
	}
	@Override
	public List<Task> getHistory() {
		List<Task> historyCopy = new LinkedList<>(history);
		return historyCopy;
	}
}
