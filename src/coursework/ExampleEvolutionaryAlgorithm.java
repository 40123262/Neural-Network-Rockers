package coursework;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import model.Fitness;
import model.Individual;
import model.LunarParameters.DataSet;
import model.NeuralNetwork;
import model.StringIO;

import java.io.File;
/**
 * Implements a basic Evolutionary Algorithm to train a Neural Network
 * 
 * You Can Use This Class to implement your EA or implement your own class that extends {@link NeuralNetwork} 
 * 
 */
public class ExampleEvolutionaryAlgorithm extends NeuralNetwork {
	

	/**
	 * The Main Evolutionary Loop
	 */
	@Override
	public void run() {		
		//Initialise a population of Individuals with random weights
		population = initialise();

		//Record a copy of the best Individual in the population
		best = getBest();
		System.out.println("Best From Initialisation " + best);

		/**
		 * main EA processing loop
		 */		

		
		while (evaluations < Parameters.maxEvaluations) {

			/**
			 * this is a skeleton EA - you need to add the methods.
			 * You can also change the EA if you want 
			 * You must set the best Individual at the end of a run
			 * 
			 */

			// Select 2 Individuals
			// uses roulette selection
			Individual parent1 = roulette_select(); 
			Individual parent2 = roulette_select();

			// Generate a child by crossover. Not Implemented			
			ArrayList<Individual> children = uniform_crossover(parent1, parent2);			
			
			//mutate the offspring
			mutate(children);
			
			// Evaluate the children
			evaluateIndividuals(children);			

			// Replace children in population
			replace(children);

			// check to see if the best has improved
			best = getBest();
			
			
			// Implemented in NN class. 
			outputStats();
	
		
			//Increment number of completed generations			
		}
		
		//save the trained network to disk
		saveNeuralNetwork();
		
	}

	

	/**
	 * Sets the fitness of the individuals passed as parameters (whole population)
	 * 
	 */
	private void evaluateIndividuals(ArrayList<Individual> individuals) {
		for (Individual individual : individuals) {
			individual.fitness = Fitness.evaluate(individual, this);
		}
	}


	/**
	 * Returns a copy of the best individual in the population
	 * 
	 */
	private Individual getBest() {
		best = null;;
		for (Individual individual : population) {
			if (best == null) {
				best = individual.copy();
			} else if (individual.fitness < best.fitness) {
				best = individual.copy();
			}
		}
		return best;
	}

	/**
	 * Generates a randomly initialised population
	 * 
	 */
	private ArrayList<Individual> initialise() {
		population = new ArrayList<>();
		for (int i = 0; i < Parameters.popSize; ++i) {
			//chromosome weights are initialised randomly in the constructor
			Individual individual = new Individual();
			population.add(individual);
		}
		evaluateIndividuals(population);
		return population;
	}

	/**
	 * Selection --
	 * 
	 * NEEDS REPLACED with proper selection this just returns a copy of a random
	 * member of the population
	 */
	private Individual random_select() {		
		Individual parent = population.get(Parameters.random.nextInt(Parameters.popSize));
		return parent.copy();
	}
	
	private Individual tournament_select(int n) {		
		double bestFitnessSoFar=100;
		int chosen_index=0;
		for(int i=0; i < n; i++)
		{
			int current_index = Parameters.random.nextInt(population.size());
			if(population.get(current_index).fitness < bestFitnessSoFar)
			{
				bestFitnessSoFar = population.get(current_index).fitness;
				chosen_index = current_index;
			}
		}
		
		
		
		return population.get(chosen_index);
	}
	private Individual roulette_select()
	{
		//////sum of all fitnesses to "map" the wheel
		double sumAllFitness = 0;
		for(Individual p : population)
		{
			sumAllFitness+=1.0f/p.fitness;
		}
		/////random number which is the pocket where the "ball" landed on the wheel
		double pocket = Parameters.random.nextDouble()*sumAllFitness;

		/// actually "spin" the wheel and land in the pocket
		double ball = 0;
	//	System.out.println("pocket landed in: " + pocket);
		for(Individual p : population)
		{
			ball+=1.0f/p.fitness;
			if(ball >= pocket )
				return p;
		}

		return null;
		
	}

