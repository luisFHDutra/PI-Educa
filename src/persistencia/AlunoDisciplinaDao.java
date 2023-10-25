
package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import negocio.Aluno;
import negocio.AlunoDisciplina;
import negocio.Disciplina;
import negocio.Nota;
import negocio.Presenca;
import negocio.Turma;
import pieduca.Sys;

public class AlunoDisciplinaDao extends DaoAdapter<AlunoDisciplina, Integer> {

    private NotificationSQL notifications = new NotificationSQL();
    
    @Override
    public void create(AlunoDisciplina objeto) throws KeyViolationException, InvalidKeyException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void createPresenca (AlunoDisciplina objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        ArrayList<Presenca> presencas = objeto.getPresencas();

        String sql = "INSERT INTO presenca (aluno_id, disciplina_id, data, presente) VALUES (?, ?, ?, ?)";
        
        PreparedStatement statement = null;
        try {
            // Prepara a instrução SQL
            statement = dbcm.prepareStatement(sql);

            // Percorre a lista de itens e insere cada um no banco de dados
            for (Presenca p : presencas) {
                statement.setInt(1, objeto.getAluno().getIdAluno());
                statement.setInt(2, p.getDisciplina().getIdDisciplina());
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
    
    public void createNota (AlunoDisciplina objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        ArrayList<Nota> notas = objeto.getNotas();

        String sql = "INSERT INTO nota (aluno_id, disciplina_id, nota) VALUES (?, ?, ?)";
        
        PreparedStatement statement = null;
        try {
            // Prepara a instrução SQL
            statement = dbcm.prepareStatement(sql);

            // Percorre a lista de itens e insere cada um no banco de dados
            for (Nota n : notas) {
                statement.setInt(1, objeto.getAluno().getIdAluno());
                statement.setInt(2, n.getDisciplina().getIdDisciplina());
                statement.setDouble(3, n.getNota());

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
    public AlunoDisciplina read(Integer primaryKey) throws NotFoundException {
        AlunoDisciplina a = null;
        DataBaseConnectionManager dbcm;
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            Aluno aluno = DaoFactory.criarAlunoDao().read(primaryKey);

            String sql = "SELECT * FROM aluno WHERE id = ?";

            ResultSet rs = dbcm.runPreparedQuerySQL(sql, primaryKey);

            if (rs.isBeforeFirst()) {
                rs.next();

                ArrayList<Presenca> presencas = new ArrayList();

                String sqlPresenca = "SELECT * FROM presenca WHERE aluno_id = ?;";
                ResultSet rsPresenca = dbcm.runPreparedQuerySQL(sqlPresenca, primaryKey);

                if (rsPresenca.isBeforeFirst()) {
                    while (rsPresenca.next()) { // Use rsPresenca aqui
                        int idDisciplina = rsPresenca.getInt("disciplina_id");
                        String data = rsPresenca.getString("data_presenca");
                        Boolean presente = rsPresenca.getBoolean("presente");

                        Disciplina disciplina = null;
                        try {
                            disciplina = DaoFactory.criarDisciplinaDao().read(idDisciplina);
                        } catch (NotFoundException ex) {
                            notifications.tabelaNaoExiste();
                        }

                        Presenca presenca = new Presenca(disciplina, data, presente);
                        presencas.add(presenca);
                    }
                }

                ArrayList<Nota> notas = new ArrayList();

                String sqlNota = "SELECT * FROM nota WHERE aluno_id = ?;";
                ResultSet rsNota = dbcm.runPreparedQuerySQL(sqlNota, primaryKey);

                if (rsNota.isBeforeFirst()) {
                    while (rsNota.next()) { // Use rsNota aqui
                        int idDisciplina = rsNota.getInt("disciplina_id");
                        double nota = rsNota.getDouble("nota");

                        Disciplina disciplina = null;
                        try {
                            disciplina = DaoFactory.criarDisciplinaDao().read(idDisciplina);
                        } catch (NotFoundException ex) {
                            notifications.tabelaNaoExiste();
                        }

                        Nota n = new Nota(disciplina, nota);
                        notas.add(n);
                    }
                }

                a = new AlunoDisciplina(aluno, presencas, notas);
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
        
        return a;
    }

    @Override
    public ArrayList<AlunoDisciplina> readAll() {
        ArrayList<AlunoDisciplina> lista = new ArrayList();
        
        DataBaseConnectionManager dbcm;
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "SELECT * FROM aluno;";
            
            ResultSet rs = dbcm.runQuerySQL( sql );
            
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String dataNasc = rs.getString("data_nascimento");
                    String rg = rs.getString("rg");
                    String filiacao = rs.getString("filiacao");
                    Boolean deletado = rs.getBoolean("deletado");
                    int idTurma = rs.getInt("turma_id");

                    Turma turma = null;
                    try {
                        turma = DaoFactory.criarTurmaDao().read(idTurma);
                    } catch (NotFoundException ex) {
                        notifications.tabelaNaoExiste();
                    }

                    ArrayList<Presenca> presencas = new ArrayList();

                    String sqlPresenca = "SELECT * FROM presenca WHERE aluno_id = ?;";
                    ResultSet rsPresenca = dbcm.runPreparedQuerySQL(sqlPresenca, id);

                    if (rsPresenca.isBeforeFirst()) {
                        while (rsPresenca.next()) { // Use rsPresenca aqui
                            int idDisciplina = rsPresenca.getInt("disciplina_id");
                            String data = rsPresenca.getString("data_presenca");
                            Boolean presente = rsPresenca.getBoolean("presente");

                            Disciplina disciplina = null;
                            try {
                                disciplina = DaoFactory.criarDisciplinaDao().read(idDisciplina);
                            } catch (NotFoundException ex) {
                                notifications.tabelaNaoExiste();
                            }

                            Presenca presenca = new Presenca(disciplina, data, presente);
                            presencas.add(presenca);
                        }
                    }

                    ArrayList<Nota> notas = new ArrayList();

                    String sqlNota = "SELECT * FROM nota WHERE aluno_id = ?;";
                    ResultSet rsNota = dbcm.runPreparedQuerySQL(sqlNota, id);

                    if (rsNota.isBeforeFirst()) {
                        while (rsNota.next()) { // Use rsNota aqui
                            int idDisciplina = rsNota.getInt("disciplina_id");
                            double nota = rsNota.getDouble("nota");

                            Disciplina disciplina = null;
                            try {
                                disciplina = DaoFactory.criarDisciplinaDao().read(idDisciplina);
                            } catch (NotFoundException ex) {
                                notifications.tabelaNaoExiste();
                            }

                            Nota n = new Nota(disciplina, nota);
                            notas.add(n);
                        }
                    }

                    Aluno a = new Aluno(id, nome, dataNasc, rg, filiacao, deletado, turma);
                    AlunoDisciplina ad = new AlunoDisciplina(a, presencas, notas);
                    lista.add(ad);
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
    public void update(AlunoDisciplina objeto) throws NotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void updatePresenca (AlunoDisciplina objeto, Presenca objeto1) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try
        {
            
            String sql = "";
            
            if(presencaExiste(objeto, objeto1)) {
                sql = "UPDATE presenca SET presente = ? WHERE aluno_id = ? AND disciplina_id = ? AND data_presenca = ?;";
            } else {
                sql = "INSERT INTO presenca (presente, aluno_id, disciplina_id, data_presenca) VALUES (?, ?, ?, ?);";
            }

            dbcm.runPreparedSQL(sql, String.valueOf(objeto1.getPresente()), objeto.getAluno().getIdAluno(), 
                    objeto1.getDisciplina().getIdDisciplina(), objeto1.getData());

            dbcm.closeConnection();
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
        }
    }
    
    private boolean presencaExiste (AlunoDisciplina objeto, Presenca objeto1) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try {
            
            String sql = "SELECT * FROM presenca WHERE aluno_id = ? AND disciplina_id = ? AND data_presenca = ?";
            ResultSet rs = dbcm.runPreparedQuerySQL(sql, objeto.getAluno().getIdAluno(),
                    objeto1.getDisciplina().getIdDisciplina(), objeto1.getData());
            
            if(rs.isBeforeFirst()) {
                return true;
            }
            
            dbcm.closeConnection();
        } catch (DataBaseException ex) {
            notifications.tabelaNaoExiste();
        } catch (SQLException ex) {
            notifications.erroSintaxe();
        }
        
        return false;
    } 
    
    public void updateNota (AlunoDisciplina objeto, Nota objeto1) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try
        {
            String sql = "";
            
            if(notaExiste(objeto, objeto1)) {
                sql = "UPDATE nota SET nota = ? WHERE aluno_id = ? AND disciplina_id = ?";
            } else {
                sql = "INSERT INTO nota (nota, aluno_id, disciplina_id) VALUES (?, ?, ?)";
            }
            
            dbcm.runPreparedSQL(sql, objeto1.getNota(), objeto.getAluno().getIdAluno(), 
                    objeto1.getDisciplina().getIdDisciplina());
            
            dbcm.closeConnection();
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
        }
    }
    
    private boolean notaExiste(AlunoDisciplina objeto, Nota objeto1) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try {
            String sql = "SELECT * FROM nota WHERE aluno_id = ? AND disciplina_id = ?";
            ResultSet rs = dbcm.runPreparedQuerySQL(sql, objeto.getAluno().getIdAluno(), objeto1.getDisciplina().getIdDisciplina());
            
            if(rs.isBeforeFirst()) {
                return true;
            }
            
            dbcm.closeConnection();
        } catch (DataBaseException ex) {
            notifications.tabelaNaoExiste();
        } catch (SQLException ex) {
            notifications.erroSintaxe();
        }
        
        return false;
    } 
    
    @Override
    public void delete(Integer primaryKey) throws NotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
