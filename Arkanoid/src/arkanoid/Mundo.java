/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template filae, choose Tools | Templates
 * and open the template in the editor.
 */

package arkanoid;

import arkanoid.ladrillos.Ladrillo;
import arkanoid.ladrillos.LadrilloSuerte;
import FRAMEWORK.LOGICA.ActorTexto;
import FRAMEWORK.LOGICA.Game;
import arkanoid.ladrillos.LadrilloNormal;
import arkanoid.ladrillos.LadrilloResistente;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author josevicente
 */
public class Mundo extends Game{
        
    private Barra barra;
    private ArrayList<Bola> bolas;
    private int mundo;
    private Sombra sombra;
    private ActorTexto tituloPuntosVidas;
    private ActorTexto textoInformativo;
    
    //ladrillos
    public static final int LADRILLO_ROJO = 1;
    public static final int LADRILLO_AZUL = 2;
    public static final int LADRILLO_VERDE = 3;
    public static final int LADRILLO_AMARILLO = 4;
    public static final int LADRILLO_AMARILLO_ROTO = 5;
    public static final int LADRILLO_AMARILLO_SUPER_ROTO = 6;
    public static final int LADRILLO_SUERTE = 7;
    
    //para futuras iteraciones en las que la velocidad pueda variar
    public static final int LENTO = 7;
    public static final int NORMAL = 10;
    public static final int MAXIMO = 30;
    public static final int RAPIDO = 15;
    public static final int EXTRARAPIDO = 20;
    public static final int EXTRALENTO = 3;

    public Barra getBarra(){
        return barra;
    }

    public void setTextoInformativo(String textoInformativo) {
        this.textoInformativo.setTexto(textoInformativo);
    }

