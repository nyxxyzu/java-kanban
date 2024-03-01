package TaskManager;
import java.util.List;
import Tasks.Task;

public interface HistoryManager {

	void add(Task task);
	List<Task> getHistory();
}
