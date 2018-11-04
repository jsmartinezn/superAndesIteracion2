
package superAndes.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.io.FileReader;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import superAndes.negocio.Cliente;
import superAndes.negocio.Sucursal;
import superAndes.negocio.SuperAndes;
import superAndes.negocio.VOEmpresa;
import superAndes.negocio.VOPersonaNatural;
import superAndes.negocio.VOSucursal;

public class SucursalTest {
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(SucursalTest.class.getName());
	
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
	public void CRDSucursal() 
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
    		List<Sucursal> lista = superAndes.darSucursales();
    		assertTrue(lista.isEmpty());
    		
    		//Se adiciona una empresa
    		VOSucursal nueva = superAndes.adicionarSucursal("Bogota", "Cll 95 A # 33");
    		lista = superAndes.darSucursales();
    		assertTrue(!lista.isEmpty());
    		assertEquals(lista.size(),1);

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
