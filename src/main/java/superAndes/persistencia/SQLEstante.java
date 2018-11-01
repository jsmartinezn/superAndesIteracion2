package superAndes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import superAndes.negocio.Bodega;
import superAndes.negocio.Estante;
import superAndes.negocio.Indice;
import superAndes.negocio.Producto;

class SQLEstante {
	
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
	public SQLEstante (PersistenciaSuperAndes pp)
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
	public long adicionarEstante (PersistenceManager pm,Long id,Long idS,String tipoProducto,Double volumen,Double peso,Double nivel) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaEstante () + "(id,id_Sucursal, tipo_Producto, volumen, peso,nivel) values (?, ?, ?, ?, ?, ?)");
        q.setParameters(id,idS,tipoProducto,volumen,peso,nivel);
        return (long) q.executeUnique();
	}

	public List<Estante> darEstante(PersistenceManager pm,Long idSucursal)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM" + pp.darTablaEstante() + "WHERE idsucursal = ?");
		q.setResultClass(Estante.class);
		q.setParameters(idSucursal);
		return q.executeList();
	}
	public List<Indice> darIndiceDeOcupacionPeso (PersistenceManager pm, Long idSucursal  )
	{
		String a =  "SELECT estante.id as id, pesos/estante.peso as indice FROM " ;
		a+= pp.darTablaEstante(); 
		a+= ",( SELECT estante_producto.id_estante as IDD, SUM(cantidad*peso) as pesos FROM ";
		a+= pp.darTablaEstanteProducto() + "," + "PRODUCTO ";
		a+= " WHERE producto.id = estante_producto.id_producto GROUP BY estante_producto.id_estante)WHERE IDD=ID AND ESTANTE.ID_SUCURSAL = ?";
		Query q = pm.newQuery(SQL, a);
		q.setParameters(idSucursal);
		q.setResultClass(Indice.class);
		return q.executeList();
	}
	
	public List<Indice> darIndiceDeOcupacionVolumen (PersistenceManager pm , Long idSucursal )
	{
		String a =  "SELECT estante.id as id, pesos/estante.volumen as indice FROM " ;
		a+= pp.darTablaEstante(); 
		a+= ",( SELECT estante_producto.id_estante as IDD, SUM(cantidad*volumen) as pesos FROM ";
		a+= pp.darTablaEstanteProducto() + "," + "PRODUCTO ";
		a+= " WHERE producto.id = estante_producto.id_producto GROUP BY estante_producto.id_estante)WHERE IDD=ID AND ESTANTE.ID_SUCURSAL = ?";
		Query q = pm.newQuery(SQL, a);
		q.setParameters(idSucursal);
		q.setResultClass(Indice.class);
		return q.executeList();
	}
	
	public List<Object[]> reOrden(PersistenceManager pm, Long idSucursal, Long idProducto)
	{
		String a = "SELECT ESTANTE_PRODUCTO.ID_ESTANTE, ESTANTE_PRODUCTO.CANTIDAD,PRODUCTO.PESO,PRODUCTO.VOLUMEN,ESTANTE.PESO,ESTANTE.VOLUMEN,ESTANTE.NIVEL FROM ";
		a += pp.darTablaEstanteProducto() + "," + pp.darTablaEstante() + "," + pp.darTablaProducto();
		a += " WHERE ESTANTE_PRODUCTO.ID_PRODUCTO=PRODUCTO.ID AND ESTANTE.ID=ESTANTE_PRODUCTO.ID_ESTANTE AND ESTANTE.ID_SUCURSAL = ? AND PRODUCTO.ID = ?";
		Query q = pm.newQuery(SQL, a);
		q.setParameters(idSucursal,idProducto);
		q.setResultClass(Object[].class);
		return q.executeList();
	}
}
