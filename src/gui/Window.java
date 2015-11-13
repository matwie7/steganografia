package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.iharder.dnd.FileDrop;
import processing.Checkers;
import processing.Common;
import processing.Configuration;
import processing.Decoder;
import processing.Encoder;
import processing.FileReaderWriter;

public class Window implements ChangeListener {
	private JFrame frame;
	private JPanel mainPanel = new JPanel();
	private JPanel chartPanel = new JPanel();
	private JLabel infoLabel = new JLabel("drop any image file here");
	private JLabel inputFilePathLabel = new JLabel();
	private JLabel inputFileSizeLabel = new JLabel();
	private JLabel freeSpaceBitmapLabel = new JLabel();
	private JLabel leftFreeSpace = new JLabel();
	private JButton btnEncode = new JButton("Encode");
	private JButton btnDecode = new JButton("Decode");
	private JButton btnShowOriginalOrEncodedImage = new JButton("Show encoded image");
	private BufferedImage originalImage = null;
	private BufferedImage encodedImage = null;
	private boolean drawOriginalImage = true;
	private List<JSlider> sliders = new ArrayList<>(Arrays.asList(new JSlider(), new JSlider(), new JSlider()));
	private List<JLabel> labels = new ArrayList<>(Arrays.asList(new JLabel("Bits encoded on each px component:   R: "),
			new JLabel("        G: "), new JLabel("        B: ")));
	private Configuration configuration;

