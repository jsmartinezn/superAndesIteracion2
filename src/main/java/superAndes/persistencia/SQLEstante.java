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
	public SQLEstante (PersistenciaSuperAndes pp)
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
	
/*
	public int actualizarVenta(PersistenceManager pm,Long idSucursal,Long idProducto,Integer cantidad,Integer cantidadMaxima)
	{
		int resp = 0;
		Query q = pm.newQuery(SQL,"SELECT volumen,peso FROM "+ pp.darTablaProducto() + " WHERE id = ?");
		q.setParameters(idProducto);
		q.setResultClass(Producto.class);
		Producto nuevo = (Producto) q.executeUnique();
		Double volumen = nuevo.getVolumen();
		Double peso = nuevo.getPeso();
		Query p = pm.newQuery("SELECT volumen,volumenactual,peso,pesoactual,cantidad FROM "+ pp.darTablaEstante() + " WHERE tipoproducto = (SELECT tipoproducto FROM " + pp.darTablaProducto() + " WHERE id = ?) AND idSucursal = ? ");
		p.setParameters(idProducto,idSucursal);
		Estante temp = (Estante) p.executeUnique();
		Integer max = temp.getCantidad() - cantidad;
		if(max<temp.getNivelDeReOrden())
		{
			Double a = 0.0;
			Double pesoDisponible = (temp.getPeso()-((temp.getCantidad()+cantidad)*peso));
			Double volumenDisponible = (temp.getVolumen()-((temp.getCantidad()+cantidad)*peso));
			a = pesoDisponible/peso < volumenDisponible/volumen ? pesoDisponible/peso : volumenDisponible/volumen;
			max = a.intValue() > cantidadMaxima ? cantidadMaxima : a.intValue();
			resp = max;
		}
		Integer cant = temp.getCantidad() +max;
		Query t =pm.newQuery(SQL, "UPDATE " + pp.darTablaEstante() + " SET volumenactual = ?, pesoactual = ?, cantidad = ? WHERE tipoproducto = (SELECT tipoproducto FROM " + pp.darTablaProducto() + " WHERE id = ?) AND idSucursal = ? ");
		t.setParameters(cant*volumen,cant*peso,cant,idProducto,idSucursal);
		t.executeUnique();
		return resp;
	}
*/
}
