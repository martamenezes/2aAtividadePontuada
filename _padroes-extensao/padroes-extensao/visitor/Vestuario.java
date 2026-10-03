public class Vestuario extends Produto {
    private final double pesoKg;

    public Vestuario(String nome, double preco, double pesoKg) {
        super(nome, preco);
        this.pesoKg = pesoKg;
    }

    @Override
    public double getPesoKg() { return pesoKg; }

    @Override
    public void aceitar(ProdutoVisitor visitor) {
        visitor.visitar(this);
    }
}