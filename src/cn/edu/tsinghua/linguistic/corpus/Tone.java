package cn.edu.tsinghua.linguistic.corpus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Tone
{
	public static final String TONE_FILE = "corpus/tone.dict";

	private static volatile Map<Character, Integer> TONE_MAP = null;

	static
	{
		if (TONE_MAP == null)
		{
			synchronized (Tone.class)
			{
				if (TONE_MAP == null)
				{
					TONE_MAP = new HashMap<Character, Integer>();
					BufferedReader reader = null;
					try
					{
						reader = new BufferedReader(new FileReader(TONE_FILE));
						String line;
						while ((line = reader.readLine()) != null)
						{
							StringTokenizer tokenizer = new StringTokenizer(
									line);
							while (tokenizer.hasMoreTokens())
							{
								TONE_MAP
										.put(tokenizer.nextToken().charAt(0),
												Integer.parseInt(tokenizer
														.nextToken()));
							}
						}
					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					finally
					{
						if (reader != null)
						{
							try
							{
								reader.close();
							}
							catch (IOException e)
							{
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	public static boolean isMatch(char c0, char c1)
	{
		try
		{
			int pattern0 = TONE_MAP.get(c0);
			int pattern1 = TONE_MAP.get(c1);

			if (isFlat(pattern0))
			{
				if (isFlat(pattern1))
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			else
			{
				if (isFlat(pattern1))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		catch (NullPointerException e)
		{
			return true;
		}
	}

	private static boolean isFlat(int tonalpattern)
	{
		if (tonalpattern == 1 || tonalpattern == 2)
		{
			return true;
		}
		return false;
	}

	public static boolean isFlat(char character)
	{
		return isFlat(TONE_MAP.get(character));
	}

	private static boolean isNarrow(int tonalpattern)
	{
		if (tonalpattern == 3 || tonalpattern == 4)
		{
			return true;
		}
		return false;
	}

	public static boolean isNarrow(char character)
	{
		return isNarrow(TONE_MAP.get(character));
	}

	public static boolean isOnAntithesisPosition(int position)
	{
		if (position == 1 || position == 3 || position == 5)
		{
			return true;
		}
		return false;
	}
}
