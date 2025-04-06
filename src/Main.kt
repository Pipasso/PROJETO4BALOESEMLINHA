const val BALAO = "\u03D9"
const val BALAOVERMELHO = "\u001b[31m$BALAO\u001b[0m"
const val BALAOAZUL = "\u001b[34m$BALAO\u001b[0m"
const val MENSAGEMTABULEIRO = "Tabuleiro"
const val SEPARADOR = "X"
const val MENSAGEMCOLUNAINVALIDA = "Coluna invalida"
const val MENSAGEMEXPLODIRINDISPONIVEL = "Funcionalidade Explodir nao esta disponivel"
const val OPCAONOVOJOGO = "1. Novo Jogo"
const val OPCAOGRAVARJOGO = "2. Gravar Jogo"
const val OPCAOLERJOGO = "3. Ler Jogo"
const val OPCAOSAIR = "0. Sair"
const val MENSAGEMCOLUNAESCOLHIDA = "Coluna escolhida: "

fun main() {
    println()
    println("Bem-vindo ao jogo \"4 Baloes em Linha\"!")
    do {
        val opcao = criaMenu()
    } while (opcao != "sair")
    //o jogo continua ate o usuario escrever sair
}

fun gravaJogo(nomeFicheiro: String, tabuleiro: Array<Array<String?>>, nomeJogador: String) {
    val file = java.io.File(nomeFicheiro) // cria um objeto file com o nome do jogador


    file.writeText(nomeJogador + "\n") // escreve o nome do jogador na primeira linha do arquivo e ha uma quebra de linha


    for (linha in tabuleiro.indices) {
        var primeiraColuna = true // Começa como true para cada nova linha
        for (coluna in 0 until tabuleiro[0].size) {
            if (!primeiraColuna) {  // Se NÃO for a primeira coluna
                file.appendText(",") // Adiciona uma vírgula, exceto para a primeira coluna de cada linha

            }
            when (tabuleiro[linha][coluna]) {
                BALAOVERMELHO -> file.appendText("H")
                BALAOAZUL -> file.appendText("C")
                else -> file.appendText("")
            }
            primeiraColuna = false // Marca que não é mais a primeira coluna e vai começar adicionar as virgulas
        }
        file.appendText("\n")
    }
}