    /**
     *
     */
    @Override
    public void principal() {
        this.setName("Arkanoid");
        
        iniciarPersonajes();
        
        while (!this.isFin()) {
            if (this.getKeyBoardHandler().getTecla() == KeyEvent.VK_SPACE){
               this.reanudarJuego();
               textoInformativo.setTexto("");
            }
       
            tituloPuntosVidas.setTexto("Puntos " + barra.getPuntos());
            this.actualizar(); //ciclo logico de juego
            
            if (barra.getVida() == 0 || Ladrillo.getNumLadrillos()==0) {
                textoInformativo.setTexto("GAME OVER");
                textoInformativo.setPosition((this.SCREEN_WIDTH-textoInformativo.getAncho())/2, textoInformativo.getY());
                this.actualizar(); //ciclo logico de juego
                try {
                    Thread.sleep(3000); //¿Dar la opción de volver a empezar?
                } catch (InterruptedException ex) {
                    Logger.getLogger(Mundo.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.terminarJuego();
            }
            
        }
    }

    public void iniciarPersonajes(){
        //Barra y sombra
        sombra = new Sombra(this);
        barra = new Barra(this);
        //barra.setVida(Barra.VIDAS_INICIALES);
                
        //Bola(s)
        bolas = new ArrayList<>();
        bolas.add(new Bola(this, Recursos.bola));

        //Ladrillos
        int matrizLadrillos[][]={ {LADRILLO_AMARILLO,   LADRILLO_AZUL, LADRILLO_AZUL,  LADRILLO_AZUL, LADRILLO_AMARILLO, LADRILLO_AMARILLO,  LADRILLO_AMARILLO, LADRILLO_AZUL, LADRILLO_AZUL,  LADRILLO_AZUL, LADRILLO_AMARILLO},
                                  {0,                   LADRILLO_AZUL, LADRILLO_VERDE, LADRILLO_AZUL, LADRILLO_AMARILLO, LADRILLO_SUERTE,    LADRILLO_AMARILLO, LADRILLO_AZUL, LADRILLO_VERDE, LADRILLO_AZUL, 0},
                                  {LADRILLO_SUERTE,     LADRILLO_AZUL, LADRILLO_AZUL,  LADRILLO_AZUL, LADRILLO_AMARILLO, LADRILLO_AMARILLO,  LADRILLO_AMARILLO, LADRILLO_AZUL, LADRILLO_AZUL,  LADRILLO_AZUL, LADRILLO_SUERTE}};
        
        this.generarParedLadrillosAMedida(matrizLadrillos, 10, 20);
        //this.generarParedLadrillosHomogenea(LADRILLO_SUERTE, 3, 10, 10, 10);
        
        //Titulo vidas y puntos
        tituloPuntosVidas = new ActorTexto(this,"Puntos " + barra.getPuntos());
        tituloPuntosVidas.setPosition(20, this.SCREEN_HEIGHT - barra.getHeight());
        tituloPuntosVidas.setTamanio(20);
        tituloPuntosVidas.setColor(Color.BLUE); 
        
        //Texto informativo para el usuario
        textoInformativo = new ActorTexto(this, "Pulsa la barra espaciadora para comenzar");
        textoInformativo.setPosition((this.SCREEN_WIDTH-textoInformativo.getAncho())/2, barra.getY()- barra.getHeight()*6);
        textoInformativo.setTamanio(14);
        textoInformativo.setColor(Color.BLACK);
        
        //Añadir personajes a actorManager
        this.actorManager.add(sombra);
        this.actorManager.add(barra);
        this.actorManager.add(bolas.get(0));
        this.actorManager.add(tituloPuntosVidas); 
        this.actorManager.add(textoInformativo); 
        
    }
    
    public Sombra getSombra() {
        return sombra;
    }
    
    public void generarParedLadrillosHomogenea (int tipoLadrillo, int numFilas, int numColumnas, int hgapLadrillo, int vgapLadrillo){
        //Creamos una matriz homogénea
        int[][] matriz = new int[numFilas][numColumnas];
        for(int fila=0; fila<matriz.length; fila++)
            for(int columna =0; columna<matriz[0].length; columna++)
                matriz[fila][columna] = tipoLadrillo;
        
        generarParedLadrillosAMedida(matriz, hgapLadrillo, vgapLadrillo);
    }
    
    public void generarParedLadrillosAMedida (int[][] matrizLadrillos, int hgapLadrillo, int vgapLadrillo){
        Ladrillo ladrillo = null;
        int tamannoLadrillos = (Recursos.ladrilloAmarillo.getWidth()+hgapLadrillo)*matrizLadrillos[0].length;
        int posxInicial = (this.SCREEN_WIDTH-tamannoLadrillos)/2;
        int posyInicial = this.SCREEN_HEIGHT/4;
        int posx, posy;
        for (int fila=0;fila<matrizLadrillos.length;fila++){     
            for (int columna=0;columna<matrizLadrillos[0].length;columna++){
                int tipoLadrillo = matrizLadrillos[fila][columna];
                switch(tipoLadrillo){
                    case LADRILLO_ROJO: //Ladrillo normal rojo
                        ladrillo=new LadrilloNormal(this, Recursos.ladrilloRojo);
                        break;
                    case LADRILLO_AZUL: //Ladrillo normal azul
                        ladrillo=new LadrilloNormal(this, Recursos.ladrilloAzul);
                        break;
                    case LADRILLO_VERDE: //Ladrillo normal verde
                        ladrillo=new LadrilloNormal(this, Recursos.ladrilloVerde);
                        break;
                    case LADRILLO_AMARILLO: //Ladrillo resistente
                        ladrillo=new LadrilloResistente(this);
                        break;
                    case LADRILLO_SUERTE: //Ladrillo suerte
                        ladrillo=new LadrilloSuerte(this);
                        break;
                        
                }    
    
                if(tipoLadrillo!=0){
                    posx = posxInicial + columna*(ladrillo.getWidth() + hgapLadrillo);
                    posy = posyInicial + fila*(ladrillo.getHeight() + vgapLadrillo);
                    ladrillo.setPosition(posx,posy);
                    this.actorManager.add(ladrillo);
                }
                                
            }          
        
        }
    }
}
