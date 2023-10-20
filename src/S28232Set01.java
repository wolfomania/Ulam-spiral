package src;

import java.awt.*;
import java.io.*;

public class S28232Set01 extends Frame {

    public static void main(String[] args) {

        //Additional values

        String file = "numbers.bin";
        String numb = "amountOfNumbers.bin";
        File f = new File(numb);
        File f1 = new File(file);
        int limit = 8295000; // up to this point prime numbers will be calculated
        //8295000 is more than enough even for 4k (8294400) monitors

        /*
        Amount of prime numbers

        Probably calculating first number in lines for numbers.bin
        */
        if(!f.exists()){
            try (DataOutputStream out = new DataOutputStream(new FileOutputStream(numb))
            ) {

                for (int i = 1, k = 0, power = 8, g = power, amount = 0; i <= limit; i++) {
                    if (isPrime(i)) {
                        amount++;
                        if (amount >= Math.pow(2, power)) {
                            power += 8;
                            k++;
                        }
                    }
                    if (i == Math.pow(2, g) || i == limit) {
                        g += 8;
                        for (int m = k; m >= 0; m--) {
                            out.write((amount >>> (8 * m)) & 0xFF);
                        }
                        amount = 0;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Calculating and writing prime numbers //

        if(!f1.exists()) {
            try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
                 DataInputStream input = new DataInputStream(new FileInputStream(numb))
            ) {

                //Writing the first number of the first line
                long temp1 = input.read();
                for (int m = 7; m >= 0; m--) {
                    out.write((byte)(temp1 >>> (8 * m)) & 0xFF);
                }
                for (int i = 1, k = 0, j = 8; i <= limit; i++) {

                    //Writing the end of the line and the beginning of the next line
                    if (i == Math.pow(2, j)) {
                        j += 8;
                        k++;
                        out.write('\n');
                        long temp = 0;
                        for (int m = k; m >= 0; m--) {
                            temp = (temp << 8) + input.read();
                        }
                        for (int m = 7; m >= 0; m--) {
                            out.write((byte)(temp >>> (8 * m)) & 0xFF);
                        }
                    }
                    if (isPrime(i)) {
                        for (int m = k; m >= 0; m--) {
                            out.write((byte)(i >>> (8 * m)) & 0xFF);
                        }
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //
        //End of pre-calculation
        //

        new S28232Set01();

    }
    public S28232Set01() {

        super();
        this.setSize(640, 640);
        this.setVisible(true);

    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        try(DataInputStream input = new DataInputStream(new FileInputStream("numbers.bin"))) {

            long temp = 0;

            //Reading the very first 8-byte number
            for (int m = 7; m >= 0; m--) {
                temp = (temp << 8) + input.read();
            }
            //Reading the very first prime number
            int pNum = input.read();

            //To fill a full screen instead of the square shape  = > change '&&' to '||'
            for (int x = this.getWidth() / 2, y = this.getHeight() / 2,
                 counter = 1, state = 0, numOfBytes = 1, numOfSteps = 1;
                 x <= this.getWidth() && y <= this.getHeight(); ) {

                for (int i = 0; i < numOfSteps; i++) {
                    if (counter == pNum) {

                        //Remove commenting of the lines below to make it more fashionable
                        /*g.setColor(new Color(
                                (float) Math.random(),
                                (float) Math.random(),
                                (float) Math.random()
                        ));*/

                        g.fillRect(x, y, 1, 1);

                        temp--;
                        pNum = 0;

                        //Reaching the end of a line
                        if (temp == 0) {
                            numOfBytes++;
                            //Reading a cha; byte with '\n'
                            input.read();
                            //Reading the 8-byte number at the beginning of the new line
                            for (int m = 7; m >= 0; m--) {
                                temp = (temp << 8) + input.read();
                            }
                        }
                        for (int m = numOfBytes; m > 0; m--) {
                            pNum = (pNum << 8) + input.read();
                        }
                    }

                    counter++;
                    //Moving around
                    switch (state) {
                        case 0: // go right
                            x++;
                            break;
                        case 1: // go up
                            y--;
                            break;
                        case 2: // go left
                            x--;
                            break;
                        case 3: // go down
                            y++;
                        default:
                            break;
                    }

                }
                state++;
                if (state % 2 == 0) {
                    numOfSteps++;
                    if (state % 4 == 0)
                        state = 0;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static boolean isPrime(int num){
        if(num == 1) return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if(num%i==0)
            {
                return false;
            }
        }
        return true;
    }


}