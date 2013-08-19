package cn.edu.tsinghua.linguistic.cfg;

public class Configuration
{
	private boolean segmented = true;
	private boolean toneMatched = true;
	private boolean backward = true;
	private boolean hiddenMarkovModeled = true;

	public Configuration(boolean segmented, boolean toneMatched,
			boolean backward, boolean hiddenMarkovModeled)
	{
		super();
		this.segmented = segmented;
		this.toneMatched = toneMatched;
		this.backward = backward;
		this.hiddenMarkovModeled = hiddenMarkovModeled;
	}

	public Configuration()
	{
	}

	public boolean isSegmented()
	{
		return segmented;
	}

	public boolean isToneMatched()
	{
		return toneMatched;
	}

	public boolean isBackward()
	{
		return backward;
	}

	public boolean isHiddenMarkovModeled()
	{
		return hiddenMarkovModeled;
	}
}
