package es.upm.dit.adsw.pract1;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.*;

/**
 * Pruebas de la clase Buscador.
 *
 * @author Jose A. Manas
 * @version 15.4.2013
 */
public class TestBFS {
    private final Laberinto laberinto = new Laberinto();
    private final Buscador buscador = new Buscador(laberinto);

    @Test
    public void prueba01() {
        Celda origen = laberinto.getCelda(0, 1);
        Celda destino = laberinto.getCelda(0, 0);
        Celda esperada = laberinto.getCelda(0, 0);
        Celda calculada = buscador.bfs(origen, destino);
        assertEquals(esperada, calculada);
    }

    @Test
    public void prueba02() {
        Celda origen = laberinto.getCelda(0, 2);
        Celda destino = laberinto.getCelda(0, 0);
        Celda esperada = laberinto.getCelda(0, 1);
        Celda calculada = buscador.bfs(origen, destino);
        assertEquals(esperada, calculada);
    }

    @Test
    public void prueba03() {
        Celda origen = laberinto.getCelda(1, 2);
        Celda destino = laberinto.getCelda(0, 0);
        Celda esperada = laberinto.getCelda(0, 2);
        Celda calculada = buscador.bfs(origen, destino);
        assertEquals(esperada, calculada);
    }

    @Test
    public void prueba04() {
        Celda origen = laberinto.getCelda(2, 2);
        Celda destino = laberinto.getCelda(0, 0);
        Celda esperada = laberinto.getCelda(1, 2);
        Celda calculada = buscador.bfs(origen, destino);
        assertEquals(esperada, calculada);
    }

    @Test
    public void prueba05() {
        Celda origen = laberinto.getCelda(2, 3);
        Celda destino = laberinto.getCelda(0, 0);
        Celda esperada = laberinto.getCelda(2, 2);
        Celda calculada = buscador.bfs(origen, destino);
        assertEquals(esperada, calculada);
    }

    @Test
    public void prueba06() {
        Celda origen = laberinto.getCelda(3, 1);
        Celda destino = laberinto.getCelda(0, 0);
        Celda esperada = laberinto.getCelda(2, 1);
        Celda calculada = buscador.bfs(origen, destino);
        assertEquals(esperada, calculada);
    }

    @Test
    public void prueba07() {
        Celda origen = laberinto.getCelda(3, 1);
        Celda destino = laberinto.getCelda(0, 0);
        Celda esperada = laberinto.getCelda(2, 1);
        Celda calculada = buscador.bfs(origen, destino);
        assertEquals(esperada, calculada);
    }

    @Test
    public void prueba08() {
        Celda origen = laberinto.getCelda(4, 1);
        Celda destino = laberinto.getCelda(0, 0);
        Set<Celda> soluciones = new HashSet<Celda>();
        soluciones.add(laberinto.getCelda(3, 1));
        soluciones.add(laberinto.getCelda(4, 0));
        Celda calculada = buscador.bfs(origen, destino);
        assertTrue(soluciones.contains(calculada));
    }

    @Test
    public void prueba11() {
        Celda origen = laberinto.getCelda(4, 4);
        Celda destino = laberinto.getCelda(0, 0);
        Celda calculada = buscador.bfs(origen, destino);
        assertNull(calculada);
    }
}
