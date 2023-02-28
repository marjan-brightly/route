
package rs.brightly.route.dto;

import java.util.ArrayList;
import java.util.List;

public class Country {
    private String cca3;
    private List<String> borders = new ArrayList<String>();

    public String getCca3() {
        return cca3;
    }

    public void setCca3(String cca3) {
        this.cca3 = cca3;
    }

    public List<String> getBorders() {
        return borders;
    }

    public void setBorders(List<String> borders) {
        this.borders = borders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (cca3 != null ? !cca3.equals(country.cca3) : country.cca3 != null) return false;
        return borders != null ? borders.equals(country.borders) : country.borders == null;
    }

    @Override
    public int hashCode() {
        int result = cca3 != null ? cca3.hashCode() : 0;
        result = 31 * result + (borders != null ? borders.hashCode() : 0);
        return result;
    }
}
