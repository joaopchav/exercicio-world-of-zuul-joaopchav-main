/**
 * Classe Ambiente - um ambiente em um jogo adventure.
 *
 * Esta classe é parte da aplicação "World of Zuul".
 * "World of Zuul" é um jogo de aventura muito simples, baseado em texto.  
 *
 * Um "Ambiente" representa uma localização no cenário do jogo. Ele é conectado aos 
 * outros ambientes através de saídas. As saídas são nomeadas como norte, sul, leste 
 * e oeste. Para cada direção, o ambiente guarda uma referência para o ambiente vizinho, 
 * ou null se não há saída naquela direção.
 * 
 * @author  Michael Kölling and David J. Barnes (traduzido e adaptado por Julio César Alves)
 */
import java.util.HashMap;
public class Ambiente {
    private String descricao;
    private Item item;
    private String descricaoLonga;
    private HashMap<String, Ambiente> saidas;
   
    public Ambiente(String descricao, String descricaoLonga) {
        this.descricao = descricao;
        this.descricaoLonga = descricaoLonga;
		saidas = new HashMap<String,Ambiente>();
    }

    public Ambiente(String descricao, String descricaoLonga, Item item){
        this.descricao = descricao;
        this.descricaoLonga = descricaoLonga;
        this.item = item;
        saidas = new HashMap<String,Ambiente>();
    }

    public Item consultarItem() {
        return item;
    }
    
    public Item coletarItem() {
        Item itemColetado = item;
        item = null;
        return itemColetado;
    }

	public void ajustarSaida(String direcao, Ambiente ambiente) {
        saidas.put(direcao, ambiente);
    }
	
	public String getDescricao() {
        return descricao;
    }
 
    public Ambiente getSaida(String direcao) {
	    return saidas.get(direcao);
    }

	public String direcoesDeSaida() {    	
    	String textoSaidas = "";
    	for (String direcao : saidas.keySet()) {
    		textoSaidas = textoSaidas + direcao + " ";  
    	}        
	return textoSaidas;
    }

    public String getDescricaoLonga() {
        String descricaoCompleta = descricaoLonga;
        if (temItem()) {
            descricaoCompleta += "\nHá um(a) " + item.getNome() + " aqui.";
        }
        return descricaoCompleta;  
    }

    public boolean temItem() {
        return item != null;
    }
}








