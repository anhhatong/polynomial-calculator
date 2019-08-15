import java.util.ArrayList;
import java.util.ArrayDeque;
/**
 * Represents the evalution of the postfix list of tokens.
 * @author Maddie Tong
 * @version CMPU102 special edition
 */
public class Evaluation {
    /**
     * represents the polynomial result computed
     */
    Poly result;
    /**
     * Evaluates the list of postfix tokens to compute the orginal expression
     * inputted and outputs the resulted polynomial
     * @param postfix the list of tokens in postfix notation
     * @return the resulted polynomial, null if zero is the denominator
     */
    public Poly eval(ArrayList<Object> postfix) {
        ArrayDeque<Poly> stack = new ArrayDeque<Poly>();
        for (Object token : postfix) {
            if (token instanceof Poly)
                stack.push((Poly)token);
            else {
                Poly operand1 = stack.pop();
                Poly operand2 = stack.pop();
                if (token.equals('+'))
                    result = operand2.add(operand1);
                else if (token.equals('-'))
                    result = operand2.subtract(operand1);
                else if (token.equals('*'))
                    result = operand2.multiply(operand1);
                else if (token.equals('/')) {
                    if (operand2.divide(operand1) == null) // indivisiable
                        return null;
                    result = operand2.divide(operand1);
                }
                stack.push(result); 
            }
        }
        result = stack.pop(); // last item in stack is the result

        return result;
    }
}