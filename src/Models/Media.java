package Models;

import java.util.Comparator;

public class Media {
    private String name;
    private String path;
    private String icon;

    public Media(String name, String path, String icon){
        this.name = name;
        this.path = path;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public static Comparator<Media> naturalComparator = new Comparator<Media>() {

        public boolean hasDigits(String s) {
            if(s.matches(".*\\d.*")) {
                return true;
            }
            return false;
        }

        @Override
        public int compare(Media m1, Media m2) {
            String d1 = null;
            String d2 = null;
            if(hasDigits(m1.getName())){
                d1 = m1.getName();
            }
            if(hasDigits(m2.getName())) {
                d2 = m2.getName();
            }
            if(d1 == null && d2 != null) {
                return 1;
            }
            else if(d1 != null && d2 == null) {
                return -1;
            }
            else if(d1 == null && d2 == null) {
                return (int) m1.getName().compareTo(m2.getName());
            }
            else {
                String[] temp1 = d1.split("\\s");
                String[] temp2 = d2.split("\\s");
                String[] num1 = temp1[temp1.length - 1].split("\\.");
                String[] num2 = temp2[temp2.length - 1].split("\\.");

                if(Integer.parseInt(num1[0]) > Integer.parseInt(num2[0])) {
                    return -1;
                }
                else if(Integer.parseInt(num1[0]) < Integer.parseInt(num2[0])) {
                    return 1;
                }
                else {
                    return (int) m1.getName().compareTo(m2.getName());
                }
            }
        }
    };
}
