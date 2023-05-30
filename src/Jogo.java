/**
 * Essa é a classe principal da aplicacao "World of Zull".
 * "World of Zuul" é um jogo de aventura muito simples, baseado em texto.
 * 
 * Usuários podem caminhar em um cenário. E é tudo! Ele realmente precisa ser 
 * estendido para fazer algo interessante!
 * 
 * Para jogar esse jogo, crie uma instancia dessa classe e chame o método "jogar".
 * 
 * Essa classe principal cria e inicializa todas as outras: ela cria os ambientes, 
 * cria o analisador e começa o jogo. Ela também avalia e  executa os comandos que 
 * o analisador retorna.
 * 
 * @author  Michael Kölling and David J. Barnes (traduzido e adaptado por Julio César Alves)
 */
import java.util.Scanner;
public class Jogo {
    // analisador de comandos do jogo
    private Analisador analisador;
    // ambiente onde se encontra o jogador
    private Ambiente ambienteAtual;
    private Jogador jogador;
        
    /**
     * Cria o jogo e incializa seu mapa interno.
     */
    
    public Jogo()  {
        criarAmbientes();
        analisador = new Analisador();
        System.out.println("Qual o nome do jogador?");
        Scanner entrada = new Scanner (System.in);
        String nome = entrada.nextLine();
        jogador = new Jogador(nome);
    }

    /**
     * Cria todos os ambientes e liga as saidas deles
     */
    private void criarAmbientes() {
        Ambiente cantina, escritorio, boate, posto, centro;
      
        // cria os ambientes
        cantina = new Ambiente("em um local cheio de mesas, na cantina da ufla", "Você vê alguns amigos jogando baralho nas mesas." + " Em uma das mesas você vê uma chave", new Item ("Chave", "Uma chave bem pequena, parece abrir uma gaveta"));
        escritorio = new Ambiente("no escritório da C.H.E.I, é um apartamento, fedido e pequeno", "Um escritório como outro qualquer, você vê varias escrivaninhas e um laptop", new Item("Laptop", "Um laptop entigo"));
        boate = new Ambiente(" em uma festa no expolavras, regada de drogas, bebidas e mulheres",  "A entrada é em uma longa avenida, você pode andar por ela. Quem sabe não encontra algo.", new Item("Corda", "Uma corda próxima a marcas de pneus, ao lado de um barranco"));
        posto = new Ambiente("você foi a um posto de gasolina, estranhamente, tem muitas pessoas ingerindo alcóol lá", "Nesse posto você vê um estranho jovem loiro com um cavanhaque");
        centro = new Ambiente("no centro da cidade, possívelmente alguém pode morar nas redondezas", "Muitas lojas, os atendentes podem saber de algo" + "");
        
        // inicializa as saidas dos ambientes
        cantina.ajustarSaida("leste", escritorio);
        cantina.ajustarSaida("sul", posto);
        cantina.ajustarSaida("oeste", boate);
        escritorio.ajustarSaida("oeste", cantina);
        boate.ajustarSaida("norte", cantina);
        posto.ajustarSaida("norte", cantina);
        posto.ajustarSaida("leste", centro);
        centro.ajustarSaida("oeste", posto);

        ambienteAtual = cantina;  // o jogo comeca em frente à cantina
    }

    /**
     *  Rotina principal do jogo. Fica em loop ate terminar o jogo.
     */
    public void jogar()  {
        imprimirBoasVindas();

        // Entra no loop de comando principal. Aqui nós repetidamente lemos comandos e 
        // os executamos até o jogo terminar.
                
        boolean terminado = false;
        while (! terminado) {
            Comando comando = analisador.pegarComando();
            terminado = processarComando(comando);
        }
        System.out.println("Obrigado por jogar. Até mais!");
    }

