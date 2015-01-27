package utils;

/**
 * This is a combo item, can return some object T from JCombobox
 */
public class ComboItem<T> {
    private String key;
    private T value;

    /**
     * Constuctor
     *
     * @param key   Key will be visible as item in JCombobox
     * @param value
     */
    public ComboItem(String key, T value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key;
    }

    /**
     * @return Key data
     */
    public String getKey() {
        return key;
    }

    /**
     * @return The value incapsulated in
     */
    public T getValue() {
        return value;
    }
}
