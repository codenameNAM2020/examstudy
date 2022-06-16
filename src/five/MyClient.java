package five;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.rmi.UnknownHostException;

public class MyClient extends JFrame {

    Socket socket = null;

    JTextField eng = new JTextField(10);
    JTextField kor = new JTextField(10);

    JButton searchBtn = new JButton("찾기");

    BufferedReader in = null;
    BufferedWriter out = null;

    public MyClient() {

        setTitle("클라이언트");

        Container c = getContentPane();
        c.setLayout(new FlowLayout());

        JLabel jl1 = new JLabel("영어");
        c.add(jl1);
        c.add(eng);

        JLabel jl2 = new JLabel("한글");
        c.add(jl2);
        c.add(kor);

        c.add(searchBtn);
        setSize(400, 100);
        setVisible(true);

        try {
            socket = new Socket("localhost", 9999);
            System.out.println("연결됨");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    out.write(eng.getText() + "\n");
                    out.flush();
                    String str = in.readLine();
                    kor.setText(str);
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        new MyClient();
    }
}
