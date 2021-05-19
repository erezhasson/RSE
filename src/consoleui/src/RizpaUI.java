package consoleui.src;

import datafiles.dto.*;
import exceptions.file.*;
import exceptions.stock.*;
import datafiles.commands.*;
import exceptions.symbol.*;
import logicinterface.*;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;

public class RizpaUI {
    private static final RizpaInterface engine;
    private static final Scanner scanner  = new Scanner(System.in);

    static {
        engine = new RizpaEngine();
    }

    public static void main(String[] args) {
        showMenu();
    }
    private enum MenuOption {
        ReadSystemInfo(1), ShowAllStocks(2), ShowStock(3),
        OperateNewCommand(4), ShowPendingCommands(5), SaveSystemInfo(6),
        ReadSystemInfoFromSaveFile(7), Exit(8);
        private final int index;

        MenuOption(int optionIndex) {
            this.index = optionIndex;
        }

        void printOption() {
            System.out.print(this.index + ". ");

            switch (this) {
                case ReadSystemInfo:
                    System.out.println("Read System Info (From XML File).");
                    break;
                case ShowAllStocks:
                    System.out.println("Show All Stocks.");
                    break;
                case ShowStock:
                    System.out.println("Show Stock Info.");
                    break;
                case OperateNewCommand:
                    System.out.println("Operate Rizpa Command.");
                    break;
                case ShowPendingCommands:
                    System.out.println("Show Pending Rizpa Commands.");
                    break;
                case SaveSystemInfo:
                    System.out.println("Save System Info (To Save File).");
                    break;
                case ReadSystemInfoFromSaveFile:
                    System.out.println("Read System Info (From Save File).");
                    break;
                case Exit:
                    System.out.println("Exit.");
                    break;
            }
        }

        public void operateOption() {
            switch (this) {
                case ReadSystemInfo:
                    readSystemInfo();
                    break;
                case ShowAllStocks:
                    showAllStocks();
                    break;
                case ShowStock:
                    showStockInfo(getStock());
                    break;
                case OperateNewCommand:
                    operateCommand(getCommand());
                    break;
                case ShowPendingCommands:
                    showPendingCommands();
                    break;
                case SaveSystemInfo:
                    saveSystemInfo();
                    break;
                case ReadSystemInfoFromSaveFile:
                    readSystemInfoFromSaveFile();
                    break;
                case Exit:
                    System.out.println("Thank you for using RSE, have a good day.");
                    break;
            }
        }
    }

    private static File getSaveFile() {
        boolean validPath = false;
        File file = null;

        scanner.nextLine();
        do {
            try {
                file = new File(getFilePath("txt"));
                validPath = true;
            }
            catch (InvalidFileTypeException ex) {
                System.out.println("ERROR: Problem occurred while loading file.");
                System.out.println("Error details: " + ex.getMessage());
            }
        } while (!validPath);

        return file;
    }

    private static String getFilePath(String fileType) throws InvalidFileTypeException{
        System.out.println("Format: [Folder1/SubFolder1/.../filename." + fileType +"]");
        String path = scanner.nextLine();
        String fileExtension = path.substring(path.lastIndexOf(".") + 1);


        if (!fileExtension.equals(fileType)) throw new InvalidFileTypeException(fileExtension, fileType);
        return path;
    }

