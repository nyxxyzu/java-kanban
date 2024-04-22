package taskmanager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.util.LinkedList;
import java.util.List;

public final class Converter {

	private Converter() {

	}

	public static Task fromString(String value) {
		String[] split = value.split(",");
		String type = split[1];
		String name = split[2];
		String description = split[4];
		Status status = Status.valueOf(split[3]);
		int id = Integer.parseInt(split[0]);
		Long duration = null;
		String startTime = null;
		String endTime = null;
		if (split.length > 6) {
			duration = Long.parseLong(split[5]);
			startTime = split[6];
			endTime = split[7];
		}
		Task task = null;

		switch (type) {
			case "EPIC":
				if (duration != null && startTime != null && endTime != null) {
					task = new Epic(name, description, duration, startTime, id, endTime);
				} else {
					task = new Epic(name, description, id);
				}
				break;
			case "SUBTASK":
				if (duration != null && startTime != null && endTime != null) {
					int epicId = Integer.parseInt(split[8]);
					task = new Subtask(name, description, status, id, epicId, duration, startTime);
				} else {
					int epicId = Integer.parseInt(split[5]);
					task = new Subtask(name, description, status, id, epicId);
				}
				break;
			case "TASK":
				if (duration != null && startTime != null && endTime != null) {
					task = new Task(name, description, status, id, duration, startTime);
				} else {
					task = new Task(name, description, status, id);
				}
				break;
		}
		return task;
	}

	public static String historyToString(HistoryManager manager) {
		StringBuilder history = new StringBuilder();
		for (Task task : manager.getHistory()) {
			history.append(task.getId()).append(",");
		}
		if (!history.isEmpty()) {
			history.setLength(history.length() - 1);
		}
		return history.toString();
	}

	public static List<Integer> historyFromString(String value) {
		String[] split = value.split(",");
		List<Integer> historyList = new LinkedList<>();
		for (String id : split) {
			historyList.add(Integer.parseInt(id));
		}
		return historyList;
	}
}
