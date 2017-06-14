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

	
	//seta o endereco no objeto caso exista
	public BeanEndereco() {
		EnderecoDAO eD = new EnderecoDAO();
		Endereco e = new Endereco();
		
		e = eD.CheckIdPessoaEndereco((Integer) SessionUtil.getParam("IDALUNO"));
		if( e != null ){
			
			//DIZ QUE É PRA ATUALIZAR O ENDERECO = TRUE
			this.is_edit = true;
			
			
			this.idCidade = e.getCidade().getId_cidade();
			this.idEstado = e.getCidade().getEstado().getId_estado();
			this.endereco = e;
			
			
			
			//pegando CIdade e estado cadastrado no endereco
			//CIDAE E ESTADO DO USUARIO
			CidadeDAO ciD = new CidadeDAO();
			cidade = ciD.readCidadeById(this.idCidade);
			EstadoDAO esD = new EstadoDAO();
			estado = esD.readEstadoById(cidade.getEstado().getId_estado());
			
			//LISTA DE ESTADO E CIDADE DO BANCO
			
			 //tirando a cidade da lista para nao haver duplicidade
			 this.lEstado = esD.readEstado();
			 this.lCidade = ciD.readCidade();
			 
//			 //SETANDO O ESADO DO USUARIO PARA PRIMEIRO
//			  if( lEstado.contains(estado.getNome()) ){
//				  lEstado.remove(estado.getNome());
//			  }
//			   //seto para primeiro para aparecer no arrayList do combo
//			  lEstado.add(0,estado);
			  
			  //SETANDO A CIDADE PARA PRIMEIRO A SER EXIBIDA
			  if( lCidade.contains(cidade.getNome_cidade()) ){
				  lCidade.remove(cidade.getNome_cidade());
			  }
			   //seto para primeiro para aparecer no arrayList do combo
			  lCidade.add(0,cidade);
			 
	
		}
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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