fun nomeDoFicheiro():String{
    println("Introduza o nome do ficheiro (ex: jogo.txt)")
    return readln()
}
fun leJogo(nome: String): Pair<String, Array<Array<String?>>> {
    val file = java.io.File(nome)  // Cria objeto File com o nome do arquivo
    val linhas = file.readLines()   // Lê todas as linhas do arquivo para uma lista
    val nomeJogador = linhas[0]     // A primeira linha (índice 0) contém o nome do jogador


    val numLinhas = linhas.size - 1  // Total de linhas menos a linha do nome
    val numColunas = if (numLinhas > 0) {
        linhas[1].count { it == ',' } + 1  // Conta as vírgulas na primeira linha do tabuleiro e soma 1
    } else {
        0
    }

    val tabuleiro = criaTabuleiroVazio(numLinhas, numColunas) // cria um tabuleiro vazio


    for (i in 0 until numLinhas) {           // Para cada linha do tabuleiro
        val linha = linhas[i + 1]            // Pega a linha do arquivo (+1 pois a primeira linha é o nome)
        val elementos = linha.split(",")      //separa a linha nas vírgulas
        for (j in 0 until numColunas) {      // Para cada coluna
            when (elementos[j]) {             // Verifica o elemento
                "H" -> tabuleiro[i][j] = BALAOVERMELHO  // Se for H, coloca balão vermelho
                "C" -> tabuleiro[i][j] = BALAOAZUL      // Se for C, coloca balão azul
                else -> tabuleiro[i][j] = null          // Se vazio, deixa nulo
            }
        }
    }


    return Pair(nomeJogador, tabuleiro)
}
fun criaMenu(): String {
    // Inicializa variáveis para armazenar o estado atual do jogo
    var tabuleiroAtual: Array<Array<String?>> = arrayOf()
    var nomeJogador = ""

    // Imprime o menu inicial com as opções disponíveis
    println()
    println(OPCAONOVOJOGO)
    println(OPCAOGRAVARJOGO)
    println(OPCAOLERJOGO)
    println(OPCAOSAIR)
    println()

    // Loop principal do menu que continua até o usuário escolher sair
    while (true) {
        // Lê a entrada do usuário e converte para número, usando when para tratar cada opção
        when (readln().toIntOrNull()) {
            1 -> { // Opção Novo Jogo
                // Inicia um novo jogo e armazena o tabuleiro e nome retornados
                val (novoTabuleiro, novoNome) = inicio()
                tabuleiroAtual = novoTabuleiro
                nomeJogador = novoNome
                // Reimprime o menu após finalizar o jogo
                println()
                println(OPCAONOVOJOGO)
                println(OPCAOGRAVARJOGO)
                println(OPCAOLERJOGO)
                println(OPCAOSAIR)
                println()
            }
            2 -> { // Opção Gravar Jogo
                // Verifica se existe um jogo em andamento para gravar
                if (tabuleiroAtual.isEmpty()) {
                    println("Funcionalidade Gravar nao esta disponivel")
                } else {
                    // Obtém nome do arquivo e grava o estado atual do jogo
                    val nomeFicheiro = nomeDoFicheiro()
                    gravaJogo(nomeFicheiro, tabuleiroAtual, nomeJogador)
                    println("Tabuleiro ${tabuleiroAtual.size}x${tabuleiroAtual[0].size} gravado com sucesso")
                }
            }
            3 -> { // Opção Ler Jogo
                // Obtém nome do arquivo e carrega um jogo salvo
                val nomeFicheiro = nomeDoFicheiro()
                val (nome, tabuleiro) = leJogo(nomeFicheiro)
                println("Tabuleiro ${tabuleiro.size}x${tabuleiro[0].size} lido com sucesso!")
                // Continua o jogo carregado do arquivo
                val resultado = inicio(tabuleiro, nome)
                tabuleiroAtual = resultado.first
                nomeJogador = resultado.second
                // Reimprime o menu após finalizar o jogo
                println()
                println(OPCAONOVOJOGO)
                println(OPCAOGRAVARJOGO)
                println(OPCAOLERJOGO)
                println(OPCAOSAIR)
                println()
            }
            0 -> { // Opção Sair
                println("A sair...")
                return "sair"
            }
            else -> println("Opcao invalida. Por favor, tente novamente.") // Trata entrada inválida
        }
    }
}
// Função que processa o estado do jogo e retorna o tabuleiro final e nome do jogador
fun inicio(tabuleiroCarregado: Array<Array<String?>>? = null, nomeJogadorCarregado: String? = null): Pair<Array<Array<String?>>, String> {
    // Determina as dimensões do tabuleiro - novo ou carregado
    val (linhas, colunas) = if (tabuleiroCarregado == null) {
        inicializaTabuleiro()
    } else {
        Pair(tabuleiroCarregado.size, tabuleiroCarregado[0].size)
    }

    // Inicializa o tabuleiro e nome do jogador
    val tabuleiro = tabuleiroCarregado ?: criaTabuleiroVazio(linhas, colunas)
    val jogadorNome = nomeJogadorCarregado ?: obtemNomeJogador()

    // Calcula a última letra disponível para jogadas (ex: se tabuleiro 5x6, última letra é F)
    val ultimaLetra = ('A' + colunas - 1).toString()

    // Mostra o estado inicial do jogo
    mostraEstadoJogo(tabuleiro, jogadorNome, linhas, colunas)

    // Loop principal do jogo
    do {
        print("Coluna? (A..$ultimaLetra):\n")
        val jogada = readln()

        // Processa comando de sair
        if (jogada == "Sair") {
            return Pair(tabuleiro, jogadorNome)
        }

        // Processa comando de gravar
        if (jogada == "Gravar") {
            println("Introduza o nome do ficheiro (ex: jogo.txt)")
            val nomeFicheiro = readln()
            gravaJogo(nomeFicheiro, tabuleiro, jogadorNome)
            println("Tabuleiro ${tabuleiro.size}x${tabuleiro[0].size} gravado com sucesso")
            return Pair(tabuleiro, jogadorNome)
        }

        // Processa a jogada do jogador e verifica se é válida e se houve explosão
        val (jogadaValida, jogadorExplodiu) = processaJogadaJogador(tabuleiro, jogada, colunas)
        if (jogadaValida) {
            println(criaTabuleiro(tabuleiro, true))

            // Verifica se o jogador ganhou
            if (verificaFimJogo(tabuleiro, jogadorNome)) {
                return Pair(tabuleiro, jogadorNome)
            }

            // Processa jogada do computador
            processaJogadaComputador(tabuleiro, linhas, colunas, jogadorExplodiu)
            println(criaTabuleiro(tabuleiro, true))

            // Verifica se o computador ganhou
            if (verificaFimJogo(tabuleiro, "Computador")) {
                return Pair(tabuleiro, jogadorNome)
            }

            // Mostra estado após as jogadas
            print("\n$jogadorNome: $BALAOVERMELHO\n")
            println("Tabuleiro ${linhas}X${colunas}")
        }
    } while (true)
}
// Função que solicita e valida o número de linhas do tabuleiro
fun validadorDeLinhas(): Int {
    // Declara uma variável nullable que armazenará o número de linhas
    var linhas: Int?

    do {
        // Solicita ao usuário o número de linhas
        println("Numero de linhas:")

        // Lê a entrada do usuário e tenta converter para número
        // toIntOrNull() retorna null se a entrada não for um número válido
        linhas = readln().toIntOrNull()

        // Verifica se a entrada é inválida (null ou menor/igual a zero)
        if (linhas == null || linhas <= 0) {
            println("Numero invalido")
        }

        // Continua pedindo entrada até receber um número válido (não null e maior que zero)
    } while (linhas == null || linhas <= 0)

    // Retorna o número de linhas válido
    return linhas
}

