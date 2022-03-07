package com.cei.single;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.TextField;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.collections.map.HashedMap;

public class UserInputManager {

	Map<String, Object> map = new HashedMap();

	public UserInputManager(List<ParamItemData> parms, String reportName) {
		JPanel pane = new JPanel(new GridBagLayout());

		Label title = new Label("Enter paramerts for report " + reportName);
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 1;
		c1.gridy = 0;
		c1.ipady = 50;
		pane.add(title, c1);

		for (int x = 0; x < parms.size(); x++) {

			ParamItemData pid = parms.get(x);
			Label lab = new Label(pid.paramPrompt);

			TextField tf = new TextField(pid.defaultValue);
			tf.addTextListener(t -> {
				pid.defaultValue = tf.getText();
			});

			GridBagConstraints labelConstraint = new GridBagConstraints();
			GridBagConstraints textFieldConstraint = new GridBagConstraints();
			// labelConstraint.fill=GridBagConstraints.CENTER;
			labelConstraint.gridx = 0;
			labelConstraint.gridy = x + 1;

			textFieldConstraint.fill = GridBagConstraints.BOTH;
			textFieldConstraint.gridx = 2;
			textFieldConstraint.gridy = x + 1;

			pane.add(lab, labelConstraint);
			pane.add(tf, textFieldConstraint);
			System.out.println("value of x: " + x);

		}

		int result = JOptionPane.showConfirmDialog(null, pane, "Report Parameters", JOptionPane.DEFAULT_OPTION);
		readParams(parms);

	}

	void readParams(List<ParamItemData> parms) {
		for (int x = 0; x < parms.size(); x++) {
			map.put(parms.get(x).paramName, parms.get(x).defaultValue);
		}
	}

	public Map<String, Object> getMap() {
		return map;
	}
}
