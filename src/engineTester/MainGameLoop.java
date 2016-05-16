package engineTester;

import org.lwjgl.opengl.Display;
import rederEngine.DisplayManager;
import rederEngine.Loader;
import rederEngine.RawModel;
import rederEngine.Renderer;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
				float[] vertices = {
					-0.5f,0.5f,0, //v1
					-0.5f,-0.5f,0, //v2
					0.5f,-0.5f,0, //v3
					0.5f,0.5f,0 //v3
		};
				int[] indices ={
						0,1,3, //top triagnle
						3,1,2 //Botton right Triangle
				};
		
		RawModel model = loader.loadtoVAO(vertices,indices);
				
		while(!Display.isCloseRequested()){
			renderer.prepare();
			//gamelogic
			renderer.render(model);
			DisplayManager.updateDisplay();
			
		}
		DisplayManager.closeDisplay();
	}

}
