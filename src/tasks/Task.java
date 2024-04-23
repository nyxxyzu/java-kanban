package tasks;

import taskmanager.Status;
import taskmanager.Type;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

	protected String name;
	protected String description;
	protected Status status;
	private Type type = Type.TASK;
	protected int id;
	protected Duration duration;
	protected ZonedDateTime startTime;

	protected static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm");


	public Task(String name, String description) {
		this.name = name;
		this.description = description;
		this.status = Status.NEW;

	}

	public Task(String name, String description, long duration, String startTime) {
		this.name = name;
		this.description = description;
		this.status = Status.NEW;
		this.duration = Duration.ofMinutes(duration);
		this.startTime = ZonedDateTime.of(LocalDateTime.parse(startTime, TIME_FORMATTER), ZoneId.of("Europe/Moscow"));

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

	public Task(String name, String description, Status status, int id, long duration, String startTime) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.id = id;
		this.duration = Duration.ofMinutes(duration);
		this.startTime = ZonedDateTime.of(LocalDateTime.parse(startTime, TIME_FORMATTER), ZoneId.of("Europe/Moscow"));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		if (duration != null && startTime != null && getEndTime() != null) {
			return id + "," + type + "," + name + "," + status + "," + description + "," + duration.toMinutes() + ","
					+ startTime.format(TIME_FORMATTER) + "," + getEndTime().format(TIME_FORMATTER) + ",";
		} else {
			return id + "," + type + "," + name + "," + status + "," + description + ",";
		}
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

	public ZonedDateTime getEndTime() {
		return startTime.plus(duration);
	}

	public Duration getDuration() {
		return duration;
	}

	public ZonedDateTime getStartTime() {
		return startTime;
	}

}
