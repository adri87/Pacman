package es.upm.dit.adsw.pract1;


public class Bicho extends BichoBase implements Runnable {

	private final int dt;
	private volatile boolean vivo;

	/**
	 * Constructor
	 * 
	 * @param laberinto
	 *            el laberinto en el que se mueve.
	 * @param celda
	 *            posición inicial.
	 * @param dt
	 *            delta de tiempo para irse moviendo.
	 */

		

	public Bicho(Celda celda, Laberinto laberinto, int dt) {
		super(celda, laberinto);
		this.dt = dt;
		vivo = true;
	}

	/**
	 * Fábrica de bichos La interfaz gráfica llama a este método para crear
	 * bichos nuevos
	 * 
	 * @param laberinto
	 *            : el laberinto donde se mueve
	 * @param celda
	 *            : posición inicial
	 */

	public static void nuevoBicho(Laberinto laberinto, Celda celda) {
		Bicho bicho = new Bicho (celda,laberinto, DT);
		Monitor monitor = laberinto.getMonitor();
		if (monitor.puedoPonerme(bicho, celda)) {
			Thread thread = new Thread(bicho);
			thread.start();
		}

	}

	/**
	 * Actividad del bicho
	 */

	@Override
	public void run() {
		try {
			while (vivo) {
				Direccion direccion = Direccion.random();
				Celda celdaSiguiente = laberinto.getConexion(celda, direccion);
				if (celdaSiguiente != null) {
					Monitor monitor = laberinto.getMonitor();
					try {
						monitor.mueveBicho(this, celdaSiguiente);
					} catch (JugadorComido j) {
						laberinto.jugadorPierde();
					}
				}
				Thread.sleep(dt);

			}
		} catch (InterruptedException e) {
			return;
		}

	}

	/**
	 * Hacer que el bicho muera, termina la ejecución de run()
	 */

	public void kill() {
		this.vivo = false;
	}

}
