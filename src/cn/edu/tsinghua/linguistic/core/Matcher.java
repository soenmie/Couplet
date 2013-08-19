package cn.edu.tsinghua.linguistic.core;

import static cn.edu.tsinghua.linguistic.ds.CoupletData.START_SYMBOL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import cn.edu.tsinghua.linguistic.cfg.Configuration;
import cn.edu.tsinghua.linguistic.corpus.Tone;
import cn.edu.tsinghua.linguistic.ds.CandidateToken;
import cn.edu.tsinghua.linguistic.ds.CoupletData;
import cn.edu.tsinghua.linguistic.exception.NonMatchException;
import cn.edu.tsinghua.linguistic.exception.UncommonWordException;
import cn.edu.tsinghua.linguistic.lex.Segmenter;

public class Matcher
{
	private static Configuration configuration = new Configuration();

	public static Configuration getConfiguration()
	{
		return configuration;
	}

	public static void setConfiguration(Configuration configuration)
	{
		Matcher.configuration = configuration;
	}

	private static final int CANDIDATE_RANK = 3;

	private static double probability(String token, String counter,
			String counterPrior, double counterPriorProbability)
	{
		double np = nextProbability(counter, counterPrior);
		double cp = counterProbability(counter, token);
		return np * cp + counterPriorProbability * counterPriorProbability;
	}

	private static double counterProbability(String counter, String token)
	{
		CoupletData coupletData = CoupletData.getInstance();

		return (double) (coupletData.get(token).getCounterToken().get(counter))
				/ (double) (coupletData.get(token).getFrequency());
	}

	private static double nextProbability(String counter, String counterPrior)
	{
		CoupletData coupletData = CoupletData.getInstance();
		if (counterPrior.equals(START_SYMBOL))
		{
			return (double) (coupletData.get(counter).getFrequency()) * 2.0
					/ (double) (coupletData.size());
		}
		else
		{
			if (coupletData.get(counterPrior) == null
					|| coupletData.get(counterPrior).getFrequency() == 0)
			{
				return 1.0 / ((double) (coupletData.size()) * 3.0);
			}
			else
			{
				if (coupletData.get(counterPrior).getNextToken().get(counter) == null)
				{
					return 1.0 / ((double) (coupletData.size()) * 3.0);
				}
				return (double) (coupletData.get(counterPrior).getNextToken()
						.get(counter))
						/ (double) (coupletData.get(counterPrior)
								.getFrequency());
			}
		}
	}

	private static List<HashMap<String, CandidateToken>> filter(String fisrtLine)
			throws NonMatchException, UncommonWordException
	{
		CoupletData coupletData = CoupletData.getInstance();
		List<HashMap<String, CandidateToken>> candidateString = new ArrayList<HashMap<String, CandidateToken>>();
		List<String> tokens = Segmenter.tokenize(fisrtLine);

		int index = 0;
		for (int i = 0; i < tokens.size(); i++)
		{
			String key0, key1;

			String token = tokens.get(i);
			HashMap<String, CandidateToken> candidateTokens;
			if (!coupletData.containsKey(token))
			{
				for (int j = 0; j < token.length(); j++)
				{
					key0 = new String(new char[]
					{ token.charAt(j) });
					if (!coupletData.containsKey(key0))
					{
						throw new UncommonWordException();
					}
					else
					{
						candidateTokens = new HashMap<String, CandidateToken>();
						for (Entry<String, Integer> entry : coupletData.get(
								key0).getCounterToken().entrySet())
						{
							key1 = entry.getKey();

							if (Tone.isOnAntithesisPosition(index + 1))
							{
								if (!fisrtLine.contains(key1)
										&& Tone.isMatch(key1.charAt(0), key0
												.charAt(0)))
								{
									candidateTokens.put(key1,
											new CandidateToken(key0));
								}
							}
							else
							{
								if (!fisrtLine.contains(key1))
								{
									candidateTokens.put(key1,
											new CandidateToken(key0));
								}
							}

						}
						if (candidateTokens.size() == 0)
						{
							throw new NonMatchException();
						}
						candidateString.add(candidateTokens);
						index++;
					}
				}
			}
			else
			{
				candidateTokens = new HashMap<String, CandidateToken>();
				int length = 0;
				key0 = token;

				for (Entry<String, Integer> entry : coupletData.get(token)
						.getCounterToken().entrySet())
				{
					key1 = entry.getKey();

					if (!fisrtLine.contains(key1))
					{
						boolean toneMatch = true;
						for (int j = 0; j < (length = key1.length()); j++)
						{
							if (Tone.isOnAntithesisPosition(index + j))
							{
								if (!Tone.isMatch(key1
										.charAt(key1.length() - 1), key0
										.charAt(key0.length() - 1)))
								{
									toneMatch = false;
									break;
								}
							}
						}

						if (toneMatch)
						{
							candidateTokens.put(key1, new CandidateToken(key0));
						}
					}
				}
				if (candidateTokens.size() == 0)
				{
					throw new NonMatchException();
				}
				candidateString.add(candidateTokens);
				index += length;
			}
		}
		return candidateString;
	}

