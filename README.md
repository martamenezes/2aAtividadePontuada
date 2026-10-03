# PADRÕES DE EXTENSÃO - 2ª Atividade Pontuada

## 1. Decorator — Sistema de Pagamentos Online

### 1.1 Conceito

O **Decorator** anexa responsabilidades adicionais a um objeto **dinamicamente**, em tempo de execução. Ele oferece uma alternativa flexível à subclassificação para estender funcionalidades: em vez de criar subclasses para cada combinação (`PagamentoComJurosComTaxaComSeguro...`), o objeto é "envolto" por objetos decoradores que adicionam seu próprio comportamento **antes/depois** de delegar a chamada ao objeto interno.

Cada decorador implementa a **mesma interface** do objeto original, de modo que o cliente não sabe (nem precisa saber) se está falando com o componente puro ou com uma pilha de decoradores — o decorador é "transparente" para o cliente.

### 1.2 Problema do mundo real

Uma loja virtual calcula o valor final de um pagamento combinando, **dinamicamente**, adicionais que variam por compra:

- **Juros de parcelamento** (dependem do número de parcelas e da promoção do dia);
- **Taxa fixa do gateway de pagamento**;
- **Seguro contra fraude** (opcional, escolhido pelo cliente).

Subclassificar `Pagamento` para cada combinação geraria uma explosão de classes. Com o Decorator, a composição final é montada em **tempo de execução**, como montar um "embrulho": cada camada adiciona seu valor e descrição ao resultado.

### 1.3 Participantes do padrão no projeto

| Papel (GoF) | Classe no projeto |
|---|---|
| Componente (interface comum) | `ProcessadorPagamento` |
| Componente concreto | `PagamentoCartao` |
| Decorador abstrato | `DecoradorPagamento` |
| Decoradores concretos | `JurosParcelamento`, `TaxaGateway`, `SeguroFraude` |

### 1.4 Implementação

```java
public interface ProcessadorPagamento {
    String getDescricao();
    double getValor();
    void processar();
}
```

```java
public class PagamentoCartao implements ProcessadorPagamento {
    private final double valor;
    private final int parcelas;

    public PagamentoCartao(double valor, int parcelas) {
        this.valor = valor;
        this.parcelas = parcelas;
    }

    @Override
    public String getDescricao() {
        return String.format("Pagamento no cartão (R$ %.2f em %dx)", valor, parcelas);
    }

    @Override
    public double getValor() {
        return valor;
    }

    @Override
    public void processar() {
        System.out.println("Enviando transação ao operador do cartão...");
    }
}
```

```java
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
```

```java
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
```

```java
public class TaxaGateway extends DecoradorPagamento {
    private final double taxaFixa;

    public TaxaGateway(ProcessadorPagamento envolvido, double taxaFixa) {
        super(envolvido);
        this.taxaFixa = taxaFixa;
    }

    @Override
    public String getDescricao() {
        return envolvido.getDescricao() + String.format(" + taxa do gateway (R$ %.2f)", taxaFixa);
    }

    @Override
    public double getValor() {
        return envolvido.getValor() + taxaFixa;
    }
}
```

```java
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
```

```java
public class Main {
    public static void main(String[] args) {
        ProcessadorPagamento simples = new PagamentoCartao(1000.00, 6);
        System.out.println("=== Pagamento simples ===");
        imprimir(simples);

        
        ProcessadorPagamento completo = new SeguroFraude(
                new TaxaGateway(
                        new JurosParcelamento(new PagamentoCartao(1000.00, 6), 0.05),
                        3.99));
        System.out.println("\n=== Pagamento com adicionais ===");
        imprimir(completo);

       
        ProcessadorPagamento avista = new TaxaGateway(new PagamentoCartao(250.00, 1), 3.99);
        System.out.println("\n=== Pagamento à vista com taxa ===");
        imprimir(avista);
    }

    private static void imprimir(ProcessadorPagamento p) {
        System.out.println(p.getDescricao());
        System.out.printf("Total a pagar: R$ %.2f%n", p.getValor());
        p.processar();
    }
}
```

### 1.5 Como executar

```bash
cd decorator
javac *.java
java Main
```

### 1.6 Saída esperada

