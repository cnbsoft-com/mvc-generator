package com.cnbsoft.plugin.generator.interactive;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * 대화형 테이블 선택 I/O 전담 클래스.
 * InputStream / PrintStream 주입으로 테스트 가능하도록 설계.
 */
public class TableSelector {

    private final Scanner scanner;
    private final PrintStream out;

    public TableSelector(InputStream in, PrintStream out) {
        this.scanner = new Scanner(in, "UTF-8");
        this.out     = out;
    }

    /**
     * 테이블 패턴 입력을 받아 분리된 패턴 목록을 반환.
     * 구분자: ',' 또는 공백. 빈 항목 제거.
     */
    public List<String> promptForPatterns() {
        out.println();
        out.println("Enter table pattern(s) [e.g. %USER%, ORDER%], comma or space separated:");
        out.print("> ");
        out.flush();
        String line = scanner.nextLine().trim();

        List<String> patterns = new ArrayList<>();
        for (String token : line.split("[,\\s]+")) {
            String t = token.trim();
            if (!t.isEmpty()) patterns.add(t);
        }
        return patterns;
    }

    /**
     * 매칭된 테이블 목록을 출력하고 사용자 확인을 받는다.
     * - y      → 전체 선택
     * - n      → 재입력 (빈 리스트 반환)
     * - "1,3"  → 번호로 부분 선택
     */
    public List<String> confirmTables(List<String> candidates) {
        out.println();
        out.printf("Matching tables (%d found):%n", candidates.size());
        for (int i = 0; i < candidates.size(); i++) {
            out.printf("  %d. %s%n", i + 1, candidates.get(i));
        }
        out.println();
        out.print("Confirm? (y/a=all, n=retry, or numbers e.g. 1,2): ");
        out.flush();
        String answer = scanner.nextLine().trim().toLowerCase();

        if ("y".equals(answer) || "a".equals(answer)) {
            return new ArrayList<>(candidates);
        }
        if ("n".equals(answer) || answer.isEmpty()) {
            return Collections.emptyList();
        }

        // 번호 선택
        List<String> selected = new ArrayList<>();
        for (int n : parseNumbers(answer)) {
            if (n >= 1 && n <= candidates.size()) {
                selected.add(candidates.get(n - 1));
            }
        }
        if (selected.isEmpty()) {
            out.println("No valid selection. Retrying...");
        }
        return selected;
    }

    /**
     * 생성 종류 선택 메뉴를 출력하고 사용자 선택을 받는다.
     * 빈 입력이면 ALL을 기본으로 한다.
     */
    public Set<GenerationType> selectGenerationTypes() {
        out.println();
        out.println("Select generation type(s) (comma-separated numbers):");
        for (GenerationType t : GenerationType.values()) {
            out.printf("  %d. %s%n", t.number, t.label);
        }
        out.print("> ");
        out.flush();
        String answer = scanner.nextLine().trim();

        if (answer.isEmpty()) {
            out.println("No input — defaulting to All.");
            return EnumSet.of(GenerationType.ALL);
        }

        Set<GenerationType> types = new LinkedHashSet<>();
        for (int n : parseNumbers(answer)) {
            GenerationType.fromNumber(n).ifPresent(types::add);
        }
        if (types.isEmpty()) {
            out.println("No valid selection — defaulting to All.");
            types.add(GenerationType.ALL);
        }
        return types;
    }

    /**
     * 생성 완료 후 계속 여부를 묻는다.
     * y/a/엔터 → true(계속), n → false(종료)
     */
    public boolean promptContinue() {
        out.println();
        out.print("Generate more? (y=continue, n=exit): ");
        out.flush();
        String answer = scanner.nextLine().trim().toLowerCase();
        return !"n".equals(answer);
    }

    /** "1,3 4" 형태의 입력을 정수 목록으로 파싱 */
    private List<Integer> parseNumbers(String input) {
        List<Integer> numbers = new ArrayList<>();
        for (String token : input.split("[,\\s]+")) {
            try {
                numbers.add(Integer.parseInt(token.trim()));
            } catch (NumberFormatException ignored) {
            }
        }
        return numbers;
    }
}
