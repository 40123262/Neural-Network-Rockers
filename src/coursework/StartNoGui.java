package coursework;

import model.Fitness;

import model.LunarParameters.DataSet;
import model.NeuralNetwork;
import model.StringIO;

/**
 * Example of how to to run the {@link ExampleEvolutionaryAlgorithm} without the need for the GUI
 * This allows you to conduct multiple runs programmatically 
 * The code runs faster when not required to update a user interface
 *
 */
public class StartNoGui {

	public static void main(String[] args)  {
		/**
		 * Train the Neural Network using our Evolutionary Algorithm 
		 * 
		 */

		String str = "";
		
		/*
		 * Set the parameters here or directly in the Parameters Class.
		 * Note you should use a maximum of 20,0000 evaluations for your experiments 
		 */
		//for(int j=0; j<5; j++)
		{
		for(int i=0; i<30; i++)
		{
		Parameters.maxEvaluations = 20000; // Used to terminate the EA after this many generations
		Parameters.popSize = 200; // Population Size

		//number of hidden nodes in the neural network
		Parameters.setHidden(7);
		
		//Set the data set for training 
		Parameters.setDataSet(DataSet.Training);
		
		
		//Create a new Neural Network Trainer Using the above parameters 
		NeuralNetwork nn = new ExampleEvolutionaryAlgorithm();		
		
		//train the neural net (Go and make a coffee) 
		nn.run();
		
		/* Print out the best weights found
		 * (these will have been saved to disk in the project default directory) 
		 */
		System.out.println("Rep:["+i + "], Best: " + nn.best);
		str+=""+nn.best.fitness+"\n";
		
		
		/**
		 * We now need to test the trained network on the unseen test Set
		 */
		Parameters.setDataSet(DataSet.Test);
		double fitness = Fitness.evaluate(nn);
		System.out.println("Fitness on " + Parameters.getDataSet() + " " + fitness);
		}
		StringIO.writeStringToFile("fitnesses.csv", str, false);
		/*
		if(j==0)
		{
			
			str = "";
			Parameters.mutateChange = 0.02;
		}
		else if(j==1)
		{
			StringIO.writeStringToFile("fitnesses002.csv", str, false);
			str = "";
			Parameters.mutateChange = 0.075;
		}
		else if(j==2)
		{
			StringIO.writeStringToFile("fitnesses0075.csv", str, false);
			str = "";
			Parameters.mutateChange = 0.2;
		}
		else if(j==3)
		{
			StringIO.writeStringToFile("fitnesses02.csv", str, false);
			break;
		}*/
		}
		/**
		 * Or We can reload the NN from the file generated during training and test it on a data set 
		 * We can supply a filename or null to open a file dialog 
		 * Note that files must be in the project root and must be named *-n.txt
		 * where "n" is the number of hidden nodes
		 * ie  1518461386696-5.txt was saved at timestamp 1518461386696 and has 5 hidden nodes
		 * Files are saved automatically at the end of training
		 *  
		 *  Uncomment the following code and replace the name of the saved file to test a previously trained network 
		 */
		
//		NeuralNetwork nn2 = NeuralNetwork.loadNeuralNetwork("1234567890123-5.txt");
//		Parameters.setDataSet(DataSet.Random);
//		double fitness2 = Fitness.evaluate(nn2);
//		System.out.println("Fitness on " + Parameters.getDataSet() + " " + fitness2);
		
		
		
	}
}
