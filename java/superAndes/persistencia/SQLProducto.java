package superAndes.persistencia;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import superAndes.negocio.Producto;

public class SQLProducto {

	
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
	
	public SQLProducto(PersistenciaSuperAndes pp){
		this.pp = pp;
	}
	
	public long adicionarProducto(PersistenceManager pm, Long codigoB, String nombre, String marca, String presentacion, Double precioUM, String UM,Integer cantdadP,Double volumen, Double peso, String catrgoria)
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaProducto () + "(codigodebarras, nombre, marca, presentacion, precioporunidaddemedida, unidaddemedida, cantidaddelapresentacion, volumen, peso, categoria) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        q.setParameters(codigoB, nombre,marca,presentacion,precioUM,UM,cantdadP, volumen);
        return (long) q.executeUnique();
	}
	
	public List<Producto> darProductoCondicion(PersistenceManager pm, String condicion){
        String sql = "SELECT * ";
        sql += " FROM " + pp.darTablaProducto();
       	sql	+= " WHERE " + condicion;
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Producto.class);
		return q.executeList();
	}
	
}
