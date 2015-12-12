package com.christopherdcanfield.desktop;

import java.awt.DisplayMode;

import com.google.common.base.Objects;

public class DisplayModeWrapper implements Comparable<DisplayModeWrapper>
{
	public final DisplayMode underlying;
	private final int originalOrder;

	public DisplayModeWrapper(DisplayMode underlyingDisplayMode, int originalOrder)
	{
		this.underlying = underlyingDisplayMode;
		this.originalOrder = originalOrder;
	}

	@Override
	public int compareTo(DisplayModeWrapper other)
	{
		return Integer.compare(originalOrder, other.originalOrder);
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof DisplayModeWrapper)) {
			return false;
		}
		DisplayModeWrapper other = (DisplayModeWrapper)o;
		return (underlying.getWidth() == other.underlying.getWidth() &&
				underlying.getHeight() == other.underlying.getHeight() &&
				underlying.getRefreshRate() == other.underlying.getRefreshRate() &&
				underlying.getBitDepth() == other.underlying.getBitDepth());
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(underlying.getWidth(),
				underlying.getHeight(),
				underlying.getRefreshRate(),
				underlying.getBitDepth());
	}

	@Override
	public String toString()
	{
		return underlying.getWidth() + "x" + underlying.getHeight() +
				" " + underlying.getRefreshRate() + " Hz " + underlying.getBitDepth() + " bpp";
	}
}
