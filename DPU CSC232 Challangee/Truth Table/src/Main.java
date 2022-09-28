import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Get the expression from key board
        String input = getExpressionInputFromKeyBoard();

        // Check the number of variable
        int numVal = 0;
        boolean num = true;
        while(num){
            numVal = checkTheNumberOfInputVariable(input);
            if(numVal <= 0 || numVal > 4){
                System.out.println("Unable to execute expression. Please try agian");
                input = getExpressionInputFromKeyBoard();
            } else {
                num = false;
            }
        }
        //List of array
        boolean[] arr1 = {false,false,false,false};
        boolean[] arr2 = {false,false,false,true};
        boolean[] arr3 = {false,false,true,false};
        boolean[] arr4 = {false,false,true,true};
        boolean[] arr5 = {false,true,false,false};
        boolean[] arr6 = {false,true,false,true};
        boolean[] arr7 = {false,true,true,false};
        boolean[] arr8 = {false,true,true,true};
        boolean[] arr9 = {true,false,false,false};
        boolean[] arr10 = {true,false,false,true};
        boolean[] arr11 = {true,false,true,false};
        boolean[] arr12 = {true,false,true,true};
        boolean[] arr13 = {true,true,false,false};
        boolean[] arr14 = {true,true,false,true};
        boolean[] arr15 = {true,true,true,false};
        boolean[] arr16 = {true,true,true,true};
        boolean[][] variables = {arr1,arr2,arr3,arr4,arr5,arr6,arr7,arr8,arr9,arr10,arr11,arr12,arr13,arr14,arr15,arr16};

        String DNFExpression = "";
        System.out.println("a b c d | e");
        for(int i = 0; i < 16 ;i++) {
            // replace a,b,c,d to T/F, and remove space
            for(int j = 0; j < arr1.length;j++){
                if(variables[i][j] == true)
                System.out.print("T ");
                if(variables[i][j] == false)
                    System.out.print("F ");
            }
            String convertedString = changeTheVariablesAndEditString(input, variables[i]);
            // Execute Expression
            String output = executeExpression(convertedString);
            System.out.print("| " + output);
            System.out.println();
            // DNFExpression;
            if(output.equals("T")){
                if(variables[i][0] == false){
                    DNFExpression += "!";
                }
                if(DNFExpression.equals("!") || DNFExpression.equals("")) {
                    DNFExpression += "a && ";
                } else {
                    DNFExpression += "|| a && ";
                }
                if(variables[i][1] == false){
                    DNFExpression += "!";
                }
                DNFExpression += "b && ";
                if(variables[i][2] == false){
                    DNFExpression += "!";
                }
                DNFExpression += "c && ";
                if(variables[i][3] == false){
                    DNFExpression += "!";
                }
                    DNFExpression += "d";
            }
        }
        System.out.println(input);
        System.out.println("DNFExpression = " + DNFExpression);
    }

    public static String getExpressionInputFromKeyBoard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Get input from keyboard: ");
        String input = scanner.nextLine();
        scanner.close();
        return input;
    }
    public static int checkTheNumberOfInputVariable(String input){
        int count = 0;
        if(input.contains("a"))
            count++;
        if(input.contains("b"))
            count++;
        if(input.contains("c"))
            count++;
        if(input.contains("d"))
            count++;
        return count;
    }
    public static String executeExpression(String input) {
        while (input.length() != 1) {
            String output = input.replaceAll("!T", "F");
            input = output;
            output = input.replaceAll("!F", "T");
            input = output;
            output = input.replaceAll("T&&T", "T");
            input = output;
            output = input.replaceAll("F&&F|F&&T|T&&F", "F");
            input = output;
            output = input.replaceAll("F\\|\\|F", "F");
            input = output;
            output = input.replaceAll("T\\|\\|T|T\\|\\|F|F\\|\\|T", "T");
            input = output;
            output = input.replaceAll("\\(T\\)", "T");
            input = output;
            output = input.replaceAll("\\(F\\)", "F");
            input = output;
        }
        return input;
    }

    public static String changeTheVariablesAndEditString(String input, boolean[] variables) {

        String output = input;
        if (variables[0])
            output = input.replaceAll("a", "T");
        input = output;
        if (variables[1])
            output = input.replaceAll("b", "T");
        input = output;

        if (variables[2])
            output = input.replaceAll("c", "T");
        input = output;

        if (variables[3])
            output = input.replaceAll("d", "T");
        input = output;

        output = input.replaceAll("a|b|c|d", "F");
        input = output;
        output = input.replaceAll(" ", "");
        input = output;
        return input;
    }

}



