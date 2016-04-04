package br.unisul.ia.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.unisul.ia.service.VelhaService;
import br.unisul.ia.velha.VelhaIA;

/**
 * Classe que implementa um painel Swing com o tabuleiro do Jogo da Velha.
 */
public class PainelJogoDaVelha extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 *  Largura do tabuleiro. 
	 */
	private static final int LARGURA = 400;

	/**
	 *  Altura do tabuleiro. 
	 */
	private static final int ALTURA = LARGURA;

	/**
	 *  Largura de uma posição do tabuleiro 
	 */
	private static final int X_3 = LARGURA / 3;

	/**
	 *  Altura de uma posicao do tabuleiro 
	 */
	private static final int Y_3 = ALTURA / 3;

	/**
	 *  Largura da linha que desenha o tabuleiro, em pixels 
	 */
	private static final float LARGURA_DA_LINHA_TABULEIRO = 4.0f;

	/**
	 *  Cor do tabuleiro 
	 */
	private static final Paint COR_TABULEIRO = Color.BLACK;

	/**
	 *  Largura da linha que marca a jogada vencedora. 
	 */
	private static final float LARGURA_LINHA_VENCEDORA = 10.0f;

	/**
	 *  Cor da linha que marca a jogada vencedora.
	 */
	private static final Paint COR_LINHA_VENCEDORA = Color.BLACK;

	/**
	 *  Largura da linha que desenha o X ou O de uma jogada 
	 */
	private static final float LARGURA_LINHA_JOGADOR = 8.0f;

	/**
	 *  Cor do jogador X 
	 */
	private static final Paint COR_JOGADOR_X = Color.RED;

	/**
	 *  Cor do jogador O 
	 */
	private static final Paint COR_JOGADOR_O = Color.BLUE;

	/**
	 *  Espaços entre a borda da posicao do tabuleiro e o X ou O 
	 */
	private static final int ESPACO_JOGADOR = LARGURA / 25;

	/**
	 *  Coordenada X do inicio do tabuleiro 
	 */
	private int X_INICIO = 0;

	/**
	 *  Coordenada Y do inicio do tabuleiro 
	 */
	private int Y_INICIO = 0;

	/**
	 *  Coordenada X do fim do tabuleiro 
	 */
	private int X_FIM = X_INICIO + LARGURA;

	/**
	 *  Coordenada Y do fim do tabuleiro 
	 */
	private int Y_FIM = Y_INICIO + ALTURA;

	/**
	 * Armazena um conjunto de coordenadas que representam cada posicao no
	 * tabuleiro.
	 */
	private Point posicoesNoTabuleiro[][] = new Point[9][2];

	/** 
	 * Lógica do jogo. 
	 */
	private VelhaIA velhaIA = null;

	/** 
	 * Serviço do jogo. 
	 */
	private VelhaService velhaService = null;

	/**
	 * Construtor da classe.
	 */
	public PainelJogoDaVelha(VelhaIA velhaIA, VelhaService service) {

		this.velhaIA = velhaIA;
		this.velhaService = service;

		calcularCoordenadasDasPosicoes();

		addMouseListener(new MouseAdapter() {
			
			/**
			 * Evento chamada quando é clicado na tela do jogo.
			 */
			public void mousePressed(MouseEvent evento) {
				if (velhaIA.isFimDeJogo() || !VelhaTela.JOGO_INICIADO) {
					return;
				}
				
				int posicao = getPosicao(evento.getX(), evento.getY());
				if (posicao >= 0) {
					velhaService.onJogadorFezUmaJogada(posicao);
					repaint();

					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							velhaService.onVezDaMaquinaJogar();
							repaint();
							velhaService.onVerificaSituacaoDoJogo();
						}
					});
				}

			}

		});
	}

	/**
	 *  Resenha a tela do jogo
	 */
	@Override
	public void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2D = (Graphics2D) graphics;

		calcularCoordenadasDasPosicoes();

		desenharTabuleiro(graphics2D);
		desenharJogadas(graphics2D);
		desenharJogadaVencedora(graphics2D);
	}

	/**
	 * Calcula as coordenadas das posições do tabuleiro.
	 */
	private void calcularCoordenadasDasPosicoes() {
		X_INICIO = (getSize().width - LARGURA) / 2;
		Y_INICIO = (getSize().height - ALTURA) / 2;
		X_FIM = X_INICIO + LARGURA;
		Y_FIM = Y_INICIO + ALTURA;

		posicoesNoTabuleiro[0][0] = new Point(X_INICIO, Y_INICIO);
		posicoesNoTabuleiro[0][1] = new Point(X_INICIO + X_3, Y_INICIO + Y_3);

		posicoesNoTabuleiro[1][0] = new Point(X_INICIO + X_3, Y_INICIO);
		posicoesNoTabuleiro[1][1] = new Point(X_FIM - X_3, Y_INICIO + Y_3);

		posicoesNoTabuleiro[2][0] = new Point(X_FIM - X_3, Y_INICIO);
		posicoesNoTabuleiro[2][1] = new Point(X_FIM, Y_INICIO + Y_3);

		posicoesNoTabuleiro[3][0] = new Point(X_INICIO, Y_INICIO + Y_3);
		posicoesNoTabuleiro[3][1] = new Point(X_INICIO + X_3, Y_FIM - Y_3);

		posicoesNoTabuleiro[4][0] = new Point(X_INICIO + X_3, Y_INICIO + Y_3);
		posicoesNoTabuleiro[4][1] = new Point(X_FIM - X_3, Y_FIM - Y_3);

		posicoesNoTabuleiro[5][0] = new Point(X_FIM - X_3, Y_INICIO + Y_3);
		posicoesNoTabuleiro[5][1] = new Point(X_FIM, Y_FIM - Y_3);

		posicoesNoTabuleiro[6][0] = new Point(X_INICIO, Y_FIM - Y_3);
		posicoesNoTabuleiro[6][1] = new Point(X_INICIO + X_3, Y_FIM);

		posicoesNoTabuleiro[7][0] = new Point(X_INICIO + X_3, Y_FIM - Y_3);
		posicoesNoTabuleiro[7][1] = new Point(X_FIM - X_3, Y_FIM);

		posicoesNoTabuleiro[8][0] = new Point(X_FIM - X_3, Y_FIM - Y_3);
		posicoesNoTabuleiro[8][1] = new Point(X_FIM, Y_FIM);
	}

	/**
	 * Método que retorna uma posicao no tabuleiro, a partir das coordenadas de tela (x,y).
	 */
	private int getPosicao(final int posicaoX, final int posicaoY) {
		
		for (int posicao = 0; posicao < posicoesNoTabuleiro.length; posicao++) {

			final Point posicaoInicial = posicoesNoTabuleiro[posicao][0];
			final Point posicaoFinal = posicoesNoTabuleiro[posicao][1];

			if (posicaoX > posicaoInicial.x 
					&& posicaoX < posicaoFinal.x 
					&& posicaoY > posicaoInicial.y 
					&& posicaoY < posicaoFinal.y) {
				return posicao;
			}
		}
		
		return -1;
	}

	/**
	 * Desenha o tabuleiro na tela.
	 */
	private void desenharTabuleiro(final Graphics2D graphics2D) {

		graphics2D.setStroke(new BasicStroke(LARGURA_DA_LINHA_TABULEIRO));
		graphics2D.setPaint(COR_TABULEIRO);

		graphics2D.drawLine(X_INICIO, Y_INICIO + Y_3, X_FIM, Y_INICIO + Y_3);
		graphics2D.drawLine(X_INICIO, Y_FIM - Y_3, X_FIM, Y_FIM - Y_3);

		graphics2D.drawLine(X_INICIO + X_3, Y_INICIO, X_INICIO + X_3, Y_FIM);
		graphics2D.drawLine(X_FIM - X_3, Y_INICIO, X_FIM - X_3, Y_FIM);
	}

	/**
	 * Desenha as jogadas na tela.
	 */
	private void desenharJogadas(final Graphics2D graphics2D) {

		graphics2D.setStroke(new BasicStroke(LARGURA_LINHA_JOGADOR));

		for (int posicao = 0; posicao < 9; posicao++) {

			final Point posicaoInicial = posicoesNoTabuleiro[posicao][0];
			final Point posicaoFinal = posicoesNoTabuleiro[posicao][1];

			final int posicaoX1 = posicaoInicial.x + ESPACO_JOGADOR;
			final int posicaoX2 = posicaoFinal.x - ESPACO_JOGADOR;
			final int posicaoY1 = posicaoInicial.y + ESPACO_JOGADOR;
			final int posicaoY2 = posicaoFinal.y - ESPACO_JOGADOR;

			if (velhaIA.getPosicao(posicao) == VelhaIA.JOGADOR_X) {

				/**
				 * Desenha o 'X' na tela.  
				 */
				graphics2D.setPaint(COR_JOGADOR_X);
				graphics2D.drawLine(posicaoX1, posicaoY1, posicaoX2, posicaoY2);
				graphics2D.drawLine(posicaoX1, posicaoY2, posicaoX2, posicaoY1);

			} else if (velhaIA.getPosicao(posicao) == VelhaIA.JOGADOR_O) {

				/**
				 * Desenha o 'O' na tela.  
				 */
				graphics2D.setPaint(COR_JOGADOR_O);
				graphics2D.drawOval(posicaoX1, posicaoY1, posicaoX2 - posicaoX1, posicaoY2 - posicaoY1);

			}
		}
	}

	/**
	 * Método que desenha uma linha sobre a jogada vencedora.
	 */
	private void desenharJogadaVencedora(final Graphics2D grphics2D) {

		int jogadaVencedora[] = velhaIA.getPosicoesGanhadoras();

		if (jogadaVencedora == null || jogadaVencedora.length < 3) {
			return;
		}

		final int spacing = X_3 / 2;

		final int posicaoX1 = posicoesNoTabuleiro[jogadaVencedora[0]][0].x + spacing;
		final int posicaoY1 = posicoesNoTabuleiro[jogadaVencedora[0]][0].y + spacing;
		final int posicaoX2 = posicoesNoTabuleiro[jogadaVencedora[2]][0].x + spacing;
		final int posicaoY2 = posicoesNoTabuleiro[jogadaVencedora[2]][0].y + spacing;

		grphics2D.setStroke(new BasicStroke(LARGURA_LINHA_VENCEDORA));
		grphics2D.setPaint(COR_LINHA_VENCEDORA);

		grphics2D.drawLine(posicaoX1, posicaoY1, posicaoX2, posicaoY2);
	}

}
