import java.util.*;

public class Main {
    static int K, M;
    static int[][] arr;
    static int[] dist;
    static PriorityQueue<Node> pq;
    static int[] dr = {1, 0, -1, 0};
    static int[] dc = {0, 1, 0, -1};
    static int idx;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        K = sc.nextInt();
        M = sc.nextInt();
        arr = new int[5][5];
        dist = new int[M];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                arr[i][j] = sc.nextInt();
            }
        }
        for (int i = 0; i < M; i++) {
            dist[i] = sc.nextInt();
        }
        idx = 0;
        StringBuilder sb = new StringBuilder();
        pq = new PriorityQueue<>((o1, o2) -> {
            if (o1.score != o2.score) {
                return o2.score - o1.score;
            } else if (o1.angle != o2.angle) {
                return o1.angle - o2.angle;
            } else if (o1.c != o2.c) {
                return o1.c - o2.c;
            }
            return o1.r - o2.r;
        });

        for (int i = 0; i < K; i++) {
            roll();
            if (pq.isEmpty()) break;
            Node poll = pq.poll();
            sb.append(calculate(poll)).append(" ");
            pq.clear();
        }
        System.out.println(sb);
    }

    // 선택된 r,c,angle로 배열 돌리고, 없애고, 채우기 과정 반복
    private static int calculate(Node node) {
        int r = node.r;
        int c = node.c;
        int angle = node.angle;
        int[][] compare = new int[5][5];
        for (int i = 0; i < 5; i++) {
            compare[i] = arr[i].clone();
        }

        int score = 0;
        if (angle == 90) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    compare[r + j][c + 2 - i] = arr[r + i][c + j];
                }
            }
        } else if (angle == 180) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    compare[r + 2 - i][c + 2 - j] = arr[r + i][c + j];
                }
            }

        } else if (angle == 270) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    compare[r + 2 - j][c + i] = arr[r + i][c + j];
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            arr[i] = compare[i].clone();
        }

        while (true) {
            int k = getScore();
            if (k >= 3) score += k;
            else break;
        }
        return score;
    }

    private static int getScore() {
        int score = 0;
        int[][] compare = new int[5][5];
        for (int i = 0; i < 5; i++) {
            compare[i] = arr[i].clone();
        }
        boolean[][] visited = new boolean[5][5];
        Queue<NN> q = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!visited[i][j]) {
                    int cnt = 0;
                    q.offer(new NN(i, j));
                    visited[i][j] = true;
                    while (!q.isEmpty()) {
                        NN poll = q.poll();
                        cnt++;
                        compare[poll.r][poll.c] = 0;
                        for (int k = 0; k < 4; k++) {
                            int nr = poll.r + dr[k];
                            int nc = poll.c + dc[k];
                            if (nr < 0 || nc < 0 || nr >= 5 || nc >= 5 || visited[nr][nc]) continue;
                            if (arr[poll.r][poll.c] == arr[nr][nc]) {
                                visited[nr][nc] = true;
                                q.offer(new NN(nr, nc));
                            }
                        }
                    }
                    if (cnt >= 3) {
                        score += cnt;
                        for (int k = 0; k < 5; k++) {
                            arr[k] = compare[k].clone();
                        }
                    } else {
                        for (int k = 0; k < 5; k++) {
                            compare[k] = arr[k].clone();
                        }
                    }
                }
            }
        }


        if (score >= 3) {
            // 채우기
            for (int c = 0; c <5 ; c++) {
                for (int r = 4; r >=0; r--) {
                    if (arr[r][c] == 0) {
                        arr[r][c] = dist[idx++];
                    }
                }
            }
        }
        return score;
    }

    // 배열 회전
    private static void roll() {

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                for (int k = 1; k <= 3; k++) {
                    rolling(90 * k, i, j);
                }
            }
        }

    }

    private static void rolling(int angle, int r, int c) {
        int[][] compare = new int[5][5];
        for (int i = 0; i < 5; i++) {
            compare[i] = arr[i].clone();
        }
        int score = 0;
        if (angle == 90) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    compare[r + j][c + 2 - i] = arr[r + i][c + j];
                }
            }
        } else if (angle == 180) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    compare[r + 2 - i][c + 2 - j] = arr[r + i][c + j];
                }
            }

        } else if (angle == 270) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    compare[r + 2 - j][c + i] = arr[r + i][c + j];
                }
            }
        }
        boolean[][] visited = new boolean[5][5];
        Queue<NN> q = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!visited[i][j]) {
                    int cnt = 0;
                    q.offer(new NN(i, j));
                    visited[i][j] = true;
                    while (!q.isEmpty()) {
                        NN poll = q.poll();
                        cnt++;
                        for (int k = 0; k < 4; k++) {
                            int nr = poll.r + dr[k];
                            int nc = poll.c + dc[k];
                            if (nr < 0 || nc < 0 || nr >= 5 || nc >= 5 || visited[nr][nc]) continue;
                            if (compare[poll.r][poll.c] == compare[nr][nc]) {
                                visited[nr][nc] = true;
                                q.offer(new NN(nr, nc));
                            }
                        }
                    }
                    if (cnt >= 3) {
                        score += cnt;
                    }
                }
            }
        }
        if (score >= 3) {
            pq.offer(new Node(r, c, angle, score));
        }


    }


    //
    private static class Node {
        int r, c, angle, score;

        public Node(int r, int c, int angle, int score) {
            this.r = r;
            this.c = c;
            this.angle = angle;
            this.score = score;
        }
    }

    private static class NN {
        int r;
        int c;

        public NN(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }
}