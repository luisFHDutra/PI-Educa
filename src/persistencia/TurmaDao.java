
package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import negocio.Disciplina;
import negocio.Professor;
import negocio.Turma;
import pieduca.Sys;

public class TurmaDao extends DaoAdapter<Turma, Integer> {

    private NotificationSQL notifications = new NotificationSQL();
    
    @Override
    public void create(Turma objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try
        {
            
            String sql = "INSERT INTO turma VALUES ( ?, ?, ?);";
            
            dbcm.runPreparedSQL(sql, objeto.getIdTurma(), objeto.getNome(), objeto.getAnoLetivo());
            
            createTurmaDisciplina(objeto);
            
            dbcm.closeConnection();
        } 
        catch (DataBaseException ex)
        {
            notifications.chaveDuplicada();
        } 
    }

    public void createMinistra (Turma objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        ArrayList<Professor> profs = objeto.getProfessores();

        String sql = "INSERT INTO ministra (professor_id, turma_id) VALUES (?, ?)";
        
        PreparedStatement statement = null;
        try {
            // Prepara a instrução SQL
            statement = dbcm.prepareStatement(sql);

            // Percorre a lista de itens e insere cada um no banco de dados
            for (Professor p : profs) {
                statement.setInt(1, p.getIdProfessor());
                statement.setInt(2, objeto.getIdTurma());

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
    
    public void createTurmaDisciplina (Turma objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        ArrayList<Disciplina> discs = DaoFactory.criarDisciplinaDao().readAll();

        String sql = "INSERT INTO turma_disciplina (turma_id, disciplina_id) VALUES (?, ?)";
        
        PreparedStatement statement = null;
        try {
            // Prepara a instrução SQL
            statement = dbcm.prepareStatement(sql);

            // Percorre a lista de itens e insere cada um no banco de dados
            for (Disciplina d : discs) {
                statement.setInt(1, objeto.getIdTurma());
                statement.setInt(2, d.getIdDisciplina());

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
    public Turma read(Integer primaryKey) throws NotFoundException {
        Turma t = null;
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();

        try {
            String sql = "SELECT * FROM turma WHERE id = ?";
            ResultSet rs = dbcm.runPreparedQuerySQL(sql, primaryKey);

            if (rs.next()) { // Use um único if em vez de isBeforeFirst e next
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String ano = rs.getString("ano");

                ArrayList<Professor> profs = new ArrayList<>();
                ArrayList<Disciplina> discs = new ArrayList();

                String sqlMinistra = "SELECT * FROM ministra WHERE turma_id = ?";
                ResultSet rsMinistra = dbcm.runPreparedQuerySQL(sqlMinistra, primaryKey);

                while (rsMinistra.next()) {
                    int idProf = rsMinistra.getInt("professor_id");
                    Professor professor = null;
                    try {
                        professor = DaoFactory.criarProfessorDao().read(idProf);
                    } catch (NotFoundException ex) {
                        notifications.tabelaNaoExiste();
                    }
                    profs.add(professor);
                }

                String sqlDisc = "SELECT * FROM turma_disciplina WHERE turma_id = ?";
                ResultSet rsDisc = dbcm.runPreparedQuerySQL(sqlDisc, primaryKey);

                while (rsDisc.next()) {
                    int idDisc = rsDisc.getInt("disciplina_id");
                    Disciplina disciplina = null;
                    try {
                        disciplina = DaoFactory.criarDisciplinaDao().read(idDisc);
                    } catch (NotFoundException ex) {
                        notifications.tabelaNaoExiste();
                    }
                    discs.add(disciplina);
                }

                t = new Turma(id, nome, ano, profs, discs, 0);
            }

            dbcm.closeConnection();
        } catch (DataBaseException ex) {
            notifications.erroSintaxe();
        } catch (SQLException ex) {
            notifications.dataTypeErrado();
        }

        return t;
    }

    @Override
    public ArrayList<Turma> readAll() {
        ArrayList<Turma> lista = new ArrayList<>();
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();

        try {
            String sql = "SELECT * FROM turma";
            ResultSet rs = dbcm.runQuerySQL(sql);

            while (rs.next()) { // Use um loop while para percorrer as linhas do ResultSet
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String ano = rs.getString("ano");

                ArrayList<Professor> profs = new ArrayList<>();
                ArrayList<Disciplina> discs = new ArrayList<>();

                String sqlMinistra = "SELECT * FROM ministra WHERE turma_id = ?";
                ResultSet rsMinistra = dbcm.runPreparedQuerySQL(sqlMinistra, id);

                while (rsMinistra.next()) {
                    int idProf = rsMinistra.getInt("professor_id");
                    Professor professor = null;
                    try {
                        professor = DaoFactory.criarProfessorDao().read(idProf);
                    } catch (NotFoundException ex) {
                        notifications.tabelaNaoExiste();
                    }
                    profs.add(professor);
                }

                String sqlDisc = "SELECT * FROM turma_disciplina WHERE turma_id = ?";
                ResultSet rsDisc = dbcm.runPreparedQuerySQL(sqlDisc, id);

                while (rsDisc.next()) {
                    int idDisc = rsDisc.getInt("disciplina_id");
                    Disciplina disciplina = null;
                    try {
                        disciplina = DaoFactory.criarDisciplinaDao().read(idDisc);
                    } catch (NotFoundException ex) {
                        notifications.tabelaNaoExiste();
                    }
                    discs.add(disciplina);
                }

                Turma t = new Turma(id, nome, ano, profs, discs, 0);
                lista.add(t);
            }

            dbcm.closeConnection();
        } catch (DataBaseException ex) {
            notifications.erroSintaxe();
        } catch (SQLException ex) {
            notifications.dataTypeErrado();
        }

        return lista;
    }

    @Override
    public void update(Turma objeto) throws NotFoundException {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try
        {
            String sql = "UPDATE turma SET nome = ?, ano_letivo = ? WHERE id_turma = ?";
            dbcm.runPreparedSQL(sql, objeto.getNome(), objeto.getAnoLetivo(), objeto.getIdTurma() );
            
            dbcm.closeConnection();
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
        }
    }

    public void updateMinistra (Turma objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try
        {
            String sql = "DELETE FROM ministra WHERE turma_id = ?";
            dbcm.runPreparedSQL(sql, objeto.getIdTurma());
            
            dbcm.closeConnection();
            
            createMinistra(objeto);
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
        }
        
    }
    
    public void updateTurmaDisciplina (Turma objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try
        {
            String sql = "DELETE FROM turma_disciplina WHERE turma_id = ?";
            dbcm.runPreparedSQL(sql, objeto.getIdTurma());
            
            dbcm.closeConnection();
            
            createTurmaDisciplina(objeto);
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
