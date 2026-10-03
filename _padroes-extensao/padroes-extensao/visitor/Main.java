public class Main {
    public static void main(String[] args) {
        CarrinhoCompras carrinho = new CarrinhoCompras();
        carrinho.adicionar(new Livro("Clean Code", 189.90, 0.8));
        carrinho.adicionar(new Eletronico("Fone Bluetooth", 349.99, 0.3));
        carrinho.adicionar(new Eletronico("Notebook", 4299.00, 2.2));
        carrinho.adicionar(new Vestuario("Camiseta Polo", 89.90, 0.2));

        CalculadoraPrecoVisitor preco = new CalculadoraPrecoVisitor();
        CalculadoraFreteVisitor frete = new CalculadoraFreteVisitor();
        RelatorioImpostosVisitor impostos = new RelatorioImpostosVisitor();

        carrinho.aceitar(preco);
        carrinho.aceitar(frete);
        carrinho.aceitar(impostos);

        System.out.println("--- Impostos por item ---");
        System.out.print(impostos.getRelatorio());
        System.out.printf("Subtotal: R$ %.2f%n", preco.getTotal());
        System.out.printf("Frete: R$ %.2f%n", frete.getTotalFrete());
        System.out.printf("Total em impostos: R$ %.2f%n", impostos.getTotalImpostos());
        System.out.printf("Total do pedido (subtotal + impostos + frete): R$ %.2f%n",
                preco.getTotal() + impostos.getTotalImpostos() + frete.getTotalFrete());
    }
}