	private static void calculateProperties(
			List<HashMap<String, CandidateToken>> candidateString)
	{
		for (int i = 1; i < candidateString.size(); i++)
		{

			HashMap<String, CandidateToken> candidateTokens0 = candidateString
					.get(i);
			String key;
			CandidateToken candidateToken;
			double total;
			if (i == 0)
			{
				total = 0.0;

				for (Entry<String, CandidateToken> entry : candidateTokens0
						.entrySet())
				{
					key = entry.getKey();
					candidateToken = entry.getValue();
					double t = probability(key, candidateToken.getCounter(),
							START_SYMBOL, 0);

					total += t;
				}
				for (Entry<String, CandidateToken> entry : candidateTokens0
						.entrySet())
				{
					candidateToken = entry.getValue();
					candidateToken.setPrior(START_SYMBOL);
					candidateToken.setProbability(total
							/ candidateTokens0.size());
				}
			}
			else
			{
				HashMap<String, Double> totals = new HashMap<String, Double>();
				HashMap<String, Integer> counts = new HashMap<String, Integer>();
				for (Entry<String, CandidateToken> entry0 : candidateTokens0
						.entrySet())
				{
					String key0 = entry0.getKey();
					CandidateToken candidateToken0 = entry0.getValue();
					HashMap<String, CandidateToken> candidateTokens1 = candidateString
							.get(i - 1);

					for (Entry<String, CandidateToken> entry1 : candidateTokens1
							.entrySet())
					{
						double t;
						String key1 = entry1.getKey();
						CandidateToken candidateToken1 = entry1.getValue();

						t = probability(key0, candidateToken0.getCounter(),
								candidateToken1.getPrior(), candidateToken1
										.getProbability());

						if (!totals.containsKey(key1))
						{
							totals.put(key1, t);
							counts.put(key1, 1);
						}
						else
						{
							totals.put(key1, t + totals.get(key1));
							counts.put(key1, counts.get(key1) + 1);
						}
					}

				}
				for (Entry<String, Double> entry : totals.entrySet())
				{
					key = entry.getKey();
					total = totals.get(key) / counts.get(key);
					totals.put(key, total);
				}

				List<Entry<String, Double>> tRanks = new ArrayList<Entry<String, Double>>(
						totals.entrySet());
				Collections.sort(tRanks,
						new Comparator<Entry<String, Double>>()
						{
							@Override
							public int compare(Entry<String, Double> e1,
									Entry<String, Double> e2)
							{
								return (int) (e1.getValue() - e2.getValue()
										/ Math.abs((e1.getValue() - e2
												.getValue())));
							}
						});

				for (Entry<String, CandidateToken> entry : candidateTokens0
						.entrySet())
				{
					candidateToken = entry.getValue();
					int rank = (int) (Math.random() * CANDIDATE_RANK);
					candidateToken.setProbability(tRanks.get(rank).getValue());
					candidateToken.setPrior(tRanks.get(rank).getKey());
				}
			}
		}
	}

	private static List<String> generateSecondLines(
			List<HashMap<String, CandidateToken>> candidateString, int max)
	{
		List<String> secondLines = new ArrayList<String>();

		List<Entry<String, CandidateToken>> sortedCandidateTokens = new ArrayList<Entry<String, CandidateToken>>(
				candidateString.get(candidateString.size() - 1).entrySet());
		Collections.sort(sortedCandidateTokens,
				new Comparator<Entry<String, CandidateToken>>()
				{
					@Override
					public int compare(Entry<String, CandidateToken> e1,
							Entry<String, CandidateToken> e2)
					{
						return e1.getValue().compareTo(e2.getValue());
					}
				});

		int count = 0;
		for (int i = sortedCandidateTokens.size() - 1; i >= 0; i--)
		{
			String token = sortedCandidateTokens.get(i).getKey();
			StringBuffer result = new StringBuffer(token);

			String prior = sortedCandidateTokens.get(i).getValue().getPrior();

			for (int j = candidateString.size() - 2; j >= 0; j--)
			{
				HashMap<String, CandidateToken> candidateTokens = candidateString
						.get(j);
				CandidateToken candidateToken = candidateTokens.get(prior);
				result.insert(0, prior);
				prior = candidateToken.getPrior();
			}
			secondLines.add(result.toString());
			count++;
			if (count > max)
			{
				break;
			}
		}
		return secondLines;
	}

	public static List<String> OnSecondLine(String firstLine)
			throws NonMatchException, UncommonWordException
	{
		return OnSecondLine(firstLine, Integer.MAX_VALUE);
	}

	public static List<String> OnSecondLine(String firstLine, int max)
			throws NonMatchException, UncommonWordException
	{
		List<HashMap<String, CandidateToken>> candidateString = filter(firstLine);
		calculateProperties(candidateString);
		return generateSecondLines(candidateString, max);

	}
}