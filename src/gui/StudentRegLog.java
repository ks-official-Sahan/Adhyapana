package gui;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import java.awt.Image;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.MySQL;

public class StudentRegLog extends javax.swing.JFrame {

    public StudentRegLog() {
        initComponents();
        viewLogin();
    }

    public void reset() {
            jTextField1.setText("");
            jTextField2.setText("");
            jTextField3.setText("");
            jTextField4.setText("");
            jPasswordField1.setText("");
            jPasswordField2.setText("");
            genderGroup.clearSelection();
            jDateChooser1.setDate(new Date());
            jTextField5.setText("");        
    }
    
    private HashMap verify(boolean Register) throws Exception {

        String result = null;

        if (Register) {

            HashMap<String, String> registerMap = new HashMap();

            String fname = jTextField1.getText();
            String lname = jTextField2.getText();
            String email = jTextField3.getText();
            String mobile = jTextField4.getText();
            String password1 = String.valueOf(jPasswordField1.getPassword());
            String password2 = String.valueOf(jPasswordField2.getPassword());
            ButtonModel genderSelection = genderGroup.getSelection();
            Date date = jDateChooser1.getDate();
            String address = jTextField5.getText();

            if (fname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your first name", "Warning", JOptionPane.ERROR_MESSAGE);
                jTextField1.requestFocus();
            } else if (lname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your last name", "Warning", JOptionPane.ERROR_MESSAGE);
                jTextField2.requestFocus();
            } else if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your email", "Warning", JOptionPane.ERROR_MESSAGE);
                jTextField3.requestFocus();
            } else if (mobile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your mobile", "Warning", JOptionPane.ERROR_MESSAGE);
                jTextField4.requestFocus();
            } else if (mobile.length() < 10 || mobile.length() > 10) {
                JOptionPane.showMessageDialog(this, "Mobile Number should contain 10 numbers", "Warning", JOptionPane.ERROR_MESSAGE);
                jTextField4.requestFocus();
            } else if (password1.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your password", "Warning", JOptionPane.ERROR_MESSAGE);
                jPasswordField1.requestFocus();
            } else if (password1.length() < 6 || password1.length() > 40) {
                JOptionPane.showMessageDialog(this, "Password should contain 6 to 40 characters", "Warning", JOptionPane.ERROR_MESSAGE);
                jPasswordField1.requestFocus();
            } else if (password2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your confirm password", "Warning", JOptionPane.ERROR_MESSAGE);
                jPasswordField2.requestFocus();
            } else if (!(password1.equals(password2))) {
                JOptionPane.showMessageDialog(this, "Entered passwords does not match correctly.", "Warning", JOptionPane.ERROR_MESSAGE);
                jPasswordField1.requestFocus();
            } else if (genderSelection == null) {
                JOptionPane.showMessageDialog(this, "Select your gender", "Warning", JOptionPane.ERROR_MESSAGE);
                jRadioButton1.requestFocus();
            } else if (date == null) {
                JOptionPane.showMessageDialog(this, "Enter your Date of Birth", "Warning", JOptionPane.ERROR_MESSAGE);
                jDateChooser1.requestFocus();
            } else if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your Home Address", "Warning", JOptionPane.ERROR_MESSAGE);
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

        } else {

            HashMap<String, String> loginMap = new HashMap();

            String email = jTextField6.getText();
            String password = String.valueOf(jPasswordField3.getPassword());

            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your email", "Warning", JOptionPane.ERROR_MESSAGE);
                jTextField6.requestFocus();
            } else {

                String query = "SELECT * FROM `student` INNER JOIN `gender` ON `student`.`g_gender_id`=`gender`.`gender_id` WHERE `email`='" + email + "'";
                ResultSet resultSet = MySQL.execute(query);

                if (resultSet.next()) {

                    if (password.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Enter your password", "Warning", JOptionPane.ERROR_MESSAGE);
                        jPasswordField3.requestFocus();
                    } else {

                        String userPassword = resultSet.getString("password");

                        if (!(password.equals(userPassword))) {
                            JOptionPane.showMessageDialog(this, "Incorrect password", "Warning", JOptionPane.ERROR_MESSAGE);
                            jPasswordField3.requestFocus();
                        } else {
                            result = "Success";

                            loginMap.put("email", email);
                            loginMap.put("sno", resultSet.getString("sno"));
                            loginMap.put("dob", resultSet.getString("dob"));
                            loginMap.put("firstName", resultSet.getString("first_name"));
                            loginMap.put("lastName", resultSet.getString("last_name"));
                            loginMap.put("mobile", resultSet.getString("mobile"));
                            loginMap.put("address", resultSet.getString("address"));
                            loginMap.put("gender", resultSet.getString("gender.type"));
                            loginMap.put("genderId", resultSet.getString("g_gender_id"));
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Invalid email. Check again or Register to continue", "Warning", JOptionPane.ERROR_MESSAGE);
                    jTextField6.requestFocus();
                }

            }

            loginMap.put("verifyResult", result);
            return loginMap;

        }

    }

