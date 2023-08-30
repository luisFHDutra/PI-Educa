
package pieduca;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import javax.swing.JOptionPane;
import negocio.Usuario;

public class Sys {
    
    private String nomeSys;
    private Usuario user;
    private DataBaseConnectionManager dbcm;
    
    private static Sys sys = new Sys();

    public static Sys getInstance()
    {
        return sys;
    }
    
    private Sys() 
    {
        
        this.nomeSys = "Educa";
        this.user = null;
        
        try
        {
            this.dbcm = new DataBaseConnectionManager( DataBaseConnectionManager.MYSQL,
                    "educa","mysql","mysql");
        } 
        catch (DataBaseException ex)
        {
            JOptionPane.showMessageDialog(null, 
                    "Erro fatal na configuração de acesso ao banco de dados",
                    "Conexão com o banco de dados", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    
    public String getNomeSistema()
    {
        return nomeSys;
    }
    
    public Usuario getUser()
    {
        return user;
    }
    
    public DataBaseConnectionManager getDB()
    {
        return dbcm;
    }
}
