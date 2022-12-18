import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Files {
    public static ArrayList<Variable> variables = new ArrayList<>();
    public static ArrayList<Rules> rules = new ArrayList<>();

    public static void splittingStringRule(String ruleStr) {
        Rules rule = new Rules();
        String[] arrOfStr = ruleStr.split(" ");
        if(arrOfStr.length != 8){
            System.out.println("Please follow the format of the rule");
            return;
        }
        rule.operator = arrOfStr[2];
        //var0 set0 op var1 set1 => var2 set2
        //0      1   2  3    4   5   6    7
        for (int i = 0; i < arrOfStr.length; i += 3) {
            for (Variable var : variables) {
                if (arrOfStr[i].equals(var.getName())) {
                    rule.variables.add(var);
                    for (FuzzySet set : var.fuzzySets) {
                        if (arrOfStr[i + 1].equals(set.getName()))
                            rule.sets.add(set);
                    }
                }
            }
        }
        rules.add(rule);
    }
    public static void main(String[] args) throws IOException {
        FileWriter fileWriter = new FileWriter("output.txt", true);
        File file = new File("input.txt");
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
            String choice = scan.nextLine();
            if (choice.equalsIgnoreCase("close"))
                break;
            switch (choice) {
                case "1":
                    while (true) {
                        String var = scan.nextLine();
                        if (var.equals("x"))
                            break;
                        String[] arrOfStr = var.split("[\\[ , \\]]+");

                        Variable variable = new Variable(arrOfStr[0], arrOfStr[1],
                                Integer.parseInt(arrOfStr[2]), Integer.parseInt(arrOfStr[3]));
                        variables.add(variable);
                    }
                    break;

                case "2":
                    String varName = scan.nextLine();
                    Variable currentVariable = null;
                    for (Variable var : variables) {
                        if (var.name.equals(varName))
                            currentVariable = var;
                    }
                    if (currentVariable != null) {
                        while (true) {
                            String fuzzy = scan.nextLine();
                            if (fuzzy.equals("x"))
                                break;
                            String[] arrOfStr = fuzzy.split(" ");
                            FuzzySet fuzzySet = new FuzzySet(arrOfStr[0], arrOfStr[1]);
                            for (int i = 2; i < arrOfStr.length; i++) {
                                fuzzySet.addValue(Integer.parseInt(arrOfStr[i]));
                            }
                            //centroid for output variables
                            if (currentVariable.getType().equals("OUT")) {
                                if (fuzzySet.type.equals("TRI")) {
                                    fuzzySet.centroid = (fuzzySet.values.get(0) + fuzzySet.values.get(1) + fuzzySet.values.get(2)) / 3.0;
                                } else if (fuzzySet.type.equals("TRAP")) {
                                    fuzzySet.centroid = (fuzzySet.values.get(1) + fuzzySet.values.get(2)) / 2.0;
                                }
                            }
                            currentVariable.addFuzzySet(fuzzySet);
                        }
                    }
                    break;

                case "3":
                    while (true) {
                        String rule = scan.nextLine();
                        if (rule.equals("x"))
                            break;
                        splittingStringRule(rule);
                    }
                    break;

                case "4":
                    ArrayList<Double> outputMemberships = new ArrayList<>();
                    for (Variable var : variables) {
                        if (var.getType().equals("IN")) {
                            double crisp = scan.nextDouble();
                            for (FuzzySet fuzzySet1 : var.fuzzySets) {
                                fuzzySet1.fuzzification(crisp);
                            }
                        }
                    }

                    fileWriter.write("Running the simulation...\n");
                    fileWriter.write("Fuzzification => done\n");

                    for (Rules rule : rules) {
                        outputMemberships.add(rule.inference());
                    }

                    for (Variable var : variables) {
                        if (var.getType().equals("OUT")) {
                            for (FuzzySet fuzzySet : var.fuzzySets) {
                                double max = -1;
                                for (int i = 0; i < rules.size(); i++) {
                                    //choosing the max membership for each rule to avoid rules with the same RHS
                                    if (rules.get(i).sets.get(2).getName().equals(fuzzySet.name)) {
                                        if (outputMemberships.get(i) > max) //outputMembership's index = rules' index
                                            max = outputMemberships.get(i);
                                    }
                                }
                                fuzzySet.degreeOfMembership = max;
                            }
                        }
                    }
                    fileWriter.write("Inference => done\n");
                    double crispOutput = 0;
                    String variableName = "", setName = "";
                    ArrayList<String> outputNames = new ArrayList<>();
                    ArrayList<String> setNames = new ArrayList<>();
                    ArrayList<Double> crispOutputs = new ArrayList<>();
                    double max = -1;
                    //defuzzification
                    for (Variable var : variables) {
                        double numeratorSum = 0, denominatorSum = 0;
                        if (var.getType().equals("OUT")) {
                            variableName = var.name;
                            for (FuzzySet fuzzySet : var.fuzzySets) {
                                numeratorSum += fuzzySet.centroid * fuzzySet.degreeOfMembership;
                                denominatorSum += fuzzySet.degreeOfMembership;
                                if (fuzzySet.degreeOfMembership >= max) {
                                    max = fuzzySet.degreeOfMembership;
                                    setName = fuzzySet.name;
                                }
                            }
                            outputNames.add(variableName);
                            setNames.add(setName);
                            crispOutputs.add(numeratorSum / denominatorSum);
                        }
                    }
                    fileWriter.write("Defuzzification => done\n");
                    for (int i = 0; i < outputNames.size(); i++) {
                        fileWriter.write("The predicted " + outputNames.get(i) + " is " + setNames.get(i) + " (" + crispOutputs.get(i) + ")");
                    }
                    fileWriter.close();
                    break;
            }
        }

    }
}
