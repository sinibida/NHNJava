import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Program {
    private static List<RationalNumber> sort(List<RationalNumber> rnList) {
        return rnList.stream().sorted(RationalNumber::compareWith).collect(Collectors.toList());
    }

    private static RationalNumber sum(List<RationalNumber> rnList) {
        return rnList.stream().reduce(new RationalNumber(0, 1), (a, b) -> (RationalNumber) a.plus(b));
    }

    private static RationalNumber posSum(List<RationalNumber> rnList) {
        return rnList.stream()
                .filter(x -> x.denominator * x.numerator > 0)
                .reduce(new RationalNumber(0, 1), (a, b) -> (RationalNumber) a.plus(b));
    }

    public static void main(String[] args) {
        RationalNumber a = new RationalNumber(2, 3);
        RationalNumber b = new RationalNumber(3, 4);
        //RationalNumber c = new RationalNumber(17, 12);
        System.out.println(a);
        System.out.println(b);
        System.out.println(a.plus(b));
        System.out.println(a.minus(b));
        System.out.println(a.multipliedBy(b));
        System.out.println(a.dividedBy(b));
        System.out.println(a.compareWith(b));

        List<RationalNumber> rnList = Arrays.asList(
                new RationalNumber(3, 5),
                new RationalNumber(5, -1),
                new RationalNumber(4, 1),
                new RationalNumber(-4, 2),
                new RationalNumber(5, 6),
                new RationalNumber(4, 3)
        );
        List<RationalNumber> sorted = sort(rnList);
        System.out.println("==================");
        for (RationalNumber rn :
                sorted) {
            System.out.println(rn);
        }
        System.out.println("==================");
        System.out.println(sum(rnList));
        System.out.println(posSum(rnList));

        Calculator calc = new Calculator();
        String infix = "3 * 2 + ( 4 / 5 * -2 )";
        String operandPattern = "-?\\d+";

        List<String> parsed = calc.parsing(infix, operandPattern);
        System.out.println(parsed);
        List<String> postFix = calc.infixToPostfix(parsed, operandPattern);
        System.out.println(postFix);

        System.out.println(Calculator.run(infix, operandPattern, x -> new MyInteger(Integer.parseInt(x))));

        String rationalInfix = "[1,3]*[1,2]+([-2,3] * 4/5)";
        String rationalPattern = "\\[-?\\d+,-?\\d+\\]|-?\\d+";

        List<String> rationalParsed = calc.parsing(rationalInfix, rationalPattern);
        System.out.println(rationalParsed);

        System.out.println(Calculator.run(rationalInfix, rationalPattern, x -> {
            if (Pattern.matches("\\[-?\\d+,-?\\d+\\]", x)) {
                int leftBracket = x.indexOf('[');
                int comma = x.indexOf(',');
                int rightBracket = x.indexOf(']');
                return new RationalNumber(
                        Integer.parseInt(x.substring(leftBracket + 1, comma)),
                        Integer.parseInt(x.substring(comma + 1, rightBracket)));
            }
            else {
                return new RationalNumber(Integer.parseInt(x));
            }
        }));
    }
}
