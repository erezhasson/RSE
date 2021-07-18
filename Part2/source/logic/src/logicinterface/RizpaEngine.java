package logicinterface;

import datafiles.commands.*;
import datafiles.dto.*;
import datafiles.generated.*;
import datafiles.stock.*;
import datafiles.user.User;
import datafiles.user.Users;
import exceptions.name.NameException;
import exceptions.stock.*;
import exceptions.symbol.*;
import exceptions.command.*;
import exceptions.user.UserNotFoundException;
import javafx.concurrent.Task;
import ui.task.TaskWindowController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;
import java.util.function.Consumer;


public class RizpaEngine implements RizpaInterface, RizpaLoaderInterface {
    private final Stocks stocks;
    private final Users users;
    private String fileURL;
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "datafiles.generated";

    public RizpaEngine() {
        stocks = new Stocks();
        users = new Users();
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
    public Collection<UserDto> getAllUsers() throws StockNotFoundException {
        Collection<UserDto> usersDto = new ArrayList<>();

        for (User user: users.getAllUsers()) {
            usersDto.add(new UserDto(user.getName(), user.getStockStructure()));
        }

        return usersDto;
    }

    @Override
    public StockDto getStock(String symbol) throws StockNotFoundException {
        Stock stock = stocks.getStock(symbol);

        return new StockDto(stock.getCompanyName(), stock.getSymbol(), stock.getPrice(), stock.getRecords());
    }

    @Override
    public UserDto getUser(String userName) throws UserNotFoundException, StockNotFoundException {
        User user = users.getUser(userName);

        return new UserDto(userName, user.getStockStructure());
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
    public void clearUsers() {
        users.clear();
    }

    public RseStocks deserializeStocksFrom(RizpaStockExchangeDescriptor descriptor) {
        return descriptor.getRseStocks();
    }

    public RseUsers deserializeUsersFrom(RizpaStockExchangeDescriptor descriptor) {
        return descriptor.getRseUsers();
    }

    @Override
    public void loadFileToEngine(String url) {
        fileURL = url;
    }

    @Override
    public Stocks loadStocks()
            throws IOException, ClassNotFoundException, SymbolException, StockException, JAXBException {
        int lastIndexOf = fileURL.lastIndexOf(".") + 1;
        Stocks stocks;

        if (fileURL.substring(lastIndexOf).equals("xml")) {
            stocks = loadStockFromXMLFile(new FileInputStream(fileURL));
        }
        else {
            stocks = loadStocksFromSaveFile(new FileInputStream(fileURL));
        }

        return stocks;
    }

    @Override
    public Users loadUsers(Stocks loadedStocks)
            throws IOException, StockException, JAXBException, NameException, ClassNotFoundException {
        int lastIndexOf = fileURL.lastIndexOf(".") + 1;
        Users users;

        if (fileURL.substring(lastIndexOf).equals("xml")) {
            users = loadUsersFromXMLFile(new FileInputStream(fileURL), loadedStocks);
        }
        else {
            users = loadUsersFromSaveFile(new FileInputStream(fileURL));
        }

        return users;
    }

    @Override
    public void addStockFromFile(Stock stock) throws StockException, SymbolException {
        addStock(stock.getSymbol(), stock);
    }

    @Override
    public void addUserFromFile(User user) throws NameException {
        addUser(user.getName(), user);
    }

    private RizpaStockExchangeDescriptor createDescriptor(InputStream in)
            throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (RizpaStockExchangeDescriptor) u.unmarshal(in);
    }

    public Users loadUsersFromXMLFile(InputStream is, Stocks loadedStocks) throws NameException, StockException, JAXBException {
        Users newUsers = new Users();
        RseUsers generatedUsers = deserializeUsersFrom(createDescriptor(is));

        for (RseUser user : generatedUsers.getRseUser()) {
            User newUser = new User(user.getName());

            for (RseItem holding : user.getRseHoldings().getRseItem()) {
                newUser.addStockToUser(loadedStocks.getStock(holding.getSymbol()), holding.getQuantity());
            }

            newUsers.addUser(newUser.getName(), newUser);
        }

        return newUsers;
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

    @Override
    public double calExchangeCycle(Collection<RizpaCommandDto> commands) {
        double cycle = 0;

        for (RizpaCommandDto command: commands) {
            cycle += command.getPrice() * command.getAmount();
        }

        return cycle;
    }


    private void validateCommand(String type, String stockSymbol, String amount, String direction, String price, User issuedCommandUser)
            throws CommandException, StockException {
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
            throws CommandException, StockException, UserNotFoundException {
        User issuedCommandUser = users.getUser(issuedCommandUserName);
        String type = commandDetails.getType();
        String stockSymbol = commandDetails.getStockSymbol().toUpperCase();
        String amount = commandDetails.getAmount();
        String direction = commandDetails.getDirection();
        String price = type == null ? "" : (type.equals("LMT") ? commandDetails.getPrice() : "0");
        boolean isBuyingCommand = direction.equalsIgnoreCase("Buying");
        validateCommand(type, stockSymbol, amount, direction, price, issuedCommandUser);

        if (!isBuyingCommand) {
            issuedCommandUser.increaseStockSellingAmount(stocks.getStock(stockSymbol), Integer.parseInt(amount));
        }

        return isBuyingCommand ?
                new BuyingCommand(stockSymbol, RizpaCommand.convertStringToType(type), Double.parseDouble(price),
                        Integer.parseInt(amount), issuedCommandUser) :
                new SellingCommand(stockSymbol, RizpaCommand.convertStringToType(type), Double.parseDouble(price),
                        Integer.parseInt(amount), issuedCommandUser);
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
        List<User> usersList = new ArrayList<>(users.getAllUsers());

        o.writeObject(stocksList);
        o.writeObject(usersList);
        o.close();
        out.close();
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

    public Users loadUsersFromSaveFile(FileInputStream in)
            throws ClassNotFoundException, IOException, NameException {
        ObjectInputStream oi = new ObjectInputStream(in);
        ArrayList<?> fileStocks = (ArrayList<?>) oi.readObject();
        ArrayList<?> fileUsers = (ArrayList<?>) oi.readObject();
        Users newUsers = new Users();

        for (Object obj : fileUsers) {
            User user = (User)obj;
            User newUser = new User(user.getName());

            for (Stock stock : user.getStocks()) {
                newUser.addStockToUser(stock, user.getAmountOfStock(stock.getSymbol()));
            }
            newUsers.addUser(newUser.getName(), newUser);
        }
        oi.close();
        in.close();

        return newUsers;
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
            originalCommand.setStatus(true);
            deal = originalCommand.clone();
            originalCommand.setAmount(0);
            comparedCommand.setAmount(Math.abs(differenceInAmount));
        }
        else { // case not all stocks from original command bought/sold
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

        deal.setBuyer(buyer);
        deal.setSeller(seller);
        decreaseSellerStockAmount(seller, stock, deal.getAmount());
        increaseBuyerStockAmount(buyer, stock, deal.getAmount());
    }

    private void increaseBuyerStockAmount(User buyer, Stock stock, int amount) {
        buyer.increaseStockAmount(stock, amount);
    }


    private void decreaseSellerStockAmount(User seller, Stock stock, int amount) {
        seller.decreaseStockAmount(stock, amount);
        seller.decreaseStockSellingAmount(stock, amount);
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

    @Override
    public void collectDataFromFile(TaskWindowController controller, String fileName, Consumer<Long> totalStockDelegate,
                                    Consumer<Long> totalUsersDelegate, Runnable onFinish) {
        Consumer<Long> totalStocksConsumer = totalStockDelegate::accept;
        Consumer<Long> totalUsersConsumer = totalUsersDelegate::accept;

        LoadFileTask currentRunningTask = new LoadFileTask(this, fileName, totalStockDelegate, totalUsersDelegate);

        controller.bindTaskToUIComponents(currentRunningTask, onFinish);

        new Thread(currentRunningTask).start();
    }
}
