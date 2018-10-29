package superAndes.persistencia;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import superAndes.negocio.Compra;

class SQLCompra {
	
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
	public SQLCompra (PersistenciaSuperAndes pp)
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
	public long adicionarCompra (PersistenceManager pm,Long id, Long idC, Long idP, Long idS, Integer cantidad, Boolean promocion,Double precio,Date fecha) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCompra () + "(id, idcliente, idproducto, idsucursal, cantidad, promocion, precio, fecha) values (?, ?, ?, ?, ?, ?)");
        q.setParameters(id,idC,idP,idS,cantidad,promocion,precio,fecha);
        return (long) q.executeUnique();
	}

	public List<Compra> darDineroTiempo(PersistenceManager pm,Date fechaI,Date fechaF)
	{
		Query q = pm.newQuery(SQL, "SELECT idSucursal, cantidad, precio FROM" + pp.darTablaCompra() + "WHERE fecha > ? AND fecha < ? ");
		q.setResultClass(Compra.class);
		q.setParameters(fechaI,fechaF);
		return  q.executeList();
	}
	
	public List<Compra> darDineroPorCliente(PersistenceManager pm,Date fechaI,Date fechaF,Long idCliente)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM" + pp.darTablaCompra() + "WHERE fecha > ? AND fecha < ? AND idCliente = ?");
		q.setResultClass(Compra.class);
		q.setParameters(fechaI,fechaF,idCliente);
		return  q.executeList();
	}
}
