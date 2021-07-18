package datafiles.dto;

import datafiles.commands.BuyingCommand;
import datafiles.commands.RizpaCommand;
import datafiles.commands.SellingCommand;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecordBookDto {
    private final ObservableList<RizpaCommandDto> buying;
    private final ObservableList<RizpaCommandDto> selling;
    private final ObservableList<RizpaCommandDto> completed;

    public RecordBookDto(List<RizpaCommand> buying, List<RizpaCommand> selling, List<RizpaCommand> completed) {
        this.buying = buildDtoList(buying);
        this.selling = buildDtoList(selling);
        this.completed = buildDtoList(completed);
    }

    public static ObservableList<RizpaCommandDto> buildDtoList(Collection<RizpaCommand> commands) {
        ObservableList<RizpaCommandDto> commandsDto = FXCollections.observableArrayList();

        for (RizpaCommand command: commands) {
            RizpaCommandDto commandDto;
            if (command instanceof BuyingCommand) {
                commandDto = new BuyingCommandDto(command.getStockSymbol(), command.getDisplayType(), command.getPrice(),
                        command.getAmount(), command.getTimestamp(), command.getBuyer().getName(), null);
            }
            else {
                commandDto = new SellingCommandDto(command.getStockSymbol(), command.getDisplayType(), command.getPrice(),
                        command.getAmount(), command.getTimestamp(), command.getSeller().getName(), null);
            }

            if (command.getStatus()) {
                commandDto.sellerProperty().setValue(command.getSeller().getName());
                commandDto.buyerProperty().setValue(command.getBuyer().getName());
            }
            commandsDto.add(commandDto);
        }

        return commandsDto;
    }

    public ObservableList<RizpaCommandDto> getBuying() {
        return buying;
    }

    public ObservableList<RizpaCommandDto> getSelling() {
        return selling;
    }

    public ObservableList<RizpaCommandDto> getCompleted() {
        return completed;
    }


}
