package superAndes.negocio;

public class BodegaProducto implements VOBodegaProducto
{
	
	private Long idProducto;
	
	private Long idBodega;
	
	private Integer cantidad;
	
	public BodegaProducto(){
		this.idProducto = (long)0;
		this.idBodega = (long)0;
		this.cantidad = 0;
	}
	
	public BodegaProducto(Long idProducto, Long idBodega,Integer cantidad){
		this.idBodega = idBodega;
		this.idProducto = idProducto;
		this.cantidad = cantidad;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Long getIdBodega() {
		return idBodega;
	}

	public void setIdBodega(Long idBodega) {
		this.idBodega = idBodega;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

}
