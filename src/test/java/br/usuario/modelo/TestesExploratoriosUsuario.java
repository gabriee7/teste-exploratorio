package br.usuario.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;

public class TestesExploratoriosUsuario {
    private Usuario usuario;

    @BeforeEach
    public void criaUsuarioNormal(){
        String nome = "Jhon";
        String senha = "123";

        this.usuario = new Usuario(nome, TipoUsuario.NORMAL, senha);
    }

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

        try {
            this.usuario.desativar();
        } catch (IllegalStateException e) {
            assertEquals("Usuário novo não pode ser desativado", e.getMessage(), "A mensagem de exceção para desativação deve ser correta");
        }

        try {
            this.usuario.advertir();
        } catch (IllegalStateException e) {
            assertEquals("Usuário novo não pode ser advertido", e.getMessage(), "A mensagem de exceção para advertência deve ser correta");
        }
    }
    
    @Test
    public void UsuarioAtivoDeveSerDesativadoOuAdvertido() {

        this.usuario.ativar(); 

        this.usuario.desativar();
        assertEquals(new Desativado().getClass().getSimpleName(), this.usuario.getNomeEstado(), "O usuário deve ser desativado e o estado deve ser 'Desativado'");

        this.usuario.ativar(); 

        this.usuario.advertir();
        assertEquals(new Ativo().getClass().getSimpleName(), this.usuario.getNomeEstado(), "O usuário deve permanecer no estado 'Ativo' após a advertência");
        assertEquals(1, this.usuario.getNumeroDeAdvertencias(), "O número de advertências deve ser incrementado após a advertência");
    }
}