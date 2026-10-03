import java.util.ArrayList;
import java.util.List;

public class CasaInteligente implements ColecaoDispositivos {
    private final List<Dispositivo> dispositivos = new ArrayList<>();

    public void adicionar(Dispositivo dispositivo) {
        dispositivos.add(dispositivo);
    }

    @Override
    public Iterador<Dispositivo> criarIterador() {
        return new IteradorCasa(dispositivos);
    }

    @Override
    public Iterador<Dispositivo> criarIteradorPorComodo(String comodo) {
        return new IteradorFiltrado(dispositivos, d -> d.getComodo().equalsIgnoreCase(comodo));
    }

    @Override
    public Iterador<Dispositivo> criarIteradorPorTipo(String tipo) {
        return new IteradorFiltrado(dispositivos, d -> d.getTipo().equalsIgnoreCase(tipo));
    }
}