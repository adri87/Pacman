package es.upm.dit.adsw.pract1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Buscador {

	private final Laberinto laberinto;
	List<Celda> pendientes;
	Map<Celda, Celda> visitadas;

	public Buscador(Laberinto laberinto) { 
		this.laberinto = laberinto; 
	}
	
	public Celda bfs(Celda origen, Celda destino) {
		pendientes = new ArrayList<Celda>();
		visitadas = new HashMap<Celda, Celda>();
		pendientes.add(origen);
		if (bfs(destino, pendientes, visitadas)) {
			Celda celda = destino;
			while (!visitadas.get(celda).equals(origen)) {
				celda = visitadas.get(celda);
			}
			return celda;			
		}
		return origen;
	} 
	
	private boolean bfs(Celda destino, List<Celda> pendientes, Map<Celda, Celda> visitadas) {
		if (pendientes.isEmpty())
			return false;
		for (int i = 0; i<pendientes.size(); i++) {
			Celda c1 = pendientes.get(i);
			if (c1 == destino)
				return true;
			for (Direccion direccion : Direccion.values()){
				Celda celda = laberinto.getConexion(c1, direccion);
				if (celda != null) {
					if (!visitadas.containsKey(celda)){
						visitadas.put(celda, c1);
						pendientes.add(celda);
					}
				}
			}	
		}
		return false;
	}

}