package br.unisul.ia.service;

/**
 * Classe de serviço, onde serão chamados os eventos da tela.
 */
public interface VelhaService {

	/**
	 * Método chamado quando o jogador faz uma jogoda. 
	 */
	void onJogadorFezUmaJogada(int posicao);

	/**
	 * Método chamado quando é a vez da maquina fazer uma jogada.
	 */
	void onVezDaMaquinaJogar();
	
	/**
	 * Método chamado ao iniciar uma partida.
	 */
	void onIniciarPartida(final int dificudade);

	/**
	 * Método que verifica a situação do jogo. 
	 */
	void onVerificaSituacaoDoJogo();

}
