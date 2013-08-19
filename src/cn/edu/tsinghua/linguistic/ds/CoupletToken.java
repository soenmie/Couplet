package cn.edu.tsinghua.linguistic.ds;

import java.util.HashMap;

public class CoupletToken
{
	private int frequency;
	private HashMap<String, Integer> counterToken;
	private HashMap<String, Integer> nextToken;

	public CoupletToken(int frequency, HashMap<String, Integer> counterToken,
			HashMap<String, Integer> nextToken)
	{
		super();
		this.frequency = frequency;
		this.counterToken = counterToken;
		this.nextToken = nextToken;
	}

	public int getFrequency()
	{
		return frequency;
	}

	public void increaseByOne()
	{
		this.frequency++;
	}

	public HashMap<String, Integer> getCounterToken()
	{
		return counterToken;
	}

	public void setCounterToken(HashMap<String, Integer> counterToken)
	{
		this.counterToken = counterToken;
	}

	public HashMap<String, Integer> getNextToken()
	{
		return nextToken;
	}

	public void setNextToken(HashMap<String, Integer> nextToken)
	{
		this.nextToken = nextToken;
	}
}
