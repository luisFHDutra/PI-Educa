
package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import negocio.Turma;
import pieduca.Sys;

public class TurmaDao extends DaoAdapter<Turma, Integer> {

    @Override
    public void create(Turma objeto) {
        DataBaseConnectionManager dbcm;
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "INSERT INTO turma VALUES ( ?, ?, ?);";
            
            dbcm.runPreparedSQL(sql, objeto.getIdTurma(), objeto.getNome(), objeto.getAnoLetivo());
        } 
        catch (DataBaseException ex)
        {
            JOptionPane.showMessageDialog(null, 
                    "Chave primária duplicada",
                    "Inserção no banco de dados", JOptionPane.ERROR_MESSAGE);
        } 
    }

    @Override
    public Turma read(Integer primaryKey) throws NotFoundException {
        Turma t = null;
//        DataBaseConnectionManager dbcm;
//        try
//        {
//            dbcm = Sys.getInstance().getDB();
//            
//            String sql = "SELECT * FROM turma WHERE id_turma = ?";
//            
//            ResultSet rs = dbcm.runPreparedQuerySQL(sql, primaryKey );
//            
//            if (rs.isBeforeFirst()) // acho alguma coisa?
//            {
//                rs.next();
//                int id = rs.getInt("id_turma");
//                String nome = rs.getString("nome");
//                int anoLetivo = rs.getInt("ano_letivo");
//                
//                t = new Turma(id,nome,anoLetivo);
//            }
//        } 
//        catch (DataBaseException ex)
//        {
//            JOptionPane.showMessageDialog(null, 
//                    "Erro de sintaxe ou semântica",
//                    "Consulta no banco de dados", JOptionPane.ERROR_MESSAGE);
//        } 
//        catch (SQLException ex)
//        {
//            JOptionPane.showMessageDialog(null, 
//                    "DataType errado na query",
//                    "Consulta no banco de dados", JOptionPane.ERROR_MESSAGE);
//        }
        
        return t;
    }

    @Override
    public ArrayList<Turma> readAll() {
        ArrayList<Turma> lista = new ArrayList();
//        
//        DataBaseConnectionManager dbcm;
//        try
//        {
//            dbcm = Sys.getInstance().getDB();
//            
//            String sql = "SELECT * FROM turma;";
//            
//            ResultSet rs = dbcm.runQuerySQL( sql );
//            
//            if (rs.isBeforeFirst())
//            {
//                rs.next();
//                while (!rs.isAfterLast())
//                {
//                    int id = rs.getInt("id_turma");
//                    String nome = rs.getString("nome");
//                    int anoLetivo = rs.getInt("ano_letivo");
//                
//                    Turma t = new Turma(id,nome,anoLetivo);
//                    lista.add(t);
//                    
//                    rs.next();
//                }
//            }
//
//        } 
//        catch (DataBaseException ex)
//        {
//            JOptionPane.showMessageDialog(null, 
//                    "Erro de sintaxe ou semântica",
//                    "Consulta no banco de dados", JOptionPane.ERROR_MESSAGE);
//        } 
//        catch (SQLException ex)
//        {
//            JOptionPane.showMessageDialog(null, 
//                    "DataType errado na query",
//                    "Consulta no banco de dados", JOptionPane.ERROR_MESSAGE);
//        }
//        
        return lista;
    }

    @Override
    public void update(Turma objeto) throws NotFoundException {
        DataBaseConnectionManager dbcm;
        
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "UPDATE turma SET nome = ?, ano_letivo = ? WHERE id_turma = ?";
            dbcm.runPreparedSQL(sql, objeto.getNome(), objeto.getAnoLetivo(), objeto.getIdTurma() );
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
            
            String sql = "DELETE FROM turma WHERE id_turma = ?";
            dbcm.runPreparedSQL(sql, primaryKey );
        } 
        catch (DataBaseException ex)
        {
            throw new NotFoundException();
        }
    }
    
}
