import java.util.Scanner;
import java.util.HashMap;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
/**
 * Represents the polynomial calculator interacting with the user.
 * @author Maddie Tong
 * @version CMPU102 special edition
 */
public class Calculator {
    /**
     * represents the storage of variables assigned to the polynomials
     */
    HashMap<String,Poly> store = new HashMap<String,Poly>();
    /**
     * represents the file inputted by the user
     */
    File file;
    /**
     * represents the result of the polynomial calculation
     */
    String res;

    /**
     * Stores a new polynomial with a corresponding key variable in the map
     * @param var the variable name, which is the map's key
     * @param p the polynomial corresponding to the variable, 
     * which is the key's value
     */
    public void addMap(String var, Poly p) {
        store.put(var,p);
    }

    /**
     * Reads the text file passed in line by line and prints out the polynomial
     * results corresponding to each expression line
     * @param file the text file passed in
     */
    public void readText(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                System.out.println("Result: "+calculate(line));
            }
            br.close();
        } catch (IOException e) {
            System.err.println(e);
            return;
        }
    }

    /**
     * Represents the polynomial calculating process: reading the expression,
     * plugging in polynomials corresponding to the indicated variables (if
     * the expression is the operation of only polynomials already stored
     * separating it into infix tokens), converting from infix to postfix tokens,
     * and evaluating the postfix list
     * @param input the polynomial expression given by the user
     * @return a string representing the polynomial result computed or
     * the appropriate error message
     */
    public String calculate(String input) {
        Reader r = new Reader(this); 
        // the 'this' keyword lets object r of type Reader know about the current map in Calculation class 
        Postfix p = new Postfix();
        Evaluation e = new Evaluation();
        Poly a;
        String[] sarray = input.split("\\s*=\\s*"); // split the string input by the equal sign
        if (sarray.length == 1) { // case 1: no storing required
            input = sarray[0]; // without the equal sign, so the array only contains one string
            if (r.readVar(input) == null)
                return "Error - Invalid expression"; // when a variable is not found in memory

            if (r.readPoly(r.nospace) == null) 
                return "Error - Invalid expression"; // when the expression does not make sense

            if (p.convert(r.tokenlist) == null)
                return p.mes; // when there is a mismatch parenthesis

            a = e.eval(p.output);
            if (a == null)
                return "Error - Indivisible"; // when the division has nonzero remainder or zero denominator
            else
                res = ""+a;
        }
        else if (sarray.length == 2) { // case 2: storing required
            if(!sarray[0].matches("[a-wy-z]")) 
            // check if the left side of the equal sign is x -> error if it is
                res = "Error - Please assign the polynomial to a single English alphabet letter other than x";
            else {
                input = sarray[1]; // right side of the equal sign
                if (r.readVar(input) == null)
                    return "Error - Invalid expression";

                if (r.readPoly(r.nospace) == null)
                    return "Error - Invalid expression";

                if (p.convert(r.tokenlist) == null) 
                    return p.mes;

                a = e.eval(p.output);
                if (a == null)
                    res = "Error - Indivisible";
                else {
                    store.put(sarray[0],a); 
                    // after successful evaluation, store the key variable and the corresponding resulted polynomial 
                    res = sarray[0]+" = "+a;
                }

            }

        }
        else 
            res = "Error - Invalid expression";

        return res;
    }

    /**
     * Runs the polynomial calculator starting with a welcome message and 
     * with the user's choice of mode (file or interactive). If the user types
     * in "file", ask the user to enter the file path, check if it is valid,
     * print out an error message if file path provided is invalid,
     * and then carry out the reading of the file & computation. If the user 
     * types in "interactive", allow him/her to enter a polynomial expression
     * with or without a variable to store, and then print out the polynomial
     * result computed. If the user types in "exit", escape the current mode
     * and go back to the menu where the user can choose mode with previous 
     * polynomials erased from memory. If the user types in "exit" at the menu,
     * then exit the program
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Polynomial Calculator by Maddie Tong!");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Calculator calc = new Calculator(); // clear mememory every time the program goes back to menu
            calc.store = new HashMap<String,Poly>();
            System.out.println("Please choose mode (file or interactive):");
            String mode = scanner.nextLine().toLowerCase().replaceAll("\\s","");
            if (mode.equals("file"))
                while(true) {
                    System.out.println("Please enter file path: ");
                    String input = scanner.nextLine().toLowerCase();

                    if (input.length() == 0)
                        continue;

                    calc.file = new File(input);

                    if(input.equals("exit"))
                        break;

                    if (calc.file.isFile()) 
                        calc.readText(calc.file);
                    else
                        System.out.println("Error - Filename is invalid");

                }
            else if (mode.equals("interactive"))
                while(true) {
                    Scanner scanner2 = new Scanner(System.in);
                    System.out.println("Please enter expression: ");
                    String input = scanner2.nextLine().toLowerCase();

                    if (input.length() == 0)
                        continue;

                    if(input.equals("exit"))
                        break;

                    System.out.println(input);
                    String output = calc.calculate(input);

                    if (output == null)
                        continue;
                    else
                        System.out.println("Result: "+output);

                }
            else if (mode.equals("exit")) {
                System.out.println("Goodbye");
                return;
            } else if (mode.length() == 0)
                continue;
            else
                System.out.println("Error - Invalid command");
        }
    }
}