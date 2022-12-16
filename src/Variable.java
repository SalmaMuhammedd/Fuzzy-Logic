import java.util.ArrayList;

public class Variable {
    String name;
    String type;
    ArrayList<FuzzySet> fuzzySets;
    int min, max;

    public Variable(String name, String type, int min, int max) {
        this.name = name;
        this.type = type;
        this.min = min;
        this.max = max;
        this.fuzzySets = new ArrayList<>();
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

    public ArrayList<FuzzySet> getFuzzySets() {
        return fuzzySets;
    }

    public void addFuzzySet(FuzzySet fuzzySet) {
        this.fuzzySets.add(fuzzySet);
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", fuzzySets=" + fuzzySets +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
