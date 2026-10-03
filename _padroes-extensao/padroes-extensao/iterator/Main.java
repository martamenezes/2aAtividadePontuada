public class Main {
    public static void main(String[] args) {
        CasaInteligente casa = new CasaInteligente();
        casa.adicionar(new Dispositivo("Lâmpada da Sala", "Sala", "iluminacao"));
        casa.adicionar(new Dispositivo("Sensor de Presença", "Sala", "seguranca"));
        casa.adicionar(new Dispositivo("Ar-condicionado", "Sala", "clima"));
        casa.adicionar(new Dispositivo("Lâmpada da Cozinha", "Cozinha", "iluminacao"));
        casa.adicionar(new Dispositivo("Cafeteira Inteligente", "Cozinha", "eletrodomestico"));
        casa.adicionar(new Dispositivo("Lâmpada do Quarto", "Quarto", "iluminacao"));
        casa.adicionar(new Dispositivo("Termostato", "Quarto", "clima"));

        System.out.println("=== Todos os dispositivos ===");
        percorrer(casa.criarIterador());

        System.out.println("\n=== Apenas dispositivos da Cozinha ===");
        percorrer(casa.criarIteradorPorComodo("Cozinha"));

        System.out.println("\n=== Acendendo toda a iluminação da casa ===");
        Iterador<Dispositivo> luzes = casa.criarIteradorPorTipo("iluminacao");
        while (luzes.hasNext()) {
            Dispositivo luz = luzes.next();
            luz.ligar();
            System.out.println("Acesa: " + luz.getNome());
        }
    }

    private static void percorrer(Iterador<Dispositivo> it) {
        while (it.hasNext()) {
            System.out.println("- " + it.next());
        }
    }
}