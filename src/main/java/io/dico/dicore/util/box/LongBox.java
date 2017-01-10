package io.dico.dicore.util.box;

public class LongBox extends Number {

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    public long value;

    public LongBox() {
    }

    public LongBox(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LongBox{" +
                "value=" + value +
                '}';
    }
}
