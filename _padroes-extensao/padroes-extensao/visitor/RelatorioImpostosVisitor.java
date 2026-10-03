public class RelatorioImpostosVisitor implements ProdutoVisitor {
    private final StringBuilder relatorio = new StringBuilder();
    private double totalImpostos = 0;

    @Override
    public void visitar(Livro livro) {
        relatorio.append(String.format("%-18s R$ %8.2f  isento (livro)%n",
                livro.getNome(), livro.getPreco()));
    }

    @Override
    public void visitar(Eletronico eletronico) {
        double imposto = eletronico.getPreco() * 0.15;
        totalImpostos += imposto;
        relatorio.append(String.format("%-18s R$ %8.2f  imposto: R$ %6.2f (15%%)%n",
                eletronico.getNome(), eletronico.getPreco(), imposto));
    }

    @Override
    public void visitar(Vestuario vestuario) {
        double imposto = vestuario.getPreco() * 0.08;
        totalImpostos += imposto;
        relatorio.append(String.format("%-18s R$ %8.2f  imposto: R$ %6.2f (8%%)%n",
                vestuario.getNome(), vestuario.getPreco(), imposto));
    }

    public String getRelatorio() { return relatorio.toString(); }
    public double getTotalImpostos() { return totalImpostos; }
}