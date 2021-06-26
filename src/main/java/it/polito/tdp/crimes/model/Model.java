package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	EventsDao dao;
	List<String> migliore;
	SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	public Model() {
		dao=new EventsDao();
	}
	
	public void creaGrafo (String categoria, int mese) {
		grafo= new SimpleWeightedGraph <String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.listAllVertici(categoria, mese));
		for(Adiacenza a: dao.listAllAdiacenza(categoria, mese)) {
			if(grafo.containsVertex(a.getTipo1())&& grafo.containsVertex(a.getTipo2())) {
				Graphs.addEdge(this.grafo, a.getTipo1(), a.getTipo2(), a.getPeso());
			}
		}
	}
	
	
	public List <Adiacenza> getPuntoD(){
		Double peso=0.0;
		List <Adiacenza> result= new ArrayList<>();
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			peso=peso+grafo.getEdgeWeight(e);
		}
		double pesoMedio=peso/this.grafo.edgeSet().size();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)>pesoMedio) {
				Adiacenza a =new Adiacenza(grafo.getEdgeSource(e), grafo.getEdgeTarget(e), grafo.getEdgeWeight(e));
				result.add(a);
			}
		}
		return result;
	}
	
	public List <String> trovaPercorso(Adiacenza a){
		this.migliore= new ArrayList<>();
		List <String> parziale= new ArrayList <>();
		parziale.add(a.getTipo1());
		cerca(parziale, a.getTipo2());
		return migliore;
	}
	
	private void cerca(List<String> parziale, String tipo2) {
		//caso terminale-->quando l'ultimo elemento è il tipo2
		if(parziale.get(parziale.size()-1).equals(tipo2)) {
			if(parziale.size()>migliore.size()) {
				migliore=new ArrayList <>(parziale);
			}
			return;
		}
		String ultimo= parziale.get(parziale.size()-1);
		for(String s: Graphs.neighborListOf(grafo, ultimo)) {
			if(!parziale.contains(s)) {
				parziale.add(s);
				cerca(parziale, tipo2);
				parziale.remove(s);
			}
		}
		
	}

	public int getNVertici() {
		return grafo.vertexSet().size();
	}
	public int getNArchi() {
		return grafo.edgeSet().size();
	}
	
	
	public EventsDao getDao() {
		return dao;
	}

	public SimpleWeightedGraph<String, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<String> listAllCategorie(){
		return dao.listAllCategorie();
	}
	
	public List<Integer> listAllMesi(){
		return dao.listAllMesi();
	}
}