// Função que solicita e valida o número de colunas do tabuleiro
fun validadorDeColunas(): Int {
    // Declara variável nullable para armazenar o número de colunas
    var colunas: Int?

    do {
        // Solicita ao usuário o número de colunas
        println("Numero de colunas:")

        // Lê entrada do usuário e tenta converter para número
        // toIntOrNull() retorna null se a entrada não for um número válido
        colunas = readln().toIntOrNull()

        // Verifica se a entrada é inválida (null ou menor/igual a zero)
        if (colunas == null || colunas <= 0) {
            // Exibe mensagem de erro se número for inválido
            println("Numero invalido")
        }

        // Continua pedindo entrada até receber um número válido (não null e maior que zero)
    } while (colunas == null || colunas <= 0)

    // Retorna o número de colunas válido
    return colunas
}


// Função que processa a jogada do jogador, retornando se foi válida e se houve explosão
//Retorna um Pair<Boolean, Boolean> onde:
//Primeiro Boolean: indica se a jogada foi válida
//Segundo Boolean: indica se houve explosão
fun processaJogadaJogador(tabuleiro: Array<Array<String?>>, jogada: String, colunas: Int): Pair<Boolean, Boolean> {
    // Divide a entrada do jogador em palavras para verificar se é um comando de explosão
    val palavras = jogada.split(" ")

    // Verifica se é um comando de explosão (ex: "Explodir A")
    if (palavras.size == 2 && palavras[0] == "Explodir") {
        // Conta o total de balões no tabuleiro
        var totalBaloes = 0
        for (linha in tabuleiro) {
            for (balao in linha) {
                if (balao != null) {
                    totalBaloes++
                }
            }
        }

        // Verifica se há balões suficientes para explodir (mínimo 2)
        if (totalBaloes < 2) {
            println(MENSAGEMEXPLODIRINDISPONIVEL)
            return Pair(false, false)
        }

        // Processa a coluna onde o jogador quer explodir
        val coluna = palavras[1]
        val colIndex = processaColuna(colunas, coluna) ?: -1

        // Verifica se a coluna está vazia
        if (colIndex >= 0 && contaBaloesColuna(tabuleiro, colIndex) == 0) {
            println("Coluna vazia")
            return Pair(false, false)
        }

        // Tenta explodir o balão
        val explodiu = explodeBalao(tabuleiro, Pair(0, processaColuna(colunas, coluna) ?: -1))
        if (explodiu) {
            // Sucesso na explosão
            println("Balao $coluna explodido!")
            return Pair(true, true)
        } else {
            // Falha na explosão
            println(MENSAGEMCOLUNAINVALIDA)
            return Pair(false, false)
        }
    }

    // Se não for explosão, processa como jogada normal
    val indiceColuna = processaColuna(colunas, jogada)
    if (indiceColuna != null) {
        // Tenta colocar o balão na coluna escolhida
        if (!colocaBalao(tabuleiro, indiceColuna, true)) {
            println(MENSAGEMCOLUNAINVALIDA)
            return Pair(false, false)
        }
        // Sucesso ao colocar o balão
        print(MENSAGEMCOLUNAESCOLHIDA)
        print(jogada)
        println()
        return Pair(true, false)
    }

    // Jogada inválida
    println(MENSAGEMCOLUNAINVALIDA)
    return Pair(false, false)
}
// Função que processa a jogada do computador, incluindo jogadas normais e explosões
fun processaJogadaComputador(tabuleiro: Array<Array<String?>>, linhas: Int, colunas: Int, jogadorExplodiu: Boolean = false) {
    // Verifica se o computador deve explodir um balão (em resposta à explosão do jogador)
    if (jogadorExplodiu) {
        // Avisa o jogador e aguarda confirmação
        println()
        println("Prima enter para continuar. O computador ira agora explodir um dos seus baloes")
        readln()

        // Obtém as coordenadas do balão que o computador vai explodir
        val coordenadas = jogadaExplodirComputador(tabuleiro)
        // Tenta explodir o balão e mostra mensagem de confirmação
        if (explodeBalao(tabuleiro, coordenadas)) {
            // Mostra mensagem indicando qual balão foi explodido
            // 'A' + coordenadas.second converte o índice numérico para letra (0=A, 1=B, etc)
            println("Balao ${('A' + coordenadas.second)},${coordenadas.first + 1} explodido pelo Computador!")
        }
        return
    }

    // Se não houve explosão, faz uma jogada normal
    // Obtém a coluna onde o computador vai jogar
    val colunaComputador = jogadaNormalComputador(tabuleiro)
    if (colunaComputador >= 0) {  // Se encontrou uma coluna válida
        // Tenta colocar o balão na coluna escolhida
        if (colocaBalao(tabuleiro, colunaComputador, false)) {
            // Mostra informações sobre a jogada do computador
            println("\nComputador: $BALAOAZUL")
            println("Tabuleiro ${linhas}X${colunas}")
            println("Coluna escolhida: ${('A' + colunaComputador)}")
        }
    }
}
// Função que mostra o estado atual do jogo na tela
fun mostraEstadoJogo(tabuleiro: Array<Array<String?>>, jogadorNome: String, linhas: Int, colunas: Int) {
    // Imprime o tabuleiro com a legenda (true indica para mostrar a legenda)
    println(criaTabuleiro(tabuleiro, true))

    // Imprime o nome do jogador e seu símbolo (balão vermelho)
    print("\n$jogadorNome: $BALAOVERMELHO\n")

    // Imprime as dimensões do tabuleiro (ex: "Tabuleiro 6X7")
    println("$MENSAGEMTABULEIRO $linhas$SEPARADOR$colunas")
}
// Função que verifica se o jogo terminou, seja por vitória ou empate
fun verificaFimJogo(tabuleiro: Array<Array<String?>>, jogadorNome: String): Boolean {
    // Verifica se houve vitória (4 balões em linha)
    if (ganhouJogo(tabuleiro)) {
        println()
        // Verifica quem ganhou e mostra mensagem apropriada
        if (jogadorNome == "Computador") {
            // Se foi o computador, mostra mensagem de derrota
            println("Perdeu! Ganhou o Computador.")
        } else {
            // Se foi o jogador humano, mostra mensagem de parabéns
            println("Parabens $jogadorNome! Ganhou!")
        }
        // Retorna true pois o jogo acabou
        return true
    }

    // Verifica se houve empate (tabuleiro cheio sem vitória)
    if (eEmpate(tabuleiro)) {
        println()
        println("Empate!")
        // Retorna true pois o jogo acabou
        return true
    }

    // Se não houve vitória nem empate, retorna false (jogo continua)
    return false
}

