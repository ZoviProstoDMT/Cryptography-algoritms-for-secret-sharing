package SecretShareLogic;

import java.util.ArrayList;

public class ShamirsSecretSharing extends SecretSharing {

    @Override
    public void generate() {
        generateRandomPolynom(); // Создание случайного полинома степени k-1
        generateKeys(); // Создание ключей вида (х,у)
        showAllKeys();
        findSecret();
    }

    protected void lagrangeFunc(double X) {
        double res = 1;
        double sum = 0;

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (j != i) {
                    res *= ((X - xyKey.get(j).getX()) / (xyKey.get(i).getX() - xyKey.get(j).getX())) % f;
                }
            }
            sum += (res * xyKey.get(i).getY()) % f;
            sum %= f;
            res = 1;
        }
        while (sum < 0)
            sum += f;
        if (X == 0)
            System.out.println("Секрет: " + sum);
        else
            System.out.println("Для Х: " + X + " соответсвует значение Y: " + sum);
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

    protected void generateKeys() {
        super.generateKeys();
    }

    protected void showAllKeys() {
        super.showAllKeys();
    }

    protected void generateRandomPolynom() {
        super.generateRandomPolynom();
    }

    protected int reverseNumber(double e) {
       return super.reverseNumber(e);
    }

}
