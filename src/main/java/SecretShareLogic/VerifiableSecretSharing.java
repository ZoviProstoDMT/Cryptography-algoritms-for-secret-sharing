package SecretShareLogic;

import java.util.*;

public class VerifiableSecretSharing {
    static Scanner sc = new Scanner(System.in);
    protected static int n = 10; // Число частей ключа
    protected static int k = 5; // Минимальный порог для восстановления ключа
    protected static int secret = 5;
    protected static int p = 1117; // Большое простое число
    protected static int q = findPrimeUnder(p);
    protected int g = findPrimitiveRoot(); // g первообразный корень mod p
    protected ArrayList<Double> r;
    public static ArrayList<Point> xyKey; // Список из ключей вида (х,у)
    public static Polynom polynom;
    public static TreeMap<Integer, Integer> primes;

    public void startMethod() {
        getPRoot();
        System.out.println("p = " + p + ", q = " + q + ", g = " + g);
        System.out.println("n = " + n + ", k = " + k + ", secret = " + secret + "\n");
        generateRandomPolynom();
        generateR();
        generateKeys();
        showAllKeys();
        System.out.println("BaseOfOrd = " + findBaseOfOrd(q));
        System.out.println("g^q = " + Math.pow(g,q)%p + " = 1 ?");

        while (true) {
            double test1 = sc.nextDouble();
      //      lagrangeFunc(test1);
            double test2 = sc.nextDouble();
            Point qwe = new Point(test1,test2);
            System.out.println(qwe);
            System.out.println(testKey(qwe));
        }
    }

    public static void getPRoot() {
        ArrayList<Integer> roots = new ArrayList<>();
        for (long i = 0; i < p; i++) {
            if (isPRoot(i))
                roots.add((int) i);
        }
        System.out.println("Корни через getPRoot: " + roots);
        for (int el : roots)
            if (Math.pow(el,q) % p == 1)
                System.out.println(el + "^q = 1. Имеет порядок q!");
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
        System.out.println("Корни через findPrimitiveRoot: " + roots);
        for (int el : roots)
            if (Math.pow(el,q) % p == 1) {
                res = el;
                System.out.println(el + "^q = 1. Имеет порядок q!");
            }
        return res;
    }

    public static boolean isPRoot2(int i, ArrayList<Integer> list) {
        boolean res = true;
        for (int elem : list) {
            res &= Math.pow(i, elem) % p != 1;
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

    protected void generateR() {
        r = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            double temp = Math.pow(g,polynom.coefficients.get(i)) % p;
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
        double res1 = Math.pow(g,secret)%p;
        double res2 = r.get(0);
        return res1 == res2;
    }

    protected boolean testKey(Point point) {
        double res1 = r.get(0);
        for (int i = 1; i < r.size(); i++) {
            res1 *= Math.pow(r.get(i), (Math.pow(point.getX(),i))%p) % p;
            res1 %= p;
        }
        double res2 = Math.pow(g, point.getY()) %p;
        return res2 == res1;
    }

    protected int findOrd(int a) {
        int ord = 1;
        while ((Math.pow(a,ord))%p != 1)
            ord++;
        return ord;
    }

    protected int findBaseOfOrd(int q) {
        int res;
        for (int i = 2; i < p; i++) {
            double temp = Math.pow(i,q)%p;
            if (temp == 1) {
                res = i;
                return res;
            }
        }
        return 0;
    }

    protected void generateKeys() {
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
                    double ax = polynom.coefficients.get(i) * Math.pow(point.getX(),i)%q;
                    y += ax;
                    y %= q;
                }
            }
            point.setY(y%q);
        }
    }

    protected void showAllKeys() {
        System.out.println("Пары ключей:");
        for (Point p : xyKey)
            System.out.print(p + "   ");
        System.out.println();
        System.out.println();
    }

    protected void generateRandomPolynom() {
        System.out.print("Сгенерируем случайный полином степени k-1, где k - минимальное число для восстановления секрета:\t");
        polynom = new Polynom(k,q);
        polynom.set0Coefficient(secret);
        System.out.println(polynom + "\t (mod " + q + ")");
        System.out.println();
        polynom.printCoefficients();
        System.out.println();
    }

    protected double lagrangeFunc(double x) {
        ArrayList<Point> points = new ArrayList<>(); // Выбранные точки для расшифровки секрета
        if (x == 0)
            System.out.println("Выпишете через пробел k = " + k + " ключей, по которым будет восстановлен секрет: ");
        else
            System.out.println("Выпишете через пробел k = " + k + " ключей, по которым будет восстановлено значение для Х = " + x);

        for (int i = 0; i < k; i++) {
            int num = sc.nextInt();
            points.add(xyKey.get(num - 1));
        }
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
    protected int reverseNumber(double e) {
        double res;
        for (int i = 1; i < q; i++) {
            res = ((i * e) - 1);
            if ((res % q) == 0)
                return i;
        }
        return 0;
    }
}
