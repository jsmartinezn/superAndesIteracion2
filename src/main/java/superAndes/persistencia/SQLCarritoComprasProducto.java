package superAndes.persistencia;

import java.math.BigDecimal;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLCarritoComprasProducto {
	
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
	public SQLCarritoComprasProducto(PersistenciaSuperAndes pp)
	{
		this.pp = pp;
	}
	
	public long adicionarProducto(PersistenceManager pm,Long idProducto,Long idCarrito,Integer cantidad)
	{
		Query p = pm.newQuery(SQL,"SELECT * FROM " + pp.darTablaCarritoProducto() + " WHERE ID_PRODUCTO = ? AND ID_CARRITO = ?");
		p.setParameters(idProducto,idCarrito);
		List<Object[]> lista= p.executeList();
		if(lista.isEmpty()){
			Query q = pm.newQuery(SQL,"INSERT INTO " + pp.darTablaCarritoProducto() + " (id_Producto, id_Carrito,cantidad) VALUES (?, ?, ?)");
			q.setParameters(idProducto,idCarrito,cantidad);
			return (long)q.executeUnique();
		}
		else
		{
			BigDecimal temp = (BigDecimal) lista.get(0)[2];
			Query q = pm.newQuery(SQL,"UPDATE " + pp.darTablaCarritoProducto() + " SET CANTIDAD = ? WHERE ID_PRODUCTO = ? AND ID_CARRITO = ?");
			q.setParameters(temp.longValue()+cantidad,idProducto,idCarrito);
			return (long)q.executeUnique();
		}		
	}
	public void quitarProducto(PersistenceManager pm,Long idProducto,Long idCarrito)
	{
		Query q = pm.newQuery(SQL,"DELETE FROM " + pp.darTablaCarritoProducto() + " WHERE ID_CARRITO = ? AND ID_PRODUCTO = ?");
		q.setParameters(idProducto,idCarrito);
		q.executeUnique();
	}
	public Integer darCantidad(PersistenceManager pm,Long idProducto,Long idCarrito)
	{
		Query p = pm.newQuery(SQL,"SELECT cantidad FROM " + pp.darTablaCarritoProducto() + " WHERE ID_PRODUCTO = ? AND ID_CARRITO = ?");
		p.setParameters(idProducto,idCarrito);
		BigDecimal temp = (BigDecimal) p.executeUnique();
		return temp.intValue();
	}
	
	public List<Object[]> pagar(PersistenceManager pm,Long idCarrito){
		Query p = pm.newQuery(SQL,"SELECT ID_PRODUCTO,CANTIDAD FROM " + pp.darTablaCarritoProducto() + " WHERE ID_CARRITO = ?");
		p.setParameters(idCarrito);
		return p.executeList();
	}
}
