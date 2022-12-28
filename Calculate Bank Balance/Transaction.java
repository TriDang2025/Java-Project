import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction implements Comparable<Transaction> {
    private Date date;
    private int amountTransaction;

    public Transaction(String date, int amountTransaction) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        this.date = format.parse(date);
        this.amountTransaction = amountTransaction;
    }

    public Date getDate() {
        return date;
    }

    public int getAmountTransaction() {
        return amountTransaction;
    }

    @Override
    public int compareTo(Transaction other) {
        int result = date.compareTo(other.date);
        if (result == 0) {
            return other.amountTransaction - amountTransaction;
        }
        return result;
    }
}
