package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Aluno;
import entity.Login;



/*
 * Classe rsponvael pelo cruzamento dos alunos com egressos
 * exemplo: quando um importe é feito temos o id do curso a qual pertence este importe
 * ou seja todos os alunos neste importe estao relacionados a este curso
 * caso queira colocar os alunos em outro curso, podera mas nao cadastraremos no banco novamente
 * os dados dos alunos pois ja temos somente incluiremos nesta tabela egresso_ids
 */
public class EgressoAlunosDAO {
	

	private Connection conexao;

	public EgressoAlunosDAO() {
		this.getConexao();
	}

	//SINGLETON
	private void getConexao() {
		if( this.conexao != null ){
			try {
				conexao.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		this.conexao = new ConnectionFactory().obterConexao();
	
	}

	//********************************************************************************************************
	 // RECEBE UM CURSO
	//	RETORNA INTEGER id do curso cadastrado 
	//********************************************************************************************************
	
	public int incluir(Integer id_aluno, Integer id_curso) {

		this.getConexao();
		
		int idInserido = 0;
		
		String sql = "INSERT INTO newEgresso.egresso_ids(id_pessoa, id_curso)"
								              + " VALUES ( ?,         ?        );";
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// seta os valores
			stmt.setInt(1, id_aluno);
			stmt.setInt(2, id_curso);
			// executa
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				idInserido = rs.getInt(1);
			}

			if (idInserido > 0) {
				System.out.println("Curso inserido com sucesso");
			} else {
				System.out.println("Erro ao inserir curso");
			}

			stmt.close();

			return idInserido;
			
		} catch (SQLException e) {
			System.out.println("Erro ao inserir curso: " + e.getSQLState() + e.getErrorCode() + e.getMessage() + e.getCause());
			throw new RuntimeException(e);
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	}
	
	/*
	 *  RETORNA UMA LISTA DE IDS
	 *  TODOS OS IDS SAO REFERENTE A PESSOA QUE ESTAO NO IMPORTE DESTE EGRESSO
	 */
	
	public List<Integer> getAllIDAlunoEgressoImporte( Integer id ){
		

		this.getConexao();
		List<Integer> listI = new ArrayList<>();
		String sql = "SELECT * FROM egresso_ids  WHERE egresso_ids.id_curso = ?";
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
		         listI.add( rs.getInt("id_pessoa"));
			}

			stmt.close();
			return listI;

		} catch (SQLException e) {
			System.out.println("Erro ao obter lista egresso: " + e.getMessage());
			e.printStackTrace();
			return listI;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	//*************************************************************************************
	
		
	}
	
	/*
	 *  RETORNA UMA ID
	 * ID DO ALUNO SERVE PARA SABER A QUAL O CURSO ESTA VINCULADO
	 */
	
	public Integer getIDAlunoEgressoImporte( Integer idPessoa ){
		

		this.getConexao();
		Integer idCurso = 0;
		String sql = "SELECT * FROM egresso_ids  WHERE egresso_ids.id_pessoa = ?";
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, idPessoa);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				idCurso =  ( rs.getInt("id_curso"));
			}

			stmt.close();
			return idCurso;

		} catch (SQLException e) {
			System.out.println("Erro ao obter lista egresso: " + e.getMessage());
			e.printStackTrace();
			return idCurso;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	//*************************************************************************************
	
		
	}
	
}
