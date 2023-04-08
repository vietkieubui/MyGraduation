/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Main.UpdateForm;

import Controller.Main.AddForm.*;
import Controller.Main.MainController;
import Model.SchoolYearModel;
import Services.Services;
import View.Main.AddForm.AddSchoolYearForm;
import View.Main.UpdateForm.UpdateSchoolYearForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author BVKieu
 */
public final class UpdateSchoolYearController {

    UpdateSchoolYearForm updateSchoolYearForm;
    DefaultTableModel table;
    SchoolYearModel schoolYearModel;

    public UpdateSchoolYearController(DefaultTableModel table, SchoolYearModel schoolYearModel) {
        this.table = table;
        this.schoolYearModel = schoolYearModel;
        updateSchoolYearForm = new UpdateSchoolYearForm();
        updateSchoolYearForm.idText.setText(schoolYearModel.id);
        updateSchoolYearForm.schoolYearText.setText(schoolYearModel.name);
        updateFormButtonController();
    }

    void updateFormButtonController() {
        Services.addActionListener(updateSchoolYearForm.updateButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SchoolYearModel schoolYear = new SchoolYearModel(updateSchoolYearForm.schoolYearText.getText());
                String[] columnsName = {"name"};
                String[] values = {Services.toSQLString(schoolYear.name)};
                try {
                    if (Services.updateData("SchoolYears", columnsName, values, "id="+Services.toSQLString(schoolYearModel.id))) {
                        Services.showMess("Cập nhật thành công!");
                        Services.getSchoolYears(table);
                        updateSchoolYearForm.dispose();
                    }

                } catch (Exception ex) {
                    Services.showMess(ex.toString());
                }
            }
        });
        Services.addActionListener(updateSchoolYearForm.cancelButton, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSchoolYearForm.dispose();
            }
        });
    }

}
