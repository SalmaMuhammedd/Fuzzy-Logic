import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

    public static ArrayList<Variable> variables = new ArrayList<>();
    public static ArrayList<Rules> rules = new ArrayList<>();

    public static void splittingStringRule(String ruleStr) {
        Rules rule = new Rules();
        String[] arrOfStr = ruleStr.split("[ =>]+");
        for (Variable var : variables) {
            if (var.getName().equals(arrOfStr[0])) {
                rule.antecedents.add(var);
                for (FuzzySet set : var.getFuzzySets()) {
                    if (set.getName().equals(arrOfStr[1])) {
                        rule.sets.add(set);
                    }
                }
            }
            if (var.getName().equals(arrOfStr[3])) {
                rule.antecedents.add(var);
                for (FuzzySet set : var.getFuzzySets()) {
                    if (set.getName().equals(arrOfStr[4])) {
                        rule.sets.add(set);
                    }
                }
            }
            if (var.getName().equals(arrOfStr[5])) {
                rule.consequent = var;
                for (FuzzySet set : var.getFuzzySets()) {
                    if (set.getName().equals(arrOfStr[6])) {
                        rule.sets.add(set);
                    }
                }
            }
        }
        rule.operator = arrOfStr[2];

        rules.add(rule);

    }


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Scanner scan = new Scanner(System.in);
        int choice;
        String mainMenuChoice;
        while (true) {
            System.out.println
                    ("Fuzzy Logic Toolbox\n" +
                            "===================\n" +
                            "1- Create a new fuzzy system\n" +
                            "2- Quit");
            choice = scanner.nextInt();

            if (choice == 2)
                break;
            else if (choice == 1) {
                variables = new ArrayList<>();
                rules = new ArrayList<>();
                System.out.println("Enter the system’s name and a brief description:");
                String name = scanner.next();
                String description = scan.nextLine();
                while (true) {
                    System.out.println
                            ("Main Menu:\n" +
                                    "==========\n" +
                                    "1- Add variables.\n" +
                                    "2- Add fuzzy sets to an existing variable.\n" +
                                    "3- Add rules.\n" +
                                    "4- Run the simulation on crisp values.");
                    mainMenuChoice = scan.nextLine();
                    if (mainMenuChoice.equals("close"))
                        break;
                    switch (mainMenuChoice) {
                        case "1":
                            System.out.println(
                                    "Enter the variable’s name, type (IN/OUT) and range ([lower, upper]):\n" +
                                            "(Press x to finish)\n" +
                                            "--------------------------------------------------------------------");
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
                            System.out.println("Enter the variable’s name:\n" +
                                    "--------------------------");
                            String varName = scan.nextLine();
                            Variable currentVariable = null;
                            for (Variable var : variables) {
                                if (var.name.equals(varName))
                                    currentVariable = var;
                            }
                            if (currentVariable != null) {
                                System.out.println("Enter the fuzzy set name, type (TRI/TRAP) and values: (Press x to finish)\n" +
                                        "-----------------------------------------------------");
                                while (true) {
                                    String fuzzy = scan.nextLine();
                                    if (fuzzy.equals("x"))
                                        break;
                                    String[] arrOfStr = fuzzy.split(" ");
                                    FuzzySet fuzzySet = new FuzzySet(arrOfStr[0], arrOfStr[1]);
                                    for (int i = 2; i < arrOfStr.length; i++) {
                                        fuzzySet.addValue(Integer.parseInt(arrOfStr[i]));
                                    }
                                    if (currentVariable.getType().equals("OUT")) {
                                        if (fuzzySet.type.equals("TRI")) {
                                            fuzzySet.centroid = (fuzzySet.values.get(0) + fuzzySet.values.get(1) + fuzzySet.values.get(2)) / 3.0;
                                        } else if (fuzzySet.type.equals("TRAP")) {
                                            fuzzySet.centroid = (fuzzySet.values.get(1) + fuzzySet.values.get(2)) / 2.0;
                                        }
                                    }
                                    currentVariable.addFuzzySet(fuzzySet);
                                }


                            } else {
                                System.out.println("Variable doesn't exist");
                            }
                            break;

                        case "3":
                            System.out.println("Enter the rules in this format: (Press x to finish)\n" +
                                    "IN_variable set operator IN_variable set => OUT_variable set");
                            while (true) {
                                String rule = scan.nextLine();
                                if (rule.equals("x"))
                                    break;
                                splittingStringRule(rule);
                            }
                            break;

                        case "4":
                            ArrayList<Double> outputMemberships = new ArrayList<>();
                            System.out.println("Enter the crisp values:\n" +
                                    "-----------------------");
                            for (Variable var : variables) {
                                if (var.getType().equals("IN")) {
                                    Scanner scan1 = new Scanner(System.in);
                                    System.out.print(var.getName() + ": ");
                                    double crisp = scan1.nextDouble();
                                    for (FuzzySet fuzzySet1 : var.fuzzySets) {
                                        fuzzySet1.fuzzification(crisp);
                                    }
                                }
                            }
                            System.out.println("Running the simulation...");
                            System.out.println("Fuzzification => done");
                            for (Rules rule : rules) {
                                outputMemberships.add(rule.inference());
                            }

                            for (Variable var : variables) {
                                if (var.getType().equals("OUT")) {
                                    for (FuzzySet fuzzySet : var.fuzzySets) {
                                        double max = -1;
                                        for (int i = 0; i < rules.size(); i++) {
                                            if (rules.get(i).sets.get(2).getName().equals(fuzzySet.name)) {
                                                if (outputMemberships.get(i) > max)
                                                    max = outputMemberships.get(i);
                                            }
                                        }
                                        fuzzySet.degreeOfMembership = max;
                                    }
                                }
                            }
                            System.out.println("Inference => done");
                            double crispOutput = 0;
                            String variableName = "", setName = "";
                            double max = -1;
                            //defuzzification
                            for(Variable var : variables){
                                double numeratorSum = 0, denominatorSum = 0;
                                if (var.getType().equals("OUT")){
                                    variableName = var.name;
                                    for (FuzzySet fuzzySet : var.fuzzySets) {
                                        numeratorSum += fuzzySet.centroid * fuzzySet.degreeOfMembership;
                                        denominatorSum += fuzzySet.degreeOfMembership;
                                        if(fuzzySet.degreeOfMembership >= max){
                                            max = fuzzySet.degreeOfMembership;
                                            setName = fuzzySet.name;
                                        }
                                    }
                                    System.out.println(var.fuzzySets);
                                }
                                crispOutput = numeratorSum/denominatorSum;

                            }
                            System.out.println("Defuzzification => done");
                            System.out.println("The predicted " + variableName + " is " + setName + " (" + crispOutput + ")");

                            break;
                        default:
                            break;
                    }

                }
            } else
                break;
        }
    }
}
