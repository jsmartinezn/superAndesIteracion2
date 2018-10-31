package superAndes.negocio;

public class EstanteProducto implements VOEstanteProducto {

private Long idProducto;
	
	private Long idEstante;
	
	private Integer cantidad;
	
	public EstanteProducto(){
		this.idProducto = (long)0;
		this.idEstante = (long)0;
		this.cantidad = 0;
	}
	
	public EstanteProducto(Long idProducto, Long idEstante,Integer cantidad){
		this.idEstante = idEstante;
		this.idProducto = idProducto;
		this.cantidad = cantidad;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Long getIdEstante() {
		return idEstante;
	}

	public void setIdBodega(Long idBodega) {
		this.idEstante = idBodega;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

}
