import java.io.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

// To run this program, you need to download JRE from the internet and replace the URL of the input file you want and the URL of the output file you want to create and write output data.

public class Main {
    public static void main(String[] args) throws Exception {

        // Read input file
        String InputURL = "C:\\Users\\vtriw\\Desktop\\JustWorkAssesment\\inputData.csv";
        ArrayList<String[]> inputData = readFile(InputURL);

        // Spread the input data to HashMap that has Key: Customer ID, Value: Customer
        // Object.
        Map<String, Customer> customers = new LinkedHashMap<>();
        for (String[] data : inputData) {
            String customerId = data[0];
            if (!customers.containsKey(customerId)) {
                customers.put(customerId, new Customer(customerId));
            }
            String date = data[1];
            int amount;
            if (data.length < 3) {
                System.out.println("Missing amount of transaction in " + data[1]);
                amount = 0;
            } else {
                amount = Integer.parseInt(data[2]);
            }
            Transaction newTransaction = new Transaction(date, amount);
            customers.get(customerId).addTransaction(newTransaction);
        }
        // Create the HashMap that has Key: Customer, Value: The arraylist contain their
        // transaction in specific month
        HashMap<Customer, ArrayList<CustomerTransactionEachMonth>> customersInEachMonth = new LinkedHashMap<>();
        for (Map.Entry<String, Customer> customer : customers.entrySet()) {
            customersInEachMonth.put(customer.getValue(), customer.getValue().getCustomerTransactionEachMonths());
            for (Transaction transaction : customer.getValue().getTransactions()) {
                Date transactionDate = transaction.getDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(transactionDate);
                int transactionYear = cal.get(Calendar.YEAR);
                int transactionMonth = 1 + cal.get(Calendar.MONTH);
                String monthOfTransaction = transactionMonth + "/" + transactionYear;
                CustomerTransactionEachMonth customerTransactionEachMonth = new CustomerTransactionEachMonth(
                        customer.getKey(), monthOfTransaction);
                customer.getValue().addCustomerTransactionEachMonths(customerTransactionEachMonth)
                        .addTransaction(transaction);
            }
        }

        // Create new output file;
        String newOutputFileURL = "C:\\Users\\vtriw\\Desktop\\JustWorkAssesment\\Output.csv";
        createNewFile(newOutputFileURL);

        // write Output Date to Output file;
        try {
            FileWriter fileWriter = new FileWriter(newOutputFileURL);
            for (Map.Entry<Customer, ArrayList<CustomerTransactionEachMonth>> customerInMonth : customersInEachMonth
                    .entrySet()) {
                for (CustomerTransactionEachMonth customerTransactionEachMonth : customerInMonth.getValue()) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(customerTransactionEachMonth.getDate());
                    int transactionYear = cal.get(Calendar.YEAR);
                    int transactionMonth = 1 + cal.get(Calendar.MONTH);
                    String monthOfTransaction = transactionMonth + "/" + transactionYear;
                    fileWriter.write(customerTransactionEachMonth.getId() + ", " + monthOfTransaction
                            + ", " + customerTransactionEachMonth.getMinBalance() + ", "
                            + customerTransactionEachMonth.getMaxBalance() + ", "
                            + customerTransactionEachMonth.getEndingBalance());
                    fileWriter.write("\n");
                }
            }
            fileWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Method for read CSV file
    public static ArrayList<String[]> readFile(String url) throws IOException {
        File file = new File(url);
        ArrayList<String[]> inputData = new ArrayList<String[]>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            String line = bufferedReader.readLine();
            while (line != null) {
                String[] array = splitString(line);
                inputData.add(array);
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputData;
    }

    // Method for split the one line in CSV into array that data contains Customer
    // Id, Date, Amount of Money
    public static String[] splitString(String input) {
        String string = input.replace(",", "");
        String[] array = string.split(" ");
        return array;
    }

    // Method for create new output File
    public static void createNewFile(String filePath) {
        try {
            File file = new File("Output.csv");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}