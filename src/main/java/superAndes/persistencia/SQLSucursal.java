package superAndes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLSucursal {

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
	
	public SQLSucursal(PersistenciaSuperAndes pp){
		this.pp = pp;
	}
	
	public long adicionarSucursal(PersistenceManager pm, Long id, String ciudad, String direccion){
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaSucursal () + "(id, ciudad, direccion) values (?, ?, ?)");
        q.setParameters(id, ciudad, direccion);
        return (long) q.executeUnique();
	}

	public List<Object> getIdSucursal(PersistenceManager pm, String direccion, String ciudad) {
		Query q = pm.newQuery(SQL, "SELECT ID FROM " + pp.darTablaSucursal() + " WHERE DIRECCION = ? AND CIUDAD = ?");
		q.setParameters(direccion,ciudad);
		return q.executeList();
	}

	public List<Object[]> darSucursales(PersistenceManager pm) {
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaSucursal());
		q.setParameters();
		return q.executeList();
	}
	
	public List<Object[]> darClientes(PersistenceManager pm) {
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaSucursal());
		return q.executeList();
	}
	
}
