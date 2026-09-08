# UI Test Plan

Document console/UI acceptance test cases here before running them with the `test-ui` skill.

## Test Cases

Add each test case using this structure:

### `<ID>` — `<aim>`

**Aim:** Describe the behavior this test verifies.

**Setup/assumptions:** List any required initial state, or write `None`.

**Command:**

```text
<command used to run the program>
```

**Console input:**

```text
<input supplied to the program>
```

**Expected output:**

```text
<complete expected console output>
```

**Variable output rule (optional):** State the permitted matching rule for intentionally variable content. Otherwise, output is compared exactly.

### UI-001 — Add, complete, and list all task types

**Aim:** Verifies that to-dos, deadlines, and events are stored as tasks and displayed with their type-specific details.

**Setup/assumptions:** The application starts with an empty in-memory task list. Java 25 is available.

**Command:**

```text
javac -d out src/main/java/cracker/*.java; @'
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
mark 1
list
bye
'@ | java -cp out cracker.Cracker
```

**Console input:**

```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
mark 1
list
bye
```

**Expected output:**

```text
____________________________________________________________
<Cracker banner>
Hello! I'm Cracker.
What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: June 6th)
 Now you have 2 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: June 6th)
 3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Variable output rule:** The six ASCII-art banner lines represented by `<Cracker banner>` are not compared because their rendering depends on the terminal character encoding. Every other line is compared exactly.

### UI-002 — Explain invalid commands and missing task details

**Aim:** Verifies that malformed commands produce specific correction guidance without adding invalid tasks.

**Setup/assumptions:** The application starts with an empty in-memory task list. Java 25 is available.

**Command:**

```text
javac -d out src/main/java/cracker/*.java; @'
todo
deadline submit report /by
event /from 2pm /to 3pm
mark zero
blah
list
bye
'@ | java -cp out cracker.Cracker
```

**Console input:**

```text
todo
deadline submit report /by
event /from 2pm /to 3pm
mark zero
blah
list
bye
```

**Expected output:**

```text
____________________________________________________________
<Cracker banner>
Hello! I'm Cracker.
What can I do for you?
____________________________________________________________
 Error: A to-do needs a description. Try: todo buy groceries
____________________________________________________________
 Error: A deadline needs a due time after /by.
____________________________________________________________
 Error: An event needs a description before /from.
____________________________________________________________
 Error: There are no tasks yet. Add a task first.
____________________________________________________________
 Error: I don't recognize that command. Use todo, deadline, event, list, mark, unmark, or bye.
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

**Variable output rule:** The six ASCII-art banner lines represented by `<Cracker banner>` are not compared because their rendering depends on the terminal character encoding. Every other line is compared exactly.
