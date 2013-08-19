package cn.edu.tsinghua.linguistic.corpus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import net.sf.json.JSONObject;

public class CoupletExtracter
{
	public static final String POETRY_FILE = "corpus/peotry.json";
	public static final String COUPLET_FILE = "corpus/couplet.dict";

	public static void extractCoupletsFromPoetry(String poetryFile,
			String coupletFile)
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(poetryFile));
			String line;
			while ((line = reader.readLine()) != null)
			{
				JSONObject poem = JSONObject.fromObject(line);
				String content = poem.getString("content");
				StringTokenizer tokenizer = new StringTokenizer(content, "。？！");
				while (tokenizer.hasMoreTokens())
				{
					String couplets[] = tokenizer.nextToken().split("，");
					if (isCouplets(couplets))
					{
						addToCoupletTable(couplets, coupletFile);
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
	}

	private static void addToCoupletTable(String[] couplets, String coupletFile)
	{
		try
		{
			PrintWriter out = new PrintWriter(new FileOutputStream(coupletFile));
			for (String couplet : couplets)
			{
				out.println(couplet);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	private static boolean isCouplets(String[] lines)
	{
		if (lines.length != 2 || lines[0].length() != lines[1].length())
		{
			return false;
		}

		return false;

	}

}
