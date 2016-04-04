package br.unisul.ia.service;

import br.unisul.ia.ui.VelhaTela;
import br.unisul.ia.velha.VelhaDominio;
import br.unisul.ia.velha.VelhaIA;

/**
 * Classe que implementa os serviços chamados pela tela.
 */
public class VelhaServiceImpl implements VelhaService {

	/** 
	 * Lógica do jogo. 
	 */
	private VelhaIA velhaIA;

	/**
	 * Objeto Jogo da Velha. 
	 */
	private VelhaDominio velhaDominio;

	/** 
	 * Tela do Jogo da Velha. 
	 */
	private VelhaTela velhaTela;
	
	/**
	 *  Construtor da classe. 
	 */
	public VelhaServiceImpl() {
		velhaIA = new VelhaIA();
		velhaDominio = new VelhaDominio();
		velhaTela = new VelhaTela(velhaIA, this);
	}

	/**
	 * Método que executa a jogado do jogador.
	 */
	@Override
	public void onJogadorFezUmaJogada(final int posicao) {
		realizaJogada(posicao);
		velhaDominio.setSituacao(VelhaIA.STATUS_AGUARDE_SUA_VEZ);
		setInformacoesDoJogo();
	}

	/**
	 * Método que executa a jogada da maquina.
	 */
	@Override
	public void onVezDaMaquinaJogar() {
		final int posicao = velhaIA.getJogada();
		
		if (posicao < 0) {
			return;
		}

		velhaDominio.setSituacao(VelhaIA.STATUS_SUA_VEZ_JOGAR);
		realizaJogada(posicao);
		setInformacoesDoJogo();
	}

	/**
	 * Método que inicia a partida.
	 */
	@Override
	public void onIniciarPartida(final int dificudade) {
		velhaIA.setDificudade(getDificudade(dificudade));
		limparTela();
		VelhaTela.JOGO_INICIADO = true;
		velhaDominio.setSituacao(VelhaIA.STATUS_SUA_VEZ_JOGAR);
		setInformacoesDoJogo();
	}

	/**
	 * Método que retorna a dificudade selecionada na tela.
	 */
	private int getDificudade(final int dificudade) {
		if (dificudade == 0) {
			return 10;
		}
		
		if (dificudade == 1) {
			return 5;
		}
		
		return 0;
	}

	/**
	 * Método que realiza a jogada.
	 */
	private void realizaJogada(final int posicao) {
		if ((velhaIA.getPosicao(posicao) != VelhaIA.VAZIO) 
				|| velhaDominio.getSituacao().equals(VelhaIA.STATUS_AGUARDE_SUA_VEZ)
				|| (velhaIA.isFimDeJogo())) {
			return;
		}

		/*
		 * Coloca a jogada na tela do jogo.
		 */
		velhaDominio.setId(VelhaIA.JOGADOR_DA_VEZ);
		velhaIA.setPosicao(posicao, velhaDominio.getId());
	}

	/**
	 * Método que mostra a situação do jogo.
	 */
	private void setInformacoesDoJogo() {
		String situacao = "";

		if (VelhaIA.STATUS_AGUARDE_SUA_VEZ.equals(velhaDominio.getSituacao())) {
			situacao += "Minha vez de jogar...";
		}
		if (VelhaIA.STATUS_SUA_VEZ_JOGAR.equals(velhaDominio.getSituacao())) {
			situacao += "Sua vez de jogar...";
		}
		if (VelhaIA.STATUS_VOCE_GANHOU.equals(velhaDominio.getSituacao())) {
			situacao += "Parabéns! Você ganhou essa partida!";
		}
		if (VelhaIA.STATUS_VOCE_PERDEU.equals(velhaDominio.getSituacao())) {
			situacao += "Ganhei! Tente novamente...";
		}
		if (VelhaIA.STATUS_EMPATE.equals(velhaDominio.getSituacao())) {
			situacao += "Empatamos! Vamos jogar novamente...";
		}

		velhaTela.setSituacaoDoJogo(situacao);
	}

	/** 
	 * Limpa a tela do jogo. 
	 */
	private void limparTela() {
		velhaTela.setSituacaoDoJogo("Pronto para iniciar a partida..");
		velhaIA.limparTabuleiro();
		velhaDominio.limpar();
		velhaTela.redesenhaPainelDoJogo();
	}

	/**
	 * Método que verifica a situação do jogo.
	 */
	@Override
	public void onVerificaSituacaoDoJogo() {
		/**
		 * Verifica se houve um empate.
		 */
		if (velhaIA.isEmpate()) {
			velhaDominio.setSituacao(VelhaIA.STATUS_EMPATE);
			setInformacoesDoJogo();
			return;
		} 

		/**
		 * Verifica se o jogador humano ganhou.
		 */
		if (velhaIA.isGanhador(VelhaIA.JOGADOR_X)) {
			velhaDominio.setSituacao(VelhaIA.STATUS_VOCE_GANHOU);
			setInformacoesDoJogo();
			return;
		} 
		
		/**
		 * Verifica se o jogador humano perdeu.
		 */
		if (velhaIA.isGanhador(VelhaIA.JOGADOR_O)) {
			velhaDominio.setSituacao(VelhaIA.STATUS_VOCE_PERDEU);
			setInformacoesDoJogo();
			return;
		}
	}
}
