package taskmanager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;


public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager manager = Managers.getDefault();
        Task task1 = manager.createTask(new Task("Уборка", "Убрать квартиру"));
        Task task2 = manager.createTask(new Task("Магазин", "Купить продукты"));
        Task task3 = manager.createTask(new Task("name3","desc3",50,"26.07.2024.15:00"));
        Epic epic1 = manager.createEpic(new Epic("Поездка", "Поездка на пляж"));
        Epic epic2 = manager.createEpic(new Epic("Стирка", "Постирать белье"));
        Subtask subtask2 = manager.createSubtask(new Subtask("Постирать цветное", "Постирать цветное белье", 5, 30, "26.07.2024.12:00"));
        Subtask subtask1 = manager.createSubtask(new Subtask("Постирать белое", "Постирать белое белье", 5, 50, "26.07.2024.12:30"));
        Subtask subtask3 = manager.createSubtask(new Subtask("Собрать вещи", "Собрать вещи в дорогу", 4));
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
        manager.getEpic(5);
        manager.getEpic(4);
        manager.getEpic(5);
        manager.getEpic(4);
        manager.getSubtask(6);
        manager.getSubtask(7);
        manager.getSubtask(6);
        manager.getTask(2);
        manager.getTask(1);
        printAllTasks(manager);
        System.out.println("=========================================================================================");
        manager.removeTaskById(1);
        printAllTasks(manager);
        manager.createTask(new Task("name10","desc10",50,"22.03.2005.12:00"));
        manager.createTask(new Task("name11","desc11",50,"22.03.2008.12:00"));
        System.out.println(manager.getPrioritizedTasks());
        manager.updateTask(new Task("name1313","desc1313", Status.NEW, 9,40,"24.03.2005.13:37"));
        if (manager.createTask(new Task("name11","desc11",39,"22.03.2008.12:20")) == null) {
            System.out.println("Задача пересекает другую по времени");
        }
        if (manager.createSubtask(new Subtask("name123123", "dsec123123", 5, 20,"26.07.2024.13:00")) == null) {
            System.out.println("Задача пересекает другую по времени");
        }
        if (!manager.updateTask(new Task("name10","desc10", Status.DONE, 9,50,"22.03.2008.12:49"))) {
            System.out.println("Такой задачи не существует или она пересекается по времени.");
        }
        System.out.println(manager.getAllSubtasksByEpic(4));
        manager.updateSubtask(new Subtask("name","desc",Status.DONE,7,5,30,"25.05.2001.12:00"));
        manager.removeTaskById(9);
        System.out.println(manager.getPrioritizedTasks());



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