// Função que inicializa o tabuleiro obtendo e validando suas dimensões
fun inicializaTabuleiro(): Pair<Int, Int> {
    // Declara variáveis para armazenar dimensões do tabuleiro
    var linhas: Int
    var colunas: Int

    do {
        // Obtém número de linhas do usuário (com validação)
        linhas = validadorDeLinhas()
        // Obtém número de colunas do usuário (com validação)
        colunas = validadorDeColunas()

        // Verifica se as dimensões são válidas para o jogo
        if (!validaTabuleiro(linhas, colunas)) {
            println("Tamanho do tabuleiro invalido")
        }

        // Continua pedindo dimensões até que sejam válidas
    } while (!validaTabuleiro(linhas, colunas))

    // Retorna as dimensões válidas como um par
    return Pair(linhas, colunas)
}

fun obtemNomeJogador(): String {
    var jogadorNome: String
    do {
        println("Nome do jogador 1:")
        jogadorNome = readln()
        if (!nomeValido(jogadorNome)) {
            println("Nome de jogador invalido")
        }
    } while (!nomeValido(jogadorNome))
    return jogadorNome
}


// Função que valida se as dimensões do tabuleiro seguem a regra: número de colunas = número de linhas + 1
fun validaTabuleiro(numLinhas: Int, numColunas: Int): Boolean {
    // Verifica se o número de colunas é igual ao número de linhas mais um
    // Por exemplo: se linhas = 6, colunas deve ser 7
    return numColunas == numLinhas + 1
}

