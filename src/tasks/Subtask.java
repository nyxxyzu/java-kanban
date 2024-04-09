package tasks;

import taskmanager.Status;
import taskmanager.Type;

public class Subtask extends Task {

	private int epicId;
	private Type type = Type.SUBTASK;

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

	public void setEpicId(int epicId) {
		this.epicId = epicId;
	}

	@Override
	public String toString() {
		return id + "," + type + "," + name + "," + status + "," + description + "," + epicId + ",";
	}
}
