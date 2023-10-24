
package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import negocio.Disciplina;
import pieduca.Sys;

public class DisciplinaDao extends DaoAdapter<Disciplina, Integer> {

    private NotificationSQL notifications = new NotificationSQL();
    
    @Override
    public void create(Disciplina objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();

        try {
            String sql = "INSERT INTO disciplina VALUES ( ?, ?, ?);";

            dbcm.runPreparedSQL(sql, objeto.getIdDisciplina(), objeto.getNome(), objeto.getCargaHorariaTotal());

            dbcm.closeConnection();
        }
        catch (DataBaseException ex)
        {
            notifications.chaveDuplicada();
        }
    }
    
    @Override
    public Disciplina read(Integer primaryKey) throws NotFoundException {
        Disciplina d = null;
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try {
            String sql = "SELECT * FROM disciplina WHERE id = ?";

            ResultSet rs = dbcm.runPreparedQuerySQL(sql, primaryKey);

            if (rs.isBeforeFirst()) // acho alguma coisa?
            {
                rs.next();
                // não precisa while por que eu sei que só tem um resultado
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int cargaTotal = rs.getInt("carga_horaria");
                
                d = new Disciplina(id, nome, cargaTotal);
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

        return d;
    }

    @Override
    public ArrayList<Disciplina> readAll() {
        ArrayList<Disciplina> lista = new ArrayList();

        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try {
            String sql = "SELECT * FROM disciplina;";

            ResultSet rs = dbcm.runQuerySQL(sql);

            if (rs.isBeforeFirst()) // acho alguma coisa?
            {
                rs.next();
                while (!rs.isAfterLast()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    int cargaTotal = rs.getInt("carga_horaria");

                    Disciplina d = new Disciplina(id, nome, cargaTotal);

                    lista.add(d);

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
    public void update(Disciplina objeto) throws NotFoundException {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try
        {
            String sql = "UPDATE disciplina SET nome = ?, carga_horaria = ? WHERE id = ?";
            dbcm.runPreparedSQL(sql, objeto.getNome(), objeto.getCargaHorariaTotal(), objeto.getIdDisciplina());
            
            dbcm.closeConnection();
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
        }
    }

    @Override
    public void delete(Integer primaryKey) throws NotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
