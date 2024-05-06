package tasks;

public class Subtask extends Task {

	private int epicId;

	public Subtask(String name, String description, int epicId) {
		super(name, description);
		this.epicId = epicId;
		this.status = Status.NEW;
		this.type = Type.SUBTASK;

	}

	public Subtask(String name, String description, int epicId, long duration, String startTime) {
		super(name, description, duration, startTime);
		this.epicId = epicId;
		this.status = Status.NEW;
		this.type = Type.SUBTASK;

	}

	public Subtask(String name, String description, Status status, int id, int epicId) {
		super(name, description, status, id);
		this.epicId = epicId;
		this.type = Type.SUBTASK;
	}

	public Subtask(String name, String description, Status status, int id, int epicId, long duration, String startTime) {
		super(name, description, status, id, duration, startTime);
		this.epicId = epicId;
		this.type = Type.SUBTASK;

	}

	public Subtask() {
		this.type = Type.SUBTASK;
		this.status = Status.NEW;
	}

	public int getEpicId() {
		return epicId;
	}

	public void setEpicId(int epicId) {
		this.epicId = epicId;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		if (duration != null && startTime != null && getEndTime() != null) {
			return id + "," + type + "," + name + "," + status + "," + description + "," + duration.toMinutes() + ","
					+ startTime.format(TIME_FORMATTER) + "," + getEndTime().format(TIME_FORMATTER) + "," + epicId + ",";
		} else {
			return id + "," + type + "," + name + "," + status + "," + description + "," + epicId + ",";
		}
	}
}
