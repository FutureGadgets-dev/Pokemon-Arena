import java.lang.*;
import java.util.*;

public class Pokemon{
	private String name,type,weakness,resistance, atkInfo;
	private int hp, attackamt;
	public int dmg;
	public int energy=50; 
	public boolean disabled=false;
	public ArrayList<Attack>allAtks=new ArrayList<Attack>();
    public Pokemon(String stats){
    	String [] pokeStats=stats.split(",");
    	name=pokeStats[0];
    	hp=Integer.parseInt(pokeStats[1]);
    	type=pokeStats[2];
    	resistance=pokeStats[3];
    	weakness=pokeStats[4];
    	attackamt=Integer.parseInt(pokeStats[5]);
    	for (int i=6; i<6+(attackamt*4); i+=4){
    		allAtks.add(new Attack(pokeStats[i]+","+pokeStats[i+1]+","+pokeStats[i+2]+","+pokeStats[i+3]));
    	}
    }
    public String name(){
    	return name;
    }
    public int hp(){
    	return hp;
    }
    public int energy(){
    	return energy;
    } 
    public String type(){
    	return type;
    }
    public void energyDown(int amt){
    	energy-=amt;
    }
    public void energyBack(int amt){
    	energy+=amt;
    }
    public void attack(int n,Pokemon otherPoke){
    	energyDown(allAtks.get(n).getCost());
    	int totDamage=allAtks.get(n).getDamage();
    	if (otherPoke.weakness.equals(type)){
    		totDamage*=2;
    	}
    	else if (otherPoke.resistance.equals(type)){
    		totDamage/=2;
    	}
    	dmg=totDamage;
    	otherPoke.hp-=totDamage;
    	if (otherPoke.hp<=0){
    		otherPoke.hp=0;
    	}
    	System.out.printf("%s attacked %s and %d damage was dealt\n",name,otherPoke.name(),totDamage);
    }
    public void energyUp(){
    	energy+=10;
    	if (energy>=50){
    		energy=50;
    	}
    }
    public void recharge(){
    	energy+=20;
    	if (energy>=50){
    		energy=50;
    	}
    }
    public void disable(){
    	if (disabled==false){
    		for(int i=0;i<atkLen();i++){
    			allAtks.get(i).disableAtk();
    		}
    		disabled=true;
    	}
    }
    public void printAtks(){
    	for(int i=0;i<atkLen();i++){
    		Attack atks=allAtks.get(i);
    		String spec=atks.special();
    		System.out.printf("%s.Name:%s, Damage:%d, Cost:%d, Special:%s\n",i+1,atks.name(),atks.getDamage(),atks.getCost(),atks.special());
    	}
    }
    public ArrayList<String> atkNames(){
    	ArrayList<String> names=new ArrayList<String>();
    	for(int i=0;i<atkLen();i++){
    		names.add(allAtks.get(i).name());
    	}
    	return names;
    }
    public ArrayList<Integer> atkCosts(){
    	ArrayList<Integer> costs=new ArrayList<Integer>();
    	for(int i=0;i<atkLen();i++){
    		costs.add(allAtks.get(i).getCost());
    	}
    	return costs;
    }
    public int atkLen(){
    	return allAtks.size();
    }
    public boolean haveEnergy(){
    	ArrayList<Integer>ac=new ArrayList<Integer>();
    	ac=atkCosts();
    	for (int i=0;i<atkLen();i++){
    		if (ac.get(i)<=energy){
    			return true;
    		}
    	}
    	return false;
    }
}