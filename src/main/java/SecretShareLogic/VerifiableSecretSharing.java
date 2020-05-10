package SecretShareLogic;

import java.util.*;

public class VerifiableSecretSharing {
    static Scanner sc = new Scanner(System.in);
    protected static int n = 10; // Число частей ключа
    protected static int k = 5; // Минимальный порог для восстановления ключа
    protected static int secret = 9;
    protected int p = 307; // Большое простое число
    protected int q = findPrimeUnder(p);
    protected int g = findBaseOfOrd(q); // g первообразный корень mod p
    protected ArrayList<Double> r;
    protected static ArrayList<Point> xyKey; // Список из ключей вида (х,у)
    public static Polynom polynom;

    public void startMethod() {
        System.out.println("p = " + p + ", q = " + q + ", g = " + g);
        System.out.println("n = " + n + ", k = " + k + ", secret = " + secret + "\n");
        generateRandomPolynom();
        generateR();
        generateKeys();
        showAllKeys();

//        while (true) {
//            double test1 = sc.nextDouble();
//            lagrangeFunc(test1);
//            double test2 = sc.nextDouble();
//            Point qwe = new Point(test1,test2);
//            System.out.println(qwe);
//            System.out.println(testKey(qwe));
//        }
    }

    public static long getPRoot(long p) {
        for (long i = 0; i < p; i++)
            if (isPRoot(p, i))
                return i;
        return 0;
    }

    public static boolean isPRoot(long p, long a) {
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

    protected int findPrimeUnder(int p) {
        ArrayList<Integer> primes = new ArrayList<>();
        for (int i = 2; i < p-1; i++) {
            if (isPrime(i) && ((p-1)%i == 0))
                primes.add(i);
        }
        return primes.get(primes.size()-1);
    }

    protected int findPrimitiveRoot(int p) {
        return findBaseOfOrd(p);
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
        res2 = g;
        for (int i = 0; i < point.getY(); i++) {
            g *= g;
            g %= p;
        }
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
