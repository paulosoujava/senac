package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.util.StringUtil;

import entity.Aluno;
import entity.Curso;
import entity.Egresso;
import entity.Login;
import entity.Pessoa;

public class EgressoDAO {
	
	private Connection conexao;

	public EgressoDAO() {
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
		
		public int incluir(Egresso eg) {
  
			
			//**********************************************************************************************************		
			//CHECK SE JA EXISTE CPF CADASTRADO 
			 
		 if( this.CheckByAnoFaseCurso( eg.getCurso().getId_curso() , eg.getAno(), eg.getFase()  ) > 0 ){
				return 2;
			}
			//**********************************************************************************************************	
		
			this.getConexao(); // tem que estar abixo devido o checkByCpf fechar a concexao
			
			int idInserido = 0;
			String sql = "INSERT INTO newEgresso.egresso (eg_dta_importe, id_curso, eg_ano, eg_fase)"
				  	                          + " VALUES (?,                ?,      ?,       ?     );";
			try {
				PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				// seta os valores
				stmt.setString(1,eg.getDataImporte());
				stmt.setInt(2, eg.getCurso().getId_curso());
				stmt.setInt(3, eg.getAno());
				stmt.setInt(4, eg.getFase());
					
				stmt.executeUpdate();

				ResultSet rs = stmt.getGeneratedKeys();

				if (rs.next()) {
					idInserido = rs.getInt(1);
				}

				if (idInserido > 0) {
					System.out.println("Egreso criado com sucesso");
				} else {
					System.out.println("Erro ao inserir egresso");
				}

				stmt.close();

				return idInserido;
				
			} catch (SQLException e) {
				System.out.println("Erro ao inserir egresso: " + e.getMessage());
				throw new RuntimeException(e);
			} finally {
				ConnectionFactory.fecharConexao(this.conexao);
			}
		}
		/**************************************************************
		 * CHECA SE O EGRESSO QUE DESEJA CRIAR JA NAO EXISTE
		 * PARA EXISTIR O ANO E A FASE E O ID DO CURSO TEM QUE BATER
		 ***************************************************************/
		public Integer CheckByAnoFaseCurso(Integer curso, Integer ano, Integer fase ){
			
		
			this.getConexao();
			Integer id_pessoa = 0;
			String sql = "SELECT id_egresso, eg_ano, eg_fase, id_curso FROM egresso WHERE eg_ano = ? AND eg_fase = ? AND id_curso = ?";
			System.out.println(
					"SELECT id_egresso, eg_ano, eg_fase, id_curso FROM egresso WHERE eg_ano = "+ano+" AND eg_fase = "+fase+" AND id_curso = "+curso 
					);
			try {
				PreparedStatement stmt = conexao.prepareStatement(sql);
				stmt.setMaxRows(1);
				stmt.setInt(1, ano);
				stmt.setInt(2, fase);
				stmt.setInt(3, curso);
				
				// executa
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					id_pessoa =  (Integer.parseInt( rs.getString("id_egresso")));
				}

				stmt.close();
		
				return id_pessoa;

			} catch (SQLException e) {
				System.out.println("Erro ao obter egresso  byANOFASECURSO: " + e.getMessage());
				e.printStackTrace();
				return id_pessoa;
			} finally {
				ConnectionFactory.fecharConexao(this.conexao);
			}
			
			
		}
		

		//********************************************************************************************************
		//	RETORNA TODOS OS EGRESSOS 
		//********************************************************************************************************
		public List<Egresso> readAllEgresso(){
			

			this.getConexao();
			List<Egresso> listE = new ArrayList<>();
			
			String sql =" SELECT * FROM egresso" ;
					
			
		
			try {
				PreparedStatement stmt = conexao.prepareStatement(sql);
				// executa
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					Egresso e = new Egresso();
					
					CursoDAO cD = new CursoDAO();
					Curso c = cD.readAllDataCursoById(rs.getInt("id_curso"));
					c.setId_curso( rs.getInt("id_curso") );
					//Egresso
					e.setAno(rs.getInt("eg_ano"));
				
					e.setDataImporte( dataDoImporte( rs.getString("eg_dta_importe") ));
					e.setCurso(c);
					e.setFase(rs.getInt("eg_fase"));
					e.setId_egresso(rs.getInt("id_egresso"));
					
					listE.add(e);
				}

				stmt.close();
				return listE;

			} catch (SQLException e) {
				System.out.println("Erro ao obter lista egresso: " + e.getMessage());
				e.printStackTrace();
				return listE;
			} finally {
				ConnectionFactory.fecharConexao(this.conexao);
			}
		
	}
		
		public List<Aluno> getAllAlunoEgresso( Integer id ){
			
			List<Aluno> listE = new ArrayList<>();
			
			//PEGANDO OS IDS DAS PESSOAS
			EgressoAlunosDAO eaD = new EgressoAlunosDAO();
			List<Integer> idsPessoas = eaD.getAllIDAlunoEgressoImporte(id);
			
			//PEGANDO AS PESSOAS REFERENTE AOS IDS ACIMA
			AlunoDAO aD = new AlunoDAO();
			
			for(int i = 0; i < idsPessoas.size(); i++ ){
				Aluno a = aD.alunosByID(idsPessoas.get(i));
				listE.add(a);
			}
			
			
	  	 return listE;
		}
		
		
		

///*************************************************************************************
//PRIVATE METHODS
//ajusta string import para se compportar como uma data
		private String dataDoImporte(String str ){
			String[] txt = str.split("-");
			return txt[2]+ "/"+txt[1]+"/"+txt[0];
		}

}
