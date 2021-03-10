package gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import shaders.Renderer;

/**
 * The main class with respective GUI components.
 * 
 * @author serhiy
 */
public class Main {
	public static void main(String[] args) {
		final GLProfile glProfile = GLProfile.getDefault();
		final GLCapabilities glCapabilities = new GLCapabilities(glProfile);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame jFrame = new JFrame ("Cube Example");
				jFrame.setSize(640, 480);
				
				GLJPanel glJPanel = new GLJPanel(glCapabilities);
				Renderer renderer = new Renderer(glJPanel);
				glJPanel.addGLEventListener(renderer);
				glJPanel.setSize(jFrame.getSize());

				// вешаем обработчики
				glJPanel.addKeyListener(renderer);
				glJPanel.addMouseListener(renderer);
				glJPanel.addMouseMotionListener(renderer);

				jFrame.getContentPane().add(glJPanel);
				
				jFrame.setVisible(true);
			}
		});
	}
}
