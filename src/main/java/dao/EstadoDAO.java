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
 * Responsavel pelo CRUD no banco 
 * TABLE estado
 * Columns es_nome, id_estado 
 */
public class EstadoDAO {

	private Connection conexao;

	public EstadoDAO() {
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
	 // RECEBE UM estado
	//	RETORNA INTEGER ID DO ESTADO CADASTRADO
	//********************************************************************************************************
	
	public int incluir(Estado estado) {
		this.getConexao();
		
		//*********	*****	*****	*****	*****	*****	*****	*		
		//CHECK SE JA EXISTE ESTADO CADASTRADO COM ESTE NOME 
	
		if( this.CheckByEstado(estado) > 0 ){
			return 2;
		}
		//**********	*****	*****	*****	*****	*****	*****	
		
		
		
		int idInserido = 0;
		String sql = "INSERT INTO newEgresso.estado  (es_nome, id_estado)  VALUES ('?', '?');";
		try {
			PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// seta os valores
			stmt.setString(1, estado.getNome());
			stmt.setInt(2, estado.getId_estado());
			
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				idInserido = rs.getInt(1);
			}

			if (idInserido > 0) {
				System.out.println("Estdo inserido com sucesso");
			} else {
				System.out.println("Erro ao inserir estado");
			}

			stmt.close();

			return idInserido;
			
		} catch (SQLException e) {
			System.out.println("Erro ao inserir estado: " + e.getMessage());
			throw new RuntimeException(e);
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	}
	
	

	//********************************************************************************************************
	 // RECEBE UM INTEGER
	//	RETORNA O ESTADO CUJO O INTEGER FOI FORNECIDO
	//********************************************************************************************************
	public Estado readEstadoById( Integer id_estado ){
		

		this.getConexao();
		Estado estado  = null;
		
		String sql = "SELECT  id, nome, sigla  FROM estados WHERE id = ? ";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setInt(1, id_estado);
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				estado = new Estado();
				estado.setId_estado(rs.getInt("id"));
				estado.setNome(rs.getString("nome"));
				estado.setSigla(rs.getString("sigla"));
			}

			stmt.close();
			return estado;

		} catch (SQLException e) {
			System.out.println("Erro ao obter estado by id" + e.getMessage());
			e.printStackTrace();
			return estado;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	//*************************************************************************************
	
	
}
	
	
	//********************************************************************************************************
	 // RETORNA UMA  LISTA DE Estado 
	//********************************************************************************************************
	public List<Estado> readEstado(){
		

		this.getConexao();
		Estado estado = null;
		List<Estado> listEstado = new ArrayList<>();
		
		String sql = "SELECT  id, nome, sigla FROM estados";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				estado  = new Estado();
				estado.setId_estado( rs.getInt("id"));
				estado.setNome(rs.getString("nome"));
				estado.setSigla(rs.getString("sigla"));
				
				listEstado.add(estado);
			}

			stmt.close();
			return listEstado;

		} catch (SQLException e) {
			System.out.println("Erro ao obter estado: " + e.getMessage());
			e.printStackTrace();
			return listEstado;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
	//*************************************************************************************
	
	
}

//*************************************************************************************
// BEGIN PRIVATE METHODS
//*************************************************************************************	
	
	/*
	 * CHECA SE O ESTADO JA FOI CADASTRADO
	 * RETURN id_estado
	 */
	private Integer CheckByEstado(Estado estado ){
		
		this.getConexao();
		Integer id_estado = 0;
		String sql = "SELECT   id_estado, es_nome  FROM estado WHERE es_nome = ?  ";

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setMaxRows(1);
			stmt.setString(1, estado.getNome().toLowerCase());
			
			// executa
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				id_estado =  (Integer.parseInt( rs.getString("id_estado")));
			}

			stmt.close();
			return id_estado;

		} catch (SQLException e) {
			System.out.println("Erro ao obter estado byId: " + e.getMessage());
			e.printStackTrace();
			return id_estado;
		} finally {
			ConnectionFactory.fecharConexao(this.conexao);
		}
		
	}
	
	
//*************************************************************************************
// END PRIVATE METHODS
//*************************************************************************************	


	
}


