import java.util.ArrayDeque;
import java.util.ArrayList;
/**
 * Represents the conversion from the list of tokens from infix notation 
 * to postfix notation 
 * @author Maddie Tong
 * @version CMPU102 special edition
 */
public class Postfix {
    /**
     * represents the list of tokens in postfix expression
     */
    ArrayList<Object> output;
    /**
     * represents an error message string if there is a parenthesis mismatch 
     */
    String mes;

    /**
     * Converts the list of tokens in infix to postfix notation
     * @param infix the list of tokens in infix notation
     * @return the list of tokens in postfix notation, and null of there is 
     * a parenthesis mismatch
     */
    public ArrayList<Object> convert(ArrayList<Object> infix) {
        ArrayDeque<Object> stack = new ArrayDeque<Object>();
        output = new ArrayList<Object>();
        for (Object token : infix) 
            if (token instanceof Poly) // check if token is a polynomial
                output.add(token);
            else if (token.equals('('))
                stack.push(token);
            else if (token.equals(')')) {
                while (!stack.isEmpty() && !(stack.peek().equals('('))) 
                    output.add(stack.pop());
                if (stack.isEmpty()) {
                    mes = "Error - Mismatched right parenthesis";
                    return null;
                }
                stack.pop();
            } else if (token.equals('+') || token.equals('-') || token.equals('*') || token.equals('/')) {
                while (!stack.isEmpty() && !(stack.peek().equals('(')) 
                && prio(stack.peek()) >= prio(token))
                    output.add(stack.pop());
                stack.push(token);
            } 
        while (!stack.isEmpty()) { // last check to see if there is any parenthesis left
            Object token = stack.pop();
            if (token.equals('(')) {
                mes = "Error - Mismatched left parenthesis";
                return null;
            }
            output.add(token);
        }

        return output;
    }

    /**
     * Ranks operators by the rule of operator precendence
     * @param o the operator needed to be ranked
     * @return an integer 0 if the operator has lower precedence and 1 if the
     * operator as higher precedence
     */
    public int prio(Object o) {
        if (o.equals('+') || o.equals('-'))
            return 0;
        return 1;
    }

}