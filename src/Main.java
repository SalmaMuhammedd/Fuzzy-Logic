import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ArrayList<Variable> variables = new ArrayList<>();
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
                    if(mainMenuChoice.equals("close"))
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
                                    currentVariable.addFuzzySet(fuzzySet);
                                }
                                for(FuzzySet fuzzySet1: currentVariable.fuzzySets){
                                    fuzzySet1.fuzzification(25);
                                }
                                System.out.println(currentVariable);
                            } else {
                                System.out.println("Variable doesn't exist");
                            }
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
