public abstract class DecoradorPagamento implements ProcessadorPagamento {
    protected final ProcessadorPagamento envolvido;

    protected DecoradorPagamento(ProcessadorPagamento envolvido) {
        this.envolvido = envolvido;
    }

    @Override
    public String getDescricao() {
        return envolvido.getDescricao();
    }

    @Override
    public double getValor() {
        return envolvido.getValor();
    }

    @Override
    public void processar() {
        envolvido.processar();
    }
}