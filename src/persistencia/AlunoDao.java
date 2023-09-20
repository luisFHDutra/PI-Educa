
package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import negocio.Aluno;
import negocio.Turma;
import pieduca.Sys;

public class AlunoDao extends DaoAdapter<Aluno, Integer> {

    @Override
    public void create(Aluno objeto) {
        DataBaseConnectionManager dbcm;
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "INSERT INTO aluno VALUES ( ?, ?, ?, ?, ?);";
            
            dbcm.runPreparedSQL(sql, objeto.getIdAluno(), objeto.getNome(), objeto.getDataNascimento(),
                    objeto.getRg(), objeto.getFiliacao());
        } 
        catch (DataBaseException ex)
        {
            JOptionPane.showMessageDialog(null, 
                    "Chave primária duplicada",
                    "Inserção no banco de dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public Aluno read(Integer primaryKey) throws NotFoundException {
        Aluno a = null;
        DataBaseConnectionManager dbcm;
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "SELECT * FROM aluno WHERE id_aluno = ?";
            
            ResultSet rs = dbcm.runPreparedQuerySQL(sql, primaryKey );
            
            if (rs.isBeforeFirst()) 
            {
                rs.next();
                int id = rs.getInt("id_aluno");
                String nome = rs.getString("nome");
                String dataNasc = rs.getString("data_nasc");
                String rg = rs.getString("rg");
                String filiacao = rs.getString("filiacao");
                
                a = new Aluno(id,nome, dataNasc, rg, filiacao);
            }
        } 
        catch (DataBaseException ex)
        {
            JOptionPane.showMessageDialog(null, 
                    "Erro de sintaxe ou semântica",
                    "Consulta no banco de dados", JOptionPane.ERROR_MESSAGE);
        } 
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, 
                    "DataType errado na query",
                    "Consulta no banco de dados", JOptionPane.ERROR_MESSAGE);
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
                    int id = rs.getInt("id_aluno");
                    String nome = rs.getString("nome");
                    String dataNasc = rs.getString("data_nasc");
                    String rg = rs.getString("rg");
                    String filiacao = rs.getString("filiacao");

                    Aluno a = new Aluno(id,nome, dataNasc, rg, filiacao);
                    lista.add(a);
                    
                    rs.next();
                }
            }

        } 
        catch (DataBaseException ex)
        {
            JOptionPane.showMessageDialog(null, 
                    "Erro de sintaxe ou semântica",
                    "Consulta no banco de dados", JOptionPane.ERROR_MESSAGE);
        } 
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, 
                    "DataType errado na query",
                    "Consulta no banco de dados", JOptionPane.ERROR_MESSAGE);
        }
        
        return lista;
    }

    @Override
    public void update(Aluno objeto) throws NotFoundException {
        DataBaseConnectionManager dbcm;
        
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "UPDATE aluno SET nome = ?, data_nascimento = ?, rg = ?, filiacao = ? WHERE id_aluno = ?";
            dbcm.runPreparedSQL(sql, objeto.getNome(), objeto.getDataNascimento(), objeto.getRg(),
                    objeto.getFiliacao(), objeto.getIdAluno());
        } 
        catch (DataBaseException ex)
        {
            throw new NotFoundException();
        }
    }

    @Override
    public void delete(Integer primaryKey) throws NotFoundException {
        DataBaseConnectionManager dbcm;
        
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "DELETE FROM aluno WHERE id_aluno = ?";
            dbcm.runPreparedSQL(sql, primaryKey );
        } 
        catch (DataBaseException ex)
        {
            throw new NotFoundException();
        }
    }
    
}
