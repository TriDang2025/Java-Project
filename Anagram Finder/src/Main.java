import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        //get input from keyboard
        String[] input = getInputFromKeyBoard();
//        for(String e: input){
//            System.out.print(e);
//        }
//        System.out.println();

        //get dictionary from .txt file
        String url = "C:\\Users\\vtriw\\IdeaProjects\\DPU CSC232 Challangee\\Challange3\\data.txt";
        Set<String[]> dictionary = readFile(url);

        //search
        Arrays.sort(input);
        for(String[] word : dictionary){
            String[] temp = Arrays.copyOf(word,word.length);
            Arrays.sort(word);
            if(Arrays.equals(word,input)){
                for (String w : temp){
                    System.out.print(w);
                }
                System.out.println();
            }
        }

    }

    public static String[] getInputFromKeyBoard(){
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] arrayInput = splitString(input);
        return arrayInput;
    }

    public static Set<String[]> readFile(String url) throws IOException {
        File file = new File(url);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        Set<String[]> Dictionary = new HashSet<String[]>();
        try {
            String line = bufferedReader.readLine();
            while(line != null){
                String[] array = splitString(line);
//                for(String e: array){
//                    System.out.print(e);
//                }
//                System.out.println();
                Dictionary.add(array);
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException exp) {
            exp.printStackTrace();
        } catch (IOException exp) {
            exp.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }
        return Dictionary;
    }

    public static String[] splitString(String input){
        String[] array = input.split("");
        return array;
    }
}
