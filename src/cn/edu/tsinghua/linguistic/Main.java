package cn.edu.tsinghua.linguistic;

import static cn.edu.tsinghua.linguistic.corpus.CoupletExtracter.COUPLET_FILE;
import static cn.edu.tsinghua.linguistic.corpus.CoupletExtracter.POETRY_FILE;

import java.io.File;

import cn.edu.tsinghua.linguistic.corpus.CoupletExtracter;
import cn.edu.tsinghua.linguistic.ui.MainFrame;

public class Main
{

	public static void main(String[] args)
	{
		if (!new File(COUPLET_FILE).exists())
		{
			CoupletExtracter.extractCoupletsFromPoetry(POETRY_FILE,
					COUPLET_FILE);
		}
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				new MainFrame();
			}
		}).start();
	
	}

}
