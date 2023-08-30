/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package pieduca;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import negocio.CategoriaExemplo;

/**
 * FXML Controller class
 *
 * @author luis.dutra
 */
public class FXMLTesteController implements Initializable {

    @FXML
    private ComboBox<CategoriaExemplo> cbCategorias;
    
    private List<CategoriaExemplo> categorias = new ArrayList<>();
    
    private ObservableList<CategoriaExemplo> obsCategorias;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarCategorias();
    }    
    
   public void carregarCategorias() {
       
       CategoriaExemplo categoria = new CategoriaExemplo(1, "teste");
       
       categorias.add(categoria);
       
       obsCategorias = FXCollections.observableArrayList(categorias);
       
       cbCategorias.setItems(obsCategorias);
   }
}
