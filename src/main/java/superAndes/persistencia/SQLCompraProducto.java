package superAndes.persistencia;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLCompraProducto {

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra ac� para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaSuperAndes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicaci�n
	 */
	private PersistenciaSuperAndes pp;

	/* ****************************************************************
	 * 			M�todos
	 *****************************************************************/
	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicaci�n
	 */
	public SQLCompraProducto (PersistenciaSuperAndes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una BODEGA a la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param idSucursal - El identificador de la sucursal
	 * @param tipoProducto - El identificador del bar
	 * @param fecha - La fecha en que se realiz� la visita
	 * @param horario - EL horario en que se realiz� la visita (DIURNO, NOCTURNO, TODOS)
	 * @return EL n�mero de tuplas insertadas
	 */
	public long adicionarCompra (PersistenceManager pm,Long idProducto, Long idFactura,Integer cantidad, Double precio) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCompraProducto () + "(id_producto, id_factura, cantidad, precio) values (?, ?, ?, ?)");
        q.setParameters(idProducto,idFactura,cantidad,precio);
        return (long) q.executeUnique();
	}
	
}