```
=== Pagamento simples ===
Pagamento no cartão (R$ 1000.00 em 6x)
Total a pagar: R$ 1000.00
Enviando transação ao operador do cartão...

=== Pagamento com adicionais ===
Pagamento no cartão (R$ 1000.00 em 6x) + juros de parcelamento (5.0%) + taxa do gateway (R$ 3.99) + seguro contra fraude (1.0%)
Total a pagar: R$ 1064.53
Enviando transação ao operador do cartão...
-> Antifraude ativado para esta transação

=== Pagamento à vista com taxa ===
Pagamento no cartão (R$ 250.00 em 1x) + taxa do gateway (R$ 3.99)
Total a pagar: R$ 253.99
Enviando transação ao operador do cartão...
```

### 1.7 Por que é um padrão de extensão?

Para lançar um novo adicional (ex.: **cashback**, **desconto de fidelidade**) basta criar **um novo decorador** que estende `DecoradorPagamento` — nenhuma classe existente é alterada e o cliente continua usando a mesma interface `ProcessadorPagamento`. A funcionalidade é estendida **por composição, em tempo de execução**, e não por herança estática.

---

## 2. Iterator — Automação Residencial

### 2.1 Conceito

O **Iterator** fornece uma maneira de acessar, **sequencialmente**, os elementos de um objeto agregado (coleção) **sem expor sua representação interna** (se é uma lista, um array, um mapa...). O cliente pede um iterador à coleção e percorre os elementos com `hasNext()`/`next()`, sem saber como eles estão armazenados.

Isso permite, ainda, criar **múltiplas formas de percorrer a mesma coleção** (ordem de cadastro, filtrados por um critério, em ordem reversa etc.) sem alterar a coleção — cada "modo de navegação" é um novo iterador.

### 2.2 Problema do mundo real

Em uma casa inteligente, o central de automação precisa percorrer os dispositivos cadastrados de várias formas:

- **Todos** os dispositivos (para o painel geral);
- Apenas os de **um cômodo** (modo "sair de casa": desligar tudo da sala);
- Apenas os de **um tipo** (acender toda a iluminação de uma vez).

A casa guarda os dispositivos em uma lista interna — mas o restante do sistema nunca acessa essa lista diretamente: sempre usa iteradores, o que permite trocar a estrutura interna da coleção (ou adicionar novos critérios de navegação) sem quebrar nada.

### 2.3 Participantes do padrão no projeto

| Papel (GoF) | Classe no projeto |
|---|---|
| Iterator (interface) | `Iterador<T>` |
| ConcreteIterator | `IteradorCasa`, `IteradorFiltrado` |
| Aggregate (interface da coleção) | `ColecaoDispositivos` |
| ConcreteAggregate | `CasaInteligente` |
| Elemento percorrido | `Dispositivo` |

### 2.4 Implementação

```java
public interface Iterador<T> {
    boolean hasNext();
    T next();
    void reset();
}
```

```java

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
```

```java
public interface ColecaoDispositivos {
    Iterador<Dispositivo> criarIterador();
    Iterador<Dispositivo> criarIteradorPorComodo(String comodo);
    Iterador<Dispositivo> criarIteradorPorTipo(String tipo);
}
```

```java
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
```

```java
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
```

```java
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
```

```java

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
```

### 2.5 Como executar

```bash
cd iterator
javac *.java
java Main
```

### 2.6 Saída esperada

```
=== Todos os dispositivos ===
- Lâmpada da Sala (Sala / iluminacao) - desligado
- Sensor de Presença (Sala / seguranca) - desligado
- Ar-condicionado (Sala / clima) - desligado
- Lâmpada da Cozinha (Cozinha / iluminacao) - desligado
- Cafeteira Inteligente (Cozinha / eletrodomestico) - desligado
- Lâmpada do Quarto (Quarto / iluminacao) - desligado
- Termostato (Quarto / clima) - desligado

=== Apenas dispositivos da Cozinha ===
- Lâmpada da Cozinha (Cozinha / iluminacao) - desligado
- Cafeteira Inteligente (Cozinha / eletrodomestico) - desligado

=== Acendendo toda a iluminação da casa ===
Acesa: Lâmpada da Sala
Acesa: Lâmpada da Cozinha
Acesa: Lâmpada do Quarto
```

### 2.7 Por que é um padrão de extensão?

