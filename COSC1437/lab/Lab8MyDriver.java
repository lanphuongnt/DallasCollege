package MyLab8;

import java.util.ArrayList;
import java.util.Arrays;

import Lab8.Item;
import Lab8.Useable;


public class MyDriver
{
	public static void main(String[] args)
	{		
		//The given code here is just demonstrating and testing the system
		//hint: it is trying to lead you to some of the problems
		//anything here in main is not a part of the inventory system as a whole
		//and thus is not part of the lab, you may of course use main to test
		//things that to see how they work (or don't work) 
		
		Player p = new Player(new AccountInfo("pass",82392,"156 MiddleSet RD, Dallas, TX", "972-238-6942"));
		Inventory inventory = p.getInventory(); 
		inventory.owner = p;
		inventory.putOn(Slot.Torso, new Clothes(Slot.Torso,0,1,0));
		inventory.putOn(Slot.Pants, new Clothes(Slot.Pants,1,1,0));
		inventory.putOn(Slot.Hands, new Clothes(Slot.Hands,1,0,1));
		inventory.add(new Food("Donut",300,10,-5,10,-10));
		inventory.add(new WoodStick());
		inventory.add(new Match());
		inventory.add(new WoodStick());
		p.updateInventory(inventory); // updated
		
		
		Item stick = (Item) inventory.remove(1);
		Useable match = (Useable) inventory.remove(1); //note we removed the previous thing so now the match is at position 1
		Item fire = (Item) match.Combine(new ArrayList<Item>(Arrays.asList(stick)));
		System.out.println("Fire: "+fire);
		Item hardenedStick = ((Useable)fire).Combine(new ArrayList<Item>(Arrays.asList((Item)inventory.remove(1))));
		inventory.putOn(Slot.Weapon, (Wearable)hardenedStick);
		p.updateInventory(inventory);
		p.PrintInfo();
		
		//lets let some time pass
		for (int time=0;time<50;time++)
		{
			inventory.updateCondition();
		}
		
		p.PrintInfo();
	}
}
class AccountInfo
{
	public AccountInfo(AccountInfo acc) {
		this.password = acc.password;
		this.ID = acc.ID;
		this.billingAddress = acc.billingAddress;
		this.phoneNumber = acc.phoneNumber;
	}

	public AccountInfo(String password, int iD, String billingAddress, String phoneNumber) {
		this.password = password;
		ID = iD;
		this.billingAddress = billingAddress;
		this.phoneNumber = phoneNumber;
	}

	String password;
	int ID;
	String billingAddress;
	String phoneNumber;
	
}
class Player
{
	private AccountInfo account;
	int calories, energy, focus, warmth, fatigue, defense, thermalInsulation, offense;
	private Inventory inventory = new Inventory();
	public Player(AccountInfo acc)
	{
		account = new AccountInfo(acc);
		//player baseline
		calories = 800;
		energy = 100;
		focus = 50;
		warmth = 100;
		fatigue = 0;
		defense = 0;
		thermalInsulation = 1;
		offense = 1; 
	}
	public void updateInventory(Inventory i) {
		inventory = i;		
	}
	public Inventory getInventory()
	{
		return new Inventory(inventory); 
	}
	public AccountInfo getAccountInfo() // checked
	{
		return account; 
	}
	public void PrintInfo()
	{
		System.out.println("Player");
		System.out.println("Calories: "+this.calories);
		System.out.println("Energy: "+this.energy);
		System.out.println("Focus: "+this.focus);
		System.out.println("Warmth: "+this.warmth);
		System.out.println("Fatigue: "+this.fatigue);
		System.out.println("Defense: "+this.defense);
		System.out.println("ThermalInsulation: "+this.thermalInsulation);
		System.out.println("Offense: "+this.offense);
		System.out.println("Bag: "+this.inventory.print());
	}
}
class Inventory
{
	private ArrayList<Item> bag = new ArrayList<>();
	Player owner;
	
	ArrayList<WearableSlot> wearables = new ArrayList<>(Arrays.asList(new WearableSlot[]{
		new WearableSlot(Slot.Torso,null),
		new WearableSlot(Slot.Pants,null),
		new WearableSlot(Slot.Hands,null),
		new WearableSlot(Slot.Shoes,null),
		new WearableSlot(Slot.Weapon,null),
		new WearableSlot(Slot.Vehicle,null),
	}));
	public Inventory(){}
	public Inventory(Inventory i) {
		this.bag = i.bag;
		this.owner = i.owner;
		this.wearables = i.wearables;
	}
	public void add(Item i)
	{
		bag.add(i);
	}
	public String print()
	{
		return bag.toString();
	}
	public void updateCondition()
	{
		for (int i=0;i<bag.size();i++)
		{
			if (bag.get(i) instanceof Conditional)
				((Conditional)bag.get(i)).UpdateCondition();
		}
		for (int i=0;i<wearables.size();i++)
		{
			if (wearables.get(i) instanceof Conditional)
				if (((Conditional)wearables.get(i)).UpdateCondition())
				{
					wearables.remove(i);
					i--;
				}
				
		}
	}
	public Item remove(int index)
	{
		return bag.remove(index);
	}	
	//note that there is no get, remove the item do what you will to it and return it to the bag.
	
