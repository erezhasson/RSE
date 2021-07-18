package logicinterface;

import datafiles.commands.BuyingCommand;
import datafiles.commands.CommandDetails;
import datafiles.commands.RizpaCommand;
import datafiles.commands.SellingCommand;
import datafiles.dto.RecordBookDto;
import datafiles.dto.RizpaCommandDto;
import datafiles.dto.StockDto;
import datafiles.dto.UserDto;
import datafiles.generated.*;
import datafiles.stock.RecordBook;
import datafiles.stock.Stock;
import datafiles.stock.Stocks;
import datafiles.user.Transaction;
import datafiles.user.User;
import datafiles.user.Users;
import exceptions.command.*;
import exceptions.name.EmptyNameException;
import exceptions.name.NameException;
import exceptions.name.NameExistsException;
import exceptions.stock.StockAmountException;
import exceptions.stock.StockException;
import exceptions.stock.StockNotFoundException;
import exceptions.stock.StockPriceException;
import exceptions.symbol.SymbolEmptyException;
import exceptions.symbol.SymbolException;
import exceptions.user.UserNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;

public class RizpaEngine implements RizpaInterface {
    private final Stocks stocks;
    private final Users users, onlineUsers;
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "datafiles.generated";

    public RizpaEngine() {
        stocks = new Stocks();
        users = new Users();
        onlineUsers = new Users();

        try {
            User admin = new User("Admin"), erez = new User("erez");
            users.addUser("Admin", admin);
            users.addUser("erez", erez);
        } catch (EmptyNameException | NameExistsException ignored) {
        }
    }

    @Override
    public int getStockAmount() {
        return stocks.getStockAmount();
    }

    @Override
    public Collection<StockDto> getAllStocks() {
        Collection<StockDto> stocksDto = new ArrayList<>();

        for (Stock stock: stocks.getAllStocks()) {
            stocksDto.add(new StockDto(stock));
        }

        return stocksDto;
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        Collection<UserDto> usersDto = new ArrayList<>();

        for (User user: users.getAllUsers()) {
            usersDto.add(new UserDto(user));
        }

        return usersDto;
    }

    @Override
    public Collection<UserDto> getOnlineUsers() {
        Collection<UserDto> usersDto = new ArrayList<>();

        for (User user: onlineUsers.getAllUsers()) {
            usersDto.add(new UserDto(user));
        }

        return usersDto;
    }

    @Override
    public StockDto getStock(String symbol) throws StockNotFoundException {
        Stock stock = stocks.getStock(symbol != null ? symbol.toUpperCase() : null);

        return new StockDto(stock);
    }

    @Override
    public UserDto getUser(String userName) throws UserNotFoundException, StockNotFoundException {
        User user = users.getUser(userName);

        return new UserDto(user);
    }

    @Override
    public void addStock(String symbol, Stock newStock) throws SymbolException, StockException {
        stocks.addStock(symbol, newStock);
    }

    @Override
    public void addUser(String name, User newUser) throws NameException {
        users.addUser(name, newUser);
    }

    @Override
    public void addOnlineUser(String name, User newUser) throws NameException {
        onlineUsers.addUser(name, newUser);
    }

    @Override
    public void removeUser(String name) {
        users.removeUser(name);
    }

    @Override
    public void makeUserOffline(String username) {
        onlineUsers.removeUser(username);
    }

    @Override
    public boolean isUserExists(String userName) {
        return users.isUserExists(userName);
    }

    @Override
    public void buildUserStockFromFile(String username, File file)
            throws JAXBException, SymbolException, StockException, IOException {
        User user = users.getUser(username);
        Stocks loadedStocks = loadStocks(file);
        HashMap<String, Integer> loadedHoldings = loadHoldings(file, loadedStocks);

        for (Stock stock : loadedStocks.getAllStocks()) {
            String stockSymbol = stock.getSymbol();

            if (!stocks.containsSymbol(stockSymbol)) {
                stocks.addStock(stockSymbol, new Stock(stock.getCompanyName(), stockSymbol, stock.getPrice()));
            }
        }

        user.clearHoldings();
        for (Map.Entry<String, Integer> holding : loadedHoldings.entrySet()) {
            user.addStockToUser(stocks.getStock(holding.getKey()), holding.getValue());
        }
        user.addTransaction(new Transaction(0, user.getBalance(),
                Transaction.TransactionType.FILE_UPLOAD));
    }

