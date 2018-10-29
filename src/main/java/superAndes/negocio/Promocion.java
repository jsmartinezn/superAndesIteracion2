package superAndes.negocio;

import java.util.Date;

public class Promocion implements VOPromocion{
	
	private Long idSucursal;
	
	private Long idProducto;
	
	private Date fechaInicio;
	
	private Date fechaFin;
	
	private String condicion;
	
	public Promocion(){
		this.idProducto = (long) 0;
		this.idSucursal = (long) 0;
		this.fechaFin = new Date();
		this.fechaInicio = new Date();
		this.condicion = "";
	}
	
	public Promocion(Long idS,Long idP,Date inicio,Date fin,String condicion){
		this.idProducto = idP;
		this.idSucursal = idS;
		this.fechaInicio = inicio;
		this.fechaFin = fin;
		this.condicion = condicion;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}
}
