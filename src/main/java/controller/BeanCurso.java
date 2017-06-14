package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import dao.CidadeDAO;
import dao.CursoDAO;
import dao.EstadoDAO;
import entity.Cidade;
import entity.Curso;
import entity.Estado;

/*
 * Bean responsavel pelo cadastro de curso
 * 
 */

@ManagedBean
@SessionScoped
public class BeanCurso implements Serializable {

	private static final long serialVersionUID = -2443304877545209122L;

	private Curso curso = new Curso(); // sempre iniciar o objeto para q nao
											// de Target Unreachable, 'null'
											// returned null
	private String msg_erro; // read-only
	private String id_cidade;
	private String id_estado;
	private List<Estado> estados = new ArrayList<>(); // read-only
	private List<Cidade> cidades = new ArrayList<>(); // read-only
	private String colorMenssage = "blue"; // read-only

	// usada para quando o usuario clica no botao de pesquisar estado sem
	// fornecer nada
	private boolean flat_erro_estado = false; // read-only

	// ************************************************************
	// INIT OBJ
	// ************************************************************
	public String init(Curso curso) {

		try {

			// id da cidade a qual o curso pertence
			curso.setId_cidade_curso(Integer.parseInt(this.id_cidade));
			this.curso = curso;

			return this.checkDataCurso(curso);

		} catch (Exception e) {
			System.out.println(" ----------------------------------------- C");
			this.msg_erro = "Opss, falha nossa, tente mais tarde por favor";
			this.colorMenssage = "red";
			return null;
		}
	}

	// ********************************************************************************************************
	// ##BEGIN PRIVATE EMETHOD
	// ********************************************************************************************************
	// ****************************************************
	//  CADASTRAR
	// ****************************************************
	private String checkDataCurso(Curso curso) {

		if (curso.getFase_curso() == 0 || curso.getUrl_image().isEmpty() || curso.getNome_curso().isEmpty()
				|| curso.getFormacao_curso().isEmpty() || curso.getSigla_curso().isEmpty()) {

			   this.msg_erro = "Opss, você precisa preencher todos os dados.";

		} else {
			// tudo ok por aqui
			 return this.checkDaoCurso();

		}

		// cai aqui pelas condicoes empty

		return null;

	}

	// ************************************************************
	// CHECK DATA IN DAO
	 // todo retorno sera para a mesma pagina de cadastro de curso
	// ************************************************************
	private String checkDaoCurso() {

		// consulta DAO
		CursoDAO cD = new CursoDAO();

		Integer myCourseId = cD.incluir(this.curso);

		if (myCourseId == -1) {
			this.msg_erro = "Opss, já existe o curso chamado: "+ curso.getNome_curso() + ", cadastrado para a cidade seleionada abaixo.";
			this.colorMenssage = "blue";
		} else if (myCourseId == 0) {
			this.msg_erro = "Opss, Por favor tente mais tarde por favor..";
			this.colorMenssage = "red";
			
		} else if (myCourseId >= 1) {
			this.curso = new Curso();  // limpando os campos dos inuts
			this.cidades = new ArrayList<>();// limpando os campos do combo
			this.colorMenssage = "green";
			this.id_estado = new String();// tornando o rendered para default e nao eibir o formulario
			this.msg_erro =  "Opaa, Curso cadastrado com sucesso";
			
		}
       return null;
	}

	// ****************************************************
	// GET AND SET
	// ****************************************************
	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public String getMsg_erro() {
		return msg_erro;
	}

	public String getId_estado() {
		return id_estado;
	}

	public void setId_estado(String id_estado) {
		this.id_estado = id_estado;
	}

	public String getId_cidade() {
		return id_cidade;
	}

	public void setId_cidade(String id_cidade) {
		this.id_cidade = id_cidade;
	}

	public boolean isFlat_erro_estado() {
		return flat_erro_estado;
	}

	public String getColorMenssage() {
		return colorMenssage;
	}

	// ***************************************************************************
	// BANCO DE DADOS LISTAS ESTADOS E CIDADES
	// Chamada para popular o combo de cidades referente ao id passao
	// ****************************************************************************
	public void ShowCidades(ValueChangeEvent event) {
		
		this.id_estado = event.getNewValue().toString();
		  
		if (!id_estado.equals( "nothing" )) {
			flat_erro_estado = false;

			 //limpa o aviso de erro apo's cadastro realizado com sucesso
			this.msg_erro = new String();
			CidadeDAO cD = new CidadeDAO();
			Estado e = new Estado();
			e.setId_estado(Integer.parseInt(this.id_estado));
			this.cidades = cD.readCidadeByEstado(e);

		} else {
			// limpa o id_estado que esta com "nothing"
			flat_erro_estado = true;
			this.id_estado = "";
		}

	}


	// ******************************************************************************
	// GET QUE PEGA DO BANCO ESTADO E CIDADE
	// ******************************************************************************
	public List<Cidade> getCidades() {
		return cidades;
	}

	public List<Estado> getEstados() {
		EstadoDAO eD = new EstadoDAO();
		return eD.readEstado();
	}
	// ********************************************************************************

}
