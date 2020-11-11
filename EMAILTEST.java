import Decoder.BASE64Encoder;
import java.io.*;
import java.net.Socket;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import static org.eclipse.swt.widgets.Display.getDefault;


class SendEmail2 {
    private Socket socket;
    private String text;
    private String receiver;
    private String subject;
    public SendEmail2(String receiver, String text, String subject){
        this.receiver = receiver;
        this.text = text;
        this.subject = subject;
    }
    public void start(){
        String sender = "llt_szu@163.com";
        //String password = "cABEUWDHJMAWUSQBU";
        String password = "JJBSMQQDYWYEIZXN";
        String user = new BASE64Encoder().encode(sender.substring(0, sender.indexOf("@")).getBytes());
        String pass = new BASE64Encoder().encode(password.getBytes());
        try {
            Socket socket = new Socket("smtp.163.com", 25);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            OutputStreamWriter osw= new OutputStreamWriter(outputStream, "gbk");
            PrintWriter writter = new PrintWriter(osw,true);

            System.out.println(reader.readLine());
            //HELO
            writter.println("HELO man");
            System.out.println(reader.readLine());
            //AUTH LOGIN
            writter.println("auth login");
            System.out.println(reader.readLine());
            writter.println(user);
            System.out.println(reader.readLine());
            writter.println(pass);
            System.out.println(reader.readLine());

            writter.println("mail from:<" + sender +">");
            System.out.println(reader.readLine());
            writter.println("rcpt to:<" + receiver +">");
            System.out.println(reader.readLine());

            //Set data
            writter.println("data");
            System.out.println(reader.readLine());
            writter.println("subject:"+subject);
            writter.println("from:" + sender);
            writter.println("to:" + receiver);
            writter.println("Content-Type: text/plain;charset=\"gb2312\"");
            writter.println();
            writter.println(text);
            writter.println(".");
            writter.println("");
            System.out.println(reader.readLine());

            //Say GoodBye
            writter.println("rset");
            System.out.println(reader.readLine());
            writter.println("quit");
            System.out.println(reader.readLine());

        } catch (IOException E) {
            E.printStackTrace();
        }
    }
}

public class EMAILTEST{
    //private DisPlayThread displayThread = null;

    public void start(Display display){

        //syncExec的函数作用是让dis所在线程视自己情况，找机会执行后面代码
        display.getDefault().syncExec( new Runnable() {
            public void run() {
                //        Display display = new Display();
                Shell shell = new Shell(display);
                shell.setText("发邮件");
                GridLayout GY = new GridLayout(2, false);
                GY.marginLeft = 10;

                shell.setLayout(GY);

                shell.setBounds(100, 100, 490, 490);
                //基本组件
                Label Reciver = new Label(shell, SWT.BORDER);
                Text reciver_input = new Text(shell, SWT.SINGLE | SWT.BORDER);
                Label Subject = new Label(shell, SWT.BORDER);
                Text subject_input = new Text(shell, SWT.SINGLE | SWT.BORDER);
                Label Text = new Label(shell, SWT.BORDER);
                Text text_input = new Text(shell, SWT.WRAP | SWT.BORDER);
                Button send = new Button(shell, SWT.NONE);

                //组件风格
                //文本标签
                GridData gd1 = new GridData();
                gd1.heightHint = 25;
                gd1.widthHint = 60;
                //收件人输入
                GridData gd2 = new GridData();
                gd2.heightHint = 25;
                gd2.widthHint = 350;
                //文本输入
                GridData gd3 = new GridData();
                gd3.heightHint = 270;
                gd3.widthHint = 350;
                GridData gd4 = new GridData();
                gd4.widthHint = 60;
                gd4.horizontalSpan = 2;
                gd4.horizontalAlignment = GridData.BEGINNING;
                gd4.horizontalIndent = 65;

                Reciver.setLayoutData(gd1);
                Text.setLayoutData(gd1);
                reciver_input.setLayoutData(gd2);
                subject_input.setLayoutData(gd2);
                text_input.setLayoutData(gd3);
                send.setLayoutData(gd4);

                send.setText("发送");
                Reciver.setText("收件人");
                Subject.setText("主题");
                Text.setText("内容");

                send.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent selectionEvent) {
                        super.widgetSelected(selectionEvent);
                        String name = reciver_input.getText();
                        String text = text_input.getText();
                        String subj = subject_input.getText();
                        SendEmail2 se = new SendEmail2(name, text,subj);
                        se.start();
                        send.setEnabled(false);
                        send.setText("已发送");
                        display.wake();
                    }
                });

                //界面显示+事件判断
                shell.open();
//                while(!shell.isDisposed())
//
//                {
//                    if (display.readAndDispatch()) {
//                        display.sleep();
//                    }
//                }
//                display.dispose();
            }
            });
        }
}

