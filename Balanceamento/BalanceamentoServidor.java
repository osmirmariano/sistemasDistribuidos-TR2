import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class BalanceamentoServidor {
        private static DataInputStream entrada1;
        private static DataOutputStream saida1;

        public static void main( String args[] ) throws IOException {
                Scanner entrada = new Scanner(System.in);
                System.out.println("número de clientes?");
                int clientes = entrada.nextInt();
                System.out.println("Digite o número!");
                int num = entrada.nextInt();
                System.out.println("Qual é o número de pedaços?");
                int ped = entrada.nextInt();        
                double somapar = 0;
                double soma = 0;
                int boo = 1;
                ServerSocket s = (ServerSocket)null;
                s = new ServerSocket( 4321,1000);
                Socket[] scli = new Socket[clientes];
                int[] vetini = new int[ped];
                int[] vetfim = new int[ped];
                int parte = num/ped;
                int i;

                for(i = 0; i < ped; i++){
                        vetini[i] = i*parte+1;
                        vetfim[i] = (i+1)*parte;
                        if(i ==  ped-1 && vetfim[i] != num) 
                                vetfim[i] = num;
                }
                i = 0;
                while(clientes > i) {
                        scli[i] = s.accept();
                        i++;
                }
                i = 0;
                int cont = 0;
                while(clientes > i){
                        saida1 = new DataOutputStream(scli[i].getOutputStream());
                        entrada1 = new DataInputStream(scli[i].getInputStream());                
                        saida1.writeInt(vetini[cont]);
                        saida1.writeInt(vetfim[cont]);
                        scli[i].close();
                        cont++;
                        i++;
                }
                long tempInicial = System.nanoTime();    
                while(cont < ped){
                        Socket s2 = new Socket();
                        s2 = s.accept();
                        saida1 = new DataOutputStream(s2.getOutputStream());
                        entrada1 = new DataInputStream(s2.getInputStream());
                        somapar=entrada1.readDouble();
                        saida1.writeInt(ped-cont);       
                        saida1.writeInt(vetini[cont]);
                        saida1.writeInt(vetfim[cont]);
                        s2.close();
                        soma = soma+somapar;
                        cont++;
                }
                for(i = 0; i < clientes; i++){
                        Socket s2 = new Socket();
                        s2 = s.accept();
                        saida1 = new DataOutputStream(s2.getOutputStream());
                        entrada1 = new DataInputStream(s2.getInputStream());
                        somapar = entrada1.readDouble();                
                        saida1.writeInt(ped-cont);
                        s2.close();
                        soma = soma + somapar;
                }
                long tempFinal = System.nanoTime();
                long dif = (tempFinal - tempInicial);
                System.out.println("Resultado: " +soma);
                System.out.println(String.format("%d nanosegundos", dif));
        }
}
