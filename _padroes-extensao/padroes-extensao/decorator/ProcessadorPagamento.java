public interface ProcessadorPagamento {
    String getDescricao();
    double getValor();
    void processar();
}