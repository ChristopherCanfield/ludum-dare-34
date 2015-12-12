package com.christopherdcanfield.desktop;


import com.badlogic.gdx.Graphics.DisplayMode;
import com.google.common.base.Objects;

public class PrettifiedDisplayMode
{
	public final DisplayMode underlying;

	public PrettifiedDisplayMode(DisplayMode underlyingDisplayMode)
	{
		this.underlying = underlyingDisplayMode;
	}

//	@Override
//	public int compareTo(DisplayModeWrapper other)
//	{
//		return Integer.compare(originalOrder, other.originalOrder);
//	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof PrettifiedDisplayMode)) {
			return false;
		}
		PrettifiedDisplayMode other = (PrettifiedDisplayMode)o;
		return (underlying.width == other.underlying.width &&
				underlying.height == other.underlying.height &&
				underlying.refreshRate == other.underlying.refreshRate &&
				underlying.bitsPerPixel == other.underlying.bitsPerPixel);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(underlying.width,
				underlying.height,
				underlying.refreshRate,
				underlying.bitsPerPixel);
	}

	@Override
	public String toString()
	{
		return underlying.width + "x" + underlying.height +
				" " + underlying.refreshRate + " Hz " + underlying.bitsPerPixel + " bpp";
	}
}
