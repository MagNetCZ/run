package mi.run.satsolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class Clauses {

    private int literals[];
    private int literalsCounter;

    public Clauses() {
        literals = new int[3];
        literalsCounter = 0;
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
        listOfClauses = new Clauses[this.countOfClauses];
        configuration = new boolean[this.countOfLiterals];
        clauseCounter = 0;
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
            System.out.println(result);
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
        listOfClauses[clauseCounter++] = new Clauses();
        for (String literal : literals) {
            listOfClauses[(clauseCounter - 1)].addLiteral(Integer.parseInt(literal));
        }
    }
}

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        BruteForce bruteforce;
        //File file = new File(args[0]);

        /*if(!file.exist()){
         System.err("Input file does not exists."); //TODO
         }*/

        /*String literals = file.readLine();
         String clauses = file.readLine();*/
        int countOfClauses = 4;
        int countOfLiterals = 4;

        try (BufferedReader br = new BufferedReader(new FileReader("src/mi/run/satsolver/sat_1.inst.dat"))) { //TODO remove
            countOfLiterals = Integer.parseInt(br.readLine());
            countOfClauses = Integer.parseInt(br.readLine());
            bruteforce = new BruteForce(countOfLiterals, countOfClauses);
            for (int i = 0; i < countOfClauses; i++) {
                String[] literals = br.readLine().split(",");
                bruteforce.addClause(literals);
            }
        }

        bruteforce.execute();
    }

}
