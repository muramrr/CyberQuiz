package com.mmdev.cyberquiz.gameplay;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.common.primitives.Ints;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.mmdev.cyberquiz.Activity_Main;
import com.mmdev.cyberquiz.R;
import com.mmdev.cyberquiz.adapters.level_view_Grid_adapter_img;
import com.mmdev.cyberquiz.data.GameContentViewModel;
import com.mmdev.cyberquiz.data.InteractionContent;
import com.mmdev.cyberquiz.data.PlayerContent_Object;
import com.mmdev.cyberquiz.utils.OnSwipeTouchListener;
import com.mmdev.cyberquiz.utils.TinyDB;
import com.mmdev.cyberquiz.utils.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Anderson on 16.08.2017.
 */

public class write_answer extends Fragment
{
    private ImageView mImageView;       //setting image based on previous intent
    private String playerName;          //setting player name chosen from previous intent
    private char[] answer_playerName;   //holder char array from "String playerName"
    private int  playersNameList_length;     //kolichestvo UGADAEK na urovne
    private int _position = 0;          //позиция игрока в общем гриде на fragment_Level_view intent
    private boolean already_guessed;    //проверяем отгадан ли игрок до этого
    private String temp ="";        //dlya podskazki "ubrat odnu bukvu"
    private int i=0;                //dlya podskazki "ubrat odnu bukvu"
                            // shob ne пробегать заново все слово - запомним место, на котором уже убрали букву
    private Activity_Main activity;
    private TinyDB tinydb;
    private String game;
    private int level;
    private Dialog dialog;

    private Recycler_adapter_answer answerAdapter;
    private RecyclerView mRecycleView_answer;
    private Recycler_adapter_suggest suggestAdapter;
    private RecyclerView mRecycleView_suggest;

    private SpeedDialView mSpeedDialView;

    private GameContentViewModel model;
    private PlayerContent_Object playerContent_object;
    private InteractionContent mInterCont;

