
package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import negocio.Aluno;
import negocio.AlunoDisciplina;
import negocio.DiaSemana;
import negocio.Disciplina;
import negocio.Periodo;
import negocio.Professor;
import pieduca.Sys;

public class DisciplinaDao extends DaoAdapter<Disciplina, Integer> {

    @Override
    public void create(Disciplina objeto) throws KeyViolationException, InvalidKeyException {
        DataBaseConnectionManager dbcm;
        dbcm = Sys.getInstance().getDB();

        try {
            dbcm.runSQL("begin transaction;");

            String sql = "INSERT INTO disciplina VALUES ( ?, ?, ?, ?, ?);";

            dbcm.runPreparedSQL(sql, objeto.getIdDisciplina(), objeto.getNome(), objeto.getCargaHorariaTotal(),
                    objeto.getDiaSemana().getIdDiaSemana(), objeto.getPeriodo().getIdPeriodo());

            createAlunoDisciplina(objeto); // insert tabela aluno_disciplina
            
            dbcm.runSQL("commit;");

        } catch (DataBaseException ex) {
            try {
                dbcm.runSQL("rollback;");
            } catch (DataBaseException ex1) {
                Logger.getLogger(DisciplinaDao.class.getName()).log(Level.SEVERE, null, ex1);
            }

            JOptionPane.showMessageDialog(null,
                    "Erro no banco de dados",
                    "Inserção no banco de dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createAlunoDisciplina(Disciplina objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();

        ArrayList<AlunoDisciplina> alunoDisc = objeto.getAlunoDisciplina();

        String sql = "INSERT INTO aluno_disciplina (id_disciplina, id_aluno, presenca, nota) VALUES (?, ?, ?, ?)";

        PreparedStatement statement = null;
        try {

            // Prepara a instrução SQL
            statement = dbcm.prepareStatement(sql);

            // Percorre a lista de itens e insere cada um no banco de dados
            for (AlunoDisciplina aluno : alunoDisc) {
                statement.setInt(1, objeto.getIdDisciplina());
                statement.setInt(2, aluno.getAluno().getIdAluno());
                statement.setDouble(3, aluno.getPresenca());
                statement.setDouble(4, aluno.getNota());

                // Executa a instrução SQL para inserir o item
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisciplinaDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public Disciplina read(Integer primaryKey) throws NotFoundException {
        Disciplina d = null;
        DataBaseConnectionManager dbcm;
        try {
            dbcm = Sys.getInstance().getDB();

            String sql = "SELECT * FROM disciplina WHERE id_disciplina = ?";

            ResultSet rs = dbcm.runPreparedQuerySQL(sql, primaryKey);

            if (rs.isBeforeFirst()) // acho alguma coisa?
            {
                rs.next();
                // não precisa while por que eu sei que só tem um resultado
                int id = rs.getInt("id_disciplina");
                String nome = rs.getString("nome");
                int cargaTotal = rs.getInt("carga_total");
                int idDiaSemana = rs.getInt("id_dia_semana");
                int idPeriodo = rs.getInt("id_periodo");

                DiaSemana diaSemana = null;
                try {
                    diaSemana = DaoFactory.criarDiaSemanaDao().read(idDiaSemana);
                } catch (NotFoundException ex) {
                    System.out.println("não existe");
                }

                Periodo periodo = null;
                try {
                    periodo = DaoFactory.criarPeriodoDao().read(idPeriodo);
                } catch (NotFoundException ex) {
                    System.out.println("não existe");
                }

                ArrayList<AlunoDisciplina> alunos = new ArrayList();

                String sqlAluno = "SELECT * FROM aluno_disciplina WHERE id_disciplina = ?;";
                ResultSet rsAluno = dbcm.runPreparedQuerySQL(sqlAluno, primaryKey);
                
                if (rsAluno.isBeforeFirst()) // acho alguma coisa?
                {
                    rsAluno.next();
                    while (!rs.isAfterLast()) {
                        int idAluno = rsAluno.getInt("id_aluno");
                        int presenca = rsAluno.getInt("presenca");
                        double nota = rsAluno.getDouble("nota");

                        Aluno aluno = null;
                        try {
                            aluno = DaoFactory.criarAlunoDao().read(idAluno);
                        } catch (NotFoundException ex) {
                            System.out.println("não existe");
                        }

                        AlunoDisciplina alunoDisciplina = new AlunoDisciplina(aluno, presenca, nota);
                        alunos.add(alunoDisciplina);
                        
                        rs.next();
                    }
                }

                d = new Disciplina(idDiaSemana, nome, cargaTotal, diaSemana, periodo, alunos);
            }
        } catch (DataBaseException ex) {
            JOptionPane.showMessageDialog(null,
                    "Erro de sintaxe ou semântica",
                    "Consulta no banco de dados", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "DataType errado na query",
                    "Consulta no banco de dados", JOptionPane.ERROR_MESSAGE);
        }

        return d;
    }

    @Override
    public ArrayList<Disciplina> readAll() {
        ArrayList<Disciplina> lista = new ArrayList();

        DataBaseConnectionManager dbcm;
        try {
            dbcm = Sys.getInstance().getDB();

            String sql = "SELECT * FROM disciplina;";

            ResultSet rs = dbcm.runQuerySQL(sql);

            if (rs.isBeforeFirst()) // acho alguma coisa?
            {
                rs.next();
                while (!rs.isAfterLast()) {
                    int id = rs.getInt("id_disciplina");
                    String nome = rs.getString("nome");
                    int cargaTotal = rs.getInt("carga_total");
                    int idDiaSemana = rs.getInt("id_dia_semana");
                    int idPeriodo = rs.getInt("id_periodo");

                    DiaSemana diaSemana = null;
                    try {
                        diaSemana = DaoFactory.criarDiaSemanaDao().read(idDiaSemana);
                    } catch (NotFoundException ex) {
                        System.out.println("não existe");
                    }

                    Periodo periodo = null;
                    try {
                        periodo = DaoFactory.criarPeriodoDao().read(idPeriodo);
                    } catch (NotFoundException ex) {
                        System.out.println("não existe");
                    }

                    ArrayList<AlunoDisciplina> alunos = new ArrayList();

                    String sqlAluno = "SELECT * FROM aluno_disciplina WHERE id_disciplina = ?;";
                    ResultSet rsAluno = dbcm.runPreparedQuerySQL(sqlAluno, id);

                    if (rsAluno.isBeforeFirst()) // acho alguma coisa?
                    {
                        rsAluno.next();
                        while (!rs.isAfterLast()) {
                            int idAluno = rsAluno.getInt("id_aluno");
                            int presenca = rsAluno.getInt("presenca");
                            double nota = rsAluno.getDouble("nota");

                            Aluno aluno = null;
                            try {
                                aluno = DaoFactory.criarAlunoDao().read(idAluno);
                            } catch (NotFoundException ex) {
                                System.out.println("não existe");
                            }

                            AlunoDisciplina alunoDisciplina = new AlunoDisciplina(aluno, presenca, nota);
                            alunos.add(alunoDisciplina);

                            rs.next();
                        }
                    }

                    Disciplina d = new Disciplina(idDiaSemana, nome, cargaTotal, diaSemana, periodo, alunos);

                    lista.add(d);

                    rs.next();
                }
            }

        } catch (DataBaseException ex) {
            JOptionPane.showMessageDialog(null,
                    "Erro de sintaxe ou semântica",
                    "Consulta no banco de dados", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "DataType errado na query",
                    "Consulta no banco de dados", JOptionPane.ERROR_MESSAGE);
        }

        return lista;
    }

    @Override
    public void update(Disciplina objeto) throws NotFoundException {
        DataBaseConnectionManager dbcm;
        
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "UPDATE disciplina SET nome = ?, carga_total = ?, id_dia_semana = ?, id_periodo = ? WHERE id_disciplina = ?";
            dbcm.runPreparedSQL(sql, objeto.getNome(), objeto.getCargaHorariaTotal(), objeto.getDiaSemana().getIdDiaSemana(),
                    objeto.getPeriodo().getIdPeriodo(), objeto.getIdDisciplina());
        } 
        catch (DataBaseException ex)
        {
            throw new NotFoundException();
        }
    }

    @Override
    public void delete(Integer primaryKey) throws NotFoundException {
        DataBaseConnectionManager dbcm;

        try {
            dbcm = Sys.getInstance().getDB();

            String sqlDelteAluno = "DELETE FROM aluno_disciplina WHERE id_disciplina = ?";
            dbcm.runPreparedSQL(sqlDelteAluno, primaryKey);
            
            String sql = "DELETE FROM disciplina WHERE id_disciplina = ?";
            dbcm.runPreparedSQL(sql, primaryKey);
        } catch (DataBaseException ex) {
            throw new NotFoundException();
        }
    }
    
}
