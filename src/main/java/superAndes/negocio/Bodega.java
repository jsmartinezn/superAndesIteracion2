package superAndes.negocio;

public class Bodega implements VOBodega {
	
	private Long id;
	
	private Long idSucursal;
	
	private String tipoProducto;
	
	private Double volumen;
	
	private Double peso;
	
	public Bodega(){
		this.id = (long)0;
		this.idSucursal = (long) 0;
		this.tipoProducto = "";
		this.volumen = 0.0;
		this.peso = 0.0;
	}
	
	public Bodega(Long id, Long idSucrsal,String tipoProducto, Double volumen, Double peso)
	{
		this.id = idSucrsal;
		this.idSucursal = id;
		this.tipoProducto = tipoProducto;
		this.volumen = volumen;
		this.peso = peso;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public String getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	public Double getVolumen() {
		return volumen;
	}

	public void setVolumen(Double volumen) {
		this.volumen = volumen;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
