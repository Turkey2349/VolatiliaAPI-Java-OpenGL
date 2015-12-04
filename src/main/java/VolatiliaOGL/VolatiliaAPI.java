package main.java.VolatiliaOGL;

import main.java.VolatiliaOGL.renderEngine.MasterRenderer;
import main.java.VolatiliaOGL.screen.ScreenManager;
import main.java.VolatiliaOGL.util.Loader;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class VolatiliaAPI
{
	public static final String VERSION = "Indev 1.1.1";
	public static VolatiliaAPI instance;

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 60;

	private static long lastFrameTime;
	private static float delta;

	public static void createDisplay()
	{
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
		try
		{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("TEST API - Version: " + VolatiliaAPI.VERSION);
		} catch(LWJGLException e)
		{
			e.printStackTrace();
		}

		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}

	public static void updateDisplay()
	{
		Display.sync(FPS_CAP);
		Display.update();

		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}

	public static void startAPIRun()
	{
		while(!Display.isCloseRequested())
		{
			ScreenManager.getInstance().getCurrentScreen().render();

			VolatiliaAPI.updateDisplay();
		}

		cleanUpGame();
	}

	public static void cleanUpGame()
	{
		ScreenManager.getInstance().finalCleanUpAllScreens();
		
		MasterRenderer.INSTANCE.cleanUp();

		Loader.INSTANCE.CleanUp();

		VolatiliaAPI.closeDisplay();
	}

	public static void closeDisplay()
	{
		Display.destroy();
	}

	private static long getCurrentTime()
	{
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

	public static float getFrameTimeSeconds()
	{
		return delta;
	}
}