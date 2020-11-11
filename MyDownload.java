import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class TheDownload extends Thread{
    HttpURLConnection conn;
    RandomAccessFile raf;
    public static int numOFcomp = 0;

    public TheDownload(HttpURLConnection conn, RandomAccessFile raf){
        this.conn = conn;
        this.raf = raf;
    }
    public void run(){
        try{
            InputStream is = conn.getInputStream();
            byte[] Byte = new byte[1024];
            int len;
            while((len = is.read(Byte)) != -1){
                raf.write(Byte, 0, len);
            }
            raf.close();
            numOFcomp ++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

public class MyDownload {
    String url;
    String savePath;
    String filename;

    JTextField URL;
    JTextField Path;
    JTextField Filename;

    JLabel JL1;
    JLabel JL2;
    JLabel JL3;
    JLabel JL4;

    JLabel bartex;
    JProgressBar bar;
    JButton Start;

    public static boolean []check;
    public MyDownload(){
        this.url = "";
        this.savePath = "";
        this.filename = "";
        this.URL = new JTextField(url,20);
        this.Path = new JTextField(savePath,20);
        this.Filename = new JTextField(filename,20);
    }

    public MyDownload(String url, String savePath, String filename){
        this.url = url;
        this.savePath = savePath;
        this.filename = filename;
        this.URL = new JTextField(url,20);
        this.Path = new JTextField(savePath,20);
        this.Filename = new JTextField(filename,20);
    }

    public void downloadstart() throws Exception{
        JL1 = new JLabel("目标文件");
        JL2 = new JLabel("保存路径");
        JL3 = new JLabel("文件名称");
        JL4 = new JLabel("进度条");
        bar = new JProgressBar(0, 100);

        Start = new JButton("下载");
        Start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Start.setEnabled(false);
                savePath = Path.getText();
                filename = Filename.getText();
                url = URL.getText();
                int num = 5;
                check = new boolean[num];
                ExecutorService pool = Executors.newFixedThreadPool(num);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                    long tatolSize = httpURLConnection.getContentLength();
                    long avgSize = tatolSize / num;
                    //分配线程工作量
                    for (int i = 0; i < num; i++) {
                        long begin = avgSize * i;
                        long end = avgSize * (i + 1);
                        if (i == num - 1)
                            end = tatolSize;
                        //这里必须要重新new一个，不然会有connection关闭的问题
                        httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                        httpURLConnection.setRequestProperty("RANGE", "bytes=" + String.valueOf(begin)+"-"+String.valueOf(end));
                        httpURLConnection.setRequestProperty("Accept", "image/gif,image/x-xbitmap,application/msword,*/*");
                        int index = url.lastIndexOf(".");
                        String type = url.substring(index);
                        RandomAccessFile raf = new RandomAccessFile(savePath + filename + type, "rw");
                        raf.seek(begin);
                        TheDownload d = new TheDownload(httpURLConnection, raf);
                        pool.submit(d);

                    }
                    pool.submit(new Thread(){
                       public void run(){
                            while(true){
                                System.out.println(TheDownload.numOFcomp);
                                if(TheDownload.numOFcomp == num - 1){
                                    Start.setText("下载完成！");
                                    break;
                                }
                            }
                       }
                    });
                    pool.shutdown();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        JPanel JP1 = new JPanel();
        JPanel JP2 = new JPanel();
        JPanel JP3 = new JPanel();
        JPanel JP4 = new JPanel();
        JP1.add(JL1);
        JP1.add(URL);
        JP2.add(JL2);
        JP2.add(Path);
        JP3.add(JL3);
        JP3.add(Filename);
        JP4.add(Start);
        JFrame jf = new JFrame("下载");
        jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jf.setLocation(200, 200);
        jf.setSize(300, 300);
        jf.setLayout(new GridLayout(4, 1));
        jf.add(JP1);
        jf.add(JP2);
        jf.add(JP3);
        jf.add(JP4);
//        jf.add(JL1);
//        jf.add(URL);
//        jf.add(JL2);
//        jf.add(Path);
//        jf.add(JL3);
//        jf.add(Filename);
//        jf.add(JL4);
//        jf.add(bar);
//        jf.add(Start);

        jf.setVisible(true);
    }

//    public static void main(String[] args){
//        String url = "http://image.baidu.com/search/down?tn=download&ipn=dwnl&word=download&ie=utf8&fr=result&url=http%3A%2F%2Fwww.open-emr.org%2Fwiki%2Fimages%2Fd%2Fdc%2FTree-Layout.jpg&thumburl=http%3A%2F%2Fimg1.imgtn.bdimg.com%2Fit%2Fu%3D4099119089%2C241684320%26fm%3D15%26gp%3D0.jpg";
//        MyDownload md = new MyDownload(url, "D://DownTest//","pic2");
//        md.start();
//    }
}
