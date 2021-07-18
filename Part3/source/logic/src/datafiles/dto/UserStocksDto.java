package datafiles.dto;

import java.util.Collection;
import java.util.HashMap;

public class UserStocksDto {
    private HashMap<StockDto, Integer> stocks;

    public UserStocksDto() {
        this.stocks = new HashMap<>();
    }

    public void addStock(StockDto stock, int amount) {
        stocks.put(stock, amount);
    }

    public StockDto getStock(StockDto stock) {
        for (StockDto stockdto : stocks.keySet()) {
            if (stockdto.getSymbol().equalsIgnoreCase(stock.getSymbol())) {
                return stockdto;
            }
        }

        return null;
    }

    public int size() {
        return stocks.size();
    }

    public Integer getStockAmount(String stockSymbol) {
        for (StockDto stockdto : stocks.keySet()) {
            if (stockdto.getSymbol().equalsIgnoreCase(stockSymbol)) {
                return stocks.get(stockdto);
            }
        }

        return 0;
    }

    public Collection<StockDto> getAllStocks() {
        return stocks.keySet();
    }
}
