public interface ColecaoDispositivos {
    Iterador<Dispositivo> criarIterador();
    Iterador<Dispositivo> criarIteradorPorComodo(String comodo);
    Iterador<Dispositivo> criarIteradorPorTipo(String tipo);
}