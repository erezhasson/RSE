package datafiles.dto;

import datafiles.stock.Stock;
import datafiles.stock.UserStocks;
import exceptions.stock.StockNotFoundException;

import java.util.Collection;

public class UserDto {
    private final String name;
    private final UserStocksDto stocks;

    public UserDto(String name, UserStocks userStocks) throws StockNotFoundException {
        this.name = name;
        stocks = new UserStocksDto();
        for(Stock stock : userStocks.getAllStocks()) {
            stocks.addStock(new StockDto(stock.getCompanyName(), stock.getSymbol(), stock.getPrice(), stock.getRecords()),
                    userStocks.getAmountOfStock(stock.getSymbol()));
        }
    }

    public Collection<StockDto> getStocks() {
        return stocks.getAllStocks();
    }

    public UserStocksDto getStockStructure() { return stocks; }

    public String getName() {
        return name;
    }

    public double investValue() {
        double value = 0;

        for (StockDto stock : stocks.getAllStocks()) {
            value += stock.getPrice() * stocks.getStockAmount(stock.getSymbol());
        }

        return value;
    }
}
