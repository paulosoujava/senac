package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.Aluno;

public class SenhaDAO {

	
	private Connection conexao;

	public SenhaDAO() {
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
	
	public int editar(String s1, String s2, Integer idLogin ) {

		Integer ok = 0;
		this.getConexao();
		System.out.println("ID LOGI A MUDAR "+ idLogin );
		String sql = "UPDATE login SET  log_senha = ? WHERE  id_Login = ?;";
		try {

			PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, s1);
			stmt.setInt(2, idLogin);

			if (stmt.executeUpdate() > 0) {
				stmt.close();
				ok = 1;
			}

			if (ok > 0) {
				System.out.println("Senha alterado com sucesso");
			} else {
				System.out.println("Erro ao alterar senha");
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
