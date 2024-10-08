import java.util.*;

public class Main {
    static int R,C,K;
    // 북 동 남 서
    static int[] dr={-1,0,1,0};
    static int[] dc={0,1,0,-1};
    static int ci;
    static int di;
    static int row;
    static int[][] arr;
    public static void main(String[] args) {
        // 여기에 코드를 작성해주세요.
        Scanner sc = new Scanner(System.in);
        R = sc.nextInt();
        C = sc.nextInt();
        K = sc.nextInt();

        // 0에서 시작. 2가 0행
        arr = new int[R+3][C];
        int res=0;
        for(int t=0; t<K; t++){
            ci=sc.nextInt()-1;
            di=sc.nextInt();
            row=1;

            // 계속 이동
            outer : while(true){
                boolean d = down();
                if(!d){
                    boolean l = left();
                    if(!l){
                        boolean r = right();
                        if(!r){
                            break outer;
                        }
                    }
                }
                // if(row==R+1){
                //     break outer;
                // }
            }
            // 끝났으면 자신의 출구 찾기
            // 출구를 기준으로 4방탐색 후 -1이하인 곳 있다면 그 곳의 중앙값과 현재 중앙값을 비교
            // 만약 row가 3 이하면 몸통 못나온거
            if(row<=3){
                arr = new int[R+3][C];
            }else{
                // 내 위치에서 가장 아래로 갈 수 있는 값을 가운데에 저장
                int max = row+1;
                // 못가는 곳으로 만들기
                for(int i=0; i<4; i++){
                    arr[row+dr[i]][ci+dc[i]]=-1;
                }
                int nr = row+dr[di];
                int nc = ci+dc[di];
                // 연결된 친구 있나 보기
                for(int i=0; i<4; i++){
                    int nrr = nr + dr[i];
                    int ncc = nc + dc[i];
                    if(ncc<0 || nrr>=R+3 || ncc>=C) continue;
                    if(arr[nrr][ncc]<0){
                        for(int j=0; j<4; j++){
                            int nnrr = nrr+dr[j];
                            int nncc = ncc+dc[j];
                            if(nncc<0 || nnrr>=R+3 || nncc>=C) continue;
                            if(arr[nnrr][nncc]>0) max = Math.max(max,arr[nnrr][nncc]);
                        }
                    }
                }
                arr[row][ci] = max;
                // System.out.println(max);
                res+=(max-2);
                // System.out.println("ci : "+ci+" row : "+row);
            }
        }
        System.out.println(res);
    }
    // 아래로 이동 확인
    private static boolean down(){
        if(row+2 >=R+3|| ci+1>=C || ci-1<0 || arr[row+2][ci]!=0 || arr[row+1][ci-1]!=0 || arr[row+1][ci+1]!=0) return false;
        row++;
        return true;
    }

    // 왼쪽으로 가면 -1
    private static boolean left(){
        if(ci-2<0 || arr[row][ci-2]!=0 || arr[row-1][ci-1]!=0 || arr[row+1][ci-1]!=0) return false;
        if(row+2>= R+3 || arr[row+1][ci-2]!=0 || arr[row+2][ci-1] !=0) return false;
        ci--;
        row++;
        di= (3+di)%4;
        return true;
    }

    // 오른쪽으로 가면 +1
    private static boolean right(){
        if(ci+2>=C || arr[row][ci+2]!=0 || arr[row-1][ci+1]!=0 || arr[row+1][ci+1]!=0) return false;
        if(row+2 >=R+3 ||arr[row+1][ci+2]!=0 || arr[row+2][ci+1]!=0) return false;
        ci++;
        row++;
        di= (di+1)%4;
        return true;
    }
}