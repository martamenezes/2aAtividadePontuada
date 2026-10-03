public class SeguroFraude extends DecoradorPagamento {
    private static final double PERCENTUAL = 0.01;

    public SeguroFraude(ProcessadorPagamento envolvido) {
        super(envolvido);
    }

    @Override
    public String getDescricao() {
        return envolvido.getDescricao() + " + seguro contra fraude (1.0%)";
    }

    @Override
    public double getValor() {
        return envolvido.getValor() * (1 + PERCENTUAL);
    }

    @Override
    public void processar() {
        super.processar(); 
        System.out.println("-> Antifraude ativado para esta transação");
    }
}