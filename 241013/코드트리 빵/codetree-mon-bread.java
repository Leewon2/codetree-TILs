import java.util.*;
import java.io.*;

public class Main {
    static int N, M;
    static int[][] arr;
    static int[][] camp;
    static int[][] store;
    static boolean[] arriveStore;
    static int[] dr = {-1, 0, 0, 1};
    static int[] dc = {0, -1, 1, 0};
    static Node[] people;
    static int time;

    public static void main(String[] args) {
        // 여기에 코드를 작성해주세요.

        // 격자에 있는 사람들 모두가 편의점 방향으로 1칸 움직임
        // 상 좌 우 하 순으로 움직임
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        M = sc.nextInt();
        int campCnt = 0;
        people = new Node[M];
        for (int i = 0; i < M; i++) {
            people[i] = new Node(-1, -1);
        }
        arr = new int[N][N];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                arr[r][c] = sc.nextInt();
                if (arr[r][c] == 1) campCnt++;
            }
        }


        // 사용자의 도착 정보를 담을 배열과, 도착했는지의 여부
        // 모든 이동이 끝난 후, 도착한 곳을 못가게 만든다.
        store = new int[M][2];
        arriveStore = new boolean[M];
        for (int i = 0; i < M; i++) {
            int r = sc.nextInt() - 1;
            int c = sc.nextInt() - 1;
            store[i][0] = r;
            store[i][1] = c;
        }

        time = 0;

        // 캠프를 사용했는지 여부
        // 모든 이동이 끝난 후, 도착한 곳을 못가게 만든다.
        // 캠프 정보를 담을 배열
        camp = new int[campCnt][2];
        int idx = 0;
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (arr[r][c] == 1) {
                    camp[idx][0] = r;
                    camp[idx++][1] = c;
                }
            }
        }

        while (true) {
            // time이 M보다 크거나 같으면 할 필요가 없다.
            move();
            boolean check = true;
            for (int i = 0; i < M; i++) {

                if (arriveStore[i]) {
                    arr[store[i][0]][store[i][1]] = -1;
                } else {
                    check = false;
                }
            }
            if (time < M) {
                goBaseCamp();

                

            }

            if (check) break;
            time++;

        }

        System.out.println(time+1);
    }

    // 격자에서 자신이 가고 싶은 편의점 방향을 향해 1칸 움직임
    // 모든 정보를 그냥 다 갖다 넣어버리자.
    private static void move() {
        // q에 비어있으면 continue 해보자
        for (int i = 0; i < M; i++) {
            if (arriveStore[i]) continue;
            // 자기 자신으로부터 목적지 까지 갈 수 있는 최단 거리를 계산하고, 다음번으로 이동한다.
            Node poll = people[i];
            if (poll.r == -1) continue;
            if (poll.r == store[i][0] && poll.c == store[i][1]) {
                arriveStore[i] = true;
            } else {
                Queue<Node> subQ = new LinkedList<>();
                subQ.offer(new Node(poll.r, poll.c, new ArrayList<>()));
                boolean[][] visited = new boolean[N][N];
                outer:
                while (!subQ.isEmpty()) {
                    Node p = subQ.poll();
                    visited[p.r][p.c] = true;
                    for (int j = 0; j < 4; j++) {
                        int nr = p.r + dr[j];
                        int nc = p.c + dc[j];
                        // 배열의 범위를 벗어나거나 이동할 수 없는 곳은 -1로 변경
                        if (nr < 0 || nc < 0 || nr >= N || nc >= N || visited[nr][nc] || arr[nr][nc] == -1) continue;
                        List<Node> l = new ArrayList<>(p.list);
                        l.add(new Node(nr, nc));
                        subQ.offer(new Node(nr, nc, l));
                        // 만약 도착점이라면 가장 먼저 도착한 것이니 갱신
                        if (nr == store[i][0] && nc == store[i][1]) {
                            people[i] = new Node(l.get(0).r, l.get(0).c);
                            break outer;
                        }
                    }
                }
                poll = people[i];
                if (poll.r == store[i][0] && poll.c == store[i][1]) {
                    arriveStore[i] = true;
                }
            }

        }
        // 이동이 끝났고, 도착하면 도착지를 -1로 만들어주기

    }

    private static void goBaseCamp() {
        // time번째 사람을 베이스캠프로 이동시킨다.
        int rowLocation = store[time][0];
        int colLocation = store[time][1];
        PriorityQueue<Node> subQ = new PriorityQueue<>((o1,o2)->{
            if(o1.cnt!=o2.cnt) return o1.cnt-o2.cnt;
            else if(o1.r!=o2.r) return o1.r-o2.r;
            return o1.c-o2.c;
        });
        subQ.offer(new Node(rowLocation, colLocation, 0));
        boolean[][] visited = new boolean[N][N];
        outer:
        while (!subQ.isEmpty()) {
            Node p = subQ.poll();
            visited[p.r][p.c] = true;
            if (arr[p.r][p.c] == 1){
                people[time] = new Node(p.r, p.c);
                arr[p.r][p.c]=-1;
                break outer;
            }
            for (int j = 0; j < 4; j++) {
                int nr = p.r + dr[j];
                int nc = p.c + dc[j];
                // 배열의 범위를 벗어나거나 이동할 수 없는 곳은 -1로 변경
                if (nr < 0 || nc < 0 || nr >= N || nc >= N || visited[nr][nc] || arr[nr][nc] == -1) continue;
                subQ.offer(new Node(nr, nc, p.cnt+1));
            }
        }
    }

    private static class Node {
        int r;
        int c;
        List<Node> list;
        int cnt;

        public Node(int r, int c) {
            this.r = r;
            this.c = c;
        }

        public Node(int r, int c, int cnt) {
            this.r = r;
            this.c = c;
            this.cnt = cnt;
        }

        public Node(int r, int c, List<Node> list) {
            this.r = r;
            this.c = c;
            this.list = list;
        }
    }
}