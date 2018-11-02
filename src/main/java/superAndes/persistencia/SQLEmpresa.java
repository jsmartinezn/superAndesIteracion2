package superAndes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

class SQLEmpresa {
	
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
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

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLEmpresa (PersistenciaSuperAndes pp)
	{
		this.pp = pp;
	}
	
	public long adicionar(PersistenceManager pm, String nombre, String correo, Long nit,String direccion) {
		Query q = pm.newQuery(SQL,"INSERT INTO " + pp.darTablaCliente() + "(identificacion, nombre, correo_electronico, direccion) values (?, ?, ?, ?)");
		q.setParameters(nit,nombre,correo,direccion);
		return (long) q.executeUnique();
	}

}
