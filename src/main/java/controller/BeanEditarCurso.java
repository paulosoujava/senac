package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

import dao.CidadeDAO;
import dao.CursoDAO;
import dao.EstadoDAO;
import entity.Cidade;
import entity.Curso;
import entity.Estado;

@ManagedBean
@SessionScoped
public class BeanEditarCurso implements Serializable {

	/**
	 * Bean responsavel por exlusao , e alteracao de cursos
	 */
	private static final long serialVersionUID = -1187551453346659702L;

	private Boolean pesquisar_flag = false; // flag para liberar tabela do
											// fomeulario de pesquisa
	
	private String str_pesq = new String(); // usado para o formulario pesquisa
	private List<Curso> myFormPes = new ArrayList<>();
	
	private Curso curso = new Curso();
	private CursoDAO cD = new CursoDAO();
	private String msg_erro = new String(); // read-ony
	private String colorMenssage = new String(); // read-only
	
	private String id_estado = new String();
	private String id_cidade = new String();
	private List<Cidade> listCidades = new ArrayList<>();
	

	public BeanEditarCurso() {
	}
	
	

	 //edicao em escopo sem redirecionamento
	 // paramentros sao os que estao no combo ou seja o que ele quer editar
	 public String redirectEdicao( Curso c ){
		
		 this.curso = c;
			this.id_cidade = String.valueOf( curso.getCidade());
			this.id_estado = String.valueOf( curso.getCidade().getEstado().getId_estado());
			
		 
			//pegando a cidade do qual esta cadastrado o curso para a edicao
			CidadeDAO cD = new CidadeDAO();
			Estado e = new Estado();
			 e.setNome(curso.getCidade().getEstado().getNome());
			 e.setId_estado(Integer.parseInt(this.id_estado ));
			 
		
			 //tirando a cidade da lista para nao haver duplicidade
			 this.listCidades = cD.readCidadeByEstado(e);
			 
			  if( listCidades.contains(curso.getCidade()) ){
				   listCidades.remove(curso.getCidade());
			  }
			  //seto para primeiro para aparecer no arrayList do combo
			 listCidades.add(0, curso.getCidade());
			 
			
			
			return "/admin/indexEditarCurso.xhtml";
	}	 

//****
	 //combobox
	 
		// ***************************************************************************
		// BANCO DE DADOS LISTAS ESTADOS E CIDADES
		// Chamada para popular o combo de cidades referente ao id passao
		// ****************************************************************************
		public void ShowCidades(ValueChangeEvent event) {
			
			this.id_estado = event.getNewValue().toString();
			  System.out.println("CHENGE VALUR PARA ID "+ id_estado );
		 	CidadeDAO cD = new CidadeDAO();
				Estado e = new Estado();
				e.setId_estado(Integer.parseInt(this.id_estado));
				this.listCidades = cD.readCidadeByEstado(e);

			

		} 
		public List<Estado> getEstados() {
			EstadoDAO eD = new EstadoDAO();
			
			
			return eD.readEstado();
		}
	 
	 
//***	 
	
	

	/************************************************
	 * LISTA DE CURSOS CADASTRADO NO BANCO
	 *
	 */
	public List<Curso> listaCurso() {
		return cD.readAllCurso();
	}


	
	// ****************************************************
	//  EDITAR 
	//   FAZ A ALTERACAO DOS DADOS
	// ****************************************************
	public String editarDados(Curso curso) {

			if (curso.getFase_curso() == 0 || curso.getUrl_image().isEmpty() || curso.getNome_curso().isEmpty()
					|| curso.getFormacao_curso().isEmpty() || curso.getSigla_curso().isEmpty()) {

				   this.msg_erro = "Opss, você precisa preencher todos os dados.";
				   this.colorMenssage = "blue";
				   return null;
			} else {

				 //associando o id da cidade
				  curso.setId_cidade_curso(Integer.parseInt( this.id_cidade )); 
				   Integer ok = cD.editar(curso);
				   
				   System.out.println( "Retorno "+ ok +"curso a cadastrar ===== "+ curso.getCidade().getNome_cidade() + "id "+ this.id_cidade );
				   switch (ok) {
				case -1:
					 this.msg_erro = "Opss, ja existe um curso chamado "+curso.getNome_curso()+" na unidade "+ curso.getCidade().getNome_cidade() + " - "+ curso.getCidade().getEstado().getNome();
						this.colorMenssage = "blue";
					return null;
				case 1:
					this.msg_erro = "Opa, curso alterado com sucesso!!";
					this.colorMenssage = "green";
					return null;

				default:
					this.colorMenssage = "red";
					this.msg_erro = "Opss, falha nossa, por favor tente mais tarde";
					return null;
		
				}
				 
			}
		

		}

	/************************************************
	 * v DELETAR CURSO
	 *
	 ************************************************/
	public String deletar(Curso cu) {

		if (cD.deleteCurso(cu)) {
			this.msg_erro = "Opa, curso deletado com sucesso!!";
			this.colorMenssage = "green";
			this.listaCurso();
		} else {
			this.colorMenssage = "red";
			this.msg_erro = "Opss, falha nossa, por favor tente mais tarde";
		}
		return null;
	}
  // faz o retorno da pagina
	 public String voltar(){
		 this.msg_erro = "";
		 this.colorMenssage = "";
		 return "/admin/indexListaCurso?faces-redirect=true";
	 }
	
	/************************************************
	 *  FORMULARIO DE PESQUISA
	 *
	 ************************************************/
	public List<Curso> showPesquisa(String str) {
		if (!str.equals("")) {
			this.pesquisar_flag = true;
			System.out.println(str + pesquisar_flag);
			this.myFormPes = cD.readCursoByName(str);
		}
		return this.myFormPes;
	}
	
	public String limpaPesquisa(){
		this.pesquisar_flag = false;
		this.msg_erro = "";
		this.str_pesq = "";
		return "/admin/indexListarCurso?faces-redirect=true";
	}

	/************************************************
	 * GETT AND SETT
	 */


	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Boolean getPesquisar_flag() {
		return pesquisar_flag;
	}

	public void setPesquisar_flag(Boolean pesquisa_flag) {
		this.pesquisar_flag = pesquisa_flag;
	}

	public List<Curso> getMyFormPes() {
		return myFormPes;
	}

	public void setMyFormPes(List<Curso> myFormPes) {
		this.myFormPes = myFormPes;
	}

	public String getStr_pesq() {
		return str_pesq;
	}

	public void setStr_pesq(String str_pesq) {
		this.str_pesq = str_pesq;
	}

	public String getMsg_erro() {
		return msg_erro;
	}

	public String getColorMenssage() {
		return colorMenssage;
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

	public List<Cidade> getListCidades() {
		return listCidades;
	}

	public void setListCidades(List<Cidade> listCidades) {
		this.listCidades = listCidades;
	}



}
