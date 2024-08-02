package br.usuario.modelo;

import java.time.Duration;

public class Usuario {
    private String nome;
    private TipoUsuario tipo;
    private String senha;
    private Estado estado;
    private int numeroDeAdvertencias;
    private Duration tempoBanimento;

    public Usuario(String nome, TipoUsuario tipo, String senha) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário não pode ser vazio ou nulo");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("A senha do usuário não pode ser vazia ou nula");
        }

        this.nome = nome;
        this.tipo = tipo;
        this.senha = senha;
        this.estado = new Novo();
    }

    public String getNome() {
        return nome;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getNumeroDeAdvertencias() {
        return numeroDeAdvertencias;
    }

    void setNumeroDeAdvertencias(int numeroDeAdvertencias) {
        this.numeroDeAdvertencias = numeroDeAdvertencias;
    }

    public Duration getTempoBanimento() {
        return tempoBanimento;
    }

    void setTempoBanimento(Duration tempoBanimento) {
        this.tempoBanimento = tempoBanimento;
    }

    void ativar() {
        estado.ativar(this);
    }

    void desativar() {
        estado.desativar(this);
    }

    void advertir() {
        estado.advertir(this);
    }

    public String getNomeEstado() {
        return estado.getClass().getSimpleName();
    }
}
