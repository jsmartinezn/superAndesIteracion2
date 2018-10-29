package superAndes.negocio;

public interface VOEstante {

	public Long getIdSucursal();
	
	public String getTipoProducto();
	
	public Double getVolumen();
	
	public String getUnidadVolumen();
	
	public Double getPeso();
	
	public String getUnidadPeso();
	
	public Double getPesoActual();
	
	public Double getVolumenActual();
	
	public Integer getCantidad();
	
	public Integer getNivelDeReOrden();
}
