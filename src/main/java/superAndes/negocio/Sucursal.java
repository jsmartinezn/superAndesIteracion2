package superAndes.negocio;

public class Sucursal implements VOSucursal{
	
	private Long id;
	
	private String ciudad;

	private String direccion;
	
	public Sucursal(){
		this.id = (long) 0;
		this.ciudad = "";
		this.direccion = "";
	}
	
	public Sucursal(Long id,String ciudad,String direccion){
		this.id = id;
		this.ciudad = ciudad;
		this.direccion = direccion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
}
