# Cracker User Guide

// Product screenshot goes here

Cracker is a command-line chatbot. Running `Cracker.main()` displays its banner and greeting. Use `todo`, `deadline`, or `event` to add a task, enter `list` to display saved tasks, enter `mark TASK_NUMBER` to mark a task as done, enter `unmark TASK_NUMBER` to mark a task as not done, and enter `bye` to exit. Cracker stores up to 100 tasks in memory for the current run only.

```
____________________________________________________________
 ██████╗██████╗  █████╗  ██████╗██╗  ██╗███████╗██████╗
██╔════╝██╔══██╗██╔══██╗██╔════╝██║ ██╔╝██╔════╝██╔══██╗
██║     ██████╔╝███████║██║     █████╔╝ █████╗  ██████╔╝
██║     ██╔══██╗██╔══██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗
╚██████╗██║  ██║██║  ██║╚██████╗██║  ██╗███████╗██║  ██║
 ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝
Hello! I'm Cracker.
What can I do for you?
____________________________________________________________
todo read book
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
deadline return book /by June 6th
 Got it. I've added this task:
   [D][ ] return book (by: June 6th)
 Now you have 2 tasks in the list.
____________________________________________________________
list
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: June 6th)
____________________________________________________________
mark 2
 Nice! I've marked this task as done:
   [D][X] return book (by: June 6th)
____________________________________________________________
unmark 2
 OK, I've marked this task as not done yet:
   [D][ ] return book (by: June 6th)
____________________________________________________________
bye
 Bye. Hope to see you again soon!
____________________________________________________________
```

## Adding tasks

Use one of the following commands to add a task. Dates and times are preserved as text; they do not need a particular format.

* `todo DESCRIPTION` adds a task without a date or time. Example: `todo read book`
* `deadline DESCRIPTION /by DUE_TIME` adds a task due by a time. Example: `deadline return book /by Sunday`
* `event DESCRIPTION /from START_TIME /to END_TIME` adds a task with a time range. Example: `event project meeting /from Mon 2pm /to 4pm`

```
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
```

## Listing tasks

Enter `list` to display every task that has been added during the current run, numbered from 1. A completed task is shown with `[X]`; an incomplete task is shown with `[ ]`. Each task begins with `[T]`, `[D]`, or `[E]` to identify its type.

## Marking tasks as done

Enter `mark TASK_NUMBER` to mark the numbered task as done. For example, `mark 2` marks the second task in the list.

## Marking tasks as not done

Enter `unmark TASK_NUMBER` to reverse a task's done status. For example, `unmark 2` marks the second task as not done.

## Exiting

Enter `bye` to end the program. Tasks are not saved to disk, so they will not be available the next time Cracker runs.
