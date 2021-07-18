package datafiles.stock;
import datafiles.commands.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordBook implements Serializable {
    private final List<RizpaCommand> buying;
    private final List<RizpaCommand> selling;
    private final List<RizpaCommand> completed;

    public RecordBook() {
        buying = new ArrayList<>();
        selling = new ArrayList<>();
        completed = new ArrayList<>();
    }

    public RecordBook(List<RizpaCommand> buying, List<RizpaCommand> selling, List<RizpaCommand> completed) {
        this.buying = buying;
        this.selling = selling;
        this.completed = completed;
    }

    public List<RizpaCommand> getSelling() {
        return selling;
    }

    public List<RizpaCommand> getBuying() {
        return buying;
    }

    public List<RizpaCommand> getCompleted() {
        return completed;
    }

    public void insertToCompletedCommands(RizpaCommand command) {
        completed.add(0, command);
    }

    public void insertToPendingCommands(List<RizpaCommand> commandList, RizpaCommand originalCommand) {
        if (commandList.size() == 0) {
            commandList.add(originalCommand);
        }
        else {
            commandList.add(originalCommand);
            Collections.sort(commandList);
        }
    }
}