	/**
	 * Crossover / Reproduction
	 * 
	 * NEEDS REPLACED with proper method this code just returns exact copies of the
	 * parents. 
	 */
	private ArrayList<Individual> reproduce(Individual parent1, Individual parent2) {
		ArrayList<Individual> children = new ArrayList<>();
		children.add(parent1.copy());
		children.add(parent2.copy());		
		return children;
	} 
	
	private ArrayList<Individual> one_point_crossover(Individual parent1, Individual parent2) {
		ArrayList<Individual> children = new ArrayList<>();
			
		for(int j=0; j<2; j++)
		{
			Individual child = new Individual();
			int split_point = Parameters.random.nextInt(parent1.chromosome.length);
			for(int i =0; i<parent1.chromosome.length; i++)
			{
				if(i<split_point)
				{
					child.chromosome[i] = parent1.chromosome[i];
				}
				else
				{
					child.chromosome[i] = parent2.chromosome[i];
				}
			}
		children.add(child);
		}	
		return children;
	} 
	private ArrayList<Individual> two_point_crossover(Individual parent1, Individual parent2) {
		ArrayList<Individual> children = new ArrayList<>();
			
		for(int j=0; j<2; j++)
		{
			Individual child = new Individual();
			int split_point1 = Parameters.random.nextInt(parent1.chromosome.length);
			int split_point2 = split_point1+ Parameters.random.nextInt(parent1.chromosome.length - split_point1);
			for(int i =0; i<parent1.chromosome.length; i++)
			{
				if(i<split_point1 || i >split_point2)
				{
					child.chromosome[i] = parent1.chromosome[i];
				}
				else
				{
					child.chromosome[i] = parent2.chromosome[i];
				}
			}
		children.add(child);
		}	
		return children;
	} 
	private ArrayList<Individual> uniform_crossover(Individual parent1, Individual parent2) {
		ArrayList<Individual> children = new ArrayList<>();
		
		for(int j=0; j<2; j++)
		{
			Individual child = new Individual();
			for(int i =0; i<parent1.chromosome.length; i++)
			{
				if(Parameters.random.nextBoolean())
				{
					child.chromosome[i] = parent1.chromosome[i];
				}
				else
				{
					child.chromosome[i] = parent2.chromosome[i];	
				}
		}
		children.add(child);
		}
		return children;
	} 
	
	/**
	 * Mutation
	 * 
	 * 
	 */
	private void mutate(ArrayList<Individual> individuals) {		
		for(Individual individual : individuals) {
			for (int i = 0; i < individual.chromosome.length; i++) {
				if (Parameters.random.nextDouble() < Parameters.mutateRate) {
					if (Parameters.random.nextBoolean()) {
						individual.chromosome[i] += (Parameters.mutateChange);
					} else {
						individual.chromosome[i] -= (Parameters.mutateChange);
					}
				}
			}
		}		
	}

	/**
	 * 
	 * Replaces the worst member of the population 
	 * (regardless of fitness)
	 * 
	 */
	private void replace(ArrayList<Individual> individuals) {
		for(Individual individual : individuals) {
			int idx = getWorstIndex();		
			population.set(idx, individual);
		}		
	}

	

	/**
	 * Returns the index of the worst member of the population
	 * @return
	 */
	private int getWorstIndex() {
		Individual worst = null;
		int idx = -1;
		for (int i = 0; i < population.size(); i++) {
			Individual individual = population.get(i);
			if (worst == null) {
				worst = individual;
				idx = i;
			} else if (individual.fitness > worst.fitness) {
				worst = individual;
				idx = i; 
			}
		}
		return idx;
	}	

	@Override
	public double activationFunction(double x) {
		if (x < -20.0) {
			return -1.0;
		} else if (x > 20.0) {
			return 1.0;
		}
		return Math.tanh(x);
	}
}
