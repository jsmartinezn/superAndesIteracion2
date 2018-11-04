package superAndes.negocio;

import java.sql.Date;
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
	 * Logger para escribir la traza de la ejecución
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
	 * 			Métodos
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
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
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
	
	public CompraProducto adicionarCompraProucto(Long idC, Long idS, Long idP,Integer cantidad,Date fecha){
		return pp.adicionarCompraProducto(idC, idS,idP,cantidad, fecha,true);
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
		return pp.adicionarEmpresas(nombre, correo, nit, direccion);
	}
	
	public Estante adicionarEstante(Long idSucursal,String tipoProducto,Double volumen,Double peso,Double nivelReOrden){
		return pp.adicionarEstante(idSucursal,tipoProducto, volumen, peso,nivelReOrden);
	}
	
	public OrdenPedido adicionarOrdenPedido(Long idProveedor,Long idSucursal,Long idProducto,Double precio,Integer cantidad,Date fechaEsperada, String estado){
		return pp.adicionarOrdenPedido(idProveedor, idSucursal, idProducto, precio, cantidad, fechaEsperada, estado);
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
	
	public SucursalProducto adicionarSucursalProducto(Long idSucursal,Long idProducto, Double precio, Integer nivelReorden)
	{
		return pp.adicionarSucursalProducto(idSucursal,idProducto, precio, nivelReorden);
	}
	
	public Sucursal adicionarSucursal(String ciudad, String direccion)
	{
		return pp.adicionarSucursal(ciudad, direccion);
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
	
	public long realizarEntrega(Long id,Date fecha,Double calificacion,String estado,Integer cantidadR){
		return pp.realizarEntrega(id, fecha, calificacion, estado,cantidadR);
	}
	
	public CarritoCompras asignarCarro(Long idCliente,Long idSucursal){
		return pp.asignarCarro(idCliente,idSucursal);
	}
	public CarritoCompras quitarCarro(Long idCliente,Long idSucursal){
		return pp.quitarCarro(idCliente,idSucursal);
	}
	public CarritoComprasProducto asignarProducto(Long idProducto,Long idCarrito,Integer cantidad){
		return pp.agregarProducto(idProducto, idCarrito, cantidad);
	}
	public void quitarProducto(Long idProducto,Long idCarrito)
	{
		pp.quitarProducto(idProducto, idCarrito);
	}
	
	public List<OrdenPedido> darOrdenes(){
		return pp.darOrdenes();
	}	
	public void pagarCarrito(Long idCarrito,Date fecha){
		pp.pagarCarrito(idCarrito, fecha);
	}
	public void recogerCarritos(){
		pp.devolverCarritos();
	}
	
	public long getIdSucursal(String direccion, String ciudad){
		return pp.getIdSucursal(direccion,ciudad);
	}

	public void borrar(){
		pp.borrarTodo();
	}
	
	public List<Bodega> darBodegas(Long idSucursal)
	{
		return pp.darBodegas(idSucursal);
	}
	public void borrarBodega(){
		pp.borrarBodega();
	}
	public List<Object[]> unidadesEnInv(Long idSucursal,Long idProducto) {
		return pp.unidadesEnInv(idSucursal, idProducto);
	}
	
	public List<Object[]> unidadesEnInvE(Long idSucursal,Long idProducto) {
		return pp.unidadesEnInvE(idSucursal, idProducto);
	}

	public VOEstanteProducto adicionarEstanteProducto(Long idSucursal, Long codigoDeBarras, int cantidad) {
		return pp.adicionarEstanteProducto(idSucursal, codigoDeBarras, cantidad);
	}

	public List<Estante> darEstantes(Long idSucursal) {
		return pp.darEstantes(idSucursal);
	}

	public List<Cliente> darClientes() {
		return pp.darClientes();
	}

	public List<Proveedor> darProveedores() {
		return pp.darProveedores();
	}

	public List<Sucursal> darSucursales() {
		return pp.darSucursales();
	}
}
