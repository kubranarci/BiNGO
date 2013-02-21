package bingo.internal.ui;

/* * Copyright (c) 2005 Flanders Interuniversitary Institute for Biotechnology (VIB)
 * *
 * * Authors : Steven Maere
 * *
 * * This program is free software; you can redistribute it and/or modify
 * * it under the terms of the GNU General Public License as published by
 * * the Free Software Foundation; either version 2 of the License, or
 * * (at your option) any later version.
 * *
 * * This program is distributed in the hope that it will be useful,
 * * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * * The software and documentation provided hereunder is on an "as is" basis,
 * * and the Flanders Interuniversitary Institute for Biotechnology
 * * has no obligations to provide maintenance, support,
 * * updates, enhancements or modifications.  In no event shall the
 * * Flanders Interuniversitary Institute for Biotechnology
 * * be liable to any party for direct, indirect, special,
 * * incidental or consequential damages, including lost profits, arising
 * * out of the use of this software and its documentation, even if
 * * the Flanders Interuniversitary Institute for Biotechnology
 * * has been advised of the possibility of such damage. See the
 * * GNU General Public License for more details.
 * *
 * * You should have received a copy of the GNU General Public License
 * * along with this program; if not, write to the Free Software
 * * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * *
 * * Authors: Steven Maere
 * * Date: Apr.20.2005
 * * Class that extends JPanel and implements ActionListener. Makes
 * * a panel with a drop-down box of ontology choices. Last option custom... opens FileChooser   
 **/


import javax.swing.*;

import bingo.internal.BingoAlgorithm;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;

/**
 * ***************************************************************
 * ChooseOntologyPanel.java:   Steven Maere (c) April 2005
 * -----------------------
 * <p/>
 * Class that extends JPanel and implements ActionListener. Makes
 * a panel with a drop-down box of ontology choices. Last option custom... opens FileChooser
 * ******************************************************************
 */

public class ChooseOntologyPanel extends JPanel implements ActionListener {


    /*--------------------------------------------------------------
     Fields.
    --------------------------------------------------------------*/
    private final String CUSTOM = BingoAlgorithm.CUSTOM;
    private final String NONE = BingoAlgorithm.NONE;

    /**
     * JComboBox with the possible choices.
     */
    public JComboBox choiceBox;
    /**
     * the selected file.
     */
    private File openFile = null;
    /**
     * parent window
     */
    private SettingsPanel settingsPanel;
    /**
     * default = true, custom = false
     */
    private boolean def = true;
    /**
     * bingo directory path
     */
    private String bingoDir;
    /**
     * bingo annotations directory path
     */
    
    private String specifiedOntology;
    
    //private File annotationFilePath;
    private String[] choiceArray;

    /*-----------------------------------------------------------------
     CONSTRUCTOR.
    -----------------------------------------------------------------*/

    /**
     * Constructor with a string argument that becomes part of the label of
     * the button.
     *
     * @param settingsPanel : parent window
     */
    public ChooseOntologyPanel(SettingsPanel settingsPanel, String bingoDir, String [] choiceArray, String choice_def) {
        super();
        this.bingoDir = bingoDir;
        this.settingsPanel = settingsPanel;
        this.choiceArray = choiceArray;

        //annotationFilePath = new File(bingoDir, "bingo");
        setOpaque(false);
        makeJComponents();
        // Layout with GridLayout.
        setLayout(new GridLayout(1, 0));
        add(choiceBox);

        //defaults

        HashSet<String> choiceSet = new HashSet<String>();
        for(String s:choiceArray){
            choiceSet.add(s);
        }
        if(choiceSet.contains(choice_def)){
            choiceBox.setSelectedItem(choice_def);
            this.settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
            specifiedOntology = (String) choiceBox.getSelectedItem() ;
            def = true;
        }
        else{
            choiceBox.removeActionListener(this);
            choiceBox.setEditable(true);
            choiceBox.setSelectedItem(choice_def);
            choiceBox.setEditable(false);
            specifiedOntology = BingoAlgorithm.CUSTOM ;
            def=false;
            if(choice_def.endsWith(".obo")){
                this.settingsPanel.getNamespacePanel().choiceBox.setEnabled(true);
            }
            else{
                this.settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
            }
            choiceBox.addActionListener(this);
        }
    }

    /*----------------------------------------------------------------
    PAINTCOMPONENT.
    ----------------------------------------------------------------*/

    /**
     * Paintcomponent, part where the drawing on the panel takes place.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    /*----------------------------------------------------------------
    METHODS.
    ----------------------------------------------------------------*/

    /**
     * Method that creates the JComponents.
     */
    public void makeJComponents() {

        // button.
        choiceBox = new JComboBox(choiceArray);
        choiceBox.setEditable(false);
        choiceBox.addActionListener(this);


    }

    /**
     * Method that returns the selected item.
     *
     * @return String selection.
     */
    public String getSelection() {
        return choiceBox.getSelectedItem().toString();
    }
    
    public String getSpecifiedOntology() {
        return specifiedOntology;
    }

    /**
     * Method that returns 1 if one of default choices was chosen, 0 if custom
     */
    public boolean getDefault() {
        return def;
    }

    /*----------------------------------------------------------------
    LISTENER-PART.
    ----------------------------------------------------------------*/

    /**
     * Method performed when button clicked.
     *
     * @param event event that triggers action, here clicking of the button.
     */
    public void actionPerformed(ActionEvent event) {

        if (choiceBox.getSelectedItem().equals(CUSTOM)) {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
            int returnVal = chooser.showOpenDialog(settingsPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                specifiedOntology = CUSTOM ;
                openFile = chooser.getSelectedFile();
                choiceBox.setEditable(true);
                choiceBox.setSelectedItem(openFile.toString());
                choiceBox.setEditable(false);
                def = false;
                if(((String) choiceBox.getSelectedItem()).endsWith(".obo")){
                    settingsPanel.getNamespacePanel().choiceBox.setEnabled(true);
                }
                else{
                    settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
                }
            }
            if (returnVal == JFileChooser.CANCEL_OPTION) {
                choiceBox.setSelectedItem(NONE);
                specifiedOntology = NONE ;
                def = true;
                settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
            }
        } else if (choiceBox.getSelectedItem().equals(NONE)) {
            specifiedOntology = NONE ;
            def = true;
            settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
        } else {
            specifiedOntology = (String) choiceBox.getSelectedItem() ;
            def = true;
            settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
        }
    }
}