	public Window() {
		configuration = Configuration.create().setRBitsAmount((byte) 1).setGBitsAmount((byte) 1)
				.setBBitsAmount((byte) 1);

		frame = new JFrame("Steganografia");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		for (int i = 0; i < 3; i++) {
			sliders.get(i).setMinimum(0);
			sliders.get(i).setMaximum(8);
			sliders.get(i).setValue(1);
			sliders.get(i).setMajorTickSpacing(1);
			sliders.get(i).setPaintTicks(true);
			sliders.get(i).setPaintLabels(true);
			sliders.get(i).addChangeListener(this);

			bottomPanel.add(labels.get(i));
			bottomPanel.add(sliders.get(i));
		}
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		mainPanel.add(infoLabel);
		mainPanel.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

		new FileDrop(frame, new FileDrop.Listener() {
			public void filesDropped(File[] files) {
				originalImage = FileReaderWriter.openBitmapFromFile(files[0]);
				encodedImage = null;
				configuration.setImageFilePath(files[0].getPath());
				freeSpaceBitmapLabel.setText(MessageFormat.format("   {0} bytes",
						Checkers.getTotalFreeBytesInBitmap(originalImage, configuration)));
				try {
					Decoder.decode(configuration, false);
				} catch (Exception e) {
					configuration.setIsVerificationBitCorrect((byte) 0);
				}
				refreshGui();
			}
		});

		mainPanel.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				if (drawOriginalImage)
					drawOriginalImage();
				else
					drawEncodecImage();
			}
		});

		JPanel leftConfigPanel = new JPanel();
		leftConfigPanel.setLayout(new GridLayout(20, 1));

		frame.getContentPane().add(leftConfigPanel, BorderLayout.WEST);

		JLabel fileToEncodeHeader = new JLabel("Steganpgraphy software 2015");
		leftConfigPanel.add(fileToEncodeHeader);
		leftConfigPanel.add(new JLabel(" Input file path:"));
		inputFilePathLabel = new JLabel("   Drop any file here.");
		leftConfigPanel.add(inputFilePathLabel);
		leftConfigPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		leftConfigPanel.add(new JLabel(" Input file size: "));
		leftConfigPanel.add(inputFileSizeLabel);
		leftConfigPanel.add(new JLabel(" Free space in bitmap: "));
		leftConfigPanel.add(freeSpaceBitmapLabel);
		leftConfigPanel.add(new JLabel(" Free space left after encoding:"));
		leftConfigPanel.add(leftFreeSpace);
		leftConfigPanel.add(chartPanel);
		leftConfigPanel.add(btnShowOriginalOrEncodedImage);
		leftConfigPanel.add(btnEncode);
		leftConfigPanel.add(btnDecode);

		btnEncode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				encodedImage = Encoder.encode(configuration);
				refreshGui();
			}
		});

		btnDecode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Decoder.decode(configuration, true);
				refreshGui();
			}
		});

		btnShowOriginalOrEncodedImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawOriginalImage = !drawOriginalImage;
				refreshGui();
			}
		});

		new FileDrop(leftConfigPanel, new FileDrop.Listener() {
			public void filesDropped(File[] files) {
				String path = files[0].getPath();
				configuration.setInputFilePath(path);
				String printedPath;
				if (path.length() < 40)
					printedPath = path;
				else
					printedPath = path.substring(path.length() - 30, path.length());

				inputFilePathLabel.setText(MessageFormat.format("   {0}", printedPath));
				inputFileSizeLabel
						.setText(MessageFormat.format("   {0} bytes", Checkers.getSizeOfInputFileInBytes(path)));
				drawChart();
			}
		});

		frame.pack();
		frame.setVisible(true);
		frame.setSize(1000, 600);
		frame.setMinimumSize(new Dimension(960, 500));
		refreshGui();
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		configuration.setRBitsAmount((byte) sliders.get(0).getValue()).setGBitsAmount((byte) sliders.get(1).getValue())
				.setBBitsAmount((byte) sliders.get(2).getValue());
		if (originalImage != null)
			freeSpaceBitmapLabel.setText(MessageFormat.format("   {0} bytes",
					Checkers.getTotalFreeBytesInBitmap(originalImage, configuration)));
		drawChart();
	}

	private void drawChart() {
		if (originalImage == null || configuration.getInputFilePath() == null
				|| configuration.getInputFilePath().equals(""))
			return;
		int width = (int) (chartPanel.getWidth()
				* ((float) Checkers.getSizeOfInputFileInBytes(configuration.getInputFilePath())
						/ Checkers.getTotalFreeBytesInBitmap(originalImage, configuration)));
		Graphics2D chartPanelGraphics = (Graphics2D) chartPanel.getGraphics();
		chartPanelGraphics.fillRect(0, 0, width, chartPanel.getHeight());
		chartPanelGraphics.setColor(Color.RED);
		chartPanelGraphics.fillRect(width + 1, 0, chartPanel.getWidth() - width, chartPanel.getHeight());
		leftFreeSpace.setText(MessageFormat.format("   {0} bytes", Checkers.getTotalFreeBytesInBitmap(originalImage, configuration) - Checkers.getSizeOfInputFileInBytes(configuration.getInputFilePath())));
	}

	private void drawOriginalImage() {
		drawImage(originalImage);
	}

	private void drawEncodecImage() {
		drawImage(encodedImage);
	}

	private void drawImage(BufferedImage bufferedImage) {
		if (bufferedImage == null)
			return;
		Dimension scaledImageDimension = Common.getScaledDimension(
				new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()),
				new Dimension(mainPanel.getWidth(), mainPanel.getHeight()));
		((Graphics2D) (mainPanel.getGraphics())).drawImage(bufferedImage, 0, 0, scaledImageDimension.width,
				scaledImageDimension.height, null);
	}

	private void refreshGui() {
		if (encodedImage == null) {
			drawOriginalImage = true;
			btnShowOriginalOrEncodedImage.setEnabled(false);
		} else {
			btnShowOriginalOrEncodedImage.setEnabled(true);
		}
		if (originalImage != null && Checkers.isDataEncoded(configuration)) {
			btnDecode.setEnabled(true);
		} else {
			btnDecode.setEnabled(false);
		}

		if (originalImage == null) {
			btnEncode.setEnabled(false);
		} else {
			btnEncode.setEnabled(true);
		}

		if (drawOriginalImage) {
			drawOriginalImage();
			btnShowOriginalOrEncodedImage.setText("Show encoded image");
		} else {
			drawEncodecImage();
			btnShowOriginalOrEncodedImage.setText("Show original image");
		}
		drawChart();
	}
}
