package SecretShareLogic;

import java.util.*;

public class VerifiableSecretSharing {
    protected static int secret = 2;
    public static int p = 311; // Большое простое число
    protected static int n = 300; // Число частей ключа
    protected static int k = 6; // Минимальный порог для восстановления ключа
    public static double g = getPRoot().get(0); // g первообразный корень mod p
    public static ArrayList<Double> r = new ArrayList<>();
    public static ArrayList<Point> xyKey; // Список из ключей вида (х,у)
    public static Polynom polynom;

    public static void startMethod() {
        System.out.println("p = " + p + ", g = " + g);
        System.out.println("n = " + n + ", k = " + k + ", secret = " + secret + "\n");
        showPrimeFactors();
        generateRandomPolynom();
        generateRandKeys(false);
        showR();
        showAllKeys();
/*        КОД ДЛЯ ТЕСТОВ
        System.out.println("getPRoots, все первообразные корни (mod "+ p +"): " + getPRoot());
        System.out.println("findPrimR, все первообразные корни (mod "+ p +"): " + findPrimitiveRoot());
        System.out.print("Порядок элементов getPRoots: ");
        for(int el : getPRoot()) {
            System.out.print(el + "(" + findOrdP(el) + ") ");
        }
        System.out.println();
        System.out.print("Порядок элементов findPrimR: ");
        for(int el : findPrimitiveRoot()) {
            System.out.print(el + "(" + findOrdP(el) + ") ");
        }
        System.out.println();
        System.out.println(); */
        for(Point p : xyKey)
            System.out.print(testKey(p) + " ");
        System.out.println();
    }

    public static ArrayList<Integer> getPRoot() {
        ArrayList<Integer> roots = new ArrayList<>();
        for (long i = 0; i < p; i++) {
            if (isPRoot(i))
                roots.add((int) i);
        }
        return roots;
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
            if (set.contains(last)) // Если повтор
                return false;
            set.add(last);
        }
        return true;
    }

    public static int findPrimeUnder(int p) {
        ArrayList<Integer> primes = new ArrayList<>();
        for (int i = 2; i < p-1; i++) {
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
        double res2 = powP(g, point.getY());
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

    protected static double powPMinusOne(double base, double exponent) {
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
            Set<Double> set = new TreeSet<>(); // Множество, заполняющееся без повторений
            while (set.size() != n) {
                int rand = (int) (Math.random() * (p - 1));
                if (rand != 0)
                    set.add((double) rand);
            }
            for (Double d : set) {
                Point point = new Point();
                point.setX(d);
                xyKey.add(point);
            }
        } else {
            for (double i = 1; i < n + 1; i++) {
                Point point = new Point();
                point.setX(i);
                xyKey.add(point);
            }
            for (Point point : xyKey) {
                double y = 0;
                for (int i = 0; i < polynom.coefficients.size(); i++) {
                    double ax = (polynom.coefficients.get(i) * Math.pow(point.getX(), i));
                    y += ax;
                    y %= (p-1);
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

    public static double lagrangeFunc(double x, ArrayList<Point> points) {
        System.out.print("Выбранные точки: ");
        for (Point p : points) {
            System.out.print(p + " ");
        }
        System.out.println();

        double res = 1;
        double sum = 0;

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (j != i) {
                    double temp = points.get(i).getX() - points.get(j).getX();
                    while (temp < 0)
                        temp += p;
                    res *= (x-points.get(j).getX()) * reverseNumber(temp);
                    res %= p;
                }
            }
            sum += (res * points.get(i).getY());
            sum %= p;
            res = 1;
        }
        while (sum < 0)
            sum += p;

        if (x == 0)
            System.out.println("Секрет: " + sum);
        else
            System.out.println("Для X = " + x + ", значение Y = " + sum);
        System.out.println();
        return sum;
    }

    // Поиск обратного числа в поле Fp
    protected static int reverseNumber(double e) {
        double res;
        for (int i = 1; i < p; i++) {
            res = ((i * e) - 1);
            if ((res % p) == 0)
                return i;
        }
        return 0;
    }

    public static void bigTest() {
        ArrayList<Integer> newG = new ArrayList<>();
        for (Point point : xyKey) {
            for (int gg = 2; gg < p; gg++) {
                double res1 = powP(gg, point.getY());
                double res2 = 1;
                for (int i = 0; i < polynom.coefficients.size(); i++) {
                    res2 *= powP(gg, powP(polynom.coefficients.get(i), powP(point.getX(),i)));
                    res2 %= p;
                }
                if (res1 == res2)
                    newG.add(gg);
            }
            System.out.println("Key " + point + ": " + newG);
        }
    }

    public static void littleTest() {
        for (int i = 1; i < p; i++) {
            boolean test = true;
            System.out.println();
            g = i;
            generateR();

            for (Point p : xyKey) {
                    test &= testKey(p);
                    System.out.print(testKey(p) + " ");
                    if (!test)
                        break;
                }
                if (test)
                    System.out.println("ВСЕ КЛЮЧИ ПОДОШЛИ, i = " + i + ", G = " + i);
        }
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