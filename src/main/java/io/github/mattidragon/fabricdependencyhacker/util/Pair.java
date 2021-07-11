package io.github.mattidragon.fabricdependencyhacker.util;

import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord")
public final class Pair<A, B> {
    private final A first;
    private final B second;
    
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }
    
    public A first() {
        return first;
    }
    
    public B second() {
        return second;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Pair) obj;
        return Objects.equals(this.first, that.first) &&
                Objects.equals(this.second, that.second);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
    
    @Override
    public String toString() {
        return "Pair[" +
                "first=" + first + ", " +
                "second=" + second + ']';
    }
    
}
