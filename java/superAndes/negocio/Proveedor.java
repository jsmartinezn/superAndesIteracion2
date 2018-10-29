package superAndes.negocio;

public class Proveedor implements VOProveedor{
		
	private Long nit;
	
	private String nombre;
	
	public Proveedor(){
		this.nit = (long) 0;
		this.nombre = "";
	}
	
	public Proveedor(Long  nit,String nombre){
		this.nit = nit;
		this.nombre = nombre;
	}

	public Long getNit() {
		return nit;
	}

	public void setNit(Long nit) {
		this.nit = nit;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
