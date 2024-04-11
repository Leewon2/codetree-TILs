import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
    static int N, M, K, C;
    static int[][] arr;
    static final int[] dr = {1, 0, -1, 0};
    static final int[] dc = {0, 1, 0, -1};
    static final int[] drLine = {1, -1, -1, 1};
    static final int[] dcLine = {1, 1, -1, -1};
    static int res;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        arr = new int[N][N];
        res = 0;
        for (int r = 0; r < N; r++) {
            st = new StringTokenizer(br.readLine());
            for (int c = 0; c < N; c++) {
                arr[r][c] = Integer.parseInt(st.nextToken());
            }
        }
        for (int i = 0; i < M; i++) {
            growTree();
            spreadTree();
            spray();
        }
        System.out.println(res);


    }

    private static boolean cantGo(int r, int c) {
        if (r < 0 || c < 0 || r >= N || c >= N || arr[r][c] <= 0 || arr[r][c] > 9999) return false;
        return true;
    }

    // 나무의 성장
    private static void growTree() {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                // 없는곳, 벽, 제초제는 패스
                if (!cantGo(r, c)) continue;
                // 근처 나무 갯수
                for (int i = 0; i < 4; i++) {
                    int nr = r + dr[i];
                    int nc = c + dc[i];
                    // 범위 out, 제초제, 벽이면 패스
                    if (!cantGo(nr, nc)) continue;
                    arr[r][c]++;
                }
            }
        }
    }

    // 나무의 번식
    private static void spreadTree() {
        int[][] compare = new int[N][N];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (!cantGo(r, c)) continue;
                // 주변 0인 곳 체크
                int cnt = 0;
                for (int i = 0; i < 4; i++) {
                    int nr = r + dr[i];
                    int nc = c + dc[i];
                    if (nr<0 || nc<0 || nr>=N || nc>=N) continue;
                    if (arr[nr][nc] == 0)
                        cnt++;
                }
                int divide = 0;
                if (cnt != 0)
                    divide = arr[r][c] / cnt;
                for (int i = 0; i < 4; i++) {
                    int nr = r + dr[i];
                    int nc = c + dc[i];
                    if (nr<0 || nc<0 || nr>=N || nc>=N) continue;
                    if(arr[nr][nc]==0)
                        compare[nr][nc] += divide;
                }
            }
        }
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                arr[r][c] += compare[r][c];
            }
        }
    }

    private static void spray() {
        int max = 0;
        int rPosition = 0;
        int cPosition = 0;
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                // 갈 수 없는 경우
                if (!cantGo(r, c)) continue;
                int cnt = arr[r][c];
//                System.out.println(arr[r][c]);
                for (int i = 0; i < 4; i++) {
                    for (int j = 1; j <= K; j++) {
                        int nr = r + drLine[i]*j;
                        int nc = c + dcLine[i]*j;
                        if (nr < 0 || nc < 0 || nr >= N || nc >= N || arr[nr][nc] <= 0) break;
                        if (arr[nr][nc] < 9999){
                            cnt += arr[nr][nc];
//                            System.out.println(arr[nr][nc]+ " nr : "+nr+" nc : "+nc);
                        }
                    }
                }
                if (cnt > max) {
                    max = cnt;
                    rPosition = r;
                    cPosition = c;
                }
            }
        }

        res += max;
        // 제초제 뿌리기
        arr[rPosition][cPosition] = 10000 + C+1;
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j <= K; j++) {
                int nr = rPosition + drLine[i]*j;
                int nc = cPosition + dcLine[i]*j;
                if (nr < 0 || nc < 0 || nr >= N || nc >= N || arr[nr][nc] < 0) break;
                    // 0보다 크면 계속 감
                else if (arr[nr][nc] > 0) {
                    arr[nr][nc] = 10000 + C+1;
                }
                // 0인 경우 멈춤
                else if (arr[nr][nc] == 0) {
                    arr[nr][nc] = 10000 + C+1;
                    break;
                }
            }
        }
        // 1년 지났으니 빼주기
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (arr[r][c] > 9999) {
                    arr[r][c]--;
                    if (arr[r][c] == 10000) arr[r][c] = 0;
                }
            }
        }

    }


}