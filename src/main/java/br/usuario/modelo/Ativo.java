package br.usuario.modelo;

import java.time.Duration;

class Ativo implements Estado {
    @Override
    public void ativar(Usuario usuario) {
        throw new IllegalStateException("O usuário já está ativo");
    }

    @Override
    public void desativar(Usuario usuario) {
        usuario.setEstado(new Desativado());
    }

    @Override
    public void advertir(Usuario usuario) {
        usuario.setNumeroDeAdvertencias(usuario.getNumeroDeAdvertencias() + 1);
        if (usuario.getNumeroDeAdvertencias() == 2) {
            usuario.setEstado(new BanidoTemporario(usuario));
        } else if (usuario.getNumeroDeAdvertencias() > 2) {
            usuario.setEstado(new BanidoDefinitivo());
        }
    }
}
