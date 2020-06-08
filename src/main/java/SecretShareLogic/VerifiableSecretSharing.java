package SecretShareLogic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

public class VerifiableSecretSharing {
    protected static int secret = 18;
    public static int p = 10013; // Большое простое число
    protected static int n = 1200; // Число частей ключа
    protected static int k = 150; // Минимальный порог для восстановления ключа
    public static double g = getMinimalPRoot(); // g первообразный корень mod p
    public static ArrayList<Double> r = new ArrayList<>();
    public static ArrayList<Point> xyKey; // Список из ключей вида (х,у)
    public static Polynom polynom;

    public static void startTestMethod() {
        Date start = new Date();
        System.out.println("p = " + p + ", g = " + g);
        System.out.println("n = " + n + ", k = " + k + ", secret = " + secret + "\n");
        showPrimeFactors();
        generateRandomPolynom();
        generateRandKeys(false);
        showR();
        showAllKeys();
        lagrangeFunc(0,xyKey);
        testAllKeys(xyKey);
        testLagrangeFunc(xyKey);
        Date stop = new Date();
        System.out.println("Program worked " + (stop.getTime() - start.getTime())/1000 + " с " + (stop.getTime() - start.getTime())%1000 + " мс" );
    }

    public static void startMethod() {
        generateRandomPolynom();
        generateRandKeys(false);
        generateR();
    }

    public static ArrayList<Integer> getPRoot() {
        ArrayList<Integer> roots = new ArrayList<>();
        for (long i = 0; i < p; i++) {
            if (isPRoot(i))
                roots.add((int) i);
        }
        return roots;
    }

    public static long getMinimalPRoot() {
        for (long i = 0; i < p; i++) {
            if (isPRoot(i))
                return i;
        }
        return 0;
    }

    public static TreeMap<Integer, Integer> findPrimeFactors(int a) {
        TreeMap<Integer, Integer> primes = new TreeMap<>();
        if (isPrime(a) && a>2) {
            a -= 1;
        }
        int temp = a;
        int i = 2;
        while (i <= Math.sqrt(a) && temp > 1) {
            if (isPrime(i) && (temp % i == 0)) {
                if (!primes.containsKey(i))
                    primes.put(i, 1);
                else
                    primes.merge(i, primes.get(i), (prime, count) -> count + 1);
                temp = temp / i;
            } else i++;
        }
        if (temp != 1)
            primes.put(temp, 1);
        return primes;
    }

    public static void showPrimeFactors() {
        System.out.print(p-1 + " = ");
        findPrimeFactors(p).forEach((key,value) -> System.out.print(key + "^" + value + " * "));
        System.out.println();
        System.out.println();
    }

    public static ArrayList<Integer> findPrimitiveRoot() {
        ArrayList<Integer> roots = new ArrayList<>();
        ArrayList<Integer> list = new ArrayList<>();
        findPrimeFactors(p).forEach((a,b) -> list.add((p-1)/a));
        int i = 2;
        while (i < p) {
            if (isPRoot2(i,list))
                roots.add(i);
            i++;
        }
        return roots;
    }

    public static boolean isPRoot2(int i, ArrayList<Integer> list) {
        boolean res = true;
        for (int elem : list) {
            boolean temp = powP(i, elem) != 1;
            res &= temp;
        }
        return res;
    }

    public static boolean isPRoot(long a) {
        if (a == 0 || a == 1)
            return false;
        long last = 1;

        Set<Long> set = new HashSet<>();
        for (long i = 0; i < p - 1; i++) {
            last = (last * a) % p;
            if (set.contains(last))
                return false;
            set.add(last);
        }
        return true;
    }

    public static int findPrimeUnder(int p) {
        ArrayList<Integer> primes = new ArrayList<>();
        for (int i = 2; i < (p-1); i++) {
            if (isPrime(i) && ((p-1)%i == 0))
                primes.add(i);
        }
        return primes.get(primes.size()-1);
    }

    public static boolean isPrime(int p) {
        for (int i=2; i<=p/2; i++) {
            int temp = p % i;
            if (temp == 0) {
                return false;
            }
        }
        return true;
    }

    protected static void generateR() {
        for (int i = 0; i < polynom.coefficients.size(); i++) {
            double temp = powP(g, polynom.coefficients.get(i));
            r.add(temp);
        }
    }

    protected static void showR() {
        generateR();
        for (int i = 0; i < r.size(); i++) {
        if (i == k-1)
            System.out.print("r" + i + " = " + r.get(i) + ".");
        else
            System.out.print("r" + i + " = " + r.get(i) + ", ");
    }
        System.out.println();
        System.out.println();
    }

    protected static boolean testSecret(int secret) {
        double res1 = powP(g,secret);
        double res2 = r.get(0);
        return res1 == res2;
    }

    public static boolean testKey(Point point) {
        double res1 = 1;
        for (int i = 0; i < r.size(); i++) {
            double temp = powPMinusOne(point.getX(),i);
            res1 *= powP(r.get(i),temp);
            res1 %= p;
        }
        BigInteger exp = point.getY().mod(BigInteger.valueOf(p-1));
        double res2 = powP(g, exp);
        return res2 == res1;
    }

    protected static double powP(double base, double exponent) {
        double result = 1;
        for (int i = 0; i < exponent; i++) {
            result *= base;
            result %= p;
        }
        return result % p;
    }

