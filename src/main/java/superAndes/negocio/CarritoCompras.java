package superAndes.negocio;

public class CarritoCompras implements VOCarritoCompras{

	private Long idCarrito;
	
	private Long idCliente;
	
	private Long idSucursal;

	public CarritoCompras(){
		this.idCarrito = (long)0;
		this.idCliente = (long)0;
		this.setIdSucursal((long)0);
	}
	
	public CarritoCompras(Long idCarro,Long idCliente,Long idSucursal){
		this.idCarrito = idCarro;
		this.idCliente = idCliente;
		this.setIdSucursal(idSucursal);
	}
	
	public Long getIdCarrito() {
		return idCarrito;
	}

	public void setIdCarrito(Long idCarrito) {
		this.idCarrito = idCarrito;
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
}
