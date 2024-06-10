package gui;

import java.awt.Image;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.MySQL;

public class AdminHome extends javax.swing.JFrame {

    /**
     * Creates new form AdminHome
     */
    public AdminHome(HashMap<String, String> loginMap) {
        initComponents();
        ImageScaling();
        setData(loginMap);
        view("manageStudents");
        loadSubjectsComboBox(false, 0);
        loadTeachersComboBox(false, 0);
        loadStudentsComboBox(false, 0);
        loadTimeSlotsComboBox(false, 0);
    }

    private HashMap verifyStudent() throws Exception {

        String result = null;

        HashMap<String, String> registerMap = new HashMap();

        String fname = jTextField1.getText();
        String lname = jTextField2.getText();
        String email = jTextField3.getText();
        String mobile = jTextField4.getText();
        String password1 = String.valueOf(jPasswordField1.getPassword());
        ButtonModel genderSelection = buttonGroup1.getSelection();
        Date date = jDateChooser1.getDate();
        String address = jTextField5.getText();

        if (fname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter first name", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField1.requestFocus();
        } else if (lname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter last name", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField2.requestFocus();
        } else if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter email", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField3.requestFocus();
        } else if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter mobile", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField4.requestFocus();
        } else if (mobile.length() < 10 || mobile.length() > 10) {
            JOptionPane.showMessageDialog(this, "Mobile Number should contain 10 numbers", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField4.requestFocus();
        } else if (password1.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter password", "Warning", JOptionPane.ERROR_MESSAGE);
            jPasswordField1.requestFocus();
        } else if (password1.length() < 6 || password1.length() > 40) {
            JOptionPane.showMessageDialog(this, "Password should contain 6 to 40 characters", "Warning", JOptionPane.ERROR_MESSAGE);
            jPasswordField1.requestFocus();
        } else if (genderSelection == null) {
            JOptionPane.showMessageDialog(this, "Select the gender", "Warning", JOptionPane.ERROR_MESSAGE);
            jRadioButton1.requestFocus();
        } else if (date == null) {
            JOptionPane.showMessageDialog(this, "Select the Date of Birth", "Warning", JOptionPane.ERROR_MESSAGE);
            jDateChooser1.requestFocus();
        } else if (address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Home Address", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField5.requestFocus();
        } else {

            String gender = genderSelection.getActionCommand();

            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            String dob = formatDate.format(date);

            registerMap.put("firstName", fname);
            registerMap.put("lastName", lname);
            registerMap.put("email", email);
            registerMap.put("mobile", mobile);
            registerMap.put("password", password1);
            registerMap.put("genderId", gender);
            registerMap.put("dob", dob);
            registerMap.put("address", address);

            result = "Success";
            registerMap.put("verifyResult", result);
        }

        registerMap.put("verifyResult", result);
        return registerMap;

    }

    private void refreshStudentTable() {
        loadStudents(false, false, "");

        jTable1.setEnabled(true);
        jTextField7.setEnabled(true);
        jButton11.setEnabled(true);
        jButton8.setEnabled(true);

        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField7.setText("");
        jPasswordField1.setText("");

        jDateChooser1.setDate(new Date());
        buttonGroup1.clearSelection();

        jButton9.setEnabled(false);
        jButton10.setEnabled(false);
    }

    private ResultSet loadStudents(Boolean isSearch, Boolean isLoad, String sno) {

        try {

            String query = "SELECT * FROM `student` INNER JOIN `gender` ON `student`.`g_gender_id`=`gender`.`gender_id`";

            if (isSearch) {

                query += " WHERE `sno` LIKE '%" + sno + "%' ORDER BY `sno` ASC";

            } else {

                query += " ORDER BY `sno` ASC";

            }

            ResultSet resultSet = MySQL.execute(query);

            if (isLoad) {
                return resultSet;
            } else {

                DefaultTableModel tModel = (DefaultTableModel) jTable1.getModel();
                tModel.setRowCount(0);

                while (resultSet.next()) {

                    Vector<String> v = new Vector();
                    v.add(resultSet.getString("sno"));
                    v.add(resultSet.getString("first_name"));
                    v.add(resultSet.getString("last_name"));
                    v.add(resultSet.getString("mobile"));
                    v.add(resultSet.getString("dob"));
                    v.add(resultSet.getString("gender.type"));

                    tModel.addRow(v);

                    jTable1.setModel(tModel);

                }

                return null;

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private HashMap loadSubjectsComboBox(boolean returnData, int cBoxNo) {

        try {

            String query = "SELECT * FROM `subject` ORDER BY `name` ASC";

            ResultSet resultSet = MySQL.execute(query);

            Vector v = new Vector<>();
            v.add("Select");

            HashMap<String, Integer> map = new HashMap<>();

            while (resultSet.next()) {
                v.add(resultSet.getString("name"));
                if (returnData) {
                    map.put(resultSet.getString("name"), Integer.valueOf(resultSet.getString("sub_no")));
                }
            }

            if (returnData) {
                return map;
            } else if (cBoxNo == 1) {
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                jComboBox1.setModel(model);
                return null;
            } else if (cBoxNo == 2) {
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                jComboBox2.setModel(model);
                return null;
            } else if (cBoxNo == 3) {
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                jComboBox3.setModel(model);
                return null;
            } else if (cBoxNo == 6) {
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                jComboBox6.setModel(model);
                return null;
            } else {
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                jComboBox1.setModel(model);
                jComboBox2.setModel(model);
                jComboBox3.setModel(model);
                jComboBox6.setModel(model);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private HashMap verifyTeacher() throws Exception {

        String result = null;

        HashMap<String, String> registerMap = new HashMap();

        String fname = jTextField9.getText();
        String lname = jTextField10.getText();
        String email = jTextField11.getText();
        String address = jTextField12.getText();
        String password1 = String.valueOf(jPasswordField2.getPassword());
        ButtonModel genderSelection = buttonGroup2.getSelection();
        String subject = String.valueOf(jComboBox1.getSelectedItem());

        if (fname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter first name", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField9.requestFocus();
        } else if (lname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter last name", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField10.requestFocus();
        } else if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter email", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField11.requestFocus();
        } else if (address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Home Address", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField12.requestFocus();
        } else if (password1.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter password", "Warning", JOptionPane.ERROR_MESSAGE);
            jPasswordField2.requestFocus();
        } else if (password1.length() < 6 || password1.length() > 40) {
            JOptionPane.showMessageDialog(this, "Password should contain 6 to 40 characters", "Warning", JOptionPane.ERROR_MESSAGE);
            jPasswordField2.requestFocus();
        } else if (genderSelection == null) {
            JOptionPane.showMessageDialog(this, "Select the gender", "Warning", JOptionPane.ERROR_MESSAGE);
            jRadioButton3.requestFocus();
        } else if (subject.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Select the subject", "Warning", JOptionPane.ERROR_MESSAGE);
            jComboBox1.requestFocus();
        } else {

            String gender = genderSelection.getActionCommand();

            HashMap<String, Integer> map = loadSubjectsComboBox(true, 0);
            String subjectId = String.valueOf(map.get(subject));

            registerMap.put("firstName", fname);
            registerMap.put("lastName", lname);
            registerMap.put("email", email);
            registerMap.put("password", password1);
            registerMap.put("genderId", gender);
            registerMap.put("subjectId", subjectId);
            registerMap.put("address", address);

            result = "Success";
            registerMap.put("verifyResult", result);
        }

        registerMap.put("verifyResult", result);
        return registerMap;

    }

    private void refreshTeacherTable() {
        loadTeachers(false, false, "");

        jTable2.setEnabled(true);
        jTextField8.setEnabled(true);
        jButton12.setEnabled(true);
        jButton14.setEnabled(true);

        jTextField8.setText("");
        jTextField9.setText("");
        jTextField10.setText("");
        jTextField11.setText("");
        jTextField12.setText("");
        jPasswordField2.setText("");

        jComboBox1.setSelectedItem("Select");
        buttonGroup2.clearSelection();

        jButton15.setEnabled(false);
        jButton16.setEnabled(false);
    }

    private ResultSet loadTeachers(Boolean isSearch, Boolean isLoad, String tno) {

        try {

            String query = "SELECT * FROM `teacher` "
                    + "INNER JOIN `gender` ON `teacher`.`g_gender_id`=`gender`.`gender_id` "
                    + "INNER JOIN `subject` ON `teacher`.`subject_sub_no`=`subject`.`sub_no`";

            if (isSearch) {

                query += " WHERE `tno` LIKE '%" + tno + "%' ORDER BY `tno` ASC";

            } else {

                query += " ORDER BY `tno` ASC";

            }

            ResultSet resultSet = MySQL.execute(query);

            if (isLoad) {
                return resultSet;
            } else {

                DefaultTableModel tModel = (DefaultTableModel) jTable2.getModel();
                tModel.setRowCount(0);

                while (resultSet.next()) {

                    Vector<String> v = new Vector();
                    v.add(resultSet.getString("tno"));
                    v.add(resultSet.getString("first_name"));
                    v.add(resultSet.getString("last_name"));
                    v.add(resultSet.getString("email"));
                    v.add(resultSet.getString("gender.type"));
                    v.add(resultSet.getString("subject.name"));

                    tModel.addRow(v);

                    jTable2.setModel(tModel);

                }

                return null;

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private void refreshSubjectTables() {
        loadSubjects(false, "");

        jTable3.setEnabled(true);
        jTable4.setEnabled(false);
        jTextField6.setEnabled(true);
        jButton18.setEnabled(true);
        jButton32.setEnabled(true);

        jTextField14.setText("");
        jTextField15.setText("");
        jTextField16.setText("");

        jLabel54.setText(""); //Teacher No
        jLabel33.setText(""); //Teacher Name
        jLabel27.setText(""); //Count

        jComboBox2.setSelectedItem("Select");

        jButton19.setEnabled(false);
        jButton20.setEnabled(false);
    }

    private void loadSubjects(Boolean isSearch, String sub_no) {

        try {

            String query = "SELECT * FROM `subject`";

            if (isSearch) {

                query += " WHERE `sub_no`='" + sub_no + "' ORDER BY `sub_no` ASC";

            } else {

                query += " ORDER BY `sub_no` ASC";

            }

            ResultSet resultSet = MySQL.execute(query);

            DefaultTableModel tModel = (DefaultTableModel) jTable3.getModel();
            tModel.setRowCount(0);

            while (resultSet.next()) {

                Vector<String> v = new Vector();
                v.add(resultSet.getString("sub_no"));
                v.add(resultSet.getString("name"));
                v.add(resultSet.getString("description"));
                v.add(resultSet.getString("price"));

                tModel.addRow(v);

                jTable3.setModel(tModel);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private HashMap verifySubject() {

        String result = null;

        HashMap<String, String> registerMap = new HashMap();

        String sName = jTextField14.getText();
        String price = jTextField15.getText();
        String description = jTextField16.getText();

        if (sName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter subject name", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField14.requestFocus();
        } else if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter subject description", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField16.requestFocus();
        } else if (price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter subject price", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField15.requestFocus();
        } else if (Integer.parseInt(price) < 1000) {
            JOptionPane.showMessageDialog(this, "Subject price should be above 1000", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField15.requestFocus();
        } else {

            registerMap.put("name", sName);
            registerMap.put("description", description);
            registerMap.put("price", price);

            result = "Success";
            registerMap.put("verifyResult", result);
        }

        registerMap.put("verifyResult", result);
        return registerMap;

    }

    private Integer loadSubjectTeachers(String sub_no) {

        try {

            String query = "SELECT * FROM `teacher` INNER JOIN `gender` ON `teacher`.`g_gender_id`=`gender`.`gender_id` WHERE `subject_sub_no`='" + sub_no + "' ORDER BY `subject_sub_no` ASC";

            ResultSet resultSet = MySQL.execute(query);

            DefaultTableModel tModel = (DefaultTableModel) jTable4.getModel();
            tModel.setRowCount(0);

            int x = 0;
            while (resultSet.next()) {

                x++;
                Vector<String> v = new Vector();
                v.add(resultSet.getString("tno"));
                v.add(resultSet.getString("first_name"));
                v.add(resultSet.getString("last_name"));
                v.add(resultSet.getString("email"));
                v.add(resultSet.getString("gender.type"));

                tModel.addRow(v);

                jTable4.setModel(tModel);

            }

            return x;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private HashMap loadTeachersComboBox(boolean returnData, int cBoxNo) {

        try {

            String query = "SELECT * FROM `teacher` ORDER BY `first_name` ASC";

            ResultSet resultSet = MySQL.execute(query);

            Vector v = new Vector<>();
            v.add("Select");

            HashMap<String, Integer> map = new HashMap<>();

            while (resultSet.next()) {
                String name = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                v.add(name);
                if (returnData) {
                    map.put(name, Integer.valueOf(resultSet.getString("tno")));
                }
            }

            if (returnData) {
                return map;
            } else if (cBoxNo == 4) {
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                jComboBox4.setModel(model);
                return null;
            } else if (cBoxNo == 7) {
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                jComboBox7.setModel(model);
                return null;
            } else {
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                jComboBox4.setModel(model);
                jComboBox7.setModel(model);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private HashMap loadTimeSlotsComboBox(boolean returnData, int cBoxNo) {

        try {

            String query = "SELECT * FROM `time_slots` ORDER BY `time_slot_id` ASC";

            ResultSet resultSet = MySQL.execute(query);

            Vector v = new Vector<>();
            v.add("Select");

            HashMap<String, Integer> map = new HashMap<>();

            while (resultSet.next()) {
                String slot = resultSet.getString("from") + "-" + resultSet.getString("to");
                v.add(slot);
                if (returnData) {
                    map.put(slot, Integer.valueOf(resultSet.getString("time_slot_id")));
                }
            }

            if (returnData) {
                return map;
            } else if (cBoxNo == 1) {
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                jComboBox5.setModel(model);
                return null;
            } else {
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                jComboBox5.setModel(model);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private ResultSet loadClasses(Boolean isSearch, Boolean isLoad, String class_no) {

        try {

            String query = "SELECT * FROM `class` "
                    + "INNER JOIN `teacher` ON `class`.`teacher_tno`=`teacher`.`tno` "
                    + "INNER JOIN `subject` ON `class`.`subject_sub_no`=`subject`.`sub_no`"
                    + "INNER JOIN `time_slots` ON `class`.`ts_time_slot_id`=`time_slots`.`time_slot_id`";

            if (isSearch) {

                query += " WHERE `class_no` LIKE '%" + class_no + "%' ORDER BY `class_no` ASC";

            } else {

                query += " ORDER BY `class_no` ASC";

            }

            ResultSet resultSet = MySQL.execute(query);

            if (isLoad) {
                return resultSet;
            } else {

                DefaultTableModel tModel = (DefaultTableModel) jTable5.getModel();
                tModel.setRowCount(0);

                while (resultSet.next()) {

                    Vector<String> v = new Vector();
                    v.add(resultSet.getString("class_no"));
                    v.add(resultSet.getString("name"));
                    v.add(resultSet.getString("subject.name"));
                    v.add(resultSet.getString("teacher.first_name") + " " + resultSet.getString("teacher.last_name"));
                    v.add(resultSet.getString("time_slots.from") + "-" + resultSet.getString("time_slots.to"));
                    v.add(resultSet.getString("date"));

                    tModel.addRow(v);

                    jTable5.setModel(tModel);

                }

                return null;

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private HashMap verifyClass() {

        String result = null;

        HashMap<String, String> registerMap = new HashMap();

        String cName = jTextField17.getText();
        String subject = String.valueOf(jComboBox3.getSelectedItem());
        String teacher = String.valueOf(jComboBox4.getSelectedItem());
        String time_slot = String.valueOf(jComboBox5.getSelectedItem());
        Date date = jDateChooser2.getDate();

        if (cName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter class name", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField17.requestFocus();
        } else if (subject.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Select the Subject", "Warning", JOptionPane.ERROR_MESSAGE);
            jComboBox3.requestFocus();
        } else if (teacher.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Select the Teacher", "Warning", JOptionPane.ERROR_MESSAGE);
            jComboBox4.requestFocus();
        } else if (time_slot.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Select the time slot", "Warning", JOptionPane.ERROR_MESSAGE);
            jComboBox5.requestFocus();
        } else if (date == null) {
            JOptionPane.showMessageDialog(this, "Select the date", "Warning", JOptionPane.ERROR_MESSAGE);
            jDateChooser2.requestFocus();
        } else {

            HashMap<String, Integer> map1 = loadSubjectsComboBox(true, 0);
            String sub_no = String.valueOf(map1.get(subject));

            HashMap<String, Integer> map2 = loadTeachersComboBox(true, 0);
            String tno = String.valueOf(map2.get(teacher));

            HashMap<String, Integer> map3 = loadTimeSlotsComboBox(true, 0);
            String time_slot_id = String.valueOf(map3.get(time_slot));

            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            String cDate = formatDate.format(date);

            registerMap.put("name", cName);
            registerMap.put("sub_no", sub_no);
            registerMap.put("tno", tno);
            registerMap.put("time_slot", time_slot_id);
            registerMap.put("date", cDate);

            result = "Success";
            registerMap.put("verifyResult", result);
        }

        registerMap.put("verifyResult", result);
        return registerMap;

    }

    private void refreshClassTable() {
        loadClasses(false, false, "");

        jTable5.setEnabled(true);
        jTextField13.setEnabled(true);
        jButton17.setEnabled(true);
        jButton22.setEnabled(true);

        jTextField13.setText("");
        jTextField17.setText("");

        jComboBox3.setSelectedItem("Select");
        jComboBox4.setSelectedItem("Select");
        jComboBox5.setSelectedItem("Select");

        jDateChooser2.setDate(new Date());

        jButton23.setEnabled(false);
        jButton24.setEnabled(false);
    }

    private HashMap loadStudentsComboBox(boolean returnData, int cBoxNo) {

        try {

            String query = "SELECT * FROM `student` ORDER BY `sno` ASC";

            ResultSet resultSet = MySQL.execute(query);

            Vector v = new Vector<>();
            v.add("Select");

            HashMap<Integer, String> map = new HashMap<>();

            while (resultSet.next()) {
                v.add(Integer.valueOf(resultSet.getString("sno")));
                if (returnData) {
                    String name = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                    map.put(Integer.valueOf(resultSet.getString("sno")), name);
                }
            }

            if (returnData) {
                return map;
            } else if (cBoxNo == 8) {
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                jComboBox8.setModel(model);
                return null;
            } else {
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                jComboBox8.setModel(model);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private ResultSet loadPayments(Boolean isSearch, Boolean isLoad, String payment_id) {

        try {

            String query = "SELECT * FROM `payments` "
                    + "INNER JOIN `teacher` ON `payments`.`teacher_tno`=`teacher`.`tno` "
                    + "INNER JOIN `subject` ON `payments`.`subject_sub_no`=`subject`.`sub_no`"
                    + "INNER JOIN `student` ON `payments`.`student_sno`=`student`.`sno`";

            if (isSearch) {
                query += " WHERE `payment_id` LIKE '%" + payment_id + "%'";
            }

            query += " ORDER BY `payment_id` ASC";

            ResultSet resultSet = MySQL.execute(query);

            if (isLoad) {
                return resultSet;
            } else {

                DefaultTableModel tModel = (DefaultTableModel) jTable6.getModel();
                tModel.setRowCount(0);

                while (resultSet.next()) {

                    Vector<String> v = new Vector();
                    v.add(resultSet.getString("payment_id"));
                    v.add(resultSet.getString("student.sno"));
                    v.add(resultSet.getString("student.first_name") + " " + resultSet.getString("student.last_name"));
                    v.add(resultSet.getString("subject.name"));
                    v.add(resultSet.getString("teacher.first_name") + " " + resultSet.getString("teacher.last_name"));
                    v.add(resultSet.getString("value"));
                    v.add(resultSet.getString("date_time"));

                    tModel.addRow(v);

                    jTable6.setModel(tModel);

                }

                return null;

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private HashMap verifyPayment() {

        String result = null;

        HashMap<String, String> registerMap = new HashMap();

        String sno = String.valueOf(jComboBox8.getSelectedItem());
        String subject = String.valueOf(jComboBox6.getSelectedItem());
        String teacher = String.valueOf(jComboBox7.getSelectedItem());
        String value = jTextField19.getText();

        Date date = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = format1.format(date);

        if (sno.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Select the Student", "Warning", JOptionPane.ERROR_MESSAGE);
            jComboBox8.requestFocus();
        } else if (subject.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Select the Subject", "Warning", JOptionPane.ERROR_MESSAGE);
            jComboBox6.requestFocus();
        } else if (teacher.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Select the Teacher", "Warning", JOptionPane.ERROR_MESSAGE);
            jComboBox7.requestFocus();
        } else if (value.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the value", "Warning", JOptionPane.ERROR_MESSAGE);
            jTextField19.requestFocus();
        } else {

            HashMap<String, Integer> map1 = loadSubjectsComboBox(true, 0);
            String sub_no = String.valueOf(map1.get(subject));

            HashMap<String, Integer> map2 = loadTeachersComboBox(true, 0);
            String tno = String.valueOf(map2.get(teacher));

            registerMap.put("sno", sno);
            registerMap.put("sub_no", sub_no);
            registerMap.put("tno", tno);
            registerMap.put("value", value);
            registerMap.put("date", dateTime);

            result = "Success";
            registerMap.put("verifyResult", result);
        }

        registerMap.put("verifyResult", result);
        return registerMap;

    }

    private void refreshPaymentTable() {
        loadPayments(false, false, "");

        jTable6.setEnabled(true);
        jButton26.setEnabled(true);
        jButton27.setEnabled(false);
        jButton28.setEnabled(false);

        jTextField18.setText("");
        jTextField18.setEnabled(true);
        jButton25.setEnabled(true);

        jComboBox8.setEnabled(true);

        jComboBox6.setSelectedItem("Select");
        jComboBox7.setSelectedItem("Select");
        jComboBox8.setSelectedItem("Select");

        jTextField19.setText("");

        jLabel47.setText("Student Name");
        jLabel49.setText("Price");

    }

    private void view(String panel) {
        jPanel7.setVisible(false);
        jPanel8.setVisible(false);
        jPanel11.setVisible(false);
        jPanel14.setVisible(false);
        jPanel17.setVisible(false);
        jPanel20.setVisible(false);
        switch (panel) {
            case "dashboard":
                jPanel7.setVisible(true);
                jButton1.requestFocus();
                break;
            case "manageStudents":
                jPanel8.setVisible(true);
                jButton2.requestFocus();
                refreshStudentTable();
                break;
            case "manageTeachers":
                jPanel11.setVisible(true);
                jButton3.requestFocus();
                refreshTeacherTable();
                break;
            case "manageSubjects":
                jPanel14.setVisible(true);
                jButton4.requestFocus();
                refreshSubjectTables();
                break;
            case "manageClasses":
                jPanel17.setVisible(true);
                jButton5.requestFocus();
                refreshClassTable();
                break;
            case "managePayments":
                jPanel20.setVisible(true);
                jButton6.requestFocus();
                refreshPaymentTable();
                break;
            default:
                break;
        }
    }

    private void setData(HashMap<String, String> loginMap) {
        jLabel5.setText(loginMap.get("username"));
    }

    private void ImageScaling() {
        ImageIcon ii = new ImageIcon(getClass().getResource("/sources/icon.png"));
        Image image = (ii).getImage().getScaledInstance(icon.getWidth(), icon.getHeight(), Image.SCALE_SMOOTH);
        ii = new ImageIcon(image);
        icon.setIcon(ii);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        icon = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton30 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabe8 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton7 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabe9 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();
        jButton13 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel13 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton31 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton32 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel27 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton21 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel37 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel38 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel39 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jButton17 = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jButton34 = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jButton25 = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel43 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox<>();
        jLabel44 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jButton29 = new javax.swing.JButton();
        jComboBox8 = new javax.swing.JComboBox<>();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jButton35 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(900, 100));

        icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/icon.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 204, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Adhyapana");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 204, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Institute");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 102, 255));
        jLabel3.setText("Admin");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Welcome");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 204, 255));
        jLabel5.setText("Username");

        jButton1.setText("Dashboard");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Manage Students");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        jButton3.setText("Manage Teachers");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Manage Subjects");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton5.setText("Manage Classes");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Manage Payments");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel4))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel3)
                .addGap(6, 6, 6)
                .addComponent(jLabel4)
                .addGap(7, 7, 7)
                .addComponent(jLabel5))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(icon, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(icon, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(7, 7, 7))
        );

        jPanel2.setLayout(null);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 153, 255));
        jLabel6.setText("Dashboard");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(840, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(480, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel7);
        jPanel7.setBounds(0, 0, 1000, 540);

        jLabel51.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setText("Manage Students");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sno", "First Name", "Last Name", "Mobile", "BirthDay", "Gender"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(40);
        }

        jButton30.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        jButton30.setText("Refresh");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jButton30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setText("Student No");

        jTextField7.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jButton11.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton11.setText("Search");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setText("First Name");

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabe8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabe8.setText("Last Name");

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setText("Email");

        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setText("Mobile");

        jTextField4.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel11.setText("Password");

        jPasswordField1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        jButton7.setText("\\u3026");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton7MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton7MouseReleased(evt);
            }
        });
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel12.setText("Address");

        jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel13.setText("Date of Birth");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel14.setText("Gender");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jRadioButton1.setText("Male");
        jRadioButton1.setActionCommand("1");
        jRadioButton1.setActionCommand("1");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jRadioButton2.setText("Female");
        jRadioButton2.setActionCommand("2");
        jRadioButton2.setActionCommand("2");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton8.setText("Create New User Account");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton9.setText("Update User Account");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton10.setText("Delete User Account");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jButton11)
                .addGap(14, 14, 14))
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(256, 264, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                                                .addComponent(jLabel14)
                                                .addGap(50, 50, 50)
                                                .addComponent(jRadioButton1)
                                                .addGap(27, 27, 27)
                                                .addComponent(jRadioButton2)))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addComponent(jPasswordField1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField2)
                                            .addComponent(jLabe8, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))))
                                .addGap(8, 8, 8)))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabe8)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addGap(2, 2, 2)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addGap(10, 10, 10)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel8);
        jPanel8.setBounds(0, 0, 1000, 540);

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel15.setText("Teacher No");

        jTextField8.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton12.setText("Search");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel16.setText("First Name");

        jTextField9.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabe9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabe9.setText("Last Name");

        jTextField10.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel17.setText("Email");

        jTextField11.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel18.setText("Address");

        jTextField12.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel19.setText("Password");

        jPasswordField2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPasswordField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField2ActionPerformed(evt);
            }
        });

        jButton13.setText("\\u3026");
        jButton13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton13MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton13MouseReleased(evt);
            }
        });
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel20.setText("Gender");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel21.setText("Subject");

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jRadioButton3.setText("Male");
        jRadioButton3.setActionCommand("1");
        jRadioButton1.setActionCommand("1");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jRadioButton4.setText("Female");
        jRadioButton4.setActionCommand("2");
        jRadioButton2.setActionCommand("2");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jButton14.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton14.setText("Add New Teacher");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton15.setText("Update Teacher");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton16.setText("Delete Teacher");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(jButton12)
                                .addGap(13, 13, 13))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel12Layout.createSequentialGroup()
                                            .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField12, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                                            .addComponent(jLabel20)
                                            .addGap(43, 43, 43)
                                            .addComponent(jRadioButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jRadioButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel12Layout.createSequentialGroup()
                                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jTextField9)
                                                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jTextField10)
                                                .addComponent(jLabe9, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)))
                                        .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabe9)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addComponent(jLabel16)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jTextField10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton4))
                .addGap(18, 18, 18)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel52.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText("Manage Teachers");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tno", "First Name", "Last Name", "Email", "Gender", "Subject"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(35);
            jTable2.getColumnModel().getColumn(4).setResizable(false);
            jTable2.getColumnModel().getColumn(4).setPreferredWidth(40);
        }

        jButton31.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        jButton31.setText("Refresh");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                    .addComponent(jButton31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(490, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel11);
        jPanel11.setBounds(0, 0, 1000, 540);

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Manage Subjects");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel25.setText("Total Subjects");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setText("Subjects");

        jLabel53.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel53.setText("Subject No");

        jButton32.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jButton32.setText("Search");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel29.setText("Subject Name");

        jTextField14.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel30.setText("Price");

        jTextField15.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel31.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel31.setText("Description");

        jTextField16.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jButton18.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton18.setText("Add New Subject");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton19.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton19.setText("Delete Subject");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton20.setText("Update Subject");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(6, 6, 6))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextField16)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextField15)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextField14)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                            .addComponent(jLabel25)
                            .addGap(33, 33, 33)
                            .addComponent(jLabel26))
                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                            .addComponent(jLabel53)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton32))))
                .addGap(0, 19, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel24)
                .addGap(27, 27, 27)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField6))
                .addGap(26, 26, 26)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Sub_no", "Subject", "Price", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 153, 153));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Subjects");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 153, 153));
        jLabel23.setText("Teachers on the selected subject");

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tno", "First Name", "Last Name", "Email", "Gender"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable4);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel27.setText("No. of Teachers");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel32.setText("Teacher");

        jComboBox2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jButton21.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jButton21.setText("Change");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton33.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        jButton33.setText("Refresh");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel27))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel32)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                        .addComponent(jButton21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(7, 7, 7)
                .addComponent(jButton33)
                .addGap(7, 7, 7))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel14);
        jPanel14.setBounds(0, 0, 1000, 540);

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Manage Classes");

        jLabel35.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel35.setText("Class Name");

        jLabel36.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel36.setText("Subject");

        jComboBox3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        jLabel37.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel37.setText("Teacher");

        jComboBox4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        jLabel38.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel38.setText("Time Slot");

        jComboBox5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        jLabel39.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel39.setText("Date");

        jButton22.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton22.setText("Add New Class");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton23.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton23.setText("Delete Class");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton24.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton24.setText("Update Class");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel28.setText("Class No");

        jTextField13.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jTextField13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField13ActionPerformed(evt);
            }
        });

        jButton17.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton17.setText("Search");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel38)
                                    .addComponent(jComboBox5, 0, 274, Short.MAX_VALUE)
                                    .addComponent(jLabel36)
                                    .addComponent(jTextField17)
                                    .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel35)
                                    .addComponent(jLabel37)
                                    .addComponent(jComboBox4, 0, 274, Short.MAX_VALUE)
                                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField13, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Class No", "Name", "Subject", "Teacher", "Time", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable5MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTable5);
        if (jTable5.getColumnModel().getColumnCount() > 0) {
            jTable5.getColumnModel().getColumn(0).setPreferredWidth(35);
        }

        jButton34.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        jButton34.setText("Refresh");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
                    .addComponent(jButton34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel17);
        jPanel17.setBounds(0, 0, 1000, 540);

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("Manage Payments");

        jLabel41.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel41.setText("Payment ID");

        jTextField18.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jTextField18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField18ActionPerformed(evt);
            }
        });

        jButton25.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton25.setText("Search");

        jLabel42.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel42.setText("Subject");

        jComboBox6.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        jLabel43.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel43.setText("Teacher");

        jComboBox7.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        jLabel44.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel44.setText("Student No");

        jLabel46.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel46.setText("Student Name");

        jLabel47.setFont(new java.awt.Font("Times New Roman", 0, 15)); // NOI18N
        jLabel47.setText("Student Name");

        jButton26.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton26.setText("Add New Payment");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jButton27.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton27.setText("Delete Payment");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton28.setFont(new java.awt.Font("Segoe UI", 3, 15)); // NOI18N
        jButton28.setText("Update Payment");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel48.setText("Price");

        jLabel49.setFont(new java.awt.Font("Times New Roman", 0, 15)); // NOI18N
        jLabel49.setText("Price");

        jLabel50.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel50.setText("Payment Value");

        jButton29.setFont(new java.awt.Font("Times New Roman", 2, 16)); // NOI18N
        jButton29.setText("Invoice");

        jComboBox8.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel41)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton25)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jButton27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel21Layout.createSequentialGroup()
                                    .addComponent(jLabel50)
                                    .addGap(18, 18, 18)
                                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel21Layout.createSequentialGroup()
                                    .addComponent(jLabel44)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel42)
                                .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel46)
                                .addComponent(jLabel43)
                                .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel21Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                                            .addComponent(jLabel48)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox8)
                    .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel46)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(jLabel49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PaymentID", "Sno", "Student", "Subject", "Teacher", "Value", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable6MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTable6);

        jButton35.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        jButton35.setText("Refresh");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel20);
        jPanel20.setBounds(0, 0, 1000, 540);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        view("dashboard");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        view("manageTeachers");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        view("manageStudents");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        view("manageSubjects");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        view("manageClasses");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        view("managePayments");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    //Student Register
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed

        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            try {

                HashMap result = verifyStudent();
                if (result.get("verifyResult") != null) {

                    //System.out.println(result);
                    String query = "INSERT INTO `student`"
                            + " (`first_name`,`last_name`,`address`,`dob`,`email`,`mobile`,`password`,`g_gender_id`)"
                            + " VALUES ('" + result.get("firstName") + "','" + result.get("lastName") + "','" + result.get("address") + "','" + result.get("dob") + "','" + result.get("email") + "','" + result.get("mobile") + "','" + result.get("password") + "','" + result.get("genderId") + "')";
                    MySQL.execute(query);

                    refreshStudentTable();

                }

            } catch (Exception e) {
                e.printStackTrace();;
            }

        } else {
            JOptionPane.showMessageDialog(this, "Unable to create new account with having a selected row. Please Refresh", "Warning", JOptionPane.ERROR_MESSAGE);
            jButton30.requestFocus();
        }

    }//GEN-LAST:event_jButton8ActionPerformed

    //Student Delete
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed

        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {
            String sno = String.valueOf(jTable1.getValueAt(selectedRow, 0));

            try {

                String query = "DELETE FROM `student` WHERE `sno`='" + sno + "'";

                MySQL.execute(query);

                refreshStudentTable();

                JOptionPane.showMessageDialog(this, "Deleted Student " + sno, "Delete Success", JOptionPane.PLAIN_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_jButton10ActionPerformed

    //Student Update
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {

            try {

                String sno = jTextField7.getText();

                HashMap result = verifyStudent();
                if (result.get("verifyResult") != null) {

                    String query = "UPDATE `student` SET "
                            + "`first_name`='" + result.get("firstName") + "', `last_name`='" + result.get("lastName") + "', `mobile`='" + result.get("mobile") + "', `email`='" + result.get("email") + "', "
                            + "`password`='" + result.get("password") + "',  `g_gender_id`='" + result.get("genderId") + "', `address`='" + result.get("address") + "', `dob`='" + result.get("dob") + "' "
                            + "WHERE `sno`='" + sno + "'";

                    MySQL.execute(query);

                    refreshStudentTable();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void jButton7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MousePressed
        jPasswordField1.setEchoChar('\u0000');
    }//GEN-LAST:event_jButton7MousePressed

    private void jButton7MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseReleased
        jPasswordField1.setEchoChar('\u2022');
    }//GEN-LAST:event_jButton7MouseReleased

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jPasswordField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField2ActionPerformed
    }//GEN-LAST:event_jPasswordField2ActionPerformed

    private void jButton13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton13MousePressed
        jPasswordField2.setEchoChar('\u0000');
    }//GEN-LAST:event_jButton13MousePressed

    private void jButton13MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton13MouseReleased
        jPasswordField2.setEchoChar('\u2022');
    }//GEN-LAST:event_jButton13MouseReleased

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    //Teacher Register
    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed

        int selectedRow = jTable2.getSelectedRow();

        if (selectedRow == -1) {
            try {

                HashMap result = verifyTeacher();
                if (result.get("verifyResult") != null) {

                    //System.out.println(result);
                    String query = "INSERT INTO `teacher`"
                            + " (`first_name`,`last_name`,`address`,`email`,`subject_sub_no`,`password`,`g_gender_id`)"
                            + " VALUES ('" + result.get("firstName") + "','" + result.get("lastName") + "','" + result.get("address") + "','" + result.get("email") + "','" + result.get("subjectId") + "','" + result.get("password") + "','" + result.get("genderId") + "')";
                    MySQL.execute(query);

                    refreshTeacherTable();
                    loadTeachersComboBox(false, 0);

                }

            } catch (Exception e) {
                e.printStackTrace();;
            }

        } else {
            JOptionPane.showMessageDialog(this, "Unable to create new account with having a selected row. Please Refresh", "Warning", JOptionPane.ERROR_MESSAGE);
            jButton31.requestFocus();
        }

    }//GEN-LAST:event_jButton14ActionPerformed

    //Teacher Delete
    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed

        int selectedRow = jTable2.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {
            String tno = String.valueOf(jTable2.getValueAt(selectedRow, 0));

            try {

                String query = "DELETE FROM `teacher` WHERE `tno`='" + tno + "'";

                MySQL.execute(query);

                refreshTeacherTable();
                loadTeachersComboBox(false, 0);

                JOptionPane.showMessageDialog(this, "Deleted Teacher " + tno, "Delete Success", JOptionPane.PLAIN_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_jButton16ActionPerformed

    //Teacher Update
    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed

        int selectedRow = jTable2.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {

            try {

                String tno = jTextField8.getText();

                HashMap result = verifyTeacher();
                if (result.get("verifyResult") != null) {

                    String query = "UPDATE `teacher` SET "
                            + "`first_name`='" + result.get("firstName") + "', `last_name`='" + result.get("lastName") + "', `subject_sub_no`='" + result.get("subjectId") + "', `email`='" + result.get("email") + "', "
                            + "`password`='" + result.get("password") + "',  `g_gender_id`='" + result.get("genderId") + "', `address`='" + result.get("address") + "' "
                            + "WHERE `tno`='" + tno + "'";

                    MySQL.execute(query);

                    refreshTeacherTable();
                    loadTeachersComboBox(false, 0);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_jButton15ActionPerformed

    private void jTextField13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField13ActionPerformed
    }//GEN-LAST:event_jTextField13ActionPerformed

    //Subject Add
    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed

        int selectedRow = jTable3.getSelectedRow();

        if (selectedRow == -1) {
            try {

                HashMap result = verifySubject();
                if (result.get("verifyResult") != null) {

                    //System.out.println(result);
                    String query = "INSERT INTO `subject`"
                            + " (`name`,`description`,`price`)"
                            + " VALUES ('" + result.get("name") + "','" + result.get("description") + "','" + result.get("price") + "')";
                    MySQL.execute(query);

                    refreshSubjectTables();
                    loadSubjectsComboBox(false, 0);

                }

            } catch (Exception e) {
                e.printStackTrace();;
            }

        } else {
            JOptionPane.showMessageDialog(this, "Unable to create new account with having a selected row. Please Refresh", "Warning", JOptionPane.ERROR_MESSAGE);
            jButton33.requestFocus();
        }

    }//GEN-LAST:event_jButton18ActionPerformed

    //Subject Delete
    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed

        int selectedRow = jTable3.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {
            String sub_no = String.valueOf(jTable3.getValueAt(selectedRow, 0));

            try {

                String query = "DELETE FROM `subject` WHERE `sub_no`='" + sub_no + "'";

                MySQL.execute(query);

                refreshSubjectTables();
                loadSubjectsComboBox(false, 0);

                JOptionPane.showMessageDialog(this, "Deleted Subject " + sub_no, "Delete Success", JOptionPane.PLAIN_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_jButton19ActionPerformed

    //Subject Update
    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed

        int selectedRow = jTable3.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {

            try {

                String sub_no = jTextField6.getText();

                HashMap result = verifySubject();
                if (result.get("verifyResult") != null) {

                    String query = "UPDATE `subject` SET "
                            + "`name`='" + result.get("name") + "', `description`='" + result.get("description") + "', `price`='" + result.get("price") + "' "
                            + "WHERE `sub_no`='" + sub_no + "'";

                    MySQL.execute(query);

                    refreshSubjectTables();
                    loadSubjectsComboBox(false, 0);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_jButton20ActionPerformed

    //Class Add
    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed

        int selectedRow = jTable5.getSelectedRow();

        if (selectedRow == -1) {
            try {

                HashMap result = verifyClass();
                if (result.get("verifyResult") != null) {

                    //System.out.println(result);
                    String query1 = "INSERT INTO `class`"
                            + " (`name`,`teacher_tno`,`subject_sub_no`,`ts_time_slot_id`,`date`)"
                            + " VALUES ('" + result.get("name") + "','" + result.get("tno") + "','" + result.get("sub_no") + "','" + result.get("time_slot") + "','" + result.get("date") + "')";
                    MySQL.execute(query1);

                    String query2 = "SELECT * FROM `class` WHERE `subject_sub_no`='" + result.get("sub_no") + "' AND `date`='" + result.get("date") + "' AND `teacher_tno`='" + result.get("tno") + "' AND `ts_time_slot_id`='" + result.get("time_slot") + "'";
                    ResultSet resultSet1 = MySQL.execute(query2);

                    while (resultSet1.next()) {
                        String class_no = resultSet1.getString("class_no");

                        String query3 = "SELECT * FROM `payments` WHERE `subject_sub_no`='" + result.get("sub_no") + "' AND `teacher_tno`='" + result.get("tno") + "'";
                        ResultSet resultSet2 = MySQL.execute(query3);

                        while (resultSet2.next()) {
                            String sno = resultSet1.getString("student_sno");

                            String query4 = "INSERT INTO `student_has_class` (`student_sno`, `c_class_no`) VALUES ('" + sno + "', '" + class_no + "')";
                            MySQL.execute(query4);

                            String query5 = "SELECT * FROM `student_has_class` WHERE `c_class_no`='" + class_no + "' AND `student_sno`='" + sno + "'";
                            ResultSet resultSet3 = MySQL.execute(query5);

                            while (resultSet3.next()) {
                                String shc_id = resultSet3.getString("student_has_class_id");
                                
                                String query6 = "INSERT INTO `attendance` (`shc_student_has_class_id`, `at_attendance_type`) VALUES ('" + shc_id + "', '2')";
                                MySQL.execute(query6);

                            }

                        }

                    }

                    refreshClassTable();

                }

            } catch (Exception e) {
                e.printStackTrace();;
            }

        } else {
            JOptionPane.showMessageDialog(this, "Unable to create new account with having a selected row. Please Refresh", "Warning", JOptionPane.ERROR_MESSAGE);
            jButton34.requestFocus();
        }

    }//GEN-LAST:event_jButton22ActionPerformed

    //Class Delete
    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed

        int selectedRow = jTable5.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {
            String class_no = String.valueOf(jTable5.getValueAt(selectedRow, 0));

            try {

                String query = "DELETE FROM `class` WHERE `class_no`='" + class_no + "'";

                MySQL.execute(query);

                refreshTeacherTable();

                JOptionPane.showMessageDialog(this, "Deleted Class " + class_no, "Delete Success", JOptionPane.PLAIN_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_jButton23ActionPerformed

    //Class Update
    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed

        int selectedRow = jTable5.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {

            try {

                String class_no = jTextField13.getText();

                HashMap result = verifyClass();
                if (result.get("verifyResult") != null) {

                    String query = "UPDATE `class` SET "
                            + "`name`='" + result.get("name") + "', `teacher_tno`='" + result.get("tno") + "', "
                            + "`subject_sub_no`='" + result.get("sub_no") + "', `ts_time_slot_id`='" + result.get("time_slot") + "', "
                            + "`date`='" + result.get("date") + "' WHERE `class_no`='" + class_no + "'";

                    MySQL.execute(query);

                    refreshClassTable();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_jButton24ActionPerformed

    private void jTextField18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField18ActionPerformed
    }//GEN-LAST:event_jTextField18ActionPerformed

    //Payment Add
    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed

        int selectedRow = jTable6.getSelectedRow();

        if (selectedRow == -1) {
            try {

                HashMap result = verifyPayment();
                if (result.get("verifyResult") != null) {

                    //System.out.println(result);
                    String query = "INSERT INTO `payments`"
                            + " (`student_sno`,`teacher_tno`,`subject_sub_no`,`value`,`date_time`)"
                            + " VALUES ('" + result.get("sno") + "','" + result.get("tno") + "','" + result.get("sub_no") + "','" + result.get("value") + "','" + result.get("date") + "')";
                    MySQL.execute(query);

                    refreshClassTable();

                }

            } catch (Exception e) {
                e.printStackTrace();;
            }

        } else {
            JOptionPane.showMessageDialog(this, "Unable to create new account with having a selected row. Please Refresh", "Warning", JOptionPane.ERROR_MESSAGE);
            jButton34.requestFocus();
        }

    }//GEN-LAST:event_jButton26ActionPerformed

    //Payment Delete
    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed

        int selectedRow = jTable6.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {
            String payment_id = String.valueOf(jTable6.getValueAt(selectedRow, 0));

            try {

                String query = "DELETE FROM `payments` WHERE `class_no`='" + payment_id + "'";

                MySQL.execute(query);

                refreshPaymentTable();

                JOptionPane.showMessageDialog(this, "Deleted Payment " + payment_id, "Delete Success", JOptionPane.PLAIN_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_jButton27ActionPerformed

    //Payment Update
    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed

        int selectedRow = jTable6.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {

            try {

                String payment_id = jTextField18.getText();

                HashMap result = verifyPayment();
                if (result.get("verifyResult") != null) {

                    String query = "UPDATE `payments` SET "
                            + "`student_sno`='" + result.get("sno") + "', `teacher_tno`='" + result.get("tno") + "', "
                            + "`subject_sub_no`='" + result.get("sub_no") + "', `value`='" + result.get("value") + "', "
                            + "`date_time`='" + result.get("date") + "' WHERE `payment_id`='" + payment_id + "'";

                    MySQL.execute(query);

                    refreshPaymentTable();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton28ActionPerformed

    //Student Search
    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        String sno = jTextField7.getText();
        if (sno.isEmpty()) {
            System.out.println("Empty");
        } else {
            loadStudents(true, false, sno);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        refreshStudentTable();
    }//GEN-LAST:event_jButton30ActionPerformed

    //Student Table
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        try {
            if (evt.getClickCount() == 2) {

                jTable1.setEnabled(false);
                jButton8.setEnabled(false);
                jButton9.setEnabled(true);
                jButton10.setEnabled(true);

                int selectedRow = jTable1.getSelectedRow();

                String sno = String.valueOf(jTable1.getValueAt(selectedRow, 0));
                jTextField7.setText(sno);
                jTextField7.setEnabled(false);
                jButton11.setEnabled(false);

                String fName = String.valueOf(jTable1.getValueAt(selectedRow, 1));
                jTextField1.setText(fName);

                String lName = String.valueOf(jTable1.getValueAt(selectedRow, 2));
                jTextField2.setText(lName);

                String mobile = String.valueOf(jTable1.getValueAt(selectedRow, 3));
                jTextField4.setText(mobile);

                String gender = String.valueOf(jTable1.getValueAt(selectedRow, 5));

                if (gender.equals("Male")) {
                    jRadioButton1.setSelected(true);
                }
                if (gender.equals("Female")) {
                    jRadioButton2.setSelected(true);
                }

                ResultSet result = loadStudents(true, true, sno);
                if (result.next()) {
                    jTextField3.setText(result.getString("email"));
                    jTextField5.setText(result.getString("address"));
                    jPasswordField1.setText(result.getString("password"));
                    jDateChooser1.setDate(result.getDate("dob"));
                } else {
                    JOptionPane.showMessageDialog(this, "Something went wrong: No Result Set", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jTable1MouseClicked

    //Teacher Table
    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        try {
            if (evt.getClickCount() == 2) {

                jTable2.setEnabled(false);
                jButton14.setEnabled(false);
                jButton15.setEnabled(true);
                jButton16.setEnabled(true);

                int selectedRow = jTable2.getSelectedRow();

                String tno = String.valueOf(jTable2.getValueAt(selectedRow, 0));
                jTextField8.setText(tno);
                jTextField8.setEnabled(false);
                jButton12.setEnabled(false);

                String fName = String.valueOf(jTable2.getValueAt(selectedRow, 1));
                jTextField9.setText(fName);

                String lName = String.valueOf(jTable2.getValueAt(selectedRow, 2));
                jTextField10.setText(lName);

                String email = String.valueOf(jTable2.getValueAt(selectedRow, 3));
                jTextField11.setText(email);

                String subject = String.valueOf(jTable2.getValueAt(selectedRow, 5));
                jComboBox1.setSelectedItem(subject);

                String gender = String.valueOf(jTable2.getValueAt(selectedRow, 4));

                if (gender.equals("Male")) {
                    jRadioButton3.setSelected(true);
                }
                if (gender.equals("Female")) {
                    jRadioButton4.setSelected(true);
                }

                ResultSet result = loadTeachers(true, true, tno);
                if (result.next()) {
                    jTextField12.setText(result.getString("address"));
                    jPasswordField2.setText(result.getString("password"));
                } else {
                    JOptionPane.showMessageDialog(this, "Something went wrong: No Result Set", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        refreshTeacherTable();
    }//GEN-LAST:event_jButton31ActionPerformed

    //Teacher Search
    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        String tno = jTextField8.getText();
        if (tno.isEmpty()) {
            System.out.println("Empty");
        } else {
            loadTeachers(true, false, tno);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    //Subject Teacher Subject Change
    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed

        int selectedRow = jTable4.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {

            try {

                //String tno = String.valueOf(jTable4.getValueAt(selectedRow, 0));
                String tno = jLabel54.getText();

                String subject = String.valueOf(jComboBox2.getSelectedItem());

                HashMap<String, Integer> map = loadSubjectsComboBox(true, 0);
                String subjectId = String.valueOf(map.get(subject));

                String query = "UPDATE `teacher` SET "
                        + "`subject_sub_no`='" + subjectId + "' WHERE `tno`='" + tno + "'";

                MySQL.execute(query);

                refreshSubjectTables();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        refreshSubjectTables();
    }//GEN-LAST:event_jButton33ActionPerformed

    //Subject Table
    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked

        if (evt.getClickCount() == 2) {

            jLabel33.setText(""); //Teacher Name
            jLabel54.setText(""); //Teacher No

            jTable3.setEnabled(false); //subject
            jTable4.setEnabled(true); //subject teachers
            jButton18.setEnabled(false); //add
            jButton19.setEnabled(true); //delete
            jButton20.setEnabled(true); //update

            int selectedRow = jTable3.getSelectedRow();

            String sub_no = String.valueOf(jTable3.getValueAt(selectedRow, 0));
            jTextField6.setText(sub_no);
            jTextField6.setEnabled(false);
            jButton32.setEnabled(false);

            String name = String.valueOf(jTable3.getValueAt(selectedRow, 1));
            jTextField14.setText(name);

            String description = String.valueOf(jTable3.getValueAt(selectedRow, 2));
            jTextField16.setText(description);

            String price = String.valueOf(jTable3.getValueAt(selectedRow, 3));
            jTextField15.setText(price);

            jComboBox2.setSelectedItem(name);

            jLabel27.setText(String.valueOf(loadSubjectTeachers(sub_no)));

        }

    }//GEN-LAST:event_jTable3MouseClicked

    //Subject Teachers Table
    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked

        if (evt.getClickCount() == 2) {

            jTable4.setEnabled(false); //subject teachers

            int selectedRow = jTable4.getSelectedRow();

            String tno = String.valueOf(jTable4.getValueAt(selectedRow, 0));
            jLabel54.setText(tno); //Teacher No

            String name = String.valueOf(jTable4.getValueAt(selectedRow, 1)) + " " + String.valueOf(jTable4.getValueAt(selectedRow, 2));
            jLabel33.setText(name); //Teacher Name

        }

    }//GEN-LAST:event_jTable4MouseClicked

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
    }//GEN-LAST:event_jComboBox2ActionPerformed

    //Subject Search
    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        String sub_no = jTextField6.getText();
        if (sub_no.isEmpty()) {
            System.out.println("Empty");
        } else {
            loadSubjects(true, sub_no);
        }
    }//GEN-LAST:event_jButton32ActionPerformed

    //Class Table
    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseClicked

        try {
            if (evt.getClickCount() == 2) {

                jTable5.setEnabled(false);
                jButton22.setEnabled(false);
                jButton23.setEnabled(true);
                jButton24.setEnabled(true);

                int selectedRow = jTable5.getSelectedRow();

                String class_no = String.valueOf(jTable5.getValueAt(selectedRow, 0));
                jTextField13.setText(class_no);
                jTextField13.setEnabled(false);
                jButton17.setEnabled(false);

                String cName = String.valueOf(jTable5.getValueAt(selectedRow, 1));
                jTextField17.setText(cName);

                String subject = String.valueOf(jTable5.getValueAt(selectedRow, 2));
                jComboBox3.setSelectedItem(subject);

                String teacher = String.valueOf(jTable5.getValueAt(selectedRow, 3));
                jComboBox4.setSelectedItem(teacher);

                String time = String.valueOf(jTable5.getValueAt(selectedRow, 4));
                jComboBox5.setSelectedItem(time);

                ResultSet result = loadClasses(true, true, class_no);
                if (result.next()) {
                    jDateChooser2.setDate(result.getDate("date"));
                } else {
                    JOptionPane.showMessageDialog(this, "Something went wrong: No Result Set", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jTable5MouseClicked

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        refreshClassTable();
    }//GEN-LAST:event_jButton34ActionPerformed

    //Class Search
    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        String class_no = jTextField13.getText();
        if (class_no.isEmpty()) {
            System.out.println("Empty");
        } else {
            loadClasses(true, false, class_no);
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    //Payment Table
    private void jTable6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable6MouseClicked

        try {
            if (evt.getClickCount() == 2) {

                jTable6.setEnabled(false);
                jButton26.setEnabled(false);
                jButton27.setEnabled(true);
                jButton28.setEnabled(true);

                int selectedRow = jTable6.getSelectedRow();

                String payment_id = String.valueOf(jTable6.getValueAt(selectedRow, 0));
                jTextField18.setText(payment_id);
                jTextField18.setEnabled(false);
                jButton25.setEnabled(false);

                String sno = String.valueOf(jTable6.getValueAt(selectedRow, 1));
                jComboBox8.setSelectedItem(sno);
                jComboBox8.setEnabled(false);

                String student = String.valueOf(jTable6.getValueAt(selectedRow, 2));
                jLabel47.setText(student);

                String subject = String.valueOf(jTable6.getValueAt(selectedRow, 3));
                jComboBox6.setSelectedItem(subject);

                String teacher = String.valueOf(jTable6.getValueAt(selectedRow, 4));
                jComboBox7.setSelectedItem(teacher);

                String value = String.valueOf(jTable6.getValueAt(selectedRow, 5));
                jTextField19.setText(value);

                ResultSet result = loadPayments(true, true, payment_id);
                if (result.next()) {
                    jLabel49.setText(result.getString("subject.price"));
                } else {
                    JOptionPane.showMessageDialog(this, "Something went wrong: No Result Set", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jTable6MouseClicked

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        refreshPaymentTable();
    }//GEN-LAST:event_jButton35ActionPerformed

    //Payment set Name
    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        int sno = Integer.parseInt((String) jComboBox8.getSelectedItem());

        HashMap<Integer, String> map = loadStudentsComboBox(true, 0);

        String name = String.valueOf(map.get(sno));
        jLabel47.setText(name);
    }//GEN-LAST:event_jComboBox8ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel icon;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabe8;
    private javax.swing.JLabel jLabe9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
