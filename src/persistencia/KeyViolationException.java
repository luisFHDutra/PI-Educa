
package persistencia;


public class KeyViolationException extends Exception
{

    public KeyViolationException() {
        super("Chave primária duplicada");
    }
    
}
