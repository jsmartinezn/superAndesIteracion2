package superAndes.persistencia;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import oracle.net.aso.p;
import oracle.net.aso.v;
import superAndes.negocio.Bodega;
import superAndes.negocio.Compra;
import superAndes.negocio.Empresa;
import superAndes.negocio.Estante;
import superAndes.negocio.LlegadaProducto;
import superAndes.negocio.OrdenPedido;
import superAndes.negocio.PersonaNatural;
import superAndes.negocio.Producto;
import superAndes.negocio.ProductoProveedor;
import superAndes.negocio.Promocion;
import superAndes.negocio.Proveedor;
import superAndes.negocio.SucursalProducto;

public class PersistenciaSuperAndes {

	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(PersistenciaSuperAndes.class.getName());
	
	/**
	 * Cadena para indicar el tipo de sentencias que se va a utilizar en una consulta
	 */
	public final static String SQL = "javax.jdo.query.SQL";

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * Atributo privado que es el único objeto de la clase - Patrón SINGLETON
	 */
	private static PersistenciaSuperAndes instance;
	
	/**
	 * Fábrica de Manejadores de persistencia, para el manejo correcto de las transacciones
	 */
	private PersistenceManagerFactory pmf;
	
	/**
	 * Arreglo de cadenas con los nombres de las tablas de la base de datos, en su orden:
	 * Secuenciador, tipoBebida, bebida, bar, bebedor, gustan, sirven y visitan
	 */
	private List <String> tablas;
	
	/**
	 * Atributo para el acceso a las sentencias SQL propias a PersistenciaParranderos
	 */
	private SQLUtil sqlUtil;
	
	private SQLBodega sqlBodega;
	
	private SQLCompra sqlCompra;
	
	private SQLEmpresa sqlEmpresa;
	
	private SQLEstante sqlEstante;
	
	private SQLOrdenPedido sqlOrden;
	
	private SQLPersonaNatural sqlPersona;
	
	private SQLProducto sqlProducto;
	
	private SQLPromocion sqlPromocion;
	
	private SQLProductoProvedor sqlProductoProvedor;
	
	private SQLCompraPromocion sqlCompraPromocion;
	
	private SQLLlegadaProducto sqlLLegadaProducto;
	
	private SQLProveedor sqlProveedor;
	
	private SQLSucursal sqlSucursal;
	
	private SQLSucursalProducto sqlSucursalProducto;
	
	private PersistenciaSuperAndes()
	{
		pmf = JDOHelper.getPersistenceManagerFactory("Parranderos");		
		
		crearClasesSql();
		
		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String> ();
		tablas.add ("SuperAndes_sequence");
		tablas.add ("BODEGA");
		tablas.add ("COMPRA");
		tablas.add ("EMPRESA");
		tablas.add ("ESTANTE");
		tablas.add ("ORDENPEDIDO");
		tablas.add ("PERSONANATURAL");
		tablas.add ("PRODUCTO");
		tablas.add ("PRODUCTO_PROVEDOR");
		tablas.add ("PROMOCION");
		tablas.add ("PROVEDOR");
		tablas.add ("SUCURSAL");
		tablas.add ("SUCURSAL_PROVEDOR");
		tablas.add ("LLEGADA_PRODUCTO");
		tablas.add ("COMPRA_PROMOCION");
		
		
	}
	
