public class MyInteger extends MyNumber {
    public int x;

    public MyInteger(int i) {
        x = i;
    }

    @Override
    public MyNumber plus(MyNumber operand) {
        if (!(operand instanceof MyInteger mi))
            throw new IllegalArgumentException("The type of operand must be 'MyInteger'");

        return new MyInteger(x + mi.x);
    }

    @Override
    public MyNumber minus(MyNumber operand) {
        if (!(operand instanceof MyInteger mi))
            throw new IllegalArgumentException("The type of operand must be 'MyInteger'");

        return new MyInteger(x - mi.x);
    }

    @Override
    public MyNumber multipliedBy(MyNumber operand) {
        if (!(operand instanceof MyInteger mi))
            throw new IllegalArgumentException("The type of operand must be 'MyInteger'");

        return new MyInteger(x * mi.x);
    }

    @Override
    public MyNumber dividedBy(MyNumber operand) {
        if (!(operand instanceof MyInteger mi))
            throw new IllegalArgumentException("The type of operand must be 'MyInteger'");

        return new MyInteger(x / mi.x);
    }

    @Override
    public String toString() {
        return Integer.toString(x);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MyInteger mi))
            return false;

        return mi.x == x;
    }

    public int compareWith(MyInteger mi) {
        return Integer.compare(x, mi.x);
    }
}
