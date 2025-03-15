const fs = require('fs');
const path = require('path');

// Función para leer el archivo transactions.txt
function leerArchivo() {
    const rutaArchivo = path.join(__dirname, 'transactions.txt');
    try {
        const data = fs.readFileSync(rutaArchivo, 'utf8');
        return JSON.parse(data);
    } catch (err) {
        console.error('Error al leer el archivo:', err);
        return {};
    }
}

// Función para escribir el archivo transactions.txt
function escribirArchivo(data) {
    const rutaArchivo = path.join(__dirname, 'transactions.txt');
    try {
        fs.writeFileSync(rutaArchivo, JSON.stringify(data, null, 2), 'utf8');
    } catch (err) {
        console.error('Error al escribir el archivo:', err);
    }
}

// Función para calcular el saldo actual de un usuario, basado en sus transacciones
function calcularSaldo(usuario) {
    const data = leerArchivo();
    const transacciones = data[usuario] || [];
    let saldo = 0;
    transacciones.forEach(transaccion => {
        saldo += parseFloat(transaccion.balance);
    });
    return saldo;
}

// Función para realizar la transferencia entre cuentas
function transferir(de, para, monto) {
    const data = leerArchivo();
    const saldoDe = calcularSaldo(de);

    if (saldoDe < monto) {
        return {
            exito: false,
            mensaje: `Saldo insuficiente en la cuenta de ${de}.`
        };
    }

    if (!data[de]) {
        return {
            exito: false,
            mensaje: `La cuenta de origen ${de} no existe.`
        };
    }

    if (!data[para]) {
        return {
            exito: false,
            mensaje: `La cuenta de destino ${para} no existe.`
        };
    }

    // Restar el monto de la cuenta de origen
    data[de].push({
        balance: `-${monto}`,
        type: 'Transferencia',
        timestamp: new Date().toISOString()
    });

    // Sumar el monto a la cuenta de destino
    data[para].push({
        balance: `${monto}`,
        type: 'Transferencia',
        timestamp: new Date().toISOString()
    });

    // Escribir los cambios en el archivo
    escribirArchivo(data);

    return {
        exito: true,
        mensaje: `Transferencia de ${monto} realizada correctamente de ${de} a ${para}.`
    };
}

const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 50);
console.log(resultado.mensaje);

// Exportar las funciones para pruebas
module.exports = { leerArchivo, escribirArchivo, calcularSaldo, transferir };