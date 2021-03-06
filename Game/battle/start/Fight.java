package start;

import item.Item;

import java.util.ArrayList;
import java.util.Scanner;
import objects.IObject;
import monsters.Monster;
import engine.Engine;
import engine.User;

public class Fight {
	private int turns;
	private User player;
	private Monster npc;
	private String area;
	private boolean playerWin;
	private boolean playerDie;
	private int exp;
	private boolean flee;
	private int dmg;
	private int startTurn;
	private int newDmg;

	public Fight(User player, Monster first, String area) {
		this.player = player;
		this.npc = first;
		this.area = area;
		turns = 0;
		exp = (int) (first.getLevel() / player.getLevel() * 100);
		playerWin = false;
		playerDie = false;
		newDmg = 0;
	}

	public String battle() {
		// DUMMY BATTLE//
		if (npc.getName().equals("Dummy")) {
			flee = false;
			Scanner in = new Scanner(System.in);
			System.out.println();
			System.out.println(player.getName() + " VS " + npc.getName());
			System.out.println();
			// System.out.println(player);
			// System.out.println(npc);
			// System.out.println();
			while (true) {
				if (player.getHealth() <= 0 && npc.getHealth() <= 0) {
					System.out.println("Draw!");
				}
				if (player.getHealth() <= 0) {
					playerDie = true;
					break;
				}
				if (npc.getHealth() <= 0) {
					playerWin = true;
					break;
				}
				System.out.println("Please choose your move:");
				System.out.println("Attack");// Calls the attack method
				// System.out.println("Use item");//
				System.out.println("Flee");//
				String option = in.nextLine();

				if (option.toLowerCase().equals("attack")) {
					System.out.println();
					attack(2);
					System.out.println();
				} else if (option.toLowerCase().equals("use item")) {
					// YOU SHOULD NOT NEED ITEMS IN DUMMY BATTLE//
					// Engine.fightInvo();
					// System.out.print("What item would you like to use: ");
					// String item = in.nextLine();
				} else if (option.toLowerCase().equals("flee")) {
					System.out.println();
					player.getBack();
					flee = true;
					break;
				} else {
					System.out.println("Please try again.");
				}
			}
			if (!flee) {
				if (playerWin) {
					player.addExp(exp);
					// Will change later to reset method
					player.setHealth(100);
					return "You Won!";
				} else {
					return "You Died...";
				}
			} else {
				return "You run away.";
			}
		} else {
			flee = false;
			Scanner in = new Scanner(System.in);
			System.out.println();
			System.out.println(player.getName() + " VS " + npc.getName());
			System.out.println("You will get " + exp + " exp");
			System.out.println();
			// System.out.println(player);
			// System.out.println(npc);
			// System.out.println();
			while (true) {
				// After 3 turns of extra dmg the potion wears off
				if (turns - startTurn > 3) {
					newDmg = 0;
					System.out.println("You loose the effects of the potion");
				}
				if (player.getHealth() <= 0 && npc.getHealth() <= 0) {
					System.out.println("Draw!");
				}
				if (player.getHealth() <= 0) {
					playerDie = true;
					break;
				}
				if (npc.getHealth() <= 0) {
					playerWin = true;
					break;
				}
				System.out.println("Please choose your move:");
				System.out.println("Attack");// Calls the attack method
				System.out.println("Use item");//
				System.out.println("Flee");//
				String option = in.nextLine();

				if (option.toLowerCase().equals("attack")) {
					System.out.println();
					attack(1);
					System.out.println();
					attack(2);
					System.out.println();
				} else if (option.toLowerCase().equals("use item")) {
					ArrayList<Item> invo = Engine.fightInvo();
					if (invo.size() == 0) {
						System.out.println("You have no items!");
					} else {
						System.out.println(invo);
						System.out.print("What item would you like to use: ");
						String item = in.nextLine();
						for (Item i : invo) {
							if (item.equals(i.getName().toLowerCase())) {
								System.out.print("You used " + i.getName());
								if (i.getEffect().equals("heal")) {
									System.out.println(" and you gained "
											+ i.getPower() + " health!");
									player.addHealth(i.getPower());
								} else if (i.getEffect().equals("str")) {
									System.out.println();
									startTurn = turns;
									newDmg = i.getDmg();
								} else if (i.getEffect().equals("mana")) {
									System.out.println(" and you gained "
											+ i.getPower() + " mana!");
									player.addMana(i.getPower());
								}
								break;
							}
						}
					}
				} else if (option.toLowerCase().equals("flee")) {
					attack(1);
					System.out.println();
					flee = true;
					break;
				} else {
					System.out.println("Please try again.");
				}
			}
			if (!flee) {
				if (playerWin) {
					player.addExp(exp);
					System.out.println(npc.getName()
							+ " dropped "
							+ player.addGold((int) ((npc.getLevel() / player
									.getLevel()) * 10)) + " gold");
					// player.addGold((int) ((npc.getLevel()/player.getLevel())
					// * 10));
					player.setHealth(100);
					return "You Won!";
				} else {
					return "You Died...";
				}
			} else {
				return "You run away.";
			}
		}
	}

