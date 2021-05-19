package logicinterface;

import datafiles.commands.*;
import datafiles.dto.*;
import datafiles.generated.*;
import datafiles.stock.*;
import exceptions.stock.*;
import exceptions.symbol.*;
import exceptions.command.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;

public class RizpaEngine implements RizpaInterface {
    private final Stocks stocks;
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "datafiles.generated";


    public RizpaEngine() {
        stocks = new Stocks();
    }
    @Override
    public int getStockAmount() {
        return stocks.getStockAmount();
    }

    @Override
    public Collection<StockDto> getAllStocks() {
        Collection<StockDto> stocksDto = new ArrayList<>();

        for (Stock stock: stocks.getAllStocks()) {
            stocksDto.add(new StockDto(stock.getCompanyName(), stock.getSymbol(), stock.getPrice(), stock.getRecords()));
        }

        return stocksDto;
    }

    @Override
    public StockDto getStock(String symbol) throws StockNotFoundException {
        Stock stock =  stocks.getStock(symbol);

        return new StockDto(stock.getCompanyName(), stock.getSymbol(), stock.getPrice(), stock.getRecords());
    }

    @Override
    public void addStock(String symbol, Stock newStock) throws SymbolException, StockException {
        stocks.addStock(symbol, newStock);
    }

    @Override
    public String checkStockSymbol(String symbol) throws SymbolException {
        if (symbol.length() == 0) throw new SymbolEmptyException();
        if (!symbol.matches("[a-zA-Z]*")) throw new SymbolMismatchException(symbol);

        return symbol.toUpperCase();
    }

    @Override
    public void clearStocks() {
        stocks.clear();
    }

    @Override
    public RseStocks deserializeFrom(InputStream in) throws JAXBException{
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        RizpaStockExchangeDescriptor descriptor = (RizpaStockExchangeDescriptor) u.unmarshal(in);
        return descriptor.getRseStocks();
    }

    @Override
    public void buildStocksFromXMLFile(InputStream in) throws JAXBException, SymbolException, StockException {
        Stocks newStocks = new Stocks();
        RseStocks generatedStocks = deserializeFrom(in);

        for (RseStock stock : generatedStocks.getRseStock()) {
            Stock newStock = new Stock(stock.getRseCompanyName(), stock.getRseSymbol(), stock.getRsePrice());

            newStocks.addStock(stock.getRseSymbol(), newStock);
        }
        clearStocks();
        for (Stock stock : newStocks.getAllStocks()) {
            addStock(stock.getSymbol(), stock);
        }
    }

    @Override
    public double calExchangeCycle(Collection<RizpaCommandDto> commands) {
        double cycle = 0;

        for (RizpaCommandDto command: commands) {
            cycle += command.getPrice() * command.getAmount();
        }

        return cycle;
    }

    @Override
    public RizpaCommand createNewCommand(String[] commandDetails)
            throws IndexOutOfBoundsException, CommandException, SymbolException, StockException {
        RizpaCommand command;
        String type = checkCommandType(commandDetails[0]);
        String stockSymbol = checkSymbol(commandDetails[1]);
        int amount = checkStockAmount(commandDetails[2]);
        String direction = checkDirection(commandDetails[3]);
        double price = -1;

        if (!type.equals("MKT")) {
            price = checkPrice(commandDetails[4]);
        }

        command = direction.equalsIgnoreCase("Buying") ?
                new BuyingCommand(stockSymbol, RizpaCommand.convertStringToType(type), price, amount) :
                new SellingCommand(stockSymbol, RizpaCommand.convertStringToType(type), price, amount);

        return command;
    }

    @Override
    public Collection<RizpaCommandDto> operateCommand(RizpaCommand command) {
        Collection<RizpaCommand> completedDeals = new ArrayList<>();

        try {
            switch (command.getType()) {
                case "LMT":
                case "MKT":
                    completedDeals = operateLMTCommand(command);
                    break;
                case "FOK":
                    System.out.println("FOK");
                    break;
                case "IOC":
                    System.out.println("IOC");
                    break;
                default:
                    break;
            }
        }
        catch (StockNotFoundException ignored) {
        }

        return RecordBookDto.buildDtoList(completedDeals);
    }

    @Override
    public void saveSystemInfoToStream(FileOutputStream out) throws IOException {
        ObjectOutputStream o = new ObjectOutputStream(out);
        List<Stock> stocksList = new ArrayList<>(stocks.getAllStocks());

        o.writeObject(stocksList);
        o.close();
        out.close();
    }