Para oferecer uma **nova forma de navegação** (ex.: "percorrer apenas dispositivos ligados", "percorrer por ordem de consumo de energia") basta criar **um novo iterador** — a coleção (`CasaInteligente`) pode até trocar sua estrutura interna de `List` para array sem quebrar o cliente, pois este depende apenas da interface `Iterador`. A coleção e as formas de percorrê-la são estendidas de forma independente.

---

## 3. Visitor — Carrinho de Compras

### 3.1 Conceito

O **Visitor** representa uma operação a ser executada sobre os elementos de uma estrutura de objetos **sem alterar as classes desses elementos**. A "mágica" acontece pelo **duplo despacho**: cada elemento possui um método `aceitar(visitor)` que chama de volta o método do visitor correspondente ao **seu tipo concreto** (`visitor.visitar(this)`). Assim, o visitor sabe exatamente com qual tipo de elemento está lidando, sem usar `instanceof`.

Cada nova operação sobre a estrutura (novo cálculo, novo relatório) é um **novo visitor** — os elementos da hierarquia permanecem intocados.

### 3.2 Problema do mundo real

Um e-commerce tem um carrinho com produtos de categorias diferentes — **Livro**, **Eletrônico** e **Vestuário** — e precisa executar operações que tratam cada categoria de um jeito diferente:

- **Subtotal** (soma dos preços);
- **Frete** (livros por peso, eletrônicos com embalagem reforçada, vestuário com frete fixo);
- **Impostos** (livros isentos, eletrônicos 15%, vestuário 8%).

Colocar esses três cálculos dentro das classes de produto faria o código crescer a cada nova regra fiscal. Com o Visitor, cada operação vive em sua própria classe, e o carrinho apenas a "aplica" sobre seus itens.

### 3.3 Participantes do padrão no projeto

| Papel (GoF) | Classe no projeto |
|---|---|
| Visitor (interface) | `ProdutoVisitor` |
| ConcreteVisitor | `CalculadoraPrecoVisitor`, `CalculadoraFreteVisitor`, `RelatorioImpostosVisitor` |
| Element / ConcreteElement | `Produto`, `Livro`, `Eletronico`, `Vestuario` |
| ObjectStructure | `CarrinhoCompras` |

### 3.4 Implementação

```java

public interface ProdutoVisitor {
    void visitar(Livro livro);
    void visitar(Eletronico eletronico);
    void visitar(Vestuario vestuario);
}
```

```java

public abstract class Produto {
    private final String nome;
    private final double preco;

    protected Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public abstract double getPesoKg();

    
    public abstract void aceitar(ProdutoVisitor visitor);
}
```

```java

public class Livro extends Produto {
    private final double pesoKg;

    public Livro(String nome, double preco, double pesoKg) {
        super(nome, preco);
        this.pesoKg = pesoKg;
    }

    @Override
    public double getPesoKg() { return pesoKg; }

    @Override
    public void aceitar(ProdutoVisitor visitor) {
        visitor.visitar(this);
    }
}
```

```java

public class Eletronico extends Produto {
    private final double pesoKg;

    public Eletronico(String nome, double preco, double pesoKg) {
        super(nome, preco);
        this.pesoKg = pesoKg;
    }

    @Override
    public double getPesoKg() { return pesoKg; }

    @Override
    public void aceitar(ProdutoVisitor visitor) {
        visitor.visitar(this);
    }
}
```

```java

public class Vestuario extends Produto {
    private final double pesoKg;

    public Vestuario(String nome, double preco, double pesoKg) {
        super(nome, preco);
        this.pesoKg = pesoKg;
    }

    @Override
    public double getPesoKg() { return pesoKg; }

    @Override
    public void aceitar(ProdutoVisitor visitor) {
        visitor.visitar(this);
    }
}
```

```java

public class CalculadoraPrecoVisitor implements ProdutoVisitor {
    private double total = 0;

    @Override
    public void visitar(Livro livro) { total += livro.getPreco(); }

    @Override
    public void visitar(Eletronico eletronico) { total += eletronico.getPreco(); }

    @Override
    public void visitar(Vestuario vestuario) { total += vestuario.getPreco(); }

    public double getTotal() { return total; }
}
```

