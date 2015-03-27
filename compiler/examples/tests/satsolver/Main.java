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
        int[] literals = this.literals;
        literals[this.literalsCounter] = literal;
        this.literalsCounter++;
    }

    public int getCLauseWeight(int[] weights) {
        int weight = 0;

        for (Integer literal : literals) {
            weight += weights[Math.abs(literal) - 1];
        }

        return weight;
    }

    public boolean isSatisfiable(boolean[] clauseValuation) {
        int[] literals = this.literals;
        for (int i = 0; i < literals.length; i++) {
            int literalIndex = literals[i];
            if (literalIndex < 0) {
                int negativeOne = 0 - 1;
                if (clauseValuation[negativeOne * literalIndex - 1] == false) {
                    return true;
                }
            } else {
                if (clauseValuation[literalIndex - 1] == true) {
                    return true;
                }
            }
        }

        return false;
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
        this.generateCombination(0);
    }

    private void generateCombination(int position) {
        if (this.countOfLiterals == position) {
            this.resolveCombination();
        } else {
            boolean[] configuration = this.configuration;
            configuration[position] = false;
            this.generateCombination(position + 1);
            configuration[position] = true;
            this.generateCombination(position + 1);
        }
    }

    private void resolveCombination() {
        boolean[] configuration = this.configuration;
        if (this.isSolution(configuration)) {
            String result = "";

            for (int i = 0; i < configuration.length; i++) {
                if (configuration[i]) {
                    result = result + "1";
                } else {
                    result = result + "0";
                }
            }

            System.println(result);
        }
    }

    public boolean isSolution(boolean[] clauseValuation) {
        Clauses[] listOfClauses = this.listOfClauses;

        for (int i = 0; i < listOfClauses.length; i++) {
            Clauses clause = listOfClauses[i];
            Boolean isSat = clause.isSatisfiable(clauseValuation);
            if (!isSat)
                return false;
        }

        return true;
    }

    public void addClause(String[] literals) {
        Clauses[] listOfClauses = this.listOfClauses;
        listOfClauses[this.clauseCounter] = new Clauses();

        for (int i = 0; i < literals.length - 1; i++) {
            Clauses clauses = listOfClauses[this.clauseCounter];
            clauses.addLiteral(Integer.parseInt(literals[i])); // TODO change literal to int
        }

        this.clauseCounter++;
    }
}

public class Main {

    public static void main(String[] args) {

        BruteForce bruteforce;
//        File file = new File(args[0]);
        File file = new File(args[0]);


//        /*if(!file.exist()){
//         System.err("Input file does not exists."); //TODO
//         }*/

        String headerLine = file.readLine();
        String[] header = headerLine.split(" ");

        int countOfLiterals = Integer.parseInt(header[2]);
        int countOfClauses = Integer.parseInt(header[3]);

        System.println("Count of literals");
        System.println(countOfLiterals);

        System.println("Count of clauses");
        System.println(countOfClauses);

        System.println("Bruteforce init");
        bruteforce = new BruteForce(countOfLiterals, countOfClauses);

        System.println("Adding clauses");

        for (int i = 0; i < countOfClauses; i++) {
            String literalLine = file.readLine();
            String[] literals = literalLine.split(" ");
            bruteforce.addClause(literals);
        }

        bruteforce.execute();
    }
}
