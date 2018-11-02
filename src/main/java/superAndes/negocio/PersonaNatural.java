package superAndes.negocio;

public class PersonaNatural extends Cliente implements VOPersonaNatural {
	private Long cedula;

	public PersonaNatural(){
		super();
		this.cedula = (long)0;
	}
	
	public PersonaNatural(Long cedula, String nombre, String correo){
		super(nombre,correo);
		this.cedula = cedula;
	}
		
	public Long getCedula() {
		return cedula;
	}

	public void setCedula(Long cedula) {
		this.cedula = cedula;
	}
}
