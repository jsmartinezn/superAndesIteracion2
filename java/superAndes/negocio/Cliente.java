package superAndes.negocio;

public class Cliente{
	
	private Long id;
	
	private String nombre;
	
	private String correo;
	
	public Cliente(){
		this.id = (long)0;
		this.nombre = "";
		this.correo = "";
	}
	
	public Cliente(Long id, String nombre, String correo)
	{
		this.id = id;
		this.nombre = nombre;
		this.correo = correo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}
}
