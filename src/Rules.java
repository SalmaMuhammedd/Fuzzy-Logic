import java.util.ArrayList;
import java.util.Collections;

public class Rules {
    String operator;
    ArrayList<Variable> antecedents;
    Variable consequent;
    ArrayList<FuzzySet> sets;

    public Rules() {
        this.operator = null;
        this.antecedents = new ArrayList<>();
        this.sets = new ArrayList<>();
        this.consequent = null;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public ArrayList<Variable> getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(ArrayList<Variable> antecedents) {
        this.antecedents = antecedents;
    }

    public Variable getConsequent() {
        return consequent;
    }

    public void setConsequent(Variable consequent) {
        this.consequent = consequent;
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
        } else if (this.operator.equals("or_not")) {
            sets.get(2).degreeOfMembership = Math.max(sets.get(0).degreeOfMembership, 1.0 - sets.get(1).degreeOfMembership);
        }
        return this.sets.get(2).degreeOfMembership;
    }

    @Override
    public String toString() {
        return "Rules{" +
                "operator='" + operator + '\'' +
                ", antecedents=" + antecedents +
                ", consequent=" + consequent +
                ", sets=" + sets +
                '}';
    }
}
