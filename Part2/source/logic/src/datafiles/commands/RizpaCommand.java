package datafiles.commands;
import datafiles.user.User;
import exceptions.command.CommandParsingException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public abstract class RizpaCommand implements Comparable<RizpaCommand>, Cloneable, Serializable {
    protected final String stockSymbol;
    protected final String displayType;
    protected CommandType type;
    protected double price;
    protected int amount;
    protected boolean status;
    protected String timestamp;
    protected User buyer;
    protected User seller;

    public RizpaCommand(String stockSymbol, CommandType type, double price, int amount, User buyer, User seller) {
        this.stockSymbol = stockSymbol;
        this.type = type;
        this.displayType = type.toString();
        this.amount = amount;
        this.price = price;
        this.timestamp = DateTimeFormatter.ofPattern("HH:mm:ss:SSS").format(LocalDateTime.now());
        this.buyer = buyer;
        this.seller = seller;
        this.status = false;
    }

    protected enum CommandType {
        LMT{
            public boolean MatchingByCommandType(RizpaCommand originalCommand, RizpaCommand comparedCommand){
                boolean matching;

                if (originalCommand instanceof BuyingCommand) {
                    matching = originalCommand.price >= comparedCommand.price;
                }
                else {
                    matching = originalCommand.price <= comparedCommand.price;
                }

                return matching;
            }
        },
        MKT {
            public boolean MatchingByCommandType(RizpaCommand originalCommand, RizpaCommand comparedCommand){
                originalCommand.price = comparedCommand.price;
                return true;
            }
        },
        FOK {
            public boolean MatchingByCommandType(RizpaCommand originalCommand, RizpaCommand comparedCommand){
                return true;
            }
        },
        IOC {
            public boolean MatchingByCommandType(RizpaCommand originalCommand, RizpaCommand comparedCommand){
                return true;
            }
        };

        public abstract boolean MatchingByCommandType(RizpaCommand originalCommand, RizpaCommand comparedCommand);

        private static boolean isValidType(String type) {
            boolean valid = false;

            for (CommandType command: CommandType.values()) {
                if (command.toString().equals(type)) {
                    valid = true;
                    break;
                }
            }

            return valid;
        }

        private static CommandType convertStringToType(String type) {
                CommandType commandType = null;

            for (CommandType command: CommandType.values()) {
                if (command.toString().equals(type)) {
                    commandType = command;
                    break;
                }
            }

            return commandType;
        }

        @Override
        public String toString() {
            switch (this) {
                case LMT: return "LMT";
                case MKT: return "MKT";
                case FOK: return "FOK";
                case IOC: return "IOC";
                default: return "";
            }
        }
    }

    public static boolean isValidType(String type) {
        return CommandType.isValidType(type);
    }

    public static boolean MatchingCommands(String typeName, RizpaCommand originalCommand, RizpaCommand comparedCommand) {
        boolean matching = false;

        try {
            CommandType type = convertStringToType(typeName);
            matching = type.MatchingByCommandType(originalCommand, comparedCommand);
        }
        catch (CommandParsingException ignored){
        }

        return matching;
    }

    public static CommandType convertStringToType(String type) throws CommandParsingException {
        if (!CommandType.isValidType(type)) throw new CommandParsingException(type, "command type");

        return CommandType.convertStringToType(type);
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getDisplayType() { return displayType;}

    public void setType(CommandType type) {
        this.type = type;
    }

    public String getType() {
        return type.toString();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() { return price; }

    public User getBuyer() {
        return buyer;
    }

    public User getSeller() {
        return seller;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    @Override
    public int compareTo(RizpaCommand comparedCommand) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");

        try {
            Date d1 = sdf.parse(this.timestamp);
            Date d2 = sdf.parse(comparedCommand.timestamp);

            return d1.compareTo(d2);
        } catch (ParseException ignored) {
        }
        return -1;
    }

    public abstract RizpaCommand clone();
}
