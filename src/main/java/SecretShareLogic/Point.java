package SecretShareLogic;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Point {

    public Double x;
    public Double y;

    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + " , " + y + ")";
    }
}
