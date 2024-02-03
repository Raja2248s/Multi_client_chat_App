public class Main {
    public static void main(String[] args) {
        String S = "AGCCCTAAGGGCTACCTAGCTT";
        String K = "GACAGCCTACAAGCGTTAGCTTG";
     
        int m = S.length();
        int n = K.length();
        int[][] c = new int[m + 1][n + 1];
      
//        for (int i = 1; i <= m; i++)
// {
// c[i][0] = S.charAt(i-1);
// }      

//  for (int j = 1; j <= n+1; j++)
// {
// c[0][j] = K.charAt(j-1);
// }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (S.charAt(i - 1) == K.charAt(j - 1)) {
                    c[i][j] = c[i - 1][j - 1] + 1;
                } else {
                    c[i][j] = Math.max(c[i - 1][j], c[i][j - 1]);
                }
            }
        }
        
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                System.out.print(c[i][j] + "  ");
            }
            System.out.println();
        }

        int lcsLength = c[m][n];

        int i = m, j = n;
        
System.out.println("LCS: " + lcsLength);
    }  
    
}