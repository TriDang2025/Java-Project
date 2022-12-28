import java.util.ArrayList;

public class Customer {
    private String id;
    private ArrayList<Transaction> transactions;
    private ArrayList<CustomerTransactionEachMonth> customerTransactionEachMonths;

    public Customer(String id) {
        this.id = id;
        transactions = new ArrayList<Transaction>();
        customerTransactionEachMonths = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction newTransaction) {
        if (transactions.contains(newTransaction)) {
            return;
        }
        transactions.add(newTransaction);
    }

    public ArrayList<CustomerTransactionEachMonth> getCustomerTransactionEachMonths() {
        return customerTransactionEachMonths;
    }

    public CustomerTransactionEachMonth addCustomerTransactionEachMonths(
            CustomerTransactionEachMonth newCustomerTransactionEachMonth) {
        for (CustomerTransactionEachMonth customerTransactionEachMonth : customerTransactionEachMonths) {
            if (customerTransactionEachMonth.getDate().equals(newCustomerTransactionEachMonth.getDate())) {
                return customerTransactionEachMonth;
            }
        }
        customerTransactionEachMonths.add(newCustomerTransactionEachMonth);
        return newCustomerTransactionEachMonth;
    }
}