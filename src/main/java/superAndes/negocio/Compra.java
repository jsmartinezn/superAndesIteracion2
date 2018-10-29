package superAndes.negocio;

import java.util.Date;

public class Compra implements VOCompra{
	
	private Long idCliente;
	
	private Long idSucursal;
	
	private Long idProducto;
	
	private Integer cantidad;
	
	private Boolean promocion;
	
	private Long id;
	
	private Double precio;
	
	private Date fecha;
	
	public Compra(){
		this.id = (long)0;
		this.idCliente=(long)0;
		this.idProducto=(long)0;
		this.idSucursal=(long)0;
		this.cantidad = 0;
		this.promocion = false;
		this.setPrecio(0.0);
		this.setFecha(new Date());
	}
	
	public Compra(Long id, Long idC, Long idP, Long idS, Integer cantidad, Boolean promocion,Double precio,Date fecha){
		this.id = id;
		this.idCliente = idC;
		this.idSucursal = idS;
		this.idProducto = idP;
		this.cantidad = cantidad;
		this.promocion = promocion;
		this.setPrecio(precio);
		this.setFecha(fecha);
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Boolean getPromocion() {
		return promocion;
	}

	public void setPromocion(Boolean promocion) {
		this.promocion = promocion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}
