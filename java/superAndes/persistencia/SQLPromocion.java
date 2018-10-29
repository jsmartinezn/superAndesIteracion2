package superAndes.persistencia;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import superAndes.negocio.Promocion;

public class SQLPromocion {

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
	
	public SQLPromocion(PersistenciaSuperAndes pp){
		this.pp = pp;
	}
	
	public long adicionarPromocion(PersistenceManager pm, Long idSucursal, Long idProducto, Date fechaInicio, Date fechaFin, String condicion){
		
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaPromocion () + "(id_sucursal, id_producto, fecha_inicio, fecha_fin, condicion) values (?, ?, ?, ?, ?)");
        q.setParameters(idSucursal, idProducto, fechaInicio, fechaFin, condicion);
        return (long) q.executeUnique();
	}
	
	public List<Promocion> darVeinteMejoresPromociones(PersistenceManager pm){
        String sql = "SELECT idBar, count (*) as numBebidas";
        sql += " FROM " + pp.darTablaCompraPromocion();
       	sql	+= " GROUP BY idBar";
       	sql += " order by numCompras DESC";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Promocion.class);
		LinkedList<Promocion> res = new LinkedList<>();
		for(int i = 1; i< q.executeList().size(); i++){
			res.add( (Promocion) q.executeList().get(i));
		}
		
		return res;
	}
}
