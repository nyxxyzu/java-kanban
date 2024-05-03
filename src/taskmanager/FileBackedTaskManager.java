package taskmanager;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

	private final File file;

	public FileBackedTaskManager(File file) {
		this.file = file;

	}

	public static void main(String[] args) {
		FileBackedTaskManager manager = new FileBackedTaskManager(new File("tasks.csv"));
		manager.createTask(new Task("name1","desc1"));
		manager.createTask(new Task("name2","desc11", 60,"26.07.2024.15:00"));
		manager.createEpic(new Epic("epic1","epic desc1"));
		manager.createEpic(new Epic("epic2","epic desc2"));
		manager.createSubtask(new Subtask("sub1", "sub desc1",3));
		manager.createSubtask(new Subtask("sub2", "sub desc2",4,60,"25.05.2023.11:00"));
		manager.createSubtask(new Subtask("sub3", "sub desc3",4,30,"25.05.2023.12:00"));
		if (manager.createSubtask(new Subtask("sub2133", "sub desc23",4,20,"26.07.2024.15:50")) == null) {
			System.out.println("Задача пересекает другую по времени.");
		}
		manager.getTask(1);
		manager.getTask(2);
		manager.getTask(1);
		manager.getSubtask(5);
		manager.getEpic(4);
		manager.getEpic(3);
		TaskManager newManager = loadFromFile(new File("tasks.csv"));
		newManager.createSubtask(new Subtask("sub4", "sub desc4",4,30,"25.05.2023.12:30"));
		System.out.println(newManager.getPrioritizedTasks());

		}

	@Override
	public Task createTask(Task task) {
		if (super.createTask(task) == task) {
			save();
			return task;
		} else {
			return null;
		}
	}

	@Override
	public Epic createEpic(Epic epic) {
		super.createEpic(epic);
		save();
		return epic;

	}

	@Override
	public Subtask createSubtask(Subtask subtask) {
		if (super.createSubtask(subtask) == subtask) {
			save();
			return subtask;
		} else {
			return null;
		}
	}

	@Override
	public Task updateTask(Task task) {
		if (super.updateTask(task) != null) {
			save();
			return task;
		} else {
			return null;
		}
	}

	@Override
	public Epic updateEpic(Epic epic) {
		if (super.updateEpic(epic) != null) {
			save();
			return epic;
		} else {
			return null;
		}
	}

	@Override
	public Subtask updateSubtask(Subtask subtask) {
		if (super.updateSubtask(subtask) != null) {
			save();
			return subtask;
		} else {
			return null;
		}
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
			fileWriter.write("id,type,name,status,description,duration,starttime,endtime,epic\n");
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
							if (convertedEpic.getId() > maxId) {
								maxId = convertedEpic.getId();
							}
							break;
						case "SUBTASK":
							Subtask convertedSubtask = (Subtask) Converter.fromString(line);
							newManager.subtasks.put(convertedSubtask.getId(), convertedSubtask);
							newManager.epics.get(convertedSubtask.getEpicId()).getSubtaskIds().add(convertedSubtask.getId());
							if (convertedSubtask.getId() > maxId) {
								maxId = convertedSubtask.getId();
							}
							break;
						case "TASK":
							Task convertedTask = Converter.fromString(line);
							newManager.tasks.put(convertedTask.getId(), convertedTask);
							if (convertedTask.getId() > maxId) {
								maxId = convertedTask.getId();
							}
							break;
						case "type":
							break;
						default:
							List<Integer> historyList = Converter.historyFromString(lastLine);
							fillHistory(historyList, newManager);
					}
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
