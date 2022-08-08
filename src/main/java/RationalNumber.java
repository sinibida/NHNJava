public class RationalNumber extends MyNumber {
    public int numerator; //분자
    public int denominator; //분모

    // 1.1 : 정수를 입력받아 초기화
    public RationalNumber(int i) {
        numerator = i;
        denominator = 1;
    }

    // 1.2 : 분자, 분모를 받아 초기화
    public RationalNumber(int num, int den) {
        numerator = num;
        denominator = den;
    }

    // 1.3 : 유리수를 받아 초기화 (Clone)
    public RationalNumber(RationalNumber rn) {
        numerator = rn.numerator;
        denominator = rn.denominator;
    }

    // 1.4, 1.5 : 유리수를 문자열로 표기.
    @Override
    public String toString() {
        // 기약 분수로 변환; optimize() 참고.
        var op = optimize();

        // 기약 분수의 분모가 1이라면, 정수로 표현
        if (op.denominator == 1)
            return Integer.toString(op.numerator);
        else
            return String.format("%d/%d", op.numerator, op.denominator);
    }

    // 2.1 : 유리수 + 유리수
    @Override
    public MyNumber plus(MyNumber operand) {
        if (!(operand instanceof RationalNumber rn))
            throw new IllegalArgumentException("The type of operand must be 'RationalNumber'");

        // 두 피연산자(this, operand)의 분모를 양수로 변환. (최소공배수를 구하기 위해)
        RationalNumber a = positiveDenominator();
        RationalNumber b = rn.positiveDenominator();

        if (a.denominator == b.denominator) // 만약 두 분모가 같다면
            return new RationalNumber(a.numerator + b.numerator, a.denominator).optimize();
        else {
            // 분모의 최소공배수 계산.
            int denLCM = LCM(a.denominator, b.denominator);
            // 각 분수의 분모가 denLCM이 될때의 분자 계산.
            int tNewNum = a.numerator * (denLCM / a.denominator);
            int rNewNum = b.numerator * (denLCM / b.denominator);
            return new RationalNumber(tNewNum + rNewNum, denLCM).optimize();
        }
    }

    // 2.2 : 유리수 - 유리수
    @Override
    public MyNumber minus(MyNumber operand) {
        if (!(operand instanceof RationalNumber rn))
            throw new IllegalArgumentException("The type of operand must be 'RationalNumber'");

        return plus(new RationalNumber(-rn.numerator, rn.denominator));
    }

    // 2.3 : 유리수 * 유리수
    @Override
    public MyNumber multipliedBy(MyNumber operand) {
        if (!(operand instanceof RationalNumber rn))
            throw new IllegalArgumentException("The type of operand must be 'RationalNumber'");

        return new RationalNumber(rn.numerator * numerator, rn.denominator * denominator).optimize();
    }

    // 2.4 : 유리수 / 유리수
    @Override
    public MyNumber dividedBy(MyNumber operand) {
        if (!(operand instanceof RationalNumber rn))
            throw new IllegalArgumentException("The type of operand must be 'RationalNumber'");

        return multipliedBy(new RationalNumber(rn.denominator, rn.numerator));
    }

    // 2.5 : 두 유리수의 값이 같은지 확인
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RationalNumber rn))
            return false;

        return rn.denominator == denominator && rn.numerator == numerator;
    }

    // 분수 비교.
    // this > rn -> 1
    // this == rn -> 0
    // this < rn -> -1
    public int compareWith(RationalNumber rn) {
        RationalNumber a = positiveDenominator();
        RationalNumber b = rn.positiveDenominator();
        return Integer.compare(a.numerator * b.denominator, b.numerator * a.denominator);
    }

    // 이 분수를 분모가 양수인 기약분수로 변환
    public RationalNumber optimize() {
        var pd = positiveDenominator();
//        int gcd = GCD(Math.abs(numerator), Math.abs(denominator));
        int gcd = GCD(
                numerator < 0 ? -numerator : numerator,
                denominator < 0 ? -denominator : denominator);
        if (gcd == 1) // 분자와 분모가 서로소일 경우
            return pd;
        else
            return new RationalNumber(numerator / gcd, denominator / gcd);
    }

    // 분모를 양수로 변환. (분수의 크기는 유지)
    public RationalNumber positiveDenominator() {
        if (denominator < 0)
            return new RationalNumber(-numerator, -denominator);
        else
            return this;
    }

    // 최소 공배수 계산. ( a * b / [a와 b의 최대 공약수] )
    private int LCM(int a, int b) {
        return a * b / GCD(a, b);
    }

    // 최대 공약수 계산; 유클리드 호제법 사용.
    // https://ko.wikipedia.org/wiki/%EC%9C%A0%ED%81%B4%EB%A6%AC%EB%93%9C_%ED%98%B8%EC%A0%9C%EB%B2%95
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
