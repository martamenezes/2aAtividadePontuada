public interface ProdutoVisitor {
    void visitar(Livro livro);
    void visitar(Eletronico eletronico);
    void visitar(Vestuario vestuario);
}