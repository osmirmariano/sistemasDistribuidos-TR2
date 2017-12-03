import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class ButterflyServidor {
    private static DataInputStream entrada1;
    private static DataOutputStream saida1;
    private static ObjectOutputStream saida2;
    public static void main( String args[]) throws IOException {
    Scanner entrada = new Scanner(System.in);
    System.out.println("Quantos clientes?");
    int clientes = entrada.nextInt();
    System.out.println("Digite o número!");
    int valor = entrada.nextInt();
    double somapar = 0;
    double soma = 0;
    ServerSocket s = (ServerSocket)null;
    s = new ServerSocket(1234, 1000);
    Socket[] scli = new Socket[clientes];
    int i = 0;
    java.net.InetAddress[] end = new java.net.InetAddress[clientes+1];
    
    while(clientes > i) {
        scli[i] = s.accept();
        end[0] = scli[i].getLocalAddress();
        end[i+1] = scli[i].getInetAddress();
        i++;
    }
    i = 0;
    while(clientes > i){
        saida2 = new ObjectOutputStream(scli[i].getOutputStream());
        saida1 = new DataOutputStream(scli[i].getOutputStream());
        saida1.writeInt(i+1);
        saida1.writeInt(clientes);
        saida1.writeInt(valor);               
        saida2.writeObject(end);
        i++;
    }
    
    long tempInicial = System.nanoTime();   
    
    //Fica implicito que o servidor tem id e o calculo pode ser simplificado.
    float fim = valor/clientes;
    for(i=0; i<= fim; i++)
        somapar = somapar + i;
    int metade = clientes + 1;
    
    do{
        metade = (int)(metade/2);          
        Socket s3 = new Socket();
        s3 = s.accept();
        entrada1 = new DataInputStream(s3.getInputStream());
        somapar = entrada1.readDouble();
        soma = soma + somapar;
        s3.close();
    }while(metade > 1);
    
    long tempFinal = System.nanoTime();
    long dif = (tempFinal - tempInicial);
    System.out.println("Resultado: " +soma);
    System.out.println(String.format("%d nanosegundos", dif));
    }
}
