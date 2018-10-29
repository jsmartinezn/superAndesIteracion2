package superAndes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLProveedor {

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
	
	public SQLProveedor(PersistenciaSuperAndes pp){
		this.pp = pp;
	}
	
	public long adicionarProvedor(PersistenceManager pm, Long nit, String nombre)
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaProvedor () + "(nit, nombre) values (?, ?)");
        q.setParameters(nit, nombre);
        return (long) q.executeUnique();
	}
	
}
