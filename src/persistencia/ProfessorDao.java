
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
//            dbcm.runSQL("begin transaction;");
            
            String sql = "INSERT INTO professor VALUES ( ?, ?, ?, ?);";
            
            dbcm.runPreparedSQL(sql, objeto.getNome(), objeto.getAreaEspecializacao(),objeto.getContato());
            
            String sqlUser = "INSERT INTO professor VALUES ( ?, ?, ?, ?);";
            
            dbcm.runPreparedSQL(sqlUser, objeto.getNome(), objeto.getAreaEspecializacao(),objeto.getContato());
            
//            dbcm.runSQL("commit;");
            
        } catch (DataBaseException ex) {
//            try {
////                dbcm.runSQL("rollback;");
//            } catch (DataBaseException ex1) {
//                Logger.getLogger(ProfessorDao.class.getName()).log(Level.SEVERE, null, ex1);
//            }

            JOptionPane.showMessageDialog(null,
                    "Chave primária duplicada",
                    "Inserção no banco de dados", JOptionPane.ERROR_MESSAGE);
        }
    }

//    private void createProfessorDisciplina(Professor objeto) {
//        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
//
//        ArrayList<Disciplina> professorDisc = objeto.getDisciplinas();
//
//        String sql = "INSERT INTO professor_disciplina (id_disciplina, id_professor) VALUES (?, ?)";
//
//        PreparedStatement statement = null;
//        try {
//
//            // Prepara a instrução SQL
//            statement = dbcm.prepareStatement(sql);
//
//            // Percorre a lista de itens e insere cada um no banco de dados
//            for (Disciplina disciplina : professorDisc) {
//                statement.setInt(1, disciplina.getIdDisciplina());
//                statement.setInt(2, objeto.getIdProfessor());
//
//                // Executa a instrução SQL para inserir o item
//                statement.executeUpdate();
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DisciplinaDao.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
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
                String areaEspecializacao = rs.getString("area_especializacao");
                String contato = rs.getString("contato");
                
                Usuario user = null;
                try {
                    user = DaoFactory.criarUsuarioDao().read(id);
                } catch (NotFoundException ex) {
                    System.out.println("não existe");
                }
                
//                ArrayList<Disciplina> disciplinas = new ArrayList();

//                String sqlDisciplina = "SELECT * FROM professor_disciplina WHERE id_professor = ?;";
//                ResultSet rsDisciplina = dbcm.runPreparedQuerySQL(sqlDisciplina, primaryKey);
                
//                if (rsDisciplina.isBeforeFirst()) // acho alguma coisa?
//                {
//                    rsDisciplina.next();
//                    while (!rs.isAfterLast()) {
//                        int idDisciplina = rsDisciplina.getInt("id_disciplina");
//
//                        Disciplina disciplina = null;
//                        try {
//                            disciplina = DaoFactory.criarDisciplinaDao().read(idDisciplina);
//                        } catch (NotFoundException ex) {
//                            System.out.println("não existe");
//                        }
//
//                        disciplinas.add(disciplina);
//                        
//                        rs.next();
//                    }
//                }
                
                p = new Professor(id, nome, areaEspecializacao, contato, user);
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
                    String areaEspecializacao = rs.getString("area_especializacao");
                    String contato = rs.getString("contato");

                    Usuario user = null;
                    try {
                        user = DaoFactory.criarUsuarioDao().read(id);
                    } catch (NotFoundException ex) {
                        System.out.println("não existe");
                    }

                    Professor p = new Professor(id, nome, areaEspecializacao, contato, user);
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
            
            String sql = "UPDATE professor SET nome = ?, area_especializacao = ?, contato = ? WHERE id_professor = ?";
            dbcm.runPreparedSQL(sql, objeto.getNome(), objeto.getAreaEspecializacao(), objeto.getContato(),
                    objeto.getIdProfessor());
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
    
    public Integer maxId() {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try {
            String sql = "SELECT MAX(id) FROM professor";
            ResultSet rs = dbcm.runQuerySQL(sql);

            int ultimoID = 0;

            if (rs.next()) {
             // Obtém o último ID da consulta
             ultimoID = rs.getInt(1);
            }

            // Calcula o próximo ID
            return ultimoID + 1;
        
        } catch (SQLException e) {
            System.out.println("erro");
        } catch (DataBaseException ex) {
            Logger.getLogger(ProfessorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
         
    }
    
}
