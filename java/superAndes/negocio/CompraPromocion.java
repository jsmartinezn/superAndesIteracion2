package superAndes.negocio;

public class CompraPromocion implements VOCompraPromocion {
	
	private Long idCompra;
	
	private Long idPromocion;

	public CompraPromocion()
	{
		this.idCompra = (long) 0;
		this.idPromocion = (long) 0;
	}
	
	public CompraPromocion(Long idCompra, Long idPromocion) {
		this.idCompra = idCompra;
		this.idPromocion = idPromocion;
	}

	public Long getIdCompra() {
		return idCompra;
	}

	public Long getIdPromocion() {
		return idPromocion;
	}

	public void setIdCompra(Long idCompra) {
		this.idCompra = idCompra;
	}

	public void setIdPromocion(Long idPromocion) {
		this.idPromocion = idPromocion;
	}
	
	

}
