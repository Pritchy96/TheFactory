package com.HexiStudios.The_Factory.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.HexiStudios.The_Factory.Manager;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	      config.title = "Drop";
	      config.width = 360;
	      config.height = 600;
	      config.fullscreen = false;
		new LwjglApplication(new Manager(new ActionResolverDesktop()), config);
	}
}
