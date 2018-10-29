package superAndes.negocio;

import java.util.Date;

public interface VOPromocion {

	public Long getIdSucursal() ;
	
	public Long getIdProducto();
	
	public Date getFechaInicio();
	
	public Date getFechaFin();
	
	public String getCondicion();
}
