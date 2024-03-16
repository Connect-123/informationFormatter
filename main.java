import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class main {

	public static void main(String[] args) {
		// Create frame
		JFrame frame = new JFrame("Information Formatter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create panel with GridBagLayout
		JPanel panel = new JPanel(new GridBagLayout());
		frame.getContentPane().add(panel, BorderLayout.NORTH); // Add panel to frame

		placeComponents(panel);

		// Pack frame (size components to preferred sizes)
		frame.pack();
		frame.setVisible(true); // Set frame visibility
	}

	private static void placeComponents(JPanel panel) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);

		// Add the name label and text field
		constraints.gridx = 0;
		constraints.gridy = 0;
		JLabel nameLabel = new JLabel("Name:");
		panel.add(nameLabel, constraints);

		constraints.gridx = 1;
		JTextField nameTextField = new JTextField(20);
		panel.add(nameTextField, constraints);

		// Add the bed info directly under the name field
		constraints.gridx = 0;
		constraints.gridy = 1; // Positioning bed info under the name field
		JLabel bedLabel = new JLabel("Bed Info:");
		panel.add(bedLabel, constraints);

		constraints.gridx = 1;
		JPanel bedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JRadioButton kingBedFirstFloor = new JRadioButton("1 King Bed (1st floor)");
		JRadioButton kingBedSecondFloor = new JRadioButton("1 King Bed (2nd floor)");
		JRadioButton queenBedFirstFloor = new JRadioButton("2 Queen Beds (1st floor)");
		JRadioButton queenBedSecondFloor = new JRadioButton("2 Queen Beds (2nd floor)");

		ButtonGroup group = new ButtonGroup();
		group.add(kingBedFirstFloor);
		group.add(kingBedSecondFloor);
		group.add(queenBedFirstFloor);
		group.add(queenBedSecondFloor);

		bedPanel.add(kingBedFirstFloor);
		bedPanel.add(kingBedSecondFloor);
		bedPanel.add(queenBedFirstFloor);
		bedPanel.add(queenBedSecondFloor);
		panel.add(bedPanel, constraints);

		// Define the rest of the labels excluding Name
		String[] labels = { "Nights", "Adults", "Company", "Rate", "Contact Number", "Additional Contact",
				"Card Number", "Card Expiry" };
		int numLabels = labels.length;

		// Create text fields for each label
		JTextField[] textFields = new JTextField[numLabels];
		for (int i = 0; i < numLabels; i++) {
			constraints.gridx = 0;
			constraints.gridy = i + 2; // Starts from 2 because 0 and 1 are used for Name and Bed Info
			JLabel label = new JLabel(labels[i] + ":");
			panel.add(label, constraints);

			constraints.gridx = 1;
			textFields[i] = new JTextField(20);
			panel.add(textFields[i], constraints);
		}

		// TextArea for displaying formatted information
		constraints.gridx = 0;
		constraints.gridy = numLabels + 2; // Shifted down by one
		constraints.gridwidth = 2;
		JTextArea textArea = new JTextArea(5, 50);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		panel.add(scrollPane, constraints);

		// Button for formatting information
		constraints.gridy = numLabels + 3; // Shifted down by one
		constraints.gridwidth = 1;
		JButton formatButton = new JButton("Format Information");
		panel.add(formatButton, constraints);

		// Button for copying information to clipboard
		constraints.gridx = 1;
		JButton copyButton = new JButton("Copy to Clipboard");
		panel.add(copyButton, constraints);

		// Action listener for format button
		formatButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String bedInfo = "No selection"; // Default if no selection
				if (kingBedFirstFloor.isSelected()) {
					bedInfo = "1 King Bed (1st floor)";
				} else if (kingBedSecondFloor.isSelected()) {
					bedInfo = "1 King Bed (2nd floor)";
				} else if (queenBedFirstFloor.isSelected()) {
					bedInfo = "2 Queen Beds (1st floor)";
				} else if (queenBedSecondFloor.isSelected()) {
					bedInfo = "2 Queen Beds (2nd floor)";
				}

				// Get text from JTextFields and format the information
				String formattedInformation = formatInformation(nameTextField.getText(), // Name
						textFields[0].getText(), // Nights
						textFields[1].getText(), // Adults
						bedInfo, // Bed Info from selected radio button
						textFields[2].getText(), // Company
						textFields[3].getText(), // Rate
						textFields[4].getText(), // Contact Number
						textFields[5].getText(), // Additional Contact
						textFields[6].getText(), // Card Number
						textFields[7].getText() // Card Expiry
				);

				// Display formatted information in the text area
				textArea.setText(formattedInformation);
			}
		});

		// Action listener for copy button
		copyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String content = textArea.getText();
				StringSelection stringSelection = new StringSelection(content);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);

				// Clear JTextFields and JTextArea after copying
				nameTextField.setText(""); // Clear the name text field
				for (JTextField textField : textFields) {
					textField.setText("");
				}
				textArea.setText("");
			}
		});
	}

	private static String formatInformation(String name, String nights, String adults, String bedInfo, String company,
			String rate, String contactNumber, String additionalContact, String cardNumber, String cardExpiry) {
		// Format the information. This method will build the formatted string
		// using the input data

		String formattedName = formatName(name);
		String formattedStay = nights + " Night | " + adults + " Adult | " + bedInfo;
		String formattedCompany = company + " | Rate: $ " + rate;
		String formattedContact = "Contact: " + contactNumber + " | " + additionalContact;
		String formattedCard = "Card: " + cardNumber + " (" + cardExpiry.trim() + ")"; // trim() removes any leading or
																						// trailing spaces

		return formattedName + " | " + formattedStay + "\n" + formattedCompany + "\n" + formattedContact + "\n"
				+ formattedCard;
	}

	private static String formatName(String name) {
		// Assuming the name is in the format "FIRSTNAME LASTNAME"
		String[] parts = name.split(" ");
		if (parts.length != 2) {
			return name; // Return the original name if it doesn't have two parts
		}
		return parts[1] + ", " + parts[0]; // Reformat to "LASTNAME, FIRSTNAME"
	}
}
