package br.usuario.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestesExploratoriosUsuario {
    @Test
    public void ExplorarCriacaoDeUsuario() {
        String nome = "Jhon";
        String senha = "123";

        Usuario usuarioNormal = new Usuario(nome, TipoUsuario.NORMAL, senha);
        Usuario usuarioAdministrador = new Usuario(nome, TipoUsuario.ADMINISTRADOR, senha);

        assertEquals(new Novo().toString(), usuarioNormal.getNomeEstado(), "Ao ser criado o usuário do tipo NORMAL deve possuir o estado 'Novo'");
        assertEquals(new Novo().toString(), usuarioAdministrador.getNomeEstado(), "Ao ser criado o usuário do tipo ADMINISTRADOR deve possuir o estado 'Novo'");
    }
}
