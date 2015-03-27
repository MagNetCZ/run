package mi.run.satsolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class Clauses {

    private int[] literals;
    private int literalsCounter;

    public Clauses() {
        this.literals = new int[3];
        this.literalsCounter = 0;
    }

    public void addLiteral(int literal) {
        literals[literalsCounter++] = literal;
    }

    public int getCLauseWeight(int[] weights) {
        int weight = 0;

        for (Integer literal : literals) {
            weight += weights[Math.abs(literal) - 1];
        }

        return weight;
    }

    public boolean isSatisfiable(boolean[] clauseValuation) {
        boolean result = false;
        for (Integer literal : literals) {
            if (literal < 0) {
                if (clauseValuation[Math.abs(literal) - 1] == false) {
                    return true;
                }
            } else {
                if (clauseValuation[Math.abs(literal) - 1] == true) {
                    return true;
                }
            }
        }

        return result;
    }
}

class BruteForce {

    Clauses[] listOfClauses;
    boolean[] configuration;
    private int countOfLiterals;
    private int countOfClauses;
    private int clauseCounter;

    public BruteForce(int countOfLiterals, int countOfClauses) {
        this.countOfLiterals = countOfLiterals;
        this.countOfClauses = countOfClauses;
        this.listOfClauses = new Clauses[this.countOfClauses];
        this.configuration = new boolean[this.countOfLiterals];
        this.clauseCounter = 0;
    }

    public void execute() {
        generateCombination(0);
    }

    private void generateCombination(int position) {
        if (this.getLiterals() == position) {
            resolveCombination();
        } else {
            this.configuration[position] = false;
            generateCombination(position + 1);
            this.configuration[position] = true;
            generateCombination(position + 1);
        }
    }

    private void resolveCombination() {
        if (this.isSolution(configuration)) {
            String result = "";
            for (Boolean value : configuration) {
                result += (value == true ? "1" : "0");
            }
            System.println(result);
        }
    }

    public boolean isSolution(boolean[] clauseValuation) {
        boolean result = true;
        for (Clauses clause : this.listOfClauses) {
            if (!clause.isSatisfiable(clauseValuation))
                return false;
        }

        return true;
    }

    public int getLiterals() {
        return countOfLiterals;
    }

    public void addClause(String[] literals) {
        System.println(this.clauseCounter);
        System.println("Add clause");
        this.listOfClauses[this.clauseCounter] = new Clauses();

//
//        for (int i = 0; i < literals.length; i++) {
//            Clauses clauses = this.listOfClauses[this.clauseCounter];
//            clauses.addLiteral(literals[i]); // TODO change literal to int
//            System.println(literals[i]);
//        }

        this.clauseCounter++;
    }
}

public class Main {

    public static void main(String[] args) {

        BruteForce bruteforce;
//        File file = new File(args[0]);
        File file = new File("examples/tests/satsolver/sat_1.inst.dat");


//        /*if(!file.exist()){
//         System.err("Input file does not exists."); //TODO
//         }*/

        int countOfLiterals = Integer.parseInt(file.readLine());
        int countOfClauses = Integer.parseInt(file.readLine());

        System.println("Bruteforce init");
        bruteforce = new BruteForce(countOfLiterals, countOfClauses);

        System.println("Adding clauses");
        System.println("Count of clauses");
        System.println(countOfClauses);
        for (int i = 0; i < countOfClauses; i++) {
            String literalLine = file.readLine();
            System.println(literalLine);
            String[] literals = literalLine.split(",");
//            System.println(literals[0]);
            bruteforce.addClause(literals);
        }

//        bruteforce.execute();
    }
}
