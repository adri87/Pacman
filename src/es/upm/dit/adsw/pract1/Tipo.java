package es.upm.dit.adsw.pract1;

import java.awt.*;

/**
 * Tipo de celda en el laberinto.<br>
 * META - la meta del jugador. <br>
 * CEPO - donde si cae un bicho queda atrapado. <br>
 * LLAVE - donde si pila el jugador, libera los cepos. <br>
 * NORMAL - las celdas que no tienen nada especial. <br>
 *
 * @author Jose A. Manas
 * @version 20.3.2013
 */
public enum Tipo {
    NORMAL(Color.WHITE), META(Color.GREEN), CEPO(Color.BLACK), LLAVE(Color.CYAN);

    private final Color color;

    Tipo(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}