package SecretShareLogic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public abstract class SecretSharing {

    static Scanner sc = new Scanner(System.in);
    protected static int n = 100; // Число частей ключа
    protected static int k = 5; // Минимальный порог для восстановления ключа
    protected static int f = 727; // Простое число, модуль конечного поля
    protected int p = 227; // Большое простое число
    protected int q = findPrimeUnder(p); // Большое простое число, которое делит p-1
    protected int g = findBaseOfOrd(q); // g имеет порядок q
    protected static int secret;
    protected static ArrayList<Point> xyKey = new ArrayList<>(); // Список из ключей вида (х,у)
    protected static Polynom polynom;

    // Генирируем случайно исксы без повторений, затем получаем y = f(x)
    protected void generateKeys() {
        HashSet<Double> set = new HashSet<>(); // Множество, заполняющееся без повторений
        while (set.size() != n) {
            int rand = (int) (Math.random() * (f - 1));
            set.add((double) rand);
        }
        for (Double d : set) {
            Point point = new Point();
            point.setX(d);
            xyKey.add(point);
        }
        for(Point point : xyKey) {
            double y = 0;
            for (int a : polynom.coefficients) {
                if (polynom.coefficients.indexOf(a) == 0)
                    y += a;
                else
                    y += a * Math.pow(point.getX(),polynom.coefficients.indexOf(a));
            }
            point.setY(y%f);
        }
    }

    protected void showAllKeys() {
        System.out.println("Пары ключей:");
        for (Point p : xyKey)
            System.out.print(p + "\t");
        System.out.println();
        System.out.println();
    }

    protected void generateRandomPolynom() {
        System.out.print("Сгенерируем случайный полином степени k-1, где k - минимальное число для восстановления секрета:\t");
        polynom = new Polynom(k,f);
        polynom.set0Coefficient(secret);
        System.out.println(polynom + "\t (mod " + f + ")");
        System.out.println();
        polynom.printCoefficients();
        System.out.println();
    }

    // Поиск обратного числа в поле Fp
    protected int reverseNumber(double e) {
        double res;
        for (int i = 1; i < f; i++) {
            res = ((i * e) - 1);
            if ((res % f) == 0)
                return i;
        }
        return 0;
    }

    protected int findOrd(int a) {
        int ord = 1;
        while ((Math.pow(a,ord))%f != 1)
            ord++;
        return ord;
    }

    protected int findBaseOfOrd(int q) {
        int res;
        for (int g = 2; g < f-1; g++) {
            double temp = Math.pow(g,q)%f;
            if (temp == 1) {
                res = g;
                return res;
            }
        }
        return 0;
    }

    protected int findBiggestPrimeUnder(int p) {
        int p1 = p - 1;
        int q = p - 2;
        while (!(isPrime(q)) && !(p1%q==0)) {
            q--;
        }
        return q;
    }

    protected int findPrimeUnder(int p) {
        int p1 = p - 1;
        int q = 11;
        while (!(isPrime(q) && (p1%q==0))) {
            q++;
        }
        return q;
    }

    protected boolean isPrime(int p) {
        for (int i=2; i<=p/2; i++) {
            int temp = p % i;
            if (temp == 0) {
                return false;
            }
        }
        return true;
    }

    public static int getSecret() {
        return secret;
    }

    public static void setSecret(int secret) {
        SecretSharing.secret = secret;
    }

    public static int getN() {
        return n;
    }

    public static void setN(int n) {
        SecretSharing.n = n;
    }

    public static int getK() {
        return k;
    }

    public static void setK(int k) {
        SecretSharing.k = k;
    }

    public static int getF() {
        return f;
    }

    public static void setF(int f) {
        SecretSharing.f = f;
    }

    public static Polynom getPolynom() {
        return polynom;
    }

    protected abstract void generate();
}
