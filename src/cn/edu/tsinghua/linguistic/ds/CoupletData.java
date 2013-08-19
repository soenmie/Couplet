package cn.edu.tsinghua.linguistic.ds;

import java.util.HashMap;

public class CoupletData extends HashMap<String, CoupletToken>
{
	private static final long serialVersionUID = -7557051447637296281L;
	public static final String START_SYMBOL = "[";
	public static final String END_SYMBOL = "]";
	private static volatile CoupletData INSTANCE = null;

	private CoupletData()
	{
	}

	public static CoupletData getInstance()
	{
		if (INSTANCE == null)
		{
			synchronized (CoupletData.class)
			{

				if (INSTANCE == null)
				{
					INSTANCE = new CoupletData();
					INSTANCE.put(START_SYMBOL, new CoupletToken(0, null,
							new HashMap<String, Integer>()));
				}
			}
		}
		return INSTANCE;
	}

	public void head(String start)
	{
		CoupletToken coupletToken = INSTANCE.get(START_SYMBOL);
		coupletToken.increaseByOne();
		HashMap<String, Integer> nextToken = coupletToken.getNextToken();
		if (nextToken.containsKey(start))
		{
			nextToken.put(start, nextToken.get(start) + 1);
		}
		else
		{
			nextToken.put(start, 1);
		}
	}

	public void tail(String end, String counter)
	{
		CoupletToken coupletToken;
		if (INSTANCE.containsKey(end))
		{
			coupletToken = INSTANCE.get(end);
			coupletToken.increaseByOne();
		}
		else
		{
			coupletToken = new CoupletToken(1, new HashMap<String, Integer>(),
					new HashMap<String, Integer>());
			INSTANCE.put(end, coupletToken);

		}

		HashMap<String, Integer> counterToken = coupletToken.getCounterToken();
		if (counterToken.containsKey(counter))
		{
			counterToken.put(counter, counterToken.get(counter) + 1);
		}
		else
		{
			counterToken.put(counter, 1);
		}

		HashMap<String, Integer> nextToken = coupletToken.getNextToken();
		if (nextToken.containsKey(END_SYMBOL))
		{
			nextToken.put(END_SYMBOL, nextToken.get(END_SYMBOL) + 1);
		}
		else
		{
			nextToken.put(END_SYMBOL, 1);
		}
	}

	public void put(String token, String counter, String next)
	{
		CoupletToken coupletToken;

		if (INSTANCE.containsKey(token))
		{
			coupletToken = INSTANCE.get(token);
			coupletToken.increaseByOne();

		}
		else
		{
			coupletToken = new CoupletToken(1, new HashMap<String, Integer>(),
					new HashMap<String, Integer>());
			INSTANCE.put(token, coupletToken);
		}

		HashMap<String, Integer> counterToken = coupletToken.getCounterToken();
		if (counterToken.containsKey(counter))
		{
			counterToken.put(counter, counterToken.get(counter) + 1);
		}
		else
		{
			counterToken.put(counter, 1);
		}

		HashMap<String, Integer> nextToken = coupletToken.getNextToken();
		if (nextToken.containsKey(next))
		{
			nextToken.put(next, nextToken.get(next) + 1);
		}
		else
		{
			nextToken.put(next, 1);
		}
	}
}
