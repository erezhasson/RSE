package logicinterface;
import datafiles.commands.CommandDetails;
import datafiles.commands.RizpaCommand;
import datafiles.dto.RizpaCommandDto;
import datafiles.dto.StockDto;
import datafiles.dto.UserDto;
import datafiles.generated.*;
import datafiles.stock.Stock;
import datafiles.user.User;
import exceptions.command.CommandException;
import exceptions.name.NameException;
import exceptions.stock.*;
import exceptions.symbol.*;
import exceptions.user.UserException;
import exceptions.user.UserNotFoundException;
import ui.task.TaskWindowController;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.Collection;
import java.util.function.Consumer;

public interface RizpaInterface {
    int getStockAmount();
    Collection<StockDto> getAllStocks();
    Collection<UserDto> getAllUsers() throws StockNotFoundException;
    StockDto getStock(String symbol) throws StockNotFoundException;
    UserDto getUser(String userName) throws UserNotFoundException, StockNotFoundException;
    void addStock(String symbol, Stock newStock) throws SymbolException, StockException;
    void addUser(String name, User newUser) throws NameException;
    RizpaCommand createNewCommand(CommandDetails commandDetails, String issuedCommandUserName)
            throws IndexOutOfBoundsException, CommandException, SymbolException, StockException, UserNotFoundException;
    Collection<RizpaCommandDto> operateCommand(RizpaCommand command);
    String checkStockSymbol(String symbol) throws SymbolException;
    double calExchangeCycle(Collection<RizpaCommandDto> commands);
    void saveSystemInfoToStream(FileOutputStream out) throws IOException;
    void collectDataFromFile(TaskWindowController controller, String fileName,
                             Consumer<Long> totalStockDelegate, Consumer<Long> totalUsersDelegate,
                             Runnable onFinish);
}
