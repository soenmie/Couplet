package cn.edu.tsinghua.linguistic.ui;

import static javax.swing.JSplitPane.TOP;
import static javax.swing.JSplitPane.BOTTOM;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class MainFrame extends JFrame
{

	private static final long serialVersionUID = 2518408287879834059L;
	private static final int WIDTH = 400;
	private static final int HEIGHT = 600;
	private static final String TITLE = "基于《全唐诗》中对仗诗句的对联系统";

	public MainFrame()
	{
		setTitle(TITLE);
		setSize(WIDTH, HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(VERTICAL_SPLIT);
		splitPane.setDividerLocation(0.5);

		BottomPanel bottomPanel = new BottomPanel();
		splitPane.add(new TopPanel(bottomPanel), TOP);
		splitPane.add(bottomPanel, BOTTOM);
		add(splitPane);
	}
}
