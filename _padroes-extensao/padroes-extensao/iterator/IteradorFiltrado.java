import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class IteradorFiltrado implements Iterador<Dispositivo> {
    private final List<Dispositivo> dispositivos;
    private final Predicate<Dispositivo> criterio;
    private int posicao = 0;

    public IteradorFiltrado(List<Dispositivo> dispositivos, Predicate<Dispositivo> criterio) {
        this.dispositivos = new ArrayList<>(dispositivos);
        this.criterio = criterio;
    }

    private int proximoValido(int inicio) {
        for (int i = inicio; i < dispositivos.size(); i++) {
            if (criterio.test(dispositivos.get(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean hasNext() {
        return proximoValido(posicao) != -1;
    }

    @Override
    public Dispositivo next() {
        int i = proximoValido(posicao);
        if (i == -1) {
            throw new java.util.NoSuchElementException("Não há mais dispositivos para este critério.");
        }
        posicao = i + 1;
        return dispositivos.get(i);
    }

    @Override
    public void reset() {
        posicao = 0;
    }
}