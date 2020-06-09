package SecretShareLogic;

import java.math.BigInteger;

public class Key {

    public long x;
    public BigInteger y;

    public Key(long x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public Key() {
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public BigInteger getY() {
        return y;
    }

    public void setY(BigInteger y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + " , " + y + ")";
    }
}