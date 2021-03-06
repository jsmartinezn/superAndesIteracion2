package superAndes.negocio;

public class CompraProducto  implements VOCompraProducto{

	private Long idProducto;
	
	private Long idFactura;
	
	private Integer cantidad;
	
	private Double precio;

	public CompraProducto(){
		this.idFactura = (long) 0;
		this.idProducto = (long) 0;
		this.cantidad = 0;
		this.precio =0.0;
	}
	
	public CompraProducto(Long idP,Long idC,Integer cantidad,Double precio){
		this.idFactura = idC;
		this.idProducto = idP;
		this.cantidad = cantidad;
		this.precio = precio;
	}
	
	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Long idFactura) {
		this.idFactura = idFactura;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}
}
