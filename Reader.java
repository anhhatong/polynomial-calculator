import java.util.ArrayList;
import java.util.Arrays;
/**
 * Represents the input expression reader.
 * @author Maddie Tong
 * @version CMPU102 special edition
 */
public class Reader{
    /**
     * represents the current calculation with the current storage
     */
    Calculator calc;
    /**
     * represents a string of polynomial after plugging the stored polynomials
     * in the variables
     */
    String nospace;
    /**
     * represents the list of tokens in infix expression
     */
    ArrayList<Object> tokenlist;

    /**
     * Constructor for objects of class Reader
     * @param calc the polynomial calculation that is occuring
     */
    Reader(Calculator calc){
        this.calc = calc; //sync the current state in Calculation with the calc object here
    }
    /**
     * An enum class that sets the states for the finite state machine which 
     * checks if the expression passed in is valid
     */
    enum RdaState {
        /**
         * An enum constant set to false and representing the state of waiting 
         * for an open parenthesis
         */
        Wopen(false),

        /**
         * An enum constant set to true and representing the state of waiting for
         * more digits to form the coefficient of a monomial
         */
        Int(true),

        /**
         * An enum constant set to false and representing the state of waiting for
         * the first decimal point to form the coefficient of a monomial
         */
        Wdec(false),

        /**
         * An enum constant set to true and representing the state of waiting for
         * more decimal points to form the coefficient of a monomial
         */
        Dec(true),

        /**
         * An enum constant set to true and representing the state at the 'x'
         * variable of the monomial
         */
        X(true),

        /**
         * An enum constant set to false and representing the state at the caret
         * symbol of the monomial
         */
        Caret(false),

        /**
         * An enum constant set to true and representing the state of waiting for
         * more digits to form the exponent of a monomial
         */
        Exp(true),

        /**
         * An enum constant set to false and representing the state at an operator
         * in the expression
         */
        Oper(false),

        /**
         * An enum constant set to true and representing the state at a close 
         * parenthesis
         */
        Wclose(true),

        /**
         * An enum constant set to false and representing the error state
         */
        ERR(false);
        /**
         * true if it is okay to end with such state and false if it is not okay 
         * to end with such state
         */
        private final boolean isAccept;

        /**
         * Constructor for objects of class RdaState
         * @param isAccept true if it is okay to end with such state and false if
         * it is not okay to end with such state
         */
        RdaState(boolean isAccept){
            this.isAccept = isAccept;
        }

        /**
         * Checks if the current state is acceptable or not
         * @return true if it is okay to end with such state and false if
         * it is not okay to end with such state
         */   
        public boolean isAccept(){ return isAccept; }

    }
    /**
     * Represents the finite state machine that checks if the polynomial 
     * expression inputted is valid
     * @param expression represents the polynomial expression passed in
     * @return a list of tokens in infix notation, or null
     * if the expression passed in is invalid
     */
    
