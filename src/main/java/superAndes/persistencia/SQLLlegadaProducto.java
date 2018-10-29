package superAndes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLLlegadaProducto {

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
	
	public SQLLlegadaProducto(PersistenciaSuperAndes pp){
		this.pp = pp;
	}
	
	public long adicionarLlegadaProducto(PersistenceManager pm, Long idProducto, Long idOrden, Double volumen, String uM, Integer cantidadR, Double calidadR  ){
		
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaLlegadaProducto () + "(id_producto, id_orden, volumen, Unidad_de_medida, cantidad_recibida, calidad_recibida) values (?, ?, ?, ?, ?, ?)");
        q.setParameters(idProducto, idOrden,volumen,uM,cantidadR,calidadR);
        return (long) q.executeUnique();
		
	}
	
}