// Função que verifica se uma palavra contém espaços em branco
fun palavraContemEspaco(nome: String): Boolean {
    // Inicializa contador para percorrer a string
    var count = 0

    // Percorre cada caractere da string
    while (count < nome.length) {
        // Se encontrar um espaço em branco
        if (nome[count] == ' ') {
            // Retorna true imediatamente
            return true
        }
        // Avança para o próximo caractere
        count++
    }

    // Se não encontrou nenhum espaço, retorna false
    return false
}

// Função que verifica se um nome de jogador é válido
fun nomeValido(nome: String): Boolean {
    // Nome deve ter entre 3 e 12 caracteres e não pode conter espaços
    return (nome.length in 3..12 && !palavraContemEspaco(nome))
}
// Função que processa e valida a coluna escolhida
fun processaColuna(numColunas: Int, coluna: String?): Int? {
    // Verifica se a coluna é válida
    if (coluna == null || coluna.length != 1) {
        return null
    }
    val letra = coluna[0]

    // Verifica se é uma letra maiúscula
    if (letra !in 'A'..'Z') {
        return null
    }
    // Converte letra para índice (A=0, B=1, etc)
    val indiceLetra = letra - 'A'

    // Verifica se o índice está dentro dos limites do tabuleiro
    if (indiceLetra < numColunas) {
        return indiceLetra
    }
    return null
}
// Função que cria o topo do tabuleiro com caracteres Unicode
fun criaTopoTabuleiro(numColunas: Int): String {
    val cantoEsquerdo = "\u2554"  // ╔
    val linhaHorizontal = "\u2550" // ═
    val cantoDireito = "\u2557"    // ╗
    var topo = ""
    var count = 0
    // Cria a linha horizontal do topo
    do {
        topo += "$linhaHorizontal$linhaHorizontal$linhaHorizontal$linhaHorizontal"
        count++
    } while (count < numColunas - 1)
    topo += linhaHorizontal + linhaHorizontal + linhaHorizontal
    return cantoEsquerdo + topo + cantoDireito
}

// Função que cria a legenda horizontal do tabuleiro (A, B, C, etc)
fun criaLegendaHorizontal(numColunas: Int): String {
    var legenda = ""
    val espaco = "  "
    var count = 0
    val primeiraLetra = 'A'

    // Cria a legenda com letras e separadores
    do {
        legenda += primeiraLetra + count
        if (count < numColunas - 1) {
            legenda += " | "
        }
        count++
    } while (count < numColunas)
    return "  $legenda$espaco"
}

