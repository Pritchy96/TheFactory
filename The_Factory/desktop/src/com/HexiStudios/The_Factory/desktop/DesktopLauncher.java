package com.HexiStudios.The_Factory.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.GenericStudios.TheCandyFactory.Manager;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	      config.title = "Drop";
	      config.width = 513;
	      config.height = 768;
	      config.fullscreen = false;
		new LwjglApplication(new Manager(new ActionResolverDesktop()), config);
	}
}
