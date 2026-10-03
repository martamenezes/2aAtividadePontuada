public class TaxaGateway extends DecoradorPagamento {
    private final double taxaFixa;

    public TaxaGateway(ProcessadorPagamento envolvido, double taxaFixa) {
        super(envolvido);
        this.taxaFixa = taxaFixa;
    }

    @Override
    public String getDescricao() {
        return envolvido.getDescricao() + String.format(" + taxa do gateway (R$ %.2f)", taxaFixa);
    }

    @Override
    public double getValor() {
        return envolvido.getValor() + taxaFixa;
    }
}