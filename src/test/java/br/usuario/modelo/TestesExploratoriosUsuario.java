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
    
    @Test
    public void UsuarioAtivoPorAdmin(){
        String nome = "Jhon Doe";
        String senha = "123";

        Usuario usuarioNormal = new Usuario(nome, TipoUsuario.NORMAL, senha);
        Usuario usuarioAdministrador = new Usuario(nome, TipoUsuario.ADMINISTRADOR, senha);
        RegraUsuarioService service = new RegraUsuarioService();
        
        service.ativar(usuarioNormal, usuarioAdministrador);
        
        assertEquals(new Ativo().getClass().getSimpleName(), usuarioNormal.getNomeEstado(), "Usuário normal foi ativado por um administrador e deve conter o estado 'Ativo'");
    }

    @Test
    public void UsuarioNovoDeveSerAtivadoMasNaoDesativadoOuAdvertido() {
        String nome = "Jhon";
        String senha = "123";

        Usuario usuario = new Usuario(nome, TipoUsuario.NORMAL, senha);

        try {
            usuario.desativar();
        } catch (IllegalStateException e) {
            assertEquals("Usuário novo não pode ser desativado", e.getMessage(), "A mensagem de exceção para desativação deve ser correta");
        }

        try {
            usuario.advertir();
        } catch (IllegalStateException e) {
            assertEquals("Usuário novo não pode ser advertido", e.getMessage(), "A mensagem de exceção para advertência deve ser correta");
        }
    }
}
