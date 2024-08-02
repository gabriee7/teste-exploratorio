package br.usuario.modelo;

class Novo implements Estado {
    @Override
    public void ativar(Usuario usuario) {
        usuario.setEstado(new Ativo());
    }

    @Override
    public void desativar(Usuario usuario) {
        throw new IllegalStateException("Usuário novo não pode ser desativado");
    }

    @Override
    public void advertir(Usuario usuario) {
        throw new IllegalStateException("Usuário novo não pode ser advertido");
    }

    @Override
    public String toString() {
        return "Novo";
    }

}
