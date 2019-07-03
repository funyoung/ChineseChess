package com.funyoung.andchess.ChessModel;

import com.google.gson.annotations.SerializedName;

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
public class Manual {
    @SerializedName(value="name", alternate={"名字"})
    private String name;
    @SerializedName(value="r", alternate={"红", "红方"})
    private Side r;
    @SerializedName(value="b", alternate={"黑", "黑方"})
    private Side b;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Side getR() {
        return r;
    }

    public void setR(Side r) {
        this.r = r;
    }

    public Side getB() {
        return b;
    }

    public void setB(Side b) {
        this.b = b;
    }

    public static class Side {
        @SerializedName(value="j", alternate={"ju", "车"})
        private String j;
        @SerializedName(value="m", alternate={"ma", "马"})
        private String m;
        @SerializedName(value="x", alternate={"xiang", "相", "象"})
        private String x;
        @SerializedName(value="s", alternate={"shi", "仕", "士"})
        private String s;
        @SerializedName(value="b", alternate={"shuai", "jiang", "帅", "将"})
        private String b;
        @SerializedName(value="p", alternate={"pao", "炮"})
        private String p;
        @SerializedName(value="z", alternate={"zu", "bing", "兵", "卒"})
        private String z;

        public String getJ() {
            return j;
        }

        public void setJ(String j) {
            this.j = j;
        }

        public String getM() {
            return m;
        }

        public void setM(String m) {
            this.m = m;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public String getZ() {
            return z;
        }

        public void setZ(String z) {
            this.z = z;
        }
    }
}
