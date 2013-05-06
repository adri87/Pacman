package es.upm.dit.adsw.pract1;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Codigo basico para un bicho.
 *
 * @author Jose A. Manas
 * @version 22.3.2013
 */
public class BichoBase {
    /**
     * controla la frecuencia de movimiento de todos los bichos.
     */
    public static final int DT = 300;

    private static int cuenta = 0;
    private static String[] imagenes = {
            "FantasmaRojo.png",
            "FantasmaRosa.png",
            "FantasmaVerde.png",
            "FantasmaAmarillo.png",
    };
    private final Image imagen;

    protected final Laberinto laberinto;
    private boolean vivo = true;
    protected Celda celda;

    /**
     * Constructor.
     *
     * @param celda     celda inicial.
     * @param laberinto laberinto en el que se mueve.
     */
    public BichoBase(Celda celda, Laberinto laberinto) {
        this.celda = celda;
        this.laberinto = laberinto;
        imagen = loadImage(imagenes[cuenta % imagenes.length]);
        cuenta++;
    }

    /**
     * Constructor.
     *
     * @param celda     celda inicial.
     * @param laberinto laberinto en el que se mueve.
     * @param fichero   imagen del bicho.
     */
    public BichoBase(Celda celda, Laberinto laberinto, String fichero) {
        this.celda = celda;
        this.laberinto = laberinto;
        imagen = loadImage(fichero);
    }

    /**
     * Carga una imagen de un fichero.
     *
     * @param fichero con la imagen.
     * @return imagen en formato java.
     */
    private static Image loadImage(String fichero) {
        Class<BichoBase> bichoBaseClass = BichoBase.class;
        try {
            URL url = bichoBaseClass.getResource("imgs/" + fichero);
            ImageIcon icon = new ImageIcon(url);
            return icon.getImage();
        } catch (Exception e) {
            System.err.println("no se puede cargar "
                    + bichoBaseClass.getPackage().getName()
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
     * Para que el bicho muera: termina la ejecucion.
     */
    public void kill() {
        this.vivo = false;
    }

    /**
     * Getter.
     *
     * @return true si esta vivo y debe seguir ejecutando.
     */
    public boolean isVivo() {
        return vivo;
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
     * Setter.
     * Ademas vacia la celda actual y marca un bicho en la nueva.
     *
     * @param nueva a donde va.
     */
    public void setCelda(Celda nueva) {
        Celda anterior = this.celda;
        anterior.setEstado(Estado.VACIA);
        nueva.setEstado(Estado.BICHO);
        this.celda = nueva;
        laberinto.pinta();
    }
}
