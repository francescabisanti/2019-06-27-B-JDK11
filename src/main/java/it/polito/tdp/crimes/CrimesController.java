/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo...\n");
    	String categoria= this.boxCategoria.getValue();
    	Integer mese=this.boxMese.getValue();
    	if(mese==null || categoria==null) {
    		this.txtResult.setText("Seleziona mese e categoria prima di creare il grafo");
    		return;
    	}
    	
    	this.model.creaGrafo(categoria, mese);
    	this.txtResult.appendText("#VERTICI: "+this.model.getNVertici()+"\n");
    	this.txtResult.appendText("#ARCHI: "+this.model.getNArchi()+"\n");
    	List <Adiacenza> result= model.getPuntoD();
    	for(Adiacenza a: result) {
    		this.txtResult.appendText(a.toString());
    	}
    	for(Adiacenza a : model.getPuntoD()) {
    		this.boxArco.getItems().add(a);
    	}
    	
    	
    	
    }
    
    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso...\n");
    	Adiacenza a= boxArco.getValue();
    	if(a==null) {
    		this.txtResult.setText("Seleziona un arco");
    		return;
    	}
    	List <String> percorso=model.trovaPercorso(a);
    	this.txtResult.appendText("Il percorso massimo da :"+a.getTipo1()+" a "+a.getTipo2()+" è: \n");
    	for(String s: percorso) {
    		this.txtResult.appendText(s+"\n");
    	}
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxCategoria.getItems().addAll(model.listAllCategorie());
    	this.boxMese.getItems().addAll(model.listAllMesi());
    }
}
