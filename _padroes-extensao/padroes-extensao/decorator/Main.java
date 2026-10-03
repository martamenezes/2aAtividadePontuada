public class Main {
    public static void main(String[] args) {
        ProcessadorPagamento simples = new PagamentoCartao(1000.00, 6);
        System.out.println("=== Pagamento simples ===");
        imprimir(simples);

        ProcessadorPagamento completo = new SeguroFraude(
                new TaxaGateway(
                        new JurosParcelamento(new PagamentoCartao(1000.00, 6), 0.05),
                        3.99));
        System.out.println("\n=== Pagamento com adicionais ===");
        imprimir(completo);

        ProcessadorPagamento avista = new TaxaGateway(new PagamentoCartao(250.00, 1), 3.99);
        System.out.println("\n=== Pagamento à vista com taxa ===");
        imprimir(avista);
    }

    private static void imprimir(ProcessadorPagamento pagamento) {
        System.out.println(pagamento.getDescricao());
        System.out.printf("Total a pagar: R$ %.2f%n", pagamento.getValor());
        pagamento.processar();
    }
}