    private void ImageScaling() {
        ImageIcon icon1;
        javax.swing.JLabel label;
        if (jPanel3.isVisible()) {
            icon1 = new ImageIcon(getClass().getResource("/sources/bg5.jpg"));
            label = jLabel17;
//            jPanel3.remove(label);
//            jPanel3.add(label, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, jPanel2.getWidth(), jPanel2.getHeight()));
        } else {
            icon1 = new ImageIcon(getClass().getResource("/sources/bg4.jpg"));
            label = jLabel18;
//            jPanel4.remove(label);
//            jPanel4.add(label, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, jPanel2.getWidth(), jPanel2.getHeight()));
        }
        Image image1 = icon1.getImage().getScaledInstance(jPanel2.getWidth(), jPanel2.getHeight(), Image.SCALE_SMOOTH);
        icon1 = new ImageIcon(image1);
//        label.setMinimumSize(new java.awt.Dimension(jPanel2.getWidth(), jPanel2.getHeight()));
//        label.setPreferredSize(new java.awt.Dimension(jPanel2.getWidth(), jPanel2.getHeight()));
        label.setIcon(icon1);

        ImageIcon icon2 = new ImageIcon(getClass().getResource("/sources/icon.png"));
        Image image2 = icon2.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icon2 = new ImageIcon(image2);
        jLabel1.setIcon(icon2);
    }

    public void panelScaling() {
        ImageScaling();
    }

    public void viewLogin() {
        jPanel3.setVisible(false);
        jPanel4.setVisible(true);
        panelScaling();
    }

