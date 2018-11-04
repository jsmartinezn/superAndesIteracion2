package superAndes.persistencia;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Timestamp;
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
import superAndes.negocio.CarritoCompras;
import superAndes.negocio.CarritoComprasProducto;
import superAndes.negocio.Cliente;
import superAndes.negocio.Compra;
import superAndes.negocio.CompraProducto;
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
import superAndes.negocio.Sucursal;
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
	
	private SQLCarritoCompras sqlCarritoCompras;
	
	private SQLCarritoComprasProducto sqlCarritoComprasProducto;
	
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
		return tablas.get(17);
	}

	public String darTablaCompra()
	{
		return tablas.get(3);
	}

	public String darTablaCliente() {
		return tablas.get(8);
	}
	public String darTablaEstante()
	{
		return tablas.get(5);
	}
	public String darTablaEstanteProducto()
	{
		return tablas.get(6);
	}
	public String darTablaCarrito() {
		return tablas.get(9);
	}
	public String darTablaCarritoProducto() {
		
		return tablas.get(10);
	}
	public String darTablaOrdenPedido()
	{
		return tablas.get(16);
	}
	public String darTablaProducto()
	{
		return tablas.get(13);
	}
	public String darTablaProductoProvedor()
	{
		return tablas.get(12);
	}
	public String darTablaPromocion()
	{
		return tablas.get(8);
	}
	public String darTablaProvedor()
	{
		return tablas.get(11);
	}
	public String darTablaSucursal()
	{
		return tablas.get(14);
	}
	public String darTablaSucursalProducto()
	{
		return tablas.get(15);
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
		sqlCarritoCompras = new SQLCarritoCompras(this);
		sqlCarritoComprasProducto = new SQLCarritoComprasProducto(this);
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
	
	public EstanteProducto adicionarEstanteProducto(Long idEstante,Long idProducto,Integer cantidad){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlEstanteProducto.adicionarEstanteProducto(pm, idEstante,idProducto,cantidad);
            tx.commit();
            
            log.trace ("Inserción de la bodega_producto: " + idEstante +" ; "+ idProducto + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new EstanteProducto(idProducto, idEstante, cantidad);
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
	
	public CompraProducto adicionarCompraProducto(Long idC, Long idS,Long idP, Integer cantidad,Date fecha,boolean a){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            //Registra la compra
            long idCompra = nextval ();

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
            	if(a)
            		abastecerEstante(pm, cantidad2, disponible, idP);
            	disp-=cantidad;
            	Integer dispBodega = 0;
            	List<Object[]> lista = sqlBodegaProducto.unidadesEnInventario(pm, idS, idP);
                for(Object[] temp : lista){
                	BigDecimal ayuda = (BigDecimal) temp[1];
                	disp += ayuda.intValue();
                	dispBodega += ayuda.intValue();
                }
                List<Object[]> reOrdenE = sqlEstante.reOrden(pm, idS, idP);
                int i = 0;
                for(Object[]temp : reOrdenE)
                {
                	BigDecimal idE = (BigDecimal) temp[0];
                	BigDecimal cant = (BigDecimal) temp[1];
                	BigDecimal pesoP = (BigDecimal) temp[2];
                	BigDecimal volP = (BigDecimal) temp[3];
                	BigDecimal pesoE = (BigDecimal) temp[4];
                	BigDecimal volE = (BigDecimal) temp[5];
                	BigDecimal niv = (BigDecimal) temp[6];
	                if(cant.doubleValue()*volP.doubleValue()/volE.doubleValue()<niv.doubleValue()){
	                	Double dispPeso = (pesoE.doubleValue()-cant.doubleValue()*pesoP.doubleValue())/pesoP.doubleValue();
	                	Double dispVol = (volE.doubleValue()-cant.doubleValue()*volP.doubleValue())/volP.doubleValue();
	                	Integer ayuda3 = 0;
	                	if(dispPeso>dispVol){
	                		ayuda3 = dispVol.intValue();
	                	}
	                	else{
	                		ayuda3 = dispPeso.intValue();
	                	}
	                	if(dispBodega > ayuda3)
	                	{
	                		sqlEstanteProducto.actualizar(pm, idE.longValue(), idP, cant.intValue()+ayuda3);
	                	}
	                	else{
	                		sqlEstanteProducto.actualizar(pm, idE.longValue(), idP, cant.intValue()+dispBodega);
	                		ayuda3 = dispBodega;
	                		dispBodega = 0;
	                	}
	                	boolean b = false;
	                	for(; i < lista.size() && !b;i++)
	                	{
	                      	BigDecimal ayuda = (BigDecimal) lista.get(i)[1];
	                       	BigDecimal idEstante = (BigDecimal) lista.get(i)[0];
	                    	if(ayuda3>ayuda.intValue()){
	                    		sqlBodegaProducto.actualizar(pm, idEstante.longValue(),idP, 0);
	                    		ayuda3-=ayuda.intValue();
	                    	}
	                       	else{
	                       		sqlBodegaProducto.actualizar(pm, idEstante.longValue(),idP, ayuda.intValue()-ayuda3);
	                       		b = true;
	                       		ayuda3=0;
	                       	}
	                	}	                	
                	}
                	
                }
                Integer nivelReOrden = 10;//Se necesita la clase sucursal-producto para esto esperar compañero
                if(disp<nivelReOrden){
                	List<Object[]> orden = sqlBodega.reOrden(pm, idS, idP);
                	Integer ayuda3 = 0;
                	for(Object[] temp : orden){
	                	BigDecimal cant = (BigDecimal) temp[1];
	                	BigDecimal pesoP = (BigDecimal) temp[2];
	                	BigDecimal volP = (BigDecimal) temp[3];
	                	BigDecimal pesoB = (BigDecimal) temp[4];
	                	BigDecimal volB = (BigDecimal) temp[5];
	                	Double dispPeso = (pesoB.doubleValue()-cant.doubleValue()*pesoP.doubleValue())/pesoP.doubleValue();
	                	Double dispVol = (volB.doubleValue()-cant.doubleValue()*volP.doubleValue())/volP.doubleValue();
	                	if(dispPeso>dispVol){
	                		ayuda3 += dispVol.intValue();
	                	}
	                	else{
	                		ayuda3 += dispPeso.intValue();
	                	}
                	}
                	List<Object[]> list = sqlProductoProvedor.darProductoProvedor(pm, idP);
                	if(!list.isEmpty())
                	{
                		BigDecimal idProveedor = (BigDecimal)list.get(0)[0];
                		BigDecimal precio = (BigDecimal)list.get(0)[1];
                		adicionarOrdenPedido(idProveedor.longValue(), idS, idP, precio.doubleValue(), ayuda3, new Date(System.currentTimeMillis()+(10*24*60*60*1000)), OrdenPedido.P);
                	}
                }
            }
            else{
            	tx.rollback();
            	throw new Exception("No hay diponibilidad del producto");
            }

            

            BigDecimal precio = (BigDecimal)sqlSucursalProducto.getPrecio(pm, idS, idP);

            if(a)
            	sqlCompra.adicionarCompra(pm, idCompra, idC, idS);
            sqlCompraProducto.adicionarCompra(pm, idP, idCompra, cantidad, precio.doubleValue());
            log.trace ("Inserción de la compra: " + idCompra);
            tx.commit();
            return new CompraProducto(idP, idCompra, cantidad, precio.doubleValue());
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
	
	public Empresa adicionarEmpresas(String nombre, String correo, Long nit,String direccion){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlEmpresa.adicionar(pm, nombre, correo, nit, direccion);
            tx.commit();
            
            log.trace ("Inserción de la empresa: " + nit + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Empresa(nombre, correo, nit, direccion);
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

	public OrdenPedido adicionarOrdenPedido(Long idProveedor,Long idSucursal,Long idProducto,Double precio,Integer cantidad,Date fechaEsperada, String estado){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long idOrden = nextval ();
            long tuplasInsertadas = sqlOrden.adicionarOrden(pm, idOrden, idProveedor, idSucursal, idProducto, fechaEsperada, cantidad, precio, estado);
            tx.commit();
            
            log.trace ("Inserción de la empresa: " + idOrden + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new OrdenPedido(idOrden, idProveedor, idSucursal, idProducto, fechaEsperada, cantidad, precio, null, estado, null, null);
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
	
	public long realizarEntrega(Long id,Date fecha,Double calificacion,String estado,Integer CantidadR)
	{		
		PersistenceManager pm = pmf.getPersistenceManager();
	    Transaction tx=pm.currentTransaction();
	    try
	    {
	        tx.begin();
	        Object[] obj = sqlOrden.darTipoOrden(pm, id);
	        String tipoP = (String) obj[0];
	        BigDecimal idSucursal = (BigDecimal) obj[3];
	        BigDecimal pesoP = (BigDecimal) obj[2];
	        BigDecimal volP = (BigDecimal) obj[1];
	        BigDecimal idP = (BigDecimal) obj[4];
	        List<Object[]> lista = sqlBodega.darDisponible(pm, tipoP, idSucursal.longValue(),idP.longValue());
	        boolean termine =false;
	        Integer faltan = CantidadR;
	        for(int i = 0; i < lista.size() && !termine;i++){
	        	Object[] obj2 = lista.get(i);
	        	Integer caben = 0;
	        	BigDecimal idB = (BigDecimal) obj2[0];
	        	BigDecimal peso = (BigDecimal) obj2[1];
	        	BigDecimal vol = (BigDecimal) obj2[2];
	        	BigDecimal cant = (BigDecimal) obj2[3];
	        	if(vol.doubleValue()/volP.doubleValue()>peso.doubleValue()/pesoP.doubleValue()){
	        		Double temp = peso.doubleValue()/pesoP.doubleValue();
	        		caben = temp.intValue();
	        	}
	        	else{
	        		Double temp = vol.doubleValue()/volP.doubleValue();
	        		caben = temp.intValue();
	        	}
	        	if(faltan > caben)
	        	{
	        		sqlBodegaProducto.actualizar(pm, idB.longValue(), idP.longValue(), cant.intValue()+caben);
	        		faltan-=caben;
	        	}
	        	else
	        	{
	        		sqlBodegaProducto.actualizar(pm, idB.longValue(), idP.longValue(), cant.intValue()+faltan);
	        		faltan-=caben;
	        		termine=true;
	        	}
	        	
	        }
	        long resp = sqlOrden.entregarOrden(pmf.getPersistenceManager(), id, fecha, calificacion, estado,CantidadR);
	        
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
	

	public PersonaNatural adicionarPersona(String nombre, String correo,Long cedula){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlPersona.adicionar(pm, cedula, nombre, correo);
            tx.commit();
            
            log.trace ("Inserción de la empresa: " + cedula + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new PersonaNatural(cedula, nombre, correo);
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
	public Sucursal adicionarSucursal(String ciudad, String direccion){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
            tx.begin();
            long id = nextval ();
            long tuplasInsertadas = sqlSucursal.adicionarSucursal(pm, id, ciudad, direccion);
            tx.commit();
            
            log.trace ("Inserción de la ciudad: " + id + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Sucursal(id, ciudad, direccion);
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
	public SucursalProducto adicionarSucursalProducto(Long idSucursal,Long idProducto, Double precio, Integer nivelReorden){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
            tx.begin();
            long tuplasInsertadas = sqlSucursalProducto.adicionarSucursalProducto(pm, idSucursal, idProducto, precio, nivelReorden);
            tx.commit();
            
            log.trace ("Inserción de la tupla: " + idSucursal + ";" + ": " + tuplasInsertadas + " tuplas insertadas");
            
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

	public CarritoCompras asignarCarro(Long idCliente,Long idSucursal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
            tx.begin();
            CarritoCompras resp = sqlCarritoCompras.asignarCarrito(pm, nextval(),idCliente,idSucursal);
            tx.commit();
            return resp;
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
	public CarritoCompras quitarCarro(Long idCliente,Long idSucursal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
            tx.begin();
            CarritoCompras resp = sqlCarritoCompras.quitarCarrito(pm, idCliente,idSucursal);
            tx.commit();
            return resp;
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

	public CarritoComprasProducto agregarProducto(Long idProducto,Long idCarrito,Integer cantidad){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
            tx.begin();
            BigDecimal t = (BigDecimal)sqlCarritoCompras.darSucursal(pm, idCarrito);
            List<Object[]> disponible = sqlEstanteProducto.unidadesEnInventario(pm,t.longValue() , idProducto);
            Integer disp = 0;
            //Revisar si existe la cantidad del producto deseado en los estantes, arroja excepcion de lo contrario
            for(Object[] temp : disponible){
            	BigDecimal ayuda = (BigDecimal) temp[1];
            	disp += ayuda.intValue();
            }
            if(cantidad<=disp)
            {
            	abastecerEstante(pm, cantidad, disponible, idProducto);
            	long resp = sqlCarritoComprasProducto.adicionarProducto(pm, idProducto, idCarrito, cantidad);
            	
            }
            
            tx.commit();
            return new CarritoComprasProducto(idCarrito, idProducto, cantidad);
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
	public void quitarProducto(Long idProducto,Long idCarrito){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
            tx.begin();
            Integer cantidad = sqlCarritoComprasProducto.darCantidad(pm, idProducto, idCarrito);
            BigDecimal t = (BigDecimal)sqlCarritoCompras.darSucursal(pm, idCarrito);
            sqlCarritoComprasProducto.quitarProducto(pm, idProducto, idCarrito);
            List<Object[]> reOrdenE = sqlEstante.reOrden(pm, t.longValue(), idProducto);
            boolean salir = false;
            for(int i = 0;i < reOrdenE.size() && !salir;i++)
            {
            	Object[] temp = reOrdenE.get(i);
            	BigDecimal idE = (BigDecimal) temp[0];
            	BigDecimal cant = (BigDecimal) temp[1];
            	BigDecimal pesoP = (BigDecimal) temp[2];
            	BigDecimal volP = (BigDecimal) temp[3];
            	BigDecimal pesoE = (BigDecimal) temp[4];
            	BigDecimal volE = (BigDecimal) temp[5];
                Double dispPeso = (pesoE.doubleValue()-cant.doubleValue()*pesoP.doubleValue())/pesoP.doubleValue();
                Double dispVol = (volE.doubleValue()-cant.doubleValue()*volP.doubleValue())/volP.doubleValue();
                Integer ayuda3 = 0;
                if(dispPeso>dispVol){
                	ayuda3 = dispVol.intValue();
                }
                else{
                	ayuda3 = dispPeso.intValue();
                }
                if(ayuda3>cantidad)
                {
                	sqlEstanteProducto.actualizar(pm, idE.longValue(),idProducto, cant.intValue()+cantidad);
                	salir = true;
                }
                else
                {
                	sqlEstanteProducto.actualizar(pm, idE.longValue(),idProducto, cant.intValue()+ayuda3);
                	cantidad -= ayuda3;
                }
                
            }
            tx.commit();
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
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
	public void devolverCarritos()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            List<Object[]> lista = sqlCarritoCompras.darCarritosAbandonados(pm);
            for(Object[] objeto : lista)
            {
            	
            	BigDecimal idCarrito = (BigDecimal) objeto[0];
            	BigDecimal idProducto = (BigDecimal) objeto[1];
            	quitarProducto(idProducto.longValue(), idCarrito.longValue());
            }
            List<Object> lista2 = sqlCarritoCompras.limpiarCarritosVacios(pm);
            for(Object objeto : lista2)
            {
            	BigDecimal id = (BigDecimal)objeto;
            	sqlCarritoCompras.borrarCarrito(pm, id.longValue());
            }
            tx.commit();
            
            log.trace ("Se recogieron todos los carros abandonados: ");
            
        }
        catch (Exception e)
        {
        	e.printStackTrace();
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
	public void pagarCarrito(Long idCarrito,Date fecha)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
			tx.begin();
			Object[] info = sqlCarritoCompras.darCarrito(pm, idCarrito);
			BigDecimal idCliente = (BigDecimal) info[1];
			BigDecimal idSucursal = (BigDecimal)info[0];
			List<Object[]> lista = sqlCarritoComprasProducto.pagar(pm, idCarrito);
			for(Object[] temp: lista){
				BigDecimal idProducto = (BigDecimal)temp[0];
				BigDecimal cantidad = (BigDecimal) temp[1];
				CompraProducto actual = adicionarCompraProducto(idCliente.longValue(), idSucursal.longValue(), idProducto.longValue(), cantidad.intValue(), fecha,false);
			}
			tx.commit();
        }catch(Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
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
	
	public void abastecerEstante(PersistenceManager pm,Integer cantidad2,List<Object[]> disponible,Long idP)
	{
    	boolean a = false;
    	for(int i = 0;i < disponible.size() && !a;i++){
        	BigDecimal ayuda = (BigDecimal) disponible.get(i)[1];
        	BigDecimal idEstante = (BigDecimal) disponible.get(i)[0];
    		if(cantidad2>ayuda.intValue()){
    			sqlEstanteProducto.actualizar(pm, idEstante.longValue(),idP, 0);
    			cantidad2-=ayuda.intValue();
    		}
    		else{
    			sqlEstanteProducto.actualizar(pm, idEstante.longValue(),idP, ayuda.intValue()-cantidad2);
    			a = true;
    			cantidad2=0;
    		}
    	}
	}

	public long getIdSucursal(String direccion, String ciudad) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
			tx.begin();
			List a = sqlSucursal.getIdSucursal(pm,direccion,ciudad);
			BigDecimal res = (BigDecimal)a.get(0);
			Long resp = res.longValue();
			tx.commit();
			return resp;
        }catch(Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return (long)0;
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
	
	public void borrarTodo(){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
			tx.begin();
			sqlBodega.borrarTodo(pm);
			tx.commit();
        }catch(Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
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
	public List<Bodega> darBodegas(Long idSucursal){
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
			tx.begin();
			List<Object[]> list = sqlBodega.darBodegas(pm, idSucursal);
			tx.commit();
			List<Bodega> lista = new ArrayList<>();
			for(Object[] obj : list){
				BigDecimal id = (BigDecimal)obj[0];
				BigDecimal id_s = (BigDecimal)obj[1];
				BigDecimal peso = (BigDecimal)obj[2];
				BigDecimal vol = (BigDecimal)obj[3];
				String tipo = obj[4].toString();
				Bodega bod = new Bodega(id.longValue(), id_s.longValue(), tipo, vol.doubleValue(), peso.doubleValue());
				lista.add(bod);
			}
			return lista;
        }catch(Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return new ArrayList<>();
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

	public void borrarBodega() {
		// TODO Auto-generated method stub
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
			tx.begin();
			sqlBodega.borrarBodega(pm);
			tx.commit();
        }catch(Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
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
	public List<Object[]> unidadesEnInv(Long idSucursal,Long idProducto) {
		// TODO Auto-generated method stub
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
			tx.begin();
			return sqlBodegaProducto.unidadesEnInventario(pm, idSucursal, idProducto);
        }catch(Exception e)
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
	
	public List<Object[]> unidadesEnInvE(Long idSucursal,Long idProducto) {
		// TODO Auto-generated method stub
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
			tx.begin();
			return sqlEstanteProducto.unidadesEnInventario(pm, idSucursal, idProducto);
        }catch(Exception e)
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

	public List<Estante> darEstantes(Long idSucursal) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
			tx.begin();
			List<Object[]> list = sqlEstante.darEstante(pm, idSucursal);
			tx.commit();
			List<Estante> lista = new ArrayList<>();
			for(Object[] obj : list){
				BigDecimal id = (BigDecimal)obj[0];
				BigDecimal id_s = (BigDecimal)obj[1];
				BigDecimal peso = (BigDecimal)obj[2];
				BigDecimal vol = (BigDecimal)obj[3];
				BigDecimal cant = (BigDecimal) obj[5];
				String tipo = obj[4].toString();
				Estante bod = new Estante(id.longValue(), id_s.longValue(), tipo, vol.doubleValue(), peso.doubleValue(),cant.doubleValue());
				lista.add(bod);
			}
			return lista;
        }catch(Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return new ArrayList<>();
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

	public List<Cliente> darClientes() {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
			tx.begin();
			List<Object[]> list = sqlEmpresa.darClientes(pm);
			tx.commit();
			List<Cliente> lista = new ArrayList<>();
			for(Object[] obj : list){
				BigDecimal id = (BigDecimal)obj[0];
				String nombre = (String)obj[1];
				String correo = (String)obj[2];
				String direccion = (String)obj[3];
				Empresa bod = new Empresa(nombre, correo, id.longValue(), direccion);
				lista.add(bod);
			}
			return lista;
        }catch(Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return new ArrayList<>();
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

	public List<Proveedor> darProveedores() {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
			tx.begin();
			List<Object[]> list = sqlProveedor.darProveedores(pm);
			tx.commit();
			List<Proveedor> lista = new ArrayList<>();
			for(Object[] obj : list){
				BigDecimal id = (BigDecimal)obj[0];
				String nombre = (String)obj[1];
				Proveedor bod = new Proveedor(id.longValue(), nombre);
				lista.add(bod);
			}
			return lista;
        }catch(Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return new ArrayList<>();
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

	public List<Sucursal> darSucursales() {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
			tx.begin();
			List<Object[]> list = sqlSucursal.darSucursales(pm);
			tx.commit();
			List<Sucursal> lista = new ArrayList<>();
			for(Object[] obj : list){
				BigDecimal id = (BigDecimal)obj[0];
				String ciudad = (String)obj[1];
				String direccion = (String)obj[2];
				Sucursal bod = new Sucursal(id.longValue(), ciudad, direccion);
				lista.add(bod);
			}
			return lista;
        }catch(Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return new ArrayList<>();
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
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
		try
        {
			tx.begin();
			List<Object[]> list = sqlOrden.darOrden(pm);
			tx.commit();
			List<OrdenPedido> lista = new ArrayList<>();
			for(Object[] obj : list){
				BigDecimal id = (BigDecimal)obj[0];
				BigDecimal id_prod = (BigDecimal)obj[1];
				BigDecimal id_prov = (BigDecimal)obj[2];
				BigDecimal id_s = (BigDecimal)obj[3];
				BigDecimal cant = (BigDecimal)obj[4];
				BigDecimal precio = (BigDecimal)obj[5];
				Timestamp fecha = (Timestamp) obj[6];
				String estado = (String)obj[9];
				OrdenPedido temp = new OrdenPedido(id.longValue(), id_prov.longValue(), id_s.longValue(), id_prod.longValue(), new Date(fecha.getTime()), cant.intValue(), precio.doubleValue(), null, estado, null, null);
				lista.add(temp);
			}
			
			return lista;
        }catch(Exception e)
        {
        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return new ArrayList<>();
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
	
}
