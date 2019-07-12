package com.mmdev.cyberquiz.utils;

import android.content.Context;
import android.util.Log;

import com.mmdev.cyberquiz.R;
import com.mmdev.cyberquiz.data.LevelContent_Object;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class common
{
    static private JSONObject jsonObj;
    static private JSONObject jsonGame;

	public static String randomString (int count)
    {
        final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final SecureRandom RANDOM = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; ++i)
        {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    public static String shuffleString (String string)
    {
        StringBuilder sb = new StringBuilder(string.length());
        double rnd;
        for (char c : string.toCharArray())
        {
            rnd = Math.random();
            if (rnd < 0.34) sb.append(c);
            else if (rnd < 0.67) sb.insert(sb.length() / 2, c);
            else sb.insert(0, c);
        }
        return sb.toString();
    }

    //todo: read in async
    private static Writer readJson (Context context)
    {
        Writer writer = new StringWriter();
        try
        {
            InputStream is = context.getResources().openRawResource(R.raw.games_data);
            char[] buffer = new char[1024];
            try
            {
                Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                int n;
                while ((n = reader.read(buffer)) != -1)
                {
                    writer.write(buffer, 0, n);
                }

            } finally
            {
                is.close();
                writer.close();
            }

        } catch (Exception e) { e.printStackTrace();}
        return writer;
    }

    public static LevelContent_Object levelContent (Context context, String game, int level)
    {
        String jsonString = readJson(context).toString();
        LevelContent_Object level_contentObject;
        String[] playersNames = null;
        int[] playersImgs = null;
        try
        {
            jsonObj = new JSONObject(jsonString);
            jsonGame = jsonObj.getJSONObject(game);
            JSONObject jsonLevel = jsonGame.getJSONObject("level" + level);

            //nameslist
            JSONArray jsonArr = jsonLevel.getJSONArray("playersNames");
            playersNames = new String[jsonArr.length()];
            for (int i = 0; i < playersNames.length; i++)
            {
                playersNames[i] = jsonArr.optString(i);
            }

            //imageslist
            jsonArr = jsonLevel.getJSONArray("playersImgs");
            playersImgs = new int[jsonArr.length()];
            for (int i = 0; i < playersImgs.length; i++)
            {
                playersImgs[i] = context.getResources().getIdentifier(jsonArr.optString(i),
                        "drawable", context.getPackageName());
            }
            jsonObj = null;
            jsonGame = null;
            //Log.d("exception",);
        } catch (Exception e)
        {
            Log.d("exception", game);
            e.printStackTrace();
        }
        level_contentObject = new LevelContent_Object(playersNames,playersImgs);
        return level_contentObject;
    }

    public static int jsonTotalLevels(Context context,String game)
    {
        String jsonString = readJson(context).toString();
        int totalLevels = 0;
        try
        {
            jsonObj = new JSONObject(jsonString);
            jsonGame = jsonObj.getJSONObject(game);
            totalLevels = jsonGame.length();
            //Log.d("exception",);
        } catch (Exception e)
        {
            Log.d("exception", game);
            e.printStackTrace();
        }
        return totalLevels;
    }

    public static int playersPerGame (Context context,String game)
    {
        int total_images_all_levels = 0;
        for (int i = 0; i<jsonTotalLevels(context,game);i++)
            total_images_all_levels += levelContent(context,game,(i+1)).getPlayersNames().length;
        return total_images_all_levels;
    }

    public static int playersTotal (Context context)
    {
        String[] game = {"csgo","dota","pubg","lol"};
        int total_players_all_games = 0;
        for (int j=0; j<4;j++)
            for (int i = 0; i<jsonTotalLevels(context,game[j]);i++)
                total_players_all_games += levelContent(context,game[j],(i+1)).getPlayersNames().length;
        return total_players_all_games;
    }

}