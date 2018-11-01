package superAndes.persistencia;

import java.math.BigDecimal;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import superAndes.negocio.CarritoCompras;
public class SQLCarritoCompras {
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
	public SQLCarritoCompras(PersistenciaSuperAndes pp)
	{
		this.pp = pp;
	}
	
	public CarritoCompras asignarCarrito(PersistenceManager pm,Long idCliente,Long idSucursal){
		Query q = pm.newQuery(SQL,"SELECT * FROM " + pp.darTablaCarrito() + " WHERE ID_CLIENTE IS NULL AND ID_SUCURSAL = ?");
		q.setParameters(idSucursal);
		List<Object[]> temp = q.executeList();

		if(temp.isEmpty())
			return null;
		BigDecimal resp = (BigDecimal) temp.get(0)[0];
		Query p = pm.newQuery(SQL,"UPDATE " + pp.darTablaCarrito() + " SET ID_CLIENTE = ? WHERE ID = ?");
		p.setParameters(idCliente,resp.longValue());
		p.executeUnique();
		return new CarritoCompras(idCliente,resp.longValue());
	}
	public CarritoCompras quitarCarrito(PersistenceManager pm,Long idCliente,Long idSucursal){
		Query p = pm.newQuery(SQL,"UPDATE " + pp.darTablaCarrito() + " SET ID_CLIENTE = NULL WHERE ID_CLIENTE = ? AND ID_SUCURSAL = ?");
		p.setParameters(idCliente,idSucursal);
		p.executeUnique();
		return new CarritoCompras(null,idCliente);
	}
	public Object darSucursal(PersistenceManager pm,Long id){
		Query p = pm.newQuery(SQL,"SELECT ID_SUCURSAL FROM " + pp.darTablaCarrito() + " WHERE ID = ?");
		p.setParameters(id);
		return p.executeUnique();
	}
	public Object[] darCarrito(PersistenceManager pm,Long id){
		Query p = pm.newQuery(SQL,"SELECT ID_SUCURSAL,ID_CLIENTE FROM " + pp.darTablaCarrito() + " WHERE ID = ?");
		p.setParameters(id);
		return (Object[]) p.executeUnique();
	}
}
