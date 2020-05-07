package SecretShareLogic;

import java.util.ArrayList;

public class FeldmanShamirsSecretSharing extends SecretSharing {

    protected int p = 227; // Большое простое число
    protected int q = findPrimeUnder(p); // Большое простое число, которое делит p-1
    protected int g = findBaseOfOrd(q); // g имеет порядок q
    protected ArrayList<Integer> r = new ArrayList<>();

    public void start() {
        f = q;
        System.out.println(p + " = p, " + q + " = q, " + g + " = g.");
        generateRandomPolynom(); // Создание случайного полинома степени k-1
        generateKeys(); // Создание ключей вида (х,у)
        showAllKeys();
        generateR();
        while (true) {
            double test1 = sc.nextDouble();
            double test2 = sc.nextDouble();
            Point qwe = new Point(test1,test2);
            System.out.println(testKey(qwe));
        }
        //findSecret();
    }

    protected void generateR() {
        for (int i = 0; i < k; i++) {
            r.add((int) Math.pow(g,polynom.coefficients.get(i))%p);
        }
    }

    protected boolean testKey(Point p) {
        int sum = r.get(0);
        for (int i = 1; i < r.size(); i++)
            sum += Math.pow(r.get(i),p.getX())%q;
        return Math.pow(g, p.getY()) % q == sum;
    }

    protected int findOrd(int a) {
        int ord = 1;
        while ((Math.pow(a,ord))%p != 1)
            ord++;
        return ord;
    }

    protected int findBaseOfOrd(int q) {
        int res;
        for (int i = 2; i < q; i++) {
            double temp = Math.pow(i,q)%p;
            if (temp == 1) {
                res = i;
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

    protected void generateKeys() {
        super.generateKeys();
    }

    protected void showAllKeys() {
        super.showAllKeys();
    }

    public void generateRandomPolynom() {
        super.generateRandomPolynom();
    }

    public int reverseNumber(double e) {
        return super.reverseNumber(e);
    }
}
