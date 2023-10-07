
package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import negocio.Aluno;
import pieduca.Sys;

public class AlunoDao extends DaoAdapter<Aluno, Integer> {

    private NotificationSQL notifications = new NotificationSQL();
    
    @Override
    public void create(Aluno objeto) {
        DataBaseConnectionManager dbcm;
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sqls = "INSERT INTO aluno VALUES ( ?, ?, ?, ?, ?, ?);";
            
            dbcm.runPreparedSQL(sqls, objeto.getIdAluno(), objeto.getNome(), objeto.getDataNascimento(),
                   objeto.getFiliacao(), objeto.getRg(), objeto.getDeletado().toString());
            
        } 
        catch (DataBaseException ex)
        {
            notifications.chaveDuplicada();
        }
    }

    @Override
    public Aluno read(Integer primaryKey) throws NotFoundException {
        Aluno a = null;
        DataBaseConnectionManager dbcm;
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "SELECT * FROM aluno WHERE id = ?";
            
            ResultSet rs = dbcm.runPreparedQuerySQL(sql, primaryKey );
            
            if (rs.isBeforeFirst()) 
            {
                rs.next();
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String dataNasc = rs.getString("data_nascimento");
                String rg = rs.getString("rg");
                String filiacao = rs.getString("filiacao");
                Boolean deletado = rs.getBoolean("deletado");
                
                a = new Aluno(id, nome, dataNasc, rg, filiacao, deletado);
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
        
        return a;
    }

    @Override
    public ArrayList<Aluno> readAll() {
        ArrayList<Aluno> lista = new ArrayList();
        
        DataBaseConnectionManager dbcm;
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "SELECT * FROM aluno;";
            
            ResultSet rs = dbcm.runQuerySQL( sql );
            
            if (rs.isBeforeFirst())
            {
                rs.next();
                while (!rs.isAfterLast())
                {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String dataNasc = rs.getString("data_nascimento");
                    String rg = rs.getString("rg");
                    String filiacao = rs.getString("filiacao");
                    Boolean deletado = rs.getBoolean("deletado");

                    Aluno a = new Aluno(id,nome, dataNasc, rg, filiacao, deletado);
                    lista.add(a);
                    
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
    public void update(Aluno objeto) {
        DataBaseConnectionManager dbcm;
        
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "UPDATE aluno SET nome = ?, data_nascimento = ?, rg = ?, filiacao = ?, deletado = ? WHERE id = ?";
            dbcm.runPreparedSQL(sql, objeto.getNome(), objeto.getDataNascimento(), objeto.getRg(),
                    objeto.getFiliacao(), objeto.getDeletado().toString(), objeto.getIdAluno());
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
            String sql = "DELETE FROM aluno WHERE id = ?";
            dbcm.runPreparedSQL(sql, primaryKey );
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
        }
    }
    
    public Integer maxId() {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try {
            String sql = "SELECT MAX(id) FROM aluno";
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
