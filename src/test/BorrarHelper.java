package test;

import java.util.Collection;

import dao.AcademiaDAO;
import dao.DAOFactory;
import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;

public class BorrarHelper {

	private AcademiaDAO dao;

	public BorrarHelper() {
		System.out.println("Inicializando DAO...");
		dao = DAOFactory.crearAcademiaDAO();
	}
	public static void main(String[] args) {
		BorrarHelper helper = new BorrarHelper();
		helper.eliminarTodasLasMatriculas();
		helper.eliminarTodosLosAlumnos();
		helper.eliminarTodosLosCursos();
		System.out.println("\nProceso completado.");
	}


	private void eliminarTodosLosAlumnos() {
		System.out.println("Eliminando todos los alumnos existentes...");
		Collection<Alumno> alumnos = dao.cargarAlumnos();
		for (Alumno a : alumnos) {
			if (dao.borrarAlumno(a.getIdAlumno()) == 1) {
				System.out.println("Alumno eliminado correctamente.");
			}
		}
	}

	private void eliminarTodasLasMatriculas() {
		System.out.println("Eliminando todas las matrículas existentes...");
		Collection<Matricula> matriculas = dao.cargarMatriculas();
		for (Matricula m : matriculas) {
			if (dao.borrarMatricula(m.getIdMatricula()) == 1) {
				System.out.println("Matrícula eliminada correctamente.");
			}
		}
	}
	private void eliminarTodosLosCursos() {
		System.out.println("Eliminando todos los cursos existentes...");
		Collection<Curso> cursos = dao.cargarCursos();
		for (Curso c : cursos) {
			if (dao.borrarCurso(c.getIdCurso()) == 1) {
				System.out.println("Curso eliminado correctamente.");
			}
		}
	}


}
