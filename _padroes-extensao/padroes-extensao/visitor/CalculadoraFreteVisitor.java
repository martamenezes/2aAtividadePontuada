public class CalculadoraFreteVisitor implements ProdutoVisitor {
    private double totalFrete = 0;

    @Override
    public void visitar(Livro livro) {
        totalFrete += livro.getPesoKg() * 15.0; 
    }

    @Override
    public void visitar(Eletronico eletronico) {
        totalFrete += eletronico.getPesoKg() * 20.0; 
    }

    @Override
    public void visitar(Vestuario vestuario) {
        totalFrete += 10.0; 
    }

    public double getTotalFrete() { return totalFrete; }
}