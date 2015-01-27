package engine;

/**
 * Generic object filter
 */
public interface ObjectFilter<T> {
    boolean canAdd(T obj);
}
