package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.Aluno;
import entity.Experiencia;
import entity.Login;

public class ExeperienciaDAO {

	private Connection conexao;

	public ExeperienciaDAO() {
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
		// RECEBE UMA ENTIDADE ExPERIENCIA
		// RETORNA INTEGER ID DA ENTIDADE CADASTRADA
		// JA FAZ A VALIDACAO PELO ID_PESSOA
		// ********************************************************************************************************

		public int incluir(Experiencia ex) {

			// **********************************************************************************************************
			// CHECK SE JA EXISTE 

			if (this.CheckById(ex.getId_pessoa() ) != null ) {
				return 2;
			}
			// **********************************************************************************************************

			this.getConexao(); // tem que estar abixo devido o checkByCpf fechar a
								// concexao

			int idInserido = 0;
			String sql = "INSERT INTO newEgresso.experiencia (ex_texto_impacto, ex_texto_livre, id_pessoa)"
													+ " VALUES (   ?,            ?,         ?       );";
			try {
				PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				// seta os valores
			
				stmt.setString(1, ex.getTxt_impacto());
				stmt.setString(2, ex.getTxt_livre());
				stmt.setInt(3, ex.getId_pessoa());
				
				
				

				stmt.executeUpdate();

				ResultSet rs = stmt.getGeneratedKeys();

				if (rs.next()) {
					idInserido = rs.getInt(1);
				}

				if (idInserido > 0) {
					System.out.println("Curriculo criado com sucesso");
				} else {
					System.out.println("Erro ao inserir curriculo");
				}

				stmt.close();

				return idInserido;

			} catch (SQLException e) {
				System.out.println("Erro ao inserir curriculo: " + e.getMessage());
				throw new RuntimeException(e);
			} finally {
				ConnectionFactory.fecharConexao(this.conexao);
			}
		}

		/**************************************************************
		 * CHECA E RETORNAR o curriculo passando o ID
		 ***************************************************************/
		public Experiencia CheckById(Integer idPessoa) {

			this.getConexao();
			Experiencia ex  = null;
			String sql = "SELECT *  FROM experiencia WHERE id_pessoa = ?";

			try {
				PreparedStatement stmt = conexao.prepareStatement(sql);
				stmt.setMaxRows(1);
				stmt.setInt(1, idPessoa);

				// executa
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					 ex = new Experiencia();
				
					ex.setTxt_impacto(rs.getString("ex_texto_impacto"));
					ex.setTxt_livre(rs.getString("ex_texto_livre"));
					

				}

				stmt.close();
				return  ex;

			} catch (SQLException e) {
				System.out.println("Erro ao obter Experiencia  CheckById: " + e.getMessage());
				e.printStackTrace();
				return  ex;
			} finally {
				ConnectionFactory.fecharConexao(this.conexao);
			}

		}

	     /**************************************************
		 * ALTERA DADO DE PESSOA RETURN 1 = ok 0 = not ok
		 ***************************************************/
		public Integer editar(Experiencia ex) {

			Integer ok = 0;
			this.getConexao();
			String sql = "UPDATE experiencia SET  ex_texto_impacto = ?, ex_texto_livre = ?  WHERE  id_pessoa = ?;";
			try {

				PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				
				stmt.setString(1, ex.getTxt_impacto());
				stmt.setString(2, ex.getTxt_livre());
				stmt.setInt(3, ex.getId_pessoa());

				if (stmt.executeUpdate() > 0) {
					stmt.close();
					ok = 1;
				}

				if (ok > 0) {
					System.out.println("Curriculo alterado com sucesso");
				} else {
					System.out.println("Erro ao alterar Curriculo");
				}

				stmt.close();

				return ok;

			} catch (SQLException e) {

				System.out.println("Erro ao editar Curriculo: " + e.getMessage());
				return ok;
			} finally {
				ConnectionFactory.fecharConexao(this.conexao);
			}

		}

}
