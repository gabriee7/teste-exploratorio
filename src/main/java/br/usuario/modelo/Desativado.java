package br.usuario.modelo;

class Desativado implements Estado {
    @Override
    public void ativar(Usuario usuario) {
        usuario.setEstado(new Ativo());
    }

    @Override
    public void desativar(Usuario usuario) {
        throw new IllegalStateException("O usuário já está desativado");
    }

    @Override
    public void advertir(Usuario usuario) {
        throw new IllegalStateException("Usuário desativado não pode ser advertido");
    }

    @Override
    public String toString() {
        return "Desativado";
    }

}
