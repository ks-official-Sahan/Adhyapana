package gui;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import java.awt.Image;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.MySQL;

public class TeacherLogReg extends javax.swing.JFrame {

    /**
     * Creates new form studentRegistration
     */
    private void ImageScaling() {

        ImageIcon ii = new ImageIcon(getClass().getResource("/sources/bg4.jpg"));
        Image image = (ii).getImage().getScaledInstance(img.getWidth(), img.getHeight(), Image.SCALE_SMOOTH);
        ii = new ImageIcon(image);
        img.setIcon(ii);
        img1.setIcon(ii);

    }

    private void ImageScaling2() {

        ImageIcon ii = new ImageIcon(getClass().getResource("/sources/icon.png"));
        Image image = (ii).getImage().getScaledInstance(logo.getWidth(), logo.getHeight(), Image.SCALE_SMOOTH);
        ii = new ImageIcon(image);
        logo.setIcon(ii);

    }

    private void ImageScaling3() {

        ImageIcon ii = new ImageIcon(getClass().getResource("/sources/icon.png"));
        Image image = (ii).getImage().getScaledInstance(logo2.getWidth(), logo2.getHeight(), Image.SCALE_SMOOTH);
        ii = new ImageIcon(image);
        logo2.setIcon(ii);

    }

    private void scaler() {
        ImageScaling();
        ImageScaling2();
        ImageScaling3();
    }

    public void reset() {

        jTextField2.setText("");
        jTextField4.setText("");
        jComboBox1.setSelectedItem("Select");
        jTextField6.setText("");
        jPasswordField2.setText("");

    }

    private void showLogin() {
        jPanel1.setVisible(true);
        jPanel2.setVisible(false);
    }

    private void showRegister() {
        jPanel1.setVisible(false);
        jPanel2.setVisible(true);
    }

    public TeacherLogReg() {
        initComponents();
        loadSubjects(false);
        scaler();
        showLogin();
    }

