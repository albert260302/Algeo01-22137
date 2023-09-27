public class bicubic {

    private static int power(double base, int power){

        /* Fungsi pangkat untuk membantu perhitungan matrix interpolasi,
           fungsi ini diperlukan untuk mengatasi kalkulasi 0^0 */

        if (power == 0){
            return 1;

        } else {
            return (int) Math.pow(base, power);
        }
    }
    
    public static double[][] get_BSI_MATRIX(){

        /* Fungsi untuk menggenerasi matrix bicubic spline interpolation,
           nilai matrix adalah konstan maka, fungsi ini hanya perlu dijalankan
           sekali setiap kali program di run */

        double[][] intpMatrix = new double[16][16];

        int row = 0;

        while (row < 16){

            for (int y = 0; y < 2; y++){
                for (int x = 0; x < 2; x++){

                    int i = 0, j = 0;

                    for (int col = 0; col < 16; col++){

                        /* F(x,y) */
                        if (row < 4){
                            intpMatrix[row][col] = (power((double) x, i)*power((double) y, j));

                        /* Fx(x,y) */
                        } else if (row < 8){
                            intpMatrix[row][col] = i*power((double) x, i-1)*power((double) y, j);

                        /* Fy(x,y) */
                        } else if (row < 12){
                            intpMatrix[row][col] = j*power((double) x, i)*power((double) y, j-1);

                        /* Fxy(x,y) */
                        } else {
                            intpMatrix[row][col] = i*j*power((double) x, i-1)*power((double) y, j-1);
                        }
                    
                        i++;

                        if (i == 4){ i = 0; j++;}
                    }
                row++;
                }
            }
        }

        /* Mereturn invers dari matrix interpolasi agar bisa langsung dipakai
           untuk mencari matrix koefisien interpolasi dengan cara mengkalikan 
           invers matrix interpolasi dengan matrix dataset */
        return invers.identity(intpMatrix);
    }
    

    /* */
    public static double[][] get_BSI_COEF(double[][] dataset, double[][] BSI_MATRIX){

        double[][] FVAL_MATRIX = new double[16][1];

        /* Mengubah matrix dataset 4x4 menjadi 16x1 */
        int idx = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                FVAL_MATRIX[idx][0] = dataset[i][j];
                idx++;
            }
        }

        /* Matrix koefisien interpolasi didapatkan dari perkalian matrix BSI dengan
           matrix dataset */
        return operator.multiplyMatrix(BSI_MATRIX, FVAL_MATRIX);
    }

    public static double get_BSI_VAL(double[][] COEF, double X, double Y){

        double VAL = 0;
        int IDX = 0;

        for (int j = 0; j < 4; j++){
            for (int i = 0; i < 4; i++){
                VAL += COEF[IDX][0]*power(X, i)*power(Y, j);
                IDX++;
            }
        }

        return VAL;
    }
    
    public static void main(String[] args){

        /* 16 data values (dari studi kasus) untuk mencari koefisien interpolasi */
        double[][] data = {{21, 98,  125, 153},
                           {51, 101, 161, 59 },
                           {0 , 42,  72,  210},
                           {16, 12,  81,  96}};

        /* Menyimpan matrix Bicubic Spline Inteerpolation pada variabel
           karena bakal sering dipake jadi biar sekali compute aja */
        double[][] BSI_MATRIX = get_BSI_MATRIX();

        /* Mencari koefisien interpolasi, fungsi ini bakal dijalanin berkali-kali
           untuk setiap dataset yang berbeda */
        double[][] BSI_COEF = get_BSI_COEF(data, BSI_MATRIX);

        /* Contoh penggunaan Bicubic Spline Interpolation untuk mencari nilai f(x,y) */
        double FuncVal = get_BSI_VAL(BSI_COEF, 0.1, 0.1);
    }
}
