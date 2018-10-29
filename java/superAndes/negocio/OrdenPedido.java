package superAndes.negocio;

import java.util.Date;

public class OrdenPedido implements VOOrdenPedido{
	
	public final static String E = "ENTREGADA";
	
	public final static String P = "PENDIENTE";
	
	private Long id;
		
	private Long idProveedor;
	
	private Long idSucursal;
	
	private Date fechaEspEntrega;
	
	private Date fechaEntrega;

	private Double calificacion;
	
	private String estado;
	
	public OrdenPedido(){
		this.id = (long) 0;
		this.idProveedor = (long) 0;
		this.idSucursal = (long) 0;
		this.fechaEntrega = new Date();
		this.fechaEspEntrega = new Date();
		this.calificacion = 0.0;
		this.estado = P;
	}
	
	public OrdenPedido(Long id,Long idProveedor,Long idS,Date fechaEsperada,Date fechaReal,String estado){
		this.id = id;
		this.idProveedor = idProveedor;
		this.idSucursal = idS;
		this.fechaEntrega = fechaReal;
		this.fechaEspEntrega = fechaEsperada;
		this.estado = estado;
	}	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public Date getFechaEspEntrega() {
		return fechaEspEntrega;
	}

	public void setFechaEspEntrega(Date fechaEspEntrega) {
		this.fechaEspEntrega = fechaEspEntrega;
	}

	public Date getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public Double getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(Double calificacion) {
		this.calificacion = calificacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
