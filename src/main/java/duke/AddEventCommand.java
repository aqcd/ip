package duke;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Handles addition of event-based Tasks.
 */
public class AddEventCommand extends Command {
    /** duke.Command details */
    private final String[] instructions;

    /**
     * Constructor for duke.AddEventCommand.
     * @param instructions Contains description and deadline.
     */
    public AddEventCommand(String[] instructions) {
        super();
        this.instructions = instructions;
    }

    /**
     * Executes the AddEvent duke.Command, adding a new duke.Task of type duke.Event with description and date.
     * @param tasks duke.TaskList to be added to.
     * @param ui For user interaction.
     * @param storage To store the added task.
     */
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        String[] eventInfo = instructions[1].split(" /at ", 2); // [name, date]
        if (eventInfo.length < 2) {
            ui.conditionError(Constants.TaskTypes.EVENT);
            return;
        }
        try {
            Task event = new Event(eventInfo[0], LocalDate.parse(eventInfo[1]));
            tasks.addTask(event);
            storage.save(tasks);
        } catch (DateTimeParseException e) {
            ui.invalidDateError();
        }
    }
}
