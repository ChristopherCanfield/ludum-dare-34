package com.christopherdcanfield;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class App
{
	public static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);
}
