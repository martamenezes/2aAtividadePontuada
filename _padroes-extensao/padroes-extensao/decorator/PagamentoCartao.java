public class PagamentoCartao implements ProcessadorPagamento {
    private final double valor;
    private final int parcelas;

    public PagamentoCartao(double valor, int parcelas) {
        this.valor = valor;
        this.parcelas = parcelas;
    }

    @Override
    public String getDescricao() {
        return String.format("Pagamento no cartão (R$ %.2f em %dx)", valor, parcelas);
    }

    @Override
    public double getValor() {
        return valor;
    }

    @Override
    public void processar() {
        System.out.println("Enviando transação ao operador do cartão...");
    }
}