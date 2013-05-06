package es.upm.dit.adsw.pract1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Una celda del laberinto.
 *
 * @author Jose A. Manas
 * @version 20.3.2013
 */
public class Celda {
    private final PuntoXY punto;
    private final Celda[] conexiones;
    private final boolean[] paredes;
    private Estado estado = Estado.VACIA;
    private Tipo tipo = Tipo.NORMAL;

    /**
     * Constructor.
     *
     * @param x coordenada X.
     * @param y coordenada Y.
     */
    public Celda(int x, int y) {
        punto = new PuntoXY(x, y);
        conexiones = new Celda[Direccion.values().length];
        paredes = new boolean[Direccion.values().length];
        Arrays.fill(paredes, true);
    }

    /**
     * Getter.
     *
     * @return punto donde esta la ceda.
     */
    public PuntoXY getPunto() {
        return punto;
    }

    /**
     * Getter.
     *
     * @return estado de la celda.
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * Setter.
     *
     * @param estado estado de la celda.
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    /**
     * Getter.
     *
     * @return tipo de celda.
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * Setter.
     *
     * @param tipo tipo de celda.
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    /**
     * Pone a la celda en su contexto: se conecta a otra celda en una cierta direccion.
     *
     * @param direccion en la que esta la otra celda.
     * @param celda     la celda a la que nos conectamos.
     */
    public void conecta(Direccion direccion, Celda celda) {
        conexiones[direccion.ordinal()] = celda;
    }

    /**
     * Getter.
     *
     * @param direccion direccion en la que buscamos.
     * @return celda en esa direccion. Puede ser null si no hay mas celdas, por ejemplo en el borde.
     */
    public Celda getCelda(Direccion direccion) {
        return conexiones[direccion.ordinal()];
    }

    /**
     * Levanta una pared entre dos celdas.
     *
     * @param celda1 una celda.
     * @param celda2 otra celda.
     */
    public static void ponPared(Celda celda1, Celda celda2) {
        celda1.ponPared(celda2);
        celda2.ponPared(celda1);
    }

    /**
     * Derrumba una pared entre dos celdas.
     *
     * @param celda1 una celda.
     * @param celda2 otra celda.
     */
    public static void quitaPared(Celda celda1, Celda celda2) {
        celda1.quitaPared(celda2);
        celda2.quitaPared(celda1);
    }

    /**
     * Levanta una pared entre THIS y otra celda.
     *
     * @param celda2 otra celda.
     */
    private void ponPared(Celda celda2) {
        for (Direccion direccion : Direccion.values()) {
            Celda c = conexiones[direccion.ordinal()];
            if (c != null && c.equals(celda2))
                paredes[direccion.ordinal()] = true;
        }
    }

    /**
     * Derrumba una pared entre THIS y otra celda.
     *
     * @param celda2 otra celda.
     */
    private void quitaPared(Celda celda2) {
        for (Direccion direccion : Direccion.values()) {
            Celda c = conexiones[direccion.ordinal()];
            if (c != null && c.equals(celda2))
                paredes[direccion.ordinal()] = false;
        }
    }

    /**
     * Mira si hay una pared entre THIS y otra celda.
     *
     * @param celda2 otra celda.
     * @return TRUE si hay una pared; FALSE si no la hay.
     */
    public boolean hayPared(Celda celda2) {
        for (Direccion direccion : Direccion.values()) {
            Celda c = conexiones[direccion.ordinal()];
            if (c != null && c.equals(celda2))
                return paredes[direccion.ordinal()];
        }
        return true;
    }

    /**
     * Mira si la celda tiene una pared en una cierta direccion.
     *
     * @param direccion direccion en la que puede haber una pared.
     * @return TRUE si hay una pared; FALSE si no la hay.
     */
    public boolean hayPared(Direccion direccion) {
        return paredes[direccion.ordinal()];
    }

    /**
     * Lista de paredes alrededor de la celda.
     *
     * @return ista de paredes alrededor de la celda.
     */
    public Collection<Pared> getParedes() {
        List<Pared> list = new ArrayList<Pared>();
        for (Direccion direccion : Direccion.values()) {
            if (hayPared(direccion)) {
                Celda celda2 = getCelda(direccion);
                list.add(new Pared(this, celda2));
            }
        }
        return list;
    }

    /**
     * Para imprimir.
     *
     * @return mensaje para imprimir.
     */
    public String toString() {
        return punto.toString();
    }

    /**
     * Compara con otra cosa.
     *
     * @param o objeto a comparar.
     * @return TRUE si THIS y o son equivalentes.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Celda celda = (Celda) o;
        return punto.equals(celda.punto);
    }

    /**
     * @return Identificador unico.
     */
    @Override
    public int hashCode() {
        return punto.hashCode();
    }
}
