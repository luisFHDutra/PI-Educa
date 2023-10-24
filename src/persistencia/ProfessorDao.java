
package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import negocio.Disciplina;
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
            
            String sql = "INSERT INTO professor VALUES ( ?, ?, ?, ?, ?, ?);";
            
            dbcm.runPreparedSQL(sql, objeto.getIdProfessor(), objeto.getNome(), objeto.getAreaEspecializacao()
                    ,objeto.getContato(), objeto.getDeletado().toString(), objeto.getDisciplina().getIdDisciplina());
            
            createUsuario(objeto);
            
            dbcm.closeConnection();
        } catch (DataBaseException ex) {
            notifications.chaveDuplicada();
        }
    }
    
    private void createUsuario (Professor objeto) {
        
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try
        {
            String sqlUser = "INSERT INTO usuario (id, senha, permissao_id, deletado) VALUES ( ?, ?, ?, ?);";
            
            PreparedStatement statement = dbcm.prepareStatement(sqlUser);
            statement.setInt(1, objeto.getUsuario().getId());
            statement.setString(2, objeto.getUsuario().getHashCode());
            statement.setInt(3, objeto.getUsuario().getPermissao().getIdPermissao());
            statement.setBoolean(4, objeto.getUsuario().getDeletado());
            statement.executeUpdate();
            
            dbcm.closeConnection();
        } catch (SQLException ex) {
            notifications.erroSintaxe();
        } catch (DataBaseException ex) {
            notifications.conexaoBD();
        }
    }
    
    @Override
    public Professor read(Integer primaryKey) {
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
                Boolean deletado = rs.getBoolean("deletado");
                int idDisciplina = rs.getInt("disciplina_id");
                
                Usuario user = null;
                try {
                    user = DaoFactory.criarUsuarioDao().read(id);
                } catch (NotFoundException ex) {
                   notifications.tabelaNaoExiste();
                }
                
                Disciplina disciplina = null;
                try {
                    disciplina = DaoFactory.criarDisciplinaDao().read(idDisciplina);
                } catch (NotFoundException ex) {
                    notifications.tabelaNaoExiste();
                }
                
                p = new Professor(id, nome, areaEspecializacao, contato, user, deletado, disciplina);
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
                    Boolean deletado = rs.getBoolean("deletado");
                    int idDisciplina = rs.getInt("disciplina_id");
                    
                    Usuario user = null;
                    try {
                        user = DaoFactory.criarUsuarioDao().read(id);
                    } catch (NotFoundException ex) {
                        notifications.tabelaNaoExiste();
                    }

                    Disciplina disciplina = null;
                    try {
                        disciplina = DaoFactory.criarDisciplinaDao().read(idDisciplina);
                    } catch (NotFoundException ex) {
                        notifications.tabelaNaoExiste();
                    }
                
                    Professor p = new Professor(id, nome, areaEspecializacao, contato, user, deletado, disciplina);
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
            String sql = "UPDATE professor SET nome = ?, area_especializacao = ?, contato = ?, deletado = ?, disciplina_id = ? WHERE id = ?";
            dbcm.runPreparedSQL(sql, objeto.getNome(), objeto.getAreaEspecializacao(), objeto.getContato(),
                    objeto.getDeletado().toString(), objeto.getDisciplina().getIdDisciplina(), objeto.getIdProfessor());
            
            String sqlUser = "UPDATE usuario SET senha = ? WHERE id = ?";
            PreparedStatement statement = dbcm.prepareStatement(sqlUser);
            
            statement.setString(1, objeto.getUsuario().getHashCode());
            statement.setInt(2, objeto.getUsuario().getId());
            statement.executeUpdate();
            
            dbcm.closeConnection();
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
        } catch (SQLException ex) {
            notifications.erroSintaxe();
        }
    }

    @Override
    public void delete(Integer primaryKey) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try
        {
            String sqlUser = "UPDATE usuario SET deletado = ? WHERE id = ?";
            dbcm.runPreparedSQL(sqlUser, "true", primaryKey );
            
            String sqlProf = "UPDATE professor SET deletado = ? WHERE id = ?";
            dbcm.runPreparedSQL(sqlProf, "true",primaryKey );
            
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
