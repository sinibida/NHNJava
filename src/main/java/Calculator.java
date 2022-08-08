import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Calculator {

    // 4.1 : 형태소 단위로 분리
    // pattern은 피연산자 검색에 사용된다.
    public List<String> parsing(String expression, String pattern) {
        // 계산 후 반환될 리스트
        List<String> ret = new java.util.ArrayList<>(List.of());
        // pattern을 이용해 수(피연산자) 검색
        var matcher = Pattern.compile(pattern).matcher(expression);
        // 바로 전 수의 끝 인덱스
        int lastEnd = -1;

        while (matcher.find()) {
            if (lastEnd != -1) { // 첫번째 수가 아니라면
                // 이 수와 이전 수 사이 연산자를 ret에 저장.
                String ss = expression.substring(lastEnd, matcher.start());
                for (int i = 0; i < ss.length(); i++) {
                    char c = ss.charAt(i);
                    if (c != ' ')
                        ret.add(Character.toString(c));
                }
            }
            // ret에 수 저장.
            ret.add(matcher.group());
            lastEnd = matcher.end();
        }
        // 마지막 수 뒤의 연산자 저장.
        if (lastEnd != expression.length()) {
            ret.add(expression.substring(lastEnd).trim());
        }
        return ret;
    }

    // 4.2 : 후위 표기법으로 전환
    public List<String> infixToPostfix(List<String> expression, String pattern) {
        Stack<String> stack = new Stack<>();
        List<String> ret = new ArrayList<>();
        for (String elem : expression) {
            if (Pattern.matches(pattern, elem)) { // 1. 피연산자는 출력한다.
                ret.add(elem);
            }
            else {
                if (elem.equals("(")) { // 2. "("이 나오면 스택에 저장한다.
                    stack.add("(");
                }
                else if (elem.equals(")")) { // 3. ")"이 나오면 스택에서 "(" 바로 앞까지 저장한다.
                    while (!stack.isEmpty()) {
                        String op = stack.pop();
                        if (op.equals("("))
                            break;
                        else
                            ret.add(op);
                    }
                }
                else { // 4. 4칙 연산자, 5. 연산자 출력
                    int p = operationPriority(elem);
                    while (!stack.isEmpty()) {
                        String le = stack.lastElement();
                        if (le.equals("("))
                            break;

                        int lastP = operationPriority(le);
                        if (lastP < p)
                            break;
                        else { // 스택 안 연산자의 우선순위가 높거나 같으면
                            // 스택 안의 연산자를 모두 출력
                            ret.add(stack.pop());
                        }
                    }
                    stack.push(elem);
                }
            }
        }
        while (!stack.isEmpty()) { // 6. 스택안의 연산자를 모두 출력한다.
            ret.add(stack.pop());
        }
        return ret;
    }

    // 연산자의 우선순위 구하기
    private int operationPriority(String op) {
        return switch (op) {
            case "*", "/" -> 2;
            case "+", "-" -> 1;
            default -> throw new IllegalArgumentException("Unknown operation");
        };
    }

    // 수식 계산
    public static <T extends MyNumber> T run(String expression,
                                             String pattern,
                                             Function<String, T> converter) {
        // String 수식을 후위 표기법으로 변환
        Calculator calc = new Calculator();

        List<String> postFixExp = calc.infixToPostfix(calc.parsing(expression, pattern), pattern);

        // 피연산자를 저장할 스택
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
