package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

	private List<Integer> subtaskIds = new ArrayList<>();

	public Epic(String name, String description) {
		super(name, description);
	}

	public Epic(String name, String description, int id) {
		super(name, description);
		this.id = id;
	}

	public List<Integer> getSubtaskIds() {
		return subtaskIds;
	}

	public void setSubtaskIds(List<Integer> subtaskIds) {
		this.subtaskIds = subtaskIds;
	}

	@Override
	public String toString() {
		return "Tasks.Epic{" + super.toString() +
				"subtasks=" + subtaskIds +
				"} ";
	}
}

