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
import pieduca.negocio.Categoria;

/**
 * FXML Controller class
 *
 * @author luis.dutra
 */
public class FXMLTesteController implements Initializable {

    @FXML
    private ComboBox<Categoria> cbCategorias;
    
    private List<Categoria> categorias = new ArrayList<>();
    
    private ObservableList<Categoria> obsCategorias;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarCategorias();
    }    
    
   public void carregarCategorias() {
       
       Categoria categoria = new Categoria(1, "teste");
       
       categorias.add(categoria);
       
       obsCategorias = FXCollections.observableArrayList(categorias);
       
       cbCategorias.setItems(obsCategorias);
   }
}
