import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int L, N, Q;
    static int[][] arr;
    // 상우하좌
    static final int[] dr = {-1, 0, 1, 0};
    static final int[] dc = {0, 1, 0, -1};
    static boolean[] liveKnight;
    static int[] initial;
    static Node[] knightPosition;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        L = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());
        arr = new int[L][L];
        for (int r = 0; r < L; r++) {
            st = new StringTokenizer(br.readLine());
            for (int c = 0; c < L; c++) {
                arr[r][c] = Integer.parseInt(st.nextToken());
            }
        }
        liveKnight = new boolean[N + 1];
        initial = new int[N + 1];
        knightPosition = new Node[N + 1];
        for (int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());
            int startX = Integer.parseInt(st.nextToken()) - 1;
            int startY = Integer.parseInt(st.nextToken()) - 1;
            int height = Integer.parseInt(st.nextToken()) - 1;
            int length = Integer.parseInt(st.nextToken()) - 1;
            int health = Integer.parseInt(st.nextToken());
            initial[i] = health;
            knightPosition[i] = new Node(startX, startY, height, length, health, i);
        }
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int number = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());
            if (liveKnight[number]) continue;
            push(number, dir);
        }
        int sum = 0;
        for (int i = 1; i <= N; i++) {
            if (liveKnight[i]) continue;
            sum += initial[i] - knightPosition[i].health;
        }
        System.out.println(sum);

    }

    private static boolean push(int cnt, int dir) {
        // 밀려났는지 판별하는 boolean
        boolean[] isPushed = new boolean[N + 1];
        // 복제용
        Node[] compare = new Node[N + 1];
        // 배열 복사
        compare = knightPosition.clone();

        // 입력받은 기사의 위치를 q에 담는다.
        Queue<Node> q = new LinkedList<>();
        q.offer(compare[cnt]);

        while (!q.isEmpty()) {
            Node poll = q.poll();
            // 꺼낸 다음 위치 이동시킨다.
            int nr = poll.x + dr[dir];
            int nc = poll.y + dc[dir];
            // 기사의 위치를 먼저 변경시킨다.
            compare[poll.num] = new Node(nr, nc, poll.height, poll.length, poll.health, poll.num);
            if (nr < 0 || nc < 0 || nr >= N + poll.height || nc >= N + poll.length) return false;
            // 범위를 벗어나거나 벽이면 return 한다.
            // 다음으로 배열을 순회하면서 확인해본다.

            for (int i = 1; i <= N; i++) {
                if (liveKnight[i] || i == poll.num) continue;
                Node knight = knightPosition[i];
                outer:
                for (int r = knight.x; r <= knight.x + knight.height; r++) {
                    for (int c = knight.y; c <= knight.y + knight.length; c++) {
                        if (r >= nr && r <= nr + poll.height && c >= nc + poll.length && c <= nc + poll.length) {
                            isPushed[i] = true;
                            q.offer(knight);
                            break outer;
                        }

                    }
                }
            }
            
        }

        for (int i = 1; i <= N; i++) {
            // 밀려났다면, 사각형 안에 함정 갯수만큼 빼준다.
            if (isPushed[i]) {
                Node knight = compare[i];
                for (int r = knight.x; r <= knight.x + knight.height; r++) {
                    for (int c = knight.y; c <= knight.y + knight.length; c++) {
                        if (arr[r][c] == 1) {
                            knight.health--;
                        }
                    }
                }
                if (knight.health < 1) liveKnight[i] = true;
            }
        }

        // 배열을 복사해준다.
        knightPosition = compare.clone();
        return true;
    }

    private static class Node {
        int x, y, height, length, health, num;

        public Node(int x, int y, int height, int length, int health, int num) {
            this.x = x;
            this.y = y;
            this.height = height;
            this.length = length;
            this.health = health;
            this.num = num;
        }
    }
}