/**
* Developed by Johnny Trejos Barrios.
* Technology: Java.
* Version: Java Development Kit 11.0.4 LTS, Standard Edition.
* Development Environment: NVIM 0.6.1
* Date: 17/02/2022, Time: 18:02:00.
*
* Additional Info.
*
* Source Code Target Or Details:
*
*     []
*
* Licenses: GNU GPL v3.0, Eclipse Public License 2.0, personal for non-commercial purposes.
* Developer Contact: jtrejosb@live.com
* github.com/jtrejosb
*/
package io.jtrejosb.api.controller;

import java.util.List;

import io.jtrejosb.api.model.AppModel;
import io.jtrejosb.api.view.AppView;
import io.jtrejosb.api.view.PrintView;
import io.jtrejosb.api.view.QueryView;
import io.jtrejosb.api.view.TableView;

public class AppController {
  private AppModel APM;
  private AppView APV;
  private QueryView QV;
  private final int PRINT_OPTION=0;
  private final int TABLE_OPTION=1;

  public AppController(AppModel APM,AppView APV,QueryView QV) {
    this.APM=APM;
    this.APV=APV;
    this.QV=QV;
    this.APV.addFindListener(e->QV.setVisible(true));
    this.APV.addPrintAllListener(e->findAll(PRINT_OPTION));
    this.APV.addQueryAllListener(e->findAll(TABLE_OPTION));
    this.APV.addStoreListener(e->create());
    this.QV.addQueryListener(e->find());
  }

  private void find() {
    String ID=QV.getStudentID();
    if(!ID.isEmpty()) {
      String Q="SELECT * FROM students WHERE ID = ?";
      List<Object> datalist=APM.getData(Q,ID);
      if(datalist.size()>0)
        QV.applyData(datalist);
      else
        APV.showWarning("No students with ID '"+ID+"' were found",2);
    } else {
      APV.showWarning("Student ID is required",0);
    }
  }

  private void findAll(int option) {
    String Q="SELECT * FROM students";
    List<Object> datalist=APM.getData(Q,null);
    if(datalist.size()>0) {
      if(option==PRINT_OPTION) {
        PrintView PV=new PrintView();
        PV.applyData(datalist);
      } else {
        TableView TV=new TableView();
        TV.applyData(datalist);
      }
    }
  }

  private void create() {
    String name=APV.getStudentName();
    String id=APV.getStudentID();
    double g1=APV.getGrade1();
    double g2=APV.getGrade2();
    double g3=APV.getGrade3();
    if(!name.isEmpty()&&!id.isEmpty()&&(g1>=0||g1<=5)&&(g2>=0||g2<=5)&&(g3>=0||g3<=5)) {
      double avg=(g1+g2+g3)/3;
      APM.store(id,name,g1,g2,g3,avg);
      APV.updatePromLabel(avg);
      APV.clearGradesFields();
    } else  {
      APV.showWarning("Be sure that every field is filled correctly",0);
    }
  }
}