    public void viewRegister() {
        jPanel3.setVisible(true);
        jPanel4.setVisible(false);
        panelScaling();
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
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();
        jButton2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel12 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jPasswordField3 = new javax.swing.JPasswordField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(236, 234, 189));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 3, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(244, 187, 39));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("  Adhypana Institute");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(153, 153, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Student Registration");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 500, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 204, 0));
        jLabel3.setText("First Name");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 230, -1));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel3.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 69, 230, 28));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 204, 0));
        jLabel4.setText("Last Name");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, 230, -1));
        jPanel3.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 69, 230, 28));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 204, 0));
        jLabel5.setText("Email");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, 480, -1));
        jPanel3.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 480, 28));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 204, 0));
        jLabel6.setText("Mobile");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 170, 480, -1));

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jPanel3.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 190, 480, 28));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 204, 0));
        jLabel7.setText("Password");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, 230, -1));

        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });
        jPanel3.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 250, 200, 28));

        jButton1.setText("Show");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton1MouseReleased(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 250, 30, 28));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 204, 0));
        jLabel8.setText("Confirm Password");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 230, 230, -1));
        jPanel3.add(jPasswordField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 250, 200, 28));

        jButton2.setText("Show");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton2MouseReleased(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 250, 30, 28));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 204, 0));
        jLabel9.setText("Date of Birth");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 290, 230, -1));
        jPanel3.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 310, 230, 28));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 204, 0));
        jLabel10.setText("Gender");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 290, 230, -1));

        genderGroup.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jRadioButton1.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton1.setText("Male");
        jRadioButton1.setActionCommand("1");
        jPanel3.add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 310, -1, 28));

        genderGroup.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jRadioButton2.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton2.setText("Female");
        jRadioButton2.setActionCommand("2");
        jPanel3.add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 310, -1, 28));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 204, 0));
        jLabel12.setText("Home Address");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 350, 480, -1));

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jPanel3.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 370, 480, 28));

        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(46, 119, 229));
        jButton3.setText("Register");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, 400, 32));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 102, 102));
        jLabel11.setText("Already Registered ?");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 470, -1, -1));

        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 204, 204));
        jButton4.setText("Student Login");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 490, 480, 32));

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setMinimumSize(new java.awt.Dimension(747, 485));
        jLabel17.setPreferredSize(new java.awt.Dimension(747, 485));
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 560));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 560));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Student Login");
        jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 6, 900, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Email");
        jPanel4.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 480, -1));

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 480, 31));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Password");
        jPanel4.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 150, 480, -1));
        jPanel4.add(jPasswordField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 170, 450, 31));

        jButton5.setText("Show");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton5MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton5MouseReleased(evt);
            }
        });
        jPanel4.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 170, 31, 31));

        jButton6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(46, 119, 229));
        jButton6.setText("Login");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 230, 400, 32));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 102, 102));
        jLabel16.setText("Create New Account");
        jPanel4.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 470, 480, -1));

        jButton7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 204, 204));
        jButton7.setText("Student Registration");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 490, 480, 33));

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setMinimumSize(new java.awt.Dimension(747, 485));
        jLabel18.setPreferredSize(new java.awt.Dimension(747, 485));
        jPanel4.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 560));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 560));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //Register

        try {

            HashMap result = verify(true);
            if (result.get("verifyResult") != null) {

                //System.out.println(result);
                String query = "INSERT INTO `student`"
                        + " (`first_name`,`last_name`,`address`,`dob`,`email`,`mobile`,`password`,`g_gender_id`)"
                        + " VALUES ('" + result.get("firstName") + "','" + result.get("lastName") + "','" + result.get("address") + "','" + result.get("dob") + "','" + result.get("email") + "','" + result.get("mobile") + "','" + result.get("password") + "','" + result.get("genderId") + "')";
                MySQL.execute(query);

                jTextField6.setText((String) result.get("mobile"));

                viewLogin();
                reset();

            }

        } catch (Exception e) {
            e.printStackTrace();;
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //Student Login
        viewLogin();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        panelScaling();
    }//GEN-LAST:event_formComponentResized

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        //Login

        try {
            HashMap result = verify(false);
            if (result.get("verifyResult") != null) {
                //System.out.println(result);

                StudentHome stHome = new StudentHome(result);
                stHome.setVisible(true);
                this.dispose();
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //Student Registration
        viewRegister();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
        jPasswordField1.setEchoChar('\u0000');
    }//GEN-LAST:event_jButton1MousePressed

    private void jButton1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseReleased
        jPasswordField1.setEchoChar('\u2022');
    }//GEN-LAST:event_jButton1MouseReleased

    private void jButton2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MousePressed
        jPasswordField2.setEchoChar('\u0000');
    }//GEN-LAST:event_jButton2MousePressed

    private void jButton2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseReleased
        jPasswordField2.setEchoChar('\u2022');
    }//GEN-LAST:event_jButton2MouseReleased

    private void jButton5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MousePressed
        jPasswordField3.setEchoChar('\u0000');
    }//GEN-LAST:event_jButton5MousePressed

    private void jButton5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseReleased
        jPasswordField3.setEchoChar('\u2022');
    }//GEN-LAST:event_jButton5MouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        FlatArcDarkOrangeIJTheme.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StudentRegLog().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup genderGroup;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private com.toedter.calendar.JDateChooser jDateChooser1;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JPasswordField jPasswordField3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
