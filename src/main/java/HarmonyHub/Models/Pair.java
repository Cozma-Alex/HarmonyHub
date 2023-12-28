package HarmonyHub.Models;

import java.util.Objects;

public class Pair<E1, E2>{
    private final E1 e1;
    private final E2 e2;

    public Pair(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public E1 getLeft() {
        return e1;
    }

    public E2 getRight() {
        return e2;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "e1=" + e1 +
                ", e2=" + e2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(e1, pair.e1) && Objects.equals(e2, pair.e2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }
}
