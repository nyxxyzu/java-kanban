package Tasks;

import TaskManager.Status;

import java.util.Objects;

public class Task {

	private String name;
	private String description;
	private Status status;
	public int id;


	public Task(String name, String description) {
		this.name = name;
		this.description = description;
		this.status = Status.NEW;

	}
	public Task(String name, String description, Status status) {
		this.name = name;
		this.description = description;
		this.status = status;

	}

	public Task(String name, String description, Status status, int id) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Tasks.Task{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", status=" + status +
				", id=" + id +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Task task = (Task) o;
		return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, status, id);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
