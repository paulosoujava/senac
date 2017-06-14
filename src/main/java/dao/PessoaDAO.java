package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.fabric.xmlrpc.base.Array;

import entity.Curso;
import entity.Pessoa;

/*
 * Classe de manipulacao com o banco de dados referente a entidade pessoa
 */
public class PessoaDAO {

	
	private Connection conexao;

	public PessoaDAO() {
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
    // RECEBE UMA ENTIDADE PESSOA
	//	RETORNA INTEGER ID DA ENTIDADE CADASTRADA
    // JA FAZ A VALIDACAO DO CPF, CASO EXISTA NAO CADASTRA
	//********************************************************************************************************
		
		public int incluir(Pessoa pessoa) {
			
			
			//****************************************************		
			//CHECK SE JA EXISTE CPF CADASTRADO 
		
			if( this.CheckByCpf(pessoa.getCpf()) > 0 ){
				return 2;
			}
			//************************************************************	
			
			this.getConexao(); // tem que estar abixo devido o checkByCpf fechar a concexao
			
			int idInserido = 0;
			String sql = "INSERT INTO newEgresso.pessoa (pe_primeiro_nome, pe_ultimo_nome, pe_cpf, pe_telefone, pe_dta_nasc, pe_sexo )"
				  	                          + " VALUES (?,                 ?,            ?,       ?,           ?,             ? );";
			try {
				PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				// seta os valores
				stmt.setString(1,pessoa.getPrimeiro_nome() );
				stmt.setString(2, pessoa.getUltimo_nome());
				stmt.setString(3, pessoa.getCpf());
				stmt.setString(4, pessoa.getTelefone());
				stmt.setString(5, pessoa.getData_nascimento());
				stmt.setString(6, pessoa.getSexo());
				
				
				stmt.executeUpdate();

				ResultSet rs = stmt.getGeneratedKeys();

				if (rs.next()) {
					idInserido = rs.getInt(1);
				}

				if (idInserido > 0) {
					System.out.println("Pessoa inserida com sucesso");
				} else {
					System.out.println("Erro ao inserir pessoa");
				}

				stmt.close();

				return idInserido;
				
			} catch (SQLException e) {
				System.out.println("Erro ao inserir pessoa: " + e.getMessage());
				throw new RuntimeException(e);
			} finally {
				ConnectionFactory.fecharConexao(this.conexao);
			}
		}
		
		
		/**************************************************
		 * CHECA SE A PESSOA JA FOI CADASTRADA
		 * RETURN ID_PESSOA
		 ***************************************************/
		public Integer CheckByCpf(String cpf ){
			
			this.getConexao();
			Integer id_pessoa = 0;
			String sql = "SELECT  id_pessoa FROM pessoa WHERE pe_cpf = ?  ";

			try {
				PreparedStatement stmt = conexao.prepareStatement(sql);
				stmt.setMaxRows(1);
				stmt.setString(1, cpf);
				
				// executa
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					id_pessoa =  (Integer.parseInt( rs.getString("id_pessoa")));
				}

				stmt.close();
				return id_pessoa;

			} catch (SQLException e) {
				System.out.println("Erro ao obter pessoa  byCPF: " + e.getMessage());
				e.printStackTrace();
				return id_pessoa;
			} finally {
				ConnectionFactory.fecharConexao(this.conexao);
			}
			
		}
		
		
		/**************************************************
		 * CHECA  A PESSOA PELO ID
		 * RETURN PESSOA
		 ***************************************************/
		public Pessoa CheckById( Integer id ){
			
			this.getConexao();
			Pessoa p = null;
			String sql = "SELECT  * FROM pessoa WHERE id_pessoa = ?  ";

			try {
				PreparedStatement stmt = conexao.prepareStatement(sql);
				stmt.setInt(1, id);
				
				// executa
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					 p = new Pessoa();
					p.setCpf(rs.getString("pe_cpf")); 
					p.setData_nascimento(rs.getString("pe_dta_nasc"));
					p.setId_pessoa(rs.getInt("id_pessoa"));
					p.setPrimeiro_nome(rs.getString("pe_primeiro_nome"));
					p.setSexo(rs.getString("pe_sexo"));
					p.setTelefone(rs.getString("pe_telefone"));
					p.setUltimo_nome(rs.getString("pe_ultimo_nome"));
					p.setSexo(rs.getString("pe_sexo"));
			
				}

				stmt.close();
				return p;

			} catch (SQLException e) {
				System.out.println("Erro ao obter pessoa  byCPF: " + e.getMessage());
				e.printStackTrace();
				return p;
			} finally {
				ConnectionFactory.fecharConexao(this.conexao);
			}
			
		}
		/**************************************************
		 * ALTERA DADO DE PESSOA
		 * RETURN 1 = ok 0 = not ok
		 ***************************************************/
		public Integer editar(Pessoa pessoa ){
			
			
			Integer ok = 0;
			this.getConexao();
			String sql = "UPDATE pessoa SET  "+
											 "pe_primeiro_nome = ?  , "+
											  "pe_ultimo_nome = ?,"+
											  "pe_cpf = ?, "+
											  "pe_telefone = ?, "+
											  "pe_dta_nasc = ?, "+
											  "pe_sexo = ? "+
											 	 "WHERE  id_pessoa = ?;";
			try {
				


				PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				
				stmt.setString(1, pessoa.getPrimeiro_nome().toLowerCase());
				stmt.setString(2, pessoa.getUltimo_nome().toLowerCase());
				stmt.setString(3, pessoa.getCpf());
				stmt.setString(4, pessoa.getTelefone());
				stmt.setString(5, pessoa.getData_nascimento());
				stmt.setString(6, pessoa.getSexo().toLowerCase());
				stmt.setInt(7, pessoa.getId_pessoa());
			
				if( stmt.executeUpdate() > 0){
					stmt.close();
					ok = 1;
				}

				if (ok > 0) {
					System.out.println("Pessoa alterado com sucesso");
				} else {
					System.out.println("Erro ao alterar pessoa");
				}

				stmt.close();

				return ok;
				
				
			} catch (SQLException e) {
				
				System.out.println("Erro ao editar curso: " + e.getMessage());
				return ok;
			} finally {
				ConnectionFactory.fecharConexao(this.conexao);
			}
		
		}
		
		
		
}
