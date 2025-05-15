package com.sghss.backend.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilTest {

    @Test
    void deveCriptografarEDescriptografarComSucesso() {
        String original = "dadoSensivel123";
        String encrypted = CryptoUtil.encrypt(original);
        String decrypted = CryptoUtil.decrypt(encrypted);
        assertNotNull(encrypted);
        assertNotEquals(original, encrypted);
        assertEquals(original, decrypted);
    }

    @Test
    void deveLancarExcecaoAoDescriptografarStringInvalida() {
        String invalido = "nao-e-base64";
        assertThrows(RuntimeException.class, () -> CryptoUtil.decrypt(invalido));
    }

    @Test
    void deveLancarExcecaoAoCriptografarValorNulo() {
        assertThrows(RuntimeException.class, () -> CryptoUtil.encrypt(null));
    }

    @Test
    void deveCobrirConstrutorPadrao() {
        // Apenas para cobertura, já que a classe é utilitária
        new CryptoUtil();
    }
} 