import java.util.*;
public class Main {
    static int N,M,K;
    static Node[][] arr;
    // 우하좌상
    static int[] dr = {0,1,0,-1};
    static int[] dc = {1,0,-1,0};
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        M = sc.nextInt();
        K = sc.nextInt();
        arr = new Node[N][M];
        for(int r=0; r<N; r++){
            for(int c=0; c<M; c++){
                int force= sc.nextInt();
                arr[r][c] = new Node(r,c,0,force);
            }
        }
        for(int i=1; i<=K; i++){
            int[] start = attackCoord(0);
            Node node = arr[start[0]][start[1]];
            int[] end = attackCoord(1);
            // System.out.println("start : "+start[0]+" "+start[1]);
            arr[start[0]][start[1]] = new Node(node.r,node.c, i, node.force+N+M);
            // System.out.println("end : "+end[0]+" "+end[1]);
            boolean attack = laserAttack(start[0],start[1], end[0],end[1],arr[start[0]][start[1]].force);
            if(!attack) bombAttack(start[0],start[1], end[0],end[1],arr[start[0]][start[1]].force);
        }
        int[] res = attackCoord(1);
        System.out.println(Math.max(0,arr[res[0]][res[1]].force));
    }

    private static int[] attackCoord(int idx){
        int[] re = new int[2];
        PriorityQueue<Node> pq;
        // 공격자 선정(가장 약한 포탑)
        if(idx==0){
            pq = new PriorityQueue<>((o1,o2)->{
            // 공격력이 가장 낮은 포탑
            if(o1.force != o2.force) return o1.force-o2.force;
            // 가장 최근에 공격한 포탑
            else if(o1.attackIdx != o2.attackIdx) return o2.attackIdx-o1.attackIdx;
            // 행과 열의 합이 가장 큰 포탑
            else if((o1.r+o1.c)!=(o2.r+o2.c)) return (o2.r+o2.c)-(o1.r+o1.c);
            // 열 값이 가장 큰 포탑
            else return o2.c-o1.c;
            });
        }
        // 가장 강한 포탑 선정
        else{
            pq = new PriorityQueue<>((o1,o2)->{
            // 공격력이 가장 높은 포탑
            if(o1.force != o2.force) return o2.force-o1.force;
            // 가장 오래전에 공격한 포탑
            else if(o1.attackIdx != o2.attackIdx) return o1.attackIdx-o2.attackIdx;
            // 행과 열의 합이 가장 작은 포탑
            else if((o1.r+o1.c)!=(o2.r+o2.c)) return (o1.r+o1.c)-(o2.r+o2.c);
            // 열 값이 가장 작은 포탑
            else return o1.c-o2.c;
            });
        }
        
        for(int r=0; r<N; r++){
            for(int c=0; c<M; c++){
                if(arr[r][c].force>0){
                    pq.offer(arr[r][c]);
                }
            }
        }
        if(pq.isEmpty()) return new int[]{0,0};
        Node poll = pq.poll();
        re[0] = poll.r;
        re[1] = poll.c;
        return re;
    }

    // 레이저 공격
    private static boolean laserAttack(int startR, int startC, int endR, int endC, int damage){
        Queue<laser> q = new LinkedList<>();
        List<int[]> list = new ArrayList<>();
        q.offer(new laser(startR,startC,list));
        boolean[][] visited = new boolean[N][M];
        visited[startR][startC] = true;
        while(!q.isEmpty()){
            laser poll = q.poll();
            visited[poll.r][poll.c] = true;
            if(poll.r==endR && poll.c==endC){
                visited = new boolean[N][M];
                Node e = arr[endR][endC];
                arr[endR][endC] = new Node(e.r,e.c,e.attackIdx,e.force-damage);
                visited[startR][startC] = true;
                visited[endR][endC] = true;
                for(int i=0; i<poll.list.size()-1; i++){
                    e = arr[poll.list.get(i)[0]][poll.list.get(i)[1]];
                    arr[e.r][e.c] = new Node(e.r,e.c,e.attackIdx,e.force-(damage/2));
                    visited[e.r][e.c]=true;
                }
                for(int j=0; j<N; j++){
                    for(int k=0; k<M; k++){
                        Node node = arr[j][k];
                        if(arr[j][k].force>0 && !visited[j][k]){
                            arr[j][k] = new Node(node.r,node.c, node.attackIdx, node.force+1);
                        }
                    }
                }
                return true;
            }
            // 4방탐색 한다.
            for(int i=0; i<4; i++){
                // 범위를 벗어나는 경우를 처리해준다.
                int nr = (poll.r+dr[i]+N)%N;
                int nc = (poll.c+dc[i]+M)%M;
                if(arr[nr][nc].force<=0 || visited[nr][nc]) continue;
                List<int[]> l = new ArrayList<>(poll.list);
                l.add(new int[]{nr,nc});
                q.offer(new laser(nr,nc,l));
            }
        }
        return false;
    }

    // 포탄 공격
    private static void bombAttack(int startR, int startC, int endR, int endC, int damage){
        boolean[][] visited = new boolean[N][M];
        visited[startR][startC] = true;
        visited[endR][endC] = true;
        Node e = arr[endR][endC];
        arr[endR][endC] = new Node(e.r,e.c,e.attackIdx,e.force-damage);
        int[] drr = {1,0,-1,0,-1,-1,1,1};
        int[] dcc = {0,1,0,-1,-1,1,1,-1};
        for(int i=0; i<8; i++){
            int nr = (endR+drr[i]+N)%N;
            int nc = (endC+dcc[i]+M)%M;
            if(arr[nr][nc].force<=0) continue;
            e = arr[nr][nc];
            arr[e.r][e.c] = new Node(e.r,e.c,e.attackIdx,e.force-(damage/2));
            visited[e.r][e.c]=true;
        }
        for(int j=0; j<N; j++){
            for(int k=0; k<M; k++){
                Node node = arr[j][k];
                if(arr[j][k].force>0 && !visited[j][k]){
                    arr[j][k] = new Node(node.r,node.c, node.attackIdx, node.force+1);
                }
            }
        }
    }
    

    // node 클래스
    private static class Node{
        int r;
        int c;
        int attackIdx;
        int force;
        public Node(int r, int c, int attackIdx, int force){
            this.r=r;
            this.c=c;
            this.attackIdx=attackIdx;
            this.force=force;
        }
    }
    
    // Queue에 사용할 클래스
    private static class laser{
        int r;
        int c;
        List<int[]> list;
        public laser(int r, int c, List<int[]> list){
            this.r=r;
            this.c=c;
            this.list=list;
        }
    }
}