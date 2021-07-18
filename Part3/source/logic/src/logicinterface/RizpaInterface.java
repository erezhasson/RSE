package logicinterface;

import datafiles.commands.CommandDetails;
import datafiles.commands.RizpaCommand;
import datafiles.dto.RizpaCommandDto;
import datafiles.dto.StockDto;
import datafiles.dto.UserDto;
import datafiles.stock.Stock;
import datafiles.user.User;
import exceptions.command.CommandException;
import exceptions.name.NameException;
import exceptions.stock.StockException;
import exceptions.stock.StockNotFoundException;
import exceptions.symbol.SymbolException;
import exceptions.user.UserNotFoundException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

public interface RizpaInterface {
    int getStockAmount();
    Collection<StockDto> getAllStocks();
    Collection<UserDto> getAllUsers();
    Collection<UserDto> getOnlineUsers();
    StockDto getStock(String symbol) throws StockNotFoundException;
    UserDto getUser(String userName) throws UserNotFoundException, StockNotFoundException;
    boolean isUserExists(String userName);
    void addStock(String symbol, Stock newStock) throws SymbolException, StockException;
    void addUser(String name, User newUser) throws NameException;
    void addOnlineUser(String name, User newUser) throws NameException;
    void removeUser(String name);
    void makeUserOffline(String username);
    RizpaCommand createNewCommand(CommandDetails commandDetails, String issuedCommandUserName)
            throws IndexOutOfBoundsException, CommandException, SymbolException, StockException, UserNotFoundException;
    Collection<RizpaCommandDto> operateCommand(RizpaCommand command);
    void saveSystemInfoToStream(FileOutputStream out) throws IOException;
    void chargeUserBalance(String username, double chargeAmount);
    void buildUserStockFromFile(String username, File file) throws ClassNotFoundException, JAXBException, SymbolException, StockException, IOException;
    void issueNewStock(String username, String stockSymbol, int stockAmount, String companyName, double companyWorth) throws StockException, SymbolException;
    Collection<RizpaCommandDto> getAllPendingCommands();
    Collection<RizpaCommandDto> getAllCompletedCommands();

    void addAlertToUser(String username, String alert);
}
