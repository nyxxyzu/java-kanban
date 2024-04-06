package taskmanager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

	private File file;

	public FileBackedTaskManager(File file) {
		this.file = file;

	}

	public static void main(String[] args) {
		FileBackedTaskManager manager = new FileBackedTaskManager(new File("tasks.csv"));
		manager.createTask(new Task("name1","desc1"));
		manager.createTask(new Task("name2","desc11"));
		manager.createEpic(new Epic("epic1","epic desc1"));
		manager.createEpic(new Epic("epic2","epic desc2"));
		manager.createSubtask(new Subtask("sub1", "sub desc1",3));
		manager.createSubtask(new Subtask("sub2", "sub desc2",4));
		manager.createSubtask(new Subtask("sub3", "sub desc3",4));
		manager.getTask(1);
		manager.getTask(2);
		manager.getTask(1);
		manager.getSubtask(5);
		manager.getEpic(4);
		manager.getEpic(3);
		TaskManager newManager = loadFromFile(new File("tasks.csv"));

		}

	@Override
	public Task createTask(Task task) {
		super.createTask(task);
		save();
		return task;

	}

	@Override
	public Epic createEpic(Epic epic) {
		super.createEpic(epic);
		save();
		return epic;

	}

	@Override
	public Subtask createSubtask(Subtask subtask) {
		super.createSubtask(subtask);
		save();
		return subtask;

	}

	@Override
	public boolean updateTask(Task task) {
		super.updateTask(task);
		save();
		return true;

	}

	@Override
	public boolean updateEpic(Epic epic) {
		super.updateEpic(epic);
		save();
		return true;
	}

	@Override
	public boolean updateSubtask(Subtask subtask) {
		super.updateSubtask(subtask);
		save();
		return true;
	}

	@Override
	public Task getTask(int taskId) {
		super.getTask(taskId);
		save();
		return super.getTask(taskId);

	}


	@Override
	public Epic getEpic(int epicId) {
		super.getEpic(epicId);
		save();
		return super.getEpic(epicId);

	}

	@Override
	public Subtask getSubtask(int subtaskId) {
		super.getSubtask(subtaskId);
		save();
		return super.getSubtask(subtaskId);
	}

	@Override
	public void clearTasks() {
		super.clearTasks();
		save();
	}

	@Override
	public void clearEpics() {
		super.clearEpics();
		save();
	}

	@Override
	public void clearSubtasks() {
		super.clearSubtasks();
		save();
	}

	@Override
	public void removeTaskById(int taskId) {
		super.removeTaskById(taskId);
		save();
	}

	@Override
	public void removeEpicById(int epicId) {
		super.removeEpicById(epicId);
		save();
	}

	@Override
	public void removeSubtaskById(int subtaskId) {
		super.removeSubtaskById(subtaskId);
		save();
	}

	private void save() {
		try (Writer fileWriter = new FileWriter(file)) {
			fileWriter.write("id,type,name,status,description,epic\n");
			for (Task task : getAllTasks()) {
				fileWriter.write(task.toString() + "\n");
			}
			for (Task epic : getAllEpics()) {
				fileWriter.write(epic.toString() + "\n");
				for (Task subtask : getAllSubtasksByEpic(epic.getId())) {
					fileWriter.write(subtask.toString() + "\n");
				}
			}
			fileWriter.write(historyToString(historyManager));
		} catch (IOException exception) {
			throw new ManagerSaveException("Ошибка при сохранении в файл.");
		}
	}

	static Task fromString(String value) {
		String[] split = value.split(",");
		String type = split[1];
		Task task = null;

		switch (type) {
			case "EPIC":
				task = new Epic(split[2], split[4],Integer.parseInt(split[0]));
				break;
			case "SUBTASK":
				task = new Subtask(split[2],split[4],Status.valueOf(split[3]),Integer.parseInt(split[0]),Integer.parseInt(split[5]));
				break;
			case "TASK":
				task = new Task(split[2],split[4],Status.valueOf(split[3]),Integer.parseInt(split[0]));
				break;
		}
		return task;
	}

	static List<Integer> historyFromString(String value) {
		String[] split = value.split(",");
		List<Integer> historyList = new LinkedList<>();
		for (String id : split) {
			historyList.add(Integer.parseInt(id));
		}
		return historyList;
	}

	static FileBackedTaskManager loadFromFile(File file) {
		FileBackedTaskManager newManager = new FileBackedTaskManager(file);
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String lastLine = "";
			while (reader.ready()) {
				String line = reader.readLine();
				lastLine = line;
				if (!line.isEmpty()) {
					if (line.contains("EPIC")) {
						newManager.epics.put(fromString(line).getId(), (Epic) fromString(line));
						newManager.incrementId();
					} else if (line.contains("SUBTASK")) {
						newManager.subtasks.put(fromString(line).getId(), (Subtask) fromString(line));
						newManager.epics.get(((Subtask) fromString(line)).getEpicId()).getSubtaskIds().add(fromString(line).getId());
						newManager.incrementId();
					} else if (line.contains("TASK")) {
						newManager.tasks.put(fromString(line).getId(), fromString(line));
						newManager.incrementId();
					}

				}
			}
			if (!(lastLine.contains("EPIC") | lastLine.contains("SUBTASK") | lastLine.contains("TASK")
					| lastLine.contains("id")) & !lastLine.isEmpty()) {
				List<Integer> historyList = historyFromString(lastLine);
				for (Integer id : historyList) {
					if (newManager.tasks.containsKey(id)) {
						newManager.historyManager.add(newManager.tasks.get(id));
					} else if (newManager.subtasks.containsKey(id)) {
						newManager.historyManager.add(newManager.subtasks.get(id));
					} else {
						newManager.historyManager.add(newManager.epics.get(id));
					}
				}
			}
		} catch (IOException exception) {
			throw new ManagerLoadException("Ошибка при загрузке файла.");
		}

		return newManager;
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

}
