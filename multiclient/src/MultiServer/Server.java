/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MultiServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *
 * @author Deepika
 */
public class Server {

    int port;
    ServerSocket server=null;
    Socket client=null;
    ExecutorService pool = null;
    int clientcount=0;


    Collection collection;

    public static void main(String[] args) throws IOException {
        Server serverobj=new Server(5000);
        serverobj.startServer();
    }

    Server(int port){
        this.port=port;
        pool = Executors.newFixedThreadPool(5);
    }

    public void startServer() throws IOException {

        server=new ServerSocket(5000);
        System.out.println("Server Booted");
        System.out.println("Any client can stop the server by sending -1");
        while(true)
        {
            client=server.accept();
            clientcount++;
            ServerThread runnable= new ServerThread(client,clientcount,this);
            pool.execute(runnable);
        }

    }

    private static class ServerThread implements Runnable {

        Server server=null;
        Socket client=null;
        BufferedReader cin;
        PrintStream cout;
        Scanner sc=new Scanner(System.in);
        int id;
        String s;
        Random rand = new Random();
        static List<String> allValue = new ArrayList();

        ServerThread(Socket client, int count ,Server server ) throws IOException {

            this.client=client;
            this.server=server;
            this.id=count;
            System.out.println("Connection "+id+"established with client "+client);

            cin=new BufferedReader(new InputStreamReader(client.getInputStream()));
            cout=new PrintStream(client.getOutputStream());

        }

        @Override
        public void run() {
            int x=1;
            try{
                while(true){
                    s=cin.readLine();

                    System. out.print("Client("+id+") :"+s+"\n");
                    System.out.print("Server : ");
                    //s=stdin.readLine();
                    if(s.toLowerCase().contains("hotel")){
                    allValue.add(s);
                    }
                    s=sc.nextLine();
                    if (s.equalsIgnoreCase("bye"))
                    {
                        if(!allValue.isEmpty()){
                        System.out.println("Chosen place is "+allValue.get(rand.nextInt(allValue.size())));
                        }else {
                            System.out.println("Please enter hotel name with hotel Prefix");
                        }
                        cout.println("BYE");
                        x=0;
                        System.out.println("Connection ended by server");
                        break;
                    }
                    cout.println(s);
                }


                cin.close();
                client.close();
                cout.close();
                if(x==0) {
                    System.out.println( "Server cleaning up." );
                    System.exit(0);
                }
            }
            catch(IOException ex){
                System.out.println("Error : "+ex);
            }


        }
    }

}
