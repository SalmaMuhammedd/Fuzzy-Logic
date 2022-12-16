import java.util.ArrayList;

public class FuzzySet {
    String name;
    String type;
    ArrayList<Integer> values;
    double degreeOfMembership;

    public FuzzySet(String name, String type) {
        this.name = name;
        this.type = type;
        this.values = new ArrayList<>();
        this.degreeOfMembership = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Integer> getValues() {
        return values;
    }

    public void addValue(int value) {
        this.values.add(value);
    }

    public void fuzzification(double x) {
        if (this.type.equals("TRAP")) {
            double x0 = values.get(0);
            double x1 = values.get(1);
            double x2 = values.get(2);
            double x3 = values.get(3);

            if (x <= x0 || x >= x3)
                this.degreeOfMembership = 0;
            else if (x >= x1 && x <= x2)
                this.degreeOfMembership = 1;
            else if (x > x0 && x < x1)
                this.degreeOfMembership = (x - x0) / (x1 - x0);
            else if (x > x2 && x < x3)
                this.degreeOfMembership = (x3 - x) / (x3 - x2);

        } else if (this.type.equals("TRI")) {
            double x0 = values.get(0);
            double x1 = values.get(1);
            double x2 = values.get(2);
            if (x <= x0 || x >= x2)
                this.degreeOfMembership = 0;
            else if (x == x1)
                this.degreeOfMembership = 1;
            else if (x > x0 && x < x1)
                this.degreeOfMembership = (x - x0) / (x1 - x0);
            else if (x > x1 && x < x2)
                this.degreeOfMembership = (x2 - x) / (x2 - x1);
        }
    }

    @Override
    public String toString() {
        return "FuzzySet{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", values=" + values +
                ", degreeOfMembership=" + degreeOfMembership +
                '}';
    }
}
