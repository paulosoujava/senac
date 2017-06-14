package util;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import dao.AlunoDAO;
import dao.CursoDAO;
import dao.EgressoAlunosDAO;
import dao.EgressoDAO;
import dao.LoginDAO;
import dao.PessoaDAO;
import entity.Aluno;
import entity.Curso;
import entity.Egresso;
import entity.Login;
import entity.Pessoa;


/*
 * Classe criada com a logica do importe da planiha apos passar por validacao de dados basicos
 * 
 */
public class ImportadorPlanilha {
	
	
	// lista para pessoas que já possui cadastro em nosso sistema
	List<Pessoa> listRejeitoPessoa = new ArrayList<>();

	public boolean importar( InputStream fis, Curso curso, Integer fase, Integer ano) {

		try {


			// Create Workbook instance holding reference to .xlsx file
			HSSFWorkbook planilha = new HSSFWorkbook(fis);

			// Pega a primeira aba da planilha
			HSSFSheet abaPlanilha = planilha.getSheetAt(0);
			
			planilha.close();

			// Obtém o iterador de linhas da planilha escolhida
			Iterator<Row> iteradorLinha = abaPlanilha.iterator();
			
			//Pula a primeira linha (cabeçalho da planilha)
			iteradorLinha.next();
	
			/*
			 *   LOGICA ABAIXO CRIA-SE UMA TABELA DE EGRESSO QUE É A IMPORTACAO
			 *   CHECA SE JA NAO EXISTE NUNHMA IMPORTACAO FEITA COM O ANO FASE E CURSO
			 *   CRIA PARA CADA ALUNO E PESSOA OS DADOS NO BANCO CHECANDO POR CPF PARA PESSOA
			 *   AQUI PODERIA DESCARTAR O CHECK DEMATRICULA DO ALUNO POIS COMO ELE EH UMA
			 *   PESSOA E TEM UM SO CPF JA VALIDAMOS ISTO, ENTAO TORNA SE AMBIGUO
			 */
			
			
			//************************************************************************
			//CRIANDO O EGRESSO
			//************************************************************************
			//EGRESSO DO ALUNO
				Egresso eg = new Egresso();
			//DADOS DO EGRESSO
				java.util.Date dataUtil = new java.util.Date();  
				java.sql.Date dataSql = new java.sql.Date(dataUtil.getTime());
				eg.setDataImporte( dataSql.toString());
				eg.setAno(ano);
				eg.setFase(fase);
				eg.setCurso(curso);
				EgressoDAO egDao = new EgressoDAO();
				
				
				if( egDao.CheckByAnoFaseCurso(curso.getId_curso(), ano, fase) == 0 ){
				
					egDao.incluir(eg);
					
				}else{
					return false;
				}
				
			//************************************************************************
				
				
		    
		    while (iteradorLinha.hasNext()) {
				
				System.out.println(iteradorLinha.toString() );
				
				Row linhaAtual = iteradorLinha.next();
				Aluno aluno = (Aluno) criarAluno(linhaAtual, eg);
				
			
					System.out.println( aluno );

				PessoaDAO pD = new PessoaDAO();
			 	
				//CASO CF NAO EXISTA CADASTRE-A
				if( pD.CheckByCpf(aluno.getCpf()) == 0  ){
					//para nao haver problema em uma tentativa de cast foi feito a criacao de
					//etidade pessoa na mao
					Pessoa p = new Pessoa();
					p.setCpf(aluno.getCpf());
					p.setData_nascimento(aluno.getData_nascimento());
					p.setPrimeiro_nome(aluno.getPrimeiro_nome());
					p.setSexo(aluno.getSexo());
					p.setTelefone(aluno.getTelefone());
					p.setUltimo_nome(aluno.getUltimo_nome());
					
					//INCLUIR UMA PESSOA NO BANCO DE DADOS;
					//RETORNO ID PESSOA CADASTRADA
					int idPessoa =  pD.incluir(p);
					
					
					//************************************************************************
					//CRIANDO O LOGIN TABLE PARA ACESSO A AREA DO ALUNO SENHA 3 PRIMEIRO DIG
					//CPF
					//************************************************************************
						LoginDAO loDao = new LoginDAO();
						 
						if( loDao.isLogin( aluno.getLogin() ) == 0 ){
							Integer idLogin = loDao.incluir(aluno.getLogin());
							
							//id do cadastro do aluno no login
							Login l = new Login();
							l = aluno.getLogin();
							l.setId_login(idLogin);
							aluno.setLogin(l);
							
							
						}
					//************************************************************************
						
					 
						//************************************************************************
						//CRIANDO O ALUNO TABLE
						//************************************************************************
							AlunoDAO alDao = new AlunoDAO();
							//SETANDO O ID DA PESSOA ACIMA NESTE ALUNO POIS OS DADOS PERTENCEM ELE
							aluno.setId_pessoa(idPessoa);
							if( alDao.CheckByMatricula( aluno.getMatricula() ) == 0 ){
								alDao.incluir(aluno);
							}
						//************************************************************************
							 
						
								//************************************************************************
								//CRIANDO O EGRESSO IDS  
								//VIDE  DAO  ( EGRESSOS ALUNOS DAO ) para mais informações
								//************************************************************************
									EgressoAlunosDAO eaDao = new EgressoAlunosDAO();
											eaDao.incluir(aluno.getId_pessoa(), curso.getId_curso());
								
								//************************************************************************
								
							
							
						
				}
			}
			
			//tudo certo jovem
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		}
	}
	
