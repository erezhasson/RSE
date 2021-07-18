//package logicinterface;
//
//import datafiles.stock.Stock;
//import datafiles.stock.Stocks;
//import datafiles.user.User;
//import datafiles.user.Users;
//import exceptions.name.NameException;
//import exceptions.stock.StockException;
//import exceptions.symbol.SymbolException;
//import javafx.application.Platform;
//import javafx.concurrent.Task;
//
//import javax.xml.bind.JAXBException;
//import java.io.IOException;
//import java.util.function.Consumer;
//
//public class LoadFileTask extends Task<Boolean> {
//    private String fileName;
//    private Consumer<Long> totalStocksDelegate;
//    private Consumer<Long> totalUsersDelegate;
//    private final RizpaLoaderInterface loader;
//    private final int SLEEP_TIME = 100;
//
//    public LoadFileTask(RizpaLoaderInterface loader, String fileName, Consumer<Long> totalStocksDelegate,
//                        Consumer<Long> totalUsersDelegate) {
//        this.fileName = fileName;
//        this.loader = loader;
//        this.totalStocksDelegate = totalStocksDelegate;
//        this.totalUsersDelegate = totalUsersDelegate;
//    }
//
//    @Override
//    protected Boolean call() throws IOException, NameException, SymbolException,
//            StockException, JAXBException, ClassNotFoundException {
//        final long totalStocks;
//        final long totalUsers;
//
//        updateMessage("Importing file...");
//        updateProgress(0,1);
//        loader.loadFileToEngine(fileName);
//        //AppUtils.sleepForAWhile(SLEEP_TIME);
//        updateProgress(1,1);
//
//        updateMessage("Validating stocks & users...");
//        updateProgress(0,1);
//        Stocks stocks = loader.loadStocks();
//        Users users = loader.loadUsers(stocks);
//        loader.clearStocks();
//        loader.clearUsers();
//        //AppUtils.sleepForAWhile(SLEEP_TIME);
//        updateProgress(1,1);
//
//        updateMessage("Importing stocks to system...");
//        final int[] currentStockNumber = {0};
//        totalStocks = stocks.getAllStocks().size();
//        for (Stock stock : stocks.getAllStocks()) {
//            loader.addStockFromFile(stock);
//            currentStockNumber[0]++;
//            updateMessage("Importing stocks to system... " + currentStockNumber[0] + "/" + totalStocks);
//            updateProgress(currentStockNumber[0], totalStocks);
//            //AppUtils.sleepForAWhile(SLEEP_TIME);
//        }
//
//        // update total lines in UI
//        Platform.runLater(
//                () -> totalStocksDelegate.accept(totalStocks)
//        );
//
//        updateMessage("Importing users to system...");
//        final int[] currentUserNumber = {0};
//        totalUsers = users.getAllUsers().size();
//        for (User user : users.getAllUsers()) {
//            loader.addUserFromFile(user);
//            currentUserNumber[0]++;
//            updateMessage("Importing users to system... " + currentUserNumber[0] + "/" + totalUsers);
//            updateProgress(currentUserNumber[0], totalUsers);
//           // AppUtils.sleepForAWhile(SLEEP_TIME);
//        }
//
//        // update total lines in UI
//        Platform.runLater(
//                () -> totalStocksDelegate.accept(totalUsers)
//        );
//        updateMessage("Done...");
//       // AppUtils.sleepForAWhile(SLEEP_TIME);
//
//        return Boolean.TRUE;
//    }
//}
