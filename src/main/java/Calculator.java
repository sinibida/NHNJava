import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Calculator {
    public List<String> parsing(String expression, String pattern) {
        List<String> ret = new java.util.ArrayList<>(List.of());
        var matcher = Pattern.compile(pattern).matcher(expression);
        int lastEnd = -1;
        while (matcher.find()) {
            if (lastEnd != -1) {
                String ss = expression.substring(lastEnd, matcher.start());
                for (int i = 0; i < ss.length(); i++) {
                    char c = ss.charAt(i);
                    if (c != ' ')
                        ret.add(Character.toString(c));
                }
            }
            ret.add(matcher.group());
            lastEnd = matcher.end();
        }
        if (lastEnd != expression.length()) {
            ret.add(expression.substring(lastEnd).trim());
        }
        return ret;
    }

    public List<String> infixToPostfix(List<String> expression, String pattern) {
        Stack<String> stack = new Stack<>();
        List<String> ret = new ArrayList<>();
        for (String elem : expression) {
            if (Pattern.matches(pattern, elem)) {
                ret.add(elem);
            }
            else {
                if (elem.equals("(")) {
                    stack.add("(");
                }
                else if (elem.equals(")")) {
                    while (!stack.isEmpty()) {
                        String op = stack.pop();
                        if (op.equals("("))
                            break;
                        else
                            ret.add(op);
                    }
                }
                else {
                    int p = operationPriority(elem);
                    while (!stack.isEmpty()) {
                        String le = stack.lastElement();
                        if (le.equals("("))
                            break;

                        int lastP = operationPriority(le);
                        if (lastP < p)
                            break;
                        else {
                            ret.add(stack.pop());
                        }
                    }
                    stack.push(elem);
                }
            }
        }
        while (!stack.isEmpty()) {
            ret.add(stack.pop());
        }
        return ret;
    }

    private int operationPriority(String op) {
        return switch (op) {
            case "*", "/" -> 2;
            case "+", "-" -> 1;
            default -> throw new IllegalArgumentException("Unknown operation");
        };
    }

    public static <T extends MyNumber> T run(String expression,
                                             String pattern,
                                             Function<String, T> converter) {
        Calculator calc = new Calculator();

        List<String> postFixExp = calc.infixToPostfix(calc.parsing(expression, pattern), pattern);

        Stack<T> stack = new Stack<>();

        for (String elem : postFixExp) {
            if (Pattern.matches(pattern, elem)) {
                stack.push(converter.apply(elem));
            }
            else {
                T b = stack.pop();
                T a = stack.pop();
                switch (elem) {
                    case "+" -> stack.push((T) a.plus(b));
                    case "-" -> stack.push((T) a.minus(b));
                    case "*" -> stack.push((T) a.multipliedBy(b));
                    case "/" -> stack.push((T) a.dividedBy(b));
                    default -> throw new IllegalArgumentException("Unknown operator");
                }
            }
        }
        return stack.lastElement();
    }
}
