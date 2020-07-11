package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Scanner;
import java.nio.file.Paths;

public class Main {

	static String[] holder = new String[5];

	public static void main(String[] args) {

		boolean statForFile = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-mode")) {
				holder[0] = args[i + 1];
			} else if (args[i].equals("-key")) {
				holder[1] = args[i + 1];
			} else if (args[i].equals("-data")) {
				holder[2] = args[i + 1];
				statForFile = true;
			} else if (args[i].equals("-in") && statForFile == false) {
				try {
					holder[2] = new String(Files.readAllBytes(Paths.get(args[i + 1])));
				} catch (IOException e) {
					System.out.println("Error");
					System.exit(0);
				}
			} else if (args[i].equals("-out")) {
				holder[3] = args[i + 1];
			} else if (args[i].equals("-alg")) {
				holder[4] = args[i + 1];
			}
		}


        switch (holder[4]){
            case "unicode":
                 Algo algo = new UnicodeAlgo(holder);
                 implementation(algo);
                 break;
            default:
                Algo algo1 = new ShiftAlgo(holder);
                implementation(algo1);
        }


		}

		public static  void implementation(Algo algo){
            switch (holder[0]) {
                case "enc":
                    if (holder[3] == null) {
                        System.out.println(algo.encrypt());
                    } else {
                        File file = new File(holder[3]);
                        PrintWriter writer;
                        try {
                            writer = new PrintWriter(file);
                            writer.write(algo.encrypt());
                            writer.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();

                        }

                    }
                    break;
                case "dec":
                    if (holder[3] == null) {
                        System.out.println(algo.decrypt());
                    } else {
                        File file = new File(holder[3]);
                        PrintWriter writer;
                        try {
                            writer = new PrintWriter(file);
                            writer.write(algo.decrypt());
                            writer.close();
                        } catch (FileNotFoundException e) {
                            System.out.println("Error");;
                        }

                    }
                    break;
                default:
                    algo.encrypt();

            }
        }
	}


abstract class Algo {

	char a;
	char z;
	String[] holder;

	Algo(String[] holder){
		this.holder = holder;
		switch (holder[4]){
			case "unicode":
				this.a = '!';
				this.z = '~';
				break;
			case "shift":
				this.a = 'a';
				
			default:
				this.a = 'a';
				this.z = 'z';
		}
	}

	public abstract String encrypt();

    public abstract String decrypt();
}

class ShiftAlgo extends  Algo{


    ShiftAlgo(String[] holder) {
        super(holder);
    }

    public String encrypt() {
        char[] chars = holder[2].toCharArray();
        if (holder[1].equals("") || holder[1].equals(" ")) {
            holder[1] = "0";
        }
        int shift = Integer.parseInt(holder[1]);
        int size = 26;
        String res = "";
        boolean wasHigher = false;
        for (char item : chars) {
            if(Character.isUpperCase(item)) {
                wasHigher = true;
                item = Character.toLowerCase(item);
            }
            if (item >= a && item <= z) {
                item = (char) (((item - a + shift) % size) + a);
                if(wasHigher == true) {
                    res += Character.toUpperCase(item);
                }else {
                    res += item;
                }

            } else {
                res += item;
            }
            wasHigher = false;

        }
        return res;
    }


    public String decrypt() {
        char[] chars = holder[2].toCharArray();
        if (holder[1].equals("") || holder[1].equals(" ")) {
            holder[1] = "0";
        }
        int shift = Integer.parseInt(holder[1]);
        int size = 125;
        String res = "";
        boolean wasHigher = false;
        for (char item : chars) {
            if(Character.isUpperCase(item)) {
                wasHigher = true;
                item = Character.toLowerCase(item);
            }
            if (item >= a && item <= z) {
                 item = (char) (item - shift);
                if(item < 'a') {
                	item = (char) (item-'a'+'z'+1);
                }
                if(wasHigher == true) {
                    res += Character.toUpperCase(item);
                }else {
                    res += item;
                }

            } else {
                res += item;
            }
            wasHigher = false;

        }
        return res;
    }
}

class UnicodeAlgo extends  Algo{

    UnicodeAlgo(String[] holder) {
        super(holder);
    }

    public String encrypt() {
        char[] chars = holder[2].toCharArray();
        if (holder[1].equals("") || holder[1].equals(" ")) {
            holder[1] = "0";
        }
        int shift = Integer.parseInt(holder[1]);
        String res = "";
        for (char item : chars) {
            if (item >= a && item <= z) {
                char shiftItem = (char) ((item + shift));
                res += shiftItem;
            } else if (item == ' ') {
                res += '%';
            } else {
                res += item;
            }
        }
        return res;
    }


    public String decrypt() {
        char[] chars = holder[2].toCharArray();
        if (holder[1].equals("") || holder[1].equals(" ")) {
            holder[1] = "0";
        }
        int shift = Integer.parseInt(holder[1]);
        String res = "";
        for (char item : chars) {
            if (item >= a && item <= z) {
                char shiftItem = (char) ((item - shift));
                res += shiftItem;
            } else {
                res += item;
            }
        }
        return res;
    }
}