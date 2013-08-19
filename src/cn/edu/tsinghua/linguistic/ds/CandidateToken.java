package cn.edu.tsinghua.linguistic.ds;

public class CandidateToken implements Comparable<CandidateToken>
{
	private double probability;
	private String prior;
	private String counter;

	public CandidateToken(String counter)
	{
		this(0, "", counter);
	}

	public CandidateToken(double probability, String prior, String counter)
	{
		super();
		this.probability = probability;
		this.prior = prior;
		this.counter = counter;
	}

	public double getProbability()
	{
		return probability;
	}

	public void setProbability(double probability)
	{
		this.probability = probability;
	}

	public String getPrior()
	{
		return prior;
	}

	public void setPrior(String prior)
	{
		this.prior = prior;
	}

	public String getCounter()
	{
		return counter;
	}

	public void setCounter(String counter)
	{
		this.counter = counter;
	}

	@Override
	public int compareTo(CandidateToken o)
	{
		return (int) ((this.probability - o.probability) / Math
				.abs(this.probability - o.probability));
	}
}
