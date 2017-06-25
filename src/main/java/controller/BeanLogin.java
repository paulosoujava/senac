package controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dao.LoginDAO;
import entity.Aluno;
import entity.Login;
import entity.Pessoa;
import util.SessionUtil;


/*
 * Bean responsável pelo acesso ao sistemas 
 * fazendo toda a válidaçáo para o devido acesso
 *  */

@ManagedBean
@SessionScoped
public class BeanLogin implements Serializable {


	private static final long serialVersionUID = 1L;
	
	protected Login login = new Login();
	// mensagens de erros referente a validacoes
	//read-only
	private String msg_erro; 
	private Boolean tem;
	
	HttpSession sessao;
	HttpServletRequest request;
	FacesContext context;
	
	
	public BeanLogin() { }

   //sair
	public String sair(){
		this.msg_erro = new String();
		this.login = new Login();
		//sessao.invalidate();
		if( SessionUtil.getParam("LOGADO").equals("LOGADO")){
			SessionUtil.remove("LOGADO");
		}
		return "../index.xhtml";
	}
	
	//************************************************************
	// CHECK VALIDATION DATA  IN LOGIN  
	//************************************************************
	public String checkDataLogin( Login login ){
		this.login = login;
	  try{
		  
			if( login.getEmail().isEmpty()  ||  login.getSenha().isEmpty() ){
			
				this.msg_erro = "Opss, você precisa preencher os dados: e-mail e login";
			
			}else if( !this.validationEmail( login.getEmail() )){
				
				this.msg_erro = "Opss, você precisa digitar um e-mail válido. Ex.: nome@dominio.com";
				
			}else{
				
				
				//Obtem a Sessao
//				 context = FacesContext.getCurrentInstance();
//				 request = (HttpServletRequest) context.getExternalContext().getRequest();
//				 sessao = request.getSession();
				//tudo ok por aqui
				return this.checkDaoLogin();
			}
 			
			//cai aqui pelas condicoes empty and validation email
		
	    	return null;
	    	
	  }catch (Exception e) {
		     this.msg_erro = "Opss, você precisa preencher os dados: e-mail e logins";	
		     return null;
	       }
	  
	}
	
	
	
	//************************************************************
	// CHECK DATA IN DAO  
	//************************************************************
	private String checkDaoLogin(){
		
		//consulta DAO
		LoginDAO lD = new LoginDAO();
	
		
		Integer myNivel = lD.isLogin(this.login);
		
		switch (myNivel) {
		
		case 1:
			
			// BUSCA O ID DA PESSOA CASO NAO ACHE RETORNA IMEDIATAMENTE
			 Aluno a  = lD.geIdPessoLogin(this.login.getEmail(), this.login.getSenha());
			 if( a.getId_pessoa() == 0 ){
				  this.msg_erro = "Opss, não encontrei ninguem com estes dados. Procure a secretaria.";	
				    return null; 
			 }
			 
			 //COLOCA O ID DA PESSOA E DO LOGIN  NA SESSAO 
			SessionUtil.setParam("IDALUNO",a.getId_pessoa() );
			SessionUtil.setParam("IDLOGIN",a.getLogin().getId_login() );
			//sessao.setAttribute("pessoaLogada", this.login);
			//redireciona
			return "/aluno/index?faces-redirect=true";
		
		case 2: 
			 //COLOCA O ID DA PESSOA   NA SESSAO 
			SessionUtil.setParam("IDPESSOAADM", 1);
			SessionUtil.setParam("LOGADO", "LOGADO");
		//	sessao.setAttribute("pessoaLogada", this.login);
			return "/admin/index?faces-redirect=true";
			
		
		// nao possui cadastro no sistema
		default: 
			 this.msg_erro = "Opss, você não possui um cadastro, entre em contato com a secretária de sua faculdade.";
			return null;
		}
	}
	

	
	//************************************************************
	//VALIDATION FRONT-END  BACK SIDE ( VALIDACAO NO SERVIDOR )
	//************************************************************
	 private boolean validationEmail(String email){
		 String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
         java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
         java.util.regex.Matcher m = p.matcher(email);
         return m.matches();
	 }
	 
//****************************************************************************************************************
//##END  PRIVATE METHOD  	
//****************************************************************************************************************	
	 
	 // implemen��o do filtro
	 
	 
//	 private Aluno aluno;
//	 private String emailInformado;
//	 private String senhaInformada;
//
//	 public String autenticar() {
//
//			String paginaDestino;
//
//			// Chamar o DAO para verificar se o login e a senha est�o corretos
//			LoginDAO dao = new LoginDAO();
//			Aluno pessoaFazendoLogin = dao.geIdPessoLogin(emailInformado, senhaInformada);
//			
//			//Obtem a Sessao
//			FacesContext context = FacesContext.getCurrentInstance();
//			HttpServletRequest request = (HttpServletRequest) 
//					context.getExternalContext().getRequest();
//			
//			HttpSession sessao = request.getSession();
//			// Caso sim:
//			if(pessoaFazendoLogin != null){
//				// Adicionar a pessoa na Session
//				sessao.setAttribute("pessoaLogada", pessoaFazendoLogin);
//				
//				// encaminhar para a p�gina de sucesso
//				paginaDestino = "/aluno/index.xhtml?faces-redirect=true";
//			}else{
//				sessao.invalidate();
//				
//				// Caso contr�rio: encaminhar para a p�gina de erro
//				paginaDestino = "/index.xhtml";
//			}
//
//			return paginaDestino;
//		}
//
//		public Aluno getAluno() {
//			if(this.aluno == null){
//				//Recupera da sessao
//				
//				//Obtem a Sessao
//				FacesContext context = FacesContext.getCurrentInstance();
//				HttpServletRequest request = (HttpServletRequest) 
//						context.getExternalContext().getRequest();
//				
//				HttpSession sessao = request.getSession();
//				aluno = (Aluno) sessao.getAttribute("pessoaLogada");
//			}
//			
//			return aluno;
//		}
		

	
	
	
	//****************************************************
	// GET AND SET
	//****************************************************
	public Login getLogin() {
		return login;
	}
	
	
	

	public void setLogin(Login login) {
		this.login = login;
	}



	public String getMsg_erro() {
		return msg_erro;
	}

	public Boolean getTem() {
		this.tem = (SessionUtil.getParam("LOGADO").equals("LOGADO"));
		return tem;
	}

	public void setTem(Boolean tem) {
		this.tem = tem;
	}

	
	
	
	//******************************************************

}
