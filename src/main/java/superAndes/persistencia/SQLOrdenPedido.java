package superAndes.persistencia;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import superAndes.negocio.OrdenPedido;

class SQLOrdenPedido {

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
	public SQLOrdenPedido (PersistenciaSuperAndes pp)
	{
		this.pp = pp;
	}
	
	public long adicionarOrden(PersistenceManager pm,Long id,Long idProveedor,Long idSucursal,Date fechaEsperada, String estado)
	{
		Query q = pm.newQuery("INSERT INTO " + pp.darTablaOrdenPedido() +  "(id, idProveedor, idSucursal, fechaEsperada, estado) values (?, ?, ?, ?, ?, ?)");
		q.setParameters(id,idProveedor,idSucursal,fechaEsperada, estado);
		return (long)q.executeUnique();
	}
	
	public List<OrdenPedido> darOrden(PersistenceManager pm)
	{
		Query q = pm.newQuery("SELECT * FROM " + pp.darTablaOrdenPedido() );
		q.setResultClass(OrdenPedido.class);
		return q.executeList();
	}
	
	public long entregarOrden(PersistenceManager pm,Long id,Date fecha,Double calificacion,String estado)
	{
		Query q = pm.newQuery("UPDATE " + pp.darTablaOrdenPedido() + " SET (fechaentrega, calificacion, estado) values (?, ?, ?) WHERE id = ?");
		q.setParameters(fecha,calificacion,estado,id);
		return (long) q.executeUnique();
	}
}
