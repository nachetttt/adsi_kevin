package eus.ehu.adsi.wordle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class f5 extends JFrame {

	private JPanel contentPane;
	private JPanel panelNorte;
	private JLabel lblTitle;
	private JTabbedPane tabbedPane;

	// Modelos de datos para poder actualizar cada tabla
	private DefaultTableModel Victorias;
	private DefaultTableModel Racha;
	private DefaultTableModel numIntentos;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					f5 frame = new f5();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public f5() {
		setTitle("Wordle");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 460, 520);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(248, 249, 250));
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 15));

		// Cabecera arriba y pestañas en el centro
		contentPane.add(getPanelNorte(), BorderLayout.NORTH);
		contentPane.add(getTabbedPane(), BorderLayout.CENTER);

		// Cargar datos de prueba iniciales
		loadSampleData();
	}

	private JPanel getPanelNorte() {
		if (panelNorte == null) {
			panelNorte = new JPanel();
			panelNorte.setOpaque(false);
			panelNorte.setLayout(new BorderLayout(0, 0));
			panelNorte.setBorder(new EmptyBorder(10, 0, 10, 0));
			panelNorte.add(getLblTitle(), BorderLayout.CENTER);
		}
		return panelNorte;
	}

	private JLabel getLblTitle() {
		if (lblTitle == null) {
			// Título en dos líneas idéntico a la imagen de referencia
			lblTitle = new JLabel("Ranking");
			lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
			lblTitle.setForeground(new Color(51, 51, 51));
			lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblTitle;
	}

	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));
			tabbedPane.setBackground(Color.WHITE);

			// Modelos independientes para cada pestaña
			Victorias = createTableModel();
			Racha = createTableModel();
			numIntentos = createTableModel();

			// Pestañas con sus tablas correspondientes
			tabbedPane.addTab("Victorias", createRankingPanel(Victorias));
			tabbedPane.addTab("Racha", createRankingPanel(Racha));
			tabbedPane.addTab("numIntentos", createRankingPanel(numIntentos));
		}
		return tabbedPane;
	}

	private DefaultTableModel createTableModel() {
		String[] columnNames = {"Pos", "Usuario", "Avg"};
		return new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; 
			}
		};
	}

	// Panel con tabla y formato limpio acorde al diseño
	private JPanel createRankingPanel(DefaultTableModel model) {
		JPanel tabContent = new JPanel();
		tabContent.setBackground(Color.WHITE);
		tabContent.setLayout(new BorderLayout(0, 0));

		JTable table = new JTable(model);
		table.setRowHeight(32);
		table.setFont(new Font("SansSerif", Font.PLAIN, 13));
		table.setShowVerticalLines(false);
		table.setGridColor(new Color(230, 230, 230));

		// Estilo de la cabecera (Rank, User, Avg Guess)
		table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		table.getTableHeader().setForeground(new Color(110, 110, 110));
		table.getTableHeader().setBackground(Color.WHITE);
		table.getTableHeader().setReorderingAllowed(false);

		// Centrar el texto en Rank y en Avg Guess
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

		// Ancho relativo de las columnas
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Color.WHITE);

		tabContent.add(scrollPane, BorderLayout.CENTER);
		return tabContent;
	}

	// Datos de prueba para rellenar las tablas
	private void loadSampleData() {
		// Pestaña Last 7 Days
		Victorias.addRow(new Object[]{"1", "Jugador1", "3.2"});
		Victorias.addRow(new Object[]{"2", "Jugador2", "3.5"});
		Victorias.addRow(new Object[]{"3", "Jugador3", "4.0"});

		// Pestaña Last 30 Days
		Racha.addRow(new Object[]{"1", "Jugador2", "3.1"});
		Racha.addRow(new Object[]{"2", "Jugador1", "3.4"});

		// Pestaña All Time
		numIntentos.addRow(new Object[]{"1", "Jugador4", "2.9"});
		numIntentos.addRow(new Object[]{"2", "Jugador1", "3.3"});
	}
}