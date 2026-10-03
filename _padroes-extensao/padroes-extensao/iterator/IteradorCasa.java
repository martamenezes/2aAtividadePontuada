import java.util.ArrayList;
import java.util.List;

public class IteradorCasa implements Iterador<Dispositivo> {
    private final List<Dispositivo> dispositivos;
    private int posicao = 0;

    public IteradorCasa(List<Dispositivo> dispositivos) {
        this.dispositivos = new ArrayList<>(dispositivos);
    }

    @Override
    public boolean hasNext() {
        return posicao < dispositivos.size();
    }

    @Override
    public Dispositivo next() {
        return dispositivos.get(posicao++);
    }

    @Override
    public void reset() {
        posicao = 0;
    }
}