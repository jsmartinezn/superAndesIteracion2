package superAndes.persistencia;


import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import superAndes.negocio.Bodega;
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
	public long adicionarBodega (PersistenceManager pm,Long id,String tipoProducto,Double volumen,Double volumen2,Double peso,Double peso2,Integer cantidad) 
	{
		System.out.println(id);
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaBodega () + "(id_Sucursal, tipo_Producto, volumen, volumen_actual, peso, peso_actual,cantidad) values (?, ?, ?, ?, ?, ?, ?)");
        q.setParameters(id,tipoProducto,volumen,volumen2,peso, peso2,cantidad);
        return (long) q.executeUnique();
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
		p.setParameters((temp.getCantidad()-cant)*volumen,(temp.getCantidad()-cant)*peso,cant,idP,idS);
		return (long)p.executeUnique();
		
	}


	
}
