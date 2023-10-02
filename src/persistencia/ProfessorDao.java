
package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import negocio.Professor;
import negocio.Usuario;
import pieduca.Sys;

public class ProfessorDao extends DaoAdapter<Professor, Integer> {

    private NotificationSQL notifications = new NotificationSQL();
    
    @Override
    public void create(Professor objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try
        {
            dbcm.runSQL("begin transaction;");
            
            String sql = "INSERT INTO professor VALUES ( ?, ?, ?, ?);";
            
            dbcm.runPreparedSQL(sql, objeto.getIdProfessor(), objeto.getNome(), objeto.getAreaEspecializacao(),objeto.getContato());
            
            String sqlUser = "INSERT INTO usuario (id, senha, permissao_id) VALUES ( ?, ?, ?);";
            
            PreparedStatement statement = dbcm.prepareStatement(sqlUser);
            statement.setInt(1, objeto.getUsuario().getId());
            statement.setString(2, objeto.getUsuario().getHashCode());
            statement.setInt(3, objeto.getUsuario().getPermissao().getIdPermissao());
            statement.executeUpdate();
            
            dbcm.runSQL("commit;");
            
            dbcm.closeConnection();
        } catch (DataBaseException ex) {
            try {
                dbcm.runSQL("rollback;");
                
                dbcm.closeConnection();
            } catch (DataBaseException ex1) {
                notifications.rollback();
            }

            notifications.chaveDuplicada();
        } catch (SQLException ex) {
            
            notifications.erroSintaxe();
        }
    }
    
    @Override
    public Professor read(Integer primaryKey) throws NotFoundException {
        Professor p = null;
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try
        {
            String sql = "SELECT * FROM professor WHERE id = ?";
            
            ResultSet rs = dbcm.runPreparedQuerySQL(sql, primaryKey );
            
            if (rs.isBeforeFirst()) 
            {
                rs.next();
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String areaEspecializacao = rs.getString("area_especializacao");
                String contato = rs.getString("contato");
                
                Usuario user = null;
                try {
                    user = DaoFactory.criarUsuarioDao().read(id);
                } catch (NotFoundException ex) {
                   notifications.tabelaNaoExiste();
                }
                
                p = new Professor(id, nome, areaEspecializacao, contato, user);
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
    public ArrayList<Professor> readAll() {
        ArrayList<Professor> lista = new ArrayList();
        
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try
        {
            String sql = "SELECT * FROM professor;";
            
            ResultSet rs = dbcm.runQuerySQL( sql );
            
            if (rs.isBeforeFirst())
            {
                rs.next();
                while (!rs.isAfterLast())
                {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String areaEspecializacao = rs.getString("area_especializacao");
                    String contato = rs.getString("contato");

                    Usuario user = null;
                    try {
                        user = DaoFactory.criarUsuarioDao().read(id);
                    } catch (NotFoundException ex) {
                        notifications.tabelaNaoExiste();
                    }

                    Professor p = new Professor(id, nome, areaEspecializacao, contato, user);
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
    public void update(Professor objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try
        {
            String sql = "UPDATE professor SET nome = ?, area_especializacao = ?, contato = ? WHERE id = ?";
            dbcm.runPreparedSQL(sql, objeto.getNome(), objeto.getAreaEspecializacao(), objeto.getContato(),
                    objeto.getIdProfessor());
            
            dbcm.closeConnection();
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
        }
    }

    @Override
    public void delete(Integer primaryKey) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try
        {
            String sqlUser = "DELETE FROM usuario WHERE id = ?";
            dbcm.runPreparedSQL(sqlUser, primaryKey );
            
            String sqlProf = "DELETE FROM professor WHERE id = ?";
            dbcm.runPreparedSQL(sqlProf, primaryKey );
            
            dbcm.closeConnection();
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
        }
    }
    
    public Integer maxId() {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try {
            String sql = "SELECT MAX(id) FROM professor";
            ResultSet rs = dbcm.runQuerySQL(sql);

            int ultimoID = 1;

            if (rs.next()) {
             // Obtém o último ID da consulta
             ultimoID = rs.getInt(1);
            }

            dbcm.closeConnection();
            
            // Calcula o próximo ID
            return ultimoID + 1;
            
        } catch (SQLException e) {
            notifications.tabelaNaoExiste();
        } catch (DataBaseException ex) {
            notifications.erroSintaxe();
        }
        return null;
         
    }
    
}