	/**
	 * 1 == npc (this method will mainly use one) 2 == user
	 */
	public void attack(int attacker) {
		// System.out.println("npc" + npc.getDef());
		// System.out.println("npc dmg " + npc.getDmg());
		// System.out.println("player" + player.getDef());
		// System.out.println("player dmg " + player.getDmg());
		int playerDmg = 0;
		int npcDmg = 0;
		if (!npc.getName().equals("Dummy")) {
			npcDmg = (npc.getLevel() * npc.getDmg() * 3)
					- (player.getDef() / npc.getDmg());
			playerDmg = (int) ((player.getLevel() * player.getDmg() * 3)
					- (npc.getDef() / npc.getDmg()) + newDmg);
			int playerRng = playerDmg / 2;
			int npcRng = npcDmg / 2;
			// System.out.println("before");
			// System.out.println(playerDmg);
			// System.out.println(npcDmg);
			playerDmg = playerDmg + range(playerRng);
			npcDmg = npcDmg + range(npcRng);
		} else {
			playerDmg = (int) ((player.getLevel() * player.getDmg() * 6) + newDmg);
			int playerRng = playerDmg + 1;
			npcDmg = 0;
		}
		// System.out.println("after");
		// System.out.println(playerDmg);
		// System.out.println(npcDmg);
		if (attacker == 1) {
			// if ((int) (Math.random() * 100) + 1 <= dodge()) {
			// System.out.println("You dodged their attack!");
			// } else
			if ((int) (Math.random() * 100) + 1 <= block()) {
				System.out.println("You blocked their attack!");
			} else if (npcDmg > 0) {
				System.out.println("You have been hit!");
				System.out.println(npcDmg);

				player.setHealth(player.getHealth() - npcDmg);

				if (player.getHealth() <= 0) {

				} else {
					System.out.println("You now have " + player.getHealth());
				}
			} else {
				System.out.println("He missed!");
			}
		}
		if (attacker == 2) {
			if (playerDmg > 0) {
				System.out.println("You hit them!");
				System.out.println(playerDmg);

				npc.setHealth(npc.getHealth() - playerDmg);

				if (npc.getHealth() <= 0) {
					playerWin = true;
				} else {
					System.out.println("They now have " + npc.getHealth());
				}
			} else {
				System.out.println("You missed!");
			}
		}

	}

	// public static int dodge() {
	// double dodge = (npc.getWt() / player.getWt()) * 10;
	// return (int) dodge;
	// }

	public int block() {
		double block = (player.getDef() / npc.getDmg()) * 10;
		return (int) block;
	}

	public int range(int range) {
		int rand = (int) (Math.random() * range);
		int posneg = (int) (Math.random() * 2);
		if (posneg == 0) {// 0 adds the rand
			// System.out.println("add "+ rand);
			return rand;
		} else if (posneg == 1) {// 1 subtracts the rand
			// System.out.println("minus " + rand * -1);
			return rand * -1;
		} else {
			return 0;
		}
	}

	public double[] hitPer() {
		int playerDmg = (int) ((player.getLevel() * player.getDmg()) - (npc
				.getDef() / 2));
		System.out.println(playerDmg);
		int d = 1;
		double[] dmgPer = new double[playerDmg];
		int j = playerDmg - 1;
		for (int i = 0; i < playerDmg; i++) {
			dmgPer[i] = 1 / playerDmg;
			// System.out.println(dmgPer[i]);
		}
		for (int w = d; w < (playerDmg / 2) - d; w++) {
			for (int i = 0; i < 2; i++) {
				dmgPer[i] = (1 / playerDmg) / (playerDmg - 2);
				System.out.println(dmgPer[i]);
				dmgPer[j] = (1 / playerDmg) / (playerDmg - 2);
				j--;
			}
			dmgPer[w] = (1 / playerDmg) / (playerDmg - 2) + (1 / playerDmg);
			// System.out.println(dmgPer[w]);
		}
		d++;
		return null;
	}

	public boolean isPlayerDead() {
		return playerDie;
	}

	public boolean playerWin() {
		return playerWin;
	}

	public void restart() {
		npc.setHealth(100);
		turns = 0;
		playerWin = false;
		playerDie = false;
	}

	public void respawn() {
		playerDie = false;
	}

	public void setNpc(Monster npc) {
		this.npc = npc;
	}

	public boolean playerFlee() {
		return flee;
	}
}
