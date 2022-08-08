public class RationalNumber extends MyNumber {
    public int numerator; //분자
    public int denominator; //분모

    public RationalNumber(int i) {
        numerator = i;
        denominator = 1;
    }

    public RationalNumber(int num, int den) {
        numerator = num;
        denominator = den;
    }

    public RationalNumber(RationalNumber rn) {
        numerator = rn.numerator;
        denominator = rn.denominator;
    }

    @Override
    public MyNumber plus(MyNumber operand) {
        if (!(operand instanceof RationalNumber rn))
            throw new IllegalArgumentException("The type of operand must be 'RationalNumber'");

        RationalNumber a = positiveDenominator();
        RationalNumber b = rn.positiveDenominator();
        if (a.denominator == b.denominator)
            return new RationalNumber(a.numerator + b.numerator, a.denominator).optimize();
        else {
            int denLCM = LCM(a.denominator, b.denominator);
            int tNewNum = a.numerator * (denLCM / a.denominator);
            int rNewNum = b.numerator * (denLCM / b.denominator);
            return new RationalNumber(tNewNum + rNewNum, denLCM).optimize();
        }
    }

    @Override
    public MyNumber minus(MyNumber operand) {
        if (!(operand instanceof RationalNumber rn))
            throw new IllegalArgumentException("The type of operand must be 'RationalNumber'");

        return plus(new RationalNumber(-rn.numerator, rn.denominator));
    }

    @Override
    public MyNumber multipliedBy(MyNumber operand) {
        if (!(operand instanceof RationalNumber rn))
            throw new IllegalArgumentException("The type of operand must be 'RationalNumber'");

        return new RationalNumber(rn.numerator * numerator, rn.denominator * denominator).optimize();
    }

    @Override
    public MyNumber dividedBy(MyNumber operand) {
        if (!(operand instanceof RationalNumber rn))
            throw new IllegalArgumentException("The type of operand must be 'RationalNumber'");

        return multipliedBy(new RationalNumber(rn.denominator, rn.numerator));
    }

    @Override
    public String toString() {
        var op = optimize();
        if (op.denominator == 1)
            return Integer.toString(op.numerator);
        else
            return String.format("%d/%d", op.numerator, op.denominator);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RationalNumber rn))
            return false;

        return rn.denominator == denominator && rn.numerator == numerator;
    }

    public int compareWith(RationalNumber rn) {
        RationalNumber a = positiveDenominator();
        RationalNumber b = rn.positiveDenominator();
        return Integer.compare(a.numerator * b.denominator, b.numerator * a.denominator);
    }

    public RationalNumber optimize() {
        var pd = positiveDenominator();
        int gcd = GCD(
                numerator < 0 ? -numerator : numerator,
                denominator < 0 ? -denominator : denominator);
        if (gcd == 1)
            return pd;
        else
            return new RationalNumber(numerator / gcd, denominator / gcd);
    }

    public RationalNumber positiveDenominator() {
        if (denominator < 0)
            return new RationalNumber(-numerator, -denominator);
        else
            return this;
    }

    private int LCM(int a, int b) {
        return a * b / GCD(a, b);
    }

    private int GCD(int a, int b) {
        int temp;
        while (b > 0) {
            temp = a % b;
            a = b;
            b = temp;
        }
        return a;
    }
}
