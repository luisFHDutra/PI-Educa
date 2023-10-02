
package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import negocio.Permissao;
import negocio.Usuario;
import pieduca.Sys;

public class UsuarioDao extends DaoAdapter<Usuario, Integer> {

    private NotificationSQL notifications = new NotificationSQL();

    @Override
    public Usuario read(Integer primaryKey) {
        Usuario u = null;
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try
        {
            String sql = "SELECT * FROM usuario WHERE id = ?";
            
            ResultSet rs = dbcm.runPreparedQuerySQL(sql, primaryKey );
            
            if (rs.isBeforeFirst()) // acho alguma coisa?
            {
                rs.next();
                int id = rs.getInt("id");
                String senha = rs.getString("senha");
                int idPermissao = rs.getInt("permissao_id");
                
                Permissao permissao = null;
                try {
                    permissao = DaoFactory.criarPermissaoDao().read(idPermissao);
                } catch (NotFoundException ex) {
                   notifications.tabelaNaoExiste();
                }
                
                u = new Usuario(id, senha, permissao);
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
        
        return u;
    }

    @Override
    public ArrayList<Usuario> readAll() {
        ArrayList<Usuario> lista = new ArrayList();
        
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try
        {
            String sql = "SELECT * FROM usuario;";
            
            ResultSet rs = dbcm.runQuerySQL( sql );
            
            if (rs.isBeforeFirst())
            {
                rs.next();
                while (!rs.isAfterLast())
                {
                    int id = rs.getInt("id");
                    String senha = rs.getString("senha");
                    int idPermissao = rs.getInt("permissao_id");
                    
                    Permissao permissao = null;
                    try {
                        permissao = DaoFactory.criarPermissaoDao().read(idPermissao);
                    } catch (NotFoundException ex) {
                       notifications.tabelaNaoExiste();
                    }
                
                    Usuario u = new Usuario(id, senha, permissao);
                    lista.add(u);
                    
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
    public void update(Usuario objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try
        {
            String sql = "UPDATE usuario SET senha = ? WHERE id = ?";
            dbcm.runPreparedSQL(sql, objeto.getHashCode(), objeto.getId());
            
            dbcm.closeConnection();
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
        }
    }

    @Override
    public void create(Usuario objeto) throws KeyViolationException, InvalidKeyException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(Integer primaryKey) throws NotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