    /**
     * Imprime a mensagem de abertura para o jogador.
     */
    private void imprimirBoasVindas() {
        System.out.println();
        System.out.println("Bem-vindo " + jogador.getNome() + " ao Operação C.H.E.I!");
        System.out.println("Operação C.H.E.I é um novo jogo de mistério, incrivelmente cheio de indiretas.");
        System.out.println("Digite 'ajuda' se voce precisar de ajuda.");
        System.out.println();

        mostrarAmbiente();
        
    }

    /**
     * Dado um comando, processa-o (ou seja, executa-o)
     * @param comando O Comando a ser processado.
     * @return true se o comando finaliza o jogo.
     */
    private boolean processarComando(Comando comando)  {
        boolean querSair = false;

        if(comando.ehDesconhecido()) {
            System.out.println("Eu nao entendi o que voce disse...");
            return false;
        }

        String palavraDeComando = comando.getPalavraDeComando();
        if (palavraDeComando.equals("ajuda")) {
            imprimirAjuda();
        }
        else if (palavraDeComando.equals("ir")) {
            irParaAmbiente(comando);
        }
        else if (palavraDeComando.equals("sair")) {
            querSair = sair(comando);
        } 
        else if (palavraDeComando.equals("observar")){
            imprimirObservar();
        }
        else if (palavraDeComando.equals("pegar")){
            coletarItem(comando);
        }
        else if (palavraDeComando.equals("inventario")){
           System.out.println(jogador.listarItens());
        }

        return querSair;
    }

    /**
     * Exibe informações de ajuda.
     * Aqui nós imprimimos algo bobo e enigmático e a lista de  palavras de comando
     */
    private void imprimirAjuda()  {
        System.out.println("Suas palavras de comando sao:");
        analisador.exibirComandosValidos();
    }

    /** 
     * Tenta ir em uma direcao. Se existe uma saída para lá entra no novo ambiente, 
     * caso contrário imprime mensagem de erro.
     */
    private void irParaAmbiente(Comando comando)  {
        // se não há segunda palavra, não sabemos pra onde ir...
        if(!comando.temSegundaPalavra()) {            
            System.out.println("Ir pra onde?");
            return;
        }

        String direcao = comando.getSegundaPalavra();

        // Tenta sair do ambiente atual
        Ambiente proximoAmbiente = ambienteAtual.getSaida(direcao);

        if (proximoAmbiente == null) {
            System.out.println("Nao ha passagem!");
        } else {
            ambienteAtual = proximoAmbiente;
            mostrarAmbiente();
        }
    }

    /** 
     * "Sair" foi digitado. Verifica o resto do comando pra ver se nós queremos 
     * realmente sair do jogo.
     * @return true, se este comando sai do jogo, false, caso contrário.
     */
    private boolean sair(Comando comando)  {
        if(comando.temSegundaPalavra()) {
            System.out.println("Sair o que?");
            return false;
        }
        else {
            return true;  // sinaliza que nós realmente queremos sair
        }
    }

    public void mostrarAmbiente() {
        System.out.println("Voce esta " + ambienteAtual.getDescricao());
        System.out.print("Saidas: " + ambienteAtual.direcoesDeSaida());
        System.out.println();
    }

    public void imprimirObservar(){
        System.out.println(ambienteAtual.getDescricaoLonga());
    }
    
    public void coletarItem(Comando comando){
        
        if(!comando.temSegundaPalavra()) {            
            System.out.println("Pegar oque?");
            return;
        }

        String nomeItem = comando.getSegundaPalavra();
        Item itemAmbiente = ambienteAtual.consultarItem();

        if (itemAmbiente != null && itemAmbiente.getNome().equalsIgnoreCase(nomeItem)) {
            Item itemColetado = ambienteAtual.coletarItem();
            jogador.adicionarItem(nomeItem);
            System.out.println("Você coletou o item: " + itemColetado.getNome());
        } else {
            System.out.println("Não há esse item para ser coletado no ambiente.");
        }
    }
}
