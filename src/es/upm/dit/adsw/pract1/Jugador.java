package es.upm.dit.adsw.pract1;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * El jugador.
 *
 * @author Jose A. Manas
 * @version 20.3.2013
 */
public class Jugador {
    private final Image imagen;
    private final Laberinto laberinto;
    private Celda celda;

    /**
     * Constructor.
     *
     * @param laberinto el laberinto en el que se mueve.
     * @param salida    posicion inicial.
     */
    public Jugador(Laberinto laberinto, Celda salida) {
        this.laberinto = laberinto;
        this.celda = salida;
        salida.setEstado(Estado.JUGADOR);
        imagen = loadImage("Fran1.png");
    }

    /**
     * Carga una imagen de un fichero.
     *
     * @param fichero con la imagen.
     * @return imagen en formato java.
     */
    private static Image loadImage(String fichero) {
        Class<Jugador> jugadorClass = Jugador.class;
        try {
            URL url = jugadorClass.getResource("imgs/" + fichero);
            ImageIcon icon = new ImageIcon(url);
            return icon.getImage();
        } catch (Exception e) {
            System.err.println("no se puede cargar "
                    + jugadorClass.getPackage().getName()
                    + System.getProperty("file.separator")
                    + fichero);
            return null;
        }
    }

    /**
     * Getter.
     *
     * @return imagen.
     */
    public Image getImagen() {
        return imagen;
    }

    /**
     * Getter.
     *
     * @return donde esta ahora.
     */
    public Celda getCelda() {
        return celda;
    }

    /**
     * Se intenta mover en la direccion indicada.
     *
     * @param direccion en la que desea moverse.
     */
    public void intentaMover(Direccion direccion) {
        try {
            // calculamos la celda a la que iriamos.
            Celda celda2 = laberinto.getConexion(celda, direccion);
            if (celda2 == null) {
                // no hay una celda alcanzable en esa direccion
                return;
            }

            // vamos a ver que hay en el tablero:
            // esto debe estar sincronizado
            // para que las fichas se esten quietas mientras miro
            Monitor monitor = laberinto.getMonitor();
            monitor.mueveJugador(this, celda2);
            // Si llegamos a la meta, se acaba el juego.
            if (celda.getTipo() == Tipo.META)
                laberinto.jugadorGana();
        } catch (JugadorComido jugadorComido) {
            //se acabo, hemos tropezado con un bicho
            laberinto.jugadorPierde();
        }
    }

    /**
     * Setter.
     * Ademas, vacia la celda actual y marca un jugador en la nueva.
     *
     * @param nueva a donde va.
     */
    public void setCelda(Celda nueva) {
        Celda anterior = this.celda;
        anterior.setEstado(Estado.VACIA);
        nueva.setEstado(Estado.JUGADOR);
        this.celda = nueva;
        laberinto.pinta();
    }
}
