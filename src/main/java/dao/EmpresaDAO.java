package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Curso;
import entity.Empresa;
import entity.Experiencia;

public class EmpresaDAO {

	private Connection conexao;

	public EmpresaDAO() {
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

	// INCLUIR EMPRESA

	public int incluir(Empresa e) {

		// **********************************************************************************************************
		// CHECK SE JA EXISTE

		// **********************************************************************************************************

		this.getConexao(); // tem que estar abixo devido o checkByCpf fechar a
							// concexao

		int idInserido = 0;
		String sql = "INSERT INTO newEgresso.empresa (em_nome, em_data_inicio, em_data_fim, em_cargo, em_texto_livre, id_aluno)" + " VALUES ( ?,       ?,             ? ,            ?,          ?,               ?      );";
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// seta os valores

			// string pode vir nula por se tratar de uma data de saida e
			// o aluno ainda esta trabalhando
			String tmp = "";
			if (e.getData_saida() != null) {
				tmp = e.getData_saida();
			}

			stmt.setString(1, e.getEmpresa());
			stmt.setString(2, e.getData_entrada());
			stmt.setString(3, tmp);
			stmt.setString(4, e.getFuncao());
			stmt.setString(5, e.getTxt_livre());
			stmt.setInt(6, e.getIdAluno());

			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				idInserido = rs.getInt(1);
			}

			if (idInserido > 0) {
				System.out.println("Empresa criado com sucesso");
			} else {
				System.out.println("Erro ao inserir empresa");
			}

			stmt.close();

			return idInserido;

		} catch (SQLException ex) {
			System.out.println("Erro ao inserir empresa: " + ex.getMessage());
			throw new RuntimeException(ex);
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	}

	// LISTA DE EMPRESAS CADASTRADAS NO CURRICULO
	public List<Empresa> readEmpresaIdAluno(Integer idAluno) {

		this.getConexao();
		List<Empresa> eList = new ArrayList<Empresa>();
		String sql = "SELECT * FROM empresa WHERE id_aluno = ? ";
		try {

			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setInt(1, idAluno);
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Empresa e = new Empresa();
				e.setData_entrada(rs.getString("em_data_inicio"));
				e.setData_saida(rs.getString("em_data_fim"));
				e.setEmpresa(rs.getString("em_nome"));
				e.setFuncao(rs.getString("em_cargo"));
				e.setTxt_livre(rs.getString("em_texto_livre"));
				e.setIdEmpresa(rs.getInt("id_empresa"));
				eList.add(e);
			}

			stmt.close();
			return eList;
		} catch (SQLException e) {
			System.out.println("Erro ao listar empresa: " + e.getMessage());
			return eList;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

	}
	// ********************************************************************************************************
	// DELETE EMPRESA CADASTRADA

	public Boolean deleteEmpresa(Empresa empresa) {

		System.out.println(empresa.getIdAluno());
		this.getConexao();
		String sql = "DELETE  FROM empresa WHERE id_empresa = ?";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, empresa.getIdEmpresa());

			// executa

			if (stmt.executeUpdate() >= 1) {
				System.out.println("Deletado com sucesso a empresa ");
				stmt.close();
				return true;
			}

		} catch (SQLException e) {
			System.out.println("Erro ao deletar a empresa : " + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

		return false;
	}

	// ********************************************************************************************************
	// EDITAR EMPRESA
	public Integer editar(Empresa e) {

		Integer ok = 0;
		/*
		 * 
		 * "em_data_inicio")); e.setData_saida(rs.getString("em_data_fim"));
		 * e.setEmpresa(rs.getString("em_nome"));
		 * e.setFuncao(rs.getString("em_cargo"));
		 * e.setTxt_livre(rs.getString("em_texto_livre"));
		 * 
		 */

		this.getConexao();
		String sql = "UPDATE empresa SET  " + "em_data_fim = ? , " + 
											"em_nome = ?," +
											"em_cargo = ?, " + 
											"em_texto_livre = ?, " + 
									        "em_data_inicio = ? " + 
											" WHERE  id_empresa = ?;";
		try {

			PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			String tmp = "";
			 if( e.getData_saida() != null ){
				 tmp = e.getData_saida();
			 }
			stmt.setString(1, e.getData_saida());
			stmt.setString(2, e.getEmpresa().toUpperCase());
			stmt.setString(3, e.getFuncao().toUpperCase());
			stmt.setString(4, e.getTxt_livre());
			stmt.setString(5, tmp );
			stmt.setInt(6, e.getIdEmpresa());

			if (stmt.executeUpdate() > 0) {
				stmt.close();
				ok = 1;
			}

			if (ok > 0) {
				System.out.println("Empresa alterado com sucesso");
			} else {
				System.out.println("Erro ao alterar empresa");
			}

			stmt.close();

			return ok;

		} catch (SQLException ex) {

			System.out.println("Erro ao editar empresa: " + ex.getMessage());
			return ok;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}

	}

}