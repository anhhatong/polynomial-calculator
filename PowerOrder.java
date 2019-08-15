import java.util.Comparator;
/**
 * Represents a comparator to sort monomials in a polynomial from higher degrees
 * to lower degrees.
 * @author Maddie Tong
 * @version CMPU102 special edition
 */
public class PowerOrder implements Comparator<Mono> {
    /**
     * Sorts monomials in a polynomial from higher degrees to lower degrees.
     * @param m1 monomial 1
     * @param m2 monomial 2
     * @return integer 0 is the two monomials have the same degree, 1 if 
     * monomial 2 has a higher degree, and -1 if monomial 1 has a higher degree
     */
    public int compare(Mono m1, Mono m2) {
        int p1 = m1.power;
        int p2 = m2.power;
        if (p1<p2)
            return 1;
        else if (p1 == p2)
            return 0;
        else
            return -1;
    }
}