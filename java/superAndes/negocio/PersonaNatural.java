package superAndes.negocio;

public class PersonaNatural extends Cliente implements VOPersonaNatural {
	private Long cedula;

	public PersonaNatural(){
		super();
		this.cedula = (long)0;
	}
	
	public PersonaNatural(Long id, String nombre, String correo,Long cedula){
		super(id,nombre,correo);
		this.cedula = cedula;
	}
		
	public Long getCedula() {
		return cedula;
	}

	public void setCedula(Long cedula) {
		this.cedula = cedula;
	}
}
