package superAndes.negocio;

public class Empresa extends Cliente implements VOEmpresa{
	
	private Long nit;
	
	private String direccion;
	
	public Empresa(){
		super();
		this.nit = (long)0;
		this.direccion ="";
	}

	public Empresa(Long id, String nombre, String correo,Long nit,String direccion){
		super(id,nombre,correo);
		this.nit = nit;
		this.direccion = direccion;
	}
		
	public Long getNit() {
		return nit;
	}

	public void setNit(Long nit) {
		this.nit = nit;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
}
