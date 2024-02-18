package Tasks;

import TaskManager.Status;

public class Subtask extends Task {

	private int epicId;


	public Subtask(String name, String description, int epicId) {
		super(name, description);
		this.epicId = epicId;
	}

	public Subtask(String name, String description, Status status, int id, int epicId) {
		super(name, description, status, id);
		this.epicId = epicId;
	}

	public int getEpicId() {
		return epicId;
	}

	@Override
	public String toString() {
		return "Tasks.Subtask{} " + super.toString();
	}
}
