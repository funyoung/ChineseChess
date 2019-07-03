package com.funyoung.andchess.ChessModel;

import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.*;

/*
{
  "name": "default",
  "r": {
    "j": "9098", "m": "9197", "x": "9296", "s": "9395", "b": "94", "z": "6062646668"
  },
  "b": {
    "j": "0008", "m": "0107", "x": "0206", "s": "0305", "b": "04", "z": "0002040608"
  }
}
 */
public class ManualTest {
    private final Gson gson = new Gson();

    @Test
    public void parse_name_isCorrect() throws Exception {
        String jsonText = "{\"name\":\"default\"}";
        Manual manual = gson.fromJson(jsonText, Manual.class);
        assertEquals("default", manual.getName());

        jsonText = "{\"名字\":\"default\"}";
        manual = gson.fromJson(jsonText, Manual.class);
        assertEquals("default", manual.getName());
    }
}