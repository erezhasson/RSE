package datafiles.dto;

import datafiles.commands.BuyingCommand;
import datafiles.commands.RizpaCommand;
import datafiles.commands.SellingCommand;
import datafiles.stock.RecordBook;
import datafiles.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecordBookDto {
    private final ObservableList<RizpaCommandDto> buying;
    private final ObservableList<RizpaCommandDto> selling;
    private final ObservableList<RizpaCommandDto> completed;

    public RecordBookDto(RecordBook book) {
        this.buying = buildDtoList(book.getBuying());
        this.selling = buildDtoList(book.getSelling());
        this.completed = buildDtoList(book.getCompleted());
    }

    public static ObservableList<RizpaCommandDto> buildDtoList(Collection<RizpaCommand> commands) {
        ObservableList<RizpaCommandDto> commandsDto = FXCollections.observableArrayList();

        for (RizpaCommand command: commands) {
            RizpaCommandDto commandDto;
            if (command instanceof BuyingCommand) {
                commandDto = new BuyingCommandDto((BuyingCommand)command);
            }
            else {
                commandDto = new SellingCommandDto((SellingCommand)command);
            }

            if (command.getStatus()) {
                User buyer = command.getBuyer(), seller = command.getSeller();

                if (seller != null) commandDto.sellerProperty().setValue(seller.getName());
                if (buyer != null) commandDto.buyerProperty().setValue(buyer.getName());
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

    public int getCommandsAmount() {
        return buying.size() + selling.size() + completed.size();
    }
}
