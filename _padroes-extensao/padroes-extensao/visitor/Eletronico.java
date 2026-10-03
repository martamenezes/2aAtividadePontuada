public class Eletronico extends Produto {
    private final double pesoKg;

    public Eletronico(String nome, double preco, double pesoKg) {
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