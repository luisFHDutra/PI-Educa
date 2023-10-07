
package persistencia;

import negocio.Aluno;
import negocio.DiaSemana;
import negocio.Disciplina;
import negocio.Permissao;
import negocio.Professor;
import negocio.Turma;
import negocio.Usuario;

public class DaoFactory {
    
    public static IDao<Aluno,Integer> criarAlunoDao()
    {
        return new AlunoDao();
    }
    
    public static IDao<DiaSemana,Integer> criarDiaSemanaDao()
    {
        return new DiaSemanaDao();
    }
    
    public static IDao<Turma,Integer> criarTurmaDao()
    {
        return new TurmaDao();
    }
    
    public static IDao<Usuario,Integer> criarUsuarioDao()
    {
        return new UsuarioDao();
    }
    
    public static IDao<Professor,Integer> criarProfessorDao()
    {
        return new ProfessorDao();
    }
    
    public static IDao<Disciplina,Integer> criarDisciplinaDao()
    {
        return new DisciplinaDao();
    }
    
    public static IDao<Permissao,Integer> criarPermissaoDao()
    {
        return new PermissaoDao();
    }
}
