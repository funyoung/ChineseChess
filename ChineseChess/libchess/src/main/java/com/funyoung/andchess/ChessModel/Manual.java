package com.funyoung.andchess.ChessModel;

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
    private String name;
    private Side r;
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
        private String j;
        private String m;
        private String x;
        private String s;
        private String b;
        private String p;
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
