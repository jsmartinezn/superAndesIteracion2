package superAndes.persistencia;

import java.math.BigDecimal;
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
import superAndes.negocio.BodegaProducto;
import superAndes.negocio.Compra;
import superAndes.negocio.Empresa;
import superAndes.negocio.Estante;
import superAndes.negocio.EstanteProducto;
import superAndes.negocio.Indice;
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
	
	private SQLBodegaProducto sqlBodegaProducto;
	
	private SQLCompra sqlCompra;
	
	private SQLCompraProducto sqlCompraProducto;
	
	private SQLEmpresa sqlEmpresa;
	
	private SQLEstante sqlEstante;
	
	private SQLEstanteProducto sqlEstanteProducto;
	
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
		tablas.add("Bodega_Producto");
		tablas.add ("COMPRA");
		tablas.add("COMPRA_PRODUCTO");
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
	public String darTablaBodegaProducto()
	{
		return tablas.get(2);
	}
	public String darTablaCompraProducto() {
		return tablas.get(4);
	}
	public String darTablaPersonaNatural()
	{
		return tablas.get(6);
	}
	public String darTablaCompra()
	{
		return tablas.get(3);
	}
	public String darTablaEmpresa()
	{
		return tablas.get(3);
	}
	public String darTablaEstante()
	{
		return tablas.get(5);
	}
	public String darTablaEstanteProducto()
	{
		return tablas.get(6);
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
		sqlBodegaProducto = new SQLBodegaProducto(this);
		sqlCompra = new SQLCompra(this);
		sqlCompraProducto = new SQLCompraProducto(this);
		sqlEmpresa = new SQLEmpresa(this);
		sqlEstante = new SQLEstante(this);
		sqlEstanteProducto = new SQLEstanteProducto(this);
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
	
	public String darIndiceDeOcupacionBodegaPeso(Long idSucursal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        String resp = "";
        try
        {
            tx.begin();
            List<Indice> lista = sqlBodega.darIndiceDeOcupacionPeso(pm, idSucursal);
            tx.commit();
            for(Indice temp : lista){
            	resp += "La bodega de indice: " +temp.getId() + " posee un indice de ocupacion por peso de: " + temp.getIndice()+"\n";
            }
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
        return resp;
	}
	public String darIndiceDeOcupacionBodegaVolumen(Long idSucursal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        String resp = "";
        try
        {
            tx.begin();
            List<Indice> lista = sqlBodega.darIndiceDeOcupacionVolumen(pm,idSucursal);
            tx.commit();
            for(Indice temp : lista){
            	resp += "La bodega de indice: " +temp.getId() + " posee un indice de ocupacion por volumen de: " + temp.getIndice()+"\n";
            }
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
        return resp;
	}
	public String darIndiceDeOcupacionEstantePeso(Long idSucursal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        String resp = "";
        try
        {
            tx.begin();
            List<Indice> lista = sqlEstante.darIndiceDeOcupacionPeso(pm,idSucursal);
            tx.commit();
            for(Indice temp : lista){
            	resp += "El estante de indice: " +temp.getId() + " posee un indice de ocupacion por peso de: " + temp.getIndice()+"\n";
            }
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
        return resp;
	}
	public String darIndiceDeOcupacionEstanteVolumen(Long idSucursal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        String resp = "";
        try
        {
            tx.begin();
            List<Indice> lista = sqlEstante.darIndiceDeOcupacionVolumen(pm,idSucursal);
            tx.commit();
            for(Indice temp : lista){
            	resp += "El estante de indice: " +temp.getId() + " posee un indice de ocupacion por volumen de: " + temp.getIndice()+"\n";
            }
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
        return resp;
	}
	
	public Bodega adicionarBodega(Long idSucursal, String tipoProducto,Double volumen,Double peso){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long id =nextval();
            long tuplasInsertadas = sqlBodega.adicionarBodega(pm, id, idSucursal,tipoProducto,volumen,peso);
            tx.commit();
            
            log.trace ("Inserción de la bodega: " + id + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Bodega (id,idSucursal, tipoProducto,volumen,peso);
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
	
	
	public BodegaProducto adicionarBodegaProducto(Long idBodega,Long idProducto,Integer cantidad){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlBodegaProducto.adicionarBodegaProducto(pm, idBodega,idProducto,cantidad);
            tx.commit();
            
            log.trace ("Inserción de la bodega_producto: " + idBodega +" ; "+ idProducto + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new BodegaProducto(idProducto, idBodega, cantidad);
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
	
	public Compra adicionarCompra(Long idC, Long idS,Long idP, Integer cantidad,Date fecha){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            //Registra la compra
            long idCompra = nextval ();
            long tuplasInsertadas = sqlCompra.adicionarCompra(pm, idCompra, idC, idS, fecha);
            List<Object[]> disponible = sqlEstanteProducto.unidadesEnInventario(pm, idS, idP);
            Integer disp = 0;
            //Revisar si existe la cantidad del producto deseado en los estantes, arroja excepcion de lo contrario
            for(Object[] temp : disponible){
            	BigDecimal ayuda = (BigDecimal) temp[1];
            	disp += ayuda.intValue();
            }
            if(cantidad<=disp)
            {
            	//Actualizar Estante
            	int cantidad2 = 0 + cantidad;
            	boolean a = false;
            	for(int i = 0;i < disponible.size() && !a;i++){
                	BigDecimal ayuda = (BigDecimal) disponible.get(i)[1];
                	BigDecimal idEstante = (BigDecimal) disponible.get(i)[0];
            		if(cantidad2>ayuda.intValue()){
            			sqlEstanteProducto.actualizar(pm, idEstante.longValue(),idP, 0);
            			cantidad2-=idEstante.longValue();
            		}
            		else{
            			sqlEstanteProducto.actualizar(pm, idEstante.longValue(),idP, ayuda.intValue()-cantidad2);
            			a = true;
            			cantidad2=0;
            		}
            	}
            	disp-=cantidad;
            	List<Object[]> lista = sqlBodegaProducto.unidadesEnInventario(pm, idS, idP);
                for(Object[] temp : lista){
                	BigDecimal ayuda = (BigDecimal) temp[1];
                	disp += ayuda.intValue();
                }
                Integer nivelReOrden = 6;//Se necesita la clase sucursal-producto para esto esperar compañero
                if(disp<nivelReOrden){
                	
                }
            }
            else{
            	tx.rollback();
            	throw new Exception("No hay diponibilidad del producto");
            }
            tx.commit();
            
            log.trace ("Inserción de la compra: " + idCompra + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Compra(idCompra, idC, idS, fecha);
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
	
	public Estante adicionarEstante(Long idSucursal, String tipoProducto,Double volumen,Double peso,Double nivel){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long id =nextval();
            long tuplasInsertadas = sqlEstante.adicionarEstante(pm, id, idSucursal,tipoProducto,volumen,peso,nivel);
            tx.commit();
            
            log.trace ("Inserción de la bodega: " + id + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Estante (id,idSucursal, tipoProducto,volumen,peso,nivel);
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
	/*
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
	*/
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
	
	public SucursalProducto adicionarSucursalProducto(Long idProducto, Double precio, Integer nivelReorden){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
            tx.begin();
            long id = nextval ();
            long tuplasInsertadas = sqlSucursalProducto.adicionarSucursalProvedor(pm, id, idProducto, precio, nivelReorden);
            tx.commit();
            
            log.trace ("Inserción de la empresa: " + id + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new SucursalProducto(id, idProducto, precio, nivelReorden);
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
