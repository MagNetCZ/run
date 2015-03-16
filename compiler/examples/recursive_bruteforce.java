/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fit.cvut.cz.paa.knapsack.solutiongenerator;

import fit.cvut.cz.paa.knapsack.Item;
import fit.cvut.cz.paa.knapsack.ProblemInstance;
import fit.cvut.cz.paa.knapsack.Solution;
import fit.cvut.cz.paa.knapsack.SolutionGeneratorInterface;
import java.util.BitSet;

/**
 *
 * @author MagNet
 */
public class RecursiveBruteforceSolutionGenerator implements SolutionGeneratorInterface {

    Solution currentBestSolution;
    double accessedStates;
    
    private void startSelect(ProblemInstance problem) {
        select(new BitSet(problem.getNumItems()), problem, 0, 0, 0);
    }
    
    private void select(
            BitSet selection, ProblemInstance problem, int itemIndex,
            int price, int weight) {
        if (currentBestSolution == null || price > currentBestSolution.getPrice())
            currentBestSolution = new Solution((BitSet)selection.clone(), problem);
        
        if (itemIndex == problem.getNumItems())
            return;
        
        accessedStates ++;
        
        Item item = problem.getItem(itemIndex);
        
        if (weight + item.getWeight() <= problem.getKnapsackCapacity()) {
            selection.set(itemIndex);
            select(selection, problem, itemIndex + 1, price + item.getPrice(), weight + item.getWeight());
        }
        
        selection.set(itemIndex, false);
        select(selection, problem, itemIndex + 1, price, weight);
    }
    
    @Override
    public Solution getSolution(ProblemInstance problem) {
        accessedStates = 0;
        currentBestSolution = null;
        startSelect(problem);
        //System.out.println("RBF accessed states: " + accessedStates);        
        return currentBestSolution;
    }

    @Override
    public double getExtra() {
        return accessedStates;
    }
    
    @Override
    public String getName() {
        return "BF";
    }

    @Override
    public SolutionGeneratorInterface copy() {
        return new RecursiveBruteforceSolutionGenerator();
    }
    
}
