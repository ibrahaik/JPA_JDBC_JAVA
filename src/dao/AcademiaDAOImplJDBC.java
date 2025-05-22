package dao;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;

public class AcademiaDAOImplJDBC implements AcademiaDAO {

	private String URLConexion = new String("jdbc:mysql://172.16.0.182:3306/gestion?user=ibra&password=usuario");

	private static final String FIND_ALL_CURSOS_SQL = "select id_curso, nombre_curso from cursos";
	private static final String FIND_ALL_MATRICULAS_SQL = "select id_matricula, id_alumno, id_curso, fecha_inicio from matriculas";

	private static final String FIND_ALL_ALUMNOS_SQL = "select id_alumno, nombre_alumno, foto from alumnos";

	private static final String ADD_ALUMNO_SQL = "insert into alumnos" + " (id_alumno, nombre_alumno, foto) "
			+ " values (?,?,?) ";

	private static final String UPDATE_ALUMNO_SQL = "update alumnos set nombre_alumno=?, foto=? "
			+ " where id_alumno=? ";

	private static final String GET_ALUMNO_SQL = " select id_alumno, nombre_alumno, foto " + " from alumnos "
			+ " where id_alumno = ?";

	public AcademiaDAOImplJDBC() {
	}

	public AcademiaDAOImplJDBC(String URLConexion) {
		this.URLConexion = URLConexion;
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URLConexion);
	}

	private void releaseConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
				con = null;
			} catch (SQLException e) {
				for (Throwable t : e) {
					System.err.println("Error: " + t);
				}
			}
		}
	}

	@Override
	public Collection<Alumno> cargarAlumnos() {
		Collection<Alumno> alumnos = new ArrayList<Alumno>();
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(FIND_ALL_ALUMNOS_SQL);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String nombre = (rs.getString(2) != null ? rs.getString(2) : "sin nombre");
				Blob foto = rs.getBlob(3);

				Alumno alumno = new Alumno(id, nombre);

				if (foto != null)
					alumno.setFoto(foto.getBytes(1L, (int) foto.length()));
				else
					alumno.setFoto(null);

				alumnos.add(alumno);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			for (Throwable t : e) {
				System.err.println("Error: " + t);
			}
		} finally {
			releaseConnection(con);
		}
		return alumnos;
	}

	@Override
	public Alumno getAlumno(int idAlumno) {
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(GET_ALUMNO_SQL);
			ps.setInt(1, idAlumno);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int id_Alumno = rs.getInt(1);

				String nombreAlumno = (rs.getString(2) != null ? rs.getString(2) : "sin nombre");
				Blob foto = rs.getBlob(3);
				Alumno alumno = new Alumno(id_Alumno, nombreAlumno);
				if (foto != null)
					alumno.setFoto(foto.getBytes(1L, (int) foto.length()));
				else
					alumno.setFoto(null);
				return alumno;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			for (Throwable t : e) {
				System.err.println("Errores: " + t);
			}
		} finally {
			releaseConnection(con);
		}
		return null;
	}

	@Override
	public int grabarAlumno(Alumno alumno) {
		int retorno = 0;
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(ADD_ALUMNO_SQL);
			ps.setInt(1, alumno.getIdAlumno());
			ps.setString(2, alumno.getNombreAlumno());

			if (alumno.getFoto() != null) {
				ps.setBinaryStream(3, new ByteArrayInputStream(alumno.getFoto()));
			} else {
				ps.setBinaryStream(3, null);
			}

			retorno = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		return retorno;
	}

	@Override
	public int actualizarAlumno(Alumno alumno) {
		int retorno = 0;
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_ALUMNO_SQL);
			ps.setString(1, alumno.getNombreAlumno());

			if (alumno.getFoto() != null) {
				ps.setBinaryStream(2, new ByteArrayInputStream(alumno.getFoto()));
			} else {
				ps.setBinaryStream(2, null);
			}

			ps.setInt(3, alumno.getIdAlumno());
			retorno = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		return retorno;
	}

	@Override
	public int borrarAlumno(int idAlumno) {
		try {
			String query = "DELETE FROM alumnos WHERE id_alumno=" + idAlumno + ";";
			Connection con = getConnection();
			Statement a = con.createStatement();
			return a.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public Collection<Curso> cargarCursos() {
		Collection<Curso> cursos = new ArrayList<Curso>();
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(FIND_ALL_CURSOS_SQL);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String nombre = (rs.getString(2) != null ? rs.getString(2) : "sin nombre");

				cursos.add(new Curso(id, nombre));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			for (Throwable t : e) {
				System.err.println("Error: " + t);
			}
		} finally {
			releaseConnection(con);
		}
		return cursos;
	}

	@Override
	public Curso getCurso(int idCurso) {
		try {
			String query = "Select id_curso,nombre_curso from cursos where id_curso= " + idCurso + ";";
			Connection con = getConnection();
			Statement a = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet res = a.executeQuery(query);

			if (res.next()) {
				return new Curso(res.getInt("id_curso"), res.getString("nombre_curso"));
			} else {
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int grabarCurso(Curso curso) {
		try {
			String query = "INSERT INTO cursos values (" + curso.getIdCurso() + ",'" + curso.getNombreCurso() + "');";
			Connection con = getConnection();
			Statement a = con.createStatement();
			return a.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int actualizarCurso(Curso curso) {
		try {
			String query = "UPDATE cursos SET nombre_curso='" + curso.getNombreCurso() + "' WHERE id_curso="
					+ curso.getIdCurso() + ";";
			Connection con = getConnection();
			Statement a = con.createStatement();
			return a.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int borrarCurso(int idCurso) {
		try {
			String query = "DELETE FROM cursos WHERE id_curso=" + idCurso + ";";
			Connection con = getConnection();
			Statement a = con.createStatement();
			return a.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public Collection<Matricula> cargarMatriculas() {
		Collection<Matricula> matriculas = new ArrayList<Matricula>();
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(FIND_ALL_MATRICULAS_SQL);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				long id = rs.getLong(1);
				int id_alumno = rs.getInt(2);
				int id_curso = rs.getInt(3);

				matriculas.add(new Matricula(id, id_alumno, id_curso));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			for (Throwable t : e) {
				System.err.println("Error: " + t);
			}
		} finally {
			releaseConnection(con);
		}
		return matriculas;
	}

	@Override
	public long getIdMatricula(int idAlumno, int idCurso) {
		try {
			String query = "Select id_matricula from matriculas where id_curso= " + idCurso + " and id_alumno= "
					+ idAlumno + ";";

			Connection con = getConnection();
			Statement a = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet res = a.executeQuery(query);
			if (res.next()) {
				return res.getLong(1);
			} else {
				return -1;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public Matricula getMatricula(long idMatricula) {
		try {
			String query = "Select id_matricula,id_alumno,id_curso,fecha_inicio from matriculas where id_alumno= "
					+ idMatricula + ";";
			Connection con = getConnection();
			Statement a = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet res = a.executeQuery(query);

			if (res.next()) {
				return new Matricula(res.getLong("id_matricula"), res.getInt("id_alumno"), res.getInt("id_curso"),
						res.getDate("fecha_inicio"));
			} else {
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int grabarMatricula(Matricula matricula) {
		try {

			String query = "INSERT INTO matriculas(id_alumno,id_curso,fecha_inicio) values(" + matricula.getIdAlumno()
					+ "," + matricula.getIdCurso() + ",'" + new java.sql.Date(matricula.getFechaInicio().getTime())
					+ "');";
			Connection con = getConnection();
			Statement a = con.createStatement();
			return a.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int actualizarMatricula(Matricula matricula) {
		try {
			String query = "UPDATE matriculas SET fecha_inicio='"
					+ new java.sql.Date(matricula.getFechaInicio().getTime()) + "'  WHERE id_matricula="
					+ matricula.getIdMatricula() + ";";
			Connection con = getConnection();
			Statement a = con.createStatement();
			return a.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int borrarMatricula(long idMatricula) {
		try {
			String query = "DELETE FROM matriculas WHERE id_matricula=" + idMatricula + ";";
			Connection con = getConnection();
			Statement a = con.createStatement();
			return a.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

}