// Função que cria a representação visual do tabuleiro como uma string
fun criaTabuleiro(tabuleiro: Array<Array<String?>>, mostraLegenda: Boolean = true): String {
    // Obtém as dimensões do tabuleiro
    val numLinhas = tabuleiro.size
    val numColunas = if (numLinhas > 0) tabuleiro[0].size else 0

    // Cria a parte superior do tabuleiro com caracteres especiais
    val topo = criaTopoTabuleiro(numColunas)
    var corpo = ""

    // Constrói o corpo do tabuleiro linha por linha
    for (linha in tabuleiro.indices) {
        // Adiciona borda esquerda (║)
        corpo += "\u2551"

        // Preenche cada célula da linha
        for (coluna in tabuleiro[0].indices) {
            // Obtém o balão na posição atual (pode ser null)
            val balao = tabuleiro[linha][coluna]
            // Adiciona balão ou espaço vazio
            corpo += if (balao != null) " $balao " else "   "
            // Adiciona separador vertical entre colunas, exceto na última
            if (coluna < numColunas - 1) corpo += "|"
        }

        // Adiciona borda direita (║)
        corpo += "\u2551"
        // Adiciona quebra de linha, exceto na última linha
        if (linha < numLinhas - 1) corpo += "\n"
    }

    // Combina topo e corpo
    val base = "$topo\n$corpo"

    // Retorna tabuleiro com ou sem legenda conforme parâmetro
    return if (mostraLegenda) "$base\n${criaLegendaHorizontal(numColunas)}" else base
}
// Função que cria uma matriz vazia para representar o tabuleiro inicial
fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int): Array<Array<String?>> {
    // Cria e retorna uma matriz 2D onde:
    // - O primeiro Array cria as linhas (numLinhas)
    // - Para cada linha, cria um Array de colunas (numColunas)
    // - Cada posição é inicializada com null (vazia)
    return Array(numLinhas) { Array(numColunas) { null } }
}
// Função que conta quantos balões existem em uma linha específica do tabuleiro
fun contaBaloesLinha(tabuleiro: Array<Array<String?>>, linha: Int): Int {
    // Inicializa contador de balões
    var count = 0

    // Percorre cada coluna da linha especificada
    for (coluna in 0 until tabuleiro[linha].size) {
        // Se encontrar um balão (posição não nula), incrementa o contador
        if (tabuleiro[linha][coluna] != null) {
            count++
        }
    }

    // Retorna o total de balões encontrados na linha
    return count
}

