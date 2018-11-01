package superAndes.negocio;

public class CarritoComprasProducto implements VOCarritoComprasProducto{

	public Long idCarrito;
	
	public Long idProducto;
	
	public Integer cantidad;

	public CarritoComprasProducto(){
		this.idCarrito = (long)0;
		this.idProducto = (long)0;
		this.cantidad = 0;
	}
	
	public CarritoComprasProducto(Long idC,Long idP,Integer cantidad){
		this.idCarrito = idC;
		this.idProducto = idP;
		this.cantidad = cantidad;
	}
	
	public Long getIdCarrito() {
		return idCarrito;
	}

	public void setIdCarrito(Long idCarrito) {
		this.idCarrito = idCarrito;
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
}
