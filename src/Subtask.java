public class Subtask extends Task{

	public Subtask(String name, String description) {
		super(name, description);
	}

	public Subtask(String name, String description, Status status, int id) {
		super(name, description, status, id);
	}

	@Override
	public String toString() {
		return "Subtask{} " + super.toString();
	}
}
