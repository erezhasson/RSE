package exceptions.command;

public class SellingAmountIncompatible extends CommandException {
    private final int sellingAmount, currentAmount;
    private final String userName;

    public SellingAmountIncompatible(int sellingAmount, int currentAmount, String userName) {
        this.sellingAmount = sellingAmount;
        this.currentAmount = currentAmount;
        this.userName = userName;
    }

    @Override
    public String getMessage() {
        return userName + " requested to sell " + sellingAmount + " stocks and has only " +
                currentAmount + " stocks.";
    }


}