// Função que conta quantos balões existem em uma coluna específica do tabuleiro
fun contaBaloesColuna(tabuleiro: Array<Array<String?>>, coluna: Int): Int {
    // Inicializa contador de balões
    var count = 0

    // Percorre cada linha do tabuleiro
    for (linha in tabuleiro) {
        // Se encontrar um balão na coluna especificada (posição não nula), incrementa o contador
        if (linha[coluna] != null) {
            count++
        }
    }

    // Retorna o total de balões encontrados na coluna
    return count
}
// Função que tenta colocar um balão em uma coluna específica do tabuleiro
fun colocaBalao(tabuleiro: Array<Array<String?>>, coluna: Int, humano: Boolean): Boolean {
    // Verifica se a coluna é válida (dentro dos limites do tabuleiro)
    if (coluna < 0 || coluna >= tabuleiro[0].size) {
        return false
    }

    // Percorre as linhas de baixo para cima procurando primeira posição vazia
    for (linha in tabuleiro.indices) {
        // Se encontrar posição vazia (null)
        if (tabuleiro[linha][coluna] == null) {
            // Coloca o balão apropriado (vermelho para humano, azul para computador)
            tabuleiro[linha][coluna] = if (humano) BALAOVERMELHO else BALAOAZUL
            return true  // Sucesso ao colocar o balão
        }
    }

    // Se não encontrou posição vazia, retorna falso
    return false
}
// Função que determina a jogada normal do computador, retornando a coluna escolhida
fun jogadaNormalComputador(tabuleiro: Array<Array<String?>>): Int {
    // Percorre cada posição do tabuleiro
    for (linha in tabuleiro.indices) {
        for (coluna in tabuleiro[0].indices) {
            // Se encontrar uma posição vazia
            if (tabuleiro[linha][coluna] == null) {
                // Flag para verificar se todas posições abaixo estão preenchidas
                var posicoesAbaixoPreenchidas = true
                var linhaAbaixo = 0

                // Verifica todas as posições abaixo da posição atual
                while (linhaAbaixo < linha && posicoesAbaixoPreenchidas) {
                    // Se encontrar uma posição vazia abaixo
                    if (tabuleiro[linhaAbaixo][coluna] == null) {
                        // Marca que nem todas posições estão preenchidas
                        posicoesAbaixoPreenchidas = false
                    }
                    linhaAbaixo++
                }

                // Se todas posições abaixo estiverem preenchidas, escolhe esta coluna
                if (posicoesAbaixoPreenchidas) {
                    return coluna
                }
            }
        }
    }
    // Se não encontrar posição adequada, retorna -1
    return -1
}
// Função que verifica se existe uma vitória na horizontal (4 balões iguais seguidos)
fun eVitoriaHorizontal(tabuleiro: Array<Array<String?>>): Boolean {
    // Percorre cada linha do tabuleiro
    for (linha in tabuleiro.indices) {
        // Contadores para sequência atual
        var contBaloes = 0          // Conta balões consecutivos da mesma cor
        var ultimoBalao: String? = null  // Guarda a cor do último balão visto

        // Percorre cada coluna da linha atual
        for (coluna in tabuleiro[linha].indices) {
            // Obtém o balão na posição atual
            val balaoAtual = tabuleiro[linha][coluna]

            // Se encontrou um balão
            if (balaoAtual != null) {
                // Se for da mesma cor do último balão
                if (balaoAtual == ultimoBalao) {
                    contBaloes++  // Incrementa contador de sequência
                    // Se alcançou 4 balões consecutivos
                    if (contBaloes >= 4) {
                        return true  // Vitória encontrada
                    }
                } else {
                    // Se for de cor diferente, reinicia contagem
                    contBaloes = 1
                    ultimoBalao = balaoAtual  // Atualiza último balão
                }
            } else {
                // Se posição vazia, reinicia contagem
                contBaloes = 0
                ultimoBalao = null
            }
        }
    }
    // Se não encontrou vitória
    return false
}
// Função que verifica se existe uma vitória na vertical (4 balões iguais em coluna)
fun eVitoriaVertical(tabuleiro: Array<Array<String?>>): Boolean {
    // Percorre cada coluna do tabuleiro
    for (coluna in tabuleiro[0].indices) {
        // Contadores para sequência atual
        var contBaloes = 0          // Conta balões consecutivos da mesma cor
        var ultimoBalao: String? = null  // Guarda a cor do último balão visto

        // Percorre cada linha da coluna atual (de baixo para cima)
        for (linha in tabuleiro) {
            // Obtém o balão na posição atual
            val balaoAtual = linha[coluna]

            // Se encontrou um balão
            if (balaoAtual != null) {
                // Se for da mesma cor do último balão
                if (balaoAtual == ultimoBalao) {
                    contBaloes++  // Incrementa contador de sequência
                    // Se alcançou 4 balões consecutivos
                    if (contBaloes >= 4) {
                        return true  // Vitória encontrada
                    }
                } else {
                    // Se for de cor diferente, reinicia contagem
                    contBaloes = 1
                    ultimoBalao = balaoAtual  // Atualiza último balão
                }
            } else {
                // Se posição vazia, reinicia contagem
                contBaloes = 0
                ultimoBalao = null
            }
        }
    }
    // Se não encontrou vitória
    return false
}
// Função que verifica se existe uma vitória na diagonal (4 balões iguais em diagonal)
fun eVitoriaDiagonal(tabuleiro: Array<Array<String?>>): Boolean {
    // Verifica diagonais da esquerda para direita (↘)
    for (linha in 0..tabuleiro.size - 4) {  // Limita até 4 posições do fim
        for (coluna in 0..tabuleiro[0].size - 4) {  // Limita até 4 posições do fim
            // Obtém o balão inicial da diagonal
            val balaoInicial = tabuleiro[linha][coluna]
            if (balaoInicial != null) {
                var contBaloes = 0
                // Verifica 4 posições na diagonal
                for (i in 0..3) {
                    // Se encontrar balão igual na diagonal
                    if (tabuleiro[linha + i][coluna + i] == balaoInicial) {
                        contBaloes++
                    }
                }
                // Se encontrou 4 balões iguais
                if (contBaloes == 4) {
                    return true
                }
            }
        }
    }

    // Verifica diagonais da direita para esquerda (↙)
    for (linha in 0..tabuleiro.size - 4) {  // Limita até 4 posições do fim
        for (coluna in 3 until tabuleiro[0].size) {  // Começa 3 posições da borda
            // Obtém o balão inicial da diagonal
            val balaoInicial = tabuleiro[linha][coluna]
            if (balaoInicial != null) {
                var contBaloes = 0
                // Verifica 4 posições na diagonal
                for (i in 0..3) {
                    // Se encontrar balão igual na diagonal
                    if (tabuleiro[linha + i][coluna - i] == balaoInicial) {
                        contBaloes++
                    }
                }
                // Se encontrou 4 balões iguais
                if (contBaloes == 4) {
                    return true
                }
            }
        }
    }

    return false
}


