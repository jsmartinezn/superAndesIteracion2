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
	
	public CarritoCompras asignarCarrito(PersistenceManager pm,Long idCarrito,Long idCliente,Long idSucursal){
		Query p = pm.newQuery(SQL,"INSERT INTO " + pp.darTablaCarrito() + " (id,id_cliente,id_sucursal) VALUES (?, ?, ?)");
		p.setParameters(idCarrito,idCliente,idSucursal);
		p.executeUnique();
		return new CarritoCompras(idCarrito,idCliente,idSucursal);
	}
	public CarritoCompras quitarCarrito(PersistenceManager pm,Long idCliente,Long idSucursal){
		Query p = pm.newQuery(SQL,"UPDATE " + pp.darTablaCarrito() + " SET ID_CLIENTE = NULL WHERE ID_CLIENTE = ? AND ID_SUCURSAL = ?");
		p.setParameters(idCliente,idSucursal);
		p.executeUnique();
		return new CarritoCompras(null,idCliente,idSucursal);
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
	
	public List<Object[]> darCarritosAbandonados(PersistenceManager pm){
		String a = "SELECT ID, ID_PRODUCTO FROM ";
		a += pp.darTablaCarritoProducto() + "," + pp.darTablaCarrito();
		a += " WHERE CARRITO_COMPRA.ID = CARRITO_COMPRA_PRODUCTO.ID_CARRITO AND CARRITO_COMPRA.ID_CLIENTE IS NULL";
		Query p = pm.newQuery(SQL, a);
		return p.executeList();
	}
	
	public void borrarCarrito(PersistenceManager pm,Long idCarrito){
		Query q = pm.newQuery(SQL,"DELETE FROM " + pp.darTablaCarrito() + " WHERE ID = ?");
		q.setParameters(idCarrito);
		q.executeUnique();
	}
	
	public List<Object> limpiarCarritosVacios(PersistenceManager pm){
		Query q = pm.newQuery(SQL,"SELECT ID FROM " + pp.darTablaCarrito() + " WHERE ID_CLIENTE IS NULL");
		return q.executeList();
	}
}
