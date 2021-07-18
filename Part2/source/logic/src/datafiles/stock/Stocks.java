package datafiles.stock;

import exceptions.stock.*;
import exceptions.symbol.*;

import java.util.Collection;
import java.util.HashMap;

public class Stocks {
    private final HashMap<String, Stock> Symbol2Stock;

    public Stocks(){
        Symbol2Stock = new HashMap<>();
    }

    public int getStockAmount() {
        return Symbol2Stock.size();
    }

    public boolean containsSymbol(String symbol) {
        return Symbol2Stock.containsKey(symbol.toUpperCase());
    }

    public boolean containsCompanyName(String name) {
        for (Stock stock: Symbol2Stock.values()) {
            if (stock.getCompanyName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public Collection<Stock> getAllStocks() {
        return Symbol2Stock.values();
    }

    public Stock getStock(String symbol) throws StockNotFoundException {
        Stock stock = Symbol2Stock.get(symbol);

        if (stock == null) throw new StockNotFoundException(symbol);
        return stock;
    }

    public void addStock(String symbol, Stock newStock) throws SymbolException, StockException{
        if (containsSymbol(symbol)) throw new SymbolExistsException(symbol);
        if (containsCompanyName(newStock.getCompanyName())) throw new CompanyNameException(newStock.getCompanyName());
        if (newStock.getPrice() <= 0) throw new StockPriceException(newStock.getPrice());

        Symbol2Stock.put(symbol, newStock);
    }

    public void clear() {
        Symbol2Stock.clear();
    }
}
