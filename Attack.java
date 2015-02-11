public class Attack{
	private String name,special;
	private int cost,damage;
	public Attack(String attackributes){
		String []atkStats=attackributes.split(",");
		name=atkStats[0];
		cost=Integer.parseInt(atkStats[1]);
		damage=Integer.parseInt(atkStats[2]);
		special=atkStats[3];
	}
	public String name(){
		return name;
	}
	public int getDamage(){
		return damage;
	} 
	public int getCost(){
		return cost;
	}
	public String special(){
		return special;
	}
	public void disableAtk(){
		damage-=10;
		if (damage<=0){
			damage=0;
		}
	}
}