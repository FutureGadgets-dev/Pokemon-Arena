import java.io.*;
import java.util.*;

public class PokemonArena {
	static ArrayList <String> pNames=new ArrayList<String>();//player pokemon names
	static ArrayList <String> pokeNames=new ArrayList<String>();//names of pokemon, then name of pc pokemon
	static ArrayList <Pokemon> pokeList=new ArrayList<Pokemon>();//all pokemon, then pc pokemon
	static ArrayList <Pokemon> pPoke=new ArrayList<Pokemon>();//player pokemon
	static ArrayList <Pokemon> thePoke= new ArrayList<Pokemon>(2);//pokemon that are on the arena
	static ArrayList<ArrayList<Pokemon>>allPokeList=new ArrayList<ArrayList<Pokemon>>();//all the pokemon remaining for both player and cpu
	static final int ATTACK=1;
	static final int PASS=2;
	static final int RETREAT=3;
	static Random rand=new Random();
	static Scanner kb=new Scanner(System.in);
	public static String whoWon;
    public static void main(String[] args){
    	int num=loadPoke();
    	pickPoke(num);
    	game();
    	System.out.printf("The winner is %s\n",whoWon);
    }
    public static boolean gameOver(ArrayList<Pokemon>list,int t){
    	if (list.size()<=0){
    		if (t==0){
	    		whoWon="The Computer";
    		}
	    	else{
	    		whoWon="Youuuuuuuuuu";
	    	}
    		return false;
    	}
    	else{
    		return true;
    	}
    }
    public static void playerPick(int n){
    	thePoke.set(0,pPoke.get(n-1));
    }  
    public static void compPick(){
    	int size=pokeList.size();
    	int r=rand.nextInt(size);
    	thePoke.set(1,pokeList.get(r));
    }
    public static void display(Pokemon player,Pokemon computer){
    	System.out.println("");
    	System.out.printf("Player:%s Hp:%d Energy:%d Type:%s\n",player.name(),player.hp(),player.energy(),player.type());
    	System.out.printf("Computer:%s Hp:%d Energy:%d Type:%s\n",computer.name(),computer.hp(),computer.energy(),computer.type());
    	System.out.println("");
    }
    public static void whatHappened(String choices,int t){
    	String[]wh=choices.split(",");
    	int [] what=new int [wh.length];
    	for (int i=0;i<what.length;i++){
    		what[0]=Integer.parseInt(wh[i]);
    	}
    	if (what[0]==PASS){
    		if (t==0){
	    		System.out.println("You passed your turn");
    		}
    		else{
    			System.out.println("The computer's pokemon doesnt have enough energy");
    		}
    	}
    	else if (what[0]==RETREAT){
    		System.out.println("You retreated and switched pokemon");
    	}
    }
	public static void printOptions(ArrayList<String>list){
		int len=list.size();
		for (int i=1;i<=len;i++){
			System.out.printf("%d.%s\n",i,list.get(i-1));
		}
	}
	public static int changeTurn(int t){
		t=Math.abs(t-1);
		return t;
    }
	public static int attacking(String stuff,Pokemon attacker,Pokemon attacked){
		String[]turnattack=stuff.split(",");
    	int [] ta=new int [turnattack.length];
    	for (int i=0;i<ta.length;i++){
    		ta[i]=Integer.parseInt(turnattack[i]);
    	}
    	int turn=ta[0];
    	int attackChoice=ta[1];
    	String pokeSpecial=attacker.allAtks.get(attackChoice).special();
    	if (pokeSpecial.equals("stun")){
    		attacker.attack(attackChoice,attacked);
    		if (percentChance()==true){
    			System.out.printf("%s was stunned\n",attacked.name());
    			return 1;
    		}
    		else {
    			return 0;
    		}
    	}
    	else if (pokeSpecial.equals("wild card")){
    		if (percentChance()==true){
    			attacker.attack(attackChoice,attacked);
    			System.out.printf("%s unleashed 'wild card'\n",attacker.name());
    			return 0;
    		}
    		else {
    			attacker.energyDown(attacker.allAtks.get(attackChoice).getCost());
    			System.out.printf("%s's 'wild card' missed\n",attacker.name());
    			return 0;
    		}
    	}
    	else if (pokeSpecial.equals("wild storm")){
    		while(percentChance()==true){
    			attacker.attack(attackChoice,attacked);
    			System.out.printf("%s unleashed 'wild storm'\n",attacker.name());
    			attacker.energyBack(attacker.allAtks.get(attackChoice).getCost());
    		}
    		attacker.energyDown(attacker.allAtks.get(attackChoice).getCost());
    		System.out.printf("%s's 'wild storm' missed\n",attacker.name());
    		return 0;
    	}
    	else if (pokeSpecial.equals("disable")){
    		attacker.attack(attackChoice,attacked);
    		attacked.disable();
    		System.out.printf("%s was disabled\n",attacked.name());
    		return 0;
    	}
    	else if (pokeSpecial.equals("recharge")){
    		attacker.attack(attackChoice,attacked);
    		attacker.recharge();
    		System.out.printf("%s recharged 20 energy\n",attacker.name());
    		return 0;
    	}
    	else{
    		attacker.attack(attackChoice,attacked);
    		return 0;
    	}
	}	
	public static boolean percentChance(){
		int num=rand.nextInt(2);
    	if (num==0){
    		return true;
    	}
    	else{
    		return false;	
    	}	
	}
    public static void game(){
    	int turn=rand.nextInt(2);
    	int atkChoice,dTurn;
    	int move=0;
    	String occur="";
    	printOptions(pNames);
    	playerPick(kb.nextInt());
    	compPick();
    	System.out.println(thePoke.get(0).name()+" I choose you!!!");
    	while (gameOver(allPokeList.get(turn),turn)==true){
    		dTurn=0;
    		atkChoice=0;
    		display(thePoke.get(0),thePoke.get(1));
    		if (turn==0){
    			System.out.println("It's your turn");
    			System.out.println("1.Attack  2.Pass  3.Retreat");
	    		move=kb.nextInt();
    		}
    		else{
    			System.out.println("It's the computers turn");
    			for (int i=0;i<thePoke.get(1).atkLen();i++){
	    			if (thePoke.get(1).energy<thePoke.get(1).atkCosts().get(i)){
	    				move=PASS;
	    			}
	    			else{
	    				move=ATTACK;
	    			}
    			}
    		}
    		occur=Integer.toString(move)+",";
    		if (move==ATTACK){
    			if (turn==0){
    				if (thePoke.get(0).haveEnergy()==true){
    					thePoke.get(turn).printAtks();
	    				atkChoice=kb.nextInt()-1;
	    				if (thePoke.get(0).allAtks.get(atkChoice).getCost()>thePoke.get(0).energy()){
	    					System.out.println("No energy for that pick another attack");
	    					continue;
	    				}
    				}
    				else {
   						System.out.println("No energy pick another option");
   						continue;
    				}
    			}
    			else{
    				if (thePoke.get(1).haveEnergy()==true){
	    				int lenn=thePoke.get(turn).atkLen();
	    				ArrayList<Attack>randList=new ArrayList<Attack>();
	    				for (int i=0;i<lenn;i++){
	    					if (thePoke.get(1).energy()>=thePoke.get(1).allAtks.get(i).getCost()){
	    						randList.add(thePoke.get(1).allAtks.get(i));
	    					}
	    				}
	    				if (randList.size()>0){
		    				atkChoice=rand.nextInt(randList.size());
	    				}
	    				else{
	    					move=PASS;
	    				}
    				}
    			}
    			occur+=Integer.toString(atkChoice);
    			dTurn=attacking(occur,thePoke.get(turn),thePoke.get(Math.abs(turn-1)));
    		}
    		else if (move==PASS){
    			turn=changeTurn(turn);
    			occur+="";
    			energyIncrease();
    			continue;
    		}
    		else if (move==RETREAT){
    			printOptions(pNames);
    			boolean flag=true;
    			while (flag==true){
    				int n=kb.nextInt();
    				if (n>0 && n<pPoke.size()){
    					playerPick(n);
    					flag=false;
    				}
    			}
    			occur+="";
    		}
    		whatHappened(occur,turn);
    		for (int i=0;i<2;i++){
	    		if (thePoke.get(i).hp()<=0){
	    			if (turn==1){
	    				System.out.println("YOUR POKEMON "+thePoke.get(0).name()+" FAINTED!");
	    				pPoke.remove(thePoke.get(0));
	    				pNames.remove(thePoke.get(0).name());
	    				printOptions(pNames);
	    				if (pPoke.size()>0){
		    				playerPick(kb.nextInt());//make even if bigger number
	    				}
	    			}
	    			else{
	    				System.out.println("THE ENEMIE'S POKEMON "+thePoke.get(1).name()+" FAINTED!");
	    				pokeList.remove(thePoke.get(1));
	    				pokeNames.remove(thePoke.get(1).name());
	    				compPick();
	    				System.out.printf("THE COMPUTER PICKED %s\n",thePoke.get(1).name());
	    			}
	    		}
    		}
    		energyIncrease();
    		turn=changeTurn(turn)+dTurn;
    		System.out.println("---------------------------------------------------");
    	}
    }
    public static void energyIncrease(){
    	for (int i=0;i<pPoke.size();i++){
    		pPoke.get(i).energyUp();
    	}
    	for (int i=0;i<pokeList.size();i++){
    		pokeList.get(i).energyUp();
    	}
    }
    public static int loadPoke(){
    	Scanner pokefile=null;
    	try{
    		pokefile=new Scanner(new FileReader("pokemon.txt"));
    	}
    	catch(IOException ex){
    		System.out.println(ex);
    	}
    	int len=Integer.parseInt(pokefile.nextLine());
    	for (int i=0;i<len;i++){
    		String poke=pokefile.nextLine();
    		pokeNames.add(poke.split(",")[0]);
    		pokeList.add(new Pokemon(poke));
    	}
    	allPokeList.add(pPoke);
		allPokeList.add(pokeList);
    	return len;
    }
    public static void pickPoke(int n){
    	int r=n%2;
    	for (int x=0;x<4;x++){
    		System.out.println("Pick four pokemon");
    		for (int y=0;y<n/2;y++){
    			if (r==1){
    				System.out.printf("%2d. %-12s %d. %s \n",y+1,pokeNames.get(y),y+n/2+2,pokeNames.get(y+n/2+1));
    				if (y==(n/2)-1){
	    				System.out.printf("%d. %s \n",y+2,pokeNames.get(y+1));
	    			}
    			}
    			else{
    				System.out.printf("%2d. %-12s %d. %s \n",y+1,pokeNames.get(y),y+n/2+1,pokeNames.get(y+n/2));
    			}
    		}
    		int pokePos=kb.nextInt();
    		if (pokePos<=n){
    			pNames.add(pokeNames.get(pokePos-1));
    			pPoke.add(pokeList.get(pokePos-1));
	    		pokeNames.remove(pokePos-1);
	    		pokeList.remove(pokePos-1);
	    		n--;
	    		r=n%2;
    		}
    		else {
    			System.out.println("Invalid Entry");
    			x--;
    		}
    		System.out.println(pNames);
    	}
    	thePoke.add(pPoke.get(0));
    	thePoke.add(pokeList.get(0));
    }
}
