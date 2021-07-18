package logicinterface;

import datafiles.stock.Stock;
import datafiles.stock.Stocks;
import datafiles.user.User;
import datafiles.user.Users;
import exceptions.name.NameException;
import exceptions.stock.StockException;
import exceptions.symbol.SymbolException;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface RizpaLoaderInterface {
    void loadFileToEngine(String url);
    Stocks loadStocks() throws IOException, ClassNotFoundException, NameException, SymbolException, StockException, JAXBException;
    Users loadUsers(Stocks loadedStocks) throws IOException, StockException, JAXBException, NameException, ClassNotFoundException;
    void addStockFromFile(Stock stock) throws StockException, SymbolException;
    void addUserFromFile(User user) throws NameException;
    void clearStocks();
    void clearUsers();
}
