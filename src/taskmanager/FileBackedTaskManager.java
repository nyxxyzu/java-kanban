package taskmanager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

	private final File file;

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
		Task task = super.getTask(taskId);
		save();
		return task;

	}


	@Override
	public Epic getEpic(int epicId) {
		Epic epic = super.getEpic(epicId);
		save();
		return epic;

	}

	@Override
	public Subtask getSubtask(int subtaskId) {
		Subtask subtask = super.getSubtask(subtaskId);
		save();
		return subtask;
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
			fileWriter.write(Converter.historyToString(historyManager));
		} catch (IOException exception) {
			throw new ManagerSaveException("Ошибка при сохранении в файл.");
		}
	}

	public static FileBackedTaskManager loadFromFile(File file) {
		FileBackedTaskManager newManager = new FileBackedTaskManager(file);
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String lastLine = "";
			List<Integer> ids = new ArrayList<>();
			int maxId = 0;
			while (reader.ready()) {
				String line = reader.readLine();
				String[] split = line.split(",");
				String type = split[1];
				lastLine = line;
				if (!line.isEmpty()) {
					switch (type) {
						case "EPIC":
							Epic convertedEpic = (Epic) Converter.fromString(line);
							newManager.epics.put(convertedEpic.getId(), convertedEpic);
							ids.add(convertedEpic.getId());
							break;
						case "SUBTASK":
							Subtask convertedSubtask = (Subtask) Converter.fromString(line);
							newManager.subtasks.put(convertedSubtask.getId(), convertedSubtask);
							newManager.epics.get(convertedSubtask.getEpicId()).getSubtaskIds().add(convertedSubtask.getId());
							ids.add(convertedSubtask.getId());
							break;
						case "TASK":
							Task convertedTask = Converter.fromString(line);
							newManager.tasks.put(convertedTask.getId(), convertedTask);
							ids.add(convertedTask.getId());
							break;
						case "type":
							break;
						default:
							List<Integer> historyList = Converter.historyFromString(lastLine);
							fillHistory(historyList, newManager);
					}
				}
			}
			for (Integer id : ids) {
				if (id > maxId) {
					maxId = id;
				}
			}
			newManager.setTaskId(maxId + 1);
		} catch (IOException exception) {
			throw new ManagerLoadException("Ошибка при загрузке файла.");
		}

		return newManager;
	}

	private static void fillHistory(List<Integer> historyList, FileBackedTaskManager newManager) {
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
}
