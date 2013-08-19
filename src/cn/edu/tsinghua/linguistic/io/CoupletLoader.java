package cn.edu.tsinghua.linguistic.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import cn.edu.tsinghua.linguistic.ds.CoupletData;
import cn.edu.tsinghua.linguistic.lex.Segmenter;

public class CoupletLoader
{
	private static boolean inCache = false;

	public static boolean isInCache()
	{
		return inCache;
	}

	public static void readFile(String fileName)
	{
		BufferedReader reader = null;
		CoupletData coupletData = CoupletData.getInstance();
		try
		{
			reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null)
			{
				List<String> firstLineTokens = Segmenter.tokenize(line);
				String secondLine = reader.readLine();
				List<String> secondLineTokens = Segmenter.tokenize(secondLine);

				if (firstLineTokens.size() > 0)
				{
					String first = firstLineTokens.get(0);
					int beginIndex = 0;
					int endIndex = first.length();
					String second = secondLine.substring(beginIndex, endIndex);
					coupletData.head(first);
					coupletData.head(second);
					if (endIndex != 1)
					{
						coupletData.head(new String(new char[]
						{ first.charAt(0) }));
						coupletData.head(new String(new char[]
						{ second.charAt(0) }));
					}
					int size = firstLineTokens.size();
					for (int i = 0; i < size - 1; i++)
					{
						first = firstLineTokens.get(i);

						second = secondLine.substring(beginIndex, endIndex);
						String firstNext = firstLineTokens.get(i + 1);

						beginIndex = endIndex;
						endIndex += firstNext.length();

						String secondNext = secondLine.substring(beginIndex,
								endIndex);
						coupletData.put(first, second, firstNext);
						coupletData.put(second, first, secondNext);

						for (int j = 0; j < first.length() - 1; j++)
						{
							coupletData.put(new String(new char[]
							{ first.charAt(j) }), new String(new char[]
							{ second.charAt(j) }), new String(new char[]
							{ first.charAt(j + 1) }));

							coupletData.put(new String(new char[]
							{ second.charAt(j) }), new String(new char[]
							{ first.charAt(j) }), new String(new char[]
							{ second.charAt(j + 1) }));
						}
						if (first.length() > 1)
						{
							coupletData.put(new String(new char[]
							{ first.charAt(first.length() - 1) }), new String(
									new char[]
									{ second.charAt(second.length() - 1) }),
									new String(new char[]
									{ firstNext.charAt(0) }));

							coupletData.put(new String(new char[]
							{ second.charAt(second.length() - 1) }),
									new String(new char[]
									{ first.charAt(first.length() - 1) }),
									new String(new char[]
									{ secondNext.charAt(0) })

							);
						}
					}

					first = firstLineTokens.get(size - 1);
					second = secondLine.substring(beginIndex, endIndex);
					coupletData.tail(first, second);
					coupletData.tail(second, first);
					for (int i = 0; i < first.length() - 1; i++)
					{
						coupletData.put(new String(new char[]
						{ first.charAt(i) }), new String(new char[]
						{ second.charAt(i) }), new String(new char[]
						{ first.charAt(i + 1) }));

						coupletData.put(new String(new char[]
						{ second.charAt(i) }), new String(new char[]
						{ first.charAt(i) }), new String(new char[]
						{ second.charAt(i + 1) }));
					}
					if (first.length() > 1)
					{
						coupletData.tail(new String(new char[]
						{ first.charAt(first.length() - 1) }), new String(
								new char[]
								{ second.charAt(second.length() - 1) }));

						coupletData.tail(new String(new char[]
						{ second.charAt(second.length() - 1) }), new String(
								new char[]
								{ first.charAt(first.length() - 1) }));
					}
				}
				if (secondLineTokens.size() > 0)
				{
					String second = secondLineTokens.get(0);
					int beginIndex = 0;
					int endIndex = second.length();
					String first = line.substring(beginIndex, endIndex);
					if (!firstLineTokens.contains(first))
					{
						coupletData.head(first);
						coupletData.head(second);
					}

					int size = secondLineTokens.size();
					for (int i = 0; i < size - 1; i++)
					{
						second = secondLineTokens.get(i);

						first = line.substring(beginIndex, endIndex);
						String secondNext = secondLineTokens.get(i + 1);

						beginIndex = endIndex;
						endIndex += secondNext.length();

						String firstNext = line.substring(beginIndex, endIndex);
						if (!firstLineTokens.contains(first))
						{
							coupletData.put(first, second, firstNext);
							coupletData.put(second, first, secondNext);
						}
					}

					second = secondLineTokens.get(size - 1);
					first = line.substring(beginIndex, endIndex);
					if (!firstLineTokens.contains(first))
					{
						coupletData.tail(first, second);
						coupletData.tail(second, first);
					}

				}
			}
			reader.close();
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
		inCache = true;
	}
}
