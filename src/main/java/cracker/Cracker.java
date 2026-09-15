package cracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Cracker chatbot application.
 */
public class Cracker {
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
        List<Task> tasks = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                break;
            }

            try {
                handleCommand(command, tasks);
            } catch (CrackerException e) {
                printError(e.getMessage());
            }
            System.out.println(DIVIDER);
        }

        printFarewell();
    }

    /**
     * Handles one command.
     *
     * @param command command entered by the user
     * @param tasks tasks stored by the chatbot
     */
    private static void handleCommand(String command, List<Task> tasks) throws CrackerException {
        String trimmedCommand = command.trim();
        if (trimmedCommand.equals("list")) {
            listTasks(tasks);
            return;
        }
        if (isCommand(trimmedCommand, "mark")) {
            markTask(tasks, getCommandDetails(trimmedCommand, "mark"));
            return;
        }
        if (isCommand(trimmedCommand, "unmark")) {
            unmarkTask(tasks, getCommandDetails(trimmedCommand, "unmark"));
            return;
        }
        if (isCommand(trimmedCommand, "delete")) {
            deleteTask(tasks, getCommandDetails(trimmedCommand, "delete"));
            return;
        }
        if (isCommand(trimmedCommand, "todo")) {
            String description = getCommandDetails(trimmedCommand, "todo");
            if (description.isEmpty()) {
                throw new CrackerException("A to-do needs a description. Try: todo buy groceries");
            }
            addTask(tasks, new Todo(description));
            return;
        }
        if (isCommand(trimmedCommand, "deadline")) {
            addDeadline(tasks, getCommandDetails(trimmedCommand, "deadline"));
            return;
        }
        if (isCommand(trimmedCommand, "event")) {
            addEvent(tasks, getCommandDetails(trimmedCommand, "event"));
            return;
        }

        throw new CrackerException("I don't recognize that command. Use todo, deadline, event, list, mark, unmark, "
                + "delete, or bye.");
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
     */
    private static void listTasks(List<Task> tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Marks the specified task as completed.
     *
     * @param tasks tasks stored by the chatbot
     * @param taskNumberText task number entered by the user
     */
    private static void markTask(List<Task> tasks, String taskNumberText) throws CrackerException {
        Task task = getTask(tasks, taskNumberText);
        task.markAsDone();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Marks the specified task as incomplete.
     *
     * @param tasks tasks stored by the chatbot
     * @param taskNumberText task number entered by the user
     */
    private static void unmarkTask(List<Task> tasks, String taskNumberText) throws CrackerException {
        Task task = getTask(tasks, taskNumberText);
        task.markAsNotDone();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /**
     * Removes the specified task from the list.
     *
     * @param tasks tasks stored by the chatbot
     * @param taskNumberText task number entered by the user
     * @throws CrackerException if no tasks exist or the task number is invalid
     */
    private static void deleteTask(List<Task> tasks, String taskNumberText) throws CrackerException {
        Task task = getTask(tasks, taskNumberText);
        tasks.remove(task);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Returns the task specified by the given task number.
     *
     * @param tasks tasks stored by the chatbot
     * @param taskNumberText task number entered by the user
     * @return the requested task
     * @throws CrackerException if no tasks exist or the task number is invalid
     */
    private static Task getTask(List<Task> tasks, String taskNumberText) throws CrackerException {
        if (tasks.isEmpty()) {
            throw new CrackerException("There are no tasks yet. Add a task first.");
        }

        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new CrackerException("Enter a task number from 1 to " + tasks.size() + ".");
            }
            return tasks.get(taskNumber - 1);
        } catch (NumberFormatException e) {
            throw new CrackerException("Enter a task number from 1 to " + tasks.size() + ".");
        }
    }

    /**
     * Creates and adds a deadline task from its command details.
     *
     * @param tasks tasks stored by the chatbot
     * @param deadlineDetails description and due time entered by the user
     */
    private static void addDeadline(List<Task> tasks, String deadlineDetails) throws CrackerException {
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
        addTask(tasks, new Deadline(description, by));
    }

    /**
     * Creates and adds an event task from its command details.
     *
     * @param tasks tasks stored by the chatbot
     * @param eventDetails description, start time, and end time entered by the user
     */
    private static void addEvent(List<Task> tasks, String eventDetails) throws CrackerException {
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
        addTask(tasks, new Event(description, from, to));
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
     * @param task task to add
     */
    private static void addTask(List<Task> tasks, Task task) {
        tasks.add(task);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }
}
