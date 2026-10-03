public class JurosParcelamento extends DecoradorPagamento {
    private final double taxa;

    public JurosParcelamento(ProcessadorPagamento envolvido, double taxa) {
        super(envolvido);
        this.taxa = taxa;
    }

    @Override
    public String getDescricao() {
        return envolvido.getDescricao() + String.format(" + juros de parcelamento (%.1f%%)", taxa * 100);
    }

    @Override
    public double getValor() {
        return envolvido.getValor() * (1 + taxa);
    }
}