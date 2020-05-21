import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Algorithm {
    Item[]items;
    int capacity;
    int quantity;
    double temperature;
    double dTemperature;

    public Algorithm(String filePath){
        readDataFromFile(filePath);
        printVector(findBestCombination(randomCombination()));
    }
    private void readDataFromFile(String path){
        try{
            BufferedReader bf=new BufferedReader(new FileReader(path));
            String line=bf.readLine();
            StringTokenizer tokenizer=new StringTokenizer(line," ");
            capacity = Integer.parseInt(tokenizer.nextToken());
            quantity = Integer.parseInt(tokenizer.nextToken());
            temperature = Double.parseDouble(tokenizer.nextToken());
            dTemperature = Double.parseDouble(tokenizer.nextToken());
            items=new Item[quantity];
            for (int i = 0; i < items.length; i++) {
                items[i]=new Item();
            }
            line=bf.readLine();
            StringTokenizer stringTokenizer=new StringTokenizer(line,",");
            for (int i = 0; i < items.length; i++)
                items[i].value=Integer.parseInt(stringTokenizer.nextToken());
            line=bf.readLine();
            stringTokenizer=new StringTokenizer(line,",");
            for (int i = 0; i < items.length; i++)
                items[i].weight=Integer.parseInt(stringTokenizer.nextToken());

            bf.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private String randomCombination(){
        String retString="";
        int numberOfCombinations = (int)Math.pow(2,quantity);
        boolean tmp=true;
        while(tmp){
            int numberTmp = (int)(Math.random()*numberOfCombinations);
            retString=Integer.toBinaryString(numberTmp);
            String formattedCombinationString="";
            for (int j = retString.length()-1; j >=0 ; j--) {
                formattedCombinationString+=retString.substring(j,j+1);
            }
            for (int j = 0; j < quantity-retString.length(); j++) {
                formattedCombinationString+=0;
            }
            if(findValueFromCombination(formattedCombinationString)!=-1)
                tmp=false;
            retString=formattedCombinationString;
        }
        return retString;
    }
    private String findBestCombination(String currentBestCombination){
        for (int i = 0; i < quantity-1; i++) {
                char[] neighborArr = currentBestCombination.toCharArray();
                if(neighborArr[i]=='1')
                    neighborArr[i]='0';
                else
                    neighborArr[i]='1';

                System.out.print("current best:");
                printVector(currentBestCombination);
                System.out.println("checking: "+new String(neighborArr));
                if(findValueFromCombination(new String(neighborArr))>findValueFromCombination(currentBestCombination))
                    return findBestCombination(new String(neighborArr));
                try {
                    if(findValueFromCombination(new String(neighborArr))==-1)
                        throw new Exception();
                    if(temperature<=0)
                        throw new Exception();
                    double valueDiffrence = findValueFromCombination(currentBestCombination)-findValueFromCombination(new String(neighborArr));
                    double possibility = Math.pow(Math.E, -(Math.max(valueDiffrence, -valueDiffrence) / temperature));
                    System.out.println("Possibility:"+possibility);
                    temperature-=dTemperature;
                    double tmp1 = Math.random()*100+1;
                    double tmp2 = 100*possibility;
                    if(tmp2>tmp1)
                        return  findBestCombination(new String(neighborArr));
                }catch(Exception e){}
        }
        return currentBestCombination;
    }

    private int findValueFromCombination(String combination){
        char[]arr=combination.toCharArray();
        int sumValue=0;
        int sumWeight=0;
        for (int i = 0; i < arr.length; i++) {
            if(arr[i]=='1'){
                sumValue+=items[i].value;
                sumWeight+=items[i].weight;
            }
        }
        if(sumWeight>capacity)
            sumValue=-1;
        return sumValue;
    }
    private void printVector(String s){
        System.out.print(s+"    ");
        char[] arr=s.toCharArray();
        int sumWeight=0;
        int sumValue=0;
        for (int i = 0; i < arr.length; i++) {
            if(arr[i]!='0'){
                sumWeight+=items[i].weight;
                sumValue+=items[i].value;
            }
        }
        System.out.print("value:"+sumValue+"    "+"weight:"+sumWeight);
        System.out.println();
    }
}
