package controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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
	protected String msg_erro; 
	
	
	public BeanLogin() { }

   //sair
	public String sair(){
		this.msg_erro = new String();
		this.login = new Login();
		return "../index.xhtml?faces-redirect=true";
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
				//tudo ok por aqui
				return this.checkDaoLogin();
			}
 			
			//cai aqui pelas condicoes empty and validation email
		
	    	return null;
	    	
	  }catch (Exception e) {
		     this.msg_erro = "Opss, você precisa preencher os dados: e-mail e login";	
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
			
			//redireciona
			return "/aluno/index?faces-redirect=true";
		
		case 2: 
			 //COLOCA O ID DA PESSOA   NA SESSAO 
			SessionUtil.setParam("IDPESSOAADM", 1);
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

	//******************************************************

}
