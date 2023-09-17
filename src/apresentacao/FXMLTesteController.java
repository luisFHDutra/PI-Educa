
package apresentacao;

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
