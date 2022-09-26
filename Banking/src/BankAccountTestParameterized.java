import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class BankAccountTestParameterized {
    private BankAccount account;

    public void setup() {
        account = new BankAccount("Tim","Bulchalka",1000.00,BankAccount.CHECKING);
        System.out.println("Running a test");
    }

}
