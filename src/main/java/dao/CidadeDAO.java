package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Cidade;
import entity.Estado;

/*
 * Responsavel pelo CRUDE no banco 
 * TABLE cidade
 * Columns ci_nome, id_cidade, id_estado 
 */

public class CidadeDAO {

	private Connection conexao;

	public CidadeDAO() {
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
	 // RECEBE UMA Cidade
	//	RETORNA INTEGER id do cidade cadastrada  
	//********************************************************************************************************
	
	public int incluir(Cidade cidade) {
		this.getConexao();
		
		//************************************************************	
		//CHECK SE JA EXISTE CIDADE CADASTRADA COM ESTE NOME 
	
		if( this.CheckByCidade(cidade) > 0 ){
			return 2;
		}
		//************************************************************	
		
		
		
		int idInserido = 0;
		String sql = "INSERT INTO newEgresso.cidade  (ci_nome, id_estado)  VALUES ( ?, ?);";
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// seta os valores
			stmt.setString(1, cidade.getNome_cidade());
			stmt.setInt(2, cidade.getEstado().getId_estado());
			
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				idInserido = rs.getInt(1);
			}

			if (idInserido > 0) {
				System.out.println("Cidade inserida com sucesso");
			} else {
				System.out.println("Erro ao inserir cidade");
			}

			stmt.close();

			return idInserido;
			
		} catch (SQLException e) {
			System.out.println("Erro ao inserir cidade: " + e.getMessage());
			throw new RuntimeException(e);
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	}
	
	

	//********************************************************************************************************
	 // RECEBE UM INTEGER
	//	RETORNA A CIDADE CUJO O INTEGER FOI FORNECIDO
	//********************************************************************************************************
	public Cidade readCidadeById( Integer id_cidade ){
		

		this.getConexao();
		Cidade cidade  = null;
		
		String sql = "SELECT  id, nome, estados_id  FROM cidades WHERE id = ? ";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setInt(1, id_cidade);
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				cidade = new Cidade();
				Estado es = new Estado();
				es.setId_estado( rs.getInt("estados_id"));
				 cidade.setNome_cidade(rs.getString("nome"));
				 cidade.setId_cidade(rs.getInt("id"));
				 cidade.setEstado(es);
			}
			stmt.close();
			return cidade;

		} catch (SQLException e) {
			System.out.println("Erro ao obter cidade by id: " + e.getMessage());
			e.printStackTrace();
			return cidade;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	//*************************************************************************************
	
	
}
	
	
	//********************************************************************************************************
	 // RETORNA UMA  LISTA DE CIDADE 
	//********************************************************************************************************
	public List<Cidade> readCidade(){
		

		this.getConexao();
		Cidade cidade  = null;
		List<Cidade> listCidade = new ArrayList<>();
		
		String sql = "SELECT  id, nome FROM cidades";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				cidade = new Cidade();
				cidade.setId_cidade(Integer.parseInt( rs.getString("id")));
				cidade.setNome_cidade( rs.getString("nome"));
				
				listCidade.add(cidade);
			}

			stmt.close();
			return listCidade;

		} catch (SQLException e) {
			System.out.println("Erro ao obter login: " + e.getMessage());
			e.printStackTrace();
			return listCidade;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	}
	//*************************************************************************************
	
	
	
	//********************************************************************************************************
	 // RETORNA UMA  LISTA DE CIDADE  REFERENTE AO ESTADO
	//********************************************************************************************************
	public List<Cidade> readCidadeByEstado( Estado estado ){
		

		this.getConexao();
		Cidade cidade  = null;
		List<Cidade> listCidade = new ArrayList<>();
		
		String sql = "SELECT  id, nome FROM cidades WHERE estados_id = ? ";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			// executa
			stmt.setInt(1, estado.getId_estado());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				cidade = new Cidade();
				cidade.setId_cidade(Integer.parseInt( rs.getString("id")));
				cidade.setNome_cidade( rs.getString("nome"));
				
				listCidade.add(cidade);
			}

			stmt.close();
			return listCidade;

		} catch (SQLException e) {
			System.out.println("Erro ao obter login: " + e.getMessage());
			e.printStackTrace();
			return listCidade;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	//*************************************************************************************	


}		
		

	

//*************************************************************************************
// BEGIN PRIVATE METHODS
//*************************************************************************************	
	
	/*
	 * CHECA SE A CIDADE JA FOI CADASTRADA
	 * RETURN ID_CIDADE
	 */
	private Integer CheckByCidade(Cidade cidade ){
		
		this.getConexao();
		Integer id_cidade = 0;
		String sql = "SELECT   id, nome, estados_id  FROM cidades WHERE nome = ?  ";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setString(1, cidade.getNome_cidade().toLowerCase());
			
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				id_cidade =  (Integer.parseInt( rs.getString("id")));
			}

			stmt.close();
			return id_cidade;

		} catch (SQLException e) {
			System.out.println("Erro ao obter cidade byId: " + e.getMessage());
			e.printStackTrace();
			return id_cidade;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
		
	}
	
	
//*************************************************************************************
// END PRIVATE METHODS
//*************************************************************************************	


	
}
