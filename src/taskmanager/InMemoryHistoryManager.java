package taskmanager;
import tasks.Task;
import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {
	private Map<Integer, Node> history = new HashMap<>();
	private Node first;
	private Node last;

	@Override
	public void add(Task task) {
		if (history.containsKey(task.getId())) {
			linkLast(removeNode(history.get(task.getId())));
		} else {
			linkLast(task);
		}
		history.put(task.getId(), last);
	}

	private void linkLast(Task element) {
		final Node oldLast = last;
		final Node newNode = new Node(oldLast, element, null);
		last = newNode;
		if (oldLast == null)
			first = newNode;
		else
			oldLast.next = newNode;
	}

	@Override
	public void remove(int id) {
		if (history.get(id) != null) {
			removeNode(history.get(id));
			history.remove(id);
		}
	}

	private Task removeNode(Node node) {
		final Task element = node.data;
		final Node next = node.next;
		final Node prev = node.prev;

		if (prev == null) {
			first = next;
		} else {
			prev.next = next;
			node.prev = null;
		}
		if (next == null) {
			last = prev;
		} else {
			next.prev = prev;
			node.next = null;
		}
		node.data = null;
		return element;
	}

	@Override
	public List<Task> getHistory() {
		List<Task> historyList = new ArrayList<>();
		for (Node x = first; x != null; x = x.next) {
			historyList.add(x.data);
		}
		return historyList;
	}
}
