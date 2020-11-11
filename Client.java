import java.net.*;
import java.io.*;
import java.util.Scanner;

class ClientTHread extends Thread{
    private String method;
    private String type;
    private String hostname;
    private int port;
    public ClientTHread(String method, String type, String hostname, int port){
        this.method = method;
        this.type = type;
        this.hostname = hostname;
        this.port = port;
    }
    public String GetHead(){
        String head = "";
        if(method.equals("GET") || method.equals("DELETE") || method.equals("PUT") || method.equals("OPTIONS")){
            if(type.equals("jpg"))
                head += method + " /pic.jpg HTTP/1.1"+"\r\n";
            else
                head += method + " /index.html HTTP/1.1" + "\r\n";
        }
        else if(method.equals("POST")){
            head += "POST /index.html HTTP/1.1"+ "\r\n";
            head += "Content-Length:112" +"\r\n";
        }
        else if(method.equals("HEAD")){
            head += "HEAD /index.html HTTP/1.1"+ "\r\n";
        }
        else{
            head += method + " / HTTP1.1"+ "\r\n";
        }
        head += "Accept: txt/jpg" + "\r\n";
        head += "Host: localhost:8080" + "\r\n";
        head += "Connection: Close" + "\r\n";
        //head += "\r\n";

        return head;
    }
    public void run(){
        String head = GetHead();
        try{
            Socket socket = new Socket(hostname, port);
            OutputStream os = socket.getOutputStream();
            boolean autoflush = true;
            PrintWriter out = new PrintWriter( socket.getOutputStream(), autoflush);
            BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ));

            out.print(head);
            out.println();
            if(method.equals("POST"))
            {
                out.println("I'm POST");
            }
            out.println();
            out.flush();
            if(method.equals("PUT"))
            {
                FileReader fr = new FileReader(".//index2.html");
                BufferedReader br = new BufferedReader(fr);
                String str = null;
                while((str = br.readLine()) != null) {
                    //System.out.println(str);
                    // 每读一行，写一行
                    out.println(str);
                    // 获取当前OS的换行符并写入
                    //bw.write(System.getProperty("line.separator"));
                }
                out.println();
                out.flush();
            }

            boolean loop = true;
            StringBuffer sb = new StringBuffer(8096);

            while (loop) {
                if ( in.ready() ) {
                    int i=0;
                    while (i!=-1) {
                        i = in.read();
                        sb.append((char) i);
                    }
                    loop = false;
                }
                try {
                    Thread.currentThread().sleep(50);
                }catch (InterruptedException i){
                    i.printStackTrace();
                }

            }
            System.out.println(sb.toString());
            socket.close();

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}

public class Client {
    public static void main(String[] args) throws Exception{
            String hostname = "127.0.0.1";
            String Filename = "";
            int port = 8888;
            ClientTHread ch = new ClientTHread("TRACE", "html", hostname, port);
            ch.start();
        }
}
