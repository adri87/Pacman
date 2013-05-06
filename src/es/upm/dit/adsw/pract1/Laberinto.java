package es.upm.dit.adsw.pract1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * El laberinto en el que se mueve el jugador y los bichos.
 * Los bichos no pueden interactuar: si una celda esta ocupada por un bicho, otro bicho no puede saltar a esa celda.
 * Pero si el jugador se mueve a una celda con bicho, o un bicho llega a la celda del jugador,
 * el bicho se come al jugador y se acaba el juego.
 *
 * @author Jose A. Manas
 * @version 20.3.2013
 */
public class Laberinto {
    private final int lado;
    private final GUI gui;

    private final Monitor monitor;

    private Celda[][] celdas;
    private final List<BichoBase> bichos = new ArrayList<BichoBase>();
    private final Jugador jugador;

    /**
     * Laberinto para pruebas.
     * Es de tamano fijo y las paredes son siempre las mismas.
     */
    public Laberinto() {
        this.lado = 5;
        this.gui = null;
        this.monitor = null;
        this.jugador = null;

        // crea e interconecta todas las celdas
        preparaCeldas();

        // abre algunas paredes
        // +-+-+-+-+-+
        // | | | | | |
        // +-+-+-+-+-+
        // | | | | | |
        // +-+-+ +-+-+
        // |     | | |
        // + +-+ +-+-+
        // |   |     |
        // + + +-+-+ +
        // | |       |
        // +-+-+-+-+-+
        Celda.quitaPared(celdas[0][0], celdas[0][1]);
        Celda.quitaPared(celdas[1][0], celdas[2][0]);
        Celda.quitaPared(celdas[1][0], celdas[1][1]);
        Celda.quitaPared(celdas[2][0], celdas[3][0]);
        Celda.quitaPared(celdas[3][0], celdas[4][0]);
        Celda.quitaPared(celdas[4][0], celdas[4][1]);

        Celda.quitaPared(celdas[0][1], celdas[0][2]);
        Celda.quitaPared(celdas[0][1], celdas[1][1]);
        Celda.quitaPared(celdas[2][1], celdas[3][1]);
        Celda.quitaPared(celdas[2][1], celdas[2][2]);
        Celda.quitaPared(celdas[3][1], celdas[4][1]);

        Celda.quitaPared(celdas[0][2], celdas[1][2]);
        Celda.quitaPared(celdas[1][2], celdas[2][2]);
        Celda.quitaPared(celdas[2][2], celdas[2][3]);
    }

    /**
     * Constructor.
     * Genera un laberinto aleatorio.
     *
     * @param lado numero de celdas en vertical y en horizontal.
     * @param gui  para pintar en la ventana.
     */
    public Laberinto(int lado, GUI gui) {
        this.lado = lado;
        this.gui = gui;

        monitor = new Monitor(this);

        preparaCeldas();

        Celda salida = getCelda(0, 0);
        Celda llave = getCelda(PuntoXY.random(lado));
        llave.setTipo(Tipo.LLAVE);
        Celda meta = getCelda(lado - 1, lado - 1);
        meta.setTipo(Tipo.META);
        jugador = new Jugador(this, salida);

        generaMinimo();

        // tira algunas paredes mas ...
        int nmas = lado;
        while (nmas > 0) {
            Celda celda1 = getCelda(PuntoXY.random(lado));
            Direccion direccion = Direccion.random();
            Celda celda2 = celda1.getCelda(direccion);
            if (celda2 != null && celda1.hayPared(celda2)) {
                Celda.quitaPared(celda1, celda2);
                nmas--;
            }
        }
    }

    /**
     * Auxiliar.
     * Crea todas las celdas y las interconecta.
     */
    private void preparaCeldas() {
        celdas = new Celda[lado][lado];
        for (int x = 0; x < lado; x++) {
            for (int y = 0; y < lado; y++) {
                celdas[x][y] = new Celda(x, y);
            }
        }
        for (int x = 0; x < lado; x++) {
            for (int y = 0; y < lado; y++) {
                Celda celda = celdas[x][y];
                if (0 < x)
                    celda.conecta(Direccion.OESTE, celdas[x - 1][y]);
                if (x + 1 < lado)
                    celda.conecta(Direccion.ESTE, celdas[x + 1][y]);
                if (0 < y)
                    celda.conecta(Direccion.SUR, celdas[x][y - 1]);
                if (y + 1 < lado)
                    celda.conecta(Direccion.NORTE, celdas[x][y + 1]);
            }
        }
    }

