package es.upm.dit.adsw.pract1;

/**
 * Las cuatro direcciones cardinales.
 *
 * @author Jose A. Manas
 * @version 20.3.2013
 */
public enum Direccion {
    NORTE, SUR, ESTE, OESTE;

    /**
     * Elije una direccion al azar.
     *
     * @return una de las direcciones posibles.
     */
    public static Direccion random() {
        Direccion[] direcciones = values();
        int i = (int) (direcciones.length * Math.random());
        return direcciones[i];
    }
}
