package entidades;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "matriculas")
public class Matricula implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_matricula")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idMatricula;

	@Column(name = "id_alumno")
	private int idAlumno;

	@Column(name = "id_curso")
	private int idCurso;

	@Column(name = "fecha_inicio")
	private Date fechaInicio;

	
	public Matricula() {}
	public Matricula(int idAlumno, int idCurso) {
		this.idAlumno = idAlumno;
		this.idCurso = idCurso;
		this.fechaInicio = new Date(System.currentTimeMillis());
	}

	public Matricula(long idMatricula, int idAlumno, int idCurso) {
		this.idMatricula = idMatricula;
		this.idAlumno = idAlumno;
		this.idCurso = idCurso;
		this.fechaInicio = new Date(System.currentTimeMillis());
	};

	public Matricula(long idMatricula, int idAlumno, int idCurso, Date date) {
		this.idMatricula = idMatricula;
		this.idAlumno = idAlumno;
		this.idCurso = idCurso;
		this.fechaInicio = date;
	};

	public long getIdMatricula() {
		return idMatricula;
	}

	public void setIdMatricula(long idMatricula) {
		this.idMatricula = idMatricula;
	}

	public int getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(int idAlumno) {
		this.idAlumno = idAlumno;
	}

	public int getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}

	public void setFecha(Date fecha) {
		this.fechaInicio = fecha;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

}
