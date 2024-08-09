package br.usuario.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestesExploratoriosUsuario {
    private Usuario usuario;
    private Usuario usuarioAdmin;
    private RegraUsuarioService service;

    public TestesExploratoriosUsuario() {
        this.service = new RegraUsuarioService();
    }

    @BeforeEach
    public void CriarUsuarios() {
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
    public void ExplorarBanimentoTemporario() throws InterruptedException {
        final int TEMPO_BANIMENTO_TEMPORARIO = 30000;
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


        // Explorar tempo de banimento temporário
        Thread.sleep(TEMPO_BANIMENTO_TEMPORARIO + 1000);
        assertEquals(Ativo.class.getSimpleName(), this.usuario.getNomeEstado(), "Após " + TEMPO_BANIMENTO_TEMPORARIO/1000 + " segundos (banimento temporário), o usuário deve retornar ao estado 'Ativo'");
    }

    @Test
    public void ExplorarBanimentoPermanente() {
        // Explorar banimento permanente na terceira advertência
        this.usuario.ativar();
        this.usuario.setNumeroDeAdvertencias(2);

        this.service.advertir(this.usuario, this.usuarioAdmin);
        assertEquals(BanidoDefinitivo.class.getSimpleName(), this.usuario.getNomeEstado(), "Usuário deve ser banido permanentemente quando for advertido pela terceira vez");

        // Explorar troca de estados de um usuário banido permanentemente
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            this.service.desativar(this.usuario, this.usuarioAdmin);
        }, "A mensagem de exceção deve ser lançada ao tentar desativar um usuário banido");
        assertEquals(exception.getMessage(), "Usuário banido definitivamente não pode ser desativado", "A mensagem de exceção deve ser correta");

        exception = assertThrows(IllegalStateException.class, () -> {
            this.service.ativar(this.usuario, this.usuarioAdmin);
        }, "A mensagem de exceção deve ser lançada ao tentar ativar um usuário banido");
        assertEquals(exception.getMessage(), "Usuário banido definitivamente não pode ser ativado", "A mensagem de exceção deve ser correta");

        exception = assertThrows(IllegalStateException.class, () -> {
            this.service.advertir(this.usuario, this.usuarioAdmin);
        }, "A mensagem de exceção deve ser lançada ao tentar advertir um usuário banido");
        assertEquals(exception.getMessage(), "Usuário banido definitivamente não pode ser advertido", "A mensagem de exceção deve ser correta");
    }

    @Test
    public void ExplorarPermissaoAdmin() {
        // Explorar permissões do administrador para alterar status de um usuário normal
        this.service.ativar(this.usuario, this.usuarioAdmin);
        assertEquals(Ativo.class.getSimpleName(), this.usuario.getNomeEstado(), "Um administrador deve poder ativar um usuário novo");

        this.service.advertir(this.usuario, this.usuarioAdmin);
        assertEquals(1, this.usuario.getNumeroDeAdvertencias(), "Um administrador deve poder advertir um usuário ativo");

        this.service.desativar(this.usuario, this.usuarioAdmin);
        assertEquals(Desativado.class.getSimpleName(), this.usuario.getNomeEstado(), "Um administrador deve poder desativar um usuário ativo");



        // Explorar permissões do administrador para alterar status de um outro administrador
        Usuario administrador2 = new Usuario(usuarioAdmin.getNome(), TipoUsuario.ADMINISTRADOR, "123");
        this.service.ativar(administrador2, this.usuarioAdmin);
        assertEquals(Ativo.class.getSimpleName(), administrador2.getNomeEstado(), "Um administrador deve poder ativar um administrador novo");

        this.service.advertir(administrador2, this.usuarioAdmin);
        assertEquals(1, administrador2.getNumeroDeAdvertencias(), "Um administrador deve poder advertir um administrador ativo");

        this.service.desativar(administrador2, this.usuarioAdmin);
        assertEquals(Desativado.class.getSimpleName(), administrador2.getNomeEstado(), "Um administrador deve poder desativar um administrador ativo");



        // Explorar se usuário normal não consegue alterar permissões
        Usuario usuarioNormal = new Usuario(this.usuario.getNome(), TipoUsuario.NORMAL, "123");
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            this.service.ativar(this.usuario, usuarioNormal);
        }, "A mensagem de exceção deve ser lançada quando um usuário não tem permissões para ativar outro");
        assertEquals(exception.getMessage(), "Ação permitida apenas para administradores", "A mensagem de exceção deve ser correta");

        this.usuario.setEstado(new Ativo());
        exception = assertThrows(SecurityException.class, () -> {
            this.service.advertir(this.usuario, usuarioNormal);
        }, "A mensagem de exceção deve ser lançada quando um usuário não tem permissões para advertir outro");
        assertEquals(exception.getMessage(), "Ação permitida apenas para administradores", "A mensagem de exceção deve ser correta");

        exception = assertThrows(SecurityException.class, () -> {
            this.service.desativar(this.usuario, usuarioNormal);
        }, "A mensagem de exceção deve ser lançada quando um usuário não tem permissões para desativar outro");
        assertEquals(exception.getMessage(), "Ação permitida apenas para administradores", "A mensagem de exceção deve ser correta");



        // Explorar administrador banido definitivamente
        this.usuario.setEstado(new Desativado());
        usuarioAdmin.setEstado(new BanidoDefinitivo());
        this.service.ativar(this.usuario, this.usuarioAdmin);

        assertNotEquals(Ativo.class.getSimpleName(), this.usuario.getNomeEstado(), "Um administrador banido permanentemente não deve poder alterar status de um usuário");
    }


}