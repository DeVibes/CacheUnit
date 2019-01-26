package parts.dao;

public interface IDao <ID extends java.io.Serializable,T> {

    public void delete(T entity);

    public T find (ID id);

    public void save(T entity);
}
