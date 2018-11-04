package superAndes.persistencia;


import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import superAndes.negocio.Bodega;
import superAndes.negocio.Indice;
import superAndes.negocio.Producto;


class SQLBodega {
	
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
	public SQLBodega (PersistenciaSuperAndes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una BODEGA a la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param idSucursal - El identificador de la sucursal
	 * @param tipoProducto - El identificador del bar
	 * @param fecha - La fecha en que se realizó la visita
	 * @param horario - EL horario en que se realizó la visita (DIURNO, NOCTURNO, TODOS)
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarBodega (PersistenceManager pm,Long id,Long idS,String tipoProducto,Double volumen,Double peso) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaBodega () + "(id,id_Sucursal, tipo_Producto, volumen, peso) values (?, ?, ?, ?, ?)");
        q.setParameters(id,idS,tipoProducto,volumen,peso);
        return (long) q.executeUnique();
	}

	public List<Indice> darIndiceDeOcupacionPeso (PersistenceManager pm, Long idSucursal )
	{
		String a =  "SELECT bodega.id as id, pesos/bodega.peso as indice FROM " ;
		a+= pp.darTablaBodega(); 
		a+= ",( SELECT bodega_producto.id_bodega as IDD, SUM(cantidad*peso) as pesos FROM ";
		a+= pp.darTablaBodegaProducto() + "," + "PRODUCTO ";
		a+= " WHERE producto.id = bodega_producto.id_producto GROUP BY bodega_producto.id_bodega)WHERE IDD=ID AND BODEGA.ID_SUCURSAL = ?";
		Query q = pm.newQuery(SQL, a);
		q.setParameters(idSucursal);
		q.setResultClass(Indice.class);
		return q.executeList();
	}
	
	public List<Indice> darIndiceDeOcupacionVolumen (PersistenceManager pm , Long idSucursal )
	{
		String a =  "SELECT bodega.id as id, pesos/bodega.volumen as indice FROM " ;
		a+= pp.darTablaBodega(); 
		a+= ",( SELECT bodega_producto.id_bodega as IDD, SUM(cantidad*volumen) as pesos FROM ";
		a+= pp.darTablaBodegaProducto() + "," + "PRODUCTO ";
		a+= " WHERE producto.id = bodega_producto.id_producto GROUP BY bodega_producto.id_bodega)WHERE IDD=ID AND BODEGA.ID_SUCURSAL = ?";
		Query q = pm.newQuery(SQL, a);
		q.setParameters(idSucursal);
		q.setResultClass(Indice.class);
		return q.executeList();
	}
	public List<Object[]> reOrden(PersistenceManager pm, Long idSucursal, Long idProducto)
	{
		String a = "SELECT BODEGA_PRODUCTO.ID_BODEGA, BODEGA_PRODUCTO.CANTIDAD,PRODUCTO.PESO,PRODUCTO.VOLUMEN,BODEGA.PESO,BODEGA.VOLUMEN FROM ";
		a += pp.darTablaBodegaProducto() + "," + pp.darTablaBodega() + "," + pp.darTablaProducto();
		a += " WHERE BODEGA_PRODUCTO.ID_PRODUCTO=PRODUCTO.CODIGO_BARRAS AND BODEGA.ID=BODEGA_PRODUCTO.ID_BODEGA AND BODEGA.ID_SUCURSAL = ? AND PRODUCTO.CODIGO_BARRAS = ?";
		Query q = pm.newQuery(SQL, a);
		q.setParameters(idSucursal,idProducto);
		q.setResultClass(Object[].class);
		return q.executeList();
	}
		
	public List<Object[]> darBodegas(PersistenceManager pm,Long idSucursal)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaBodega() + " WHERE id_sucursal = ?");
		q.setParameters(idSucursal);
		return q.executeList();
	}
	
	public Bodega darBodega(PersistenceManager pm, Long idSucursal,Long idCliente){
		Query q = pm.newQuery(SQL,"SELECT cantidad,nivel FROM "+ pp.darTablaBodega() + " WHERE tipoproducto = (SELECT tipoproducto FROM " + pp.darTablaProducto() + " WHERE id = ?)");
		q.setParameters(idCliente,idSucursal);
		q.setResultClass(Bodega.class);
		return (Bodega) q.executeUnique();
	}
	
	public List<Object[]> darDisponible(PersistenceManager pm, String tipoProducto,Long idSucursal,Long idProducto){
		String a = "SELECT BODEGA.ID, BODEGA.PESO - PESOS, BODEGA.VOLUMEN - VOLUM, BODEGA_PRODUCTO.CANTIDAD  FROM ";
		a += pp.darTablaBodega() + "," + pp.darTablaBodegaProducto();
		a += ",( SELECT BODEGA_PRODUCTO.ID_BODEGA AS IDD,SUM(CANTIDAD*PESO) AS PESOS,SUM(CANTIDAD*VOLUMEN) AS VOLUM FROM ";
		a += pp.darTablaBodegaProducto() + "," + pp.darTablaProducto();
		a += " WHERE PRODUCTO.CODIGO_BARRAS = BODEGA_PRODUCTO.ID_PRODUCTO AND PRODUCTO.TIPO_PRODUCTO = ? GROUP BY BODEGA_PRODUCTO.ID_BODEGA) WHERE IDD=ID AND ID_SUCURSAL = ? AND ID = ID_BODEGA AND ID_PRODUCTO = ?";
		Query q = pm.newQuery(SQL, a);
		q.setParameters(tipoProducto,idSucursal,idProducto);
		return q.executeList();
	}

	public void borrarBodega(PersistenceManager pm) {
		Query q = pm.newQuery(SQL,"DELETE FROM " + pp.darTablaBodega());
		q.executeUnique();
		
	}
	
	public void borrarTodo(PersistenceManager pm){
		Query q1 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaBodegaProducto());
		q1.executeUnique();
		Query q2 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaEstanteProducto());
		q2.executeUnique();
		Query q3 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaBodega());
		q3.executeUnique();
		Query q12 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaEstante());
		q12.executeUnique();
		Query q4 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaSucursalProducto());
		q4.executeUnique();
		Query q5 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaProductoProvedor());
		q5.executeUnique();
		Query q6 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCompraProducto());
		q6.executeUnique();		
		Query q7 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaOrdenPedido());
		q7.executeUnique();		
		Query q8 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaSucursal());
		q8.executeUnique();
		Query q9 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCarritoProducto());
		q9.executeUnique();
		Query q10 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaEstanteProducto());
		q10.executeUnique();
		Query q14 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCarrito());
		q14.executeUnique();
		Query q13 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCliente());
		q13.executeUnique();
		Query q11 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaProducto());
		q11.executeUnique();
		Query q15 = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaProvedor());
		q15.executeUnique();
	}
	
	
}
