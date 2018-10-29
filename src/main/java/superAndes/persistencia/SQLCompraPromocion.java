package superAndes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLCompraPromocion {

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
	
	public SQLCompraPromocion(PersistenciaSuperAndes pp){
		this.pp = pp;
	}
	
	public long adicionarCompraPromocion(PersistenceManager pm, Long idCompra, String idPromocion)
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCompraPromocion () + "(id_compra, id_promocion) values (?, ?)");
        q.setParameters(idCompra, idPromocion);
        return (long) q.executeUnique();
	}
}
