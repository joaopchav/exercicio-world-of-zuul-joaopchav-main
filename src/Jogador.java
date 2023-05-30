import java.util.ArrayList;

public class Jogador {
    
    private String nome;
    private ArrayList<String> mochila;

    public Jogador(String nome) {
        this.nome = nome;
        mochila = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void adicionarItem(String item) {
        mochila.add(item);
    }

    public String removerItem(String item) {
        if (mochila.contains(item)) {
            mochila.remove(item);
            return item;
        } else {
            return null;
        }
    }

    public String listarItens() {
        if (mochila.isEmpty()) {
            return "A mochila está vazia.";
        } else {
            StringBuilder itens = new StringBuilder("Itens na mochila: ");
            for (String item : mochila) {
                itens.append(item).append(", ");
            }
            return itens.toString();
        }
    }
}