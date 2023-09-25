
package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import negocio.Usuario;
import pieduca.Sys;

public class UsuarioDao extends DaoAdapter<Usuario, Integer> {

    private NotificationSQL notifications = new NotificationSQL();
    
    @Override
    public void create(Usuario objeto) {
       DataBaseConnectionManager dbcm;
       dbcm = Sys.getInstance().getDB();
        try
        {
            String sql = "INSERT INTO usuario VALUES ( ?, ?);";
            
            dbcm.runPreparedSQL(sql, objeto.getId(), objeto.getHashCode());
        } 
        catch (DataBaseException ex)
        {
            notifications.chaveDuplicada();
        } 
    }

    @Override
    public Usuario read(Integer primaryKey) {
        Usuario u = null;
        DataBaseConnectionManager dbcm;
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "SELECT * FROM usuario WHERE id = ?";
            
            ResultSet rs = dbcm.runPreparedQuerySQL(sql, primaryKey );
            
            if (rs.isBeforeFirst()) // acho alguma coisa?
            {
                rs.next();
                int id = rs.getInt("id");
                String senha = rs.getString("senha");
                
                u = new Usuario(id,senha);
            }
        } 
        catch (DataBaseException ex)
        {
            notifications.erroSintaxe();
        } 
        catch (SQLException ex)
        {
            notifications.dataTypeErrado();
        }
        
        return u;
    }

    @Override
    public ArrayList<Usuario> readAll() {
        ArrayList<Usuario> lista = new ArrayList();
        
        DataBaseConnectionManager dbcm;
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "SELECT * FROM usuario;";
            
            ResultSet rs = dbcm.runQuerySQL( sql );
            
            if (rs.isBeforeFirst())
            {
                rs.next();
                while (!rs.isAfterLast())
                {
                    int id = rs.getInt("id");
                    String senha = rs.getString("senha");
                
                    Usuario u = new Usuario(id,senha);
                    lista.add(u);
                    
                    rs.next();
                }
            }

        } 
        catch (DataBaseException ex)
        {
            notifications.erroSintaxe();
        } 
        catch (SQLException ex)
        {
            notifications.dataTypeErrado();
        }
        
        return lista;
    }

    @Override
    public void update(Usuario objeto) throws NotFoundException {
        DataBaseConnectionManager dbcm;
        
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "UPDATE usuario SET senha = ? WHERE id = ?";
            dbcm.runPreparedSQL(sql, objeto.getHashCode(), objeto.getId());
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
            throw new NotFoundException();
        }
    }

    @Override
    public void delete(Integer primaryKey) throws NotFoundException {
        DataBaseConnectionManager dbcm;
        
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "DELETE FROM usuario WHERE id = ?";
            dbcm.runPreparedSQL(sql, primaryKey );
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
            throw new NotFoundException();
        }
    }
    
}