    @Override
    public void readSystemInfoFromSaveFile(FileInputStream in)
            throws ClassNotFoundException, IOException, StockException, SymbolException{
        ObjectInputStream oi = new ObjectInputStream(in);
        ArrayList<?> stocks = (ArrayList<?>) oi.readObject();
        Stocks newStocks = new Stocks();

        for (Object obj : stocks) {
            Stock stock = (Stock)obj;
            RecordBook recordBook = stock.getRecords();
            Stock newStock = new Stock(stock.getCompanyName(), stock.getSymbol(), stock.getPrice());

            newStock.setRecords(new RecordBook(recordBook.getBuying(), recordBook.getSelling(), recordBook.getCompleted()));
            newStocks.addStock(stock.getSymbol(), newStock);
        }
        clearStocks();
        for (Stock stock : newStocks.getAllStocks()) {
            addStock(stock.getSymbol(), stock);
        }

        oi.close();
        in.close();
    }

    private String checkSymbol(String symbol) throws SymbolException, StockNotFoundException {
        return getStock(checkStockSymbol(symbol)).getSymbol();
    }

    private String checkCommandType(String type) throws CommandException {
        if (!RizpaCommand.isValidType(type)) throw new CommandTypeNotExistsException(type);

        return type;
    }

    private int checkStockAmount(String amount) throws CommandException, StockException {
        int stockAmount;

        try {
            stockAmount = Integer.parseInt(amount);

            if (stockAmount <= 0) throw new StockAmountException(stockAmount);
        }
        catch (NumberFormatException ex) {
            throw new CommandParsingException(amount, "Integer");
        }

        return stockAmount;
    }

    private String checkDirection(String direction) throws CommandException  {
        if (!direction.equalsIgnoreCase("Buying") && !direction.equalsIgnoreCase("Selling"))
            throw new CommandParsingException(direction, "command direction (buying/selling)");

        return direction;
    }

    private double checkPrice(String price) throws CommandException, StockPriceException {
        double stockPrice;

        try {
            stockPrice = Double.parseDouble(price);
            if (stockPrice <= 0) throw new StockPriceException(stockPrice);
        }
        catch (NumberFormatException ex) {
            throw new CommandParsingException(price, "Number");
        }

        return stockPrice;
    }

    private RizpaCommand ProcessDeal(Stock stock, Collection<RizpaCommand> commands,
                             RizpaCommand originalCommand, RizpaCommand comparedCommand) {
        RecordBook book = stock.getRecords();
        RizpaCommand deal;
        int differenceInAmount = originalCommand.getAmount() - comparedCommand.getAmount();
        double dealPrice = originalCommand instanceof BuyingCommand ?
                Math.min(originalCommand.getPrice(), comparedCommand.getPrice()) :
                Math.max(originalCommand.getPrice(), comparedCommand.getPrice());

        if (differenceInAmount < 0) { // case all stocks from original command bought/sold
            originalCommand.setPrice(dealPrice);
            deal = originalCommand.clone();
            originalCommand.setAmount(0);
            comparedCommand.setAmount(Math.abs(differenceInAmount));
        }
        else { // case not all stocks from original command bought/sold
            comparedCommand.setTimestamp(originalCommand.getTimestamp());
            deal = comparedCommand.clone();
            originalCommand.setAmount(differenceInAmount);
            commands.remove(comparedCommand);
        }
        book.insertToCompletedCommands(deal);
        stock.setPrice(dealPrice);

        return deal;
    }

    private Collection<RizpaCommand> operateLMTCommand(RizpaCommand command) throws StockNotFoundException {
        Stock stock = stocks.getStock(command.getStockSymbol());
        List<RizpaCommand> oppositeDirectionCommands = command instanceof BuyingCommand  ?
                stock.getRecords().getSelling() : stock.getRecords().getBuying();
        Collection<RizpaCommand> completedDeals = new ArrayList<>();

        for (int i = 0; i < oppositeDirectionCommands.size(); i++) {
            RizpaCommand comparedCommand = oppositeDirectionCommands.get(i);

            if (command.getAmount() != 0 ) {
                if (RizpaCommand.MatchingCommands(command.getType(), command, comparedCommand)) {
                    RizpaCommand deal = ProcessDeal(stock, oppositeDirectionCommands, command, comparedCommand);
                    completedDeals.add(deal);
                    i--;
                }
            }
            else break;
        }

        if (command.getAmount() != 0) {
            boolean isBuyingCommand = command instanceof BuyingCommand;
            List<RizpaCommand> sameDirectionCommands =  isBuyingCommand ?
                    stock.getRecords().getBuying() : stock.getRecords().getSelling();

            // Modify to support all command types later
            if (command.getType().equals("MKT")) {
                try {
                    command.setType(RizpaCommand.convertStringToType("LMT"));
                    command.setPrice(stock.getPrice());
                }
                catch (CommandParsingException ignored) {
                }
            }
            stock.getRecords().insertToPendingCommands(sameDirectionCommands, command);
        }

        return completedDeals;
    }

}
