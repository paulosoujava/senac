package controller;

import javax.faces.bean.ManagedBean;

import dao.SenhaDAO;
import util.SessionUtil;

@ManagedBean
public class BeanSenha {
	
	private static final String INF = "blue";
	private static final String DANGER = "red";
	private static final String SUCCESS = "green";
	private static final String ICONSUCCESS = "done_all";
	private static final String ICONINF = "info";
	private static final String ICONDANGER = "error_outline";
	private String msg_erro = new String(); // read-only
	private String msg_color = new String(); // read-only
	private String msg_icon = new String(); // read-only
	private String senha;
	private String senha1;
	
	public BeanSenha() {
	
	}
	

	public String alterar(){
		
		if( this.senha.isEmpty() || this.senha1.isEmpty()){
			this.msg_icon = ICONINF;
			this.msg_color = INF;
			this.msg_erro = "Opss, preencha os campos";
			return null;	
		}
		if( this.senha.equals(senha1)){
			
			SenhaDAO sD = new SenhaDAO();
			if( sD.editar(this.senha, this.senha1, (Integer) SessionUtil.getParam("IDLOGIN") ) > 0 ){
				this.msg_icon = ICONSUCCESS;
				this.msg_color = SUCCESS;
				this.msg_erro = "Opa, dados aualizado com sucesso";
				this.senha = new String();
				this.senha1 = new String();
				return "";
			}else{
				this.msg_icon = ICONDANGER;
				this.msg_color = DANGER;
				this.msg_erro = "Opss, por favor estamos passando por uma turbulencia, tente mais tarde";
				return "";
			}
			
			
		} else {
			this.msg_icon = ICONINF;
			this.msg_color = INF;
			this.msg_erro = "Opss, senhas não conferem";
			return null;
		}
	}
	
	//SENHA ADM, 
     public String alterarAdm(){
		
		if( this.senha.isEmpty() || this.senha1.isEmpty()){
			this.msg_icon = ICONINF;
			this.msg_color = INF;
			this.msg_erro = "Opss, preencha os campos";
			return null;	
		}
		if( this.senha.equals(senha1)){
			
			SenhaDAO sD = new SenhaDAO();
			if( sD.editar(this.senha, this.senha1, (Integer) SessionUtil.getParam("IDPESSOAADM") ) > 0 ){
				this.msg_icon = ICONSUCCESS;
				this.msg_color = SUCCESS;
				this.msg_erro = "Opa, dados aualizado com sucesso";
				this.senha = new String();
				this.senha1 = new String();
				return "";
			}else{
				this.msg_icon = ICONDANGER;
				this.msg_color = DANGER;
				this.msg_erro = "Opss, por favor estamos passando por uma turbulencia, tente mais tarde";
				return "";
			}
			
			
		} else {
			this.msg_icon = ICONINF;
			this.msg_color = INF;
			this.msg_erro = "Opss, senhas não conferem";
			return null;
		}
	}
	
	//GET AND SET
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSenha1() {
		return senha1;
	}

	public void setSenha1(String senha1) {
		this.senha1 = senha1;
	}


	public static String getInf() {
		return INF;
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


	
	
}
