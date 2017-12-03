import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ButterflyCliente {
    private static DataInputStream entrada1;
    private static DataOutputStream saida1;
    private static ObjectInputStream entrada2;
    private static DataOutputStream saida2;

    public static void main( String args[] ) throws IOException, ClassNotFoundException{
        Scanner entrada = new Scanner(System.in);
        System.out.println("Qual o ip do servidor?");
        String servidor = entrada.next();    
        InetSocketAddress servidor2 = new InetSocketAddress(servidor, 1234);
        Socket s = new Socket();
        s.connect(servidor2,1000);  
        entrada1 = new DataInputStream(s.getInputStream());
        entrada2 = new ObjectInputStream(s.getInputStream());
        int id = entrada1.readInt();
        int clientes = entrada1.readInt();
        int valor = entrada1.readInt();
        java.net.InetAddress[] end = (java.net.InetAddress[]) entrada2.readObject();
        s.close();
        int porta = id + 1234;
        ServerSocket s2 = (ServerSocket)null;
        s2 = new ServerSocket(porta,1000);
        
        float parcela = valor/clientes;
        int inicio = ((int)parcela*(id-1))+1;
        int fim = ((int)parcela*id);
        if(id == clientes){
            fim = valor;
        }
        double somapar = 0;
        for(int i = inicio; i <= fim; i++){
            somapar = somapar + i;
        }
        int metade = clientes+1;
        do{
            metade = (int)(metade/2);
            if(id >= metade){
                Socket s3 = new Socket();
                int porta2 = id-metade+1234;
                InetSocketAddress servidor3 = new InetSocketAddress(end[id-metade],porta2);
                s3.connect(servidor3,1000);
                saida2 = new DataOutputStream(s3.getOutputStream());
                saida2.writeDouble(somapar);
                s3.close();
            }
            else{
                Socket s4 = new Socket();
                s4 = s2.accept();
                entrada1 = new DataInputStream(s4.getInputStream());
                somapar = somapar+entrada1.readDouble();
                s4.close();                
            }
        }while(metade > 1 && id < metade);
    }
}
