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
		a += " WHERE BODEGA_PRODUCTO.ID_PRODUCTO=PRODUCTO.ID AND BODEGA.ID=BODEGA_PRODUCTO.ID_BODEGA AND BODEGA.ID_SUCURSAL = ? AND PRODUCTO.ID = ?";
		Query q = pm.newQuery(SQL, a);
		q.setParameters(idSucursal,idProducto);
		q.setResultClass(Object[].class);
		return q.executeList();
	}
		
	public List<Bodega> darBodega(PersistenceManager pm,Long idSucursal)
	{
		Query q = pm.newQuery(SQL, "SELECT FROM" + pp.darTablaBodega() + "WHERE idsucursal = ?");
		q.setResultClass(Bodega.class);
		q.setParameters(idSucursal);
		return (List<Bodega>) q.executeList();
	}
	
	public Bodega darBodega(PersistenceManager pm, Long idSucursal,Long idCliente){
		Query q = pm.newQuery(SQL,"SELECT cantidad,nivel FROM "+ pp.darTablaBodega() + " WHERE tipoproducto = (SELECT tipoproducto FROM " + pp.darTablaProducto() + " WHERE id = ?)");
		q.setParameters(idCliente,idSucursal);
		q.setResultClass(Bodega.class);
		return (Bodega) q.executeUnique();
	}

	public long actualizarBodega(PersistenceManager pm,Integer cant, Long idP, Long idS) {
		Query q = pm.newQuery(SQL,"SELECT volumen,peso FROM "+ pp.darTablaProducto() + " WHERE id = ?");
		q.setParameters(idP);
		q.setResultClass(Producto.class);
		Producto nuevo = (Producto) q.executeUnique();
		Double volumen = nuevo.getVolumen();
		Double peso = nuevo.getPeso();	
		Bodega temp = darBodega(pm, idS, idP);
		Query p = pm.newQuery(SQL, "UPDATE " + pp.darTablaBodega() + " SET volumenactual = ?, pesoactual = ?, cantidad = ? WHERE tipoproducto = (SELECT tipoproducto FROM " + pp.darTablaProducto() + " WHERE id = ?) AND idSucursal = ? ");
		p.setParameters((0-cant)*volumen,(0-cant)*peso,cant,idP,idS);
		return (long)p.executeUnique();
		
	}


	
}
