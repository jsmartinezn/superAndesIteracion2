package superAndes.negocio;

public class ProductoProveedor implements VOProductoProveedor{
	
	private Long idProducto;
	
	private Long idProveedor;
	
	private Double precio;
	
	private Double indiceCalidad;
	
	public ProductoProveedor(){
		this.idProducto = (long) 0;
		this.idProveedor = (long) 0;
		this.precio = 0.0;
		this.indiceCalidad = 0.0;
	}
	
	public ProductoProveedor(Long idproducto,Long idproveedor,Double precio,Double indice){
		this.idProducto = idproducto;
		this.idProveedor = idproveedor;
		this.precio = precio;
		this.indiceCalidad = indice;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Double getIndiceCalidad() {
		return indiceCalidad;
	}

	public void setIndiceCalidad(Double indiceCalidad) {
		this.indiceCalidad = indiceCalidad;
	}
}
