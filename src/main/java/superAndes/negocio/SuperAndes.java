package superAndes.negocio;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

import superAndes.persistencia.PersistenciaSuperAndes;


public class SuperAndes {

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecuci�n
	 */
	private static Logger log = Logger.getLogger(SuperAndes.class.getName());
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia
	 */
	private PersistenciaSuperAndes pp;
	
	/* ****************************************************************
	 * 			M�todos
	 *****************************************************************/
	/**
	 * El constructor por defecto
	 */
	public SuperAndes ()
	{
		pp = PersistenciaSuperAndes.getInstance ();
	}
	
	/**
	 * El constructor qye recibe los nombres de las tablas en tableConfig
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad de persistencia
	 */
	public SuperAndes (JsonObject tableConfig)
	{
		pp = PersistenciaSuperAndes.getInstance (tableConfig);
	}
	
	/**
	 * Cierra la conexi�n con la base de datos (Unidad de persistencia)
	 */
	public void cerrarUnidadPersistencia ()
	{
		pp.cerrarUnidadPersistencia ();
	}
	
	public Bodega adicionarBodega(Long idSucursal,String tipoProducto,Double volumen,Double peso){
		return pp.adicionarBodega(idSucursal,tipoProducto, volumen, peso);
	}
	
	public String indiceDeOcupacionPesoBodega(Long idSucursal){
		return pp.darIndiceDeOcupacionBodegaPeso(idSucursal);
	}
	
	public String indiceDeOcupacionVolumenBodega(Long idSucursal){
		return pp.darIndiceDeOcupacionBodegaVolumen(idSucursal);
	}
	
	public String indiceDeOcupacionPesoEstante(Long idSucursal){
		return pp.darIndiceDeOcupacionEstantePeso(idSucursal);
	}
	
	public String indiceDeOcupacionVolumenEstante(Long idSucursal){
		return pp.darIndiceDeOcupacionEstanteVolumen(idSucursal);
	}
	
	public BodegaProducto adicionarBodegaProducto(Long idBodega,Long idProducto,Integer cantidad){
		return pp.adicionarBodegaProducto(idBodega, idProducto, cantidad);
	}
	
	public Compra adicionarCompra(Long idC, Long idS, Long idP,Integer cantidad,Date fecha){
		return pp.adicionarCompra(idC, idS,idP,cantidad, fecha);
	}
	
	public List<VOCompra> darComprasFechas(Date fechaInicio,Date fechaFin){
		List<Compra> nueva = pp.darComprasFechas(fechaInicio, fechaFin);
		List<VOCompra> resp = new LinkedList<>();
		for(Compra compra : nueva)
			resp.add(compra);
		return resp;
	}
	
	public List<VOCompra> darComprasPorUsuario(Date fechaInicio,Date fechaFin,Long idCliente){
		List<Compra> nueva = pp.darComprasPorUsuario(fechaInicio, fechaFin,idCliente);
		List<VOCompra> resp = new LinkedList<>();
		for(Compra compra : nueva)
			resp.add(compra);
		return resp;
	}
	//
	public Empresa adicionarEmpresa(String nombre, String correo, Long nit,String direccion){
		return pp.adicionarEmpresa(nombre, correo, nit, direccion);
	}
	
	public Estante adicionarEstante(Long idSucursal,String tipoProducto,Double volumen,Double peso,Double nivelReOrden){
		return pp.adicionarEstante(idSucursal,tipoProducto, volumen, peso,nivelReOrden);
	}
	
	public OrdenPedido adicionarOrdenPedido(Long idProveedor,Long idSucursal,Date fechaEsperada, String estado){
		return pp.adicionarOrdenPedido(idProveedor, idSucursal, fechaEsperada, estado);
	}
	
	public PersonaNatural adicionarPersona(String nombre, String correo, Long cedula){
		return pp.adicionarPersona(nombre, correo, cedula);
	}
	
	public Producto adicionarProducto(Long codigoB, String nombre, String marca, String presentacion, Double precioUM, String UM,Integer cantdadP,Double volumen, Double peso, String catrgoria)
	{
		return pp.adicionarProducto(codigoB, nombre, marca, presentacion, precioUM, UM, cantdadP, volumen, peso, catrgoria);
	}
	
	public LlegadaProducto adicionarLlegadaProducto( Long idProducto, Long idOrden, Double volumen, String uM, Integer cantidadR, Double calidadR)
	{
		return pp.adicionarLlegadaProducto(idProducto, idOrden, volumen, uM, cantidadR, calidadR);
	}
	
	public ProductoProveedor adicionarProductoProveedor(Long idProducto, Long idProvedor, Double precio, Double indiceCalidad){
		return pp.adicionarProductoProveedor(idProducto, idProvedor, precio, indiceCalidad);
	}
	
	public Promocion adicionarPromocion(Long idSucursal, Long idProducto, Date fechaInicio, Date fechaFin, String condicion)
	{
		return pp.adicionarPromocion(idSucursal, idProducto, fechaInicio, fechaFin, condicion);
	}
	
	public Proveedor adicionarProveedor(Long nit, String nombre)
	{
		return pp.adicionarProveedor(nit, nombre);
	}
	
	public SucursalProducto adicionarSucursalProducto(Long idProducto, Double precio, Integer nivelReorden)
	{
		return pp.adicionarSucursalProducto(idProducto, precio, nivelReorden);
	}
	
	public List<VOPromocion> darPromocionesMasPopulares()
	{
		List<Promocion> temp = pp.darPromocionesMasPopulares();
		List<VOPromocion> resp = new LinkedList<>();
		for(Promocion nuevo: temp)
			resp.add(nuevo);
		return resp;
	}
	
	public List<VOProducto> darProductoPorCondicion(String condicion)
	{
		List<Producto> temp = pp.darProductoPorCondicion(condicion);
		List<VOProducto> resp = new LinkedList<>();
		for(Producto nuevo: temp)
			resp.add(nuevo);
		return resp;
	}
	
	public long realizarEntrega(Long id,Date fecha,Double calificacion,String estado){
		return pp.realizarEntrega(id, fecha, calificacion, estado);
	}
	
	public List<VOOrdenPedido> darOrdenes(){
		List<OrdenPedido> temp = pp.darOrdenes();
		List<VOOrdenPedido> resp = new LinkedList<>();
		for(OrdenPedido nuevo : temp)
			resp.add(nuevo);
		return resp;
	}	
	

}