    private ImageButton left;
    private ImageButton right;
    private LinearLayout layout_main;


    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    { return inflater.inflate(R.layout.write_answer, container, false); }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity()!=null)
            activity = (Activity_Main) getActivity();

        layout_main = view.findViewById(R.id.write_answer_main);
        game = activity.getGame();
        level = activity.getLevel();
        ((TextView) activity.findViewById(R.id.tv_CurrentWindow)).setText(String.format(Locale.getDefault(),"Level %d",level));
        (activity.findViewById(R.id.tv_Hints)).setVisibility(View.VISIBLE);
        initSpeedDial(savedInstanceState == null,view);
        mRecycleView_answer = view.findViewById(R.id.RecycleView_answer);
        mRecycleView_suggest = view.findViewById(R.id.RecycleView_suggest);
        mImageView = view.findViewById(R.id.iv_bigpic);


        tinydb = activity.getTinyDb();
        if (!tinydb.getBoolean("Second_time"))
        {
            dialog = new Dialog(activity){
                // Tap anywhere to close dialog.
                @Override
                public boolean onTouchEvent(@NonNull MotionEvent event) {
                    tinydb.putBoolean("Second_time",true); //don't show it again
                    this.dismiss();
                    return true;
                }
            };
            dialog.setContentView(R.layout.dialog_tips);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setDimAmount(0.76f);
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        left = view.findViewById(R.id.button_left);
        left.setOnClickListener(left_Click);
        right = view.findViewById(R.id.button_right);
        right.setOnClickListener(right_Click);
        //swiping
        CoordinatorLayout cLayout = view.findViewById(R.id.write_coord_main);
        cLayout.setOnTouchListener(swipeTouchListener(activity));
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        model = ViewModelProviders.of(activity).get(GameContentViewModel.class);
        model.getPlayerContent().observe(this, new Observer<PlayerContent_Object>() {
            @Override
            public void onChanged (PlayerContent_Object playerContent_object1)
            {
                playerContent_object = playerContent_object1;
                playerName = playerContent_object.getPlayerName().toUpperCase();
                mImageView.setImageResource(playerContent_object.getPlayerImg());
                _position = playerContent_object.getPosition();
                playersNameList_length = playerContent_object.getPlayersNameList_length();
                already_guessed = playerContent_object.getAlready_guessed();
                settingUpButtons();
                initRecyclerViews(already_guessed);
            }
        });
    }

    private OnSwipeTouchListener swipeTouchListener(Context context)
    {
        return new OnSwipeTouchListener(context)
        {
            public void onSwipeTop() {}
            public void onSwipeRight() { left.performClick(); }
            public void onSwipeLeft() { right.performClick(); }
            public void onSwipeBottom() {}
        };
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRecyclerViews(boolean already_guessed)
    {
        //answer
        FlexboxLayoutManager manager_answer = new FlexboxLayoutManager(activity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        manager_answer.setFlexDirection(FlexDirection.ROW);
        manager_answer.setJustifyContent(JustifyContent.CENTER);
        manager_answer.setAlignItems(AlignItems.CENTER);
        mRecycleView_answer.setLayoutManager(manager_answer);
        if(already_guessed)
            answerAdapter = new Recycler_adapter_answer(answer_playerName);
        else {answerAdapter = new Recycler_adapter_answer(mInterCont.getUser_submit_answer());
        answerAdapter.setOnItemClickListener(RecycleView_answer_OnItemClickListener);}
        mRecycleView_answer.setAdapter(answerAdapter);
        mRecycleView_answer.setOnTouchListener(swipeTouchListener(activity));


        //если не отгадан игрок, то инициализируем RV_suggest
        if(!already_guessed)
        {
            //suggest
            FlexboxLayoutManager manager_suggest = new FlexboxLayoutManager(activity)
            {
                @Override
                public boolean canScrollVertically ()
                {
                    return false;
                }
            };
            manager_suggest.setFlexDirection(FlexDirection.ROW);
            manager_suggest.setJustifyContent(JustifyContent.SPACE_AROUND);
            manager_suggest.setAlignItems(AlignItems.CENTER);
            mRecycleView_suggest.setVisibility(View.VISIBLE);
            mRecycleView_suggest.setLayoutManager(manager_suggest);
            suggestAdapter = new Recycler_adapter_suggest(mInterCont.getSuggested_chars());
            suggestAdapter.setOnItemClickListener(RecycleView_suggest_OnItemClickListener);
            mRecycleView_suggest.setAdapter(suggestAdapter);
            mRecycleView_suggest.setOnTouchListener(swipeTouchListener(activity));
        }
        else if (mRecycleView_suggest!=null)
            mRecycleView_suggest.setVisibility(View.INVISIBLE);
        //changing height of recyclerView_ANSWER шоб не было пустого места снизу
        //идет проверка на количество буковок в слове
        if(playerName.length()<7)
        {
            ViewGroup.LayoutParams params_answer =mRecycleView_answer.getLayoutParams();
            params_answer.height=(int) TypedValue.applyDimension
                    (TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            mRecycleView_answer.setLayoutParams(params_answer);
        }
        //возвращаем старое значение, если буковок больше
        else{
            ViewGroup.LayoutParams params_answer =mRecycleView_answer.getLayoutParams();
            params_answer.height=(int) TypedValue.applyDimension
                    (TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
            mRecycleView_answer.setLayoutParams(params_answer);
            //меняем размер suggest, если игрок не отгада
            //иначе он просто не инициализируется (см. выше)
            if(!already_guessed)
            {
                if (playerName.length() > 12)
                {
                    ViewGroup.LayoutParams params_suggest = mRecycleView_suggest.getLayoutParams();
                    params_suggest.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
                    mRecycleView_suggest.setLayoutParams(params_suggest);
                }
                //возвращаем старое значение, если буковок больше
                else
                {
                    ViewGroup.LayoutParams params_suggest = mRecycleView_suggest.getLayoutParams();
                    params_suggest.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
                    mRecycleView_suggest.setLayoutParams(params_suggest);
                }
            }
        }
    }

    private void initSpeedDial(boolean addActionItems,View view)
    {
        mSpeedDialView = view.findViewById(R.id.speedDial);

        if (addActionItems) {
            //delete letter
            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_delete_letter, R.drawable
                    .ic_delete_letter_24dp)
                    .setLabel("Delete wrong letter (-1)")
                    .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.white_text,
                            activity.getTheme()))
                    .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.material_orange_900,
                            activity.getTheme()))
                    .create());

            //reveal word
            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_reveal_word, R.drawable
                    .ic_reveal_word_24dp)
                    .setLabel("Reveal answer (-3)")
                    .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.material_black_1000,
                            activity.getTheme()))
                    .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.material_lime_300,
                            activity.getTheme()))
                    //icon color
                    //.setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.inbox_primary, getTheme()))
                    .create());
            //Drawable drawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_custom_color);
            //mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_reveal_word, drawable)

