package tasks;

import taskmanager.Status;
import taskmanager.Type;

import java.util.Objects;

public class Task {

	String name;
	String description;
	Status status;
	private Type type = Type.TASK;
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
		return id + "," + type + "," + name + "," + status + "," + description + ",";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Task task = (Task) o;
		return id == task.id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
