package br.unisul.ia.velha;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Classe que implementa a logica do Jogo da Velha.
 */
public class VelhaIA {

	/**
	 * Constante que representa o Jogador X.
	 */
	public static final char JOGADOR_X = 'X';

	/**
	 * Constante que representa o Jogador O.
	 */
	public static final char JOGADOR_O = 'O';

	/**
	 * Constante que representa um espaço vazio no tabuleiro.
	 */
	public static final char VAZIO = ' ';

	/**
	 * Constante que representa o jogador da rodada.
	 */
	public static char JOGADOR_DA_VEZ = JOGADOR_X;

	/**
	 * Aguarde a vez de jogar.
	 */
	public static final String STATUS_AGUARDE_SUA_VEZ = "aguarde-sua-vez";

	/**
	 * Sua vez de jogar.
	 */
	public static final String STATUS_SUA_VEZ_JOGAR = "sua-vez-jogar";

	/**
	 * Jogador ganhou a partida.
	 */
	public static final String STATUS_VOCE_GANHOU = "voce-ganhou";

	/**
	 * Jogador perdeu a partida.
	 */
	public static final String STATUS_VOCE_PERDEU = "voce-perdeu";

	/**
	 * Ocorreu um empate.
	 */
	public static final String STATUS_EMPATE = "empate";

