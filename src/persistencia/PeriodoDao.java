
package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import negocio.Periodo;
import pieduca.Sys;

public class PeriodoDao extends DaoAdapter<Periodo, Integer> {

    @Override
    public void create(Periodo objeto) {
        DataBaseConnectionManager dbcm;
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "INSERT INTO perido VALUES ( ?, ?, ?);";
            
            dbcm.runPreparedSQL(sql, objeto.getIdPeriodo(), objeto.getHoraInicio(), objeto.getHoraFim());
        } 
        catch (DataBaseException ex)
        {
            JOptionPane.showMessageDialog(null, 
                    "Chave primária duplicada",
                    "Inserção no banco de dados", JOptionPane.ERROR_MESSAGE);
        } 
    }

    @Override
    public Periodo read(Integer primaryKey) throws NotFoundException {
        Periodo p = null;
        DataBaseConnectionManager dbcm;
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "SELECT * FROM periodo WHERE id_periodo = ?";
            
            ResultSet rs = dbcm.runPreparedQuerySQL(sql, primaryKey );
            
            if (rs.isBeforeFirst()) // acho alguma coisa?
            {
                rs.next();
                int id = rs.getInt("id_periodo");
                int horaInicio = rs.getInt("hora_inicio");
                int horaFim = rs.getInt("hora_fim");
                
                p = new Periodo(id,horaInicio,horaFim);
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
        
        return p;
    }

    @Override
    public ArrayList<Periodo> readAll() {
        ArrayList<Periodo> lista = new ArrayList();
        
        DataBaseConnectionManager dbcm;
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "SELECT * FROM periodo;";
            
            ResultSet rs = dbcm.runQuerySQL( sql );
            
            if (rs.isBeforeFirst())
            {
                rs.next();
                while (!rs.isAfterLast())
                {
                    int id = rs.getInt("id_periodo");
                    int horaInicio = rs.getInt("hora_inicio");
                    int horaFim = rs.getInt("hora_fim");

                    Periodo p = new Periodo(id,horaInicio,horaFim);
                    lista.add(p);
                    
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
    public void update(Periodo objeto) throws NotFoundException {
        DataBaseConnectionManager dbcm;
        
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "UPDATE periodo SET hora_inicio = ?, hora_fim = ? WHERE id_periodo = ?";
            dbcm.runPreparedSQL(sql, objeto.getHoraInicio(), objeto.getHoraFim(), objeto.getIdPeriodo());
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
            
            String sql = "DELETE FROM periodo WHERE id_periodo = ?";
            dbcm.runPreparedSQL(sql, primaryKey );
        } 
        catch (DataBaseException ex)
        {
            throw new NotFoundException();
        }
    }
    
}
