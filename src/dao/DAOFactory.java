package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


public class DAOFactory {


	public static AcademiaDAO crearAcademiaDAO() {
		String modo = leerModoPersistencia();

		switch (modo.toLowerCase()) {
			case "jdbc":
				return new AcademiaDAOImplJDBC();

			case "jpa":
				EntityManagerFactory emf = Persistence.createEntityManagerFactory("AcademiaPU");
				EntityManager em = emf.createEntityManager();
				return new AcademiaDAOImplJPA(em);

			default:
				throw new RuntimeException("Modo de persistencia no reconocido: " + modo);
		}
	}


	private static String leerModoPersistencia() {
		try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
			Properties propiedades = new Properties();
			propiedades.load(input);
			return propiedades.getProperty("persistence.mode", "jdbc");
		} catch (IOException ex) {
			throw new RuntimeException("Error leyendo config.properties", ex);
		}
	}
}
