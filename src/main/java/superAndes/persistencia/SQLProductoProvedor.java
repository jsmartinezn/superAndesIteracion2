package superAndes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLProductoProvedor {

	
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaSuperAndes.SQL;
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaSuperAndes pp;
	
	public SQLProductoProvedor(PersistenciaSuperAndes pp){
		this.pp = pp;
	}
	
	public long adicionarProductoProvedor(PersistenceManager pm, Long idProducto, Long idProvedor, Double precio, Double indiceCalidad){
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaProductoProvedor () + "(id_producto, id_proveedor, precio, indice_calidad) values (?, ?, ?, ?)");
        q.setParameters(idProducto, idProvedor, precio, indiceCalidad);
        return (long) q.executeUnique();
	}
	
	public List<Object[]> darProductoProvedor(PersistenceManager pm, Long idProducto){
        Query q = pm.newQuery(SQL, "SELECT ID_PROVEEDOR, PRECIO FROM " + pp.darTablaProductoProvedor () + " WHERE ID_PRODUCTO = ?");
        q.setParameters(idProducto);
        return q.executeList();
	}
}
