package util;

public class Position {
    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isSamePosition(Position position) {
        if(position == null) return false;
        if (this.x == position.getX() && this.y == position.getY()) {
            return true;
        }
        System.out.println("this = " + this);
        System.out.println("position = " + position);
        return false;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
