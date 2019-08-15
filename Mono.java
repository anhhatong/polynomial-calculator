/**
 * Represents a monomial.
 * @author Maddie Tong
 * @version CMPU102 special edition
 */
public class Mono {
    /**
     * represents the coeficient of a monomial
     */
    double coef;
    /**
     * represents the degree of a monomial
     */
    int power;

    /**
     * Constructor for objects of class Mono
     * @param coef represents the coefficient of a monomial
     * @param power represents the power of a monomial 
     */
    public Mono(double coef, int power) {
        this.coef = coef;
        this.power = power;
    }
}