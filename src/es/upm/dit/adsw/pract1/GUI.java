package es.upm.dit.adsw.pract1;

import javax.swing.*;

import es.upm.dit.adsw.pract1.Bicho;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

/**
 * Interfaz grafica.
 * <p/>
 * 22.4.2013 ajustes el escalado de imágenes.
 * 22.4.2013 centrado de la imagen en la celda.
 *
 * @author Jose A. Manas
 * @version 20.3.2013
 */
public class GUI
        extends JPanel implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Nombre del juego.
     */
    public static final String TITULO = "Pacman (19.3.2013)";

    /**
     * Espacio entre la zona de juego y el borde de la ventana.
     */
    private static final int MARGEN = 10;
    /**
     * Ancho de la zona de juego.
     */
    private static final int ANCHO = 500;
    /**
     * Tamano de una celda: pixels.
     */
    private int lado1;

    /**
     * Para que el usuario indique el tamano del laberinto.
     */
    private final JTextField campoLado;

    /**
     * El laberinto.
     */
    private Laberinto laberinto;
    private Celda celdaSeleccionada;
    private JButton botonCepo;
    private JButton botonNormal;
    private JButton botonOrientado;
    private JButton botonDepredador;

    private void nuevoLaberinto(int lado) {
        lado1 = (ANCHO - 2 * MARGEN) / lado;
        laberinto = new Laberinto(lado, this);
        pintame();
    }

    private GUI(Container container) {
        nuevoLaberinto(15);

        setPreferredSize(new Dimension(ANCHO, ANCHO));
        container.add(this, BorderLayout.CENTER);
        setFocusable(true);
        requestFocusInWindow();

        campoLado = new JTextField(5);
        campoLado.setAlignmentX(JTextField.RIGHT_ALIGNMENT);
        campoLado.setText(String.valueOf(laberinto.getLado()));
        campoLado.setMaximumSize(campoLado.getPreferredSize());

        JToolBar toolBarS = new JToolBar();
        toolBarS.setFloatable(false);
        toolBarS.add(campoLado);
        toolBarS.add(new CreaAction());
        toolBarS.add(Box.createHorizontalStrut(10));

        botonCepo = new JButton("cepo");
        botonCepo.addActionListener(this);
        toolBarS.add(botonCepo);

        botonNormal = new JButton("normal");
        botonNormal.addActionListener(this);
        toolBarS.add(botonNormal);

        botonOrientado = new JButton("orientado");
        botonOrientado.addActionListener(this);
        toolBarS.add(botonOrientado);

        botonDepredador = new JButton("depredador");
        botonDepredador.addActionListener(this);
        toolBarS.add(botonDepredador);

        toolBarS.add(Box.createHorizontalGlue());
        container.add(toolBarS, BorderLayout.SOUTH);

        addKeyListener(new MyKeyListener());
        addMouseListener(new MyMouseListener());

        repaint();
    }

    /**
     * Constructor.
     *
     * @param applet Applet.
     */
    public GUI(JApplet applet) {
        this(applet.getContentPane());
    }

    /**
     * Constructor.
     *
     * @param frame Pantalla en consola.
     */
    public GUI(JFrame frame) {
        this(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Le dice al thread de swing que deberia refrescar la pantalla.
     * Swing lo hara cuando le parezca bien.
     */
    public void pintame() {
        repaint();
    }

    /**
     * Llamada por java para pintarse en la pantalla.
     *
     * @param g sistema grafico 2D para dibujarse.
     */
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.LIGHT_GRAY);
        int nwx = MARGEN;
        int nwy = MARGEN;
        int lado = laberinto.getLado();

        // pinta las celdas
        for (int x = 0; x < lado; x++) {
            for (int y = 0; y < lado; y++)
                pintaCelda(g, x, y);
        }

        // pinta el marco
        g.setColor(Color.BLACK);
        g.drawLine(nwx - 1, nwy - 1, nwx - 1, nwy + lado * lado1 + 1);
        g.drawLine(nwx + lado * lado1 + 1, nwy - 1, nwx + lado * lado1 + 1, nwy + lado * lado1 + 1);
        g.drawLine(nwx - 1, nwy - 1, nwx + lado * lado1 + 1, nwy - 1);
        g.drawLine(nwx - 1, nwy + lado * lado1 + 1, nwx + lado * lado1 + 1, nwy + lado * lado1 + 1);


        // pinta al jugador
        Jugador jugador = laberinto.getJugador();
        pintaImagen((Graphics2D) g, jugador.getImagen(), jugador.getCelda());

        // pinta los bichos
        for (BichoBase bicho : laberinto.getBichos()) {
            Image imagen = bicho.getImagen();
            if (imagen != null) {
                Celda celda = bicho.getCelda();
                pintaImagen((Graphics2D) g, imagen, celda);
            }
        }
    }

    private void pintaImagen(Graphics2D g2d, Image imagen, Celda celda) {
        if (imagen == null)
            return;
        int x = celda.getPunto().getX();
        int y = celda.getPunto().getY();
        int iWidth = imagen.getWidth(null);
        int iHeight = imagen.getHeight(null);
        double escalaX = 0.9 * lado1 / iWidth;
        double escalaY = 0.9 * lado1 / iHeight;
        double escala = Math.min(escalaX, escalaY);
        double nwX = sw_x(x) + (lado1 - escala * iWidth) / 2;
        double nwY = sw_y(y + 1) + (lado1 - escala * iHeight) / 2;
        AffineTransform transform = new AffineTransform(escala, 0, 0, escala, nwX, nwY);
        g2d.drawImage(imagen, transform, null);
    }

    /**
     * Pinta el contenido de una celda.
     *
     * @param g     sistema grafico 2D para dibujarse.
     * @param celda celda a pintar.
     */
    @SuppressWarnings("unused")
	private void pintaTipo(Graphics g, Celda celda) {
        Tipo tipo = celda.getTipo();
        if (tipo == null || tipo == Tipo.NORMAL)
            return;
        PuntoXY punto = celda.getPunto();
        int x = punto.getX();
        int y = punto.getY();
        int nwx = sw_x(x) + 3;
        int nwy = sw_y(y + 1) + 3;
        int dx = this.lado1 - 6;
        int dy = this.lado1 - 6;
        g.setColor(tipo.getColor());
        g.fillOval(nwx, nwy, dx, dy);
    }

    /**
     * Pinta una celda.
     *
     * @param g sistema grafico 2D para dibujarse.
     * @param x columna.
     * @param y fila.
     */
    private void pintaCelda(Graphics g, int x, int y) {
        Celda celda = laberinto.getCelda(x, y);

        if (celda.equals(celdaSeleccionada))
            rellena(g, x, y, Color.LIGHT_GRAY);
        if (celda.getTipo() != Tipo.NORMAL)
            pintaCirculo(g, x, y, celda.getTipo().getColor());

        // pinta las paredes de la celda
        g.setColor(Color.RED);
        if (celda.hayPared(Direccion.NORTE))
            g.drawLine(sw_x(x), sw_y(y + 1), sw_x(x + 1), sw_y(y + 1));
        if (celda.hayPared(Direccion.SUR))
            g.drawLine(sw_x(x), sw_y(y), sw_x(x + 1), sw_y(y));
        if (celda.hayPared(Direccion.ESTE))
            g.drawLine(sw_x(x + 1), sw_y(y), sw_x(x + 1), sw_y(y + 1));
        if (celda.hayPared(Direccion.OESTE))
            g.drawLine(sw_x(x), sw_y(y), sw_x(x), sw_y(y + 1));
    }

    /**
     * Pinta el contenido de una celda.
     *
     * @param g     sistema grafico 2D para dibujarse.
     * @param x     columna.
     * @param y     fila.
     * @param color para rellenar.
     */
    private void pintaCirculo(Graphics g, int x, int y, Color color) {
        int nwx = sw_x(x) + 3;
        int nwy = sw_y(y + 1) + 3;
        int dx = this.lado1 - 6;
        int dy = this.lado1 - 6;
        g.setColor(color);
        g.fillOval(nwx, nwy, dx, dy);
    }

    /**
     * Pinta el contenido de una celda.
     *
     * @param g     sistema grafico 2D para dibujarse.
     * @param x     columna.
     * @param y     fila.
     * @param color para rellenar.
     */
    private void rellena(Graphics g, int x, int y, Color color) {
        int nwx = sw_x(x) + 1;
        int nwy = sw_y(y + 1) + 1;
        int dx = this.lado1 - 2;
        int dy = this.lado1 - 2;
        g.setColor(color);
        g.fillRect(nwx, nwy, dx, dy);
    }

    /**
     * Dada una columna, calcula el vertice inferior izquierdo.
     *
     * @param columna columna.
     * @return abscisa del vertice inferior izquierdo.
     */
    private int sw_x(int columna) {
        return MARGEN + columna * lado1;
    }

    /**
     * Dada una fila, calcula el vertice inferior izquierdo.
     *
     * @param fila fila.
     * @return vertice inferior izquierdo.
     */
    private int sw_y(int fila) {
        int lado = laberinto.getLado();
        return MARGEN + (lado - fila) * lado1;
    }

    /**
     * Pone fin a un juego: imprime un mensaje y genera un juego nuevo.
     *
     * @param msg mensaje explicativo.
     */
    public void fin(String msg) {
        JOptionPane.showMessageDialog(this,
                msg, "Laberinto",
                JOptionPane.INFORMATION_MESSAGE);
        // apuntamos el tamano del laberinto actual
        int lado = laberinto.getLado();
        // nos hacemos otro laberinto de igual tamano
        nuevoLaberinto(lado);
    }

    public void actionPerformed(ActionEvent event) {
        if (celdaSeleccionada == null) {
            requestFocus();
            return;
        }

        Component source = (Component) event.getSource();
        if (source == botonCepo)
            celdaSeleccionada.setTipo(Tipo.CEPO);

        else if (source == botonNormal)
            Bicho.nuevoBicho(laberinto, celdaSeleccionada);

        else if (source == botonOrientado)
            BichoOrientado.nuevoBicho(laberinto, celdaSeleccionada);

        else if (source == botonDepredador)
            BichoDepredador.nuevoBicho(laberinto, celdaSeleccionada);

        celdaSeleccionada = null;
        pintame();
        requestFocus();
    }

    /**
     * Interprete del boton NUEVO.
     */
    @SuppressWarnings("serial")
	private class CreaAction
            extends AbstractAction {
        /**
         * Constructor.
         */
        public CreaAction() {
            super("Nuevo");
        }

        /**
         * Se hace cargo de la pulsacion.
         *
         * @param event evento que dispara la accion.
         */
        public void actionPerformed(ActionEvent event) {
            for (BichoBase bicho : laberinto.getBichos())
                bicho.kill();
            int lado = Integer.parseInt(campoLado.getText());
            nuevoLaberinto(lado);
            celdaSeleccionada = null;
            requestFocus();
        }
    }

    /**
     * Captura el teclado.
     */
    private class MyKeyListener
            extends KeyAdapter {
        /**
         * Gestiona el teclado.
         *
         * @param event tecla pulsada.
         */
        @Override
        public void keyPressed(KeyEvent event) {
            Direccion direccion = getDireccion(event);
            if (direccion != null)
                laberinto.getJugador().intentaMover(direccion);
        }

        private Direccion getDireccion(KeyEvent ke) {
            if (ke.getKeyCode() == KeyEvent.VK_UP)
                return Direccion.NORTE;
            if (ke.getKeyCode() == KeyEvent.VK_DOWN)
                return Direccion.SUR;
            if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
                return Direccion.ESTE;
            if (ke.getKeyCode() == KeyEvent.VK_LEFT)
                return Direccion.OESTE;
            return null;
        }
    }

    private class MyMouseListener
            extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            try {
                int pixelX = event.getX();
                int pixelY = event.getY();
                int x = (pixelX - MARGEN) / lado1;
                int y = laberinto.getLado() - 1 - (pixelY - MARGEN) / lado1;
                Celda celda = laberinto.getCelda(x, y);
                if (celda == celdaSeleccionada)
                    celdaSeleccionada = null;
                else
                    celdaSeleccionada = celda;
                pintame();
            } catch (Exception ignored) {
            }
        }
    }
}
