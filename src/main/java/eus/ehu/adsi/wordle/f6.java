package eus.ehu.adsi.wordle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

public class f6 extends JFrame {

	public static class Logro {
		private final String nombre;
		private final String descripcion;
		private final LocalDate fechaObtencion;
		private final List<String> usuarios;

		public Logro(String nombre, String descripcion, LocalDate fechaObtencion, List<String> usuarios) {
			this.nombre = nombre;
			this.descripcion = descripcion;
			this.fechaObtencion = fechaObtencion;
			this.usuarios = usuarios;
		}

		public String getNombre() { return nombre; }
		public String getDescripcion() { return descripcion; }
		public LocalDate getFechaObtencion() { return fechaObtencion; }
		public List<String> getUsuarios() { return usuarios; }
		public boolean isObtenido() { return fechaObtencion != null; }
	}

	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private static final int MAX_USUARIOS_INVITADO = 3;

	private final boolean invitado;

	private JPanel contentPane;
	private JPanel panelNorte;
	private JLabel lblTitle;
	private JLabel lblProgreso;
	private JTabbedPane tabbedPane;

	private DefaultTableModel obtenidos;
	private DefaultTableModel pendientes;
	private DefaultTableModel todos;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					f6 frame = new f6(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public f6(boolean invitado) {
		this.invitado = invitado;

		setTitle("Wordle");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 520);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(248, 249, 250));
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 15));

		contentPane.add(getPanelNorte(), BorderLayout.NORTH);
		contentPane.add(getTabbedPane(), BorderLayout.CENTER);

		// Cargar datos de prueba iniciales (sustituir por los datos reales)
		setLogros(datosDePrueba());
	}

	private JPanel getPanelNorte() {
		if (panelNorte == null) {
			panelNorte = new JPanel();
			panelNorte.setOpaque(false);
			panelNorte.setLayout(new BorderLayout(0, 4));
			panelNorte.setBorder(new EmptyBorder(10, 0, 10, 0));
			panelNorte.add(getLblTitle(), BorderLayout.CENTER);
			panelNorte.add(getLblProgreso(), BorderLayout.SOUTH);
		}
		return panelNorte;
	}

	private JLabel getLblTitle() {
		if (lblTitle == null) {
			lblTitle = new JLabel("Logros");
			lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
			lblTitle.setForeground(new Color(51, 51, 51));
			lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblTitle;
	}

	private JLabel getLblProgreso() {
		if (lblProgreso == null) {
			lblProgreso = new JLabel(" ");
			lblProgreso.setFont(new Font("SansSerif", Font.PLAIN, 13));
			lblProgreso.setForeground(new Color(110, 110, 110));
			lblProgreso.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblProgreso;
	}

	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));
			tabbedPane.setBackground(Color.WHITE);

			obtenidos = createTableModel(new String[] {"Logro", "Descripci n", "Fecha"});
			pendientes = createTableModel(new String[] {"Logro", "Descripci n"});
			todos = createTableModel(new String[] {"Logro", "Descripci n", "Conseguido por"});

			if (!invitado) {
				tabbedPane.addTab("Mis logros", createLogrosPanel(obtenidos, new int[] {150, 260, 100}, true));
				tabbedPane.addTab("Pendientes", createLogrosPanel(pendientes, new int[] {150, 360}, false));
			}
			tabbedPane.addTab("Todos los logros", createLogrosPanel(todos, new int[] {130, 220, 200}, false));
		}
		return tabbedPane;
	}

	private DefaultTableModel createTableModel(String[] columnNames) {
		return new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	}

	private JPanel createLogrosPanel(DefaultTableModel model, int[] anchos, boolean centrarUltima) {
		JPanel tabContent = new JPanel();
		tabContent.setBackground(Color.WHITE);
		tabContent.setLayout(new BorderLayout(0, 0));

		JTable table = new JTable(model);
		table.setRowHeight(32);
		table.setFont(new Font("SansSerif", Font.PLAIN, 13));
		table.setShowVerticalLines(false);
		table.setGridColor(new Color(230, 230, 230));

		table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		table.getTableHeader().setForeground(new Color(110, 110, 110));
		table.getTableHeader().setBackground(Color.WHITE);
		table.getTableHeader().setReorderingAllowed(false);

		for (int i = 0; i < anchos.length; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
		}

		if (centrarUltima) {
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			int ultima = anchos.length - 1;
			table.getColumnModel().getColumn(ultima).setCellRenderer(centerRenderer);
		}

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Color.WHITE);

		tabContent.add(scrollPane, BorderLayout.CENTER);
		return tabContent;
	}

	public void setLogros(List<Logro> logros) {
		obtenidos.setRowCount(0);
		pendientes.setRowCount(0);
		todos.setRowCount(0);

		int total = 0;
		int conseguidos = 0;
		for (Logro l : logros) {
			total++;
			todos.addRow(new Object[] {l.getNombre(), l.getDescripcion(), textoUsuarios(l.getUsuarios())});

			if (l.isObtenido()) {
				conseguidos++;
				obtenidos.addRow(new Object[] {
						l.getNombre(), l.getDescripcion(), l.getFechaObtencion().format(FORMATO_FECHA)});
			} else {
				pendientes.addRow(new Object[] {l.getNombre(), l.getDescripcion()});
			}
		}

		if (invitado) {
			getLblProgreso().setText("Inicia sesi n para ver tus logros");
		} else {
			getLblProgreso().setText(conseguidos + " de " + total + " logros conseguidos");
		}
	}

	private String textoUsuarios(List<String> usuarios) {
		if (usuarios.isEmpty()) {
			return "Nadie todav a";
		}
		int limite = invitado ? Math.min(MAX_USUARIOS_INVITADO, usuarios.size()) : usuarios.size();
		String texto = String.join(", ", usuarios.subList(0, limite));
		if (limite < usuarios.size()) {
			texto += " y " + (usuarios.size() - limite) + " m s";
		}
		return texto;
	}

	private List<Logro> datosDePrueba() {
		List<Logro> lista = new ArrayList<>();
		lista.add(new Logro("Primera victoria", "Gana tu primera partida", LocalDate.of(2026, 9, 14),
				List.of("ana", "luis", "marta", "pablo", "irene")));
		lista.add(new Logro("Primer intento", "Acierta la palabra en el primer intento", null,
				List.of("luis")));
		lista.add(new Logro("Racha de 3", "Gana 3 partidas consecutivas", LocalDate.of(2026, 9, 21),
				List.of("ana", "marta", "pablo")));
		lista.add(new Logro("Racha de 5", "Gana 5 partidas consecutivas", null,
				new ArrayList<String>()));
		lista.add(new Logro("Jugador habitual", "Juega 10 partidas", LocalDate.of(2026, 9, 28),
				List.of("ana", "luis", "marta", "pablo")));
		lista.add(new Logro("Veterano", "Juega 50 partidas", null,
				List.of("ana")));
		return lista;
	}
}