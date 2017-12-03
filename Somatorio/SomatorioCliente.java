import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SomatorioCliente {
    private static DataInputStream entrada1;
    private static DataOutputStream saida1;
    public static void main( String args[] ) throws IOException{
        Scanner entrada = new Scanner(System.in);
        System.out.println("Qual o ip do servidor?");
        String servidor = entrada.next();
        InetSocketAddress servidor2 = new InetSocketAddress(servidor, 4321);
        Socket s = new Socket();
        s.connect(servidor2, 1000);
        entrada1 = new DataInputStream(s.getInputStream());
        saida1 = new DataOutputStream(s.getOutputStream());
        int id = entrada1.readInt();
        int clientes = entrada1.readInt();
        int valor = entrada1.readInt();
        float parcela = valor/clientes;
        int inicio = ((int)parcela*(id-1))+1;
        int fim = ((int)parcela*id);
        int i;
        if(id == clientes){
            fim = valor;
        }
        double somapar = 0;
        for(i = inicio; i<= fim; i++){
            somapar = somapar+i;
        }
        saida1.writeDouble(somapar);
        s.close();
    }
}
