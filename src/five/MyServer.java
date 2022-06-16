package five;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.*;

public class MyServer extends JFrame {

    JTextField eng = new JTextField(10);
    JTextField kor = new JTextField(10);

    JButton saveBtn = new JButton("저장");
    JButton searchBtn = new JButton("찾기");

    JTextArea ta = new JTextArea(7, 25);

    HashMap<String, String> dic = new HashMap<String, String>();

    public MyServer() {
        setTitle("서버");
        setSize(350, 350);
        Container c = getContentPane();
        c.setLayout(new FlowLayout());

        JLabel jl1 = new JLabel("영어");
        c.add(jl1);
        c.add(eng);

        JLabel jl2 = new JLabel("한글");
        c.add(jl2);
        c.add(kor);

        c.add(saveBtn);
        c.add(searchBtn);
        c.add(new JScrollPane(ta));

        setSize(350, 270);
        setVisible(true);

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ta.append("저장: " + eng.getText() + ", " + kor.getText() + "\n");
                dic.put(eng.getText(), kor.getText());
            }
        });

        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = dic.get(eng.getText());
                if (str == null) {
                    str = "없음";
                }
                kor.setText(str);
            }
        });

        ServerThread server = new ServerThread();
        server.start();
    }

    class ServerThread extends Thread {

        @Override
        public void run() {
            ServerSocket listener = null;
            Socket socket = null;

            try {
                listener = new ServerSocket(9999);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return;
            }

            while (true) {
                try {
                    socket = listener.accept();
                    ServiceThread th = new ServiceThread(socket);
                    th.start();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }
        }
    }

    class ServiceThread extends Thread {

        Socket socket;
        BufferedReader in;
        BufferedWriter out;

        public ServiceThread(Socket socket) {
            this.socket = socket;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return;
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String eng = in.readLine();
                    String kors = dic.get(eng);

                    if (kors == null) {
                        kors = "없음";
                    }

                    out.write(kors + "\n");
                    out.flush();

                    ta.append("검색" + eng + "\n");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    public static void main(String[] args) {
        new MyServer();
    }
}
