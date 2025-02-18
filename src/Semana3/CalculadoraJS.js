
    
function sumar() {
    
    let n1 = parseInt(document.getElementById("num1").value);
    
    let n2 = parseInt(document.getElementById("num2").value);
    
    document.getElementById("resultado").innerText = "Resultado: " + (n1 + n2);
    
    }

function restar() {
    
    let n1 = parseInt(document.getElementById("num1").value);
    
    let n2 = parseInt(document.getElementById("num2").value);
    
    document.getElementById("resultado").innerText = "Resultado: " + (n1 - n2);
    
    }

function multiplicar() {
    
    let n1 = parseInt(document.getElementById("num1").value);
    
    let n2 = parseInt(document.getElementById("num2").value);
    
    document.getElementById("resultado").innerText = "Resultado: " + (n1 * n2);
    
    }

function dividir() {
    
     let n1 = parseInt(document.getElementById("num1").value);
        
    let n2 = parseInt(document.getElementById("num2").value);
        
    document.getElementById("resultado").innerText = "Resultado: " + (n1 / n2);
        
    }
    
