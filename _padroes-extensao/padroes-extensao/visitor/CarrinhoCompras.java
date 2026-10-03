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