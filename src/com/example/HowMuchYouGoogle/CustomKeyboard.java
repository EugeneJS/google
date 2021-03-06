package com.example.HowMuchYouGoogle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class CustomKeyboard extends RelativeLayout implements View.OnClickListener
{
    Context context;

    Button[] WordBtnArray;
    Button[] LettersBtnArray;

    int CurrentLetter;
    int max;
    List<Button> BtnHistory;

    public CustomKeyboard(Context _context, int WordLenght, char[] AvialibleChars)
    {
        super(_context);

        context = _context;
        max = WordLenght;

        LayoutInflater  mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.customlayout, this, true);

        LinearLayout Word = (LinearLayout)findViewById(R.id.word);

        WordBtnArray = new Button[WordLenght];
        for(int i = 0; i < WordLenght; i++)
        {
            WordBtnArray[i] = (Button)mInflater.inflate(R.layout.word_btn, null);
            WordBtnArray[i].setOnClickListener(this);
            WordBtnArray[i].setTag(new Object[]{"word", false});
            Word.addView(WordBtnArray[i]);
        }

        LinearLayout Letters = (LinearLayout)findViewById(R.id.letters);
        LinearLayout Letters2 = (LinearLayout)findViewById(R.id.letters2);

        int length = AvialibleChars.length;
        int l1 = length / 2 + length % 2 ;

        if(length < 9)
        {
            l1 = length;
            Letters2.setVisibility(GONE);
        }

        LettersBtnArray = new Button[length];

        for(int i = 0; i < l1; i++)
        {
            LettersBtnArray[i] = (Button)mInflater.inflate(R.layout.letter_btn, null);
            LettersBtnArray[i].setOnClickListener(this);
            LettersBtnArray[i].setText(AvialibleChars, i, 1);
            LettersBtnArray[i].setSelected(false);
            LettersBtnArray[i].setTag(new Object[]{"letter",false});
            Letters.addView(LettersBtnArray[i]);
        }
        for(int i = l1; i < length; i++)
        {
            LettersBtnArray[i] = (Button)mInflater.inflate(R.layout.letter_btn, null);
            LettersBtnArray[i].setOnClickListener(this);
            LettersBtnArray[i].setText(AvialibleChars, i, 1);
            LettersBtnArray[i].setSelected(false);
            LettersBtnArray[i].setTag(new Object[]{"letter",false});
            Letters2.addView(LettersBtnArray[i]);
        }

        Button accept = (Button)findViewById(R.id.accept);
        accept.setTag(new Object[]{"accept"});

        Button Xbtn = (Button)findViewById(R.id.Xbtn);
        Xbtn.setOnClickListener(this);
        Xbtn.setTag(new Object[]{"X"});

        BtnHistory = new ArrayList<Button>();
    }

    @Override
    public void onClick(View view)
    {
        Object[] tag = (Object[]) view.getTag();
        if(tag[0].equals("letter"))
        {
            if(tag[1].equals(false) && CurrentLetter < max)
            {
                view.setTag(new Object[]{"letter", true});
                WordBtnArray[CurrentLetter].setText(((Button) view).getText());
                WordBtnArray[CurrentLetter].setTag(new Object[]{"word", true});
                view.setSelected(true);
                BtnHistory.add((Button)view);
                CurrentLetter++;
            }
            else
            {
                CurrentLetter--;
                if(!BtnHistory.get(CurrentLetter).equals(view))
                {
                    CurrentLetter++;
                    return;
                }
                view.setTag(new Object[]{"letter",false});
                view.setSelected(false);
                WordBtnArray[CurrentLetter].setText("");
                WordBtnArray[CurrentLetter].setTag(new Object[]{"word",false});
                BtnHistory.remove(CurrentLetter);
            }
        }
        else if(tag[0].equals("accept"))
        {

        }
        else if(tag[0].equals("X"))
        {
            if(CurrentLetter < 1)
                return;
            CurrentLetter--;
            for(int i = 0; i < LettersBtnArray.length; i++)
            {
                if(LettersBtnArray[i].getText().equals(WordBtnArray[CurrentLetter].getText()))
                {
                    LettersBtnArray[i].setTag(new Object[]{"letter", false});
                    LettersBtnArray[i].setSelected(false);
                }
            }
            WordBtnArray[CurrentLetter].setText("");
            WordBtnArray[CurrentLetter].setTag(new Object[]{"word",false});
            BtnHistory.remove(CurrentLetter);
        }
    }

    public void SetOnNextBtnClickListner(OnClickListener listner)
    {
        ((Button)findViewById(R.id.accept)).setOnClickListener(listner);
    }

    public String GetCurrentText()
    {
        String str = "";
        for(int i = 0; i < WordBtnArray.length; i++)
        {
            str += WordBtnArray[i].getText();
        }
        return str;
    }
}
