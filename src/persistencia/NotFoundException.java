package persistencia;

public class NotFoundException extends Exception
{

    public NotFoundException()
    {
        super("Registro não encontrado");
    }
    
}
