 $(function() {
	  $('ul.tabs').tabs();
	  $('select').material_select();
function teste(){
		
		 swal({
			  title: "Opss",
			  text: "Verifique os campos email e senha",
			  type: "info"
			});
		//evt.preventDefault();
	}

function excluir()  
{  
    if(window.confirm("Confirma a ação?")) {  
    	

		 swal({
			  title: "Opss",
			  text: "Verifique os campos email e senha",
			  type: "info"
			});
        document.form["excluir-registro"].submit();  
    }   
}


})
