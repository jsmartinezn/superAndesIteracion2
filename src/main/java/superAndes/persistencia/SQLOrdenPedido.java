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
	
	public long adicionarOrden(PersistenceManager pm,Long id,Long idProveedor,Long idSucursal,Long idProducto,Date fechaEsperada,Integer cantidad,Double precio,String estado)
	{
		Query q = pm.newQuery(SQL,"INSERT INTO " + pp.darTablaOrdenPedido() +  "(id, id_Proveedor, id_Sucursal,id_Producto, fecha_Esperada, cantidad,precio,estado) values (?, ?, ?, ?, ?, ?,?,?)");
		q.setParameters(id,idProveedor,idSucursal,idProducto,fechaEsperada,cantidad,precio, estado);
		return (long)q.executeUnique();
	}
	
	public List<Object[]> darOrden(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL,"SELECT * FROM " + pp.darTablaOrdenPedido() );
		return q.executeList();
	}
	
	
	public Object[] darTipoOrden(PersistenceManager pm, Long id)
	{
		Query q = pm.newQuery(SQL,"SELECT TIPO_PRODUCTO, VOLUMEN, PESO,ID_SUCURSAL,ID_PRODUCTO FROM " + pp.darTablaOrdenPedido() + "," + pp.darTablaProducto() + " WHERE ORDEN_PEDIDO.ID = ? AND PRODUCTO.CODIGO_BARRAS = ID_PRODUCTO");
		q.setParameters(id);
		q.setResultClass(Object[].class);
		return (Object[]) q.executeUnique();
	}
	public long entregarOrden(PersistenceManager pm,Long id,Date fecha,Double calificacion,String estado,Integer cantidadR)
	{
		Query q = pm.newQuery(SQL,"UPDATE " + pp.darTablaOrdenPedido() + " SET fecha_entrega= ?, calificacion = ?, estado = ?,cantidad_recibida = ? WHERE id = ?");
		q.setParameters(fecha,calificacion,estado,cantidadR,id);
		return (long) q.executeUnique();
	}
}
