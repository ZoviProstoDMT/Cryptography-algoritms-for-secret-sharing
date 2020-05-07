package SecretShareLogic;

import java.util.ArrayList;
import java.util.Arrays;

public class Polynom {

    int dim;
    int f;
    ArrayList<Integer> coefficients = new ArrayList<>();
    ArrayList<String> polynomY = new ArrayList<>();

    public Polynom(int dim) {
        this.dim = dim;
        addRandomCoefficients();
        generatePolynomY(dim);
    }

    public Polynom(int dim, int f) {
        this.dim = dim;
        this.f = f;
        addRandomCoefficients(f);
        generatePolynomY(dim);
    }

    public Polynom(int dim, ArrayList<Integer> coefficients) {
        this.dim = dim;
        this.coefficients = coefficients;
        generatePolynomY(dim);
    }

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim % f;
        while (coefficients.size() != dim % f) {
            coefficients.add((int) (Math.random() * (f-1)));
        }
    }

    public String getCoefficients() {
        return String.valueOf(coefficients);
    }

    public void addRandomCoefficients() {
        for(int i = 1; i < dim; i++) {
            coefficients.add((int) Math.random());
        }
    }

    public void addRandomCoefficients(int f) {
        for(int i = 1; i < dim; i++) {
            if (i == dim-1) { // Проверка, чтобы k-1 коэффициент не был равен 0
                while (true) {
                    int rand = (int) (Math.random() * (f - 1));
                    if (rand != 0) {
                        coefficients.add(rand);
                        break;
                    }
                }
            }
            else
                coefficients.add((int) (Math.random() * (f-1)));
        }
    }

    public void set0Coefficient(int a) {
        if (coefficients.size() == dim) {
            coefficients.remove(0);
        }
        coefficients.add(0,a);
    }

    public void printCoefficients() {
        for (int i : coefficients) {
            if (coefficients.indexOf(i) == coefficients.size()-1)
                System.out.println("a" + coefficients.indexOf(i) + " = " + i + ".");
            else
                System.out.print("a" + coefficients.indexOf(i) + " = " + i + ", ");
        }
    }

    public void generatePolynomY(int dim) {
        polynomY.add(" ");
        polynomY.add("y");
        for (int i = 2; i < dim; i++) {
            polynomY.add("y^" + i);
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("");
        for(int i = 0; i < dim; i++) {
            if (i == dim-1) {
                s.append(coefficients.get(i)).append(polynomY.get(i));
            }
            else
                s.append(coefficients.get(i)).append(polynomY.get(i)).append(" + ");
        }
        return s.toString();
    }
}
