package engine.accounts;

import java.io.Serializable;

/**
 * Created by Neutro on 15/12/2014.
 */
public class EmployHolder implements Serializable, Cloneable {
    private static final String[] values = {"Studente", "Pensionato", "Lavori socialmente utili", "Altro"};
    private IntEmploy selectedEmploy;
    private int selected;

    public EmployHolder(int selected) {
        switch (selected) {
            case 0:
                selectedEmploy = IntEmploy.STUDENT;
                break;
            case 1:
                selectedEmploy = IntEmploy.PENSIONER;
                break;
            case 2:
                selectedEmploy = IntEmploy.COMMWORK;
                break;
            case 3:
                selectedEmploy = IntEmploy.OTHER;
                break;
            default:
                throw new IllegalArgumentException("Valore massimo 3 ricevuto: " + selected);
        }
        this.selected = selected;
    }

    public static String[] getValues() {
        return values;
    }

    public String getValue() {
        return values[selected];
    }

    @Override
    protected EmployHolder clone() {
        EmployHolder clone = null;
        try {
            clone = (EmployHolder) super.clone();
        } catch (CloneNotSupportedException ignore) {
            //NOP
        }
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o.getClass() == getClass())) return false;

        EmployHolder that = (EmployHolder) o;

        if (selected != that.selected) return false;
        if (selectedEmploy != that.selectedEmploy) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = selectedEmploy.hashCode();
        result = 31 * result + selected;
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "selectedEmploy='" + getValue() +
                "'}";
    }

    public static enum IntEmploy {
        STUDENT, PENSIONER, COMMWORK, OTHER
    }
}