    //only the regular polynomial expressions are passed in here
    public ArrayList<Object> readPoly(String expression){ 
        String coef = "0"; // store the coefficient of a monomial
        String power = "0"; // store the exponent of a monomial

        String s = expression.replaceAll("\\s+","");

        tokenlist = new ArrayList<Object>();

        int length = s.length();
        RdaState curState = RdaState.Wopen; //start with Wopen

        int i = 0;
        while (curState != RdaState.ERR && i<length) {
            char c = s.charAt(i++);
            double dcoef = 0; 
            int dpower = 0;

            switch(curState) {

                case Wopen:
                if (c == '(') 
                    tokenlist.add(c);
                else if (c == '-') {
                    tokenlist.add(new Poly(0,0));
                    tokenlist.add(c);             
                } else if (Character.isDigit(c)) {
                    if (i == length) { // check if c is the last token
                        coef += c;
                        dcoef = Double.parseDouble(coef);
                        tokenlist.add(new Poly(dcoef,0));
                        curState = RdaState.Int;
                    } else {
                        curState = RdaState.Int;
                        coef += c;
                    }
                } else if (c == '.') {
                    curState = RdaState.Wdec;
                    coef += "0" + c;
                } else if (c == 'x') {
                    if (i == length) { // check if c is the last token
                        tokenlist.add(new Poly(1,1));
                        curState = RdaState.X;
                    }
                    else {
                        curState = RdaState.X;
                        coef += "1";
                    }
                }  else
                    curState = RdaState.ERR;
                break;

                case Int:
                if (Character.isDigit(c)) {
                    if (i == length) { // check if c is the last token
                        coef += c;
                        dcoef = Double.parseDouble(coef);
                        dpower = Integer.parseInt(power);
                        tokenlist.add(new Poly(dcoef,dpower));
                    } else
                        coef += c;
                } else if (c == '.') {
                    curState = RdaState.Wdec;
                    coef += c;
                } else if (c == 'x') {
                    if (i == length) { // check if c is the last token
                        power += "1";
                        dcoef = Double.parseDouble(coef);
                        dpower = Integer.parseInt(power);
                        tokenlist.add(new Poly(dcoef,dpower));
                        curState = RdaState.X;
                    } else
                        curState = RdaState.X;
                } else if (c == ')') {
                    curState = RdaState.Wclose;
                    dcoef = Double.parseDouble(coef);
                    dpower = Integer.parseInt(power);
                    tokenlist.add(new Poly(dcoef,dpower));
                    tokenlist.add(c);
                    coef = "0";
                    power = "0";
                } else if (c == '+'|| c == '-' || c == '*' || c == '/') {
                    curState = RdaState.Oper;
                    dcoef = Double.parseDouble(coef);
                    dpower = Integer.parseInt(power);
                    tokenlist.add(new Poly(dcoef,dpower));
                    tokenlist.add(c);
                    coef = "0";
                    power = "0";
                } else
                    curState = RdaState.ERR;
                break;

                case Wdec:
                if (Character.isDigit(c)) {
                    curState = RdaState.Dec;
                    coef += c;
                } else
                    curState = RdaState.ERR;
                break; 

                case Dec:
                if (Character.isDigit(c)) {
                    if (i == length) { // check if c is the last token
                        coef += c;
                        dcoef = Double.parseDouble(coef);
                        dpower = Integer.parseInt(power);
                        tokenlist.add(new Poly(dcoef,dpower));
                    } else
                        coef += c;
                } else if (c == 'x') {
                    if (i == length) { // check if c is the last token
                        power += "1";
                        dcoef = Double.parseDouble(coef);
                        dpower = Integer.parseInt(power);
                        tokenlist.add(new Poly(dcoef,dpower));
                        curState = RdaState.X;
                    } else
                        curState = RdaState.X;
                } else if (c == '+'|| c == '-' || c == '*' || c == '/') {
                    curState = RdaState.Oper;
                    dcoef = Double.parseDouble(coef);
                    dpower = Integer.parseInt(power);
                    tokenlist.add(new Poly(dcoef,dpower));
                    tokenlist.add(c);
                    coef = "0";
                    power = "0";
                } else if (c == ')') {
                    curState = RdaState.Wclose;
                    dcoef = Double.parseDouble(coef);
                    dpower = Integer.parseInt(power);
                    tokenlist.add(new Poly(dcoef,dpower));
                    tokenlist.add(c);
                    coef = "0";
                    power = "0";
                } else
                    curState = RdaState.ERR;
                break;

                case X:
                if (c == '^') {
                    curState = RdaState.Caret;
                } else if (c == '+'|| c == '-' || c == '*' || c == '/') {
                    power += "1";
                    curState = RdaState.Oper;
                    dcoef = Double.parseDouble(coef);
                    dpower = Integer.parseInt(power);
                    tokenlist.add(new Poly(dcoef,dpower));
                    tokenlist.add(c);
                    coef = "0";
                    power = "0";
                } else if (c == ')') {
                    power += "1";
                    curState = RdaState.Wclose;
                    dcoef = Double.parseDouble(coef);
                    dpower = Integer.parseInt(power);
                    tokenlist.add(new Poly(dcoef,dpower));
                    tokenlist.add(c);
                    coef = "0";
                    power = "0";
                } else
                    curState = RdaState.ERR;
                break;

                case Caret:
                if (Character.isDigit(c)) {
                    if (i == length) { // check if c is the last token
                        power += c;
                        dcoef = Double.parseDouble(coef);
                        dpower = Integer.parseInt(power);
                        tokenlist.add(new Poly(dcoef,dpower));
                        curState = RdaState.Exp;
                    } else {
                        curState = RdaState.Exp;
                        power += c;
                    }
                } else
                    curState = RdaState.ERR;
                break;

                case Exp:
                if (Character.isDigit(c)) {
                    if (i == length) { // check if c is the last token
                        power += c;
                        dcoef = Double.parseDouble(coef);
                        dpower = Integer.parseInt(power);
                        tokenlist.add(new Poly(dcoef,dpower));
                    } else
                        power += c;
                } else if (c == '+'|| c == '-' || c == '*' || c == '/') {
                    curState = RdaState.Oper;
                    dcoef = Double.parseDouble(coef);
                    dpower = Integer.parseInt(power);
                    tokenlist.add(new Poly(dcoef,dpower));
                    tokenlist.add(c);
                    coef = "0";
                    power = "0";
                } else if (c == ')') {
                    curState = RdaState.Wclose;
                    dcoef = Double.parseDouble(coef);
                    dpower = Integer.parseInt(power);
                    tokenlist.add(new Poly(dcoef,dpower));
                    tokenlist.add(c);
                    coef = "0";
                    power = "0";
                } else 
                    curState = RdaState.ERR;
                break;

                case Oper:
                if (c == '(') {
                    curState = RdaState.Wopen;
                    tokenlist.add(c);
                } else if (Character.isDigit(c)) {
                    if (i == length) { // check if c is the last token
                        coef += c;
                        dcoef = Double.parseDouble(coef);
                        dpower = Integer.parseInt(power);
                        tokenlist.add(new Poly(dcoef,dpower));
                        curState = RdaState.Int;
                    } else {
                        curState = RdaState.Int;
                        coef += c;
                    }
                } else if (c == '.') {
                    curState = RdaState.Wdec;
                    coef += "0" +c;
                } else if (c == 'x') {
                    if (i == length) { // check if c is the last token
                        coef += "1";
                        power += "1";
                        dcoef = Double.parseDouble(coef);
                        dpower = Integer.parseInt(power);
                        tokenlist.add(new Poly(dcoef,dpower));
                        curState = RdaState.X;
                    } else{
                        curState = RdaState.X;
                        coef += "1";
                    }
                } else
                    curState = RdaState.ERR;
                break;

                case Wclose:
                if (c == '+'|| c == '-' || c == '*' || c == '/') {
                    curState = RdaState.Oper;
                    tokenlist.add(c);
                }
                else if (c == ')') 
                    tokenlist.add(c);
                else
                    curState = RdaState.ERR;
                break;
            }
        }
        if(curState.isAccept() == false)
            return null;
        return tokenlist;
    }

