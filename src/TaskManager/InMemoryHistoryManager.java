package TaskManager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
	private List<Task> history = new ArrayList<>();

	@Override
	public void add(Task task) {
		if (history.size() < 10) {
			history.add(task);

		} else {
			history.removeFirst();
			history.add(task);

		}
	}
	@Override
	public List<Task> getHistory() {
		return history;

	}
}