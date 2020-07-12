package xyz.crystallizedprison.generators;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class Generator {
    private String owner;
    private double moneydue;
    private int SpeedLevel,MoneyLevel,BlocksMined;
    private Location loc;
    private String type;
    private Long TimeLeft = null;
    private ArmorStand ArmourStand;


    public Generator(String owner, double moneydue, int speedLevel, int moneyLevel, int blocksMined, Location loc, String type, ArmorStand armourStand) {
        this.owner = owner;
        this.moneydue = moneydue;
        SpeedLevel = speedLevel;
        MoneyLevel = moneyLevel;
        BlocksMined = blocksMined;
        this.loc = loc;
        this.type = type;
        ArmourStand = armourStand;
    }

    public Long getTimeLeft() {
        return TimeLeft;
    }

    public void setTimeLeft(Long timeLeft) {
        TimeLeft = timeLeft;
    }

    public String getOwner() {
        return owner;
    }

    public double getMoneydue() {
        return moneydue;
    }

    public int getSpeedLevel() {
        return SpeedLevel;
    }

    public int getMoneyLevel() {
        return MoneyLevel;
    }

    public Location getLoc() {
        return loc;
    }

    public ArmorStand getArmourStand() {
        return ArmourStand;
    }

    public void setArmourStand(ArmorStand armourStand) {
        ArmourStand = armourStand;
    }

    public String getType() {
        return type;
    }

    public int getBlocksMined() {
        return BlocksMined;
    }

    public void setBlocksMined(int blocksMined) {
        BlocksMined = blocksMined;
    }

    public void setMoneydue(double moneydue) {
        this.moneydue = moneydue;
    }

    public void setSpeedLevel(int speedLevel) {
        SpeedLevel = speedLevel;
    }

    public void setMoneyLevel(int moneyLevel) {
        MoneyLevel = moneyLevel;
    }
}
