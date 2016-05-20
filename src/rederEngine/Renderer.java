package rederEngine;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import toolbox.Maths;

import entities.Entity;

public class Renderer {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projectionMatrix;
	
	//é necessario criar a ProjectionMatrix apenas uma vez, e ela nunca muda, por isso foi chamada
	//no Renderer
	public Renderer(StaticShader shader){
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);//Testa quais os vertex e planos estão à frente dos outros
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);//Apaga os buffers
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	public void render(Entity entity, StaticShader shader) {
		TexturedModel model = entity.getModel();//recebe o modelo completo
		RawModel rawModel = model.getRawModel();//pega os vertex do modelo completo
		GL30.glBindVertexArray(rawModel.getVaoID());//salva no VAO o modelo
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		//cria a matriz de tranformação dado os valores de posicao atual, além das novas transformacoes
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());	
		shader.loadTransformationMatrix(transformationMatrix);
		
		//coloc as texturas em cada parte
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		
		//desenha
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	//croa a matriz de projecao que sempre sera a mesma. (eh uma matriz 4x4 que da a nocao de profundidade 3d vista pela camera)
	private void createProjectionMatrix(){
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

}
