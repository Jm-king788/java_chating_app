
package chat_app;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;  
import java.net.*; 
import javax.swing.*;


class client extends JFrame
{
 Socket  socket;   
 BufferedReader read,reading;
 PrintWriter write;
 
 private JLabel heading= new JLabel("Client");
 private JTextField messageInput = new JTextField();
 private JTextArea messageArea = new JTextArea();
 private Font font = new Font("Roboto",Font.PLAIN,20);
 public client()
 {
  try{
          System.out.println("sending");
          socket = new Socket("localhost",6666);
          System.out.println("connection done");
          read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          write= new PrintWriter(socket.getOutputStream());
           createGUI();
           handleEvent();
          start_reading();
//          start_writing();
  }
  catch(Exception e)
  {
  System.out.println(e);
  }
 }
 private void handleEvent()
 {
 messageInput.addKeyListener(new KeyListener() {
    @Override
    public void keyPressed(KeyEvent e) {
        // This method is called when a key is pressed
//        System.out.println("Key Pressed: " + e.getKeyCode());
          if(e.getKeyCode() == 10)
          {
          String messageContent = messageInput.getText();
          messageArea.append("Me :"+messageContent+"\n");
          write.println(messageContent);
          write.flush();
          messageInput.setText("");
          }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // This method is called when a key is released
//        System.out.println("Key Released: " + e.getKeyChar());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // This method is called when a key is typed (pressed and released)
//        System.out.println("Key Typed: " + e.getKeyCode());
    }
});

 }
 private void createGUI()
 {
  this.setTitle("Client");
  this.setSize(600,600);
  this.setLocationRelativeTo(null);
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//  Component
  heading.setFont(font);
  messageInput.setFont(font);
  messageArea.setFont(font);
  messageArea.setEditable(false);
  heading.setHorizontalAlignment(SwingConstants.CENTER);
  heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
 
  
// Layout 
 
this.setLayout(new BorderLayout());
this.add(heading,BorderLayout.NORTH);
JScrollPane Jscroll = new JScrollPane(messageArea);
this.add(Jscroll,BorderLayout.CENTER);
this.add(messageInput,BorderLayout.SOUTH);
  this.setVisible(true);
 }
 
 
 
 
   public  void start_reading()
    { //Thread for  reading data
       Runnable r1=()->{  
           try{
           while(!socket.isClosed()){
             
                // Data  receving
               String msg = read.readLine();
               if(msg.equals("exit"))
               {
                System.out.println("Connection close");
                JOptionPane.showMessageDialog(this, "Connection closed");
                messageArea.setEnabled(false);
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                socket.close();
                break;
               }
//               System.out.println("Receved " +msg);
                messageArea.append(msg+" \n");
           }
       
        
       } catch(Exception e)
       {System.out.println(e);      
       }
               };
       new Thread(r1).start();
      
    
    }
    
    public  void start_writing()
    {
        Runnable r2=()->
        {
            System.out.println("Writer started");
            try{
            while(!socket.isClosed())
            {
                 // Data  sending   
                  reading = new BufferedReader(new InputStreamReader(System.in));
                 String content = reading.readLine();
                 write.println(content);
                 write.flush();
                 if(content.equals("exit"))
                 {
                     socket.close();
                     break;
                 }
               
                         
                               
            }
            }catch(Exception e)
                {
                    e.printStackTrace();
                } 
        }; 
        new Thread(r2).start();
        
    }

}
 

public class client_server {
    public static void main(String args[]){
         new client();
//    
    }
}
