package engine.sorting;

/**
 * Sorting interface
 */
public interface Sorting {
    /**
     * Comparable criteria
     *
     * @return Comparable Object
     */
    public Comparable getSortElement();

    /**
     * Setts sorting element
     *
     * @param sortingby sorted element impl. Comparable
     */
    public void setSortElement(String sortingby);
}
