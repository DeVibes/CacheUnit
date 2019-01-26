package parts.server;

import parts.dm.DataModel;

import java.util.Map;

public class Request<T>
        extends java.lang.Object
        implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1733621589332132232L;
    private T body;
    private Map<String,String> headers;

    public Request(Map<String,String> headers, T body) {
        this.body = body;
        this.headers = headers;
    }

    public T getBody() {return this.body;}

    public Map<String,String> getHeaders() {return this.headers;}

    public void	setBody(T body) {this.body=body;}

    public void	setHeaders(Map<String,String> headers) {this.headers=headers;}

    public String toString() {

        StringBuilder sb = new StringBuilder();
        DataModel<T>[] dms = (DataModel<T>[]) body;
        sb.append("Headers: ");
        sb.append(headers.get("action") + "\n");
        sb.append("Body:\n");
        for (DataModel<T> dm : dms)
            sb.append(dm.toString());

        return sb.toString();
    }
}
