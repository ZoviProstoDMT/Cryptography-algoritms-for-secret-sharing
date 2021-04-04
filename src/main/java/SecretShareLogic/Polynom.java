package SecretShareLogic;

import java.util.ArrayList;

public class Polynom {

    int secret = VerifiableSecretSharing.getSecret();
    int dim;
    int f;
    ArrayList<Integer> coefficients = new ArrayList<>();
    ArrayList<String> polynomY = new ArrayList<>();

    public Polynom(int dim, int f) {
        this.dim = dim;
        this.f = f;
        addRandomCoefficients(secret);
        generatePolynomY(dim);
    }

    public void addRandomCoefficients(int secret) {
        coefficients.add(secret);
        for (int i = 1; i < dim; i++) {
            while (true) {
                int rand;
                rand = (int) (Math.random() * (VerifiableSecretSharing.n * 2));
                if (rand != 0) {
                    coefficients.add(rand);
                    break;
                }
            }
        }
    }

    public void printCoefficients() {
        for (int i = 0; i < coefficients.size(); i++) {
            if (i == coefficients.size() - 1)
                System.out.print("a" + i + " = " + coefficients.get(i) + ".");
            else
                System.out.print("a" + i + " = " + coefficients.get(i) + ", ");
        }
    }

    public void generatePolynomY(int dim) {
        polynomY.add("");
        polynomY.add("x");
        for (int i = 2; i < dim; i++) {
            polynomY.add("x^" + i);
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < dim; i++) {
            if (i == dim - 1) {
                s.append(coefficients.get(i)).append(polynomY.get(i));
            } else
                s.append(coefficients.get(i)).append(polynomY.get(i)).append(" + ");
        }
        return s.toString();
    }
}