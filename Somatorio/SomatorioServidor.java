import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class SomatorioServidor{
    private static DataInputStream entrada1;
    private static DataOutputStream saida1;
    public static void main( String args[] ) throws IOException {
        
        Scanner entrada=new Scanner(System.in);
        System.out.println("Quantos clientes?");
        int clientes = entrada.nextInt();
        System.out.println("Digite o número!");
        int num = entrada.nextInt();
        double somapar = 0;
        double soma = 0;
        
        ServerSocket s = (ServerSocket)null;
        s = new ServerSocket( 4321,1000);
        Socket[] somatorioCliente = new Socket[clientes];
        int i=0;
        while( clientes>i) {
                somatorioCliente[i] = s.accept();
                i++;
        }
        i=0;
        while(clientes>i){
                saida1=new DataOutputStream(somatorioCliente[i].getOutputStream());
                saida1.writeInt(i+1);
                saida1.writeInt(clientes);
                saida1.writeInt(num);
                i++;
        }
        long tempInicial = System.nanoTime();    
        i=0;
        while(clientes>i){
                entrada1 = new DataInputStream(somatorioCliente[i].getInputStream());
                somapar=entrada1.readDouble();
                soma=soma+somapar;
                somatorioCliente[i].close();
                i++;
        }
        long tempFinal = System.nanoTime();
        long dif = (tempFinal - tempInicial);
        System.out.println("Resultado: "+soma);
        System.out.println(String.format("%d nanosegundos", dif));
        }
    }