	/****
	 * Method Responsavel por criar entidades ALUNO EGRESSO LOGIN PESSOA  TODO EGRESSO, LOGIN, PESSOA, NESTE CASO AQUI
	 * EH UM ALUNO POR ISSO O RETORNO EH ALUNO
	 * @param row
	 * @return ALUNO
	 */
	
	private Aluno criarAluno(Row row, Egresso eg) {
	
	Cell celulaMatricula = row.getCell(0);
		Cell celulaSituacao = row.getCell(1);
		Cell celulaCpf = row.getCell(2);
		Cell celulaNome = row.getCell(3);
		Cell celulaSobreNome = row.getCell(4);
		Cell celulaEmail = row.getCell(5);
		Cell celulaTelefone = row.getCell(6);
		Cell celulaFrmEgresso = row.getCell(7);
		Cell celulaSexo = row.getCell(8);
		Cell celulaDtaNasc = row.getCell(9);
		
		Aluno aluno = new Aluno();
		   
		//String data = tratarData(celulaDtaNasc.toString());
//        DateFormat formatter = null;
//
//        formatter = new SimpleDateFormat("dd/MM/yyyy");
//
//        if (formatter != null) {
//            java.util.Date dtFormatada;
//			try {
//				dtFormatada =  formatter.parse(data);
//				aluno.setData_nascimento(dtFormatada);   
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//                         
//        }
		
		
		//CRIAMOS UMA ENTIDADE LOGIN PARA JA DISPONIBLIZAR O ACESSO AO SISTEMA
		Login log = new Login();
		
	
		//DADOS PESSOA
		String cpfSemPontos =  celulaCpf.toString() ;
		aluno.setCpf( this.removePontosCpf( cpfSemPontos ) );
		
		aluno.setData_nascimento( this.tratarData( celulaDtaNasc.toString() ));
		aluno.setPrimeiro_nome(celulaNome.toString().toLowerCase());
		aluno.setUltimo_nome(celulaSobreNome.toString().toLowerCase());
		aluno.setSexo(celulaSexo.toString().toLowerCase());
		
		
		
		aluno.setTelefone(celulaTelefone.toString());
		
		
		//DADOS ALUNO
		aluno.setMatricula( celulaMatricula.toString());
		aluno.setForm_egresso(celulaFrmEgresso.toString());
		aluno.setSitucao(celulaSituacao.toString().toLowerCase());
		
		//DADOS LOGIN
	    log.setEmail( celulaEmail.toString() );
	    log.setSenha(cpfSemPontos.substring(0, 3) ); //33 primeiros caracteres do cpf
	    log.setNivel( 1 );  // ADM = 2 ALUNO 1
		
		//DADOS EGRESO
	    eg.setForma_egresso(celulaFrmEgresso.toString().toLowerCase());

		//LOG PARA CONSULTA NO CONSOLE
		System.err.println(aluno.toString());
	

		//login do aluno
		aluno.setLogin(log);
		
		//egresso ao aluno
		aluno.setEgresso(eg);
		
		
		return aluno;
	}
	
	//REMOVENDO TODOS OS PONTOS DO CPF PARA DEIXAR SOMENTE UMA STRING SEM NUMEROS
	private String removePontosCpf(String str ){
		//cpf com somente numeros
				String cpf = " ";
				cpf = str.replace( " " , ""); //tira espaço em branco
				cpf = str.replace( "." , ""); //tira ponto
				cpf = str.replace( "/" , ""); //tira barra
				cpf = str.replace( "-" , ""); //tira hífen
			return (cpf);
	}
	
	//trata data vinda do escle
	private String tratarData(String data) {
        if (data.contains("-")) {
            data = data.replace("-", "/");
            data = data.replace("jan", "01");
            data = data.replace("fev", "02");
            data = data.replace("mar", "03");
            data = data.replace("abr", "04");
            data = data.replace("mai", "05");
            data = data.replace("jun", "06");
            data = data.replace("jul", "07");
            data = data.replace("ago", "08");
            data = data.replace("set", "09");
            data = data.replace("out", "10");
            data = data.replace("nov", "11");
            data = data.replace("dez", "12");

        }
        return data;
    }
}
