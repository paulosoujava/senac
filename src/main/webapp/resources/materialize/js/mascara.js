		/*
		*       Script: Mascaras em Javascript
		*/
		/*Função Pai de Mascaras*/
		function Mascara(o,f){
			v_obj=o
		    v_fun=f
		    setTimeout("execmascara()",1)
		}
		/*Função que Executa os objetos*/
		function execmascara(){
		   v_obj.value=v_fun(v_obj.value)
		}
		/*Função que Determina as expressões regulares dos objetos*/
		function leech(v){
			v=v.replace(/o/gi,"0")
		    v=v.replace(/i/gi,"1")
		    v=v.replace(/z/gi,"2")
		    v=v.replace(/e/gi,"3")
		    v=v.replace(/a/gi,"4")
		    v=v.replace(/s/gi,"5")
		    v=v.replace(/t/gi,"7")
		    return v
		}
		/*Função que permite apenas numeros*/
		function Integer(v){
			return v.replace(/\D/g,"")
		}
		/*Função que padroniza telefone (11) 4184-1241*/
		function Telefone(v){
			v=v.replace(/\D/g,"")                            
		    v=v.replace(/^(\d\d)(\d)/g,"($1) $2") 
		    v=v.replace(/(\d{4})(\d)/,"$1-$2")      
		    return v
		}
		/*Função que padroniza telefone (11) 41841241*/
		function TelefoneCall(v){
			v=v.replace(/\D/g,"")                            
			v=v.replace(/^(\d\d)(\d)/g,"($1) $2")   
			return v
		}
		/*Função que padroniza CPF*/
		function Cpf(v){
			v=v.replace(/\D/g,"")                                   
			v=v.replace(/(\d{3})(\d)/,"$1.$2")         
			v=v.replace(/(\d{3})(\d)/,"$1.$2")         
			v=v.replace(/(\d{3})(\d{1,2})$/,"$1-$2")
			return v
		}
		
		/*Função que padroniza DATA*/
		function Data(v){
		    v=v.replace(/\D/g,"") 
		    v=v.replace(/(\d{2})(\d)/,"$1/$2") 
		    v=v.replace(/(\d{2})(\d)/,"$1/$2") 
		    return v
		}

		//Função que padroniza matricula
		function Matricula(v){
			v=v.replace(/\D/g,"")                                   
			v=v.replace(/(\d{2})(\d)/,"$1.$2")         
			v=v.replace(/(\d{2})(\d)/,"$1.$2")         
			v=v.replace(/(\d{4})(\d)/,"$1.$2")
			v=v.replace(/(\d{5})(\d)/,"$1.$2") 
			return v
		}
	