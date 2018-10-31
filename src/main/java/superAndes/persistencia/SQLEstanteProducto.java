package superAndes.persistencia;

import java.sql.PreparedStatement;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import superAndes.negocio.EstanteProducto;

public class SQLEstanteProducto {
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
	public SQLEstanteProducto (PersistenciaSuperAndes pp)
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
	public long adicionarEstanteProducto (PersistenceManager pm,Long idEstante,Long idProducto,Integer cantidad) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaEstanteProducto () + "(id_producto,id_estante, cantidad) values (?, ?, ?)");
        q.setParameters(idProducto,idEstante,cantidad);

        return (long) q.executeUnique();
	}
	public List<Object[]> unidadesEnInventario(PersistenceManager pm, Long idSucursal,Long idProducto){
		String a = "SELECT ID_ESTANTE,CANTIDAD FROM ";
		a += pp.darTablaEstanteProducto() + "," + pp.darTablaEstante();
		a+=" WHERE ID_PRODUCTO = ? AND ESTANTE.ID=ESTANTE_PRODUCTO.ID_ESTANTE AND ESTANTE.ID_SUCURSAL=?";
		
		Query q = pm.newQuery(SQL,a);
		q.setResultClass(Object[].class);
		q.setParameters(idProducto,idSucursal);
		return q.executeList();
	}
	public void actualizar(PersistenceManager pm,Long idEstante,Long idProducto,Integer cantidad){
		Query q = pm.newQuery(SQL, "UPDATE "+ pp.darTablaEstanteProducto()+" SET CANTIDAD = ? WHERE ID_ESTANTE = ? AND ID_PRODUCTO = ?");
		q.setParameters(cantidad,idEstante,idProducto);
		q.executeUnique();
	}	
}

