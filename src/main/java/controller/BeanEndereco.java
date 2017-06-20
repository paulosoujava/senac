package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import dao.CidadeDAO;
import dao.EnderecoDAO;
import dao.EstadoDAO;
import entity.Cidade;
import entity.Endereco;
import entity.Estado;
import util.SessionUtil;

@ManagedBean
public class BeanEndereco implements Serializable {

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
	
// FALSE CADASTRAR	
	private Boolean is_edit = false;

	// PAGE /ALUNO/INDEX
	private Endereco endereco = new Endereco();
	private Integer idEstado;
	private Integer idCidade;
	
	private List<Cidade> lCidade = new ArrayList<>();
	private Estado estado = new Estado();
	private List<Estado>  lEstado = new ArrayList<>();
	private Cidade cidade = new Cidade();	
	private EnderecoDAO eD = new EnderecoDAO();
	
	
	
	
	public BeanEndereco() {
	
	}

	// EDITAR ALUNO PAGE /ALUNO/ENDERECO
	public String editEndereco( Endereco e ) {
      
		Cidade c = new Cidade();
		c.setId_cidade( idCidade );
		Estado es = new Estado();
		es.setId_estado(idEstado);
		c.setEstado(es);
		
		
		e.setCidade(c);
		System.out.println(e);
			EnderecoDAO eD = new EnderecoDAO();
			if (eD.actionEndereco(this.endereco, (Integer) SessionUtil.getParam("IDALUNO")) > 0) {
				this.msg_icon = ICONSUCCESS;
				this.msg_color = SUCCESS;
				this.msg_erro = "Opa, dados aualizado com sucesso";
			} else {
				this.msg_icon = ICONDANGER;
				this.msg_color = DANGER;
				this.msg_erro = "Opss, por favor estamos passando por uma turbulencia, tente mais tarde";
			}
	

		return null;
	}

	// GET AND SET

	public Endereco getEndereco() {
	
		return endereco;
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

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Integer getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}

	public Integer getIdCidade() {
		return idCidade;
	}

	public void setIdCidade(Integer idCidade) {
		this.idCidade = idCidade;
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

	public Boolean getIs_edit() {
		
		this.endereco = eD.CheckIdPessoaEndereco((Integer) SessionUtil.getParam("IDALUNO"));
		
		if( this.endereco != null ){
			this.is_edit = true;
			
			this.idEstado = endereco.getCidade().getEstado().getId_estado();
			EstadoDAO esD = new EstadoDAO();
			lEstado = esD.readEstado();
		
			 
			  if( lEstado.contains(this.endereco.getCidade().getEstado()) ){
				  lEstado.remove(this.endereco.getCidade().getEstado());
			  }
			  //seto para primeiro para aparecer no arrayList do combo
			  lEstado.add(0, this.endereco.getCidade().getEstado());
			
			  CidadeDAO cidD = new CidadeDAO();
			  lCidade  = cidD.readCidadeByEstado(endereco.getCidade().getEstado());
			
		}
		return is_edit;
	}

	public List<Cidade> getlCidade() {
		return lCidade;
	}

	public List<Estado> getlEstado() {
		return lEstado;
	}

	public Estado getEstado() {
		return estado;
	}

	public Cidade getCidade() {
		return cidade;
	}

	

	
	

}
