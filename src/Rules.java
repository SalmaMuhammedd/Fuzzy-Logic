import java.util.ArrayList;
import java.util.Collections;

public class Rules {
    String operator;
    ArrayList<Variable> variables;
    ArrayList<FuzzySet> sets;

    public Rules() {
        this.operator = null;
        this.variables = new ArrayList<>();
        this.sets = new ArrayList<>();

    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<Variable> antecedents) {
        this.variables = antecedents;
    }

    public ArrayList<FuzzySet> getSets() {
        return sets;
    }

    public void setSets(ArrayList<FuzzySet> sets) {
        this.sets = sets;
    }

    public double inference() {
        if (this.operator.equals("and")) {
            sets.get(2).degreeOfMembership = Math.min(sets.get(0).degreeOfMembership, sets.get(1).degreeOfMembership);
        } else if (this.operator.equals("or")) {
            sets.get(2).degreeOfMembership = Math.max(sets.get(0).degreeOfMembership, sets.get(1).degreeOfMembership);
        } else if (this.operator.equals("and_not")) {
            sets.get(2).degreeOfMembership = Math.min(sets.get(0).degreeOfMembership, 1.0 - sets.get(1).degreeOfMembership);
        }else if (this.operator.equals("or_not")) {
            sets.get(2).degreeOfMembership = Math.max(sets.get(0).degreeOfMembership, 1.0 - sets.get(1).degreeOfMembership);
        }
        return this.sets.get(2).degreeOfMembership;
    }

    @Override
    public String toString() {
        return "Rules{" +
                "operator='" + operator + '\'' +
                ", variables=" + variables +
                ", sets=" + sets +
                '}';
    }
}