    protected static long powP(double base, BigInteger exponent) {
        long result = 1;
        BigInteger one = new BigInteger("1");
        BigInteger i = new BigInteger("0");
        while (i.compareTo(exponent) < 0) {
            result *= base;
            result %= p;
            i = i.add(one);
        }
        return result % p;
    }

    protected static BigInteger bigPow(long base, long exponent) {
        BigInteger result = new BigInteger("1");
        long i = 0;
        while (i < exponent) {
            result = result.multiply(BigInteger.valueOf(base));
            i++;
        }
        return result;
    }

    protected static double powPMinusOne(long base, long exponent) {
        double result = 1;
        for (int i = 0; i < exponent; i++) {
            result *= base;
            result %= (p - 1);
        }
        return result % (p - 1);
    }

    protected static int findOrdP(int a) {
        for (int i = 2; i < p; i++) {
            if (powP(a,i) == 1)
                return i;
        }
        return 999999999;
    }


    public static ArrayList<Integer> findBaseOfOrd(int q) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 2; i < p; i++) {
            double temp = powP(i,q);
            if (temp == 1) {
                res.add(i);
            }
        }
        return res;
    }

    public static void generateRandKeys(boolean random) {
        xyKey = new ArrayList<>();
        if (random) {
            Set<Long> set = new TreeSet<>(); // Множество, заполняющееся без повторений
            while (set.size() != n) {
                long rand = (long) (Math.random() * (p - 1));
                if (rand != 0)
                    set.add(rand);
            }
            for (long d : set) {
                Point point = new Point();
                point.setX(d);
                xyKey.add(point);
            }
        } else {
            for (long i = 1; i < n + 1; i++) {
                Point point = new Point();
                point.setX(i);
                xyKey.add(point);
            }
            for (Point point : xyKey) {
                BigInteger y = new BigInteger("0");
                for (int i = 0; i < polynom.coefficients.size(); i++) {
                    BigInteger ax = BigInteger.valueOf(polynom.coefficients.get(i).longValue());
                    ax = ax.multiply(bigPow(point.getX(), i));
                    y = y.add(ax);
//                    y %= (p-1);
                }
                point.setY(y);
            }
        }
    }

    protected static void showAllKeys() {
        System.out.println("Пары ключей (" + n + " штук):");
        for (Point p : xyKey)
            System.out.print(p + "   ");
        System.out.println();
        System.out.println();
    }

    protected static void generateRandomPolynom() {
        System.out.print("Полином степени k-1:\t");
        polynom = new Polynom(k,p);
        System.out.println(polynom + "\t (mod " + p + ")");
        System.out.println();
        polynom.printCoefficients();
        System.out.println();
    }

    public static BigInteger lagrangeFunc(long x, ArrayList<Point> points) {
        BigDecimal res = BigDecimal.valueOf(1);
        BigDecimal sum = BigDecimal.valueOf(0);

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (j != i) {
                    long temp = points.get(i).getX() - points.get(j).getX();
                    long temp2 = x - points.get(j).getX();
                    BigDecimal temp3 = BigDecimal.valueOf(temp2).divide(BigDecimal.valueOf(temp), 4*VerifiableSecretSharing.k, RoundingMode.HALF_UP);
                    res = res.multiply(temp3);
                }
            }
            sum = sum.add(res.multiply(new BigDecimal(points.get(i).getY())));
            res = BigDecimal.valueOf(1);
        }
        for (int i = 9; i >= 0; i--)
            sum = sum.setScale(i, RoundingMode.HALF_UP);
        if (x == 0)
            System.out.println("Секрет: " + sum);
        return sum.toBigInteger();
    }

    // Поиск обратного числа в поле Fp
    protected static int reverseNumber(long e) {
        long res;
        for (int i = 1; i < (p-1); i++) {
            res = ((i * e) - 1);
            if ((res % (p-1)) == 0)
                return i;
        }
        return 99999;
    }

    public static void testAllKeys(ArrayList<Point> points) {
        boolean result = true;
        int counter = 0;
        for (Point p : points) {
            boolean temp = testKey(p);
            if (!temp)
                counter++;
            result &= temp;
        }
        if (result) {
            System.out.println("TRUE | Все " + points.size() + " штук ключей прошли проверку!");
        }
        else
            System.out.println("FALSE | " + counter + " из " + points.size() + " не прошли проверку!" );
        System.out.println();
    }

    public static void testLagrangeFunc(ArrayList<Point> points) {
        BigInteger y;
        boolean res = true;
        for (int i = 0; i < n; i++) {
            y = xyKey.get(i).getY();
            BigInteger testY = lagrangeFunc(xyKey.get(i).getX(),points);
            res &= (y.compareTo(testY) == 0);
/*            if (y.compareTo(testY) == 0)
                System.out.println("TRUE | Для " + xyKey.get(i) + " получили верный Y = " + testY);
            else
                System.out.println("FALSE | Для " + xyKey.get(i) + " получили неверный Y = " + testY);*/
        }
        System.out.println(res);
    }

    public static int getSecret() {
        return secret;
    }

    public static void setSecret(int secret) {
        VerifiableSecretSharing.secret = secret;
    }

    public static int getP() {
        return p;
    }

    public static void setP(int p) {
        VerifiableSecretSharing.p = p;
    }

    public static int getN() {
        return n;
    }

    public static void setN(int n) {
        VerifiableSecretSharing.n = n;
    }

    public static int getK() {
        return k;
    }

    public static void setK(int k) {
        VerifiableSecretSharing.k = k;
    }
}