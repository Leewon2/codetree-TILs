import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
    static int N, M, K;
    static int[][] arr;
    static Node[] people;
    static Node exit;
    static int moveDist;
    static int cnt;
    static boolean[] escape;
    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        arr = new int[N][N];
        escape = new boolean[M + 1];
        // 이동 거리
        moveDist = 0;
        cnt = M;
        people = new Node[M + 1];
        for (int r = 0; r < N; r++) {
            st = new StringTokenizer(br.readLine());
            for (int c = 0; c < N; c++) {
                arr[r][c] = Integer.parseInt(st.nextToken());
            }
        }
        for (int i = 1; i <= M; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            people[i] = new Node(x, y);
        }

        st = new StringTokenizer(br.readLine());
        int x = Integer.parseInt(st.nextToken()) - 1;
        int y = Integer.parseInt(st.nextToken()) - 1;
        // 출구는 100으로 설정
        exit = new Node(x, y);
        for (int i = 0; i < K; i++) {
            movePeople();
            if (cnt == 0) break;
            findSquare();
        }
        exit.r++;
        exit.c++;
        System.out.println(moveDist);
        System.out.println(exit.r + " " + exit.c);
    }

    private static void movePeople() {
        for (int i = 1; i <= M; i++) {
            boolean isMove = false;
            // 탈출했으면 볼필요 없음
            if (escape[i]) continue;
            // 현재 거리 계산
            int dist = calculateDist(exit, people[i]);
            int newR = 0;
            int newC = 0;
            for (int j = 0; j < 4; j++) {
                int nr = people[i].r + dr[j];
                int nc = people[i].c + dc[j];
                if (nr < 0 || nc < 0 || nr >= N || nc >= N || (arr[nr][nc] > 0 && arr[nr][nc] <= 9)) continue;
                int d = calculateDist(exit, new Node(nr, nc));
                // 상하좌우 순으로 살핀다.
                if (d < dist) {
                    // 거리가 바뀔 수 있다면, arr 배열의 현재 위치는 0으로 바꾼다.
                    isMove = true;
                    dist = d;
                    newR = nr;
                    newC = nc;
                }
            }
            if (isMove) {
                moveDist++;
                people[i] = new Node(newR, newC);
                if (people[i].r == exit.r && people[i].c == exit.c) {
                    escape[i] = true;
                    // 현재 사람 수 줄이기
                    cnt--;
                }
            }
            // 탈출 여부
            // 탈출하지 못했다면 arr 위치도 변경

        }
    }

    private static void findSquare() {
        int min = Integer.MAX_VALUE;
        for (int i = 1; i <= M; i++) {
            if (escape[i]) continue;
            // 가장 작은 정사각형이 한 변의 길이를 찾는다.
            int rDist = Math.abs(exit.r - people[i].r) + 1;
            int cDist = Math.abs(exit.c - people[i].c) + 1;
            min = Math.min(min, Math.max(rDist, cDist));
        }

        // 정사각형 한변을 찾았으니, 사람 1명, 출구가 들어가는, 가장 빨리 나오는 사각형을 찾는다.
        int startR = -1;
        int startC = -1;
        int endR = -1;
        int endC = -1;
        outer:
        for (int r = 0; r < N - min + 1; r++) {
            for (int c = 0; c < N - min + 1; c++) {
                boolean p = false;
                boolean e = false;
                for (int i = r; i < r + min; i++) {
                    for (int j = c; j < c + min; j++) {
                        for (int k = 1; k <= M; k++) {
                            if(escape[k]) continue;
                            if (people[k].r == i && people[k].c == j) p = true;
                            else if (exit.r == i && exit.c == j) e = true;
                        }
                    }
                }
                if (p && e) {
                    startR = r;
                    startC = c;
                    endR = r + min - 1;
                    endC = c + min - 1;

                    break outer;
                }
            }
        }

        // 최소 사각형을 구했으니 사각형을 돌려보자.

        int squareCnt = min / 2;
        int[][] compare = new int[N][N];
        // 배열 복사
        for (int i = 0; i < N; i++) {
            compare[i] = arr[i].clone();
        }
        Node[] comparePeople = new Node[M + 1];
        comparePeople = people.clone();
        Node compareExit = exit;
        // 사각형의 갯수만큼 반복한다.
        for (int i = 0; i < squareCnt; i++) {
            // 시계방향으로 90도 회전한다.
            for (int k = i; k < min - i; k++) {
                compare[startR + k][endC - i] = arr[startR + i][startC + k];
                compare[endR - i][endC - k] = arr[startR + k][endC - i];
                compare[endR - k][startC + i] = arr[endR - i][endC - k];
                compare[startR + i][startC + k] = arr[endR - k][startC + i];
                for (int j = 1; j <= M; j++) {
                    if(people[j].r==startR+i && people[j].c == startC+k){
                        comparePeople[j] = new Node(startR + k, endC - i);
                    }else if(people[j].r==startR + k && people[j].c == endC - i){
                        comparePeople[j] = new Node(endR - i, endC - k);
                    }else if(people[j].r==endR - i && people[j].c == endC - k){
                        comparePeople[j] = new Node(endR - k, startC + i);
                    }else if(people[j].r==endR - k && people[j].c == startC + i){
                        comparePeople[j] = new Node(startR + i, startC + k);
                    }
                }
                if(exit.r==startR+i && exit.c == startC+k){
                    compareExit = new Node(startR + k, endC - i);
                }else if(exit.r==startR + k && exit.c == endC - i){
                    compareExit = new Node(endR - i, endC - k);
                }else if(exit.r==endR - i && exit.c == endC - k){
                    compareExit = new Node(endR - k, startC + i);
                }else if(exit.r==endR - k && exit.c == startC + i){
                    compareExit = new Node(startR + i, startC + k);
                }
            }
        }
        for (int r = startR; r < startR + min; r++) {
            for (int c = startC; c < startC + min; c++) {
                if (compare[r][c] > 0 && compare[r][c] <= 9) compare[r][c]--;
                    // 사람이 있는 경우 위치 변경
                else if (compare[r][c] < 0) {
                    people[Math.abs(compare[r][c])] = new Node(r, c);
                } else if (compare[r][c] == 100) {
                    exit = new Node(r, c);
                }
            }
        }
        for (int i = 0; i < N; i++) {
            arr[i] = compare[i].clone();
        }
        people = comparePeople.clone();
        exit = compareExit;
    }

    private static int calculateDist(Node x, Node y) {
        return Math.abs(x.r - y.r) + Math.abs(x.c - y.c);
    }


    private static class Node {
        int r, c;

        public Node(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }
}