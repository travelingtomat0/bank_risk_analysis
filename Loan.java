import org.apache.commons.math3.distribution.*;

public class Loan {

	double ead;
	double pd;
	double threshold;
	double gamma;
	double alpha;
	double lgd;
	
	boolean def;
	
	int region;
	String rating;
	
	static NormalDistribution n;
	
	public Loan(String[] fl, String[] pf) {
		if(n == null) n = new NormalDistribution();
		this.ead = Double.parseDouble(pf[3]);
		this.rating = pf[2];
		String reg = pf[1];
		if(reg == "CH") {
			region = 0;
		} else if(reg == "EU") {
			region = 1;
		} else {
			region = 2;
		}
		
		this.lgd = Double.parseDouble(pf[4]);
		this.pd = getPD(this.rating);
		this.threshold = n.inverseCumulativeProbability(this.pd);
		this.alpha = getAlpha(fl);
		this.gamma = Math.sqrt(1-(this.alpha*this.alpha));
		this.def = false;
	}
	
	double getPD(String a) {
		switch(a) {
		case "AAA": return 0.0001;
		case "AA": return 0.0002;
		case "A": return 0.0006;
		case "BBB": return 0.0064;
		case "BB": return 0.0148;
		case "B": return 0.0568;
		case "CCC": return 0.0758;
		default: return 1;
		}
	}
	
	double getAlpha(String[] in) {
		for(int i = 1; i < 4; i++) {
			double temp = Double.parseDouble(in[i]);
			if(temp != 0) return temp;
		}
		return 0.0;
	}
	
}
