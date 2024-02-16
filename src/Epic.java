import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{

	private List<Subtask> subtasks = new ArrayList<>();

	public Epic(String name, String description, Status status, int id) {
		super(name, description, status, id);
	}

	public Epic(String name, String description) {
		super(name, description);
	}

	public List<Subtask> getSubtasks() {
		return subtasks;
	}

	public void setSubtasks(List<Subtask> subtasks) {
		this.subtasks = subtasks;
	}

	@Override
	public String toString() {
		return "Epic{" + super.toString() +
				"subtasks=" + subtasks +
				"} ";
	}
}

