package cn.edu.tsinghua.linguistic.ui;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.WEST;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

public class BottomPanel extends JPanel
{
	private static final long serialVersionUID = -6770789846462102956L;
	private JTextArea secondLineList = new JTextArea();
	private JLabel label = new JLabel("欢迎使用");

	public void showSecondLines(List<String> secondLines)
	{
		StringBuffer text = new StringBuffer();

		for (String line : secondLines)
		{
			text.append(line).append("\r\n");
		}
		secondLineList.setText(text.toString());

	}

	public BottomPanel()
	{
		setLayout(new BorderLayout());

		secondLineList.setBackground(getBackground());

		add(secondLineList);
		JScrollPane scrollPane = new JScrollPane(secondLineList);
		add(scrollPane, CENTER);

		JPanel statusBar = new JPanel();

		statusBar.setLayout(new BorderLayout());
		statusBar.setBorder(new EtchedBorder());
		statusBar.add(label, WEST);

		add(statusBar, SOUTH);

	}

	public void setStatusText(String text)
	{
		label.setText(text);
	}
}