import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Stack;
import javax.swing.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

class MyWEBBrowser{

    Stack<String> BackStack = new Stack<String>();
    Stack<String> FrontStack = new Stack<String>();
    String curpage = "http://www.baidu.com";

    public MyWEBBrowser(){
    }


    //显示收藏夹
    public  void showFavour(Shell shell, SelectionEvent selectionEvent){
        Menu menu = new Menu(shell,  SWT.POP_UP);
        MenuItem addItem = new MenuItem(menu, SWT.CASCADE);
        addItem.setText("add");
        addItem.setMenu(menu);
        menu.setLocation(selectionEvent.x, selectionEvent.y);
        menu.setVisible(true);
        shell.setMenu(menu);
    }


    public void start(){
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout(8, false));
        shell.setText("东方红101");

        //基本组件装载
        MyMenu mm = new MyMenu(shell);
        mm.setMenuBar();
        Button back = new Button(shell,SWT.NONE);
        back.setText("Back");
        Button forward = new Button(shell,SWT.NONE);
        forward.setText("Forward");
        Text url_input = new Text(shell, SWT.SINGLE|SWT.BORDER|SWT.BORDER_DOT);
        Button go = new Button(shell,SWT.NONE);
        go.setText("Browser");
        Button refresh = new Button(shell,SWT.NONE);
        refresh.setText("Refresh");
//        Button favourite = new Button(shell,SWT.NONE);
//        favourite.setText("Favor");
        Button download = new Button(shell,SWT.NONE);
        download.setText("Download");
        Button email = new Button(shell,SWT.NONE);
        email.setText("Email");
        Menu menu = new Menu(shell, SWT.BAR);
        Browser browser = new Browser(shell, SWT.NONE);


        //组件的布局
        //搜索栏布局
        GridData gd_url_input = new GridData();
        gd_url_input.widthHint = 950;
        url_input.setLayoutData(gd_url_input);
        //网页布局
        GridData gd_html = new GridData();
        gd_html.widthHint = 1400;
        gd_html.heightHint = 650;
        gd_html.horizontalSpan = 8;
        browser.setLayoutData(gd_html);

        //显示一个主页
        browser.setUrl(curpage);
        go.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
            }
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if(keyEvent.keyCode == 13)
                {
                    String newURL = url_input.getText();
                    int index = newURL.lastIndexOf(".") + 1;
                    String type = newURL.substring(index);
                    if(type.equals("jpg") || type.equals("exe"))
                    {
                        try {
                            MyDownload md = new MyDownload(newURL, "D://DownTest//","pic2");
                            md.downloadstart();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else if(newURL.startsWith("http://")||newURL.startsWith("https://") )
                        browser.setUrl(newURL);
                    else if(newURL.startsWith("www")||newURL.startsWith("localhost"))
                        browser.setUrl("http://" + newURL);
                    else
                        browser.setUrl("https://www.baidu.com/s?tn=25017023_5_dg&ch=1&ie=UTF-8&wd=" + newURL);
                    System.out.println(newURL);
                    //强制唤醒display，不然不能刷新页面
                    display.wake();
                }
            }
        });
        //按钮监听
        go.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                super.widgetSelected(selectionEvent);
                //根据输入，自动补全
                String newURL = url_input.getText();
                int index = newURL.lastIndexOf(".") + 1;
                String type = newURL.substring(index);
                if(type.equals("jpg") || type.equals("exe"))
                {
                    try {
                        MyDownload md = new MyDownload(newURL, "D://DownTest//","pic2");
                        md.downloadstart();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else if(newURL.startsWith("http://")||newURL.startsWith("https://") )
                    browser.setUrl(newURL);
                else if(newURL.startsWith("www")||newURL.startsWith("localhost"))
                    browser.setUrl("http://" + newURL);
                else
                    browser.setUrl("https://www.baidu.com/s?tn=25017023_5_dg&ch=1&ie=UTF-8&wd=" + newURL);
                System.out.println(newURL);
                //强制唤醒display，不然不能刷新页面
                display.wake();
                //browser.refresh();
            }
        });
        refresh.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                super.widgetSelected(selectionEvent);
                browser.refresh();
            }
        });
        back.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                super.widgetSelected(selectionEvent);
                browser.back();
            }
        });
        forward.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                super.widgetSelected(selectionEvent);
                browser.forward();
            }
        });
        download.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                super.widgetSelected(selectionEvent);
                try {
                    String url = "http://image.baidu.com/search/down?tn=download&ipn=dwnl&word=download&ie=utf8&fr=result&url=http%3A%2F%2Fwww.open-emr.org%2Fwiki%2Fimages%2Fd%2Fdc%2FTree-Layout.jpg&thumburl=http%3A%2F%2Fimg1.imgtn.bdimg.com%2Fit%2Fu%3D4099119089%2C241684320%26fm%3D15%26gp%3D0.jpg";
                    //MyDownload md = new MyDownload(url, "D://DownTest//","pic2");
                    MyDownload md = new MyDownload();
                    md.downloadstart();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        //超链接事件监听
        browser.addOpenWindowListener(new OpenWindowListener() {
            @Override
            public void open(WindowEvent event) {
                // TODO Auto-generated method stub
                final Shell she= new Shell(shell);
                final Browser brow= new Browser(she, SWT.NONE);
                event.browser= brow;
                event.display.asyncExec(new Runnable() {  //将事件用我的Browser打开
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String st= brow.getUrl();
                        browser.setUrl(st);
                        she.close();
                    }
                });
            }
        });
        //邮件
        email.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                super.widgetSelected(selectionEvent);
                EMAILTEST et = new EMAILTEST();
                et.start(display);
            }
        });


        shell.open();
        while(!shell.isDisposed())
        {
            if (display.readAndDispatch())
            {
                display.sleep();
            }
        }
            display.dispose();
        }

}

public class MySWTBrowser {
    public static void main(String[] args){
        MyWEBBrowser web = new MyWEBBrowser();
        web.start();
    }
}