//            Drawable drawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_fab_close_24dp);
//            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_add_action, drawable)
//                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.material_green_500,
//                            getTheme()))
//                    .setLabel(R.string.label_add_action)
//                    .setLabelBackgroundColor(Color.TRANSPARENT)
//                    .create());

        }

//        //Set main action clicklistener.
//        mSpeedDialView.setOnChangeListener(new SpeedDialView.OnChangeListener() {
//            @Override
//            public boolean onMainActionSelected() {
//                Toast.makeText(getApplicationContext(),"Main action clicked!",Toast.LENGTH_SHORT).show();
//                return false; // True to keep the Speed Dial open
//            }
//
//            @Override
//            public void onToggleChanged(boolean isOpen) {
//
//            }
//        });

        //Set option fabs clicklisteners.
        mSpeedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    //delete letter
                    case R.id.fab_delete_letter:
                        deleteLetter_hint_click();
                        mSpeedDialView.close(); // To close the Speed Dial with animation
                        return true; // false will close it without animation

                    //reveal word
                    case R.id.fab_reveal_word:
                        revealWord_hint_click();
                        mSpeedDialView.close(); // To close the Speed Dial with animation
                        return true; // false will close it without animation
                    //(same as mSpeedDialView.close(false); return false;)

//                    case R.id.fab_add_action:
//                        mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_replace_action,
//                                R.drawable.ic_replace_white_24dp)
//                                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color
//                                                .material_orange_500,
//                                        getTheme()))
//                                .setLabel(getString(R.string.label_replace_action))
//                                .create(), ADD_ACTION_POSITION);
//                        break;
//                    case R.id.fab_replace_action:
//                        mSpeedDialView.replaceActionItem(new SpeedDialActionItem.Builder(R.id
//                                .fab_remove_action,
//                                R.drawable.ic_delete_letter_24dp)
//                                .setLabel(getString(R.string.label_remove_action))
//                                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.inbox_accent,
//                                        getTheme()))
//                                .create(), ADD_ACTION_POSITION);
//                        break;
//                    case R.id.fab_remove_action:
//                        mSpeedDialView.removeActionItemById(R.id.fab_remove_action);
//                        break;

                    default:
                        break;
                }
                return true; // To keep the Speed Dial open
            }
        });

    }

    //peremeshivaem s randomom i zapolnyaem buttoni
    private void settingUpButtons ()
    {
        answer_playerName = playerName.toCharArray(); //casting true player name
        String fake_playersName; //строка, которая хранит ник + добавленные буквы
        int fake_letters_count; //количество добавляемых букв, зависит от длины ника
        //12, потому что оптимальный размер для RV_suggest (2 строки по 6)
        if (answer_playerName.length<12)
            //добавляем разницу между 12 и длиной ника
            fake_letters_count=12-answer_playerName.length;
        else fake_letters_count=18 - answer_playerName.length;
        fake_playersName = playerName + common.randomString(fake_letters_count); //adding true name to random chars
        fake_playersName = common.shuffleString(fake_playersName); //mixing
        char[] suggest_playerName = fake_playersName.toCharArray(); //making array of mixed
        mInterCont = new
                InteractionContent(new char[answer_playerName.length],//creating zagotovka dlya proverki
                                    new int[answer_playerName.length],//dlya zapominaniya mesta na kakom bila bukva
                                    Arrays.copyOf(suggest_playerName, suggest_playerName.length), //main manipulations in grid
                                0);//zapolnen li otvet?
//        common.user_submit_answer = new char[answer_playerName.length]; //creating zagotovka dlya proverki
//        common.positions = new int[answer_playerName.length]; //dlya zapominaniya mesta na kakom bila bukva
//        common.suggested_chars = Arrays.copyOf(suggest_playerName, suggest_playerName.length); //main manipulations in grid
//        common.isFull = 0;//zapolnen li otvet?
    }

    private void true_answer()
    {
        mRecycleView_suggest.setVisibility(View.INVISIBLE);
        answerAdapter.setOnItemClickListener(null);
        // Запisivaem данные
        ArrayList<Integer> getLevels_from_PREFS = new ArrayList<>();
        //ArrayList<Integer> getScore_from_PREFS = new ArrayList<>();//highscore

        //ecли уже существует сохранение - делаем get
        if (!tinydb.getListInt(game + "_level_" + level).isEmpty())
            getLevels_from_PREFS = tinydb.getListInt(game + "_level_" + level);

        //highscore
//	    if (!tinydb.getListInt(activity.getGame() + "_highscores").isEmpty())
//      getScore_from_PREFS= tinydb.getListInt(activity.getGame() + "_highscores");

        //если уровень не записан в сохранении
        if (!getLevels_from_PREFS.contains(_position))
        {
            getLevels_from_PREFS.add(_position);

            //highscore
//          int score_img = getScore_from_PREFS.get(activity.getLevel()-1);
//          getScore_from_PREFS.set(activity.getLevel()-1, (score_img+1000));
        }
        //записываем в сохранение
        tinydb.putListInt(game + "_level_" + level, getLevels_from_PREFS);

        //tinydb.putListInt(activity.getGame() + "_highscores", getScore_from_PREFS); //highscore
//	                    Toast.makeText(context,
//                                tinydb.getListInt(activity.getGame() + "_level_" + activity.getLevel()).toString(),
//                                Toast.LENGTH_SHORT).show();
//                        Toast.makeText(context,
//                                tinydb.getListInt(activity.getGame() + "_highscores").toString(),
//                                Toast.LENGTH_SHORT).show();

        //zapisivaem podskazku HINT
        //esli ne sushestvuet - sozdaem, esli sushestvuet - get
        if (tinydb.getInt("Hints") == 0) tinydb.putInt("Hints", 1);
        else
        {
            int hints_count = tinydb.getInt("Hints");
            tinydb.putInt("Hints", hints_count + 1);
        }
        ((TextView) activity.findViewById(R.id.tv_Hints)).setText(String.valueOf(tinydb.getInt("Hints")));

        //zapisivaem kolichestvo otgadanih
        //esli ne sushestvuet - sozdaem, esli sushestvuet - get
        int completed_count = 1;

        //pergame
        if (tinydb.getInt(game+"completed_count") == 0) tinydb.putInt(game+"completed_count", completed_count);
        else
        {
            completed_count = tinydb.getInt(game+"completed_count") + 1;
            tinydb.putInt(game+"completed_count", completed_count);
        }

        //total
        if (tinydb.getInt("total_completed_count") == 0) tinydb.putInt("total_completed_count", completed_count);
        else
        {
            completed_count = tinydb.getInt("total_completed_count") + 1;
            tinydb.putInt("total_completed_count", completed_count);
        }

        dialog = new Dialog(activity){    // Tap anywhere to close dialog.
            @Override
            public boolean onTouchEvent(@NonNull MotionEvent event) {
                this.dismiss();
                return true;
            }
            };

        dialog.setContentView(R.layout.dialog_true_answer);
        Window window = dialog.getWindow();
        Objects.requireNonNull(window).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setDimAmount(0.87f);
        dialog.show();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        //animation
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                (dialog.findViewById(R.id.tv_Hints_icon)).setVisibility(View.VISIBLE);
                (dialog.findViewById(R.id.tv_Hints_icon)).startAnimation(AnimationUtils.loadAnimation(activity, R.anim.dialog_bounce));
            }
        }, 200);
        (dialog.findViewById(R.id.tv_topText1)).startAnimation(AnimationUtils.loadAnimation(activity, R.anim.dialog_fade_right_in));
        (dialog.findViewById(R.id.tv_topText1)).startAnimation(AnimationUtils.loadAnimation(activity, R.anim.dialog_fade_left_in));
        TextView true_answer_was = dialog.findViewById(R.id.revealed_answer);
        true_answer_was.setText(String.valueOf(playerName));
        true_answer_was.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.dialog_scale));
        //
        (dialog.findViewById(R.id.butDiag_nextImg)).setOnClickListener(right_Click);
    }

    //подсказка "Убрать лишнюю букву"
	private void deleteLetter_hint_click ()
    {
        //если игрок не отгадан, то юзается подсказка
        if (!already_guessed)
        {
            int hints_count = tinydb.getInt("Hints");
            int hints_used = tinydb.getInt("hints_used");
            int hints_require = 1;
            char[] suggestedChars = mInterCont.getSuggested_chars();
            //proveryam est li podskazki
            if (hints_count!=0)
            {
                for (int j = i; j < suggestedChars.length; j++)
                {
                    if ((!playerName.contains(String.valueOf(suggestedChars[j]))    //esli takogo simvola net v pravilnom otvete
                            && suggestedChars[j] != 0)                                //esli pole ne pustoe
                            || (temp.contains(String.valueOf(suggestedChars[j]))       //esli simvol uje zapisan vo vremennoi massiv
                            && count(playerName, suggestedChars[j]) == count(temp, suggestedChars[j]))
                            //esli kolichestvo vhojdeniy v slovo = kolichestvu vhojdeniy v temporary massiv
                            )
                    {
                        suggestedChars[j] = 0;  //obnulyaem
                        i++;    //increment chtobi zanovo vse simvoli snova ne prohodit'
                        break;
                    } else
                    {
                        if (!temp.contains(String.valueOf(suggestedChars[j]))  //если символ не записан в темпорари массив
                                || count(playerName, suggestedChars[j]) != 1)    //или если уже записан, но количество записей не единичное
                        {
                            temp = temp + String.valueOf(suggestedChars[j]);    //dobavlyaem v temp stroku
                            i++;    //increment chtobi zanovo vse simvoli snova ne prohodit'
                        }
                    }
                }
                //obnovlyaem recycle s suggested chars
                suggestAdapter.notifyDataSetChanged();
                hints_count -= hints_require;
                tinydb.putInt("Hints",hints_count);
                tinydb.putInt("hints_used",(hints_used+hints_require));
                ((TextView) activity.findViewById(R.id.tv_Hints)).setText(String.valueOf(tinydb.getInt("Hints")));
            }
            else Toast.makeText(activity,
                    "Not enough coins",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(activity,
                    "You've already pass this player",Toast.LENGTH_SHORT).show();
    }

    private void revealWord_hint_click ()
    {
        if (!already_guessed)
        {
            int hints_count = tinydb.getInt("Hints");
            int hints_require = 3;
            int hints_used = tinydb.getInt("hints_used");
            //proveryam est li podskazki
            if (hints_count>=hints_require)
            {
                answerAdapter.notifyDataSetChanged();
                //obnovlyaem grid s suggested chars
                mRecycleView_suggest.setVisibility(View.INVISIBLE);
                hints_count -= hints_require;
                tinydb.putInt("Hints",hints_count);
                tinydb.putInt("hints_used",(hints_used+hints_require));
                true_answer();
            }
            else Toast.makeText(activity,
                    "Not enough coins",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(activity,
                    "You've already pass this player",Toast.LENGTH_SHORT).show();
    }

    //считает количество повторений определенного символа в строке
    //нужно для подсказки "убрать лишнюю букву"
    private int count(String s, char c) {
        return s.length()==0 ? 0 : (s.charAt(0)==c ? 1 : 0) + count(s.substring(1),c);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (dialog!=null && dialog.isShowing() )
            dialog.dismiss();
    }

    @Override
    public void onDestroyView ()
    {
        super.onDestroyView();
        (activity.findViewById(R.id.tv_Hints)).setVisibility(View.GONE);
    }

    private Recycler_adapter_answer.OnItemClickListener RecycleView_answer_OnItemClickListener = new Recycler_adapter_answer.OnItemClickListener() {
        @Override
        public void onItemClick (View view, int position)
        {
            //возвращаем букву на свое место в suggested letters
            char letter = answerAdapter.getLetter(position);
            if (mInterCont.getUser_submit_answer()[position] != 0 && letter != 0)
            {
                mInterCont.setUserSubmitAnswerItem(position, (char) 0);
                mInterCont.setSuggestedCharsItem(mInterCont.getPositions()[position],letter);
                mInterCont.incrementIsFull(-1);
            }

            //updating views
            answerAdapter.notifyItemChanged(position);
            suggestAdapter.notifyItemChanged(mInterCont.getPositions()[position]);
            //.
        }
    };

    private Recycler_adapter_suggest.OnItemClickListener RecycleView_suggest_OnItemClickListener = new Recycler_adapter_suggest.OnItemClickListener()
    {
        @Override
        public void onItemClick (View view, int position)
        {
            char letter = suggestAdapter.getLetter(position);
            for (int i = 0; i < answer_playerName.length; i++)
            {
                if (mInterCont.getUser_submit_answer()[i] == 0 && letter != 0)
                {
                    mInterCont.setUserSubmitAnswerItem(i,letter); //word container to store chosen letters
                    mInterCont.setPostitionsItem(i,position); //saving _position of the letter in grid
                    mInterCont.setSuggestedCharsItem(position, (char) 0); //making suggested letter "invisible" to select
                    mInterCont.incrementIsFull(1); //increment for checking if word is completed in length
                    //Toast.makeText(this,String.valueOf(letter),Toast.LENGTH_SHORT).show(); //test
                    //updating views
                    suggestAdapter.notifyItemChanged(position);
                    answerAdapter.notifyItemChanged(i);
                    //.
                    break;
                }
            }
            if (mInterCont.getIsFull() == answer_playerName.length)
                if (Arrays.equals(mInterCont.getUser_submit_answer(), answer_playerName))
                    true_answer();
                else
                    layout_main.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.wrong_answer_shake));

        }
    };

    private View.OnClickListener left_Click = new View.OnClickListener() {
        @Override
        public void onClick (View v)
        {
            if (_position > 0)
            {
                already_guessed = false;
                final int[] completed = Ints.toArray(tinydb.getListInt(game + "_level_" + level));
                for (int i : completed)
                    if (i == (_position-1))
                        already_guessed = true;

                PlayerContent_Object pl = new PlayerContent_Object(level_view_Grid_adapter_img._playersNameList[_position - 1],
                        level_view_Grid_adapter_img.imageId[_position - 1],(_position - 1),playersNameList_length,already_guessed);
                model.setPlayerContent(pl);
            } else
                activity.onBackPressed();
        }
    };

    private View.OnClickListener right_Click = new View.OnClickListener() {
        @Override
        public void onClick (View v)
        {
            if (_position != (playersNameList_length - 1))
            {
                already_guessed = false;
                final int[] completed = Ints.toArray(tinydb.getListInt(game + "_level_" + level));
                for (int i : completed)
                    if (i == (_position+1))
                        already_guessed = true;

                PlayerContent_Object pl = new PlayerContent_Object(level_view_Grid_adapter_img._playersNameList[_position + 1],
                        level_view_Grid_adapter_img.imageId[_position + 1],(_position + 1),playersNameList_length,already_guessed);

                model.setPlayerContent(pl);
                if (dialog!=null)
                    dialog.dismiss();
            }
            else
                activity.onBackPressed();
        }
    };

}