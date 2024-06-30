package io.github.shaunbharat.minecraftstatushook.utils;

public class Color {
    public static final java.awt.Color RUBY = new java.awt.Color(0xE91E63); // Stop
    public static final java.awt.Color MEDIUM_SEA_GREEN = new java.awt.Color(0x2ECC71); // Start
    public static final java.awt.Color ENDEAVOUR = new java.awt.Color(0x9B59B6); // Leave
    public static final java.awt.Color SKY_BLUE = new java.awt.Color(0x3498DB); // Join

    public static int rgb(java.awt.Color color) {
        // From: https://gist.github.com/k3kdude/fba6f6b37594eae3d6f9475330733bdb
        // int rgb = color.getRed();
        // rgb = (rgb << 8) + color.getGreen();
        // rgb = (rgb << 8) + color.getBlue();

            /*
                Bitwise operators are used for manipulating individual bits in a binary number. They can be used when performing low-level programming tasks where you need to control individual bits.  Here are the basic bitwise operators:
                & (bitwise AND): Takes two numbers as operands and does AND on every bit of two numbers. The result of AND is 1 only if both bits are 1.
                | (bitwise OR): Takes two numbers as operands and does OR on every bit of two numbers. The result of OR is 1 if any of the two bits is 1.
                ^ (bitwise XOR): Takes two numbers as operands and does XOR on every bit of two numbers. The result of XOR is 1 if the two bits are different.
                ~ (bitwise NOT): Takes one number and inverts all bits of it.
                << (left shift): Takes two numbers, left shifts the bits of the first operand, the second operand decides the number of places to shift.
                >> (right shift): Takes two numbers, right shifts the bits of the first operand, the second operand decides the number of places to shift.
                In the context of the code you provided:

                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                int rgb = (red << 16) | (green << 8) | blue;

                This code is combining the red, green, and blue components of a color into a single integer, which represents the color in RGB format.  Here's how it works:
                (red << 16): This shifts the bits of the red component 16 places to the left. In other words, it multiplies the red component by 2^16. This ensures that the red component occupies the most significant (leftmost) 8 bits of the resulting integer.
                (green << 8): This shifts the bits of the green component 8 places to the left, or multiplies the green component by 2^8. This ensures that the green component occupies the middle 8 bits of the resulting integer.
                blue: The blue component doesn't need to be shifted because it occupies the least significant (rightmost) 8 bits of the resulting integer.
                |: The bitwise OR operator is used to combine the red, green, and blue components into a single integer. This works because the red, green, and blue components occupy different bits of the integer, so the OR operation effectively just combines them.
                So, the resulting rgb integer represents the color in RGB format, with the red component in the most significant bits, the green component in the middle, and the blue component in the least significant bits.
             */

        return (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
    }
}
