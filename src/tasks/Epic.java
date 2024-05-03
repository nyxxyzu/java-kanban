package tasks;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

	private List<Integer> subtaskIds = new ArrayList<>();

	private ZonedDateTime endTime;

	public Epic(String name, String description) {
		super(name, description);
		this.type = Type.EPIC;
	}

	public Epic(String name, String description, int id) {
		super(name, description);
		this.id = id;
		this.status = Status.NEW;
		this.type = Type.EPIC;
	}

	public Epic(String name, String description, long duration, String startTime, int id, String endTime) {
		super(name, description, duration, startTime);
		this.id = id;
		this.status = Status.NEW;
		this.type = Type.EPIC;
		this.endTime = ZonedDateTime.of(LocalDateTime.parse(endTime, TIME_FORMATTER), ZoneId.of("Europe/Moscow"));
	}

	public Epic() {
		this.type = Type.EPIC;
		this.status = Status.NEW;
	}

	public List<Integer> getSubtaskIds() {
		return subtaskIds;
	}

	public void setSubtaskIds(List<Integer> subtaskIds) {
		this.subtaskIds = subtaskIds;
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

	public void setEndTime(ZonedDateTime endTime) {
		this.endTime = endTime;
	}

	@Override
	public ZonedDateTime getEndTime() {
		return endTime;
	}


}



