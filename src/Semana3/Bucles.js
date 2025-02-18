let edad2 = 20;

if (edad %2 == 0) {
    console.log("Es par.");
    
} else {
    console.log("Es impar.");
    
}

let dia = prompt("Elige un número para obtener el mes del año.");
dia = parseInt(dia);
switch (dia) {

    case 1: console.log("Enero"); break;
    case 2: console.log("Febrero"); break;
    case 3: console.log("Marzo"); break;
    case 4: console.log("Abril"); break;
    case 5: console.log("Mayo"); break;
    case 6: console.log("Junio"); break;
    case 7: console.log("Julio"); break;
    case 8: console.log("Agosto"); break;
    case 9: console.log("Septiembre"); break;
    case 10: console.log("Octubre"); break;
    case 11: console.log("Noviembre"); break;
    case 12: console.log("Diciembre"); break;

    default: console.log("Otro día");
    
    
    }

    for (let i = 10; i >= 1; i--) {

        console.log("Número: " + i);
        
        }

let contador = 1;
while (contador <= 1000) {
    console.log("Contador " + contador);
    contador *= 2;
}