package cn.edu.tsinghua.linguistic.ui;

import static cn.edu.tsinghua.linguistic.corpus.CoupletExtracter.COUPLET_FILE;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import cn.edu.tsinghua.linguistic.cfg.Configuration;
import cn.edu.tsinghua.linguistic.core.Matcher;
import cn.edu.tsinghua.linguistic.exception.NonMatchException;
import cn.edu.tsinghua.linguistic.exception.UncommonWordException;
import cn.edu.tsinghua.linguistic.io.CoupletLoader;

public class TopPanel extends JPanel
{

	private static final long serialVersionUID = 7220736482051655848L;
	private JTextField inputField;
	private JCheckBox segmentedCheckBox = new JCheckBox("分词");
	private JCheckBox toneMatchedCheckBox = new JCheckBox("平仄对仗");
	private JCheckBox backwardCheckBox = new JCheckBox("反向计算概率");
	private JCheckBox hiddenMarkovModeledCheckBox = new JCheckBox("隐Markov模型");

	private JButton button = new JButton("对下联");

	public TopPanel(final BottomPanel bottomPanel)
	{

		setLayout(new GridLayout(2, 1));

		JPanel textPanel = new JPanel();
		textPanel.add(new JLabel("请输入上联："));

		inputField = new JTextField();
		inputField.setColumns(18);
		textPanel.add(inputField);

		button.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{

				String firstLine = inputField.getText().trim();
				if (firstLine.equals(""))
				{
					JOptionPane.showMessageDialog(null, "上联为空，请输入！", "错误",
							ERROR_MESSAGE);
					return;
				}

				try
				{
					Matcher.setConfiguration(new Configuration(
							segmentedCheckBox.isSelected(), toneMatchedCheckBox
									.isSelected(), backwardCheckBox
									.isSelected(), hiddenMarkovModeledCheckBox
									.isSelected()));
					button.setEnabled(false);

					if (!CoupletLoader.isInCache())
					{
						bottomPanel.setStatusText("正在加载语料库……");
						CoupletLoader.readFile(COUPLET_FILE);
						bottomPanel.setStatusText("语料库加载成功！");
					}
					bottomPanel.setStatusText("正在计算下联……");
					long startTime = System.nanoTime();
					bottomPanel
							.showSecondLines(Matcher.OnSecondLine(firstLine));
					long endTime = System.nanoTime();
					bottomPanel.setStatusText("共用时间" + (endTime - startTime)
							/ Math.pow(10, 9) + "秒");
					button.setEnabled(true);

				}
				catch (NonMatchException ex)
				{
					JOptionPane.showMessageDialog(null, "抱歉，不能对出下联！", "信息",
							INFORMATION_MESSAGE);
					button.setEnabled(true);
				}
				catch (UncommonWordException ex)
				{
					JOptionPane.showMessageDialog(null, "抱歉，上联含有生僻字！", "警告",
							WARNING_MESSAGE);
					button.setEnabled(true);
				}
			}
		});

		textPanel.add(button);

		JPanel configPanel = new JPanel();
		configPanel.setBorder(new TitledBorder("对联选项"));
		configPanel.add(segmentedCheckBox);
		configPanel.add(toneMatchedCheckBox);
		configPanel.add(backwardCheckBox);
		configPanel.add(hiddenMarkovModeledCheckBox);

		add(textPanel);
		add(configPanel);

	}

}
