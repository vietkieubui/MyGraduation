/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Main.AddForm;

import Controller.Main.MainController;
import Model.SchoolYearModel;
import Services.Services;
import View.Main.AddForm.AddSchoolYearForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author BVKieu
 */
public final class AddSchoolYearController {

    AddSchoolYearForm addSchoolYearForm;
    DefaultTableModel table;

    public AddSchoolYearController(DefaultTableModel table) {
        this.table = table;
        addSchoolYearForm = new AddSchoolYearForm();
        addFormButtonController();
    }

    void addFormButtonController() {
        Services.addActionListener(addSchoolYearForm.addButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SchoolYearModel schoolYear = new SchoolYearModel(addSchoolYearForm.schoolYearText.getText());
                String[] columnsName = {"name"};
                String[] values = {Services.toSQLString(schoolYear.name)};
                try {
                    if (Services.insertIntoDatabase("SchoolYears", columnsName, values)) {
                        Services.showMess("Thêm thành công!");
                        Services.getSchoolYears(table);
                        addSchoolYearForm.dispose();
                    }

                } catch (Exception ex) {
                    Services.showMess(ex.toString());
                }
            }
        });
        Services.addActionListener(addSchoolYearForm.cancelButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSchoolYearForm.dispose();
            }
        });
    }

}
