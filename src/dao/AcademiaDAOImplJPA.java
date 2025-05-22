package dao;

import java.util.Collection;
import java.util.List;

import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class AcademiaDAOImplJPA implements AcademiaDAO {

	private EntityManager em;

	public AcademiaDAOImplJPA(EntityManager em) {
		this.em = em;
	}


	@Override
	public Collection<Alumno> cargarAlumnos() {
		TypedQuery<Alumno> query = em.createQuery("SELECT a FROM Alumno a", Alumno.class);
		return query.getResultList();
	}

	@Override
	public Alumno getAlumno(int idAlumno) {
		return em.find(Alumno.class, idAlumno);
	}

	@Override
	public int grabarAlumno(Alumno alumno) {
		return ejecutarTransaccion(() -> em.persist(alumno));
	}

	@Override
	public int actualizarAlumno(Alumno alumno) {
		return ejecutarTransaccion(() -> em.merge(alumno));
	}

	@Override
	public int borrarAlumno(int idAlumno) {
		return ejecutarTransaccion(() -> {
			Alumno alumno = em.find(Alumno.class, idAlumno);
			if (alumno != null) {
				em.remove(alumno);
			}
		});
	}

	// ======== Métodos para Curso ========

	@Override
	public Collection<Curso> cargarCursos() {
		TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c", Curso.class);
		return query.getResultList();
	}

	@Override
	public Curso getCurso(int idCurso) {
		return em.find(Curso.class, idCurso);
	}

	@Override
	public int grabarCurso(Curso curso) {
		return ejecutarTransaccion(() -> em.persist(curso));
	}

	@Override
	public int actualizarCurso(Curso curso) {
		return ejecutarTransaccion(() -> em.merge(curso));
	}

	@Override
	public int borrarCurso(int idCurso) {
		return ejecutarTransaccion(() -> {
			Curso curso = em.find(Curso.class, idCurso);
			if (curso != null) {
				em.remove(curso);
			}
		});
	}



	@Override
	public Collection<Matricula> cargarMatriculas() {
		TypedQuery<Matricula> query = em.createQuery("SELECT m FROM Matricula m", Matricula.class);
		return query.getResultList();
	}

	@Override
	public long getIdMatricula(int idAlumno, int idCurso) {
		TypedQuery<Long> query = em.createQuery(
				"SELECT m.idMatricula FROM Matricula m WHERE m.idAlumno = :idAlumno AND m.idCurso = :idCurso",
				Long.class
		);
		query.setParameter("idAlumno", idAlumno);
		query.setParameter("idCurso", idCurso);

		List<Long> resultados = query.getResultList();
		return resultados.isEmpty() ? -1 : resultados.get(0);
	}

	@Override
	public Matricula getMatricula(long idMatricula) {
		return em.find(Matricula.class, idMatricula);
	}

	@Override
	public int grabarMatricula(Matricula matricula) {
		return ejecutarTransaccion(() -> em.persist(matricula));
	}

	@Override
	public int actualizarMatricula(Matricula matricula) {
		return ejecutarTransaccion(() -> em.merge(matricula));
	}

	@Override
	public int borrarMatricula(long idMatricula) {
		return ejecutarTransaccion(() -> {
			Matricula matricula = em.find(Matricula.class, idMatricula);
			if (matricula != null) {
				em.remove(matricula);
			}
		});
	}


	private int ejecutarTransaccion(Runnable operacion) {
		try {
			em.getTransaction().begin();
			operacion.run();
			em.getTransaction().commit();
			return 1;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			return 0;
		}
	}
}
