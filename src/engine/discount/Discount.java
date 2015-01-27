package engine.discount;

import utils.Utils;

import java.io.Serializable;

/**
 * Created by Neutro on 10/01/2015.
 */
public class Discount implements AbstractDiscount, Serializable {
    private double percentage = 0;
    private String naming;

    public Discount(double percentage, String naming) {
        this.percentage = percentage;
        this.naming = naming;
    }

    @Override
    public double getPrice(double regularPrice) {
        return Utils.round(regularPrice - regularPrice * percentage / 100, 2);
    }

    @Override
    public String getName() {
        return naming;
    }

    @Override
    public double getPercentage() {
        return percentage;
    }
}