	private PersistenciaSuperAndes(JsonObject config)
	{
		crearClasesSql();
		tablas = leerNombresTablas(config);
		
		String unidadPersistencia = config.get ("unidadPersistencia").getAsString ();
		log.trace ("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory (unidadPersistencia);
		
	}
	
	public static PersistenciaSuperAndes getInstance ()
	{
		if (instance == null)
		{
			instance = new PersistenciaSuperAndes ();
		}
		return instance;
	}
	

	public static PersistenciaSuperAndes getInstance (JsonObject tableConfig)
	{
		if (instance == null)
		{
			instance = new PersistenciaSuperAndes (tableConfig);
		}
		return instance;
	}

	
	public void cerrarUnidadPersistencia ()
	{
		pmf.close ();
		instance = null;
	}
	
	public String darSeqParranderos() {
		return tablas.get(0);
	}
	
	public String darTablaBodega()
	{
		return tablas.get(1);
	}
	public String darTablaPersonaNatural()
	{
		return tablas.get(6);
	}
	public String darTablaCompra()
	{
		return tablas.get(2);
	}
	public String darTablaEmpresa()
	{
		return tablas.get(3);
	}
	public String darTablaEstante()
	{
		return tablas.get(4);
	}
	public String darTablaOrdenPedido()
	{
		return tablas.get(5);
	}
	public String darTablaProducto()
	{
		return tablas.get(6);
	}
	public String darTablaProductoProvedor()
	{
		return tablas.get(7);
	}
	public String darTablaPromocion()
	{
		return tablas.get(8);
	}
	public String darTablaProvedor()
	{
		return tablas.get(9);
	}
	public String darTablaSucursal()
	{
		return tablas.get(10);
	}
	public String darTablaSucursalProvedor()
	{
		return tablas.get(11);
	}
	public String darTablaLlegadaProducto()
	{
		return tablas.get(12);
	}
	public String darTablaCompraPromocion()
	{
		return tablas.get(13);
	}
	
	public void crearClasesSql()
	{
		sqlBodega = new SQLBodega(this);
		sqlCompra = new SQLCompra(this);
		sqlEmpresa = new SQLEmpresa(this);
		sqlEstante = new SQLEstante(this);
		sqlOrden = new SQLOrdenPedido(this);
		sqlPersona = new SQLPersonaNatural(this);
		sqlLLegadaProducto = new SQLLlegadaProducto(this);
		sqlProducto = new SQLProducto(this);
		sqlProductoProvedor = new SQLProductoProvedor(this);
		sqlProveedor = new SQLProveedor(this);
		sqlPromocion = new SQLPromocion(this);
		sqlSucursal = new SQLSucursal(this);
		sqlSucursalProducto = new SQLSucursalProducto(this);
		sqlUtil = new SQLUtil(this);
	}
	private List <String> leerNombresTablas (JsonObject tableConfig)
	{
		JsonArray nombres = tableConfig.getAsJsonArray("tablas") ;

		List <String> resp = new LinkedList <String> ();
		for (JsonElement nom : nombres)
		{
			resp.add (nom.getAsString ());
		}
		
		return resp;
	}
	
	private long nextval()
	{
        long resp = sqlUtil.nextval (pmf.getPersistenceManager());
        log.trace ("Generando secuencia: " + resp);
        return resp;
    }

	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}
	
