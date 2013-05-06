package es.upm.dit.adsw.pract1;

public class Monitor {
	private final Laberinto laberinto;
	
	public Monitor(Laberinto laberinto) {
		this.laberinto = laberinto;
	}

	/**
	* Intenta poner un bicho en la celda indicada.
	* Si la celda esta vacia, lo pone; si no, no hace nada.
	*
	* @param bicho referencia al bicho que queremos poner.
	* @param celda referencia a la celda en la que queremos ponernos.
	* @return true si se puede poner en ese sitio.
	*/
	public synchronized boolean puedoPonerme(BichoBase bicho, Celda celda) {
		if (celda.getEstado() == Estado.VACIA) {
			bicho.setCelda(celda);
			laberinto.add(bicho);
			return true;
		} else {
			return false;
		}
	}

	public synchronized void mueveJugador(Jugador jugador, Celda celda2) throws JugadorComido {
		if (celda2.getEstado() == Estado.BICHO)
			throw new JugadorComido();
		else 
			jugador.setCelda(celda2);
		if (celda2.getTipo() == Tipo.LLAVE) {
			laberinto.limpiaCepos();
			notifyAll();
		}
	}

	public synchronized void mueveBicho(BichoBase bicho, Celda celda2) throws JugadorComido {
		if (celda2.getEstado() == Estado.JUGADOR)
			throw new JugadorComido();
		else if (celda2.getEstado() == Estado.VACIA)
			bicho.setCelda(celda2);
		if (celda2.getTipo() == Tipo.CEPO) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
