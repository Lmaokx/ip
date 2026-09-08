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

            try {
                taskCount = handleCommand(command, tasks, taskCount);
            } catch (CrackerException e) {
                printError(e.getMessage());
            }
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
    private static int handleCommand(String command, Task[] tasks, int taskCount) throws CrackerException {
        String trimmedCommand = command.trim();
        if (trimmedCommand.equals("list")) {
            listTasks(tasks, taskCount);
            return taskCount;
        }
        if (isCommand(trimmedCommand, "mark")) {
            markTask(tasks, taskCount, getCommandDetails(trimmedCommand, "mark"));
            return taskCount;
        }
        if (isCommand(trimmedCommand, "unmark")) {
            unmarkTask(tasks, taskCount, getCommandDetails(trimmedCommand, "unmark"));
            return taskCount;
        }
        if (isCommand(trimmedCommand, "todo")) {
            String description = getCommandDetails(trimmedCommand, "todo");
            if (description.isEmpty()) {
                throw new CrackerException("A to-do needs a description. Try: todo buy groceries");
            }
            return addTask(tasks, taskCount, new Todo(description));
        }
        if (isCommand(trimmedCommand, "deadline")) {
            return addDeadline(tasks, taskCount, getCommandDetails(trimmedCommand, "deadline"));
        }
        if (isCommand(trimmedCommand, "event")) {
            return addEvent(tasks, taskCount, getCommandDetails(trimmedCommand, "event"));
        }

        throw new CrackerException("I don't recognize that command. Use todo, deadline, event, list, mark, unmark, "
                + "or bye.");
    }

    /**
     * Returns whether the input begins with the specified command and no partial command name.
     *
     * @param input user input with surrounding whitespace removed
     * @param commandName supported command name
     * @return whether the input is the command or begins with the command followed by whitespace
     */
    private static boolean isCommand(String input, String commandName) {
        return input.equals(commandName)
                || input.startsWith(commandName) && Character.isWhitespace(input.charAt(commandName.length()));
    }

    /**
     * Returns the text after a command name.
     *
     * @param input user input with surrounding whitespace removed
     * @param commandName supported command name at the start of the input
     * @return command details with surrounding whitespace removed
     */
    private static String getCommandDetails(String input, String commandName) {
        return input.substring(commandName.length()).trim();
    }

    /**
     * Prints a user-facing explanation for invalid input.
     *
     * @param message explanation and correction for the invalid input
     */
    private static void printError(String message) {
        System.out.println(" Error: " + message);
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
    private static void markTask(Task[] tasks, int taskCount, String taskNumberText) throws CrackerException {
        Task task = getTask(tasks, taskCount, taskNumberText);
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
    private static void unmarkTask(Task[] tasks, int taskCount, String taskNumberText) throws CrackerException {
        Task task = getTask(tasks, taskCount, taskNumberText);
        task.markAsNotDone();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /**
     * Returns the task specified by the given task number.
     *
     * @param tasks tasks stored by the chatbot
     * @param taskCount number of stored tasks
     * @param taskNumberText task number entered by the user
     * @return the requested task
     * @throws CrackerException if no tasks exist or the task number is invalid
     */
    private static Task getTask(Task[] tasks, int taskCount, String taskNumberText) throws CrackerException {
        if (taskCount == 0) {
            throw new CrackerException("There are no tasks yet. Add a task first.");
        }

        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new CrackerException("Enter a task number from 1 to " + taskCount + ".");
            }
            return tasks[taskNumber - 1];
        } catch (NumberFormatException e) {
            throw new CrackerException("Enter a task number from 1 to " + taskCount + ".");
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
    private static int addDeadline(Task[] tasks, int taskCount, String deadlineDetails) throws CrackerException {
        int byIndex = deadlineDetails.indexOf("/by");
        if (byIndex < 0) {
            throw new CrackerException("A deadline needs /by followed by a due time.");
        }

        String description = deadlineDetails.substring(0, byIndex).trim();
        String by = deadlineDetails.substring(byIndex + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new CrackerException("A deadline needs a description before /by.");
        }
        if (by.isEmpty()) {
            throw new CrackerException("A deadline needs a due time after /by.");
        }
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
    private static int addEvent(Task[] tasks, int taskCount, String eventDetails) throws CrackerException {
        int fromIndex = eventDetails.indexOf("/from");
        int toIndex = eventDetails.indexOf("/to");
        if (fromIndex < 0 || toIndex <= fromIndex) {
            throw new CrackerException("An event needs /from and /to, for example: event meeting /from 2pm /to 3pm");
        }

        String description = eventDetails.substring(0, fromIndex).trim();
        String from = eventDetails.substring(fromIndex + "/from".length(), toIndex).trim();
        String to = eventDetails.substring(toIndex + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new CrackerException("An event needs a description before /from.");
        }
        if (from.isEmpty()) {
            throw new CrackerException("An event needs a start time after /from.");
        }
        if (to.isEmpty()) {
            throw new CrackerException("An event needs an end time after /to.");
        }
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
    private static int addTask(Task[] tasks, int taskCount, Task task) throws CrackerException {
        if (taskCount == MAX_TASKS) {
            throw new CrackerException("Your task list is full. Remove a task before adding another one.");
        }

        tasks[taskCount] = task;
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        int updatedTaskCount = taskCount + 1;
        System.out.println(" Now you have " + updatedTaskCount + " tasks in the list.");
        return updatedTaskCount;
    }
}
