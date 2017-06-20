package controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

import dao.AlunoDAO;
import dao.CursoDAO;
import dao.EgressoAlunosDAO;
import dao.EgressoDAO;
import dao.EnderecoDAO;
import dao.PessoaDAO;
import entity.Aluno;
import entity.Curso;
import entity.Endereco;
import entity.Pessoa;
import util.SessionUtil;

@ManagedBean
public class BeanEditarAluno implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String INF = "blue";
	private static final String DANGER = "red";
	private static final String SUCCESS = "green";
	private static final String ICONSUCCESS = "done_all";
	private static final String ICONINF = "info";
	private static final String ICONDANGER = "error_outline";

	// PAGE /ALUNO/INDEX
	private boolean flag_sessao = false;

	private Integer idAluno; // ready-only
	private Pessoa pessoa;
	private Aluno aluno;
	private String msg_erro = new String(); // read-only
	private String msg_color = new String(); // read-only
	private String msg_icon = new String(); // read-only

	public BeanEditarAluno() {
		// Para recuperar o valor. Vai precisar fazer um cast aqui.
		this.idAluno = (Integer) SessionUtil.getParam("IDALUNO");
		
		PessoaDAO pD = new PessoaDAO();
		pessoa = pD.CheckById(idAluno);
		
		AlunoDAO aD = new AlunoDAO();
		
		this.aluno = aD.alunosByID(this.idAluno);
		

	}

	// EDITAR PESSOA
	public String editarPessoa(Pessoa p) {
		
		System.out.println( " -- - - -- - -- -- - - --"+ this.aluno);
		PessoaDAO pD = new PessoaDAO();
		if (pD.editar(p) > 0) {
			this.pessoa = p;
			this.msg_icon = ICONSUCCESS;
			this.msg_color = SUCCESS;
			this.msg_erro = "Opa, dados aualizado com sucesso";
			return "";
		} else {
			this.msg_icon = ICONDANGER;
			this.msg_color = DANGER;
			this.msg_erro = "Opss, por favor estamos passando por uma turbulencia, tente mais tarde";
			return null;
		}
	}

	// EDITAR ALUNO
	public String editarAluno(Aluno a) {
		AlunoDAO aD = new AlunoDAO();
		if (aD.editar(a) > 0) {
			this.msg_icon = ICONSUCCESS;
			this.msg_color = SUCCESS;
			this.msg_erro = "Opa, dados aualizado com sucesso";
			return null;
		} else {
			this.msg_icon = ICONDANGER;
			this.msg_color = DANGER;
			this.msg_erro = "Opss, por favor estamos passando por uma turbulencia, tente mais tarde";
			return null;
		}
	}

	// METHODS DO INDEX ALUNO
	// verifica a sessao caso nao exista redireciona
	// caso exista seta a flaga sessao para true para entrar somene uma vez no
	// setAllDataEdit
	// que é onde o dado do aluno é povoado
	public String getDataPessoa() {

		if ((Integer) SessionUtil.getParam("IDALUNO") == 0) {
			return "/index?faces-redirect=true";
		} else {
			// chama uma vez para popular
			this.setAllDataEdit();
		}

		return null;
	}

	// PEGAR OS DADOS DO ALUNO / PESSOA e seta este bean com os dados do curso
	public void setAllDataEdit() {
		if (!this.flag_sessao) {
			AlunoDAO aL = new AlunoDAO();
			this.aluno = aL.alunosByID((Integer) SessionUtil.getParam("IDALUNO"));

			EgressoAlunosDAO eAd = new EgressoAlunosDAO();
			Integer idCurso = eAd.getIDAlunoEgressoImporte((Integer) SessionUtil.getParam("IDALUNO"));

			CursoDAO cD = new CursoDAO();
			Curso c = cD.readAllDataCursoById(idCurso);
			this.aluno.setCurso(c);

			// SETANDO A FLAG PARA TRUE PARA NAO MAIS ENTRAR NESTA CONSULTA
			this.flag_sessao = true;
		}
	}

	// SAIR MATA A SESSAO DO ID E A FLAG VOLTA A FALSE
	public String sair() {
		SessionUtil.remove("IDALUNO");
    	SessionUtil.remove("IDLOGIN");
		this.flag_sessao = false;
		return "/index?faces-redirect=true";
	}

	// EDITAR ALUNO PAGE /ALUNO/HOME

	public String frmEditPageAluno( Pessoa p ) {

		String sexo = p.getSexo();
		
		if( p != null )
            this.pessoa = p;
		
            //bug do milenio
           this.pessoa.setSexo(sexo);
           
		PessoaDAO pD = new PessoaDAO();
	
		

		if (pD.editar(pessoa) > 0) {
			
			this.msg_icon = ICONINF;
			this.msg_color = INF;
			this.msg_erro = "Opa, dados aualizado com sucesso";
			return null;
		} else {
			this.msg_icon = ICONDANGER;
			this.msg_color = DANGER;
			this.msg_erro = "Opss, por favor estamos passando por uma turbulencia, tente mais tarde";
			return null;
		}

	}



	// ****/****/****/****/****/****/****/****/****/****/****/****/****/****

	// VOLTA PARA LISTA MATA SESSAO DO ID
	public String backList() {
		SessionUtil.remove("IDALUNO");
		return "indexListarEgresso?faces-redirect=true";
	}

		// GET AND SETT
	public Integer getIdAluno() {
		return idAluno;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
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

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}


	


	

}
