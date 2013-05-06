package es.upm.dit.adsw.pract1;

/**
 * @author Sergy
 *
 */
public class BichoOrientado extends BichoBase implements Runnable{
	
	private final int dt;
	private volatile boolean vivo;
	
	/**
	 * @param laberinto
	 * @param celda
	 * @param dt
	 */
	private BichoOrientado(Laberinto laberinto, Celda celda, int dt) {
		super(celda, laberinto, "Listo.png");
		this.dt = dt;
		vivo = true;
	}
	
	/**
	 * @param laberinto
	 * @param celda
	 */
	public static void nuevoBicho(Laberinto laberinto, Celda celda) {
		BichoOrientado bicho = new BichoOrientado (laberinto, celda, DT);
		Monitor monitor = laberinto.getMonitor();
		if (monitor.puedoPonerme(bicho, celda)) {
			Thread thread = new Thread(bicho);
			thread.start();
		}
	}


	public void run() {
		try {
			while (vivo) {
			Direccion direccion;
			Jugador jugador = laberinto.getJugador();
			PuntoXY puntoJugador = jugador.getCelda().getPunto();
			PuntoXY puntoBicho = getCelda().getPunto();
			int dx = puntoJugador.getX() - puntoBicho.getX();
			int dy = puntoJugador.getY() - puntoBicho.getY();
			if (dx>dy && dx>-dy) direccion = Direccion.ESTE;
			else if (dy>dx && dy>-dx) direccion = Direccion.NORTE;
			else if (-dx>dy && -dx>-dy) direccion = Direccion.OESTE;
			else if (-dy>dx && -dy>-dx)	direccion = Direccion.SUR;
			else if (dx==dy) direccion = Direccion.NORTE;
			else direccion = Direccion.SUR;
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
