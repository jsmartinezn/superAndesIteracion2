package superAndes.negocio;

public class Estante implements VOEstante {
	
	private Long id;
	
	private Long idSucursal;
	
	private String tipoProducto;
	
	private Double volumen;
	
	private Double peso;
	
	private Double nivelDeReOrden;
	
	public Estante(){
		this.id = (long)0;
		this.idSucursal = (long) 0;
		this.tipoProducto = "";
		this.volumen = 0.0;
		this.volumen = 0.0;
		this.nivelDeReOrden = 0.0;
	}
	
	public Estante(Long id,Long idSucursal, String tipoProducto, Double volumen, Double peso,Double nivel)
	{
		this.id = id;
		this.idSucursal = idSucursal;
		this.tipoProducto = tipoProducto;
		this.volumen = volumen;
		this.peso = peso;
		this.nivelDeReOrden = nivel;
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


	public Double getNivelDeReOrden() {
		return nivelDeReOrden;
	}

	public void setNivelDeReOrden(Double nivelDeReOrden) {
		this.nivelDeReOrden = nivelDeReOrden;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
