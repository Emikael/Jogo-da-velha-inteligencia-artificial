package br.unisul.ia.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import br.unisul.ia.service.VelhaService;
import br.unisul.ia.service.VelhaServiceImpl;
import br.unisul.ia.velha.VelhaIA;

/**
 * Classe responsavel por montar a tela do jogo.
 */
public class VelhaTela implements ActionListener {

	private JFrame tela;

	/**
	 *  Painel onde ficará o jogo 
	 */
	private PainelJogoDaVelha painelJogoDaVelha;

	/**
	 *  Painel de Opções do jogo 
	 */
	private JPanel painelOpcoesDeJogo;
	private JButton botaoIniciarPartida;
	private JComboBox<String> comboDificudades;

	/**
	 *  Painel que mostra a situação do jogo 
	 */
	private JPanel painelDeInformacao;
	private JLabel situacaoDoJogo;

	/** 
	 * Serviço do jogo.
	 */
	private VelhaService velhaService;
	
	/**
	 * Mostra se o jogo está iniciado ou não.
	 */
	public static boolean JOGO_INICIADO;
	
	/**
	 * Construtor da classe.
	 */
	public VelhaTela(VelhaIA velharIA, VelhaService service) {

		velhaService = service;

		tela = new JFrame("Jogo da Velha IA Unisul");
		tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tela.setLayout(new BorderLayout(10, 10));
		tela.setResizable(false);
		
		criaPainelDeOpcoes();
		criaPainelDeInformacoes();

		painelJogoDaVelha = new PainelJogoDaVelha(velharIA, service);

		tela.add(painelOpcoesDeJogo, BorderLayout.NORTH);
		tela.add(painelJogoDaVelha, BorderLayout.CENTER);
		tela.add(painelDeInformacao, BorderLayout.SOUTH);

		tela.setSize(650, 550);
		tela.setLocationByPlatform(true);
		tela.setVisible(true);
		tela.setLocationRelativeTo(null);
	}

	/** 
	 * Redesenha o painel do jogo. 
	 */
	public void redesenhaPainelDoJogo() {
		painelJogoDaVelha.repaint();
	}

	/**
	 * Mostra a situação do jogo.
	 */
	public void setSituacaoDoJogo(String info) {
		situacaoDoJogo.setText(info);
	}

	/**
	 *  Método chamada quando um botão é chamado
	 */
	@Override
	public void actionPerformed(ActionEvent evento) {

		if (evento.getSource() == botaoIniciarPartida) {
			velhaService.onIniciarPartida(comboDificudades.getSelectedIndex());
		}
	}

	/** 
	 * Cria o painel de opções do jogo. 
	 */
	private void criaPainelDeOpcoes() {

		painelOpcoesDeJogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		painelOpcoesDeJogo.setBorder(new EtchedBorder());

		botaoIniciarPartida = new JButton("Iniciar partida");
		botaoIniciarPartida.addActionListener(this);
		painelOpcoesDeJogo.add(botaoIniciarPartida);
		
		final String[] dificudades = {"FÁCIL", "MÉDIO", "DIFÍCIL"};
		comboDificudades = new JComboBox<String>(dificudades);
		comboDificudades.setSelectedIndex(0);
		comboDificudades.addActionListener(this);
		painelOpcoesDeJogo.add(comboDificudades);
		
	}

	/** 
	 * Cria o painel de infomações do jogo. 
	 */
	private void criaPainelDeInformacoes() {

		painelDeInformacao = new JPanel(new FlowLayout());
		painelDeInformacao.setBorder(new EtchedBorder());
		situacaoDoJogo = new JLabel("Pronto para iniciar a partida..");
		situacaoDoJogo.setFont(new Font(null, Font.BOLD, 14));
		painelDeInformacao.add(situacaoDoJogo);
	}
	
	/**
	 * Método que inicia a aplicação.
	 */
	public static void main(String[] args) {
		new VelhaServiceImpl();
	}
}
