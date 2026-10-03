public class CalculadoraPrecoVisitor implements ProdutoVisitor {
    private double total = 0;

    @Override
    public void visitar(Livro livro) { total += livro.getPreco(); }

    @Override
    public void visitar(Eletronico eletronico) { total += eletronico.getPreco(); }

    @Override
    public void visitar(Vestuario vestuario) { total += vestuario.getPreco(); }

    public double getTotal() { return total; }
}