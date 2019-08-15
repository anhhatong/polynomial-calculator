import java.util.TreeSet;
import java.util.Iterator;
/**
 * Represents a polynomial.
 * @author Maddie Tong
 * @version CMPU102 special edition
 */
public class Poly {
    /**
     * represents the set of monomials in a polynomial
     */
    TreeSet<Mono> set;

    /**
     * Empty constructor for objects of class Poly
     */
    public Poly() { // empty polynomial
        set = new TreeSet<Mono>(new PowerOrder());
    }

    /**
     * Constructor for objects of class Poly
     * @param coef represents the coefficient of the single monomial
     * in the polynomial indicated
     * @param power represents the power of the single monomial in the 
     * polynomial indicated
     */
    public Poly(double coef, int power) { 
        set = new TreeSet<Mono>(new PowerOrder());
        set.add(new Mono(coef,power));
    }

    /**
     * Constructor for objects of class Poly
     * @param set represents the set of monomials in the polynomial indicated
     */
    public Poly(TreeSet<Mono> set) {
        this.set = set;
    }

    /**
     * Adds a new monomial to a polynomial
     * @param o monomial to be added
     * @return the resulted polynomial after adding 
     */
    public Poly add(Mono o) { 
        Poly newpoly;

        if (o.coef == 0) {
            return new Poly(this.set);
        }

        for (Mono m: this.set) {

            if (m.power == o.power) {
                m.coef = m.coef + o.coef;

                if (m.coef == 0)
                    this.set.remove(m);

                newpoly = new Poly(this.set);
                return newpoly;
            }

        }

        (this.set).add(o);
        newpoly = new Poly(this.set);
        return newpoly;

    }

    /**
     * Substracts a new monomial from a polynomial
     * @param o monomial to be subtracted
     * @return the resulted polynomial after subtracting 
     */
    public Poly subtract(Mono o) { 
        Poly newpoly;

        for (Mono m: this.set) {
            if (m.power == o.power) {
                m.coef = m.coef - o.coef;

                if (m.coef == 0)
                    this.set.remove(m);

                newpoly = new Poly(this.set);
                return newpoly;
            }

        }

        o.coef = -o.coef;
        (this.set).add(o);
        newpoly = new Poly(this.set);
        return newpoly;

    }

    /**
     * Multiplies a new monomial and a polynomial together
     * @param o monomial to be multiplied
     * @return the resulted polynomial after multiplying 
     */
    public Poly multiply(Mono o) {
        Poly newpoly = new Poly();
        double tempcoef;
        int temppower;

        for (Mono m: this.set) {
            tempcoef = m.coef * o.coef;
            temppower = m.power + o.power;
            newpoly = newpoly.add(new Mono(tempcoef,temppower));
        }

        return newpoly;
    }

    /**
     * Divides a polynomial by a new monomial
     * @param o monomial to be divided
     * @return the resulted polynomial after dividing 
     */
    public Poly divide(Mono divisor) {
        Poly newpoly = new Poly();
        double tempcoef;
        int temppower;

        tempcoef = this.set.first().coef / divisor.coef;
        temppower = this.set.first().power - divisor.power;
        newpoly.add(new Mono(tempcoef,temppower));

        return newpoly;
    }

    /**
     * Adds a new polynomial to a polynomial
     * @param o polynomial to be added
     * @return the resulted polynomial after adding 
     */
    public Poly add(Poly p) {

        if (this.set.size() == 0) // when the first operand is 0, return the second operand
            return p;
        else if (p.set.size() == 0) // when the second operand is 0, return the first operand
            return new Poly(this.set);

        Poly newpoly = new Poly();
        //compute
        for (Mono m : this.set)
            newpoly.add(new Mono(m.coef, m.power));

        for (Mono n : p.set)
            newpoly = newpoly.add(n);

        return newpoly;
    }

    /**
     * Subtracts a new polynomial from a polynomial
     * @param o polynomial to be subtracted
     * @return the resulted polynomial after subtracting
     */
    public Poly subtract(Poly p) {
        
        if (p.set.size() == 0) // if the second operand is 0, output the first operand
            return new Poly(this.set);

        Poly newpoly = new Poly();
        Poly newpoly2 = new Poly();
        Poly newpoly3 = new Poly();
        
        // clean and compute
        for (Mono m : this.set)
            newpoly.add(new Mono(m.coef, m.power));

        for (Mono n : p.set)
            newpoly3 = newpoly2.add(newpoly.subtract(n));

        return newpoly3;
    }

