package cn.edu.tsinghua.linguistic.lex;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class Segmenter
{
	public static List<String> tokenize(String scentence)
	{
		Reader reader = new StringReader(scentence);
		IKSegmenter segmenter = new IKSegmenter(reader, true);
		Lexeme lexeme;
		List<String> tokens = new ArrayList<String>();

		try
		{
			while ((lexeme = segmenter.next()) != null)
			{
				tokens.add(lexeme.getLexemeText());
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
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
		return tokens;
	}
}
