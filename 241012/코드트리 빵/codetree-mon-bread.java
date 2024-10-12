import java.util.*;
import java.io.*;
public class Main {
    static int N,M;
    static int[][] arr;
    static int[][] camp;
    static int[][] store;
    static boolean[] useCmap;
    static boolean[] arriveStore;
    static int[] dr = {-1,0,0,1};
    static int[] dc = {0,-1,1,0};
    static Node[] people;
    static Queue<Node>[] q;
    static Set<String>[] set;
    static int time;
    public static void main(String[] args) {
        // 여기에 코드를 작성해주세요.

        // 격자에 있는 사람들 모두가 편의점 방향으로 1칸 움직임
        // 상 좌 우 하 순으로 움직임
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        M = sc.nextInt();
        int campCnt=0;
        people = new Node[M];
        arr = new int[N][N];
        for(int r=0; r<N; r++){
            for(int c=0; c<N; c++){
                arr[r][c] = sc.nextInt();
                if(arr[r][c]==1) campCnt++;
            }
        }
        set = new Set[M];
        for(int i=0; i<M; i++){
            set[i] = new HashSet<>();
        }
        q = new Queue[M];
        for(int i=0; i<M; i++){
            q[i] = new LinkedList<>();
        }

        // 사용자의 도착 정보를 담을 배열과, 도착했는지의 여부
        // 모든 이동이 끝난 후, 도착한 곳을 못가게 만든다.
        store = new int[M][2];
        arriveStore = new boolean[M];
        for(int i=0; i<M; i++){
            int r = sc.nextInt()-1;
            int c = sc.nextInt()-1;
            store[i][0]=r;
            store[i][1]=c;
        }

        time=0;
        
        // 캠프를 사용했는지 여부
        // 모든 이동이 끝난 후, 도착한 곳을 못가게 만든다.
        useCmap = new boolean[campCnt];
        // 캠프 정보를 담을 배열
        camp = new int[campCnt][2];
        int idx=0;
        for(int r=0; r<N; r++){
            for(int c=0; c<N; c++){
                if(arr[r][c]==1){
                    camp[idx][0] = r;
                    camp[idx++][1] = c;
                }
            }
        }

        while(!move()){
            // time이 M보다 크거나 같으면 할 필요가 없다.
            // boolean mo = move();
            // if(mo) break;
            if(time<M){
                goBaseCamp();
            }
            time++;
        }
        System.out.println(time+1);
    }

    // 격자에서 자신이 가고 싶은 편의점 방향을 향해 1칸 움직임
    // 모든 정보를 그냥 다 갖다 넣어버리자.
    private static boolean move(){
        // q에 비어있으면 continue 해보자
        for(int i=0; i<M; i++){
            if(q[i].isEmpty()) continue;
            // 그게 아니라면 한칸 이동한 q를 다시 넣는다.
            Queue<Node> subQ = new LinkedList<>();
            while(!q[i].isEmpty()){
                Node poll = q[i].poll();
                for(int j=0; j<4; j++){
                    int nr = poll.r + dr[j];
                    int nc = poll.c + dc[j];
                    // 배열의 범위를 벗어나거나 이동할 수 없는 곳은 -1로 변경
                    if(nr<0 || nc<0 || nr>=N || nc>=N || arr[nr][nc]==-1) continue;
                    // 만약 도착점이라면 해당 q를 비워버리고 끝내기
                    if(nr==store[i][0] && nc == store[i][1]){
                        arriveStore[i] = true;
                        q[i].clear();
                    }else{
                        // nr과 nc를 합쳐서 String으로 변환하고, 길이가 2가 안되면 앞에 0을 붙여서 넣기
                        String left = Integer.toString(nr);
                        String right = Integer.toString(nc);
                        if(left.length()<2) left = "0"+left;
                        if(right.length()<2) right = "0"+right;
                        String str = left+right;
                        if(!set[i].contains(str)){
                            subQ.offer(new Node(nr,nc));
                            set[i].add(str);
                        }
                    }
                }
            }
            // 다시 q에 집어넣기
            while(!subQ.isEmpty()){
                q[i].offer(subQ.poll());
            }
        }
        // 이동이 끝났고, 도착하면 도착지를 -1로 만들어주기
        boolean check = true;
        for(int i=0; i<M; i++){
            if(arriveStore[i]){
                arr[store[i][0]][store[i][1]] = -1;
            }else{
                check = false;
            }
        }
        return check;
    }

    private static void goBaseCamp(){
        // time번째 사람을 베이스캠프로 이동시킨다.
        int dist=Integer.MAX_VALUE;
        int row=0;
        int col=0;
        int rowLocation = store[time][0];
        int colLocation = store[time][1];
        int idx=0;
        for(int i=0; i<camp.length; i++){
            if(useCmap[i]) continue;
            // dist 비교
            boolean check = false;
            int d = Math.abs(rowLocation-camp[i][0])+Math.abs(colLocation-camp[i][1]);
            if(d<dist){
                check = true;
                
            }else if(d==dist){
                // 거리가 같으면, 행이 작은 곳
                if(camp[i][0]<row){
                    check = true;
                }
                // 행도 같으면 열이 작은 곳
                else if(camp[i][0]==row){
                    if(camp[i][1]<col){
                        check = true;
                    }
                }
            }
            if(check){
                dist = d;
                row = camp[i][0];
                col = camp[i][1];
                idx=i;
            }
        }
        useCmap[idx] = true;
        arr[camp[idx][0]][camp[idx][1]] = -1;

        // 출발지 q에 넣기
        q[time].offer(new Node(row,col));
    }

    private static class Node{
        int r;
        int c;
        public Node(int r, int c){
            this.r=r;
            this.c=c;
        }
    }
}