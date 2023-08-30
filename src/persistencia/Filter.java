package persistencia;

public interface Filter<T>
{
    public boolean isAccept(T record);
}
