package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import dao.EmpresaDAO;
import dao.ExeperienciaDAO;
import entity.Empresa;
import entity.Experiencia;
import util.SessionUtil;

@ManagedBean
public class BeanExperiencia  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String INF = "blue";
	private static final String DANGER = "red";
	private static final String SUCCESS = "green";
	private static final String ICONSUCCESS = "done_all";
	private static final String ICONINF = "info";
	private static final String ICONDANGER = "error_outline";
	private String msg_erro = new String(); // read-only
	private String msg_color = new String(); // read-only
	private String msg_icon = new String(); // read-only
	private Boolean flag_cadastrar_empresa = false; //renderiza o botao de cadastro de empresa apos criar o curriculo
	
	private Experiencia experiencia = new Experiencia(); // for this-> Target Unreachable, 'null' returned null
	private Empresa empresa = new Empresa();
	private List<Empresa> myListEmpresa = new ArrayList<>();
	
	
	//CONSULTA SE JA EXISTE UM MINI CURRICULO CADASTRADO E SE SIM JA POVOA O OBJE
	public BeanExperiencia() {
	 
		ExeperienciaDAO eDAO = new ExeperienciaDAO();
		Experiencia ex = new Experiencia();
		ex = eDAO.CheckById( (Integer) SessionUtil.getParam("IDALUNO"));
		
		if( ex != null ){
			//se ja existir dados, habilidte o cadastrar empresas
			this.flag_cadastrar_empresa = true;
			this.experiencia = ex;
			
			//empresas
			EmpresaDAO eD = new EmpresaDAO();
			this.myListEmpresa =  eD.readEmpresaIdAluno((Integer) SessionUtil.getParam("IDALUNO"));
		}
	}
	
	//CADASTRAR EMPRESA NOVO
	public String cadastrarEmpresaNova(){
		return "indexCadastrarEmpresaNova";
	}
	
	//EDITAR
	public String editar(Experiencia ex ){
		if(ex != null ){
			//setando o id do aluno
			ex.setId_pessoa( (Integer) SessionUtil.getParam("IDALUNO") );
			this.experiencia = ex;
			ExeperienciaDAO exD = new ExeperienciaDAO();
			if( exD.editar(ex) > 0 ){
			
				this.msg_color = SUCCESS;
				this.msg_icon = ICONSUCCESS;
				this.msg_erro = "OPa, atualizado realizado com sucesso, cadastre empresa logo abaixo.";
				
			}else{
				this.msg_color = DANGER;
				this.msg_icon = ICONDANGER;
				this.msg_erro = "Opss, falha nossa por favor tente mais tarde. ";
				
			}
			
		}else{
			this.msg_erro = "Opss, preencha os dados.";
			this.msg_color = INF;
			this.msg_icon = ICONINF;
		}
		return null;
	}
	
	
	//CADASTRAR MINICURRICULO
	public String cadastrar(Experiencia ex ){
		
		if(ex != null ){
			//setando o id do aluno
			ex.setId_pessoa( (Integer) SessionUtil.getParam("IDALUNO") );
			this.experiencia = ex;
			ExeperienciaDAO exD = new ExeperienciaDAO();
			if( exD.incluir(ex) > 0 ){
			
				this.msg_color = SUCCESS;
				this.msg_icon = ICONSUCCESS;
				this.msg_erro = "OPa, cadastro realizado com sucesso, cadastre empresa logo abaixo.";
				this.flag_cadastrar_empresa = true;
			}else{
				this.msg_color = DANGER;
				this.msg_icon = ICONDANGER;
				this.msg_erro = "Opss, falha nossa por favor tente mais tarde. ";
				
			}
			
		}else{
			this.msg_erro = "Opss, preencha os dados.";
			this.msg_color = INF;
			this.msg_icon = ICONINF;
		}
		return null;
	}
	
	
	//ADD NOVA EMPRESA
	public String add(Empresa empresa){
		
		if(empresa != null ){
			//setando o id do aluno
			empresa.setIdAluno( (Integer) SessionUtil.getParam("IDALUNO") );
			this.empresa = empresa;
			EmpresaDAO eD = new EmpresaDAO();
			if( eD.incluir(this.empresa) > 0 ){
			
				this.msg_color = SUCCESS;
				this.msg_icon = ICONSUCCESS;
				this.msg_erro = "OPa, cadastro realizado com sucesso.";
				this.empresa = new Empresa();
				
				
			}else{
				this.msg_color = DANGER;
				this.msg_icon = ICONDANGER;
				this.msg_erro = "Opss, falha nossa por favor tente mais tarde. ";
				
			}
			
		}else{
			this.msg_erro = "Opss, preencha os dados.";
			this.msg_color = INF;
			this.msg_icon = ICONINF;
		}
		return null;
	}
	
	//EDITAR EMPRESA
	public String editarEmpresa(Empresa e){
		System.out.println(e + " --- -- --- ");
		if(e != null ){
			this.empresa = e;
			
			EmpresaDAO eD = new EmpresaDAO();
			if( eD.editar(this.empresa) > 0 ){
			
				this.msg_color = SUCCESS;
				this.msg_icon = ICONSUCCESS;
				this.msg_erro = "OPa, atualização realizada com sucesso.";
				
				
				
			}else{
				this.msg_color = DANGER;
				this.msg_icon = ICONDANGER;
				this.msg_erro = "Opss, falha nossa por favor tente mais tarde... ";
				
			}
			
		}else{
			this.msg_erro = "Opss, preencha os dados.";
			this.msg_color = INF;
			this.msg_icon = ICONINF;
		}
		
		return null;
	}
	
	//REDIRECIONA P/ EMPRESA
	public String goToEmpresa(Empresa e){
		this.empresa = e;
		return "indexEditarEmpresa";
	}
	//DELETAR EMPRESA
	public String deleteEmpresa( Empresa e ){
		
		System.out.println("DELETAR EMRPESA "+ e.getEmpresa());
		if( e != null){
			
			this.empresa = e;
			
			EmpresaDAO eD = new EmpresaDAO();
			
		 if( eD.deleteEmpresa(this.empresa)  ){
			 this.msg_color = SUCCESS;
				this.msg_icon = ICONSUCCESS;
				this.msg_erro = "Opa, cadastro realizado com sucesso.";
				//reexibe lista
				this.myListEmpresa =  eD.readEmpresaIdAluno((Integer) SessionUtil.getParam("IDALUNO"));
				
			 
		 }else{
			 this.msg_color = DANGER;
				this.msg_icon = ICONDANGER;
				this.msg_erro = "Opss, falha nossa por favor tente mais tarde. ";
		 }
		}
		return null;
	}
	
	//voltar para curriculo
	public String back(){
		 return "indexExperiencia";
	}
	

	//GET AND SET
	
	public static String getInf() {
		return INF;
	}
	public Experiencia getExperiencia() {
		return experiencia;
	}

	public void setExperiencia(Experiencia experiencia) {
		this.experiencia = experiencia;
	}

	public static String getDanger() {
		return DANGER;
	}
	public static String getSuccess() {
		return SUCCESS;
	}
	public static String getIconsuccess() {
		return ICONSUCCESS;
	}
	public static String getIconinf() {
		return ICONINF;
	}
	public static String getIcondanger() {
		return ICONDANGER;
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
	public Boolean getFlag_cadastrar_empresa() {
		return flag_cadastrar_empresa;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Empresa> getMyListEmpresa() {
		return myListEmpresa;
	}
	
	
	
	

}