    /**
     * Algoritmo de PRIM.
     * Quita el numero minimo de paredes para que todas las celdas esten conectadas.
     */
    private void generaMinimo() {
        Celda origen = getCelda(PuntoXY.random(lado));
//        origen.setEstado(Color.CYAN);

        Set<Celda> enganchadas = new HashSet<Celda>();
        enganchadas.add(origen);

        List<Pared> paredes = new ArrayList<Pared>();
        paredes.addAll(origen.getParedes());

        while (paredes.size() > 0) {
            int random = (int) (paredes.size() * Math.random());
            Pared pared = paredes.remove(random);
            Celda celda1 = pared.getCelda1();
            Celda celda2 = pared.getCelda2();
            if (celda2 == null)
                continue;
            if (!enganchadas.contains(celda2)) {
                Celda.quitaPared(celda1, celda2);
                enganchadas.add(celda2);
                for (Pared pared1 : celda2.getParedes()) {
                    if (!paredes.contains(pared))
                        paredes.add(pared1);
                }
            }
        }
    }

    /**
     * Getter.
     *
     * @return numero de celdas en horizontal o en vertical.
     */
    public int getLado() {
        return lado;
    }

    /**
     * Getter.
     *
     * @return el jugador.
     */
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * Getter.
     *
     * @return el monitor que controla movimientos.
     */
    public Monitor getMonitor() {
        return monitor;
    }

    /**
     * Mete un bicho en la lista.
     *
     * @param bicho nuevo bicho.
     */
    public void add(BichoBase bicho) {
        bichos.add(bicho);
    }

    /**
     * Getter.
     *
     * @return lista de bichos vivos.
     */
    public List<BichoBase> getBichos() {
        return bichos;
    }

    /**
     * Getter.
     *
     * @param punto un punto (fila, columna).
     * @return celda en ese punto.
     */
    public Celda getCelda(PuntoXY punto) {
        return celdas[punto.getX()][punto.getY()];
    }

    /**
     * Getter.
     *
     * @param x abscisa (fila).
     * @param y ordenada (columna).
     * @return celda en ese punto.
     */
    public Celda getCelda(int x, int y) {
        return celdas[x][y];
    }

    /**
     * Busca una conexion desde la celda1 en la direccion indicada.
     *
     * @param celda1    celda desde la que queremos movernos.
     * @param direccion direccion en la que queremos movernos.
     * @return NULL si nos salimos del laberinto o si hay una pared.<br>
     *         Si podemos movernos, devuelve la celda a la que vamos.
     */
    public Celda getConexion(Celda celda1, Direccion direccion) {
        if (celda1.hayPared(direccion))
            return null;
        Celda celda2 = celda1.getCelda(direccion);
        if (celda2 == null)
            return null;
        return celda2;
    }

    /**
     * Le dice a la interfaz de usuario que seria conveniente repintar.
     */
    public void pinta() {
        gui.pintame();
    }

    /**
     * Se acabo el juego: el jugador ha llegado a la meta fijada.
     */
    public void jugadorGana() {
        // para los bichos del laberinto actual
        for (BichoBase bicho : getBichos())
            bicho.kill();
        gui.fin("jugador gana");
    }

    /**
     * Se acabo el juego: el jugador ha tropezado con un bicho.
     */
    public void jugadorPierde() {
        // para los bichos del laberinto actual
        for (BichoBase bicho : getBichos())
            bicho.kill();
        gui.fin("jugador pierde");
    }

    /**
     * Elimina los cepos.
     * Las celdas de tipo CEPO pasan a celdas de tipo NORMAL.
     */
    public void limpiaCepos() {
        for (int x = 0; x < lado; x++) {
            for (int y = 0; y < lado; y++) {
                Celda celda = getCelda(x, y);
                if (celda.getTipo() == Tipo.CEPO)
                    celda.setTipo(Tipo.NORMAL);
            }
        }
    }
}
