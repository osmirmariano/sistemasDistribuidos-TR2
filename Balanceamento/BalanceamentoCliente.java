import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class BalanceamentoCliente {
    private static DataInputStream entrada1;
    private static DataOutputStream saida1;
    
    public static void main( String args[] ) throws IOException{
        Scanner entrada=new Scanner(System.in);
        System.out.println("Qual o ip do servidor?");
        String servidor = entrada.next();
        InetSocketAddress servidor2 = new InetSocketAddress(servidor, 4321);
        int boo = 0, i;        
        Socket s = new Socket();
        Socket s3 = new Socket();
        s.connect(servidor2,5000);
        entrada1 = new DataInputStream(s.getInputStream());
        int valorini = entrada1.readInt();
        int valorfim = entrada1.readInt();
        s.close();
        
        double somapar = 0;
        for(i = valorini; i <= valorfim; i++)
            somapar = somapar + i;
        do{
            Socket scli = new Socket();
            scli.connect(servidor2,5000);
            saida1 = new DataOutputStream(scli.getOutputStream());
            entrada1 = new DataInputStream(scli.getInputStream());
            saida1.writeDouble(somapar);    
            boo = entrada1.readInt();
            if(boo > 0){
              valorini = entrada1.readInt();
              valorfim = entrada1.readInt();
               scli.close();
               somapar = 0;
               for(i = valorini; i <= valorfim; i++)
                    somapar = somapar + i;
            }  
        }while(boo > 0);
        
        s3.connect(servidor2,5000);
        saida1 = new DataOutputStream(s3.getOutputStream());
        saida1.writeDouble(somapar);
        s3.close();
    }
}