    /**
     * Reads the expression passed in and plugs the stored polynomials into the
     * corresponding key variables found in the map. If the expression represents
     * a regular polynomial, then output the expression passed in. If the key
     * variables are not to be found in the storing map, then output null and
     * print out an error message.
     * @param expression represents the expression passed in, which can either
     * be a regular polynomial or an operation of several key variables
     * @return a string representing a regular polynomial expression after
     * reading the variables and plugging in, or null if it is an invalid
     * expression
     */
    public String readVar(String expression) {
        nospace = expression.replaceAll("\\s+",""); 
        
        // split by taking away nonword characters and numbers so that we only have the 
        // variables left to check
        String[] sarray = nospace.split("\\W+|\\d+"); 

        for(String s:sarray)
            if(!calc.store.containsKey(s)) {
                if (s.length() == 0) // check if the item is an empty string
                    continue;
                else if (s.equals("x")) { 
                    // after splitting, if x is the variables, then we know it is a regular 
                    //polynomial expression
                    nospace = expression; // output the original expression
                    break;
                }
                else 
                    return null; // when the variables is not found in map

            } else {
                String plugin = calc.store.get(s).toString(); // get key from map
                nospace = nospace.replaceAll(s,"("+plugin+")"); // take the original expression and substitute 
            }

        return nospace;
    }
}