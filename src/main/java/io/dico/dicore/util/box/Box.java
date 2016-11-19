package io.dico.dicore.util.box;

public class Box<T> {

    public T value;

    public Box() {
    }

    public Box(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Box(" + String.valueOf(value) + ')';
    }

}
