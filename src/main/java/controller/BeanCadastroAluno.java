package controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

import dao.AlunoDAO;
import dao.CursoDAO;
import dao.EgressoAlunosDAO;
import dao.LoginDAO;
import dao.PessoaDAO;
import entity.Aluno;
import entity.Curso;
import entity.Login;
import entity.Pessoa;
import util.SessionUtil;

@ManagedBean
public class BeanCadastroAluno implements Serializable {

	private static final String INF = "blue";
	private static final String DANGER = "red";
	private static final String SUCCESS = "green";
	private static final String ICONSUCCESS = "done_all";
	private static final String ICONINF = "info";
	private static final String ICONDANGER = "error_outline";

	// curso do egresso ao qual vai vincular o aluno
	private Integer idCurso; // ready-only
	private Curso curso = new Curso(); // ready-only
	private Aluno aluno = new Aluno();
	private String msg_erro = new String(); // read-only
	private String msg_color = new String(); // read-only
	private String msg_icon = new String(); // read-only
	
	//LOGIN DO ALUNO
	private String email = new String();

	// construtor recebe idCurso da sessao
	public BeanCadastroAluno() {
		// Para recuperar o valor. Vai precisar fazer um cast aqui.
		this.idCurso = (Integer) SessionUtil.getParam("IDCURSOADICAO");
		CursoDAO cD = new CursoDAO();
		this.curso = cD.readAllDataCursoById(this.idCurso);
	}

	// VOLTAR AO INDEX DESTRUINDO A SESSAO
	public String back() {
		SessionUtil.remove("IDCURSOADICAO");
		return "index?faces-redirect=true";
	}

	// CADASTRAR 
	public String cadastro( Aluno a) {
		
		//*FAZER VALICAO PARA DADOS DE PESSOA AQUI
		if( a.getCpf().length() < 3 ){
			return null;
		}
		/*
		 *  LOGICA AQUI É INSERIMOS NA TABELA PESSOA PEGAMOS O IDA DA INSERCAO
		 *  INSERIMOS NO ALUNO COM O ID REFERENCIANDO A PESSOA
		 *  INSERIMOS NO LOGIN REFERENCIANDO O ID DO ALUNO
		 *  E INSERIMOS NO TABELAS DE IDS DOS EGRESSOS COM O ID DA PESSOA E DO CURSO
		 * 
		 */
	
		
		PessoaDAO pD = new PessoaDAO();
		AlunoDAO aD = new AlunoDAO();
		LoginDAO lD = new LoginDAO();
		
		// email senha nivel
		Login lo = new Login(email, a.getCpf().substring(0, 3) ,1 );
		
		Integer idPessoa = pD.incluir(a);
		 // este id é o da pessoa que foi incluido na tabela pessoa
		//precisamos dele para referenciar o aluno 
		a.setId_pessoa(idPessoa);
		
		
		Integer idLogin = lD.incluir(lo);
		lo.setId_login(idLogin);
		a.setLogin(lo);
		
		Integer idAluno =  aD.incluir(a) ;
		
		//inclusao na tabela egresso dos ids
		EgressoAlunosDAO eaD = new EgressoAlunosDAO();
		eaD.incluir(idPessoa, this.idCurso );
		
		
		
		System.out.println( "INCLUINDO : "+ a );
		
		if( idPessoa > 0  && idAluno > 0 ) {
			this.msg_color = SUCCESS;
			this.msg_icon = ICONSUCCESS;
			this.msg_erro = "OPa, o cadastro de dados pessoais foi realizado com sucesso!";
			this.aluno = new Aluno();
			
		}else{
			this.msg_erro = "Opss, a qual o curso pertence este importe?";
			this.msg_color = INF;
			this.msg_icon = ICONINF;
		}
		
		
		return null;
	}


	// GET AND SET
	public Integer getIdCurso() {
		return idCurso;
	}

	public Curso getCurso() {
		return curso;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public String getMsg_erro() {
		return msg_erro;
	}

	public String getMsg_color() {
		return msg_color;
	}

	public String getMsg_icon() {
		return msg_icon;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
