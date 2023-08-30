
package persistencia;

public class InvalidKeyException extends Exception
{

    public InvalidKeyException()
    {
        super("Valor incorreto para chave primária");
    }
    
}