    private HashMap loadSubjects(boolean returnData) {

        try {
            String query = "SELECT * FROM `subject`";

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
            } else {
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                jComboBox1.setModel(model);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private HashMap verify(boolean Register) throws Exception {

        String result = null;

        if (Register) {

            HashMap<String, String> registerMap = new HashMap();

            String fname = jTextField2.getText();
            String lname = jTextField3.getText();
            String address = jTextField4.getText();
            String subject = String.valueOf(jComboBox1.getSelectedItem());
            String email = jTextField6.getText();
            String password = String.valueOf(jPasswordField2.getPassword());
            ButtonModel genderSelection = genderGroup.getSelection();

            if (fname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your First Name", "Warning", JOptionPane.ERROR_MESSAGE);
                jTextField2.requestFocus();
            } else if (lname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your Last Name", "Warning", JOptionPane.ERROR_MESSAGE);
                jTextField3.requestFocus();
            } else if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your Home Address", "Warning", JOptionPane.ERROR_MESSAGE);
                jTextField4.requestFocus();
            } else if (subject.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Select your Subject", "Warning", JOptionPane.ERROR_MESSAGE);
                jComboBox1.requestFocus();
            } else if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your Email", "Warning", JOptionPane.ERROR_MESSAGE);
                jTextField6.requestFocus();
            } else if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your Password", "Warning", JOptionPane.ERROR_MESSAGE);
                jPasswordField2.requestFocus();
            } else if (password.length() < 6 || password.length() > 40) {
                JOptionPane.showMessageDialog(this, "Password should contain 6 to 40 characters", "Warning", JOptionPane.ERROR_MESSAGE);
                jPasswordField2.requestFocus();
            } else if (genderSelection == null) {
                JOptionPane.showMessageDialog(this, "Select your Gender", "Warning", JOptionPane.ERROR_MESSAGE);
                jRadioButton1.requestFocus();
            } else {

                HashMap<String, Integer> map = loadSubjects(true);

                String subjectId = String.valueOf(map.get(subject));
                String gender = genderSelection.getActionCommand();

                registerMap.put("firstName", fname);
                registerMap.put("lastName", lname);
                registerMap.put("email", email);
                registerMap.put("password", password);
                registerMap.put("genderId", gender);
                registerMap.put("subjectId", subjectId);
                registerMap.put("subject", subject);
                registerMap.put("address", address);

                result = "Success";
                registerMap.put("verifyResult", result);
            }

            registerMap.put("verifyResult", result);
            return registerMap;

        } else {

            HashMap<String, String> loginMap = new HashMap();

            String email = jTextField1.getText();
            String password = String.valueOf(jPasswordField1.getPassword());

            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your email", "Warning", JOptionPane.ERROR_MESSAGE);
                jTextField1.requestFocus();
            } else {

                try {
                    String query = "SELECT * FROM `teacher` INNER JOIN `gender` ON `teacher`.`g_gender_id`=`gender`.`gender_id` INNER JOIN `subject` ON `teacher`.`subject_sub_no`=`subject`.`sub_no` WHERE `email`='" + email + "'";
                    ResultSet resultSet = MySQL.execute(query);

                    if (resultSet.next()) {

                        if (password.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Enter your password", "Warning", JOptionPane.ERROR_MESSAGE);
                            jPasswordField1.requestFocus();
                        } else {

                            String userPassword = resultSet.getString("password");

                            if (!(password.equals(userPassword))) {
                                JOptionPane.showMessageDialog(this, "Incorrect password", "Warning", JOptionPane.ERROR_MESSAGE);
                                jPasswordField1.requestFocus();
                            } else {
                                result = "Success";

                                loginMap.put("email", email);
                                loginMap.put("tno", resultSet.getString("tno"));
                                loginMap.put("firstName", resultSet.getString("first_name"));
                                loginMap.put("lastName", resultSet.getString("last_name"));
                                loginMap.put("address", resultSet.getString("address"));
                                loginMap.put("subject", resultSet.getString("subject.description"));
                                loginMap.put("sub_no", resultSet.getString("subject_sub_no"));
                                loginMap.put("gender", resultSet.getString("gender.type"));
                                loginMap.put("genderId", resultSet.getString("g_gender_id"));

                            }
                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid email. Check again or Register to continue", "Warning", JOptionPane.ERROR_MESSAGE);
                        jTextField1.requestFocus();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            loginMap.put("verifyResult", result);
            return loginMap;

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        genderGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        logo = new javax.swing.JLabel();
        img = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();
        jTextField3 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        logo2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        img1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 650));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 45)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 204, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Adhyapana Institute");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 460, 160));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 40)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 153, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Teacher Login");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 270, 310, 56));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 153, 255));
        jLabel3.setText("Email");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 350, 142, 33));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 153, 255));
        jLabel4.setText("Password");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 390, 142, 33));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 350, 311, 33));

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 153, 255));
        jButton1.setText("Log In");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 450, 457, 33));

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(51, 153, 255));
        jButton2.setText("New Teacher");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 510, 457, 33));
        jPanel1.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 390, 311, 33));

        logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/icon.png"))); // NOI18N
        jPanel1.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, 220, 220));

        img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/bg4.jpg"))); // NOI18N
        jPanel1.add(img, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 650));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 650));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 45)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 204, 51));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Adhyapana Institute");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 60, 400, 90));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 40)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 153, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Teacher Registration");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 200, 420, -1));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 153, 255));
        jLabel7.setText("First Name");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 260, 142, 28));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 153, 255));
        jLabel8.setText("Home Address");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 340, 142, 28));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 153, 255));
        jLabel9.setText("Subject");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 380, 142, 28));
        jPanel2.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 260, 311, 28));

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jPanel2.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 340, 311, 28));

        jComboBox1.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "select" }));
        jPanel2.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 380, 310, 28));

        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(51, 153, 255));
        jButton3.setText("Register");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 550, 457, 33));

        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(51, 153, 255));
        jButton4.setText("Go to Login");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 590, 457, 33));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 153, 255));
        jLabel10.setText("Email");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 420, 142, 28));
        jPanel2.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 420, 311, 28));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 153, 255));
        jLabel11.setText("Password");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 460, 142, 28));
        jPanel2.add(jPasswordField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 460, 311, 28));
        jPanel2.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 300, 311, 28));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 153, 255));
        jLabel13.setText("Last Name");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 300, 142, 28));

        logo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/icon.png"))); // NOI18N
        jPanel2.add(logo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 180, 180));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 153, 255));
        jLabel12.setText("Gender");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 500, 142, 30));

        genderGroup.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Times New Roman", 0, 15)); // NOI18N
        jRadioButton1.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton1.setText("Male");
        jRadioButton1.setActionCommand("1");
        jPanel2.add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 500, -1, 28));

        genderGroup.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Times New Roman", 0, 15)); // NOI18N
        jRadioButton2.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton2.setText("Female");
        jRadioButton2.setActionCommand("2");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 500, 110, 28));

        img1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/bg4.jpg"))); // NOI18N
        jPanel2.add(img1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 650));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //Login

        try {
            HashMap result = verify(false);
            if (result.get("verifyResult") != null) {
                //System.out.println(result);
                
                TeacherHome tHome = new TeacherHome(result);
                tHome.setVisible(true);
                this.dispose();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        showRegister();
        jTextField2.grabFocus();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        showLogin();
        jTextField1.grabFocus();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //Register

        try {

            HashMap result = verify(true);
            if (result.get("verifyResult") != null) {

                //System.out.println(result);
                String query = "INSERT INTO `teacher`"
                        + " (`first_name`,`last_name`,`address`,`email`,`password`,`g_gender_id`,`subject_sub_no`)"
                        + " VALUES ('" + result.get("firstName") + "','" + result.get("lastName") + "','" + result.get("address") + "','" + result.get("email") + "','" + result.get("password") + "','" + result.get("genderId") + "','" + result.get("subjectId") + "')";
                MySQL.execute(query);

                jTextField1.setText((String) result.get("email"));

                showLogin();
                reset();

            }
        } catch (Exception e) {
            e.printStackTrace();;
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        FlatArcDarkOrangeIJTheme.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TeacherLogReg().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup genderGroup;
    private javax.swing.JLabel img;
    private javax.swing.JLabel img1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel logo;
    private javax.swing.JLabel logo2;
    // End of variables declaration//GEN-END:variables
}