    private static void readSystemInfoFromSaveFile() {
        try {
            System.out.println("Please enter TEXT file path (file must not be empty) : ");
            File file = getSaveFile();

            if (file.length() == 0) throw new EmptyFileException();
            engine.readSystemInfoFromSaveFile(new FileInputStream(file));
            System.out.println("Save file loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Try again. ERROR: Invalid path entered.");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (StockException ex) {
            System.out.println("ERROR: Problem occurred while loading stock.");
            System.out.println("Error details: " + ex.getMessage());
        } catch (SymbolException ex) {
            System.out.println("ERROR: Problem occurred while loading symbol.");
            System.out.println("Error details: " + ex.getMessage());
        } catch (EmptyFileException e) {
            System.out.println("ERROR: file must not be empty.");
        } catch (Exception e) {
            System.out.println("ERROR: Problem occurred while loading file.");
        }
    }

    private static void saveSystemInfo() {
        if (engine.getStockAmount() != 0) {
            try {
                System.out.println("Please enter TEXT file path (file doesn't need to be exist but must be empty): ");
                File file = getSaveFile();

                if (file.length() != 0) throw new FileNotEmptyException();
                engine.saveSystemInfoToStream(new FileOutputStream(file));
                System.out.println("Save file created successfully!");
            } catch (IOException ex) {
                System.out.println("Error initializing stream");
            } catch (FileNotEmptyException ex) {
                System.out.println("ERROR: Please enter EMPTY text file path.");
            }
            catch (Exception ex) {
                System.out.println("ERROR: Problem occurred while saving file.");
            }
        }

        else {
            System.out.println("No stocks to save...");
        }
    }

    private static String[] getCommandDetails() {
        System.out.println("Please enter command details: ");
        System.out.println("Format: [[TYPE] [SYMBOL] [AMOUNT] [SELLING/BUYING] [PRICE]]");

        return scanner.nextLine().split("\\s+");
    }

    private static RizpaCommand getCommand() {
        RizpaCommand command = null;
        boolean validInput = false;

        scanner.nextLine();
        do {
            try {
                String[] details = getCommandDetails();
                command = engine.createNewCommand(details);
                validInput = true;
            }
            catch (IndexOutOfBoundsException ex) {
                System.out.println("Try again. ERROR: missing arguments.");
            }
            catch (Exception ex) {
                System.out.println("Try again. ERROR: " + ex.getMessage());
            }
        } while (!validInput);

        return command;
    }

    private static void operateCommand(RizpaCommand command) {
        RizpaCommandDto commandDto = command instanceof BuyingCommand ?
                new BuyingCommandDto(command.getStockSymbol(), command.getType(), command.getPrice(),
                        command.getAmount(), command.getTimestamp()) :
                new SellingCommandDto(command.getStockSymbol(), command.getType(), command.getPrice(),
                        command.getAmount(), command.getTimestamp());
        Collection<RizpaCommandDto> completedDeals = engine.operateCommand(command);

        if (completedDeals.size() != 0) {
            System.out.println(commandDto.toString() + " has been executed successfully.");
            System.out.println("The following deals have been completed after executing command: ");
            displayCommands(completedDeals);
        }
        else {
            System.out.println(commandDto.toString() + " has been not executed and have been recorded in stock's pending commands.");
        }
    }

    private static InputStream getXMLPath(){
        boolean validPath = false;
        InputStream inputStream = null;

        scanner.nextLine();
        do {
            try {
                System.out.println("Please enter XML file path: ");
                inputStream = new FileInputStream(getFilePath("xml"));
                validPath = true;
            }
            catch (FileNotFoundException e) {
                System.out.println("Try again. ERROR: Invalid path entered.");
            }            catch (InvalidFileTypeException ex) {
                System.out.println("ERROR: Problem occurred while loading file.");
                System.out.println("Error details: " + ex.getMessage());
            }
        } while (!validPath);

        return inputStream;
    }

    private static void readSystemInfo() {
        try {
            InputStream sysInfo = getXMLPath();

            engine.buildStocksFromXMLFile(sysInfo);
            System.out.println("XML file loaded successfully!");
        }
        catch (JAXBException ex) {
            System.out.println("Try again. ERROR: Problem occurred while generating stocks from XML file.");
            System.out.println("JAXB error details: " + ex.getMessage());
        } catch (StockException ex) {
            System.out.println("ERROR: Problem occurred while loading stock.");
            System.out.println("Error details: " + ex.getMessage());
        }
        catch (SymbolException ex) {
            System.out.println("ERROR: Problem occurred while loading symbol.");
            System.out.println("Error details: " + ex.getMessage());
        }
    }

    private static void displayCommands(Collection<RizpaCommandDto> commands) {
        double cycle = 0;

        if (commands.size() != 0) {
            for (RizpaCommandDto command: commands) {
                System.out.println(command.toString());
                cycle += command.getCycle();
                System.out.println("===================================");
            }
            System.out.println("Total cycle: " + cycle);
        }
        else {
            System.out.println("No commands recorded.");
        }
        System.out.println("===================================");
    }

    private static void showPendingCommands() {
        Collection<StockDto> stocks = engine.getAllStocks();

        if (stocks.size() != 0) {
            System.out.println("===================================");
            System.out.println("SYSTEM INCLUDES THE FOLLOWING COMMANDS");
            System.out.println("===================================");
            for (StockDto stock : stocks) {
                RecordBookDto book = stock.getRecords();

                System.out.println(stock.toString());
                System.out.println("Pending commands: ");
                System.out.println("Buying commands: ");
                displayCommands(book.getBuying());
                System.out.println("Selling commands: ");
                displayCommands(book.getSelling());
                System.out.println("Completed commands: ");
                displayCommands(book.getCompleted());
            }
        }
        else {
            System.out.println("No commands to display.");
        }
    }

    private static void showAllStocks() {
        int stockAmount = engine.getStockAmount();

        if (stockAmount != 0) {
            System.out.println("===================================");
            System.out.println("SYSTEM INCLUDES THE FOLLOWING STOCKS");
            System.out.println("===================================");
            for (StockDto stock : engine.getAllStocks()) {
                Collection<RizpaCommandDto> completedCommands = stock.getRecords().getCompleted();

                System.out.println(stock.toString());
                if (completedCommands.size() != 0) {
                    System.out.println("Completed exchanges= " + completedCommands.size());
                    System.out.println("Total exchanges cycle= " + engine.calExchangeCycle(completedCommands));

                }
                else {
                    System.out.println("No completed exchanges found.");
                }
                System.out.println("===================================");
            }
        }
        else {
            System.out.println("No stocks to display.");
        }
    }

    private static void showStockInfo(StockDto stock) {
        if (stock != null) {
            List<RizpaCommandDto> completedCommands = stock.getRecords().getCompleted();

            System.out.println("===================================");
            System.out.println("DISPLAYING CHOSEN STOCK");
            System.out.println("===================================");
            System.out.println(stock.toString());
            if (completedCommands.size() != 0) {
                Collections.reverse(completedCommands);
                System.out.println("Recorded completed datafiles.commands: ");
                System.out.println("===================================");
                System.out.println("Completed exchanges= " + completedCommands.size());
                System.out.println("===================================");
                displayCommands(completedCommands);
            }
            else {
                System.out.println("No completed datafiles.commands recorded.");
            }
        }
    }

    private static StockDto getStock() {
        StockDto stock = null;
        boolean validInput = false;

        scanner.nextLine();
        if (engine.getStockAmount() == 0) {
            System.out.println("No stocks loaded in system.");
        }
        else {
            do {
                try {
                    System.out.println("Please enter stock's symbol: ");
                    System.out.println("Format: [SYMBOL]");
                    String stockSymbol = engine.checkStockSymbol(scanner.nextLine());
                    stock = engine.getStock(stockSymbol);
                    validInput = true;
                }
                catch (StockException ex) {
                    System.out.println("Try again. ERROR: " + ex.getMessage());
                }
                catch (SymbolException ex) {
                    System.out.println("Try again. ERROR: " + ex.getMessage());
                }
            } while (!validInput);
        }

        return stock;
    }

    private static void printMenu() {
        System.out.println("Please choose option:");
        for (MenuOption option : MenuOption.values()) {
            option.printOption();
        }
        System.out.println("Format: [OptionNumber]");
    }

    public static void showMenu() {
        MenuOption option = null;

        do {
            try {
                printMenu();
                option = MenuOption.values()[scanner.nextInt() - 1];
                option.operateOption();
            }
            catch (IndexOutOfBoundsException ex) {
                System.out.println("Try again. (ERROR: invalid option entered).");
            }
            catch (InputMismatchException ex) {
                System.out.println("Try again. (ERROR: an integer is required).");
                scanner.nextLine();
            }
        }
        while (option != MenuOption.Exit);
    }
}