	public Bodega adicionarBodega(Long idSucursal, String tipoProducto,Double volumen,Double peso){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlBodega.adicionarBodega(pm, idSucursal,tipoProducto,volumen,0.0,peso,0.0,0);
            tx.commit();
            
            log.trace ("Inserción de la bodega: " + idSucursal + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Bodega (idSucursal, tipoProducto,volumen,0.0,peso,0.0,0);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public List<String> indiceDeOcupacionVolumenBodega(Long idSucursal){
		
		List<Bodega> nueva = sqlBodega.darBodega(pmf.getPersistenceManager(), idSucursal);
		ArrayList<String> resp = new ArrayList<>();
		for(Bodega a : nueva)
		{
			resp.add("El indice del producto " + a.getTipoProducto() + " es: " + a.getVolumenActual()/a.getVolumen());
		}
		return resp;
	}
	
	public List<String> indiceDeOcupacionPesoBodega(Long idSucursal){
		List<Bodega> nueva = sqlBodega.darBodega(pmf.getPersistenceManager(), idSucursal);
		ArrayList<String> resp = new ArrayList<>();
		for(Bodega a : nueva)
		{
			resp.add("El indice del producto " + a.getTipoProducto() + " es: " + a.getPesoActual()/a.getPeso());
		}
		return resp;
	}
	
	public Compra adicionarCompra(Long idC, Long idP, Long idS, Integer cantidad, Boolean promocion,Double precio,Date fecha){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long idCompra = nextval ();
            long tuplasInsertadas = sqlCompra.adicionarCompra(pm, idCompra, idC, idP, idS, cantidad, promocion, precio, fecha);
            Bodega nuevo = sqlBodega.darBodega(pm, idS, idP);
            Integer actualizarEstante = sqlEstante.actualizarVenta(pm, idS, idP,cantidad, nuevo.getCantidad());
            if(actualizarEstante != 0)  sqlBodega.actualizarBodega(pm,actualizarEstante,idC,idS);
            tx.commit();
            
            log.trace ("Inserción de la compra: " + idCompra + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Compra(idCompra, idC, idP, idS, cantidad, promocion, precio, fecha);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public List<Compra> darComprasFechas(Date fechaInicio,Date fechaFin)
	{
		return sqlCompra.darDineroTiempo(pmf.getPersistenceManager(), fechaInicio, fechaFin);
	}
	
	public List<Compra> darComprasPorUsuario(Date fechaInicio,Date fechaFin,Long idCliente)
	{
		return sqlCompra.darDineroPorCliente(pmf.getPersistenceManager(), fechaInicio, fechaFin,idCliente);
	}
	
	public Empresa adicionarEmpresa(String nombre, String correo, Long nit,String direccion){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long idEmpresa = nextval ();
            long tuplasInsertadas = sqlEmpresa.adicionar(pm, idEmpresa, nombre, correo, nit, direccion);
            tx.commit();
            
            log.trace ("Inserción de la empresa: " + idEmpresa + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Empresa(idEmpresa, nombre, correo, nit, direccion);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public Estante adicionarEstante(String tipoProducto,Double volumen,Double volumen2,String unidadV,Double peso,Double peso2,String unidadP,Integer nivel){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long idEstante = nextval ();
            long tuplasInsertadas = sqlEstante.adicionarEstante(pm, idEstante, tipoProducto, volumen, volumen2, unidadV, peso, peso2, unidadP,0,nivel);
            tx.commit();
            
            log.trace ("Inserción de la empresa: " + idEstante + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Estante(idEstante, tipoProducto, volumen, volumen2, unidadV, peso, peso2, unidadP,0,nivel);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public List<String> indiceDeOcupacionVolumenEstante(Long idSucursal){
		
		List<Estante> nueva = sqlEstante.darEstante(pmf.getPersistenceManager(), idSucursal);
		ArrayList<String> resp = new ArrayList<>();
		for(Estante a : nueva)
		{
			resp.add("El indice del producto " + a.getTipoProducto() + " es: " + a.getVolumenActual()/a.getVolumen());
		}
		return resp;
	}
	
	public List<String> indiceDeOcupacionPesoEstante(Long idSucursal){
		List<Estante> nueva = sqlEstante.darEstante(pmf.getPersistenceManager(), idSucursal);
		ArrayList<String> resp = new ArrayList<>();
		for(Estante a : nueva)
		{
			resp.add("El indice del producto " + a.getTipoProducto() + " es: " + a.getPesoActual()/a.getPeso());
		}
		return resp;
	}
	
	public OrdenPedido adicionarOrdenPedido(Long idProveedor,Long idSucursal,Date fechaEsperada, String estado){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long idOrden = nextval ();
            long tuplasInsertadas = sqlOrden.adicionarOrden(pm, idOrden, idProveedor, idSucursal, fechaEsperada, estado);
            tx.commit();
            
            log.trace ("Inserción de la empresa: " + idOrden + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new OrdenPedido(idOrden, idProveedor, idSucursal, fechaEsperada, null, estado);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public long realizarEntrega(Long id,Date fecha,Double calificacion,String estado)
	{		
		PersistenceManager pm = pmf.getPersistenceManager();
	    Transaction tx=pm.currentTransaction();
	    try
	    {
	        tx.begin();
	        long resp = sqlOrden.entregarOrden(pmf.getPersistenceManager(), id, fecha, calificacion, estado);
	        tx.commit();
	        
	        log.trace ("Se actualizo la orden: " + id + " a entregada");
	        
	        return resp;
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
	    	return -1;
	    }
	    finally
	    {
	        if (tx.isActive())
	        {
	            tx.rollback();
	        }
	        pm.close();
	    }
	}
	
	public List<OrdenPedido> darOrdenes(){
		return sqlOrden.darOrden(pmf.getPersistenceManager());
	}
	
	public PersonaNatural adicionarPersona(String nombre, String correo, Long cedula){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long idPersona = nextval ();
            long tuplasInsertadas = sqlPersona.adicionar(pm, idPersona, nombre, correo, cedula);
            tx.commit();
            
            log.trace ("Inserción de la empresa: " + idPersona + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new PersonaNatural(idPersona, nombre, correo, cedula);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public Producto adicionarProducto(Long codigoB, String nombre, String marca, String presentacion, Double precioUM, String UM,Integer cantdadP,Double volumen, Double peso, String catrgoria){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
            tx.begin();
            long codigo = nextval ();
            long tuplasInsertadas = sqlProducto.adicionarProducto(pm, codigoB, nombre, marca, presentacion, precioUM, UM, cantdadP, volumen, peso, catrgoria);
            tx.commit();
            
            log.trace ("Inserción de la empresa: " + codigo + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Producto(codigoB, marca, nombre, presentacion, precioUM, UM, cantdadP, volumen, peso, catrgoria);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public LlegadaProducto adicionarLlegadaProducto( Long idProducto, Long idOrden, Double volumen, String uM, Integer cantidadR, Double calidadR){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
            tx.begin();
            long id = nextval ();
            long tuplasInsertadas = sqlLLegadaProducto.adicionarLlegadaProducto(pm, idProducto, idOrden, volumen, uM, cantidadR, calidadR);
            tx.commit();
            
            log.trace ("Inserción de la empresa: " + id + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new LlegadaProducto(idProducto, idOrden, volumen, uM, cantidadR, calidadR);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public ProductoProveedor adicionarProductoProveedor(Long idProducto, Long idProvedor, Double precio, Double indiceCalidad){
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
            tx.begin();
            long id = nextval ();
            long tuplasInsertadas = sqlProductoProvedor.adicionarProductoProvedor(pm, idProducto, idProvedor, precio, indiceCalidad);
            tx.commit();
            
            log.trace ("Inserción de la empresa: " + id + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new ProductoProveedor(idProducto, idProvedor, precio, indiceCalidad);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public Promocion adicionarPromocion(Long idSucursal, Long idProducto, Date fechaInicio, Date fechaFin, String condicion){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
            tx.begin();
            long id = nextval ();
            long tuplasInsertadas = sqlPromocion.adicionarPromocion(pm, idSucursal, idProducto, fechaInicio, fechaFin, condicion);
            tx.commit();
            
            log.trace ("Inserción de la empresa: " + id + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Promocion(idSucursal, idProducto, fechaInicio, fechaFin, condicion);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public Proveedor adicionarProveedor(Long nit, String nombre){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
            tx.begin();
            long id = nextval ();
            long tuplasInsertadas = sqlProveedor.adicionarProvedor(pm, nit, nombre);
            tx.commit();
            
            log.trace ("Inserción de la empresa: " + id + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Proveedor(nit, nombre);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public SucursalProducto adicionarSucursalProducto( Long idSucursal, Long idProducto, Double precio, Integer nivelReorden){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
            tx.begin();
            long id = nextval ();
            long tuplasInsertadas = sqlSucursalProducto.adicionarSucursalProvedor(pm, idSucursal, idProducto, precio, nivelReorden);
            tx.commit();
            
            log.trace ("Inserción de la empresa: " + id + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new SucursalProducto(idSucursal, idProducto, precio, nivelReorden);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public List<Promocion> darPromocionesMasPopulares(){
		PersistenceManager pm = pmf.getPersistenceManager();
		return sqlPromocion.darVeinteMejoresPromociones(pm);
	}
	
	public List<Producto> darProductoPorCondicion(String condicion){
		PersistenceManager pm = pmf.getPersistenceManager();
		return sqlProducto.darProductoCondicion(pm, condicion);
	}

	
}
