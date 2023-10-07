
package persistencia;

import db.DataBaseConnectionManager;
import db.DataBaseException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import negocio.Disciplina;
import negocio.Professor;
import negocio.Usuario;
import pieduca.Sys;

public class ProfessorDao extends DaoAdapter<Professor, Integer> {

    private NotificationSQL notifications = new NotificationSQL();
    
    @Override
    public void create(Professor objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try
        {
            dbcm.runSQL("begin transaction;");
            
            String sql = "INSERT INTO professor VALUES ( ?, ?, ?, ?, ?);";
            
            dbcm.runPreparedSQL(sql, objeto.getIdProfessor(), objeto.getNome(), objeto.getAreaEspecializacao()
                    ,objeto.getContato(), objeto.getDeletado().toString());
            
            cadastrarDisciplinas(objeto);
            
            cadastrarUsuario(objeto);
            
            dbcm.runSQL("commit;");
            
            dbcm.closeConnection();
        } catch (DataBaseException ex) {
            try {
                dbcm.runSQL("rollback;");
                
                dbcm.closeConnection();
            } catch (DataBaseException ex1) {
                notifications.rollback();
            }

            notifications.chaveDuplicada();
        }
    }
    
    private void cadastrarDisciplinas (Professor objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();

        ArrayList<Disciplina> disciplinas = objeto.getDisciplinas();

        String sql = "INSERT INTO ensina (professor_id, disciplina_id) VALUES (?, ?)";

        PreparedStatement statement = null;
        try {

            // Prepara a instrução SQL
            statement = dbcm.prepareStatement(sql);

            // Percorre a lista de itens e insere cada um no banco de dados
            for (Disciplina disc : disciplinas) {
                statement.setInt(1, objeto.getIdProfessor());
                statement.setInt(2, disc.getIdDisciplina());

                // Executa a instrução SQL para inserir o item
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            notifications.erroSintaxe();
        }
    }
    
    private void cadastrarUsuario (Professor objeto) {
        
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try
        {
            String sqlUser = "INSERT INTO usuario (id, senha, permissao_id, deletado) VALUES ( ?, ?, ?, ?);";
            
            PreparedStatement statement = dbcm.prepareStatement(sqlUser);
            statement.setInt(1, objeto.getUsuario().getId());
            statement.setString(2, objeto.getUsuario().getHashCode());
            statement.setInt(3, objeto.getUsuario().getPermissao().getIdPermissao());
            statement.setBoolean(4, objeto.getUsuario().getDeletado());
            statement.executeUpdate();
            
        } catch (SQLException ex) {
            notifications.erroSintaxe();
        }
    }
    
    @Override
    public Professor read(Integer primaryKey) throws NotFoundException {
        Professor p = null;
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try
        {
            String sql = "SELECT * FROM professor WHERE id = ?";
            
            ResultSet rs = dbcm.runPreparedQuerySQL(sql, primaryKey );
            
            if (rs.isBeforeFirst()) 
            {
                rs.next();
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String areaEspecializacao = rs.getString("area_especializacao");
                String contato = rs.getString("contato");
                Boolean deletado = rs.getBoolean("deletado");
                
                Usuario user = null;
                try {
                    user = DaoFactory.criarUsuarioDao().read(id);
                } catch (NotFoundException ex) {
                   notifications.tabelaNaoExiste();
                }
                
                ArrayList<Disciplina> disciplinas = new ArrayList();

                String sqlDisc = "SELECT * FROM ensina WHERE professor_id = ?;";
                ResultSet rsDisc = dbcm.runPreparedQuerySQL(sqlDisc, id);

                if (rsDisc.isBeforeFirst()) // acho alguma coisa?
                {
                    rsDisc.next();
                    while (!rs.isAfterLast()) {
                        int idDisc = rsDisc.getInt("disciplina_id");

                        Disciplina disciplina = null;
                        try {
                            disciplina = DaoFactory.criarDisciplinaDao().read(idDisc);
                        } catch (NotFoundException ex) {
                            notifications.tabelaNaoExiste();
                        }

                        disciplinas.add(disciplina);

                        rs.next();
                        }
                    }
                
                p = new Professor(id, nome, areaEspecializacao, contato, user, deletado, disciplinas);
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
        
        return p;
    }

    @Override
    public ArrayList<Professor> readAll() {
        ArrayList<Professor> lista = new ArrayList();
        
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        try
        {
            String sql = "SELECT * FROM professor;";
            
            ResultSet rs = dbcm.runQuerySQL( sql );
            
            if (rs.isBeforeFirst())
            {
                rs.next();
                while (!rs.isAfterLast())
                {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String areaEspecializacao = rs.getString("area_especializacao");
                    String contato = rs.getString("contato");
                    Boolean deletado = rs.getBoolean("deletado");
                    
                    Usuario user = null;
                    try {
                        user = DaoFactory.criarUsuarioDao().read(id);
                    } catch (NotFoundException ex) {
                        notifications.tabelaNaoExiste();
                    }
                    ArrayList<Disciplina> disciplinas = new ArrayList();

                    String sqlDisc = "SELECT * FROM ensina WHERE professor_id = ?;";
                    ResultSet rsDisc = dbcm.runPreparedQuerySQL(sqlDisc, id);

                    if (rsDisc.isBeforeFirst()) // acho alguma coisa?
                    {
                        rsDisc.next();
                        while (!rs.isAfterLast()) {
                            int idDisc = rsDisc.getInt("disciplina_id");

                            Disciplina disciplina = null;
                            try {
                                disciplina = DaoFactory.criarDisciplinaDao().read(idDisc);
                            } catch (NotFoundException ex) {
                                notifications.tabelaNaoExiste();
                            }

                            disciplinas.add(disciplina);

                            rs.next();
                        }
                    }
                
                    Professor p = new Professor(id, nome, areaEspecializacao, contato, user, deletado, disciplinas);
                    lista.add(p);
                    
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
    public void update(Professor objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try
        {
            String sql = "UPDATE professor SET nome = ?, area_especializacao = ?, contato = ?, deletado = ? WHERE id = ?";
            dbcm.runPreparedSQL(sql, objeto.getNome(), objeto.getAreaEspecializacao(), objeto.getContato(),
                    objeto.getDeletado().toString(), objeto.getIdProfessor());
            
            dbcm.closeConnection();
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
        }
    }

    @Override
    public void delete(Integer primaryKey) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try
        {
            String sqlUser = "UPDATE usuario SET deletado = ? WHERE id = ?";
            dbcm.runPreparedSQL(sqlUser, "true", primaryKey );
            
            String sqlProf = "UPDATE professor SET deletado = ? WHERE id = ?";
            dbcm.runPreparedSQL(sqlProf, "true",primaryKey );
            
            dbcm.closeConnection();
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
        }
    }
    
    public Integer maxId() {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try {
            String sql = "SELECT MAX(id) FROM professor";
            ResultSet rs = dbcm.runQuerySQL(sql);

            int ultimoID = 1;

            if (rs.next()) {
             // Obtém o último ID da consulta
             ultimoID = rs.getInt(1);
            }

            dbcm.closeConnection();
            
            // Calcula o próximo ID
            return ultimoID + 1;
            
        } catch (SQLException e) {
            notifications.tabelaNaoExiste();
        } catch (DataBaseException ex) {
            notifications.erroSintaxe();
        }
        return null;
         
    }
    
    public void updateDisciplinas (Professor objeto) {
        DataBaseConnectionManager dbcm = Sys.getInstance().getDB();
        
        try
        {
            String sql = "DELETE FROM ensina WHERE id = ?";
            dbcm.runPreparedSQL(sql, objeto.getIdProfessor());
            
            dbcm.closeConnection();
            
            cadastrarDisciplinas(objeto);
            
        } 
        catch (DataBaseException ex)
        {
            notifications.tabelaNaoExiste();
        }
        
    }
    
}
