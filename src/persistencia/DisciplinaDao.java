
package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.Aluno;
import negocio.Presenca;
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

    public void createPresenca(Disciplina objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        ArrayList<Presenca> presencas = objeto.getPresencas();

        String sql = "INSERT INTO presenca (aluno_id, disciplina_id, data, presente) VALUES (?, ?, ?, ?)";
        
        PreparedStatement statement = null;
        try {
            // Prepara a instrução SQL
            statement = dbcm.prepareStatement(sql);

            // Percorre a lista de itens e insere cada um no banco de dados
            for (Presenca p : presencas) {
                statement.setInt(1, p.getAluno().getIdAluno());
                statement.setInt(2, objeto.getIdDisciplina());
                statement.setString(3, p.getData());
                statement.setBoolean(4, p.getPresente());

                // Executa a instrução SQL para inserir o item
                statement.executeUpdate();
            }

            dbcm.closeConnection();
        } catch (SQLException ex) {
            notifications.erroSintaxe();
        } catch (DataBaseException ex) {
            notifications.conexaoBD();
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

                ArrayList<Presenca> presencas = new ArrayList();

                String sqlPresenca = "SELECT * FROM presenca WHERE disciplina_id = ?;";
                ResultSet rsPresenca = dbcm.runPreparedQuerySQL(sqlPresenca, primaryKey);
                
                if (rsPresenca.isBeforeFirst()) // acho alguma coisa?
                {
                    rsPresenca.next();
                    while (!rs.isAfterLast()) {
                        int idAluno = rsPresenca.getInt("aluno_id");
                        String data = rsPresenca.getString("data");
                        Boolean presente = rsPresenca.getBoolean("presente");

                        Aluno aluno = null;
                        try {
                            aluno = DaoFactory.criarAlunoDao().read(idAluno);
                        } catch (NotFoundException ex) {
                            notifications.tabelaNaoExiste();
                        }

                        Presenca presenca = new Presenca(aluno, data, presente);
                        presencas.add(presenca);
                        
                        rs.next();
                    }
                }

                d = new Disciplina(id, nome, cargaTotal, presencas);
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

                    ArrayList<Presenca> presencas = new ArrayList();

                    String sqlPresenca = "SELECT * FROM presenca WHERE disciplina_id = ?;";
                    ResultSet rsPresenca = dbcm.runPreparedQuerySQL(sqlPresenca, id);

                    if (rsPresenca.isBeforeFirst()) // acho alguma coisa?
                    {
                        rsPresenca.next();
                        while (!rs.isAfterLast()) {
                            int idAluno = rsPresenca.getInt("aluno_id");
                            String data = rsPresenca.getString("data");
                            Boolean presente = rsPresenca.getBoolean("presente");

                            Aluno aluno = null;
                            try {
                                aluno = DaoFactory.criarAlunoDao().read(idAluno);
                            } catch (NotFoundException ex) {
                                notifications.tabelaNaoExiste();
                            }

                            Presenca presenca = new Presenca(aluno, data, presente);
                            presencas.add(presenca);

                            rs.next();
                        }
                    }

                    Disciplina d = new Disciplina(id, nome, cargaTotal, presencas);

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

    public void updatePresenca(Disciplina objeto, Presenca objeto1) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try
        {
            String sql = "UPDATE presenca SET presente = ? WHERE aluno_id = ? AND disciplina_id = ? AND data = ?";
            dbcm.runPreparedSQL(sql, objeto1.getPresente(), objeto1.getAluno().getIdAluno(), 
                    objeto.getIdDisciplina(), objeto1.getData());
            
            dbcm.closeConnection();
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
        }
    }
}