	/**
	 * Posições possíveis para ganhar a partida.
	 */
	private final int TRIOS_GANHADORES[][] = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
			{ 0, 4, 8 }, { 2, 4, 6 } };

	/**
	 * Tabuleiro com 9 posições.
	 */
	private char[] tabuleiro = new char[9];

	/**
	 * Dificudade da partida.
	 */
	private int dificudade;

	/**
	 * Construtor da classe.
	 */
	public VelhaIA() {
		limparTabuleiro();
	}

	/**
	 * Método que retorna o oponente do jogador atual.
	 */
	public static char getOponente(final char jogador) {
		switch (jogador) {
			case JOGADOR_X: {
				return JOGADOR_O;
			}
			case JOGADOR_O: {
				return JOGADOR_X;
			}
			default: {
				return VAZIO;
			}
		}
	}

	/**
	 * Método que retorna a posição da jogada no tabuleiro.
	 */
	public char getPosicao(final int posicao) {
		return tabuleiro[posicao];
	}

	/**
	 * Método que preenche a posição do jogador no tabuleiro.
	 */
	public void setPosicao(final int pos, final char jogador) {
		if (jogador == JOGADOR_O || jogador == JOGADOR_X) {
			tabuleiro[pos] = jogador;
		}
		alteraJogadorDaVez(jogador);
	}

	/**
	 * Método que altera o jogador da rodada.
	 */
	private void alteraJogadorDaVez(final char jogadorDaVez) {
		JOGADOR_DA_VEZ = jogadorDaVez == JOGADOR_X ? JOGADOR_O : JOGADOR_X;
	}

	/**
	 * Método que limpa o tabuleiro, preenchendo com 'vazio' os espaços.
	 */
	public void limparTabuleiro() {
		JOGADOR_DA_VEZ = JOGADOR_X;
		for (int pos = 0; pos < tabuleiro.length; pos++) {
			tabuleiro[pos] = VAZIO;
		}
	}

	/**
	 * Método que verifica se o jogo acabou.
	 * Se ocorreu Empate, ou 
	 * se o jogador X ganhou, ou
	 * se o jogador O ganhou.
	 */
	public boolean isFimDeJogo() {
		return (isCheio() || isGanhador(JOGADOR_X) || isGanhador(JOGADOR_O));
	}

	/**
	 * Método que retorna o ganhador da partida.
	 */
	public char getGanhador() {
		int posicoesGanhadoras[] = getPosicoesGanhadoras();

		if (posicoesGanhadoras == null || posicoesGanhadoras.length < 3) {
			return VAZIO;
		} else {
			return tabuleiro[posicoesGanhadoras[0]];
		}
	}

	/**
	 * Método que retorna a posição ganhadora da partida,
	 * ou retorna 'null' caso nenhum jogador tenha ganhado. 
	 */
	public int[] getPosicoesGanhadoras() {
		int posicaoGanhadoras[] = null;
		int posicao[] = new int[3];
		char jogada[] = new char[3];

		for (int indice = 0; indice < TRIOS_GANHADORES.length; indice++) {
			for (int ponto = 0; ponto < 3; ponto++) {
				posicao[ponto] = TRIOS_GANHADORES[indice][ponto];
				jogada[ponto] = tabuleiro[posicao[ponto]];
			}

			if (jogada[0] != VAZIO && jogada[0] == jogada[1] && jogada[1] == jogada[2]) {
				posicaoGanhadoras = new int[3];
				for (int ponto = 0; ponto < 3; ponto++) {
					posicaoGanhadoras[ponto] = posicao[ponto];
				}
			}
		}
		return posicaoGanhadoras;
	}

	/**
	 * Método que verifica se o jogador ganhou.
	 */
	public boolean isGanhador(final char jogador) {
		if (VAZIO == getGanhador()) {
			return false;
		}
		return getGanhador() == jogador;
	}
	
	/**
	 * Método que verifica se houve um empate.
	 */
	public boolean isEmpate() {
		return isCheio() && getGanhador() == VAZIO;
	}

	/**
	 * Método que verifica se o tabuleiro está vazio.
	 */
	public boolean isVazio() {
		return isVazioEntre(0, tabuleiro.length - 1);
	}

	/**
	 * Método que possui a "IA" necessaria para retornar a proxima jogada.
	 * Algoritmo baseado na "Jogada Perfeita"
	 * descrita em 'http://pt.wikipedia.org/wiki/Jogo_da_velha#Jogada_perfeita'.
	 */
	public int getJogada() {
		int jogada = -1;

		/**
		 *  Jogada Perfeita: primeiro passo. 
		 *  Ganhar: Se você tem duas peças numa linha, ponha a terceira.
		 */
		jogada = getJogadaAtaque(JOGADOR_DA_VEZ);
		if (jogada >= 0) { 
			return jogada;
		}

		/** 
		 * Jogada Perfeita: segundo passo.
		 * Bloquear: Se o oponente tiver duas peças em linha, ponha a terceira para bloqueá-lo. 
		 */
		if (new Random().nextInt(10) >= getDificudade()) {
			jogada = getJogadaDefesa(JOGADOR_DA_VEZ);
			if (jogada >= 0) {
				return jogada;
			}
		}

		/** Jogada Perfeita: terceiro passo.
		 *  Triângulo: Crie uma oportunidade em que você poderá ganhar de duas maneiras.
		 * 
		 * Obs: para que a 'IA' começe sempre pelos cantos, deixando o jogo chato, foi
		 * adicionado a condição abaixo.
		 */
		if (isVazio() && new Random().nextBoolean()) {
			jogada = getJogadaCanto(JOGADOR_DA_VEZ);
			if (jogada >= 0) {
				return jogada;
			}
		}
		
		jogada = getJogadaAtaqueTriangulo(JOGADOR_DA_VEZ);
		if (jogada >= 0) { 
			return jogada;
		}

		/** Jogada Perfeita: quarto passo. 
		 *  Bloquear o Triângulo do oponente:
		 *     Opção 1: Crie 2 peças em linha para forçar o oponente a se defender, contanto que não resulte nele criando um triângulo ou vencendo. Por exemplo, se 'X' tem dois cantos opostos do tabuleiro e 'O' tem o centro, 'O' não pode jogar num canto (Jogar no canto nesse cenário criaria um triângulo em que 'X' vence).
         *     Opção 2: Se existe uma configuração em que o oponente pode formar um triângulo, bloqueiem-no.
		 */
		if (new Random().nextInt(10) >= getDificudade()) {
			jogada = getJogadaDefesaTriangulo(JOGADOR_DA_VEZ);
			if (jogada >= 0)
				return jogada;
		}

		/** Jogada Perfeita: quinto passo.
		 *	Centro: Jogue no centro. 
		 */
		if (tabuleiro[4] == VAZIO) {
			jogada = 4;
			return jogada;
		}

		/** Jogada Perfeita: sexto passo
		 *  Canto vazio: jogue num canto vazio. 
		 */
		jogada = getJogadaCanto(JOGADOR_DA_VEZ);
		if (jogada >= 0)
			return jogada;

		/** 
		 * Última opção: jogar nas bordas.
		 */
		jogada = getJogadaBorda(JOGADOR_DA_VEZ);
		if (jogada >= 0)
			return jogada;

		return -1;
	}

	/**
	 * Método que verifica se um trecho do tabuleiro está vazio.
	 */
	private boolean isVazioEntre(int inicio, int fim) {
		if (inicio > fim) {
			int inicioTemporario = inicio;
			inicio = fim;
			fim = inicioTemporario;
		}

		if (inicio < 0) {
			inicio = 0;
		}

		if (fim >= tabuleiro.length) {
			fim = tabuleiro.length - 1;
		}

		for (int posicao = inicio; posicao <= fim; posicao++) {
			if (tabuleiro[posicao] != VAZIO) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Método que verifica se o tabuleiro está completo.
	 */
	public boolean isCheio() {
		for (int posicao = 0; posicao < tabuleiro.length; posicao++) {
			if (tabuleiro[posicao] == VAZIO)
				return false;
		}
		return true;
	}

	/**
	 * Método que retorna uma possível jogada de ataque para o jogador especificado. A
	 * jogada de ataque é aquela que completa três posições em linha para que o
	 * jogador ganhe o jogo.
	 */
	private int getJogadaAtaque(final char jogador) {

		List<Integer> jogadasPossiveis = new ArrayList<Integer>();
		for (int posicao = 0; posicao < 9; posicao++) {
			if (tabuleiro[posicao] == VAZIO) {
				jogadasPossiveis.add(posicao);
			}
		}

		List<Integer> boasJogadas = getMelhoresJogadas(jogadasPossiveis, new char[] { VAZIO, jogador, jogador });

		if (boasJogadas.size() > 0) {
			return boasJogadas.get(0);
		}

		return -1;
	}

	/**
	 * Método que retorna uma possível jogada de defesa para o jogador especificado. A
	 * jogada de defesa é aquela que bloqueia o oponente de completar três
	 * posições em linha.
	 */
	private int getJogadaDefesa(final char jogador) {
		return getJogadaAtaque(getOponente(jogador));
	}

	/**
	 * Método que retorna uma possível jogada de defesa em "triangulo" para o jogador
	 * especificado. A jogada em "triangulo" é aquela em que o jogador tem duas
	 * possibilidades simultaneas de completar uma linha e ganhar o jogo.
	 */
	private int getJogadaDefesaTriangulo(final char jogador) {
		if (isJogarNaBorda(jogador)) {
			return getJogadaBorda(jogador);
		}

		if (isJogadorNoCentroEBordasVazias(jogador)) {

			if (isJogarNaPosicao2ParaBordasVazias(jogador)) {
				return 2;
			}
			
			if (isJogarNaPosicao8ParaBordasVazias(jogador)) {
				return 8;
			}
			
			if (isJogarNaPosicao6ParaBordasVazias(jogador)) {
				return 6;
			}
			
			if (isJogarNaPosicao0ParaBordasVazias(jogador)) {
				return 0;
			}
		}
		
		if (tabuleiro[4] == jogador) {

			if (isJogarNaPosicao2(jogador)) {
				return 2;
			}
			
			if (isJogarNaPosicao8(jogador)) {
				return 8;
			}
			
			if (isJogarNaPosicao6(jogador)) {
				return 6;
			}
			
			if (isJogarNaPosicao0(jogador)) {
				return 0;
			}
		}

		return -1;
	}

	/**
	 * Método que retorna a opção de jogar na posição 0 
	 */
	private boolean isJogarNaPosicao0(final char jogador) {
		return tabuleiro[0] == VAZIO 
				&& tabuleiro[1] == getOponente(jogador) 
				&& tabuleiro[2] == VAZIO
				&& tabuleiro[3] == VAZIO 
				&& tabuleiro[5] == VAZIO 
				&& tabuleiro[6] == getOponente(jogador)
				&& tabuleiro[7] == VAZIO 
				&& tabuleiro[8] == VAZIO;
	}

	/**
	 * Método que retorna a opção de jogar na posição 6 
	 */
	private boolean isJogarNaPosicao6(final char jogador) {
		return tabuleiro[0] == VAZIO 
				&& tabuleiro[1] == VAZIO 
				&& tabuleiro[2] == VAZIO
				&& tabuleiro[3] == getOponente(jogador) 
				&& tabuleiro[5] == VAZIO 
				&& tabuleiro[6] == VAZIO
				&& tabuleiro[7] == VAZIO 
				&& tabuleiro[8] == getOponente(jogador);
	}

	/**
	 * Método que retorna a opção de jogar na posição 8 
	 */
	private boolean isJogarNaPosicao8(final char jogador) {
		return tabuleiro[0] == VAZIO 
				&& tabuleiro[1] == VAZIO 
				&& tabuleiro[2] == getOponente(jogador)
				&& tabuleiro[3] == VAZIO 
				&& tabuleiro[5] == VAZIO 
				&& tabuleiro[6] == VAZIO
				&& tabuleiro[7] == getOponente(jogador) 
				&& tabuleiro[8] == VAZIO;
	}

	/**
	 * Método que retorna a opção de jogar na posição 2 
	 */
	private boolean isJogarNaPosicao2(final char jogador) {
		return tabuleiro[0] == getOponente(jogador) 
				&& tabuleiro[1] == VAZIO 
				&& tabuleiro[2] == VAZIO
				&& tabuleiro[3] == VAZIO 
				&& tabuleiro[5] == getOponente(jogador) 
				&& tabuleiro[6] == VAZIO
				&& tabuleiro[7] == VAZIO 
				&& tabuleiro[8] == VAZIO;
	}
	/**
	 * Método que verifica se o jogador está no centro
	 * e as se as bordas do tabuleiro estão vazias.
	 */
	private boolean isJogadorNoCentroEBordasVazias(final char jogador) {
		return tabuleiro[4] == jogador 
				&& tabuleiro[0] == VAZIO 
				&& tabuleiro[2] == VAZIO 
				&& tabuleiro[6] == VAZIO
				&& tabuleiro[8] == VAZIO;
	}

	/**
	 * Método que retorna a opção de jogar na posição 0
	 * Se as bordas estiverem vazias 
	 */
	private boolean isJogarNaPosicao0ParaBordasVazias(final char jogador) {
		return tabuleiro[1] == getOponente(jogador) 
				&& tabuleiro[3] == getOponente(jogador) 
				&& tabuleiro[5] == VAZIO
				&& tabuleiro[7] == VAZIO;
	}

	/**
	 * Método que retorna a opção de jogar na posição 6
	 * Se as bordas estiverem vazias 
	 */
	private boolean isJogarNaPosicao6ParaBordasVazias(final char jogador) {
		return tabuleiro[1] == VAZIO 
				&& tabuleiro[3] == getOponente(jogador) 
				&& tabuleiro[5] == VAZIO
				&& tabuleiro[7] == getOponente(jogador);
	}

	/**
	 * Método que retorna a opção de jogar na posição 8
	 * Se as bordas estiverem vazias 
	 */
	private boolean isJogarNaPosicao8ParaBordasVazias(final char jogador) {
		return tabuleiro[1] == VAZIO 
				&& tabuleiro[3] == VAZIO 
				&& tabuleiro[5] == getOponente(jogador)
				&& tabuleiro[7] == getOponente(jogador);
	}

	/**
	 * Método que retorna a opção de jogar na posição 2
	 * Se as bordas estiverem vazias 
	 */
	private boolean isJogarNaPosicao2ParaBordasVazias(final char jogador) {
		return tabuleiro[1] == getOponente(jogador) 
				&& tabuleiro[3] == VAZIO 
				&& tabuleiro[5] == getOponente(jogador)
				&& tabuleiro[7] == VAZIO;
	}

	/**
	 * Método que retorna a opção de jogar nas bordas.
	 */
	private boolean isJogarNaBorda(final char jogador) {
		return (tabuleiro[0] == getOponente(jogador) 
				&& tabuleiro[1] == VAZIO 
				&& tabuleiro[2] == VAZIO
				&& tabuleiro[3] == VAZIO 
				&& tabuleiro[4] == jogador 
				&& tabuleiro[5] == VAZIO 
				&& tabuleiro[6] == VAZIO
				&& tabuleiro[7] == VAZIO 
				&& tabuleiro[8] == getOponente(jogador))
				|| (tabuleiro[0] == VAZIO 
					&& tabuleiro[1] == VAZIO 
					&& tabuleiro[2] == getOponente(jogador) 
					&& tabuleiro[3] == VAZIO
					&& tabuleiro[4] == jogador 
					&& tabuleiro[5] == VAZIO 
					&& tabuleiro[6] == getOponente(jogador)
					&& tabuleiro[7] == VAZIO 
					&& tabuleiro[8] == VAZIO);
	}

	/**
	 * Método que retorna uma possível jogada de ataque em "triangulo" para o jogador
	 * especificado. A jogada em "triangulo" é aquela em que o jogador tem duas
	 * possibilidades simultaneas de completar uma linha e ganhar o jogo.
	 */
	private int getJogadaAtaqueTriangulo(final char jogador) {
		if (tabuleiro[4] != getOponente(jogador) 
				|| tabuleiro[1] != VAZIO 
				|| tabuleiro[3] != VAZIO
				|| tabuleiro[5] != VAZIO 
				|| tabuleiro[7] != VAZIO) {
			return -1;
		}

		if (tabuleiro[0] == jogador 
				&& isVazioEntre(1, 2) 
				&& isVazioEntre(6, 8)) {
			return 8;
		}

		if (isVazioEntre(0, 1) 
				&& tabuleiro[2] == jogador 
				&& isVazioEntre(6, 8)) {
			return 6;
		}

		if (isVazioEntre(0, 2) 
				&& tabuleiro[6] == jogador 
				&& isVazioEntre(7, 8)) {
			return 2;
		}

		if (isVazioEntre(0, 2) 
				&& isVazioEntre(6, 7) 
				&& tabuleiro[8] == jogador) {
			return 0;
		}
		
		return -1;
	}

	/**
	 * Método que retorna uma possível jogada no canto para o jogador especificado.
	 */
	private int getJogadaCanto(final char jogador) {
		final List<Integer> jogadasPossiveis = new ArrayList<Integer>();

		if (tabuleiro[0] == VAZIO) {
			jogadasPossiveis.add(0);
		}
		
		if (tabuleiro[2] == VAZIO) {
			jogadasPossiveis.add(2);
		}
		
		if (tabuleiro[6] == VAZIO) {
			jogadasPossiveis.add(6);
		}
		
		if (tabuleiro[8] == VAZIO) {
			jogadasPossiveis.add(8);
		}

		return escolherMelhorJogada(jogador, jogadasPossiveis);
	}

	/**
	 * Método que retorna uma possível jogada na borda para o jogador especificado.
	 */
	private int getJogadaBorda(final char jogador) {
		final List<Integer> jogadasPossiveis = new ArrayList<Integer>();

		if (tabuleiro[1] == VAZIO) {
			jogadasPossiveis.add(1);
		}
		
		if (tabuleiro[3] == VAZIO) {
			jogadasPossiveis.add(3);
		}
		
		if (tabuleiro[5] == VAZIO) {
			jogadasPossiveis.add(5);
		}
		
		if (tabuleiro[7] == VAZIO) {
			jogadasPossiveis.add(7);
		}

		return escolherMelhorJogada(jogador, jogadasPossiveis);
	}

	/**
	 * Método que retorna uma lista com as melhores jogadas de ataque dentre um conjunto
	 * especificado de jogadas.
	 */
	private List<Integer> getMelhoresJogadas(final List<Integer> jogadasPossiveis, final char[] padrao) {

		final List<Integer> melhoresJogadas = new ArrayList<Integer>();

		if (padrao.length < 3) {
			return melhoresJogadas;
		}

		for (Integer jogada : jogadasPossiveis) {

			for (int posicao = 0; posicao < TRIOS_GANHADORES.length; posicao++) {

				for (int ponto1 = 0; ponto1 < 3; ponto1++) {

					if (TRIOS_GANHADORES[posicao][ponto1] == jogada) {

						int ponto2 = (ponto1 == 2) ? 0 : (ponto1 + 1);
						int ponto3 = (ponto2 == 2) ? 0 : (ponto2 + 1);

						if (tabuleiro[TRIOS_GANHADORES[posicao][ponto1]] == padrao[0] 
								&& tabuleiro[TRIOS_GANHADORES[posicao][ponto2]] == padrao[1]
								&& tabuleiro[TRIOS_GANHADORES[posicao][ponto3]] == padrao[2]) {

							melhoresJogadas.add(TRIOS_GANHADORES[posicao][ponto1]);
						}
					}
				}
			}
		}
		
		return melhoresJogadas;
	}

	/**
	 * Método que seleciona dentre um conjunto de jogadas de ataque qual é a melhor. Caso
	 * nao haja uma melhor, sorteia uma.
	 */
	private int escolherMelhorJogada(final char jogador, final List<Integer> jogadasPossiveis) {

		/**
		 *  Nenhuma jogada possível 
		 */
		if (jogadasPossiveis.size() < 1) {
			return -1;
		}

		/** 
		 * Só uma jogada possível 
		 */
		if (jogadasPossiveis.size() == 1) {
			return jogadasPossiveis.get(0);
		}

		/**
		 *  Mais de uma jogada possível. 
		 *  Verifica quais delas tem chance para formar um trio no futuro.
		 */
		List<Integer> boasJogadas = new ArrayList<Integer>();

		/**
		 *  Jogadas para formar uma dupla [2] 
		 */
		boasJogadas.addAll(getMelhoresJogadas(jogadasPossiveis, new char[] { VAZIO, VAZIO, jogador }));
		boasJogadas.addAll(getMelhoresJogadas(jogadasPossiveis, new char[] { VAZIO, jogador, VAZIO }));

		/**
		 *  Se há jogadas, sorteia uma 
		 */
		if (boasJogadas.size() > 0) {
			return boasJogadas.get(new Random().nextInt(boasJogadas.size()));
		}

		/**
		 *  Jogadas para iniciar um novo trio [1] 
		 */
		boasJogadas = getMelhoresJogadas(jogadasPossiveis, new char[] { VAZIO, VAZIO, VAZIO });

		/**
		 *  Se há jogadas, sorteia uma 
		 */
		if (boasJogadas.size() > 0) {
			return boasJogadas.get(new Random().nextInt(boasJogadas.size()));
		}

		/**
		 *  Se não há boas jogadas, sorteia qualquer uma das possíveis 
		 */
		return jogadasPossiveis.get(new Random().nextInt(jogadasPossiveis.size()));
	}

	/**
	 * Método que retorna a dificudade do jogo 
	 */
	public int getDificudade() {
		return dificudade;
	}
	
	/**
	 * Método que preenche a dificudade do jogo 
	 */
	public void setDificudade(int dificudade) {
		this.dificudade = dificudade;
	}
}
