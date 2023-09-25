
package pieduca;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import negocio.Usuario;
import persistencia.NotificationSQL;

public class Sys {
    
    private String nomeSys;
    private Usuario user;
    private DataBaseConnectionManager dbcm;
    private NotificationSQL notifications = new NotificationSQL();
    
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
            this.dbcm = new DataBaseConnectionManager( DataBaseConnectionManager.POSTGRESQL,
                    "educa","postgres","postgres");
        } 
        catch (DataBaseException ex)
        {
            notifications.conexaoBD();
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

    public void setUser(Usuario user) {
        this.user = user;
    }
    
    public DataBaseConnectionManager getDB()
    {
        return dbcm;
    }
}
