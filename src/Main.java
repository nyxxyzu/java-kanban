import Tasks.Epic;
import TaskManager.Status;
import TaskManager.TaskManager;
import TaskManager.Managers;
import Tasks.Subtask;
import Tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager manager = Managers.getDefault();
        Task task1 = manager.createTask(new Task("Уборка", "Убрать квартиру"));
        Task task2 = manager.createTask(new Task("Магазин", "Купить продукты"));
        Epic epic1 = manager.createEpic(new Epic("Поездка", "Поездка на пляж"));
        Epic epic2 = manager.createEpic(new Epic("Стирка", "Постирать белье"));
        Subtask subtask1 = manager.createSubtask(new Subtask("Постирать белое", "Постирать белое белье", 4));
        Subtask subtask2 = manager.createSubtask(new Subtask("Постирать цветное", "Постирать цветное белье", 4));
        Subtask subtask3 = manager.createSubtask(new Subtask("Собрать вещи", "Собрать вещи в дорогу", 3));
        printAllTasks(manager);
        System.out.println("=========================================================================================");
        if (!manager.updateTask(new Task("Уборка", "Убрать квартиру", Status.DONE,1))) {
            System.out.println("Такой задачи нет");
        }
        if (!manager.updateTask(new Task("Магазин", "Купить продукты", Status.DONE,12))) {
            System.out.println("Такой задачи нет");
        }
        if (!manager.updateSubtask(new Subtask("Постирать белое","Постирать белое белье",Status.IN_PROGRESS,5, 4))) {
            System.out.println("Такой подзадачи нет");
        }
        if (!manager.updateEpic(new Epic("Стирочка","Постирать белье",8))) {
            System.out.println("Такого эпика нет");
        }
        printAllTasks(manager);
        System.out.println("=========================================================================================");
        manager.getTask(1);
        manager.getEpic(4);
        manager.getEpic(3);
        manager.getEpic(4);
        manager.getEpic(3);
        manager.getSubtask(5);
        manager.getSubtask(6);
        manager.getSubtask(5);
        manager.getTask(2);
        manager.getTask(1);
        printAllTasks(manager);
        System.out.println("=========================================================================================");
        manager.removeTaskById(1);
        manager.removeEpicById(3);
        manager.clearSubtasks();
        printAllTasks(manager);
    }
    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getAllSubtasksByEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        if (manager.getHistory().isEmpty()) {
            System.out.println("История пуста");
        } else {
            for (Task task : manager.getHistory()) {
                System.out.println(task);
        }
        }
    }
}