```java

public class CalculadoraFreteVisitor implements ProdutoVisitor {
    private double totalFrete = 0;

    @Override
    public void visitar(Livro livro) {
        totalFrete += livro.getPesoKg() * 15.0; 
    }

    @Override
    public void visitar(Eletronico eletronico) {
        totalFrete += eletronico.getPesoKg() * 20.0; 
    }

    @Override
    public void visitar(Vestuario vestuario) {
        totalFrete += 10.0; 
    }

    public double getTotalFrete() { return totalFrete; }
}
```

```java

public class RelatorioImpostosVisitor implements ProdutoVisitor {
    private final StringBuilder relatorio = new StringBuilder();
    private double totalImpostos = 0;

    @Override
    public void visitar(Livro livro) {
        
        relatorio.append(String.format("%-18s R$ %8.2f  isento (livro)%n",
                livro.getNome(), livro.getPreco()));
    }

    @Override
    public void visitar(Eletronico eletronico) {
        double imposto = eletronico.getPreco() * 0.15;
        totalImpostos += imposto;
        relatorio.append(String.format("%-18s R$ %8.2f  imposto: R$ %6.2f (15%%)%n",
                eletronico.getNome(), eletronico.getPreco(), imposto));
    }

    @Override
    public void visitar(Vestuario vestuario) {
        double imposto = vestuario.getPreco() * 0.08;
        totalImpostos += imposto;
        relatorio.append(String.format("%-18s R$ %8.2f  imposto: R$ %6.2f (8%%)%n",
                vestuario.getNome(), vestuario.getPreco(), imposto));
    }

    public String getRelatorio() { return relatorio.toString(); }
    public double getTotalImpostos() { return totalImpostos; }
}
```

```java
import java.util.ArrayList;
import java.util.List;


public class CarrinhoCompras {
    private final List<Produto> itens = new ArrayList<>();

    public void adicionar(Produto produto) {
        itens.add(produto);
    }

    public void aceitar(ProdutoVisitor visitor) {
        for (Produto item : itens) {
            item.aceitar(visitor);
        }
    }
}
```

```java

public class Main {
    public static void main(String[] args) {
        CarrinhoCompras carrinho = new CarrinhoCompras();
        carrinho.adicionar(new Livro("Clean Code", 189.90, 0.8));
        carrinho.adicionar(new Eletronico("Fone Bluetooth", 349.99, 0.3));
        carrinho.adicionar(new Eletronico("Notebook", 4299.00, 2.2));
        carrinho.adicionar(new Vestuario("Camiseta Polo", 89.90, 0.2));

        CalculadoraPrecoVisitor preco = new CalculadoraPrecoVisitor();
        CalculadoraFreteVisitor frete = new CalculadoraFreteVisitor();
        RelatorioImpostosVisitor impostos = new RelatorioImpostosVisitor();

        carrinho.aceitar(preco);
        carrinho.aceitar(frete);
        carrinho.aceitar(impostos);

        System.out.println("--- Impostos por item ---");
        System.out.print(impostos.getRelatorio());
        System.out.printf("Subtotal: R$ %.2f%n", preco.getTotal());
        System.out.printf("Frete: R$ %.2f%n", frete.getTotalFrete());
        System.out.printf("Total em impostos: R$ %.2f%n", impostos.getTotalImpostos());
        System.out.printf("Total do pedido (subtotal + impostos + frete): R$ %.2f%n",
                preco.getTotal() + impostos.getTotalImpostos() + frete.getTotalFrete());
    }
}
```

### 3.5 Como executar

```bash
cd visitor
javac *.java
java Main
```

### 3.6 Saída esperada

```
--- Impostos por item ---
Clean Code        R$   189.90  isento (livro)
Fone Bluetooth    R$   349.99  imposto: R$  52.50 (15%)
Notebook          R$  4299.00  imposto: R$ 644.85 (15%)
Camiseta Polo     R$    89.90  imposto: R$   7.19 (8%)
Subtotal: R$ 4928.79
Frete: R$ 72.00
Total em impostos: R$ 704.54
Total do pedido (subtotal + impostos + frete): R$ 5705.33
```

### 3.7 Por que é um padrão de extensão?

Para adicionar uma **nova operação** ao carrinho (ex.: *cálculo de cashback*, *relatório de embalagens sustentáveis*) basta criar **um novo visitor** — `Livro`, `Eletronico`, `Vestuario` e `CarrinhoCompras` permanecem **intactos**. As operações sobre a estrutura são estendidas sem modificar as classes dos elementos, exatamente o que o OCP prega.


