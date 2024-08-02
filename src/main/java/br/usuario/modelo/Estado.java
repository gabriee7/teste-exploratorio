package br.usuario.modelo;

interface Estado {
    void ativar(Usuario usuario);
    void desativar(Usuario usuario);
    void advertir(Usuario usuario);
}
