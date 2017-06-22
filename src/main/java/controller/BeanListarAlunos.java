package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import dao.AlunoDAO;
import dao.EgressoDAO;
import entity.Aluno;
import entity.Egresso;
import util.SessionUtil;

@ManagedBean
public class BeanListarAlunos implements Serializable{

	
	private List<Aluno> listTableAluno = new ArrayList<>();
	private List<Egresso> listTablePessoa = new ArrayList<>();
	private Integer idCurso;
	
	
	//RECUPERAMOS O ID DO CURSO CONFORME ALUNOS DO EGRESSO
	public BeanListarAlunos() {
		//Para recuperar o valor. Vai precisar fazer um cast aqui.
		this.idCurso =  (Integer) SessionUtil.getParam("IDCURSO");
	}
	
	
	// exibe a lista de alunos do egresso clicado
	public List<Aluno> getListTableAluno() {
		EgressoDAO eD = new EgressoDAO();
		this.listTableAluno = (eD.getAllAlunoEgresso(idCurso));
		return listTableAluno;
	}
	
	//lista todos os alunos
	public List<Aluno> listTablePessoa() {
		AlunoDAO aD = new AlunoDAO();
		return  aD.alunosTodosALunos();
	}
	//REDIRECIONA PARA MODO EDICAO
	public String modoExebicao(Integer idAluno){
		//Para guardar na session
		SessionUtil.setParam("IDALUNO",idAluno );
		return "indexEditarAluno?faces-redirect=true";
	}
	//VOLTA PARA INDEX MATA SESSAO DO ID
		public String backList(){
			SessionUtil.remove("IDCURSO");
			return "index?faces-redirect=true";
		}


}
