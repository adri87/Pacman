package es.upm.dit.adsw.pract1;

import javax.swing.*;

/**
 * Lanza la aplicacion grafica, bien como applet o como aplicacion autocontenida.
 *
 * @author Jose A. Manas
 * @version 20.3.2013
 */
public class Pacman
        extends JApplet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Inicia el applet.
     */
    public void init() {
        new GUI(this);
    }

    /**
     * Metodo principal para arrancar desde consola.
     *
     * @param args No utiliza argumentos.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame(GUI.TITULO);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        new GUI(frame);
    }
}