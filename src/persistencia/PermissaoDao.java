package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import negocio.Permissao;
import pieduca.Sys;

public class PermissaoDao extends DaoAdapter<Permissao, Integer> {

    private NotificationSQL notifications = new NotificationSQL();
    
    @Override
    public void create(Permissao objeto) throws KeyViolationException, InvalidKeyException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Permissao read(Integer primaryKey) throws NotFoundException {
        Permissao p = null;
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try
        {
            String sql = "SELECT * FROM permissao WHERE id = ?";
            
            ResultSet rs = dbcm.runPreparedQuerySQL(sql, primaryKey );
            
            if (rs.isBeforeFirst()) // acho alguma coisa?
            {
                rs.next();
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                
                p = new Permissao(id, nome);
            }
            
            dbcm.closeConnection();
        } 
        catch (DataBaseException ex)
        {
            notifications.erroSintaxe();
        } 
        catch (SQLException ex)
        {
            notifications.dataTypeErrado();
        }
        
        return p;
    }

    @Override
    public ArrayList<Permissao> readAll() {
        ArrayList<Permissao> lista = new ArrayList();
        
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try
        {
            String sql = "SELECT * FROM permissao;";
            
            ResultSet rs = dbcm.runQuerySQL( sql );
            
            if (rs.isBeforeFirst())
            {
                rs.next();
                while (!rs.isAfterLast())
                {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                
                    Permissao p = new Permissao(id, nome);
                    lista.add(p);
                    
                    rs.next();
                }
            }

            dbcm.closeConnection();
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
    public void update(Permissao objeto) throws NotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(Integer primaryKey) throws NotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
