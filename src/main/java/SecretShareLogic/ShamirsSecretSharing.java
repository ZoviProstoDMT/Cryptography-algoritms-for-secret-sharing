package SecretShareLogic;

import java.util.ArrayList;

public class ShamirsSecretSharing extends SecretSharing {

    public void start() {
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
        super.findSecret();
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
