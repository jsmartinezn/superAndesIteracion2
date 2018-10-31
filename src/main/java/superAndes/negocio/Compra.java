package superAndes.negocio;

import java.util.Date;

public class Compra implements VOCompra{
	
	private Long idCliente;
	
	private Long idSucursal;
	
	private Long id;
	
	private Date fecha;
	
	public Compra(){
		this.id = (long)0;
		this.idCliente=(long)0;
		this.idSucursal=(long)0;
		this.setFecha(new Date());
	}
	
	public Compra(Long id, Long idC, Long idS,Date fecha){
		this.id = id;
		this.idCliente = idC;
		this.idSucursal = idS;
		this.setFecha(fecha);
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}
