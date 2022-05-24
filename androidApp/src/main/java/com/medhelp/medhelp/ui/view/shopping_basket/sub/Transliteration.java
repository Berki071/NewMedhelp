package com.medhelp.medhelp.ui.view.shopping_basket.sub;

public class Transliteration {
    private String inputStr;

    private String[] cyrillic=new String[]{"а","б","в","г","д","е","ё","ж","з","и","й","к","л","м","н","о","п","р","с","т","у","ф","х","ц","ч","ш","щ","ъ","ы","ь","э","ю","я"};
    private String[] latin=new String[]{"a","b","v","g","d","e","yo","zh","z","i","i","k","l","m","n","o","p","r","s","t","u","f","h","ts","ch","sh","sh","'","y","'","e","yu","ia"};

    public Transliteration(String inputStr) {
        this.inputStr = inputStr;
        trans();
    }

    private void trans()
    {
        inputStr=inputStr.toLowerCase();

        for(int i=0;i<cyrillic.length;i++)
        {
            inputStr=inputStr.replaceAll(cyrillic[i],latin[i]);
        }
    }

    public String getModStr()
    {
        return inputStr;
    }
}
