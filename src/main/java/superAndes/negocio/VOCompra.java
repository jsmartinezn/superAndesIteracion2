package superAndes.negocio;

import java.util.Date;

public interface VOCompra {
	
	public Long getIdCliente();
	
	public Long getIdSucursal();
	
	public Long getIdProducto();
	
	public Integer getCantidad();
	
	public Boolean getPromocion();
	
	public Long getId();
	
	public Double getPrecio();
	
	public Date getFecha();
}