    /**
     * Multiplies a new polynomial and a polynomial together
     * @param o monomial to be multiplied
     * @return the resulted polynomial after multiplying 
     */
    public Poly multiply(Poly p) {
        if (this.set.size() == 0 || p.set.size() == 0) // if one of the operands is 0, output a zero polynomial
            return new Poly();

        Poly newpoly = new Poly();
        Poly newpoly2 = new Poly();
        // clean and compute
        for (Mono m : this.set)
            newpoly.add(new Mono(m.coef, m.power));

        for (Mono n : p.set)
            newpoly2 = newpoly2.add(newpoly.multiply(n)); // multiply then add the results to a new polynomial

        return newpoly2;
    }

    /**
     * Divides a polynomial by a new polynomial
     * @param o polynomial to be divided
     * @return the resulted polynomial after dividing 
     */
    public Poly divide(Poly p) {

        if (p.set.size() == 0) // if the divisor is 0, return null
            return null;
        else if (this.set.size() == 0) // if the divider is 0, return 0
            return new Poly();

        Poly newpoly = new Poly();
        Poly newpoly2 = new Poly();
        // clean 
        for (Mono m : this.set)
            newpoly.add(new Mono(m.coef, m.power));
        for (Mono n : p.set)
            newpoly2.add(new Mono(n.coef, n.power));

        if (newpoly2.set.size() == 0) 
            return null;
        else if (newpoly.set.size() == 0)
            return new Poly();    

        Poly term = new Poly();
        Poly result = new Poly();
        Poly remainder = newpoly;  
        // compute 
        while ((remainder.set.size() != 0) && (remainder.set.first().power >= newpoly2.set.first().power)) {

            term = remainder.divide(newpoly2.set.first());
            result = result.add(term);
            remainder = remainder.subtract(term.multiply(newpoly2));

        }

        if (remainder.set.size() != 0) // check if the remainder is 0
            return null;

        return result;
    }

    /**
     * Creates a textual representation of a polymonomial expression
     * @return a string representing a polymonomial expression  
     */
    public String toString() {
        String s = "";
        Iterator<Mono> itr = set.iterator();

        if (itr.hasNext() == false) // empty set
            s = "0";

        if (itr.hasNext()) { // first term of the polynomial is different textually

            Mono element = itr.next();

            if (element.power == 0)
                s += element.coef;
            else if (element.power == 1) {
                if (element.coef > 0)
                    if (element.coef == 1)
                        s += "x";
                    else
                        s += element.coef+"x";
                else if (element.coef < 0)
                    if (element.coef == -1)
                        s += "-x";
                    else
                        s += element.coef+ "x";
            }
            else if (element.power > 1) {
                if (element.coef > 0)
                    if (element.coef == 1)
                        s += "x^"+element.power;
                    else
                        s += element.coef+"x^"+element.power;
                else if (element.coef < 0)
                    if (element.coef == -1)
                        s += "-x^"+element.power;
                    else
                        s += element.coef+ "x^"+element.power;
            }
        }
        while (itr.hasNext()) {
            Mono element = itr.next();

            if (element.power == 0)
                if (element.coef > 0)
                    s += "+"+element.coef;
                else
                    s += element.coef;
            else if (element.power == 1) {
                if (element.coef > 0)
                    if (element.coef == 1)
                        s += "+x";
                    else
                        s += "+"+element.coef+"x";
                else if (element.coef < 0)
                    if (element.coef == -1)
                        s += "-x";
                    else
                        s += element.coef+ "x";
            }
            else if (element.power > 1)
                if (element.coef > 0)
                    if (element.coef == 1)
                        s += "+x^"+element.power;
                    else
                        s += "+"+element.coef+"x^"+element.power;
                else if (element.coef < 0)
                    if (element.coef == -1)
                        s += "-x^"+element.power;
                    else
                        s += element.coef+ "x^"+element.power;
        }

        return s;
    }

}