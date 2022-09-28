import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        // get Input
        String input = getInputFromKeyBoard();

        // check String
        checkString(input);

    }

    public static String getInputFromKeyBoard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Get input from keyboard: ");
        String input = scanner.nextLine();
        scanner.close();
        return input;
    }

    public static void checkString(String input){

//      It is either the string "0" or it starts with a 1
        boolean b = Pattern.matches("^0",input);
        if(b) {
            System.out.println("It is the string \"0\"");
        }

        b = Pattern.matches("^(1).*",input);
        if(b) {
            System.out.println("It starts with a 1");
        }

//        It ends with a 0
        b = Pattern.matches(".*(0)$",input);
        if(b){
            System.out.println("It ends with a 0");
        }

//        It starts and ends with 0
        b = Pattern.matches("^(0).*(0)$",input);
        if(b){
            System.out.println("It starts and ends with 0");
        }

//        It consists of strictly alternating 0's and 1's (starting and ending with either)
        b = Pattern.matches("(^(0[^0])*||^(1[^1]||^0||^1)*)||(^(1[^1])*||^(0[^0]||^0||^1)*)" ,input);
        if(b){
            System.out.println("It consists of strictly alternating 0's and 1's");
        }

//        It contains the substring "101"
        b = Pattern.matches(".*(101).*",input);
        if(b){
            System.out.println("It contains the substring \"101\"");
        }

//        It does not contain three adjacent 1' (I cannot find the solution with a positive test can you give me some hint ? )
        b = Pattern.matches(".*(111).*",input);
        if(b == false){
            System.out.println("It does not contain three adjacent 1\'");
        }
    }
}
