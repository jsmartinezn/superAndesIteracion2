package superAndes.interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import superAndes.negocio.OrdenPedido;
import superAndes.negocio.Proveedor;
import superAndes.negocio.SucursalProducto;
import superAndes.negocio.SuperAndes;
import superAndes.negocio.VOBodega;
import superAndes.negocio.VOBodegaProducto;
import superAndes.negocio.VOCarritoCompras;
import superAndes.negocio.VOCarritoComprasProducto;
import superAndes.negocio.VOCompra;
import superAndes.negocio.VOCompraProducto;
import superAndes.negocio.VOEmpresa;
import superAndes.negocio.VOEstante;
import superAndes.negocio.VOOrdenPedido;
import superAndes.negocio.VOPersonaNatural;
import superAndes.negocio.VOProducto;
import superAndes.negocio.VOProductoProveedor;
import superAndes.negocio.VOProveedor;
import superAndes.negocio.VOSucursal;
import superAndes.negocio.VOSucursalProducto;

/**
 * Clase principal de la interfaz
 * @author Germán Bravo
 */
@SuppressWarnings("serial")


public class InterfazSuperandes  extends JFrame implements ActionListener{

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazSuperandes.class.getName());
	
	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfig.json"; 
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
    /**
     * Asociación a la clase principal del negocio.
     */
    private SuperAndes superandes;
    
	/* ****************************************************************
	 * 			Atributos de interfaz
	 *****************************************************************/
    /**
     * Objeto JSON con la configuración de interfaz de la app.
     */
    private JsonObject guiConfig;
    
    /**
     * Panel de despliegue de interacción para los requerimientos
     */
    private PanelDatos panelDatos;
    
    /**
     * Menú de la aplicación
     */
    private JMenuBar menuBar;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
    /**
     * Construye la ventana principal de la aplicación. <br>
     * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
     */
    public InterfazSuperandes()
    {
        // Carga la configuración de la interfaz desde un archivo JSON
        guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);
        
        // Configura la apariencia del frame que contiene la interfaz gráfica
        configurarFrame ( );
        if (guiConfig != null) 	   
        {
     	   crearMenu( guiConfig.getAsJsonArray("menuBar") );
        }
        
        tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
        superandes = new SuperAndes (tableConfig);
        
    	String path = guiConfig.get("bannerPath").getAsString();
        panelDatos = new PanelDatos ( );

        setLayout (new BorderLayout());
        add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
        add( panelDatos, BorderLayout.CENTER );        
    }
    
	/* ****************************************************************
	 * 			Métodos de configuración de la interfaz
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicació, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String tipo, String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración válido: " + tipo);
		} 
		catch (Exception e)
		{
//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }
    
    /**
     * Método para configurar el frame principal de la aplicación
     */
    private void configurarFrame(  )
    {
    	int alto = 0;
    	int ancho = 0;
    	String titulo = "";	
    	
    	if ( guiConfig == null )
    	{
    		log.info ( "Se aplica configuración por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
    	}
    	else
    	{
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
    		titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
    	}
    	
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocation (50,50);
        setResizable( true );
        setBackground( Color.WHITE );

        setTitle( titulo );
		setSize ( ancho, alto);        
    }

    /**
     * Método para crear el menú de la aplicación con base em el objeto JSON leído
     * Genera una barra de menú y los menús con sus respectivas opciones
     * @param jsonMenu - Arreglo Json con los menùs deseados
     */
    private void crearMenu(  JsonArray jsonMenu )
    {    	
    	// Creación de la barra de menús
        menuBar = new JMenuBar();       
        for (JsonElement men : jsonMenu)
        {
        	// Creación de cada uno de los menús
        	JsonObject jom = men.getAsJsonObject(); 

        	String menuTitle = jom.get("menuTitle").getAsString();        	
        	JsonArray opciones = jom.getAsJsonArray("options");
        	
        	JMenu menu = new JMenu( menuTitle);
        	
        	for (JsonElement op : opciones)
        	{       	
        		// Creación de cada una de las opciones del menú
        		JsonObject jo = op.getAsJsonObject(); 
        		String lb =   jo.get("label").getAsString();
        		String event = jo.get("event").getAsString();
        		
        		JMenuItem mItem = new JMenuItem( lb );
        		mItem.addActionListener( this );
        		mItem.setActionCommand(event);
        		
        		menu.add(mItem);
        	}       
        	menuBar.add( menu );
        }        
        setJMenuBar ( menuBar );	
    }
    
	/* ****************************************************************
	 * 			CRUD de TipoBebida
	 *****************************************************************/
    /**
     * Adiciona un tipo de bebida con la información dada por el usuario
     * Se crea una nueva tupla de tipoBebida en la base de datos, si un tipo de bebida con ese nombre no existía
     */
    public void adicionarBodega( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog(this,"Id de la sucursal", "Adicionar Bodega",JOptionPane.QUESTION_MESSAGE);
    		Long idSucursal = Long.valueOf(id);
    		String tipoProducto = JOptionPane.showInputDialog (this, "Tipo del producto", "Adicionar tipo de bebida", JOptionPane.QUESTION_MESSAGE);
    		String volumen = JOptionPane.showInputDialog(this, "Capacidad de volumen total en cm3", "Adicionar tipo de bebida", JOptionPane.QUESTION_MESSAGE);
    		String peso = JOptionPane.showInputDialog(this, "Capacidad de peso total en kg", "Adicionar tipo de bebida", JOptionPane.QUESTION_MESSAGE);
    		if (id != null)
    		{
        		VOBodega tb = superandes.adicionarBodega(idSucursal, tipoProducto, Double.valueOf(volumen), Double.valueOf(peso));
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un tipo de bodega de: " + tipoProducto);
        		}
        		String resultado = "En adicionarBodega\n\n";
        		resultado += "Bodega adicionada exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    /**
     * Adiciona un tipo de bebida con la información dada por el usuario
     * Se crea una nueva tupla de tipoBebida en la base de datos, si un tipo de bebida con ese nombre no existía
     */
    public void adicionarCompra( )
    {
    	try 
    	{
    		String idS = JOptionPane.showInputDialog(this,"Id de la sucursal", "Adicionar Compra",JOptionPane.QUESTION_MESSAGE);
    		String idC = JOptionPane.showInputDialog(this,"Id del cliente", "Adicionar Compra",JOptionPane.QUESTION_MESSAGE);
    		String idP = JOptionPane.showInputDialog(this,"Id del producto", "Adicionar Compra",JOptionPane.QUESTION_MESSAGE);
    		String cantidad = JOptionPane.showInputDialog(this, "Cantidad a comprar", "Adicionar Compra", JOptionPane.QUESTION_MESSAGE);
    		if (idS != null)
    		{
        		VOCompraProducto tb = superandes.adicionarCompraProucto(Long.valueOf(idC), Long.valueOf(idS), Long.valueOf(idP),Integer.valueOf(cantidad), new Date(System.currentTimeMillis()));
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo realzar la compra de: " + idC);
        		}
        		String resultado = "\n";
        		resultado += "Compra realizada exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }  
    /**
     * Adiciona un tipo de bebida con la información dada por el usuario
     * Se crea una nueva tupla de tipoBebida en la base de datos, si un tipo de bebida con ese nombre no existía
     */
    public void indiceDeOcupacionPesoBodega( )
    {
    	try 
    	{
    		String idS = JOptionPane.showInputDialog(this,"Id de la sucursal", "Indice de ocupacion",JOptionPane.QUESTION_MESSAGE);
       		String tb = superandes.indiceDeOcupacionPesoBodega(Long.valueOf(idS));
       		if (tb == null)
       		{
       			throw new Exception ();
       		}
       		String resultado = "\n";
       		resultado += tb;
   			resultado += "\n Operación terminada";
   			panelDatos.actualizarInterfaz(resultado);
  		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    } 
    public void indiceDeOcupacionVolumenBodega( )
    {
    	try 
    	{
    		String idS = JOptionPane.showInputDialog(this,"Id de la sucursal", "Indice de ocupacion",JOptionPane.QUESTION_MESSAGE);
       		String tb = superandes.indiceDeOcupacionVolumenBodega(Long.valueOf(idS));
       		if (tb == null)
       		{
       			throw new Exception ();
       		}
       		String resultado = "\n";
       		resultado += tb;
   			resultado += "\n Operación terminada";
   			panelDatos.actualizarInterfaz(resultado);
  		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    /**
     * Adiciona un tipo de bebida con la información dada por el usuario
     * Se crea una nueva tupla de tipoBebida en la base de datos, si un tipo de bebida con ese nombre no existía
     */
    public void indiceDeOcupacionPesoEstante( )
    {
    	try 
    	{
    		String idS = JOptionPane.showInputDialog(this,"Id de la sucursal", "Indice de ocupacion",JOptionPane.QUESTION_MESSAGE);
       		String tb = superandes.indiceDeOcupacionPesoEstante(Long.valueOf(idS));
       		if (tb == null)
       		{
       			throw new Exception ();
       		}
       		String resultado = "\n";
       		resultado += tb;
   			resultado += "\n Operación terminada";
   			panelDatos.actualizarInterfaz(resultado);
  		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    } 
    public void indiceDeOcupacionVolumenEstante( )
    {
    	try 
    	{
    		String idS = JOptionPane.showInputDialog(this,"Id de la sucursal", "Indice de ocupacion",JOptionPane.QUESTION_MESSAGE);
       		String tb = superandes.indiceDeOcupacionVolumenEstante(Long.valueOf(idS));
       		if (tb == null)
       		{
       			throw new Exception ();
       		}
       		String resultado = "\n";
       		resultado += tb;
   			resultado += "\n Operación terminada";
   			panelDatos.actualizarInterfaz(resultado);
  		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    /**
     * Adiciona un tipo de bebida con la información dada por el usuario
     * Se crea una nueva tupla de tipoBebida en la base de datos, si un tipo de bebida con ese nombre no existía
     */
    public void adicionarEstante( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog(this,"Id de la sucursal", "Adicionar Estante",JOptionPane.QUESTION_MESSAGE);
    		Long idSucursal = Long.valueOf(id);
    		String tipoProducto = JOptionPane.showInputDialog (this, "Tipo del producto", "Adicionar Estante", JOptionPane.QUESTION_MESSAGE);
    		String volumen = JOptionPane.showInputDialog(this, "Capacidad de volumen total en cm3", "Adicionar Estante", JOptionPane.QUESTION_MESSAGE);
    		String peso = JOptionPane.showInputDialog(this, "Capacidad de peso total en kg", "Adicionar Estante", JOptionPane.QUESTION_MESSAGE);
    		String nivel = JOptionPane.showInputDialog(this, "Porcentaje de reorden", "Adicionar Estante", JOptionPane.QUESTION_MESSAGE);
    		if (id != null)
    		{
        		VOEstante tb = superandes.adicionarEstante(idSucursal, tipoProducto, Double.valueOf(volumen), Double.valueOf(peso),Double.valueOf(nivel));
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un tipo de bodega de: " + tipoProducto);
        		}
        		String resultado = "En adicionar Estante\n\n";
        		resultado += "Estante adicionada exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
  
    /**
     * Adiciona un tipo de bebida con la información dada por el usuario
     * Se crea una nueva tupla de tipoBebida en la base de datos, si un tipo de bebida con ese nombre no existía
     */
    public void adicionarProveedor( )
    {
    	try 
    	{
    		String nit = JOptionPane.showInputDialog(this,"NIT del proveedor", "Adicionar Proveedor",JOptionPane.QUESTION_MESSAGE);
    		String nombre = JOptionPane.showInputDialog(this, "Nombre del proveedor", "Adicionar Proveedor", JOptionPane.QUESTION_MESSAGE);
    		if (nit != null && nombre!=null)
    		{
    			VOProveedor tb = superandes.adicionarProveedor(Long.valueOf(nit), nombre);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo adicionar el proveedor: " + nombre);
        		}
        		String resultado = "En adicionar Proveedor\n\n";
        		resultado += "Proveedor adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void adicionarSucursal( )
    {
    	try 
    	{
    		String direccion = JOptionPane.showInputDialog(this,"NIT del proveedor", "Adicionar Sucursal",JOptionPane.QUESTION_MESSAGE);
    		String ciudad = JOptionPane.showInputDialog(this, "Nombre del proveedor", "Adicionar Sucursal", JOptionPane.QUESTION_MESSAGE);
    		if (ciudad != null && direccion!=null)
    		{
    			VOSucursal tb = superandes.adicionarSucursal(ciudad, direccion);
    			if(tb==null){
        			throw new Exception ("No se pudo adicionar la sucursal de la ciudad: " + ciudad);
        		}
        		String resultado = "En adicionar Sucursal\n\n";
        		resultado += "Sucursal adicionada exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void adicionarSucursalProducto( )
    {
    	try 
    	{
    		String idSucursal = JOptionPane.showInputDialog(this,"ID de la sucursal", "Adicionar Sucursal Producto",JOptionPane.QUESTION_MESSAGE);
    		String idProducto = JOptionPane.showInputDialog(this, "Codigo de barras del producto", "Adicionar Sucursal Producto", JOptionPane.QUESTION_MESSAGE);
    		String precio = JOptionPane.showInputDialog(this,"Precio de venta", "Adicionar Sucursal Producto",JOptionPane.QUESTION_MESSAGE);
    		String nivel = JOptionPane.showInputDialog(this, "Nivel de reorden", "Adicionar Sucursal Producto", JOptionPane.QUESTION_MESSAGE);
    		if (idSucursal != null && idProducto!=null && precio != null && nivel != null)
    		{
    			VOSucursalProducto tb = superandes.adicionarSucursalProducto(Long.valueOf(idSucursal), Long.valueOf(idProducto), Double.valueOf(precio), Integer.valueOf(nivel));
    			if(tb==null){
        			throw new Exception ("No se pudo adicionar la relacion entre: " + idSucursal + " y " + idProducto);
        		}
        		String resultado = "En adicionar Sucursal Producto\n\n";
        		resultado += "Relacion adicionada exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void adicionarOrden( )
    {
    	try 
    	{
    		
    		String idProveedor = JOptionPane.showInputDialog(this,"NIT del proveedor", "Adicionar Orden de pedido",JOptionPane.QUESTION_MESSAGE);
    		String idProducto = JOptionPane.showInputDialog(this, "Codigo de barras del producto", "Adicionar Orden de pedido Producto", JOptionPane.QUESTION_MESSAGE);
    		String idSucursal = JOptionPane.showInputDialog(this,"Id de la sucursal", "Adicionar Orden de pedido",JOptionPane.QUESTION_MESSAGE);
    		String precio = JOptionPane.showInputDialog(this, "Precio acordado", "Adicionar Orden de pedido", JOptionPane.QUESTION_MESSAGE);
    		String fechaEsperada = JOptionPane.showInputDialog(this, "Fecha esperada de entrega", "Adicionar Orden de pedido Producto", JOptionPane.QUESTION_MESSAGE);
    		String cantidad = JOptionPane.showInputDialog(this,"Cantidad a pedir", "Adicionar Orden de pedido",JOptionPane.QUESTION_MESSAGE);
    		if (idSucursal != null && idProducto!=null && precio != null && idProveedor != null && fechaEsperada !=null && cantidad != null)
    		{
    			System.out.println("hola");
    			System.out.println(Date.valueOf(fechaEsperada));
    			VOOrdenPedido tb = superandes.adicionarOrdenPedido(Long.valueOf(idProveedor),Long.valueOf(idSucursal), Long.valueOf(idProducto), Double.valueOf(precio), Integer.valueOf(cantidad), Date.valueOf(fechaEsperada), OrdenPedido.P);
    			if(tb==null){
        			throw new Exception ("No se pudo adicionar la relacion entre: " + idSucursal + " y " + idProducto);
        		}
        		String resultado = "En adicionar Sucursal Producto\n\n";
        		resultado += "Relacion adicionada exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void adicionarProductoProveedor( )
    {
    	try 
    	{
    		String idProducto = JOptionPane.showInputDialog(this,"Codigo de barras del producto", "Adicionar un proucto a un proveedor",JOptionPane.QUESTION_MESSAGE);
    		String idProveedor = JOptionPane.showInputDialog(this, "NIT del proveedor", "Adicionar un proucto a un proveedor", JOptionPane.QUESTION_MESSAGE);
    		String precio = JOptionPane.showInputDialog(this, "Precio acordado con el proveedor", "Adicionar un proucto a un proveedor", JOptionPane.QUESTION_MESSAGE);
    		String indice = JOptionPane.showInputDialog(this, "Indice de calidad del producto", "Adicionar un proucto a un proveedor", JOptionPane.QUESTION_MESSAGE);
    		if (idProducto != null && idProveedor!=null && precio != null && indice != null)
    		{
    			VOProductoProveedor tb = superandes.adicionarProductoProveedor(Long.valueOf(idProducto), Long.valueOf(idProveedor), Double.valueOf(precio), Double.valueOf(indice));
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo adicionar al proveedor: " + idProveedor + " el producto:" +idProducto);
        		}
        		String resultado = "En adicionar asociación\n\n";
        		resultado += "Se creo la asociación exitosamente exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void adicionarProducto( )
    {
    	try 
    	{
    		String codigoB = JOptionPane.showInputDialog(this,"Codigo de barras", "Adicionar un producto",JOptionPane.QUESTION_MESSAGE);
    		String marca = JOptionPane.showInputDialog(this, "Marca", "Adicionar un proucto", JOptionPane.QUESTION_MESSAGE);
    		String nombre = JOptionPane.showInputDialog(this, "Nombre", "Adicionar un proucto", JOptionPane.QUESTION_MESSAGE);
    		String presentacion = JOptionPane.showInputDialog(this, "Presentacion", "Adicionar un proucto", JOptionPane.QUESTION_MESSAGE);
    		String precioU = JOptionPane.showInputDialog(this, "Precio segun la unidad de medida", "Adicionar un proucto", JOptionPane.QUESTION_MESSAGE);
    		String unidadM = JOptionPane.showInputDialog(this, "Unidad de medida", "Adicionar un proucto", JOptionPane.QUESTION_MESSAGE);
    		String cantidadP = JOptionPane.showInputDialog(this, "Cantidad en la presentacion", "Adicionar un proucto", JOptionPane.QUESTION_MESSAGE);
    		String volumen = JOptionPane.showInputDialog(this, "Volumen", "Adicionar un proucto", JOptionPane.QUESTION_MESSAGE);
    		String peso = JOptionPane.showInputDialog(this, "Peso", "Adicionar un proucto", JOptionPane.QUESTION_MESSAGE);
    		String tipoP = JOptionPane.showInputDialog(this, "Tipo de producto", "Adicionar un proucto", JOptionPane.QUESTION_MESSAGE);
    		if (codigoB != null && marca!=null && nombre != null && presentacion != null && precioU != null && unidadM != null && cantidadP != null && volumen != null && peso != null && tipoP != null)
    		{
    			VOProducto tb = superandes.adicionarProducto(Long.valueOf(codigoB), nombre, marca, presentacion, Double.valueOf(precioU), unidadM, Integer.valueOf(cantidadP), Double.valueOf(volumen), Double.valueOf(peso), tipoP);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo adicionar el producto: " + codigoB);
        		}
        		String resultado = "En adicionar producto\n\n";
        		resultado += "Se creo la asociación exitosamente exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    /**
     * Adiciona un tipo de bebida con la información dada por el usuario
     * Se crea una nueva tupla de tipoBebida en la base de datos, si un tipo de bebida con ese nombre no existía
     */
    public void adicionarCliente( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog(this,"Escriba\n1Si es Persona Natura\n2Si es empresa", "Adicionar Cliente",JOptionPane.QUESTION_MESSAGE);
    		Integer resp = Integer.valueOf(id);
    		
    		if (resp == 1)
    		{
    			String nombre = JOptionPane.showInputDialog(this, "Nombre del Cliente", "Adicionar Persona Natural", JOptionPane.QUESTION_MESSAGE);
        		String correo = JOptionPane.showInputDialog(this, "Correo del cliente", "Adicionar Persona Natural", JOptionPane.QUESTION_MESSAGE);
        		String cedula = JOptionPane.showInputDialog(this, "Cedula del cliente", "Adicionar Persona Natural", JOptionPane.QUESTION_MESSAGE);
        		VOPersonaNatural tb = superandes.adicionarPersona(nombre, correo, Long.valueOf(cedula));
        		if (tb == null)
        		{
        			throw new Exception ("Se el cliente " + nombre);
        		}
        		String resultado = "En adicionar Cliente\n\n";
        		resultado += "Cliente adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else if(resp == 2)
    		{
    			String nombre = JOptionPane.showInputDialog(this, "Nombre de la empresa", "Adicionar Empresa", JOptionPane.QUESTION_MESSAGE);
        		String correo = JOptionPane.showInputDialog(this, "Correo de la empresa", "Adicionar Empresa", JOptionPane.QUESTION_MESSAGE);
        		String nit = JOptionPane.showInputDialog(this, "NIT de la empresa", "Adicionar Empresa", JOptionPane.QUESTION_MESSAGE);
        		String direccion = JOptionPane.showInputDialog(this, "Direccion de la empresa", "Adicionar Empresa", JOptionPane.QUESTION_MESSAGE);
        		VOEmpresa tb = superandes.adicionarEmpresa(nombre, correo, Long.valueOf(nit),direccion);
        		if (tb == null)
        		{
        			throw new Exception ("Se adiciono la empresa: " + nombre);
        		}
        		String resultado = "En adicionar Cliente\n\n";
        		resultado += "Cliente adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    public void asignarCarrito(){
    	try{
    		String idCliente = JOptionPane.showInputDialog(this,"Id Cliente", "Asignar Carro",JOptionPane.QUESTION_MESSAGE);
    		String idSucursal = JOptionPane.showInputDialog(this,"Id Sucursal", "Quitar Carro",JOptionPane.QUESTION_MESSAGE);
    		VOCarritoCompras carro = superandes.asignarCarro(Long.valueOf(idCliente),Long.valueOf(idSucursal));
    		if(carro == null)
    			JOptionPane.showMessageDialog(this, "Todos los carros estan ocupados");
    		else{
        		String resultado = "En asignar carro\n\n";
        		resultado += "Se asigno el carro: " + carro;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    	}catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    
    
    public void quitarCarrito(){
    	try{
    		String idCliente = JOptionPane.showInputDialog(this,"Id Cliente", "Quitar Carro",JOptionPane.QUESTION_MESSAGE);
    		String idSucursal = JOptionPane.showInputDialog(this,"Id Sucursal", "Quitar Carro",JOptionPane.QUESTION_MESSAGE);
    		VOCarritoCompras carro = superandes.quitarCarro(Long.valueOf(idCliente),Long.valueOf(idSucursal));
    		if(carro == null)
    			JOptionPane.showMessageDialog(this, "Todos los carros estan ocupados");
    		else{
        		String resultado = "En asignar carro\n\n";
        		resultado += "Se asigno el carro: " + carro;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    	}catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void agregarProductoCarrito(){
    	try{
    		String idProducto = JOptionPane.showInputDialog(this,"Codigo de barras del Producto", "Agregar Producto",JOptionPane.QUESTION_MESSAGE);
    		String idCarrito = JOptionPane.showInputDialog(this,"Id Carrito", "Agregar Producto",JOptionPane.QUESTION_MESSAGE);
    		String cantidad = JOptionPane.showInputDialog(this,"Cantidad a añadir", "Agregar Producto",JOptionPane.QUESTION_MESSAGE);
    		VOCarritoComprasProducto carro = superandes.asignarProducto(Long.valueOf(idProducto), Long.valueOf(idCarrito), Integer.valueOf(cantidad));
    		if(carro == null)
    			JOptionPane.showMessageDialog(this, "Todos los carros estan ocupados");
    		else{
        		String resultado = "En asignar carro\n\n";
        		resultado += "Se asigno el carro: " + carro;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    	}catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void quitarProductoCarrito(){
    	try{
    		String idProducto = JOptionPane.showInputDialog(this,"Codigo de barras del Producto", "Agregar Producto",JOptionPane.QUESTION_MESSAGE);
    		String idCarrito = JOptionPane.showInputDialog(this,"Id Carrito", "Agregar Producto",JOptionPane.QUESTION_MESSAGE);
    		superandes.quitarProducto(Long.valueOf(idProducto), Long.valueOf(idCarrito));

        		String resultado = "En quitar producto\n\n";
        		resultado += "Se quito el producto";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		
    	}catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void pagarCarrito(){
    	try{
    		String idCarrito = JOptionPane.showInputDialog(this,"Id Carrito", "Pagar Carrito",JOptionPane.QUESTION_MESSAGE);
    		superandes.pagarCarrito(Long.valueOf(idCarrito), new Date(System.currentTimeMillis()));;

        		String resultado = "Se pago el carrito\n\n";
        		resultado += idCarrito;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		
    	}catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void devolverCarritos(){
    	try{
    		superandes.recogerCarritos();
        		String resultado = "Se recogieron los carritos abandonados\n\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		
    	}catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void registrarLlegada( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog(this,"Id de la orden", "Registrar llegada producto",JOptionPane.QUESTION_MESSAGE);
    		Long idO = Long.valueOf(id);
    		String fecha = JOptionPane.showInputDialog (this, "Fecha de llegada escribir yyyy-mm-dd", "Registrar llegada producto", JOptionPane.QUESTION_MESSAGE);
    		String calificacion = JOptionPane.showInputDialog(this, "Calificacion del servicio", "Registrar llegada producto", JOptionPane.QUESTION_MESSAGE);
    		String cantidadR = JOptionPane.showInputDialog(this, "Cantidad recibida", "Registrar llegada producto", JOptionPane.QUESTION_MESSAGE);
    		if (id != null && fecha!= null && calificacion != null && cantidadR != null)
    		{
        		long tb = superandes.realizarEntrega(idO, Date.valueOf(fecha), Double.valueOf(calificacion), OrdenPedido.E, Integer.valueOf(cantidadR));
        		if (tb < 0)
        		{
        			throw new Exception ("No se pudo registrar la orden de pedido: " + idO);
        		}
        		String resultado = "En adicionar Llegada\n\n";
        		resultado += "Llegada de pedido adicionada exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
	/* ****************************************************************
	 * 			Métodos administrativos
	 *****************************************************************/
	/**
	 * Muestra el log de Parranderos
	 */
	public void mostrarLogSuperAndes ()
	{
		mostrarArchivo ("superAndes.log");
	}
	
	/**
	 * Muestra el log de datanucleus
	 */
	public void mostrarLogDatanuecleus ()
	{
		mostrarArchivo ("datanucleus.log");
	}
	
	/**
	 * Limpia el contenido del log de parranderos
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogSuperAndes ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("superAndes.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de superAndes ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia el contenido del log de datanucleus
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogDatanucleus ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("datanucleus.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Muestra la presentación general del proyecto
	 */
	public void mostrarPresentacionGeneral ()
	{
		mostrarArchivo ("data/00-ST-ParranderosJDO.pdf");
	}
	
	/**
	 * Muestra el modelo conceptual de Parranderos
	 */
	public void mostrarModeloConceptual ()
	{
		mostrarArchivo ("data/Modelo Conceptual Parranderos.pdf");
	}
	
	/**
	 * Muestra el esquema de la base de datos de Parranderos
	 */
	public void mostrarEsquemaBD ()
	{
		mostrarArchivo ("data/Esquema BD Parranderos.pdf");
	}
	
	/**
	 * Muestra el script de creación de la base de datos
	 */
	public void mostrarScriptBD ()
	{
		mostrarArchivo ("data/EsquemaParranderos.sql");
	}
	
	/**
	 * Muestra la arquitectura de referencia para Parranderos
	 */
	public void mostrarArqRef ()
	{
		mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	}
	
	/**
	 * Muestra la documentación Javadoc del proyectp
	 */
	public void mostrarJavadoc ()
	{
		mostrarArchivo ("doc/index.html");
	}
	

    /**
     * Genera una cadena de caracteres con la descripción de la excepcion e, haciendo énfasis en las excepcionsde JDO
     * @param e - La excepción recibida
     * @return La descripción de la excepción, cuando es javax.jdo.JDODataStoreException, "" de lo contrario
     */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicación
	 * @param e - La excepción generada
	 * @return La cadena con la información de la excepción y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "************ Error en la ejecución\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para más detalles";
		return resultado;
	}

	/**
	 * Limpia el contenido de un archivo dado su nombre
	 * @param nombreArchivo - El nombre del archivo que se quiere borrar
	 * @return true si se pudo limpiar
	 */
	private boolean limpiarArchivo(String nombreArchivo) 
	{
		BufferedWriter bw;
		try 
		{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) 
		{
//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el archivo dado como parámetro con la aplicación por defecto del sistema
	 * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo (String nombreArchivo)
	{
		try
		{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* ****************************************************************
	 * 			Métodos de la Interacción
	 *****************************************************************/
    /**
     * Método para la ejecución de los eventos que enlazan el menú con los métodos de negocio
     * Invoca al método correspondiente según el evento recibido
     * @param pEvento - El evento del usuario
     */
    @Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
        try 
        {
			Method req = InterfazSuperandes.class.getMethod ( evento );			
			req.invoke ( this );
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		} 
	}
    
	/* ****************************************************************
	 * 			Programa principal
	 *****************************************************************/
    /**
     * Este método ejecuta la aplicación, creando una nueva interfaz
     * @param args Arreglo de argumentos que se recibe por línea de comandos
     */
    public static void main( String[] args )
    {
        try
        {
        	
            // Unifica la interfaz para Mac y para Windows.
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
            InterfazSuperandes interfaz = new InterfazSuperandes( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
