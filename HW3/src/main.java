import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class main {

	public static void main(String[] args) {
		String path = "..\\wdbc.data.txt";
		File file = new File(path);
		// feature 0-29, output 30
		ArrayList<Double[]> trainingSet = new ArrayList<Double[]>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				Double[] feature = new Double[31];
				if (data[1].equals("M"))
					feature[30] = 0.0;
				else
					feature[30] = 1.0;
				for (int j = 2;j < data.length;j++) {
					feature[j-2] = Double.parseDouble(data[j]);
				}
				trainingSet.add(feature);
			}
			Collections.shuffle(trainingSet);
			System.out.println(trainingSet.size());
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		GA ga = new GA();
		//int[] MLP_struct = {30,40,20,2};
		//int[] MLP_struct = {30,15,7,2};
		//int[] MLP_struct = {30,12,2};
		int[] MLP_struct = {30,10,5,2}; 
		
		System.out.print("Neuron network ");
		for (int i = 0;i < MLP_struct.length;i++) {
			if (i != 0)
				System.out.print('-');
			System.out.print(MLP_struct[i]);
		}
		ga.initChromosome(50, MLP_struct);
		double eav = 0;
		for (int c = 0;c < 10;c++) {
			System.out.println("\n---------------- Fold: "+(c+1)+" ----------------");
			int i = (int) (c*trainingSet.size()*0.1);
			ArrayList<Double[]> test = new ArrayList<Double[]>(trainingSet.subList(i, (int) (i+(trainingSet.size()*0.1))));
			ArrayList<Double[]> train = (ArrayList<Double[]>) trainingSet.clone();
			train.subList(i, (int) (i+(train.size()*0.1))).clear();
			ga.train(200, (ArrayList<Double[]>) train);
			eav += ga.test(test);
		}
		System.out.println("Error average: "+eav/10+"%");
	}
}
