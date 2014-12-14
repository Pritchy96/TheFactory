package com.HexiStudios.The_Factory;

public class BasicState {
	
	protected Manager manager;

		public BasicState(Manager manager)
		{
			this.manager = manager;
		}
	   
	   public void draw() {
	   }
	   
	   public void drawGUI() {
	   }   
	   
	   public void update() {
	   }
		
	   public void touchDown(int screenX, int screenY, int pointer, int button) {
	   }
		
	   public void keyDown(int keycode) {
	   }
			
	   public void dispose() {
	   }

	   public void resize(int width, int height) {
	   }

	   public void pause() {
	   }

	   public void resume() {
	   }
	}