    public RseStocks deserializeStocksFrom(RizpaStockExchangeDescriptor descriptor) {
        return descriptor.getRseStocks();
    }

    private RseHoldings deserializeHoldingsFrom(RizpaStockExchangeDescriptor descriptor) {
        return descriptor.getRseHoldings();
    }

    private HashMap<String, Integer> loadHoldings(File file, Stocks stocks)
            throws FileNotFoundException, JAXBException, StockNotFoundException {
        return loadHoldingFromXMLFile(new FileInputStream(file), stocks);
    }

    public Stocks loadStocks(File file)
            throws IOException, SymbolException, StockException, JAXBException {
        return loadStockFromXMLFile(new FileInputStream(file));
    }

    @Override
    public void chargeUserBalance(String username, double chargeAmount) {
        User user = users.getUser(username);
        double userBalance = user.getBalance(), userBalanceAfterCharge = userBalance + chargeAmount;

        user.setBalance(userBalanceAfterCharge);
        user.addTransaction(new Transaction(chargeAmount, userBalanceAfterCharge, Transaction.TransactionType.BALANCE_CHARGE));
    }

    private RizpaStockExchangeDescriptor createDescriptor(InputStream in)
            throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (RizpaStockExchangeDescriptor) u.unmarshal(in);
    }

    private HashMap<String, Integer> loadHoldingFromXMLFile(FileInputStream is, Stocks stocks)
            throws JAXBException, StockNotFoundException {
        HashMap<String, Integer> holdings = new HashMap<>();
        RseHoldings generatedHoldings = deserializeHoldingsFrom(createDescriptor(is));

        for (RseItem holding : generatedHoldings.getRseItem()) {
            String symbol = holding.getSymbol();

            if (stocks.containsSymbol(symbol)) {
                holdings.put(symbol, holding.getQuantity());
            }
            else {
                throw new StockNotFoundException(symbol);
            }
        }

        return holdings;
    }

    public Stocks loadStockFromXMLFile(InputStream is)
            throws SymbolException, StockException, JAXBException {
        Stocks newStocks = new Stocks();
        RseStocks generatedStocks = deserializeStocksFrom(createDescriptor(is));

        for (RseStock stock : generatedStocks.getRseStock()) {
            Stock newStock = new Stock(stock.getRseCompanyName(), stock.getRseSymbol().toUpperCase(), stock.getRsePrice());

            newStocks.addStock(stock.getRseSymbol(), newStock);
        }

        return newStocks;
    }


    private void validateCommand(String type, String stockSymbol, String amount, String direction, String price, User issuedCommandUser)
            throws CommandException, StockException, SymbolEmptyException {
            checkSymbol(stockSymbol);
            checkCommandType(type);
            checkDirection(direction);
            checkStockAmount(amount);
            checkUser(issuedCommandUser, stockSymbol, Integer.parseInt(amount), direction);

            if (!type.equalsIgnoreCase("MKT")) {
                checkPrice(price);
            }
    }


    @Override
    public RizpaCommand createNewCommand(CommandDetails commandDetails, String issuedCommandUserName)
            throws CommandException, StockException, SymbolEmptyException {
        User issuedCommandUser = users.getUser(issuedCommandUserName);
        String type = commandDetails.getType();
        String stockSymbol = commandDetails.getStockSymbol().toUpperCase();
        String amount = commandDetails.getAmount();
        String direction = commandDetails.getDirection();
        String price = type == null ? "" : (type.equals("MKT") ? "0" : commandDetails.getPrice());
        boolean isBuyingCommand = direction.equalsIgnoreCase("Buying");

        validateCommand(type, stockSymbol, amount, direction, price, issuedCommandUser);
        if (!isBuyingCommand) {
            issuedCommandUser.increaseStockSellingAmount(stocks.getStock(stockSymbol), Integer.parseInt(amount));
        }

        RizpaCommand newCommand =  isBuyingCommand ?
                new BuyingCommand(stockSymbol, RizpaCommand.convertStringToType(type), Double.parseDouble(price),
                        Integer.parseInt(amount), issuedCommandUser) :
                new SellingCommand(stockSymbol, RizpaCommand.convertStringToType(type), Double.parseDouble(price),
                        Integer.parseInt(amount), issuedCommandUser);

        issuedCommandUser.addCommandToAppliedRecords(newCommand);
        return newCommand;
    }


    @Override
    public Collection<RizpaCommandDto> operateCommand(RizpaCommand command) {
        Collection<RizpaCommand> completedDeals = new ArrayList<>();

        try {
            switch (command.getType()) {
                case "LMT":
                case "IOC":
                case "MKT":
                    completedDeals = operateLMTCommand(command);
                    break;
                case "FOK":
                    completedDeals = operateFOKCommand(command);
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
    public void issueNewStock(String username, String stockSymbol, int stockAmount, String companyName, double companyWorth)
            throws StockException, SymbolException {
        User stockOwner = users.getUser(username);
        Stock issuedStock = new Stock(companyName, stockSymbol, (int)(companyWorth / stockAmount));

        stocks.addStock(stockSymbol, issuedStock);
        stockOwner.addStockToUser(issuedStock, stockAmount);
    }

    @Override
    public void saveSystemInfoToStream(FileOutputStream out) throws IOException {
        ObjectOutputStream o = new ObjectOutputStream(out);
        List<Stock> stocksList = new ArrayList<>(stocks.getAllStocks());
        List<User> usersList = new ArrayList<>(users.getAllUsers());

        o.writeObject(stocksList);
        o.writeObject(usersList);
        o.close();
        out.close();
    }

    @Override
    public Collection<RizpaCommandDto> getAllPendingCommands() {
        List<RizpaCommandDto> totalCommands = new ArrayList<>();

        for (StockDto stock : getAllStocks()) {
            RecordBookDto book = stock.getRecords();

            totalCommands.addAll(book.getBuying());
            totalCommands.addAll(book.getSelling());
        }

        return totalCommands;
    }

    @Override
    public Collection<RizpaCommandDto> getAllCompletedCommands() {
        List<RizpaCommandDto> totalCommands = new ArrayList<>();

        for (StockDto stock : getAllStocks()) {
            RecordBookDto book = stock.getRecords();

            totalCommands.addAll(book.getCompleted());
        }

        return totalCommands;
    }

    public Stocks loadStocksFromSaveFile(FileInputStream in)
            throws ClassNotFoundException, IOException, StockException, SymbolException {
        ObjectInputStream oi = new ObjectInputStream(in);
        ArrayList<?> fileStocks = (ArrayList<?>) oi.readObject();
        Stocks newStocks = new Stocks();

        for (Object obj : fileStocks) {
            Stock stock = (Stock)obj;
            RecordBook recordBook = stock.getRecords();
            Stock newStock = new Stock(stock.getCompanyName(), stock.getSymbol(), stock.getPrice());

            newStock.setRecords(new RecordBook(recordBook.getBuying(), recordBook.getSelling(), recordBook.getCompleted()));
            newStocks.addStock(stock.getSymbol(), newStock);
        }
        oi.close();
        in.close();

        return newStocks;
    }

    private void checkCommandType(String type) throws CommandException {
        if (type == null|| type.equals("")) throw new CommandArgEmptyException("Commands' type");
        if (!RizpaCommand.isValidType(type)) throw new CommandTypeNotExistsException(type);
    }

    private void checkStockAmount(String amount) throws CommandException, StockException {
        int stockAmount;

        try {
            if (amount == null || amount.equals("")) throw new CommandArgEmptyException("Stock's amount");
            stockAmount = Integer.parseInt(amount);

            if (stockAmount <= 0) throw new StockAmountException(stockAmount);

        }
        catch (NumberFormatException ex) {
            throw new CommandParsingException(amount, "number");
        }
    }

    private void checkDirection(String direction) throws CommandException  {
        if (direction == null)
            throw new CommandArgEmptyException("Command Direction");
    }

    private void checkPrice(String price) throws CommandException, StockPriceException {
        double stockPrice;

        try {
            if (price == null || price.equals("")) throw new CommandArgEmptyException("Stock's price");
            stockPrice = Double.parseDouble(price);
            if (stockPrice <= 0) throw new StockPriceException(stockPrice);
        }
        catch (NumberFormatException ex) {
            throw new CommandParsingException(price, "number");
        }
    }


    private void checkUser(User user, String stockSymbol, int amount, String direction)
            throws UserDoesntHasStockException, SellingAmountIncompatible {
        if (direction.equalsIgnoreCase("selling")) {
            if (!user.hasStock(stockSymbol)) {
                throw new UserDoesntHasStockException(user.getName());
            }

            int userStockAmount = user.getAmountOfStock(stockSymbol),
                    userStockSellingAmount = user.getAmountOfSellingStock(stockSymbol);
            if (amount > userStockAmount - userStockSellingAmount) {
                throw new SellingAmountIncompatible(amount, userStockAmount, user.getName());
            }
        }
    }

    private void checkSymbol(String stockSymbol) throws SymbolEmptyException, StockNotFoundException {
        if (stockSymbol == null || stockSymbol.isEmpty()) {
            throw new SymbolEmptyException();
        }
        stocks.getStock(stockSymbol);
    }

    private RizpaCommand ProcessDeal(Stock stock, Collection<RizpaCommand> commands,
                             RizpaCommand originalCommand, RizpaCommand comparedCommand) {
        RecordBook book = stock.getRecords();
        RizpaCommand deal;
        int differenceInAmount = originalCommand.getAmount() - comparedCommand.getAmount();
        double dealPrice;
        User issuedCommandUser, otherCommandUser;

        if (originalCommand instanceof BuyingCommand) {
            dealPrice = Math.min(originalCommand.getPrice(), comparedCommand.getPrice());
            issuedCommandUser = originalCommand.getBuyer();
            otherCommandUser = comparedCommand.getSeller();
        }

        else {
            dealPrice = Math.max(originalCommand.getPrice(), comparedCommand.getPrice());
            issuedCommandUser = originalCommand.getSeller();
            otherCommandUser = comparedCommand.getBuyer();
        }

        if (differenceInAmount < 0) { // case all stocks from original command bought/sold
            updateUserRecords(issuedCommandUser, originalCommand);
            originalCommand.setPrice(dealPrice);
            originalCommand.setStatus(true);
            deal = originalCommand.clone();
            originalCommand.setAmount(0);
            comparedCommand.setAmount(Math.abs(differenceInAmount));
        }
        else { // case not all stocks from original command bought/sold
            if (differenceInAmount == 0) {
                updateUserRecords(issuedCommandUser, originalCommand);
            }

            updateUserRecords(otherCommandUser, comparedCommand);
            comparedCommand.setTimestamp(originalCommand.getTimestamp());
            comparedCommand.setStatus(true);
            deal = comparedCommand.clone();
            originalCommand.setAmount(differenceInAmount);
            commands.remove(comparedCommand);
        }

        updateCommandsUsers(stock, originalCommand, comparedCommand, deal);
        deal.setStatus(true);
        book.insertToCompletedCommands(deal);
        stock.setPrice(dealPrice);

        return deal;
    }

    private void updateUserRecords(User user, RizpaCommand command) {
        user.addCommandToCompletedRecords(command);
    }

    private void updateCommandsUsers(Stock stock, RizpaCommand originalCommand, RizpaCommand comparedCommand, RizpaCommand deal) {
        User seller, buyer;

        if (originalCommand instanceof BuyingCommand) {
            seller = comparedCommand.getSeller();
            buyer = originalCommand.getBuyer();
        }

        else {
            seller = originalCommand.getSeller();
            buyer = comparedCommand.getBuyer();
        }

        updateBalances(buyer, seller, deal);
        deal.setBuyer(buyer);
        deal.setSeller(seller);
        decreaseSellerStockAmount(seller, stock, deal.getAmount());
        increaseBuyerStockAmount(buyer, stock, deal.getAmount());
    }

    private void updateBalances(User buyer, User seller, RizpaCommand deal) {
        double dealPrice = deal.getPrice() * deal.getAmount();
        double sellerBalanceAfterDeal = seller.getBalance() + dealPrice;
        double buyerBalanceAfterDeal = buyer.getBalance() - dealPrice;

        seller.setBalance(sellerBalanceAfterDeal);
        buyer.setBalance(buyerBalanceAfterDeal);
        seller.addTransaction(new Transaction(dealPrice, sellerBalanceAfterDeal, Transaction.TransactionType.STOCK_SELL));
        buyer.addTransaction(new Transaction(-dealPrice, buyerBalanceAfterDeal, Transaction.TransactionType.STOCK_PURCHASE));
    }

    private void increaseBuyerStockAmount(User buyer, Stock stock, int amount) {
        buyer.increaseStockAmount(stock, amount);
    }


    private void decreaseSellerStockAmount(User seller, Stock stock, int amount) {
        seller.decreaseStockAmount(stock, amount);
    }

    private Collection<RizpaCommand> operateLMTCommand(RizpaCommand command) throws StockNotFoundException {
        Stock stock = stocks.getStock(command.getStockSymbol());
        boolean isBuyingCommand = command instanceof BuyingCommand;
        List<RizpaCommand> oppositeDirectionCommands = isBuyingCommand ?
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

        if (!command.getType().equals("IOC") && command.getAmount() != 0) {
            List<RizpaCommand> sameDirectionCommands = isBuyingCommand ?
                    stock.getRecords().getBuying() : stock.getRecords().getSelling();

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
        else {
            if (isBuyingCommand)
                command.getSeller().decreaseStockSellingAmount(stock, command.getAmount());
        }

        return completedDeals;
    }

    private Collection<RizpaCommand> operateFOKCommand(RizpaCommand command) throws StockNotFoundException {
        Stock stock = stocks.getStock(command.getStockSymbol());
        boolean isBuyingCommand = command instanceof BuyingCommand;
        List<RizpaCommand> oppositeDirectionCommands = isBuyingCommand ?
                stock.getRecords().getSelling() : stock.getRecords().getBuying();
        Collection<RizpaCommand> potentialDeals = new ArrayList<>();
        Collection<RizpaCommand> completedDeals = new ArrayList<>();
        int commandAmount = command.getAmount();

        for (RizpaCommand comparedCommand : oppositeDirectionCommands) {
            if (commandAmount != 0) {
                if (RizpaCommand.MatchingCommands(command.getType(), command, comparedCommand)) {
                    potentialDeals.add(comparedCommand);
                    commandAmount -= comparedCommand.getAmount();
                }
            } else break;
        }

        if (commandAmount == 0) { // case FOK command completed
            for (RizpaCommand potentialCommand : potentialDeals) {
                RizpaCommand deal = ProcessDeal(stock, oppositeDirectionCommands, command, potentialCommand);
                completedDeals.add(deal);
            }
        }
        else {
            User issuedCommandUser = isBuyingCommand ? command.getBuyer() : command.getSeller();

            issuedCommandUser.decreaseStockSellingAmount(stock, command.getAmount());
        }

        return completedDeals;
    }

    @Override
    public void addAlertToUser(String username, String alert) {
        User user = users.getUser(username);

        user.addAlert(alert);
    }
}
