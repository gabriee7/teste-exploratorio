package br.usuario.modelo;

class BanidoDefinitivo implements Estado {
    @Override
    public void ativar(Usuario usuario) {
        throw new IllegalStateException("Usuário banido definitivamente não pode ser ativado");
    }

    @Override
    public void desativar(Usuario usuario) {
        throw new IllegalStateException("Usuário banido definitivamente não pode ser desativado");
    }

    @Override
    public void advertir(Usuario usuario) {
        throw new IllegalStateException("Usuário banido definitivamente não pode ser advertido");
    }

    @Override
    public String toString() {
        return "Banido definitivamente";
    }

}
