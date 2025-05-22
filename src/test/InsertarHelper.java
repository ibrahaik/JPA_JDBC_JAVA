package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import dao.AcademiaDAO;
import dao.DAOFactory;
import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;

public class InsertarHelper {

	private AcademiaDAO dao = null;

	// Constructor
	public InsertarHelper() {
		System.out.println("Inicializando el DAO...");
		dao = DAOFactory.crearAcademiaDAO();
	}

	/*
	 * Mostrar datos
	 */
	private void mostrarTodosLosDatos() {
		mostrarDatos(dao.cargarAlumnos(), "Alumnos");
		mostrarDatos(dao.cargarCursos(), "Cursos");
		mostrarDatos(dao.cargarMatriculas(), "Matrículas");
	}

	private void mostrarDatos(Collection<?> coleccion, String entidad) {
		System.out.println("\nListado de " + entidad + ":");
		for (Object obj : coleccion)
			System.out.println(obj);
	}

	/*
	 * Modificar matriculas
	 */
	private void actualizarFechaMatricula(int idAlumno, int idCurso, java.util.Date fecha) {
		long idMatricula = dao.getIdMatricula(idAlumno, idCurso);
		Matricula matricula = new Matricula(idMatricula, idAlumno, idCurso);
		matricula.setFecha(fecha);

		System.out.println("\nActualizando fecha de matrícula...");
		if (dao.actualizarMatricula(matricula) == 1) {
			System.out.println("Fecha de matrícula actualizada correctamente.");
		} else {
			System.out.println("Error al actualizar la fecha de matrícula.");
		}
	}

	/*
	 * Insertar matriculas
	 */
	private void agregarNuevaMatricula(int idAlumno, int idCurso) {
		System.out.println("\nCreando nueva matrícula...");
		Matricula matricula = new Matricula(idAlumno, idCurso);

		System.out.println("Guardando matrícula...");
		if (dao.grabarMatricula(matricula) == 1) {
			System.out.println("Matrícula guardada exitosamente.");
		} else {
			System.out.println("Error al guardar la matrícula.");
		}
	}

	/*
	 * Modificar cursos
	 */
	private void actualizarCurso(int id, String nombre) {
		Curso curso = dao.getCurso(id);

		System.out.println("\nActualizando curso con ID " + id + "...");
		curso.setNombreCurso(nombre);

		if (dao.actualizarCurso(curso) == 1) {
			System.out.println("Curso actualizado correctamente.");
		} else {
			System.out.println("Error al actualizar el curso.");
		}
	}

	/*
	 * Insertar cursos
	 */
	private void agregarCurso(int id, String nombre) {
		System.out.println("\nCreando nuevo curso...");
		Curso curso = new Curso(id, nombre);

		System.out.println("Guardando curso...");
		if (dao.grabarCurso(curso) == 1) {
			System.out.println("Curso guardado exitosamente.");
		} else {
			System.out.println("Error al guardar el curso.");
		}
	}

	/*
	 * Modificar alumnos
	 */
	private void actualizarAlumno(int id, String nombre, String rutaFoto) {
		Alumno alumno = dao.getAlumno(id);

		System.out.println("\nActualizando alumno con ID " + id + "...");
		alumno.setNombreAlumno(nombre);

		if (rutaFoto != null) {
			System.out.println("Actualizando foto del alumno...");
			try {
				byte[] foto = getBytesFromFile(new File(rutaFoto));
				alumno.setFoto(foto);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (dao.actualizarAlumno(alumno) == 1) {
			System.out.println("Alumno actualizado correctamente.");
		} else {
			System.out.println("Error al actualizar el alumno.");
		}
	}

	/*
	 * Insertar alumnos
	 */
	private void agregarAlumno(int id, String nombre, String rutaFoto) {
		System.out.println("\nCreando nuevo alumno...");
		Alumno alumno = new Alumno(id, nombre);

		try {
			byte[] foto = getBytesFromFile(new File(rutaFoto));
			alumno.setFoto(foto);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Guardando alumno...");
		if (dao.grabarAlumno(alumno) == 1) {
			System.out.println("Alumno guardado exitosamente.");
		} else {
			System.out.println("Error al guardar el alumno.");
		}
	}

	public static void main(String[] args) {
		InsertarHelper helper = new InsertarHelper();

		// Insertar alumnos
		helper.agregarAlumno(1000, "Daniel", "imagenes/cara2.jpg");
		helper.agregarAlumno(1001, "Francisco", "imagenes/cara4.jpg");

		// Modificar alumno
		helper.actualizarAlumno(1000, "Ezequiel", null);
		helper.actualizarAlumno(1000, "Agapito", "imagenes/cara1.jpg");

		// Insertar cursos
		helper.agregarCurso(500, "Java");
		helper.agregarCurso(501, ".NET");

		// Modificar curso
		helper.actualizarCurso(500, "Java avanzado");

		// Insertar matrículas
		helper.agregarNuevaMatricula(1000, 500);
		helper.agregarNuevaMatricula(1000, 501);
		helper.agregarNuevaMatricula(1001, 500);

		// Modificar fecha matrícula
		Calendar fecha = GregorianCalendar.getInstance();
		fecha.set(Calendar.MONTH, 11);
		helper.actualizarFechaMatricula(1001, 500, fecha.getTime());

		// Mostrar datos
		helper.mostrarTodosLosDatos();

		System.out.println("\nFin del programa.");
	}

	private static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			System.out.println("Archivo demasiado grande!");
			System.exit(1);
		}

		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("No se pudo leer completamente el archivo " + file.getName());
		}

		is.close();
		return bytes;
	}
}
