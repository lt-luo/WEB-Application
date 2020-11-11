import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;

class ServerThread extends Thread{
    private Socket connection;
    private int flag = 0;
    ServerThread(Socket connection) {
        this.connection = connection;
    }
    public void run(){
        try {
            BufferedReader bd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String requestHeader;
            while ((requestHeader = bd.readLine()) != null && !requestHeader.isEmpty()) {
                System.out.println(requestHeader);

                //解析GET
                if (requestHeader.startsWith("GET")) {
                    int begin = requestHeader.indexOf("GET") + 5;
                    int end = requestHeader.indexOf("HTTP/");
                    String condition = requestHeader.substring(begin, end);
                    System.out.println("GET参数是：" + condition);

                    OutputStreamWriter ow = new OutputStreamWriter(connection.getOutputStream(), "gbk");
                    PrintWriter pw = new PrintWriter(ow);
                    pw.println("HTTP/1.1 200 OK");
                    pw.println("Content-type:text/html");
                    pw.println();
                    pw.println("<h1>Successful Connection</h1>");

                    if(condition.contains(".html"))
                    {
                        //System.out.println("yes");
                        File f = new File("D:" + File.separator + "JAVA互联网编程" + File.separator + "FinalExam" + File.separator + condition);
                        if(!f.exists())
                        {
                            //System.out.println("no file");
                            pw.println("no file");
                            pw.flush();
                        }
                        else{
                            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                            String line = "";
                            while((line = br.readLine()) != null){
                                //System.out.println(line);
                                pw.println(line);
                                pw.flush();
                            }
                            br.close();
                        }
                    }
                    pw.println();
                    //pw.println("<h2>GET: "+condition + "</h2");
                    pw.flush();
                    connection.close();
                }
                //解析POST
                if (requestHeader.startsWith("POST")) {
                    while((requestHeader = bd.readLine()) != null && !requestHeader.isEmpty())
                    {
                        System.out.println(requestHeader);
                    }
                    String post = bd.readLine();
                    System.out.println("post的信息："+post);
                    PrintWriter pw = new PrintWriter(connection.getOutputStream());
                    pw.println("HTTP/1.1 200 OK");
                    pw.println("Content-type:text/html");
                    pw.println();
                    pw.println("<h1>Successful POST</h1>");

                    pw.flush();
                    connection.close();
                    break;
                }
                //解析Head
                if(requestHeader.startsWith("HEAD"))
                {
                    PrintWriter pw = new PrintWriter(connection.getOutputStream());
                    pw.println("HTTP/1.1 200 OK");
                    pw.println("Content-type:text/html");
                    pw.println();
                    pw.println("<h1>Successful HEAD</h1>");
                    pw.flush();
                    connection.close();
                }
                //解析TRACE
                if(requestHeader.startsWith("TRACE"))
                {
                    PrintWriter pw = new PrintWriter(connection.getOutputStream());
                    pw.println(requestHeader);
                    while((requestHeader = bd.readLine()) != null && !requestHeader.isEmpty())
                    {
                        System.out.println(requestHeader);
                        pw.println(requestHeader);
                    }
                    bd.readLine();
                    pw.println("HTTP/1.1 200 OK");
                    pw.println("Content-type:text/html");
                    pw.println();
                    pw.println("<h1>Successful TRACE</h1>");

                    pw.flush();
                    connection.close();
                    break;
                }
                //解析DELETE,删除某个文件
                if(requestHeader.startsWith("DELETE"))
                {
                    int begin = requestHeader.indexOf("DELETE") + 8;
                    int end = requestHeader.indexOf("HTTP/");
                    String condition = requestHeader.substring(begin, end);
                    System.out.println("DELETE参数是：" + condition);

                    PrintWriter pw = new PrintWriter(connection.getOutputStream());
                    File f = new File("D:" + File.separator + "JAVA互联网编程" + File.separator + "FinalExam" + File.separator + condition);
                    if(!f.exists())
                    {
                        pw.println("no file");
                        System.out.println("No File!");
                    }
                    else
                    {
                        f.delete();
                    }
                    pw.println("HTTP/1.1 200 OK");
                    pw.println("Content-type:text/html");
                    pw.println();
                    pw.println("<h1>Successful DELETE</h1>");
                    //pw.println("<h2>GET: "+condition + "</h2");
                    pw.flush();
                    connection.close();
                }
                //解析PUT，接收某个文件
                if(requestHeader.startsWith("PUT"))
                {
                    int begin = requestHeader.indexOf("PUT") + 5;
                    int end = requestHeader.indexOf("HTTP/");
                    String condition = requestHeader.substring(begin, end);
                    System.out.println("PUT参数是：" + condition);

                    while((requestHeader = bd.readLine()) != null && !requestHeader.isEmpty())
                    {
                        System.out.println(requestHeader);
                    }
                    String html = "";

                    String fileName = condition;
                    File f = new File("D:" + File.separator + "JAVA互联网编程" + File.separator + "FinalExam" + File.separator + fileName);
                    if(!f.exists())
                    {
                        f.createNewFile();
                        //System.out.println("No file");
                    }
                    FileWriter fw = new FileWriter(f);
                    bd.readLine();
                    while((html = bd.readLine()) != null && !html.isEmpty())
                    {
                        //System.out.println(html);
                        fw.write(html);
                        fw.write("\r\n");
                    }
                    fw.flush();
                    fw.close();
                    PrintWriter pw = new PrintWriter(connection.getOutputStream());
                    pw.println("HTTP/1.1 200 OK");
                    pw.println("Content-type:text/html");
                    pw.println();
                    pw.println("<h1>Successful PUT</h1>");
                    //pw.println("<h2>GET: "+condition + "</h2");
                    pw.flush();
                    connection.close();
                    break;
                }
                if(requestHeader.startsWith("OPTIONS"))
                {
                    int begin = requestHeader.indexOf("OPTIONS") + 9;
                    int end = requestHeader.indexOf("HTTP/");
                    String condition = requestHeader.substring(begin, end);
                    System.out.println("OPTIONS参数是：" + condition);
                    int index = condition.lastIndexOf(".");
                    String type = condition.substring(index+1);
                    String support="NULL";
                    if(type.equals("html") || type.equals("jpg"))
                    {
                        support = "GET|PUT|DELETE|";
                    }
                    PrintWriter pw = new PrintWriter(connection.getOutputStream());
                    pw.println("HTTP/1.1 200 OK");
                    pw.println("Content-type:text/html");
                    pw.println("support options:" + support);
                    pw.println();
                    pw.println("<h1>Successful OPTIONS</h1>");
                    pw.flush();
                    connection.close();
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
public class WebServer3 {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8888);

            while (true) {
                 Socket socket = ss.accept();
                 ServerThread st = new ServerThread(socket);
                 st.run();
//                Socket socket = ss.accept();
//                BufferedReader bd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//                String requestHeader;
//                int contentLength = 0;
//                while ((requestHeader = bd.readLine()) != null && !requestHeader.isEmpty()) {
//                    System.out.println(requestHeader);
//
//                    //解析GET
//                    if (requestHeader.startsWith("GET")) {
//                        int begin = requestHeader.indexOf("GET") + 5;
//                        int end = requestHeader.indexOf("HTTP/");
//                        String condition = requestHeader.substring(begin, end);
//                        System.out.println("GET参数是：" + condition);
//
//                        OutputStreamWriter ow = new OutputStreamWriter(socket.getOutputStream(), "gbk");
//                        PrintWriter pw = new PrintWriter(ow);
//                        pw.println("HTTP/1.1 200 OK");
//                        pw.println("Content-type:text/html");
//                        pw.println();
//                        pw.println("<h1>Successful GET</h1>");
//                        //pw.println("<h2>GET: "+condition + "</h2");
//                        pw.flush();
//                        socket.close();
//
//                    }
//                    //解析POST
//                    if (requestHeader.startsWith("POST")) {
//                        while((requestHeader = bd.readLine()) != null && !requestHeader.isEmpty())
//                        {
//                            System.out.println(requestHeader);
//                        }
//                        String post = bd.readLine();
//
//                        System.out.println("post的信息："+post);
//
//                        PrintWriter pw = new PrintWriter(socket.getOutputStream());
//                        pw.println("HTTP/1.1 200 OK");
//                        pw.println("Content-type:text/html");
//                        pw.println();
//                        pw.println("<h1>Successful POST</h1>");
//
//                        pw.flush();
//                        socket.close();
//                    }
//                    //解析Head
//                    if(requestHeader.startsWith("HEAD"))
//                    {
//
//                    }
//                    if(requestHeader.startsWith("TRACE"))
//                    {
//                        PrintWriter pw = new PrintWriter(socket.getOutputStream());
//                        while((requestHeader = bd.readLine()) != null && !requestHeader.isEmpty())
//                        {
//                            System.out.println(requestHeader);
//                            pw.println(requestHeader);
//                        }
//                        bd.readLine();
//                        pw.println("HTTP/1.1 200 OK");
//                        pw.println("Content-type:text/html");
//                        pw.println();
//                        pw.println("<h1>Successful TRACE</h1>");
//
//                        pw.flush();
//                        socket.close();
//                    }
//                }
//            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
