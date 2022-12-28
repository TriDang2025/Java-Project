import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CustomerTransactionEachMonth extends Customer {
    private Date date;
    private ArrayList<Transaction> transactionsInMonth;
    private int minBalance;
    private int maxBalance;
    private int endingBalance;

    public CustomerTransactionEachMonth(String id, String date) throws ParseException {
        super(id);
        SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
        this.date = (Date) format.parse(date);
        minBalance = 0;
        maxBalance = 0;
        endingBalance = 0;
        transactionsInMonth = new ArrayList<Transaction>();
    }

    public Date getDate() {
        return date;
    }

    public void addTransaction(Transaction newTransaction) {
        transactionsInMonth.add(newTransaction);
    }

    public int getMinBalance() {
        int balance = 0;
        if (transactionsInMonth.size() > 0) {
            minBalance = transactionsInMonth.get(0).getAmountTransaction();
        }
        for (Transaction transaction : transactionsInMonth) {
            balance += transaction.getAmountTransaction();
            if (balance < minBalance) {
                minBalance = balance;
            }
        }
        return minBalance;
    }

    public int getMaxBalance() {
        int balance = 0;
        if (transactionsInMonth.size() > 0) {
            maxBalance = transactionsInMonth.get(0).getAmountTransaction();
        }
        for (Transaction transaction : transactionsInMonth) {
            balance += transaction.getAmountTransaction();
            if (balance > maxBalance) {
                maxBalance = balance;
            }
        }
        return maxBalance;
    }

    public int getEndingBalance() {
        for (Transaction transaction : transactionsInMonth) {
            endingBalance += transaction.getAmountTransaction();
        }
        return endingBalance;
    }

    public ArrayList<Transaction> getTransactionsInMonth() {
        return transactionsInMonth;
    }
}