	public void putOn(Slot s, Wearable item)
	{
		if (getSlot(s).currentItem != null)
			getSlot(s).currentItem.takeOff(this.owner);
		
		getSlot(s).currentItem = item;
		item.putOn(this.owner);
		
	}
	public void eat(Consumable c, float percent)
	{
		if (c.Consume(percent, owner))
			bag.remove(c);
		
	}
	public WearableSlot getSlot(Slot s) // change wearables right here
	{
		for (WearableSlot w : wearables)
			if (w.slot == s)
				return w;
		return null;
	}
}
enum Slot
{
	Torso,
	Pants,
	Hands,
	Shoes,
	Weapon,
	Vehicle
}
interface Conditional
{	
	default boolean UpdateCondition()
	{
		System.out.println("this items condition never changes: "+this);
		return false;
	}
}
interface Wearable extends Conditional{

	void takeOff(Player owner);
	void putOn(Player owner);
}
abstract class Weapon extends Item implements Wearable  //Change Weapon from interface to class
{}
class WearableSlot
{
	public WearableSlot(Slot slot, Wearable currentItem)
	{
		this.slot = slot;
		this.currentItem = currentItem;
	}
	public Wearable putOnItem(Wearable newItem)
	{
		Wearable old = currentItem;
		currentItem = newItem;
		return old;
	}
	Slot slot;
	Wearable currentItem;
}

class Clothes extends Item implements Wearable
{
	int conditionRate = 1;
	int condition = 100;
	Slot slotToBeWornOn;
	int defense;
	int thermalInsulation;
	int offense;
	public Clothes(Slot slot, int i, int j, int k)
	{
		slotToBeWornOn = slot;
		defense = i;
		thermalInsulation = j;
		offense = k;
	}
	@Override
	public void takeOff(Player owner)
	{
		owner.defense -= defense;
		owner.thermalInsulation -= thermalInsulation;
		owner.offense -= offense;
	}
	@Override
	public void putOn(Player owner)
	{
		owner.defense += defense;
		owner.thermalInsulation += thermalInsulation;
		owner.offense += offense;
		
	}
	@Override
	public boolean UpdateCondition()
	{
		condition -= conditionRate;
		if (condition <= 0)
			return true;
		return false;
	}
}

abstract class Item
{
	
}
interface Consumable
{
	public abstract boolean Consume(float percentage, Player p);
}
class Food extends Item implements Consumable, Conditional
{
	int condition;
	String name;
	int calories, energy, focus, warmth, fatigue;
	public Food(String name, int calories, int energy, int focus, int warmth, int fatigue)
	{
		this.name = name;
		this.calories = calories;
		this.energy = energy;
		this.focus = focus;
		this.warmth = warmth;
		this.fatigue = fatigue;
		condition = 1;
	}
	//Percentage 0-1f
	public boolean Consume(float percentage, Player p)
	{
		if (condition <=0)
			return true; //and don't eat
		
		int amt = (int)(calories * percentage);
		calories -= amt;
		p.calories += amt;
		amt = (int)(energy * percentage);
		energy -= amt;
		p.energy += amt;
		amt = (int)(focus * percentage);
		focus -= amt;
		p.focus += amt;
		amt = (int)(warmth * percentage);
		warmth -= amt;
		p.warmth += amt;
		amt = (int)(fatigue * percentage);
		fatigue -= amt;
		p.fatigue += amt;
		
		if (calories <= 0)
			return true;
		return false;
	}
	public boolean UpdateCondition()
	{
		condition--;
		//the food has spoiled, but it will stay in your bag until you look at it
		return false;
	}
	public String toString()
	{
		String s = "";
		if (condition < 0)
			s+="spoiled!!! ";
		s+=name;
		return s;
	}
}
interface Useable // Change class Useable extends 
{
	public abstract Item Combine(ArrayList<Item> otherItems);
	public abstract void Use(Player owner);
}
class WoodStick extends Weapon implements Useable
{
	int conditionRate = 1;
	int condition = 100;
	int offense = 5;
	int defense = 3;
	public void takeOff(Player owner)
	{
		owner.offense -= offense;
		owner.defense -= defense;
		
	}
	public void putOn(Player owner)
	{
		owner.offense += offense;
		owner.defense += defense;
		
	}
	public Item Combine(ArrayList<Item> otherItems)
	{
		System.out.println(this+ " + "+otherItems.get(0));
		if (otherItems.get(0) instanceof Match)
		{
			System.out.println("I have created fire");
			return new Fire();
		}
		return null;
	}
	@Override
	public void Use(Player owner)
	{
		System.out.println("Oww");
		owner.calories-=1;		
	}
	@Override
	public boolean UpdateCondition()
	{
		condition -= conditionRate;
		if (condition <= 0)
			return true;
		return false;
	}
}
class FireHardenedStick extends WoodStick
{
	public FireHardenedStick(int o, int d)
	{
		offense = o;
		defense = d;
		conditionRate = 4;
	}
}
class Match extends UseableThings
{

	@Override
	public Item Combine(ArrayList<Item> otherItems)
	{
		System.out.println(this+ " + "+otherItems.get(0));
		if (otherItems.get(0) instanceof WoodStick)
		{
			System.out.println("I have created fire");
			return new Fire();
		}
		return null;
	}

	@Override
	public void Use(Player owner)
	{
		System.out.println("Ahhh, warmth.... aw.. so short and fleeting");
		owner.warmth += 10;
	}	
	
}
class Fire extends UseableThings
{
	@Override
	public Item Combine(ArrayList<Item> otherItems)
	{
		if (otherItems.get(0) instanceof WoodStick)
			return new FireHardenedStick(8,3);
		return null;
	}
	@Override
	public void Use(Player owner)
	{
		owner.warmth += 100;
	}	
}	

class BasicStats {
	int calories, energy, focus, warmth, fatigue;
}

class CombatStats {
	int defense, thermalInsulation, offense;
}

abstract class UseableThings extends Item implements Useable{

}