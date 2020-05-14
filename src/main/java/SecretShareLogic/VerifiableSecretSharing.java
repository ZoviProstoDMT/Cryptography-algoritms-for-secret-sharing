package SecretShareLogic;

import java.util.*;

public class VerifiableSecretSharing {
    static Scanner sc = new Scanner(System.in);
    protected static int secret = 6;
    protected static int p = 953; // Большое простое число
    public static int q = findPrimeUnder(p);
    protected static int n = q-2; // Число частей ключа
    protected static int k = n/4; // Минимальный порог для восстановления ключа
    protected static int g = findBaseOfOrd(); // g первообразный корень mod p
    protected static ArrayList<Double> r;
    public static ArrayList<Point> xyKey; // Список из ключей вида (х,у)
    public static Polynom polynom;
    public static TreeMap<Integer, Integer> primes;

    public static void startMethod() {
        System.out.println("p = " + p + ", q = " + q + ", g = " + g);
        System.out.println("n = " + n + ", k = " + k + ", secret = " + secret + "\n");
        getPRoot();
        generateRandomPolynom();
        generateR();
        generateKeys();
        showAllKeys();
    }

    public static void getPRoot() {
        ArrayList<Integer> roots = new ArrayList<>();
        for (long i = 0; i < p; i++) {
            if (isPRoot(i))
                roots.add((int) i);
        }
        for (int el : roots)
            if (powP(el,q) == 1)
                System.out.println(el + "^q = 1. Имеет порядок q и является первообразным корнем");
    }

    public static void primeFactors(int a) {
        primes = new TreeMap<>();
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
        System.out.print(a + " = ");
        primes.forEach((key,value) -> System.out.print(key + "^" + value + " * "));
        System.out.println();
    }

    public static int findPrimitiveRoot() {
        int res = 0;
        primeFactors(p);
        ArrayList<Integer> roots = new ArrayList<>();
        ArrayList<Integer> list = new ArrayList<>();
        primes.forEach((a,b) -> list.add((p-1)/a));
        list.add((p-1) / 2);
        int i = 2;
        while (i < p) {
            if (isPRoot2(i,list))
                roots.add(i);
            i++;
        }
        System.out.println("Корни через findPrimitiveRoot: \n" + roots);
        for (int el : roots)
            if (powP(el,q) == 1) {
                res = el;
                System.out.println(el + "^q = 1. Имеет порядок q!");
            }
        return res;
    }

    public static boolean isPRoot2(int i, ArrayList<Integer> list) {
        boolean res = true;
        for (int elem : list) {
            res &= powP(i, elem) != 1;
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
        r = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            double temp = powP(g,polynom.coefficients.get(i));
            r.add(temp);
            if (i == k-1)
                System.out.print("r" + i + " = " + temp + ".");
            else
                System.out.print("r" + i + " = " + temp + ", ");
        }
        System.out.println();
        System.out.println();
    }

    protected boolean testSecret(int secret) {
        double res1 = powP(g,secret);
        double res2 = r.get(0);
        return res1 == res2;
    }

    public static boolean testKey(Point point) {
        double res1 = r.get(0);
        for (int i = 1; i < r.size(); i++) {
            res1 *= powP(r.get(i), (powP(point.getX(),i)));
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

    protected int findOrd(int a) {
        int ord = 1;
        while (powP(a,ord) != 1)
            ord++;
        return ord;
    }

    protected static int findBaseOfOrd() {
        int res;
        for (int i = 2; i < p; i++) {
            double temp = powP(i,q);
            if (temp == 1) {
                res = i;
                return res;
            }
        }
        return 0;
    }

    protected static void generateKeys() {
        xyKey = new ArrayList<>();
        Set<Double> set = new TreeSet<>(); // Множество, заполняющееся без повторений
        while (set.size() != n) {
            int rand = (int) (Math.random() * (q - 1));
            if (rand != 0)
                set.add((double) rand);
        }
        for (Double d : set) {
            Point point = new Point();
            point.setX(d);
            xyKey.add(point);
        }
        for(Point point : xyKey) {
            double y = 0;
            for (int i = 0; i < polynom.coefficients.size(); i++) {
                if (i == 0) {
                    int a0 = polynom.coefficients.get(i);
                    y += a0;
                }
                else {
                    double ax = polynom.coefficients.get(i) * powP(point.getX(),i);
                    y += ax;
                    y %= q;
                }
            }
            point.setY(y%q);
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
        System.out.print("Сгенерируем случайный полином степени k-1, где k - минимальное число для восстановления секрета:\t");
        polynom = new Polynom(k,q);
        polynom.set0Coefficient(secret);
        System.out.println(polynom + "\t (mod " + q + ")");
        System.out.println();
        polynom.printCoefficients();
        System.out.println();
    }

    public static double lagrangeFunc(double x, ArrayList<Point> points) {
        x %= x;
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
                        temp += q;
                    res *= (x-points.get(j).getX()) * reverseNumber(temp);
                    res %= q;
                }
            }
            sum += (res * points.get(i).getY());
            sum %= q;
            res = 1;
        }
        while (sum < 0)
            sum += q;

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
        for (int i = 1; i < q; i++) {
            res = ((i * e) - 1);
            if ((res % q) == 0)
                return i;
        }
        return 0;
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
