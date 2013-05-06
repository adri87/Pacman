package es.upm.dit.adsw.pract1;

public class BichoDepredador extends BichoBase implements Runnable {
	
	private final int dt;
	private Buscador buscador;
	private volatile boolean vivo;
	
	/**
	 * @param laberinto
	 * @param celda
	 * @param dt
	 */
	private BichoDepredador(Laberinto laberinto, Celda celda, int dt) {
		super(celda, laberinto, "Anibal.png");
		this.dt = dt;
		vivo = true;
		buscador = new Buscador(laberinto);
	}
	
	/**
	 * @param laberinto
	 * @param celda
	 */
	public static void nuevoBicho(Laberinto laberinto, Celda celda) {
		BichoDepredador bicho = new BichoDepredador(laberinto, celda, DT);
		Monitor monitor = laberinto.getMonitor();
		if (monitor.puedoPonerme(bicho, celda)) {
			Thread thread = new Thread(bicho);
			thread.start();
		}
	}

	@Override
	public void run() {
		try {
			while (vivo) {
			Celda origen = getCelda();
			Celda destino = laberinto.getJugador().getCelda();
			Celda celdaSiguiente = buscador.bfs(origen, destino);
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
