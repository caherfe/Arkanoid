/**
 *
 * @author Rafael Pérez
 * DESARROLLO DE APLICACIONES MULTIPLATAFORMAS
 * COLEGIO STMA TRINIDAD DE SALAMANCA
 */
package FRAMEWORK.LOGICA;

import FRAMEWORK.GRAFICOS.Sprite;
import java.awt.Color;
import java.awt.Graphics;

public class StageManager {
    private Game game;   
    private Sprite spFondo;
    public StageManager(Game game){
        this.game=game;           
    }
    public void dibujar(Graphics g){
        Actor item;
        game.actorManager.rewind();
        if (spFondo==null){
            g.setColor(Color.black);
            g.fillRect(0,0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
        }
        else
            spFondo.dibujar(g);
        while((item=game.actorManager.current())!=null){
            item.dibujar(g);
            game.actorManager.next();
        }    
    }       
    public void setFondo(Sprite fondo){
        spFondo=fondo;
    }
}
