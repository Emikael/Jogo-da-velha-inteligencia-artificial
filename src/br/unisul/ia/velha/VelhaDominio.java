package br.unisul.ia.velha;

/**
 * Classe que representa o objeto do jogo.
 */
public class VelhaDominio {

	/** 
	 * id do jogador
	 * 'X' ou 'O' 
	 */
	private char id;
	
	/** 
	 * Situacao do jogo. 
	 */
	private String situacao;

	/** 
	 * Tabuleiro do jogo da Velha com 9 posições. 
	 */
	private char[] tabuleiro = new char[9];

	/** 
	 * Construtor da classe. 
	 */
	public VelhaDominio() {
		limpar();
	}

	/** 
	 * Limpa todos campos do tabuleiro. 
	 */
	public void limpar() {
		id = VelhaIA.VAZIO;
		situacao = VelhaIA.STATUS_AGUARDE_SUA_VEZ;
		for (int pos = 0; pos < tabuleiro.length; pos++) {
			tabuleiro[pos] = VelhaIA.VAZIO;
		}
	}

	/**
	 * Método que retorna a posição no tabuleiro
	 */
	public char getPosicao(final int pos) {
		return tabuleiro[pos];
	}

	/**
	 * Método que preenche uma jogada no tabuleiro.
	 */
	public void setPosicao(final int pos, final char jogador) {
		tabuleiro[pos] = jogador;
	}

	/**
	 * Método que retorna o id do jogador.
	 */
	public char getId() {
		return id;
	}

	/**
	 * Método que preenche o id do jogador.
	 */
	public void setId(final char id) {
		this.id = id;
	}

	/**
	 * Método que retorna a situação da jogada.
	 */
	public String getSituacao() {
		return situacao;
	}

	/**
	 * Método que preenche a situação da jogada.
	 */
	public void setSituacao(final String status) {
		this.situacao = status;
	}

}
