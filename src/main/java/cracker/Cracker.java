package cracker;

import java.util.Scanner;

/**
 * Entry point for the Cracker chatbot application.
 */
public class Cracker {
    /** Maximum number of tasks stored during one run of the application. */
    private static final int MAX_TASKS = 100;

    /** Divider printed between chatbot responses. */
    private static final String DIVIDER = "____________________________________________________________";

    /** Banner printed when the chatbot starts. */
    private static final String BANNER = " ██████╗██████╗  █████╗  ██████╗██╗  ██╗███████╗██████╗ \n"
            + "██╔════╝██╔══██╗██╔══██╗██╔════╝██║ ██╔╝██╔════╝██╔══██╗\n"
            + "██║     ██████╔╝███████║██║     █████╔╝ █████╗  ██████╔╝\n"
            + "██║     ██╔══██╗██╔══██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗\n"
            + "╚██████╗██║  ██║██║  ██║╚██████╗██║  ██╗███████╗██║  ██║\n"
            + " ╚═════╝╚═╝  ╚═╝╚═╝  ╚════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝\n";

    /**
     * Starts the chatbot, responding to commands until the user enters {@code bye}.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        printGreeting();

        Scanner scanner = new Scanner(System.in);
        runCommandLoop(scanner);
        scanner.close();
    }

    /**
     * Prints the chatbot's greeting.
     */
    private static void printGreeting() {
        System.out.println(DIVIDER);
        System.out.print(BANNER);
        System.out.println("Hello! I'm Cracker.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
    }

    /**
     * Reads and handles commands until the user exits the chatbot.
     *
     * @param scanner scanner used to read commands
     */
    private static void runCommandLoop(Scanner scanner) {
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                break;
            }

            taskCount = handleCommand(command, tasks, taskCount);
            System.out.println(DIVIDER);
        }

        printFarewell();
    }

    /**
     * Handles one command and returns the resulting task count.
     *
     * @param command command entered by the user
     * @param tasks tasks stored by the chatbot
     * @param taskCount number of stored tasks
     * @return the updated number of tasks
     */
    private static int handleCommand(String command, Task[] tasks, int taskCount) {
        if (command.equals("list")) {
            listTasks(tasks, taskCount);
            return taskCount;
        }
        if (command.startsWith("mark ")) {
            markTask(tasks, taskCount, command.substring("mark ".length()).trim());
            return taskCount;
        }
        if (command.startsWith("unmark ")) {
            unmarkTask(tasks, taskCount, command.substring("unmark ".length()).trim());
            return taskCount;
        }
        if (command.startsWith("todo ") && taskCount < MAX_TASKS) {
            return addTask(tasks, taskCount, new Todo(command.substring("todo ".length()).trim()));
        }
        if (command.startsWith("deadline ") && taskCount < MAX_TASKS) {
            return addDeadline(tasks, taskCount, command.substring("deadline ".length()).trim());
        }
        if (command.startsWith("event ") && taskCount < MAX_TASKS) {
            return addEvent(tasks, taskCount, command.substring("event ".length()).trim());
        }

        System.out.println(" Don't understand the command ... yet.");
        return taskCount;
    }

    /**
     * Lists all tasks stored by the chatbot.
     *
     * @param tasks tasks stored by the chatbot
     * @param taskCount number of stored tasks
     */
    private static void listTasks(Task[] tasks, int taskCount) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + "." + tasks[i]);
        }
    }

    /**
     * Marks the specified task as completed.
     *
     * @param tasks tasks stored by the chatbot
     * @param taskCount number of stored tasks
     * @param taskNumberText task number entered by the user
     */
    private static void markTask(Task[] tasks, int taskCount, String taskNumberText) {
        Task task = getTask(tasks, taskCount, taskNumberText);
        if (task == null) {
            return;
        }

        task.markAsDone();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Marks the specified task as incomplete.
     *
     * @param tasks tasks stored by the chatbot
     * @param taskCount number of stored tasks
     * @param taskNumberText task number entered by the user
     */
    private static void unmarkTask(Task[] tasks, int taskCount, String taskNumberText) {
        Task task = getTask(tasks, taskCount, taskNumberText);
        if (task == null) {
            return;
        }

        task.markAsNotDone();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /**
     * Returns the task specified by the given task number, or {@code null} when it is invalid.
     *
     * @param tasks tasks stored by the chatbot
     * @param taskCount number of stored tasks
     * @param taskNumberText task number entered by the user
     * @return the requested task, or {@code null} when the number is invalid
     */
    private static Task getTask(Task[] tasks, int taskCount, String taskNumberText) {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > taskCount) {
                System.out.println(" Please provide the number of a task in your list.");
                return null;
            }
            return tasks[taskNumber - 1];
        } catch (NumberFormatException e) {
            System.out.println(" Please provide the number of a task in your list.");
            return null;
        }
    }

    /**
     * Creates and adds a deadline task from its command details.
     *
     * @param tasks tasks stored by the chatbot
     * @param taskCount number of stored tasks
     * @param deadlineDetails description and due time entered by the user
     * @return the updated number of tasks
     */
    private static int addDeadline(Task[] tasks, int taskCount, String deadlineDetails) {
        int byIndex = deadlineDetails.indexOf(" /by ");
        if (byIndex < 0) {
            System.out.println(" Please specify a deadline using /by.");
            return taskCount;
        }

        String description = deadlineDetails.substring(0, byIndex).trim();
        String by = deadlineDetails.substring(byIndex + " /by ".length()).trim();
        return addTask(tasks, taskCount, new Deadline(description, by));
    }

    /**
     * Creates and adds an event task from its command details.
     *
     * @param tasks tasks stored by the chatbot
     * @param taskCount number of stored tasks
     * @param eventDetails description, start time, and end time entered by the user
     * @return the updated number of tasks
     */
    private static int addEvent(Task[] tasks, int taskCount, String eventDetails) {
        int fromIndex = eventDetails.indexOf(" /from ");
        int toIndex = eventDetails.indexOf(" /to ");
        if (fromIndex < 0 || toIndex <= fromIndex) {
            System.out.println(" Please specify an event using /from and /to.");
            return taskCount;
        }

        String description = eventDetails.substring(0, fromIndex).trim();
        String from = eventDetails.substring(fromIndex + " /from ".length(), toIndex).trim();
        String to = eventDetails.substring(toIndex + " /to ".length()).trim();
        return addTask(tasks, taskCount, new Event(description, from, to));
    }

    /**
     * Prints the chatbot's farewell.
     */
    private static void printFarewell() {
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }

    /**
     * Adds a task to the list and prints its confirmation.
     *
     * @param tasks tasks stored by the chatbot
     * @param taskCount number of stored tasks
     * @param task task to add
     * @return the updated number of tasks
     */
    private static int addTask(Task[] tasks, int taskCount, Task task) {
        tasks[taskCount] = task;
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        int updatedTaskCount = taskCount + 1;
        System.out.println(" Now you have " + updatedTaskCount + " tasks in the list.");
        return updatedTaskCount;
    }
}
