const { leerArchivo, transferir } = require('../src/app');

test('Transferencia entre cuentas', () => {
    const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 30);
    expect(resultado.exito).toBe(true);
    expect(resultado.mensaje).toBe('Transferencia de 30 realizada correctamente de juan.jose@urosario.edu.co a sara.palaciosc@urosario.edu.co.');

    const data = leerArchivo();
    const transaccionesJuan = data['juan.jose@urosario.edu.co'];
    const transaccionesSara = data['sara.palaciosc@urosario.edu.co'];

    expect(transaccionesJuan[transaccionesJuan.length - 1].balance).toBe('-30');
    expect(transaccionesJuan[transaccionesJuan.length - 1].type).toBe('Transferencia');
    expect(transaccionesSara[transaccionesSara.length - 1].balance).toBe('30');
    expect(transaccionesSara[transaccionesSara.length - 1].type).toBe('Transferencia');
});

test('Transferencia con saldo insuficiente', () => {
    const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 1000);
    expect(resultado.exito).toBe(false);
    expect(resultado.mensaje).toBe('Saldo insuficiente en la cuenta de juan.jose@urosario.edu.co.');

    const data = leerArchivo();
    const transaccionesJuan = data['juan.jose@urosario.edu.co'];
    const transaccionesSara = data['sara.palaciosc@urosario.edu.co'];

    // Verificar que no se haya añadido ninguna transacción nueva
    expect(transaccionesJuan[transaccionesJuan.length - 1].balance).not.toBe('-1000');
    expect(transaccionesSara[transaccionesSara.length - 1].balance).not.toBe('1000');
});