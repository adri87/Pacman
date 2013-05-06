package es.upm.dit.adsw.pract1;

/**
 * Excepcion que salta cuando un bicho y un jugador tropiezan.
 *
 * @author Jose A. Manas
 * @version 20.3.2013
 */
public class JugadorComido
        extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructor. Sin mensaje.
     */
    public JugadorComido() {
    }

    /**
     * Constructor.
     *
     * @param s mensaje explicativo.
     */
    public JugadorComido(String s) {
        super(s);
    }
}
