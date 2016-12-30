package io.dico.dicore.util.box;

public class DoubleBox extends Number {

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return (long) value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    public double value;

    public DoubleBox() {
    }

    public DoubleBox(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DoubleBox{" +
                "value=" + value +
                '}';
    }
}
