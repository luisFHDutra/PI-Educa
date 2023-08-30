package db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataBaseConnectionManager
{

    public final static int MYSQL = 0;
    public final static int POSTGRESQL = 1;
    public final static int SQLITE = 2;

    private Connection connection;
    private ResultSet rs;

    private String url;
    private String dataBaseName;
    private String user;
    private String password;

    private String scriptPath;

    public DataBaseConnectionManager(int dbms, String dataBaseName, String user, String pw) throws DataBaseException
    {
        this(dbms, dataBaseName, user, pw, null, null);
    }

    public DataBaseConnectionManager(int dbms, String dataBaseName, String user, String pw, String ip) throws DataBaseException
    {
        this(dbms, dataBaseName, user, pw, ip, null);
    }

    public DataBaseConnectionManager(int dbms, String dataBaseName, String user, String pw, String ip, String port) throws DataBaseException
    {
        if (dbms < 0 || dbms > 2) // dbms desconhecido
        {
            throw new DataBaseException("O número do SGBD está errado: [0] MySQL; [1] Postgresql; [3] SQLite ");
        }

        ip = (ip == null ? "localhost" : ip);

        String[] ports =
        {
            "3306", "5432", ""
        };
        port = (port == null ? ports[dbms] : port);

        String[] jdbc =
        {
            "jdbc:mysql://" + ip + ":" + port + "/" + dataBaseName,
            "jdbc:postgresql://" + ip + ":" + port + "/" + dataBaseName,
            "jdbc:sqlite:" + dataBaseName
        };

        if (dataBaseName == null)
        {
            throw new DataBaseException("Falta definir o nome da base de dados");
        }

        if (user == null && pw == null)
        {
            throw new DataBaseException("Falta definir usuário e senha da base de dados");
        }

        this.url = jdbc[dbms];
        this.dataBaseName = dataBaseName;
        this.user = user;
        this.password = pw;
    }

    public void connectDataBase() throws DataBaseException
    {
        this.connection = null;
        try
        {
            if (user == null || user.isEmpty())
            {
                Connection c = DriverManager.getConnection(url);
                this.connection = c;
            } else
            {
                Connection c = DriverManager.getConnection(url, this.user, this.password);
                this.connection = c;
            }
        } catch (SQLException ex)
        {
            throw new DataBaseException("A conexão com o banco de dados falhou");
        }
    }

    /**
     * *
     * Abre e fecha uma conexão com o banco de dados. Realiza um teste conexão
     * com o banco.
     *
     * @throws DataBaseException
     */
    public void connectionTest() throws DataBaseException
    {
        try
        {
            connectDataBase();
            this.connection.close();
            this.connection = null;
        } catch (DataBaseException ex)
        {
            throw new DataBaseException("O teste de conexão com o banco de dados falhou");
        } catch (SQLException ex)
        {
            throw new DataBaseException("O teste de conexão com o banco de dados falhou");
        }
    }

    public void executeUpdate(String sql) throws SQLException {
        try {
            
            this.connection.createStatement().executeUpdate(sql);
        } finally {
            // Fecha a conexão para liberar os recursos
            if (connection != null) {
                connection.close();
            }
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        try {
            
            return this.connection.prepareStatement(sql);
        } finally {
            // Não fechamos a conexão aqui, pois o PreparedStatement será responsável por fechá-la
        }
    }
    
    public void runSQL(String sql) throws DataBaseException
    {
        try
        {
            if (this.connection == null || this.connection.isClosed())
            {
                this.connectDataBase();
            }
            executeSQL(sql);
        } catch (SQLException ex)
        {
            throw new DataBaseException("Erro na execução de uma instrução SQL");
        }
    }

    public void runPreparedSQL(String sql, Object... args) throws DataBaseException
    {
        try
        {
            if (this.connection == null || this.connection.isClosed())
            {
                this.connectDataBase();
            }
            executePreparedSQL(sql, args);
        } catch (SQLException ex)
        {
            throw new DataBaseException("Erro na execução de uma instrução SQL");
        }
    }
    public ResultSet runQuerySQL(String sql) throws DataBaseException
    {
        try
        {
            if (this.connection == null || this.connection.isClosed())
            {
                this.connectDataBase();
            }
            this.rs = executeQuerySQL(sql);
        } catch (SQLException ex)
        {
            throw new DataBaseException("Erro em runQuerySQL");
        }
        return rs;
    }
    
    public ResultSet runPreparedQuerySQL(String sql, Object... args) throws DataBaseException
    {
        try
        {
            if (this.connection == null || this.connection.isClosed())
            {
                this.connectDataBase();
            }
            this.rs = executePreparedQuerySQL(sql, args);
        } catch (SQLException ex)
        {
            throw new DataBaseException("Erro na execução de uma instrução SQL");
        }
        return rs;
    }

    public Connection getConnection() throws DataBaseException
    {
        try
        {
            if (this.connection == null || this.connection.isClosed())
            {
                this.connectDataBase();
            }
        } catch (SQLException ex)
        {
            throw new DataBaseException(ex.getMessage());
        } catch (DataBaseException ex)
        {
            throw new DataBaseException(ex.getMessage());
        }
        return connection;
    }

    public ResultSet getResultSet()
    {
        return rs;
    }

    public void closeConnection() throws DataBaseException
    {
        try
        {
            connection.close();
        } catch (NullPointerException ex) // se a variável connection estiver null
        {
            throw new DataBaseException("Ocorreu um erro ao fechar a conexão com o banco");
        } catch (SQLException ex)
        {
            throw new DataBaseException("Ocorreu um erro ao fechar a conexão com o banco");
        }
    }

    
    private ResultSet executeQuerySQL(String sql) throws DataBaseException
    {
        Statement stm;
        try
        {
            stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            this.rs = rs;
        } catch (NullPointerException ex) // se a variável connection estiver null
        {
            throw new DataBaseException("Falha na execução da consulta SQL (Variável não inicializada)");
        } catch (SQLException ex)
        {
            throw new DataBaseException("Falha na execução da consulta SQL (Revisar a instrução SQL)");
        }
        return rs;
    }
    
    private ResultSet executePreparedQuerySQL(String sql, Object... args) throws DataBaseException
    {
        Statement stm;
        try
        {
            stm = connection.createStatement();
            
            String subst = "";
            for (Object object : args)
            {
                if (object instanceof Date)
                {
                    Date aux = (Date) object;
                    DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
                    subst = "'" + df.format(aux) + "'";
                }
                if (object instanceof Double)
                {
                    double aux = (Double) object;
                    subst = aux + "";
                }
                if (object instanceof Integer)
                {
                    int aux = (Integer) object;
                    subst = aux + "";
                }
                if (object instanceof String)
                {
                    subst = "'" + ((String) object) + "'";
                }
                sql = sql.replaceFirst("\\?", subst);
            }
            
            ResultSet rs = stm.executeQuery(sql);
            this.rs = rs;
        } catch (NullPointerException ex) // se a variável connection estiver null
        {
            throw new DataBaseException("Falha na execução da consulta SQL (Variável não inicializada)");
        } catch (SQLException ex)
        {
            throw new DataBaseException("Falha na execução da consulta SQL (Revisar a instrução SQL)");
        }
        return rs;
    }
    
    private void executePreparedSQL(String sql, Object... args) throws DataBaseException
    {
        Statement stm;
        try
        {
            stm = connection.createStatement();
            String subst = "";
            for (Object object : args)
            {
                if (object instanceof Date)
                {
                    Date aux = (Date) object;
                    DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
                    subst = "'" + df.format(aux) + "'";
                }
                if (object instanceof Double)
                {
                    double aux = (Double) object;
                    subst = aux + "";
                }
                if (object instanceof Integer)
                {
                    int aux = (Integer) object;
                    subst = aux + "";
                }
                if (object instanceof String)
                {
                    subst = "'" + ((String) object) + "'";
                }
                sql = sql.replaceFirst("\\?", subst);
            }
            
            stm.executeUpdate(sql);
        } catch (NullPointerException ex) // se a variável connection estiver null
        {
            throw new DataBaseException("Falha na execução da instrução SQL (NullPointer)");
        } catch (SQLException ex)
        {
            throw new DataBaseException("Falha na execução da instrução SQL");
        }
    }

    private void executeSQL(String sql) throws DataBaseException
    {
        Statement stm;
        try
        {
            stm = connection.createStatement();
            stm.executeUpdate(sql);
        } catch (NullPointerException ex) // se a variável connection estiver null
        {
            throw new DataBaseException("Falha na execução da instrução SQL (NullPointer)");
        } catch (SQLException ex)
        {
            throw new DataBaseException("Falha na execução da instrução SQL");
        }
    }

    public void setScriptPath(String scriptPath)
    {
        this.scriptPath = scriptPath;
    }

    public void runScritpSQL() throws DataBaseException
    {
        if (!this.scriptPath.isEmpty())
        {
            InputStream is = getClass().getResourceAsStream(this.scriptPath);
            this.runScritpSQL(is);
        } else
        {
            throw new DataBaseException("Caminho do script SQL não foi definido");
        }
    }

    public void runScritpSQL(InputStream is) throws DataBaseException
    {
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            String sql = "";
            String linha = bufferedReader.readLine();
            while (linha != null)
            {
                // pular comentários
                if ((linha.length() > 0 && (linha.charAt(0) == '#' || linha.charAt(0) == '-')) || linha.length() == 0)
                {
                    linha = bufferedReader.readLine();
                    continue;
                }
                sql += " " + linha.trim();

                //comando está completo (encontrou o ;)
                if (sql.charAt(sql.length() - 1) == ';')
                {
                    try
                    {
                        this.executeSQL(sql.trim());
                        sql = "";
                    } catch (DataBaseException ex)
                    {
                        bufferedReader.close();
                        throw new DataBaseException(ex.getMessage() + ": " + sql.trim());
                    }

                }
                linha = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException ex)
        {
            throw new DataBaseException("Falha na execução do script SQL");
        }
    }

}
