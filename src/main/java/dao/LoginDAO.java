package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.Aluno;
import entity.Login;

/*
 * DAO responsável pelo acesso ao banco de dado;
 * Tabela PESSOA
 * dados email, senha, nivel
 */
public class LoginDAO {

	private Connection conexao;

	public LoginDAO() {
		this.getConexao();
	}

	// SINGLETON
	private void getConexao() {
		if (this.conexao != null) {
			try {
				conexao.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		this.conexao = new ConnectionFactory().obterConexao();
	}

	// ********************************************************************************************************
	// SELECIONA USUARIO PARA CONSULTA DE NIVEL
	// RECEBE LOGIN
	// RETORNA UM INTEGER POSSIBILIDADES: ( -1 == NAO CADASTRADO ) | ( 0 ==
	// ALUNO ) | ( 1 == ADM )
	// ASSOCIADO: LOGIN BEANLOGIN INDEX
	// ********************************************************************************************************
	public Integer isLogin(Login login) {

		this.getConexao();

		String sql = "SELECT log_nivel FROM login WHERE log_email = ? AND log_senha = ? ";
		System.out.println(login.getEmail().toLowerCase() + " " + login.getSenha().toLowerCase());
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setString(1, login.getEmail().toLowerCase());
			stmt.setString(2, login.getSenha().toLowerCase());
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return (Integer.parseInt(rs.getString("log_nivel")));
			}

			stmt.close();
			return 0;

		} catch (SQLException e) {
			System.out.println("Erro ao obter login: " + e.getMessage());
			e.printStackTrace();
			return 0;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

	}

	// ********************************************************************************************************
	// RECEBE UMA ENTIDADE LOGIN
	// RETORNA INTEGER ID DA ENTIDADE CADASTRADA
	// JA FAZ A VALIDACAO DO LOGIN
	// ********************************************************************************************************

	public int incluir(Login lo) {

		// **********************************************************************************************************
		// CHECK SE JA EXISTE EMAIL CADASTRADO

		if (this.isLogin(lo) > 0) {
			return 2;
		}
		// **********************************************************************************************************

		this.getConexao(); // tem que estar abixo devido o isLogin fechar a
							// concexao

		int idInserido = 0;
		String sql = "INSERT INTO newEgresso.login (log_nivel, log_email, log_senha)"
				+ " VALUES (?,         ?,        ?       );";
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// seta os valores
			stmt.setInt(1, lo.getNivel());
			stmt.setString(2, lo.getEmail());
			stmt.setString(3, lo.getSenha());

			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				idInserido = rs.getInt(1);
			}

			if (idInserido > 0) {
				System.out.println("Login criado com sucesso");
			} else {
				System.out.println("Erro ao inserir login");
			}

			stmt.close();

			return idInserido;

		} catch (SQLException e) {
			System.out.println("Erro ao inserir login: " + e.getMessage());
			throw new RuntimeException(e);
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	}

	// ********************************************************************************************************
	// Retorna o email referente ao id
	// ********************************************************************************************************
	public String geEmailLogin(Integer id) {

		this.getConexao();
		String email = null;
		String sql = "SELECT log_email FROM login WHERE id_login = ? ";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setInt(1, id);

			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				email = (rs.getString("log_email"));
			}

			stmt.close();
			return email;

		} catch (SQLException e) {
			System.out.println("Erro ao obter login: " + e.getMessage());
			e.printStackTrace();
			return email;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

	}

	// ********************************************************************************************************
	// Retorna o nivel referente ao id
	// ********************************************************************************************************
	public Integer geNivelLogin(Integer id) {

		this.getConexao();
		Integer nivel = null;
		String sql = "SELECT log_nivel FROM login WHERE id_login = ? ";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setInt(1, id);

			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				nivel = (rs.getInt("log_nivel"));
			}

			stmt.close();
			return nivel;

		} catch (SQLException e) {
			System.out.println("Erro ao obter nivel: " + e.getMessage());
			e.printStackTrace();
			return nivel;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

	}

	// ********************************************************************************************************
	// Retorna o aluno
	// ********************************************************************************************************
	public Aluno geIdPessoLogin(String email, String senha) {

		this.getConexao();
		Aluno a = null;
		String sql = "SELECT * FROM login INNER JOIN aluno ON login.id_login = aluno.id_login WHERE log_email = ? "
				+ " AND log_senha = ?; ";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setString(1, email);
			stmt.setString(2, senha);

			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				a = new Aluno();
				Login l = new Login();
				a.setId_pessoa(rs.getInt("id_pessoa"));
				l.setId_login(rs.getInt("id_login"));
				a.setLogin(l);

			}

			stmt.close();
			return a;

		} catch (SQLException e) {
			System.out.println("Erro ao obter login: " + e.getMessage());
			e.printStackTrace();
			return a;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

	}

	/**************************************************
	 * INATIVANDO O ALUNO
	 ***************************************************/
	public Integer inativarAtivarLogin(Integer id, Integer ativar) {

		Integer ok = 0;
		this.getConexao();

		String sql = "UPDATE login SET  log_nivel = " + (ativar == 1 ? 1 : -1) + "    WHERE  id_login = ?;";
		try {

			PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			stmt.setInt(1, id);

			if (stmt.executeUpdate() > 0) {
				stmt.close();
				ok = 1;
			}

			if (ok > 0) {
				System.out.println("Aluno inativado com sucesso");
			} else {
				System.out.println("Erro ao inativar aluno");
			}

			stmt.close();

			return ok;

		} catch (SQLException e) {

			System.out.println("Erro ao editar aluno: " + e.getMessage());
			return ok;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

	}

}
