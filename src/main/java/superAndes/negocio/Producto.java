package superAndes.negocio;

public class Producto implements VOProducto {
	

	private Long codigoDeBarras;
	
	private String marca;
	
	private String nombre;
	
	private String presentacion;
	
	private Double precioUnidadDeMedida;
	
	private String unidadDeMedida;
	
	private Integer cantidadPresentacion;
	
	private Double volumen;
	
	private Double peso;
	
	private String categoria;
	

	public Producto(){
		this.marca = "";
		this.nombre = "";
		this.presentacion = "";
		this.setPrecioUnidadDeMedida(0.0);
		this.unidadDeMedida = "";
		this.cantidadPresentacion = 0;
		this.volumen = 0.0;
		this.peso = 0.0;
		this.codigoDeBarras=(long) 0;
		this.categoria = "";
	}
	
	public Producto(Long codigoBarras,String marca,String nombre,String presentacion, Double precioUnidadDeMedida, String unidadDeMedida, Integer cantidadPresentacion,
			Double volumen,Double peso,String categoria){
		this.marca = marca;
		this.nombre = nombre;
		this.presentacion = presentacion;
		this.setPrecioUnidadDeMedida(precioUnidadDeMedida);
		this.unidadDeMedida = unidadDeMedida;
		this.cantidadPresentacion = cantidadPresentacion;
		this.volumen = volumen;
		this.peso = peso;
		this.codigoDeBarras = codigoBarras;
		this.categoria = categoria;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPresentacion() {
		return presentacion;
	}

	public void setPresentacion(String presentacion) {
		this.presentacion = presentacion;
	}

	public String getUnidadDeMedida() {
		return unidadDeMedida;
	}

	public void setUnidadDeMedida(String unidadDeMedida) {
		this.unidadDeMedida = unidadDeMedida;
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

	public Long getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(Long codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}


	public Double getPrecioUnidadDeMedida() {
		return precioUnidadDeMedida;
	}

	public void setPrecioUnidadDeMedida(Double precioUnidadDeMedida) {
		this.precioUnidadDeMedida = precioUnidadDeMedida;
	}

	public Integer getCantidadPresentacion() {
		return cantidadPresentacion;
	}

	public void setCantidadPresentacion(Integer cantidadPresentacion) {
		this.cantidadPresentacion = cantidadPresentacion;
	}


}
