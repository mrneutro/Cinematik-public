package engine.discount;

/**
 * Disounting interface for abstract factory classes
 */
public interface AbstractDiscount {
    /**
     * Returns discounted price of ticket
     *
     * @param regularPrice regular ticket price without discounting
     * @return discounted price
     */
    double getPrice(double regularPrice);

    /**
     * Naming of applied discount
     *
     * @return Discount name
     */
    String getName();

    /**
     * Percentage of applied discount
     *
     * @return Percentage discount
     */
    double getPercentage();
}
