package superAndes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLSucursalProducto {

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
	
	public SQLSucursalProducto(PersistenciaSuperAndes pp){
		this.pp = pp;
	}
	
	public long adicionarSucursalProducto(PersistenceManager pm, Long idSucursal, Long idProducto, Double precio, Integer nivelReorden){
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaSucursalProducto () + "(id_sucursal, id_producto, precio, nivel) values ( ?, ?, ?, ?)");
        q.setParameters(idSucursal, idProducto, precio, nivelReorden);
        return (long) q.executeUnique();
	}
	
	public Object getPrecio(PersistenceManager pm,Long idSucursal,Long idproducto){
		Query q = pm.newQuery(SQL,"SELECT PRECIO FROM " + pp.darTablaSucursalProducto() + " WHERE ID_SUCURSAL = ? AND ID_PRODUCTO = ?");
		q.setParameters(idSucursal,idproducto);
		return q.executeUnique();
	}
}
