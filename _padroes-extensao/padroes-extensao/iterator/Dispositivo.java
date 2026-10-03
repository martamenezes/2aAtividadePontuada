public class Dispositivo {
    private final String nome;
    private final String comodo;
    private final String tipo; 
    private boolean ligado = false;

    public Dispositivo(String nome, String comodo, String tipo) {
        this.nome = nome;
        this.comodo = comodo;
        this.tipo = tipo;
    }

    public String getNome() { return nome; }
    public String getComodo() { return comodo; }
    public String getTipo() { return tipo; }
    public boolean isLigado() { return ligado; }

    public void ligar() { ligado = true; }
    public void desligar() { ligado = false; }

    @Override
    public String toString() {
        return String.format("%s (%s / %s) - %s", nome, comodo, tipo, ligado ? "ligado" : "desligado");
    }
}