package translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Translator translator = new JSONTranslator();

            // translation JList
            JPanel countryPanel = new JPanel();
            String[] items = new String[translator.getCountryCodes().size()];
            int i = 0;
            for(String countryCodes : translator.getCountryCodes()) {
                CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
                items[i++] = countryCodeConverter.fromCountryCode(countryCodes);
            }
            // create the JList with the array of strings and set it to allow multiple
            // items to be selected at once.
            JList<String> list = new JList<>(items);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            // place the JList in a scroll pane so that it is scrollable in the UI
            JScrollPane scrollPane = new JScrollPane(list);
            countryPanel.add(scrollPane);

            // language combobox
            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));

            // create combobox, add country codes into it, and add it to our panel
            JComboBox<String> languageComboBox = new JComboBox<>();
            // add language code
            for(String languageCode : translator.getLanguageCodes()) {
                LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
                languageComboBox.addItem(languageCodeConverter.fromLanguageCode(languageCode));
            }
            languagePanel.add(languageComboBox);

            //  button
            JPanel buttonPanel = new JPanel();
            JButton submit = new JButton("Submit");
            buttonPanel.add(submit);


            // result
            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            buttonPanel.add(resultLabel);


            // adding listener for when the user clicks the submit button
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get selected language from combo box
                    String selectedLanguage = (String) languageComboBox.getSelectedItem();
                    LanguageCodeConverter languageConverter = new LanguageCodeConverter();
                    String languageCode = languageConverter.fromLanguage(selectedLanguage);

                    // Get selected countries from JList
                    java.util.List<String> selectedCountries = list.getSelectedValuesList();
                    if (selectedCountries.isEmpty()) {
                        resultLabel.setText("Please select at least one country");
                        return;
                    }

                    // Translate for each selected country
                    CountryCodeConverter countryConverter = new CountryCodeConverter();
                    StringBuilder results = new StringBuilder();
                    for (String countryName : selectedCountries) {
                        String countryCode = countryConverter.fromCountry(countryName);

                        // convert to lower case
                        if (countryCode != null) {
                            countryCode = countryCode.toLowerCase();
                        }

                        // System.out.println("Debug - Country: " + countryName + " -> Code: " + countryCode);
                        // System.out.println("Debug - Language: " + selectedLanguage + " -> Code: " + languageCode);

                        String translation = translator.translate(countryCode, languageCode);

                        // System.out.println("Debug - Translation result: " + translation);
                        if (translation != null) {
                            results.append(translation);
                        }
                    }

                    if (results.length() > 0) {
                        resultLabel.setText(results.toString());
                    } else {
                        resultLabel.setText("no translation found!");
                    }

                }

            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);

            mainPanel.add(buttonPanel);

            mainPanel.add(countryPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }
}
