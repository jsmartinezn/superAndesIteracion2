package superAndes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileReader;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import superAndes.negocio.Estante;
import superAndes.negocio.SuperAndes;
import superAndes.negocio.VOEstante;
import superAndes.negocio.VOEstanteProducto;
import superAndes.negocio.VOProducto;
import superAndes.negocio.VOSucursal;

public class EstanteTest {
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(EstanteTest.class.getName());
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos: La unidad de persistencia existe y el esquema de la BD también
	 */
	private static final String CONFIG_TABLAS_A = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
	/**
	 * La clase que se quiere probar
	 */
    private SuperAndes superAndes;
	
    /* ****************************************************************
	 * 			Métodos de prueba para la tabla TipoBebida - Creación y borrado
	 *****************************************************************/
	/**
	 * Método que prueba las operaciones sobre la tabla TipoBebida
	 * 1. Adicionar un tipo de bebida
	 * 2. Listar el contenido de la tabla con 0, 1 y 2 registros insertados
	 * 3. Borrar un tipo de bebida por su identificador
	 * 4. Borrar un tipo de bebida por su nombre
	 */
    @Test
	public void CRDEstante() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando las operaciones CRD sobre TipoBebida");
			superAndes = new SuperAndes(openConfig (CONFIG_TABLAS_A));
    		superAndes.borrar();
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de CRD de Tipobebida incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de CRD de Tipobebida incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de parranderos y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
    	try
		{
    		//Se necesita adicionar una sucursal primero
    		VOSucursal sucursal = superAndes.adicionarSucursal("Bogota", "Cll 95 A");
    		Long idSucursal = superAndes.getIdSucursal("Cll 95 A", "Bogota");
			// Lectura de los tipos de bebida con un tipo de bebida adicionado
			VOEstante estante1 = superAndes.adicionarEstante(idSucursal, "CONGELADOS", 30.0, 20.0,0.8);
			List<Estante> lista = superAndes.darEstantes(idSucursal);
			assertEquals ("Debe haber una bodega creada !!", 1, lista.size ());
			// Lectura de los tipos de bebida con dos tipos de bebida adicionados
			VOEstante estante = superAndes.adicionarEstante(idSucursal, "CONGELADOS", 10.0, 15.0,0.8);
			lista = superAndes.darEstantes(idSucursal);
			assertEquals ("Debe haber dos tipos de bebida creados !!", 2, lista.size ());
			Estante bod1 = lista.get(0);
			Estante bod2 = lista.get(1);
			assertTrue ("El primer tipo de bebida adicionado debe estar en la tabla", 30.0==bod1.getVolumen() || 30.0 ==bod2.getVolumen());
			assertTrue ("El segundo tipo de bebida adicionado debe estar en la tabla", 30.0==bod1.getVolumen() || 30.0 ==bod2.getVolumen());
			
			//Adicionar un producto a la bodega
			//Se necesita agregar un producto a la base de datos
			
			VOProducto producto = superAndes.adicionarProducto((long)1, "nuevo", "p", "500 gr 1 paquete", 5000.0, "kg", 4, 3.0, 2.0, "Congelado");
			List<Object[]> temp = superAndes.unidadesEnInvE(idSucursal, producto.getCodigoDeBarras());
			assertTrue(temp.isEmpty());
			
			//Se agregar la relacion y se revisa si fue agregado correctamente
			VOEstanteProducto bodegap = superAndes.adicionarEstanteProducto(bod2.getId(), producto.getCodigoDeBarras(), 4);
			temp = superAndes.unidadesEnInvE(idSucursal, producto.getCodigoDeBarras());
			assertTrue(!temp.isEmpty());
			assertEquals(1, temp.size());
			BigDecimal t = (BigDecimal)temp.get(0)[1];
			assertEquals(t.intValue(),4);			
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de operaciones sobre la tabla TipoBebida.\n";
			msg += "Revise el log de parranderos y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas sobre la tabla TipoBebida");
		}
		finally
		{
			superAndes.borrar();
    		superAndes.cerrarUnidadPersistencia ();
		}
		
	}
    
    
        /**
         * Lee datos de configuración para la aplicación, a partir de un archivo JSON o con valores por defecto si hay errores.
         * @param tipo - El tipo de configuración deseada
         * @param archConfig - Archivo Json que contiene la configuración
         * @return Un objeto JSON con la configuración del tipo especificado
         * 			NULL si hay un error en el archivo.
         */
        private JsonObject openConfig (String archConfig)
        {
        	JsonObject config = null;
    		try 
    		{
    			Gson gson = new Gson( );
    			FileReader file = new FileReader (archConfig);
    			JsonReader reader = new JsonReader ( file );
    			config = gson.fromJson(reader, JsonObject.class);
    			log.info ("Se encontró un archivo de configuración de tablas válido");
    		} 
    		catch (Exception e)
    		{
//    			e.printStackTrace ();
    			log.info ("NO se encontró un archivo de configuración válido");			
    			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de tablas válido: ", "TipoBebidaTest", JOptionPane.ERROR_MESSAGE);
    		}	
            return config;
        }	
}