fun ganhouJogo(tabuleiro: Array<Array<String?>>): Boolean {
    return eVitoriaHorizontal(tabuleiro) ||
            eVitoriaVertical(tabuleiro) ||
            eVitoriaDiagonal(tabuleiro)
}
// Função que verifica se o jogo terminou em empate (tabuleiro completamente preenchido)
fun eEmpate(tabuleiro: Array<Array<String?>>): Boolean {
    // Verifica cada linha do tabuleiro
    for (linha in tabuleiro.indices) {
        // Se encontrar uma linha que não está totalmente preenchida
        // (número de balões menor que o tamanho da linha)
        if (contaBaloesLinha(tabuleiro, linha) < tabuleiro[0].size) {
            // Não é empate, ainda há espaço para jogadas
            return false
        }
    }

    // Se todas as linhas estiverem completamente preenchidas, é empate
    return true
}
// Função que implementa a mecânica de explosão de balões
fun explodeBalao(tabuleiro: Array<Array<String?>>, coordenadas: Pair<Int, Int>): Boolean {
    // Extrai linha e coluna das coordenadas
    val (linha, coluna) = coordenadas

    // Verifica se as coordenadas estão dentro dos limites do tabuleiro
    if (linha < 0 || linha >= tabuleiro.size ||
        coluna < 0 || coluna >= tabuleiro[0].size) {
        return false  // Coordenadas inválidas
    }

    // Verifica se existe um balão na posição indicada
    if (tabuleiro[linha][coluna] == null) {
        return false  // Não há balão para explodir
    }

    // Faz os balões "caírem" após a explosão
    // Move cada balão uma posição para baixo
    for (i in linha until (tabuleiro.size - 1)) {
        tabuleiro[i][coluna] = tabuleiro[i + 1][coluna]
    }
    // Limpa a última posição da coluna
    tabuleiro[tabuleiro.size - 1][coluna] = null

    // Indica que a explosão foi bem sucedida
    return true
}

// Esta função analisa o tabuleiro do jogo e retorna as coordenadas (linha, coluna)
// onde o computador deve fazer sua jogada
fun jogadaExplodirComputador(tabuleiro: Array<Array<String?>>): Pair<Int, Int> {

    // Primeiro loop: verifica sequências horizontais de 3 balões vermelhos
    for (linha in tabuleiro.indices) {
        var contBaloes = 0          // Contador de balões vermelhos consecutivos
        var primeiroBalao = -1      // Guarda a posição do primeiro balão da sequência
        for (coluna in tabuleiro[0].indices) {
            if (tabuleiro[linha][coluna] == BALAOVERMELHO) {
                if (contBaloes == 0) primeiroBalao = coluna  // Marca início da sequência
                contBaloes++
                if (contBaloes == 3) {  // Se encontrou 3 balões consecutivos
                    return Pair(linha, primeiroBalao)  // Retorna posição do primeiro balão
                }
            } else {
                // Reseta contadores se a sequência for interrompida
                contBaloes = 0
                primeiroBalao = -1
            }
        }
    }

    // Segundo loop: verifica sequências verticais de 3 balões vermelhos
    // Mesma lógica do loop anterior, mas verificando colunas
    for (coluna in tabuleiro[0].indices) {
        var contBaloes = 0
        var primeiroBalao = -1
        for (linha in tabuleiro.indices) {
            if (tabuleiro[linha][coluna] == BALAOVERMELHO) {
                if (contBaloes == 0) primeiroBalao = linha
                contBaloes++
                if (contBaloes == 3) {
                    return Pair(primeiroBalao, coluna)
                }
            } else {
                contBaloes = 0
                primeiroBalao = -1
            }
        }
    }

    // Se não encontrou sequências de 3, procura a coluna com mais balões
    var maxBaloes = -1
    var colunaEscolhida = 0

    // Percorre todas as colunas contando número de balões
    for (coluna in tabuleiro[0].indices) {
        val numBaloes = contaBaloesColuna(tabuleiro, coluna)
        if (numBaloes >= maxBaloes) {  // Encontra coluna com maior número de balões
            maxBaloes = numBaloes
            colunaEscolhida = coluna
        }
    }

    // Encontra primeira posição livre na coluna escolhida
    for (linha in tabuleiro.indices) {
        if (tabuleiro[linha][colunaEscolhida] != null) {
            return Pair(linha, colunaEscolhida)
        }
    }

    // Se a coluna estiver vazia, retorna primeira linha
    return Pair(0, colunaEscolhida)
}