package util;

public class Direction {
    private byte horizontal;
    private byte vertical;

    public Direction() {
        this.horizontal = 0;
        this.vertical = 0;
    }

    public Direction(byte horizontal, byte vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public boolean isZero() {
        return (this.horizontal == 0 && this.vertical == 0);
    }

    public byte getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(byte horizontal) {
        this.horizontal = horizontal;
    }

    public byte getVertical() {
        return vertical;
    }

    public void setVertical(byte vertical) {
        this.vertical = vertical;
    }

    @Override
    public String toString() {
        return "Direction{" +
                "horizontal=" + horizontal +
                ", vertical=" + vertical +
                '}';
    }
}
