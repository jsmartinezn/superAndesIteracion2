package superAndes.negocio;

public class LlegadaProducto implements VOLlegadaProducto{
	
	private Long idProducto;
	
	private Long idOrden;
	
	private Double volumen;
	
	private String unidadDeMedida;
	
	private Integer cantidadRecibida;
	
	private Double calidadRecibida;
	
	public LlegadaProducto(){
		this.idOrden = (long)0;
		this.idProducto = (long)0;
		this.volumen = 0.0;
		this.unidadDeMedida = "cm3";
		this.cantidadRecibida = 0;
		this.calidadRecibida = 0.0;
	}
	
	public LlegadaProducto(Long idP,Long idO,Double volumen,String undMedida,Integer cantidad,Double calidad){
		this.idOrden = idP;
		this.idProducto = idO;
		this.volumen = volumen;
		this.unidadDeMedida = undMedida;
		this.cantidadRecibida = cantidad;
		this.calidadRecibida = calidad;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Long getIdOrden() {
		return idOrden;
	}

	public void setIdOrden(Long idOrden) {
		this.idOrden = idOrden;
	}

	public Double getVolumen() {
		return volumen;
	}

	public void setVolumen(Double volumen) {
		this.volumen = volumen;
	}

	public String getUnidadDeMedida() {
		return unidadDeMedida;
	}

	public void setUnidadDeMedida(String unidadDeMedida) {
		this.unidadDeMedida = unidadDeMedida;
	}

	public Integer getCantidadRecibida() {
		return cantidadRecibida;
	}

	public void setCantidadRecibida(Integer cantidadRecibida) {
		this.cantidadRecibida = cantidadRecibida;
	}

	public Double getCalidadRecibida() {
		return calidadRecibida;
	}

	public void setCalidadRecibida(Double calidadRecibida) {
		this.calidadRecibida = calidadRecibida;
	}
}
