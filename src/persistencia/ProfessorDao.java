
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
import negocio.Disciplina;
import negocio.Professor;
import negocio.Usuario;
import pieduca.Sys;

public class ProfessorDao extends DaoAdapter<Professor, Integer> {

    @Override
    public void create(Professor objeto) {
        DataBaseConnectionManager dbcm;
        dbcm = Sys.getInstance().getDB();
        try
        {
            dbcm.runSQL("begin transaction;");
            
            String sql = "INSERT INTO professor VALUES ( ?, ?, ?, ?, ?, ?);";
            
            dbcm.runPreparedSQL(sql, objeto.getIdProfessor(), objeto.getNome(), objeto.getCpf(),
                    objeto.getEndereco(), objeto.getDataNascimento(), objeto.getUsuario().getIdUsuario());
            
            createProfessorDisciplina(objeto);
            
            dbcm.runSQL("commit;");
            
        } catch (DataBaseException ex) {
            try {
                dbcm.runSQL("rollback;");
            } catch (DataBaseException ex1) {
                Logger.getLogger(ProfessorDao.class.getName()).log(Level.SEVERE, null, ex1);
            }

            JOptionPane.showMessageDialog(null,
                    "Erro no banco de dados",
                    "Inserção no banco de dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createProfessorDisciplina(Professor objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();

        ArrayList<Disciplina> professorDisc = objeto.getDisciplinas();

        String sql = "INSERT INTO professor_disciplina (id_disciplina, id_professor) VALUES (?, ?)";

        PreparedStatement statement = null;
        try {

            // Prepara a instrução SQL
            statement = dbcm.prepareStatement(sql);

            // Percorre a lista de itens e insere cada um no banco de dados
            for (Disciplina disciplina : professorDisc) {
                statement.setInt(1, disciplina.getIdDisciplina());
                statement.setInt(2, objeto.getIdProfessor());

                // Executa a instrução SQL para inserir o item
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisciplinaDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public Professor read(Integer primaryKey) throws NotFoundException {
        Professor p = null;
        DataBaseConnectionManager dbcm;
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "SELECT * FROM professor WHERE id_professor = ?";
            
            ResultSet rs = dbcm.runPreparedQuerySQL(sql, primaryKey );
            
            if (rs.isBeforeFirst()) 
            {
                rs.next();
                int id = rs.getInt("id_professor");
                String nome = rs.getString("nome");
                String dataNasc = rs.getString("data_nasc");
                String cpf = rs.getString("cpf");
                String endereco = rs.getString("endereco");
                int idUser = rs.getInt("id_usuario");
                
                Usuario user = null;
                try {
                    user = DaoFactory.criarUsuarioDao().read(idUser);
                } catch (NotFoundException ex) {
                    System.out.println("não existe");
                }
                
                ArrayList<Disciplina> disciplinas = new ArrayList();

                String sqlDisciplina = "SELECT * FROM professor_disciplina WHERE id_professor = ?;";
                ResultSet rsDisciplina = dbcm.runPreparedQuerySQL(sqlDisciplina, primaryKey);
                
                if (rsDisciplina.isBeforeFirst()) // acho alguma coisa?
                {
                    rsDisciplina.next();
                    while (!rs.isAfterLast()) {
                        int idDisciplina = rsDisciplina.getInt("id_disciplina");

                        Disciplina disciplina = null;
                        try {
                            disciplina = DaoFactory.criarDisciplinaDao().read(idDisciplina);
                        } catch (NotFoundException ex) {
                            System.out.println("não existe");
                        }

                        disciplinas.add(disciplina);
                        
                        rs.next();
                    }
                }
                
                p = new Professor(idUser, nome, cpf, endereco, dataNasc, user, disciplinas);
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
    public ArrayList<Professor> readAll() {
        ArrayList<Professor> lista = new ArrayList();
        
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
                    int id = rs.getInt("id_professor");
                    String nome = rs.getString("nome");
                    String dataNasc = rs.getString("data_nasc");
                    String cpf = rs.getString("cpf");
                    String endereco = rs.getString("endereco");
                    int idUser = rs.getInt("id_usuario");

                    Usuario user = null;
                    try {
                        user = DaoFactory.criarUsuarioDao().read(idUser);
                    } catch (NotFoundException ex) {
                        System.out.println("não existe");
                    }

                    ArrayList<Disciplina> disciplinas = new ArrayList();

                    String sqlDisciplina = "SELECT * FROM professor_disciplina WHERE id_professor = ?;";
                    ResultSet rsDisciplina = dbcm.runPreparedQuerySQL(sqlDisciplina, id);

                    if (rsDisciplina.isBeforeFirst()) // acho alguma coisa?
                    {
                        rsDisciplina.next();
                        while (!rs.isAfterLast()) {
                            int idDisciplina = rsDisciplina.getInt("id_disciplina");

                            Disciplina disciplina = null;
                            try {
                                disciplina = DaoFactory.criarDisciplinaDao().read(idDisciplina);
                            } catch (NotFoundException ex) {
                                System.out.println("não existe");
                            }

                            disciplinas.add(disciplina);

                            rs.next();
                        }
                    }

                    Professor p = new Professor(idUser, nome, cpf, endereco, dataNasc, user, disciplinas);
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
    public void update(Professor objeto) throws NotFoundException {
        DataBaseConnectionManager dbcm;
        
        try
        {
            dbcm = Sys.getInstance().getDB();
            
            String sql = "UPDATE professor SET nome = ?, cpf = ?, endereco = ?, data_nasc = ?, id_usuario WHERE id_professor = ?";
            dbcm.runPreparedSQL(sql, objeto.getNome(), objeto.getCpf(), objeto.getEndereco(),
                    objeto.getDataNascimento(), objeto.getUsuario().getIdUsuario(), objeto.getIdProfessor());
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
            
            String sql = "DELETE FROM professor WHERE id_professor = ?";
            dbcm.runPreparedSQL(sql, primaryKey );
        } 
        catch (DataBaseException ex)
        {
            throw new NotFoundException();
        }
    }
    
}
