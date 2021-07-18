package logicinterface;
import datafiles.commands.RizpaCommand;
import datafiles.dto.RizpaCommandDto;
import datafiles.dto.StockDto;
import datafiles.generated.*;
import datafiles.stock.Stock;
import exceptions.command.CommandException;
import exceptions.stock.*;
import exceptions.symbol.*;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.Collection;

public interface RizpaInterface {
    int getStockAmount();
    Collection<StockDto> getAllStocks();
    StockDto getStock(String symbol) throws StockNotFoundException;
    void addStock(String symbol, Stock newStock) throws SymbolException, StockException;
    void buildStocksFromXMLFile(InputStream in) throws JAXBException, SymbolException, StockException;
    RseStocks deserializeFrom(InputStream in) throws JAXBException;
    void clearStocks();
    RizpaCommand createNewCommand(String[] commandDetails)
            throws IndexOutOfBoundsException, CommandException, SymbolException, StockPriceException, StockException;
    Collection<RizpaCommandDto> operateCommand(RizpaCommand command);
    String checkStockSymbol(String symbol) throws SymbolException;
    double calExchangeCycle(Collection<RizpaCommandDto> commands);
    void saveSystemInfoToStream(FileOutputStream out) throws IOException;
    void readSystemInfoFromSaveFile(FileInputStream in) throws ClassNotFoundException, IOException, StockException, SymbolException;
}
