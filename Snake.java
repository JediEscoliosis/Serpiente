package serpiente;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

@SuppressWarnings("serial")
public class Snake extends JFrame{
	
	Container contenedor;
	
	Momento mom;
	
	JPanel menu;
	
	Juego juego;
	
	JButton op1,op2,op3;

	boolean estaJugando=true,gameOver=false;
	
	Point sn,comida; //sn= Snake
	
	int anchoP=10,alturaP=10;//Anchura y altura del punto
	
	int longitud=2;
	
	int ancho=600,altura=600;//Anchura y altura de la ventana
	
	long frequency=50;
	
	String direccion="RIGHT";
	
	ArrayList<Point> listaPosiciones=new ArrayList<Point>();
	
	Action up,down,right,left,exit, gOver, restart;//Creo las acciones  (gOver no es usado)

	public Snake(String titulo) {
		super(titulo);
		juego=new Juego();
		setSize(600,600);
		contenedor=getContentPane();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		componentesMenu();
		mom=new Momento();
		Thread hilo=new Thread(mom);
		hilo.start();
		
		startGame();
		
		//Código que hace que funcionen los botones:
		up=new UpAction();//Flecha para arriba
		down=new DownAction();//Flecha para abajo
		left=new LeftAction();//Flecha para izquierda
		right=new RightAction();//Flecha para derecha
		exit=new ExitAction();//Tecla "esc" (escape)
		restart=new RestartAction();//Tecla "ENTER" para reiniciar
		
		//Asignación a la tecla "ESC"
		juego.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "exitAccion");
		juego.getActionMap().put("exitAccion", exit);
		//Asignación a la tecla direccional derecha
		juego.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "rightAccion");
		juego.getActionMap().put("rightAccion", right);
		//Asignación a la tecla direccional izquierda
		juego.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "leftAccion");
		juego.getActionMap().put("leftAccion", left);
		//Asignación a la tecla direccional abajo
		juego.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "downAccion");
		juego.getActionMap().put("downAccion", down);
		//Asignación a la tecla direccional arriba
		juego.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "upAccion");
		juego.getActionMap().put("upAccion", up);
		//Asignación a la tecla "ENTER"
		juego.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "restartAction");
		juego.getActionMap().put("restartAction", restart);
	}
	//¿Cómo hago para que la primera posición del punto también sea random?
	public void startGame() {
		comida=new Point(200,300);
		generarComidaNueva();
		sn=new Point (0,100);
		listaPosiciones=new ArrayList<Point>();
		listaPosiciones.add(sn);
	}

	//ERROR MAS IMPORTANTE:
	//CUANDO LA SERPIENTE TOCA UNA DE LAS PAREDES Y SE REINICIA CON ENTER
	//EL LA SERPIENTE SE QUEDA QUIETA, NO SE MUEVE EN NINGUNA DIRECCIÓN
	//AUNQUE SE APRIETEN LAS FLECHAS DIRECCIONALES.
	
	public class Juego extends JPanel {
		public void prepararPanel() {
			juego.setBounds(0,0,600,600);
			juego.setOpaque(true);
			juego.setLayout(null);
			juego.setBackground(new Color(52, 73, 94));
		}
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(gameOver)
            	g.setColor(new Color(0,0,0));
            else
            	g.setColor(new Color(255,255,255));
            
            g.setColor(new Color(0,255,0));
            g.fillRect(sn.x,sn.y,anchoP,alturaP);
            if(listaPosiciones.size()>0)
            	for(int i=0;i<listaPosiciones.size();i++) {
            		Point p=(Point)listaPosiciones.get(i);
            		g.fillRect(p.x, p.y, anchoP,alturaP);
            	}
            g.setColor(new Color(255,0,0));
            g.fillRect(comida.x,comida.y,anchoP,alturaP);
            //
			if(direccion=="GameOver"||gameOver) {
            	juego.setBackground(new Color(100, 30, 22 ));
                g.setFont(new Font("Consolas", 0, 30));
                g.drawString("GAME OVER",185,70);
                g.drawString("SCORE "+(listaPosiciones.size()-1), 300, 240);

                g.setFont(new Font("TimesRoman", Font.BOLD, 20));
                g.drawString("Sos tremendo maleta", 185, 120);
                g.drawString("ENTER para reiniciar",185,180);
                g.drawString("ESC para cerrar", 185, 150);
        }
    }
	}
	
	public void componentesMenu() {
		menu=new JPanel();
		menu.setBounds(0,0,600,600);
		menu.setLayout(null);
		menu.setOpaque(true);
		menu.setBackground(Color.black);

		//Estos 2 botones estan sin usar
		JButton oBoton1=new JButton("Intentar de Nuevo");
		
		oBoton1.setBounds(165,100,250,100);
		
		JButton oBoton2=new JButton("Salir");
		
		
		
		JLabel et1=new JLabel("S E R P I E N T E",SwingConstants.CENTER);
		et1.setFont(new Font("Consolas",0,20));
		et1.setOpaque(true);
		et1.setBackground(Color.black);
		et1.setForeground(Color.white);
		et1.setBounds(185,50,200,30);
		menu.add(et1);
		
		op1=new JButton("Juego nuevo");
		op1.addActionListener(new OyenteOp1());
		op1.setFont(new Font("Consolas",0,20));
		op1.setBounds(165,200,250,100);
		op1.setBackground(new Color(100, 235, 198));
		
		
		op2=new JButton("Cerrar");
		op2.addActionListener(new OyenteOp2());
		op2.setFont(new Font("Consolas",0,20));
		op2.setBackground(new Color(148, 49, 38));
		op2.setForeground(Color.LIGHT_GRAY);
		op2.setBounds(165,350,250,100);
		
		
		menu.add(op1);
		menu.add(op2);
		
		contenedor.add(menu);
	}
	//ERROR:
	// Cada vez que la serpiente agarra uno de los puntos de comida, la zona de juego parece aumentar ya que
	// cuando empieza la partida y vas hacia uno de los bordes, se puede ver donde termina la pantalla,
	// pero si haces lo mismo (ir a una pared) después de agarrar varios puntos, se puede notar que la serpiente puede 
	// ir mas lejos.
	public void generarComidaNueva() {
		Random rnd=new Random();
		 // Asegura que la comida esté dentro de los límites en el eje X
	    comida.x = rnd.nextInt(ancho);
	    
	    // Ajusta la posición para que sea múltiplo de la anchura del cuerpo de la serpiente
	    comida.x -= comida.x % anchoP;
	    
	    // Asegura que la comida no esté demasiado cerca del borde derecho
	    if (comida.x > ancho - anchoP) {
	        comida.x -= anchoP;
	    }
	    
	    // Asegura que la comida esté dentro de los límites en el eje Y
	    comida.y = rnd.nextInt(altura);
	    
	    // Ajusta la posición para que sea múltiplo de la altura del cuerpo de la serpiente
	    comida.y -= comida.y % alturaP;
	    
	    // Asegura que la comida no esté demasiado cerca del borde inferior
	    if (comida.y > altura - alturaP) {
	        comida.y -= alturaP;
	    }
	    
	    // Asegura que la comida no esté demasiado cerca del borde superior
	    if (comida.y < alturaP) 
	        comida.y += alturaP;
	}
	//ERROR: cuando se reinicia el punto de la serpiente se queda quieto.
	public class RestartAction extends AbstractAction{
		public void actionPerformed(ActionEvent e) {
			juego.setBackground(new Color(52, 73, 94));
			direccion="RIGHT";
			gameOver=false;
			startGame();
		}		
	}
	
	public class ExitAction extends AbstractAction{
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	public class UpAction extends AbstractAction{	
		public void actionPerformed(ActionEvent e) {
			if(direccion!="DOWN")
				direccion="UP";
			//System.out.println("Arriba");
		}
	}
	
	public class DownAction extends AbstractAction{	
		public void actionPerformed(ActionEvent e) {
			if(direccion!="UP")
				direccion="DOWN";
			//System.out.println("Abajo");
		}
	}
	
	public class LeftAction extends AbstractAction{	
		public void actionPerformed(ActionEvent e) {
			if(direccion!="RIGHT")
				direccion="LEFT";
			//System.out.println("Izquierda");
		}
	}
	
	public class RightAction extends AbstractAction{
		public void actionPerformed(ActionEvent e) {
			if(direccion!="LEFT")
				direccion="RIGHT";
			//System.out.println("Derecha");
		}	
	}
	
	private class OyenteOp1 implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			contenedor.remove(menu);
			contenedor.repaint();
			juego.prepararPanel();
			contenedor.add(juego);
			estaJugando=true;	
		}
	}
	
	private class OyenteOp2 implements ActionListener{	
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	public class Momento extends Thread{
		private long last=0;
		public Momento() {
			
		}
		public void run() {
			
				while(direccion!="GameOver") {
					if(System.currentTimeMillis()-last>frequency) {
						if(!gameOver) {
							switch(direccion) {
							case "RIGHT":
								sn.x+=anchoP;
								if(sn.x>ancho)
									direccion="GameOver";
								break;
							case "LEFT":
								sn.x-=anchoP;
								if(sn.x<0)
									direccion="GameOver";
								break;
							case "UP":
								sn.y-=alturaP;
								if(sn.y<0)
									direccion="GameOver";
								break;
							case "DOWN":
								sn.y+=alturaP;
								if(sn.y>altura)
									direccion="GameOver";
								break;
							}
						}
					actualizar();
					last=System.currentTimeMillis();
					}
				}
			}
		}
	
	public void actualizar() {
		listaPosiciones.add(0,new Point(sn.x,sn.y));
		listaPosiciones.remove(listaPosiciones.size()-1);
		
		for(int i=1;i<listaPosiciones.size();i++) {
			Point po=listaPosiciones.get(i);
			if(sn.x==po.x&&sn.y==po.y)
				gameOver=true;
		}
		//Si la serpiente se come un punto, se genera un punto de comida nuevo y se agranda la serpiente por una unidad
		if((sn.x > (comida.x-10) && sn.x < (comida.x+10)) && (sn.y > (comida.y-10) && sn.y < (comida.y+10))) {
	        listaPosiciones.add(0,new Point(sn.x,sn.y));
			generarComidaNueva();
	    }
		juego.repaint();
	}
	
	
	
	
	
	public static void main (String[]a) {
		Snake s1=new Snake("Juego de la serpiente");
		s1.setVisible(true);
		s1.requestFocus();
	}

}
