package SecretShareLogic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

public class VerifiableSecretSharing {
    protected static int secret; // Шифруемый секрет
    public static int p; // Большое простое число
    protected static int n; // Число частей ключа
    protected static int k; // Минимальный порог для восстановления ключа
    public static double g; // Первообразный корень mod p
    public static ArrayList<Double> r; // Список элементов r
    public static ArrayList<Key> xyKey; // Список из ключей вида (х,у)
    public static Polynom polynom; // Многочлен

    public static void startMethod() {
        generateRandomPolynom();
        generateRandKeys(false);
        generateR();
    }

    public static long getMinimalPRoot() {
        for (long i = 0; i < p; i++) {
            if (isPRoot(i))
                return i;
        }
        return 0;
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

    protected static void generateR() {
        r = new ArrayList<>();
        for (int i = 0; i < polynom.coefficients.size(); i++) {
            double temp = powP(g, polynom.coefficients.get(i));
            r.add(temp);
        }
    }

    public static boolean testKey(Key key) {
        double res1 = 1;
        for (int i = 0; i < r.size(); i++) {
            double temp = powPMinusOne(key.getX(),i);
            res1 *= powP(r.get(i),temp);
            res1 %= p;
        }
        BigInteger exp = key.getY().mod(BigInteger.valueOf(p-1));
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
                Key key = new Key();
                key.setX(d);
                xyKey.add(key);
            }
        } else {
            for (long i = 1; i < n + 1; i++) {
                Key key = new Key();
                key.setX(i);
                xyKey.add(key);
            }
            for (Key key : xyKey) {
                BigInteger y = new BigInteger("0");
                for (int i = 0; i < polynom.coefficients.size(); i++) {
                    BigInteger ax = BigInteger.valueOf(polynom.coefficients.get(i).longValue());
                    ax = ax.multiply(bigPow(key.getX(), i));
                    y = y.add(ax);
                }
                key.setY(y);
            }
        }
    }

    protected static void generateRandomPolynom() {
        System.out.print("Полином степени k-1:\t");
        polynom = new Polynom(k,p);
        System.out.println(polynom + "\t (mod " + p + ")");
        System.out.println();
        polynom.printCoefficients();
        System.out.println();
    }

    public static BigInteger lagrangeFunc(long x, ArrayList<Key> keys) {
        BigDecimal res = BigDecimal.valueOf(1);
        BigDecimal sum = BigDecimal.valueOf(0);

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (j != i) {
                    long temp = keys.get(i).getX() - keys.get(j).getX();
                    long temp2 = x - keys.get(j).getX();
                    BigDecimal temp3 = BigDecimal.valueOf(temp2).divide(BigDecimal.valueOf(temp), 4*VerifiableSecretSharing.k, RoundingMode.HALF_UP);
                    res = res.multiply(temp3);
                }
            }
            sum = sum.add(res.multiply(new BigDecimal(keys.get(i).getY())));
            res = BigDecimal.valueOf(1);
        }
        for (int i = 9; i >= 0; i--)
            sum = sum.setScale(i, RoundingMode.HALF_UP);
        if (x == 0)
            System.out.println("Секрет: " + sum);
        return sum.toBigInteger();
    }

    public static int getSecret() {
        return secret;
    }

    public static void setSecret(int secret) {
        VerifiableSecretSharing.secret = secret;
    }

    public static void setP(int p) {
        VerifiableSecretSharing.p = p;
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