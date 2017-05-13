package de.skosnowich.ld38.gameobject;

public class Upgrade
{
	private static Upgrade instance = null;
	private int healthLevel, regenerationLevel, strengthLevel, speedLevel, money;

	public static Upgrade getInstance()
	{
		if (instance == null)
		{
			instance = new Upgrade();
		}
		return instance;
	}

	private Upgrade()
	{

	}

	public void reset()
	{
		healthLevel = 0;
		regenerationLevel = 0;
		strengthLevel = 0;
		speedLevel = 0;
		money = 0;
	}

	public int getHealthLevel()
	{
		return healthLevel;
	}

	public int getRegenerationLevel()
	{
		return regenerationLevel;
	}

	public int getStrengthLevel()
	{
		return strengthLevel;
	}

	public int getSpeedLevel()
	{
		return speedLevel;
	}

	public int getMoney()
	{
		return money;
	}

	public boolean buyHealth()
	{
		if (money < getHealthPrice())
		{
			return false;
		}
		money -= getHealthPrice();
		healthLevel++;
		return true;
	}

	public boolean buyStrength()
	{
		if (money < getStrengthPrice())
		{
			return false;
		}
		money -= getStrengthPrice();
		strengthLevel++;
		return true;
	}

	public boolean buyRegeneration()
	{
		if (money < getRegenerationPrice())
		{
			return false;
		}
		money -= getRegenerationPrice();
		regenerationLevel++;
		return true;
	}

	public boolean buySpeed()
	{
		if (money < getSpeedPrice())
		{
			return false;
		}
		money -= getSpeedPrice();
		speedLevel++;
		return true;
	}

	public void addMoney(int value)
	{
		money += value;
	}

	public int getHealth()
	{
		return 100 + healthLevel * 10;
	}

	public int getStrength()
	{
		return 100 + strengthLevel * 10;
	}

	public int getRegeneration()
	{
		return regenerationLevel;
	}

	public float getSpeed()
	{
		return 20f + speedLevel;
	}

	public int getHealthPrice()
	{
		return 10 + healthLevel;
	}

	public int getStrengthPrice()
	{
		return 10 + strengthLevel;
	}

	public int getRegenerationPrice()
	{
		return 10 + regenerationLevel;
	}

	public int getSpeedPrice()
	{
		return 5 + speedLevel;
	}
}
