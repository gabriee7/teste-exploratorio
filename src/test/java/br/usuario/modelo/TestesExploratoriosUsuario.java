package br.usuario.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;

public class TestesExploratoriosUsuario {
    private Usuario usuario;
    private Usuario usuarioAdmin;
    private RegraUsuarioService service;

    public TestesExploratoriosUsuario(){
        this.service = new RegraUsuarioService();
    }

    @BeforeEach
    public void CriarUsuarios(){
        String senha = "123";
        this.usuario = new Usuario("User", TipoUsuario.NORMAL, senha);
        this.usuarioAdmin = new Usuario("Admin", TipoUsuario.ADMINISTRADOR, senha);
    }

    @Test
    public void ExplorarCriacaoDeUsuario() {
        // Usuário normal deve ter inicialmente o estado Novo
        assertEquals(Novo.class.getSimpleName(), usuario.getNomeEstado(), "Ao ser criado o usuário do tipo NORMAL deve possuir o estado 'Novo'");

        // Usuário administrador deve ter inicialmente o estado Novo
        assertEquals(Novo.class.getSimpleName(), usuarioAdmin.getNomeEstado(), "Ao ser criado o usuário do tipo ADMINISTRADOR deve possuir o estado 'Novo'");
    }
    
    @Test
    public void ExplorarPermissaoAdmin(){

        // Administrador
        usuarioAdmin.setEstado(new BanidoDefinitivo());
        this.service.ativar(this.usuario, this.usuarioAdmin);
    }

    @Test
    public void ExplorarUsuarioNovo() {
        // Tentar desativar usuário novo
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                this.service.desativar(this.usuario, this.usuarioAdmin);
        }, "A mensagem de exceção deve ser lançada ao tentar desativar um usuário novo");

        assertEquals(exception.getMessage(), "Usuário novo não pode ser desativado", "A mensagem de exceção para desativação deve ser correta");


        // Tentar advertir usuário novo
        exception = assertThrows(IllegalStateException.class, () -> {
            this.service.advertir(this.usuario, this.usuarioAdmin);
        }, "A mensagem de exceção deve ser lançada ao tentar advertir um usuário novo");

        assertEquals(exception.getMessage(), "Usuário novo não pode ser advertido", "A mensagem de exceção para advertência deve ser correta");

        // Tentar ativar usuário novo
        this.service.ativar(this.usuario, this.usuarioAdmin);
        assertEquals(Ativo.class.getSimpleName(), this.usuario.getNomeEstado(), "Deve ser possível ativar um usuário novo");

    }
    
    @Test
    public void ExplorarUsuarioAtivo() {
        this.usuario.ativar();

        // Explorar desativar usuário ativo
        this.service.desativar(this.usuario, this.usuarioAdmin);
        assertEquals(Desativado.class.getSimpleName(), this.usuario.getNomeEstado(), "O usuário deve ser desativado e o estado deve ser 'Desativado'");

        // Explorar advertir usuário ativo
        this.usuario.ativar();
        this.service.advertir(this.usuario, this.usuarioAdmin);
        assertEquals(Ativo.class.getSimpleName(), this.usuario.getNomeEstado(), "O usuário deve permanecer no estado 'Ativo' após a primeira advertência");

        // Explorar ativar usuário ativo
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            this.service.ativar(this.usuario, this.usuarioAdmin);
        }, "A mensagem de exceção deve ser lançada ao tentar ativar um usuário que já está ativo");

        assertEquals(exception.getMessage(), "O usuário já está ativo", "A mensagem de exceção deve ser correta");
    }

    @Test
    public void ExplorarUsuarioDesativado() {
        this.usuario.setEstado(new Desativado());

        // Explorar advertir usuário desativado
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            this.service.advertir(this.usuario, this.usuarioAdmin);
        }, "A mensagem de exceção deve ser lançada ao tentar adverti um usuário que está desativado");
        assertEquals(exception.getMessage(), "Usuário desativado não pode ser advertido", "A mensagem de exceção deve ser correta");


        // Explorar desativar usuário desativado
        exception = assertThrows(IllegalStateException.class, () -> {
            this.service.desativar(this.usuario, this.usuarioAdmin);
        }, "A mensagem de exceção deve ser lançada ao tentar desativar um usuário que já está desativado");

        assertEquals(exception.getMessage(), "O usuário já está desativado", "A mensagem de exceção deve ser correta");


        // Explorar ativar usuário desativado
        this.service.ativar(this.usuario, this.usuarioAdmin);
        assertEquals(Ativo.class.getSimpleName(), usuario.getNomeEstado(), "Deve ser possível ativar um usuário desativado");
    }
    
    @Test
    public void ExplorarBanimentoTemporario() {
        this.usuario.ativar();

        // Explorar primeira advertencia
        this.service.advertir(this.usuario, this.usuarioAdmin);
        assertEquals(1, this.usuario.getNumeroDeAdvertencias(), "O número de advertências deve ser 1 após a primeira advertência");
        assertEquals(Ativo.class.getSimpleName(), this.usuario.getNomeEstado(), "O usuário deve permanecer no estado 'Ativo' após a primeira advertência");

        // Explorar segunda advertência
        this.service.advertir(this.usuario, this.usuarioAdmin);
        assertEquals(2, this.usuario.getNumeroDeAdvertencias(), "O número de advertências deve ser 2 após a segunda advertência");
        assertEquals(BanidoTemporario.class.getSimpleName(), this.usuario.getNomeEstado(), "O usuário deve estar banido temporariamente após duas advertências");

        // Explorar advertir usuário banido temporariamente
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            this.service.advertir(this.usuario, this.usuarioAdmin);
        }, "A mensagem de exceção deve ser lançada ao tentar advertir um usuário que está banido temporariamente");
        assertEquals(exception.getMessage(), "Usuário banido temporariamente não pode ser advertido", "A mensagem de exceção deve ser correta");

        // Explorar desativar usuário banido temporariamente
        exception = assertThrows(IllegalStateException.class, () -> {
            this.service.desativar(this.usuario, this.usuarioAdmin);
        }, "A mensagem de exceção deve ser lançada ao tentar desativar um usuário que está banido temporariamente");
        assertEquals(exception.getMessage(), "Usuário banido temporariamente não pode ser desativado", "A mensagem de exceção deve ser correta");

        // Explorar ativar usuário banido temporariamente
        exception = assertThrows(IllegalStateException.class, () -> {
            this.service.ativar(this.usuario, this.usuarioAdmin);
        }, "A mensagem de exceção deve ser lançada ao tentar ativar um usuário que está banido temporariamente");
        assertEquals(exception.getMessage(), "Usuário ainda está banido temporariamente", "A mensagem de exceção deve ser correta");

    }
    
    @Test
    public void BanimentoTemporarioDeveDurar30Segundos() throws InterruptedException {
        this.usuario.ativar();
        this.usuario.setNumeroDeAdvertencias(2);
        
        assertEquals(BanidoTemporario.class.getSimpleName(), this.usuario.getNomeEstado(), "O usuário deve estar banido temporariamente após duas advertências");

        Thread.sleep(30001);

        assertEquals(Ativo.class.getSimpleName(), this.usuario.getNomeEstado(), "Após 30 segundos (banimento temporário), o usuário deve retornar ao estado 'Ativo'");
    
        this.usuario.advertir(); 
        assertEquals(BanidoDefinitivo.class.getSimpleName(), this.usuario.getNomeEstado(), "Usuário banido definitivamente após período de banimento temporário (30s), deve conter o estado 'BanidoDefinitivo' sem possibilidade de retorno para outro estado.");
    
    }


}