function initData(){
	forms()
}

function forms() {
	var xhttp = createXHR();
	xhttp.onreadystatechange = handleforms;
	xhttp.open("GET", "/forms", true);
	xhttp.send();
}

function handleforms() {
	
	if (this.readyState == 4 && this.status == 200) {
		var obj = JSON.parse(this.responseText)
		var cont = document.getElementById('formlist')
		for (let i in obj) {
			
			var line = document.createElement('div')
			var name = document.createElement('div')
			
			name.innerText = obj[i].metadata.name
			name.style.float = "left"
			name.style.width = "10em"
			name.title = obj[i].metadata.description
			line.appendChild(name)
			
			if(obj[i].pdf){
				var pdf = document.createElement('a')
				pdf.href = "/form/" + obj[i].uuid + "/pdf"
				var pdficon = document.createElement('img')
				pdficon.src = "pdf.png"
				pdf.appendChild(pdficon)
				line.appendChild(pdf)
			}
			
			if(obj[i].odt){
				var odt = document.createElement('a')
				odt.href = "/form/" + obj[i].uuid + "/odt"
				var odticon = document.createElement('img')
				odticon.src = "odt.png"
				odt.appendChild(odticon)
				line.appendChild(odt)
			}
			
			cont.appendChild(line)
		}
	}
}