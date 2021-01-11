import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.math3.distribution.*;

class MonteCarlo extends Thread {
	Loan[] loans;
	int id;

	double[][] coeffs;
	int size;
	
	MonteCarlo(Loan[] loans, double[][] coeffs, int id, int size) {
		this.loans = loans;
		this.id = id;
		this.coeffs = coeffs;
		this.size = size;
	}
	
	public void run() {
		compute();
	}
	
	public void compute() {
		NormalDistribution n = new NormalDistribution();
		MultivariateNormalDistribution mn = new MultivariateNormalDistribution(new double [] {0.0,0.0,0.0}, this.coeffs);
		
		double[] loss = new double[Main.DATA / Main.KAKA];
		for(int k = 0; k < Main.DATA / Main.KAKA; k++) {
			double[] risks = mn.sample();
			double[] normalDist =  n.sample(size);
			int count = 0;
			for(int i = 0; i < loans.length; i++) {
				double x = loans[i].alpha * risks[loans[i].region] + loans[i].gamma * normalDist[i];
				if(x < loans[i].threshold) {
					//loans[i].def = true;
					count++;
					loss[k] += loans[i].ead * loans[i].lgd;
				}
			}
		}
		Main.losses.push(loss);
	}
}

public class Main {
	public static final int DATA = 1000, KAKA = 10;
	public static LinkedList<double[]> losses = new LinkedList<double[]>();

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		ParseModule parser = new ParseModule();
		ArrayList<String[]> c = parser.instantiateFile(new File("/home/phil/eclipse-workspace/Deloitte/src/Correlation.csv"));
		ArrayList<String[]> fl = parser.instantiateFile(new File("/home/phil/eclipse-workspace/Deloitte/src/Factor_Loadings.csv"));
		ArrayList<String[]> pt = parser.instantiateFile(new File("/home/phil/eclipse-workspace/Deloitte/src/PD_table.csv"));
		ArrayList<String[]> pf = parser.instantiateFile(new File("/home/phil/eclipse-workspace/Deloitte/src/Portfolio.csv"));
		
		double t_start = System.currentTimeMillis();
		double[][] coeffs = getCoeffs(c);
		
		Loan[] loans = new Loan[pf.size()];
		MonteCarlo[] threads = new MonteCarlo[KAKA];
		for(int i = 0; i < KAKA; i++) {
			threads[i] = new MonteCarlo(loans, coeffs, i, pf.size());
		}
		for(int i = 0; i < pf.size(); i++) {
			loans[i] = new Loan(fl.get(i), pf.get(i));
		}
		
		System.out.println((System.currentTimeMillis() - t_start) / 1000 + " Seconds for initialization.");
		
		t_start = System.currentTimeMillis();
		
		for(int i = 0; i < KAKA; i++) {
			threads[i].start();
		}
		for(int i = 0; i < KAKA; i++) {
			threads[i].join();
		}
		System.out.println((System.currentTimeMillis() - t_start) / 1000 + " Seconds for Monte Carlo Simulation.");
		
		// TODO: Calculate risk metrics and histogram.
		int length = 0;
		for(int i = 0; i < losses.size(); i++) {
			length += losses.get(i).length;
		}
		double[]stuff = new double[length];
		int k = 0;
		for(double[] node:losses) {
			for(int i= 0; i < node.length; i++) {
				stuff[k] = node[i];
			}
			k += node.length;
		}
		Arrays.sort(stuff);
		int ind_95 = (int)Math.round(0.95*length);
		int ind_99 = (int)Math.round(0.99*length);
		System.out.println(ind_95);
		System.out.println(stuff[ind_95+10]);
		System.out.println(stuff[ind_99]);
	}
	
	
	static double[][] getCoeffs(ArrayList<String[]> c) {
		double[][] res = new double[3][3];
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				res[i][j] = Double.parseDouble(c.get(i)[j+1]);
			}
		}
		return res;
	}

}
