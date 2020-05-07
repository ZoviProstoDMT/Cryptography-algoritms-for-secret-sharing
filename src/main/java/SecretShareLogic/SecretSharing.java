package SecretShareLogic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public abstract class SecretSharing {

    static Scanner sc = new Scanner(System.in);
    protected static int n = 20; // Число частей ключа
    protected static int k = 5; // Минимальный порог для восстановления ключа
    protected static int f = 727; // Простое число, модуль конечного поля
    protected static int secret = 33;
    protected static ArrayList<Point> xyKey; // Список из ключей вида (х,у)
    protected static Polynom polynom;

    // Генирируем случайно исксы без повторений, затем получаем y = f(x)
    protected void generateKeys() {
        xyKey = new ArrayList<>();
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

    protected void findSecret() {
        ArrayList<Point> points = new ArrayList<>(); // Выбранные точки для расшифровки секрета
        System.out.println("Выпишете через пробел k = " + k + " ключей, по которым будет восстановлен секрет: ");
        for (int i = 0; i < k; i ++) {
            int num = sc.nextInt();
            points.add(xyKey.get(num-1));
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
                    if (temp < 0)
                        temp += f;
                    res *= -points.get(j).getX() * reverseNumber(temp);
                    res %= f;
                }
            }
            sum += (res * points.get(i).getY());
            sum %= f;
            res = 1;
        }
        while (sum < 0)
            sum += f;

        System.out.println("Секрет: " + sum);
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

}
