package datafiles.dto;

import datafiles.commands.BuyingCommand;
import datafiles.commands.RizpaCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecordBookDto {
    private final List<RizpaCommandDto> buying;
    private final List<RizpaCommandDto> selling;
    private final List<RizpaCommandDto> completed;

    public RecordBookDto(List<RizpaCommand> buying, List<RizpaCommand> selling, List<RizpaCommand> completed) {
        this.buying = buildDtoList(buying);
        this.selling = buildDtoList(selling);
        this.completed = buildDtoList(completed);
    }

    public static List<RizpaCommandDto> buildDtoList(Collection<RizpaCommand> commands) {
        List<RizpaCommandDto> commandsDto = new ArrayList<>();

        for (RizpaCommand command: commands) {
            if (command instanceof BuyingCommand) {
                commandsDto.add(new BuyingCommandDto(command.getStockSymbol(), command.getDisplayType(), command.getPrice(),
                        command.getAmount(), command.getTimestamp()));
            }
            else {
                commandsDto.add(new SellingCommandDto(command.getStockSymbol(), command.getDisplayType(), command.getPrice(),
                        command.getAmount(), command.getTimestamp()));
            }
        }

        return commandsDto;
    }

    public List<RizpaCommandDto> getBuying() {
        return buying;
    }

    public List<RizpaCommandDto> getSelling() {
        return selling;
    }

    public List<RizpaCommandDto> getCompleted() {
        return completed;
    }
}
