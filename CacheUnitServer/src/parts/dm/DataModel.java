package parts.dm;

public class DataModel<T>
        extends java.lang.Object
        implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -616017516154926601L;
    private T content;
    private Long dataModelId;

    public DataModel(Long id, T content) {

        this.setContent(content);
        this.setDataModelId(id);
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public Long getDataModelId() {
        return dataModelId;
    }

    public void setDataModelId(Long id) {
        this.dataModelId = id;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof DataModel<?>) {
            if ( ((DataModel<?>)obj).content.equals(this.content) )
                return true;
        }
        return false;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataModelId == null) ? 0 : dataModelId.hashCode());
        return result;
    }

    public String toString() {
        return "Id is:" + dataModelId + " content is:" + content.toString() + "\n";

    }
}
